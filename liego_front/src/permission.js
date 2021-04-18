import router from './router'
import store from './store'
import { Message } from 'element-ui'
import NProgress from 'nprogress' // progress bar
import 'nprogress/nprogress.css' // progress bar style
import { getToken } from '@/utils/auth' // get token from cookie
import getPageTitle from '@/utils/get-page-title'
import { encode, decode } from "js-base64";


NProgress.configure({ showSpinner: false }) // NProgress Configuration

const whiteList = ['/login', '/', '/register', '/goods/goodsinfo/(.*?)', '/forget', 'reset', '/user/(.*?)']      // 无须登陆的
const blackList = ['/users', '/goodss', '/message', '/reply', '/orders']                        // 需要管理员的

function getUserName(token) {
    var payload = String(token).split(".")[1]
    var name = JSON.parse(decode(payload)).username
    console.log("username", name)
    return name
}

function getUserAuth(token) {
    var payload = String(token).split(".")[1]
    var auth = JSON.parse(decode(payload)).authority
    console.log("authority", auth)
    return auth
}

function hit(path) {
    var hitt = false
    for (var i = 0; i < whiteList.length; i++) {
        if (whiteList[i] == "/") {
            if (path == "/") {
                hitt = true
                break
            }
        } else if (path.search(whiteList[i]) != -1) {
            hitt = true
            break
        }
    }
    return hitt
}


router.beforeEach(async (to, from, next) => {
    NProgress.start()
    document.title = getPageTitle(to.meta.title)
    const hasToken = getToken()
    console.log(to.path)
    console.log(hasToken)
    if (hasToken) {                         // 从本地存储取出token，判断非空
        // console.log(getUserName(hasToken))
        if (to.path === '/login') {         // 在有token的情况下，无需登陆
            next({ path: '/' })
            NProgress.done()
        } else {                            // 访问非登陆界面，判断token合法性，源代码是通过获取UserInfo，还没有具体的修改
            const hasGetUserInfo = getUserName(hasToken)    // 预期解是从token的第二段提取信息
            if (hasGetUserInfo) {           // token含有用户信息，但是未鉴权
                if (blackList.indexOf(to.path) != -1) {     // 此处需要针对管理功能进行authority鉴权
                    if (getUserAuth(hasToken) == 0) {       // 具备管理员权限
                        next()                              // 放行
                    } else {
                        Message.error("权限错误，无法访问！")
                        next("/")
                        NProgress.done()
                    }
                } else {
                    next()
                }
            } else {
                await store.dispatch('user/resetToken')
                Message.error(error || 'Has Error')
                next(`/login?redirect=${to.path}`)
                NProgress.done()
            }
        }
    } else {
        // 本地没有token
        if (hit(to.path)) { // 无需token要求的白名单
            next() // 白名单放行
        } else {
            // 白名单外页面没有token需要登陆才能访问
            next(`/login?redirect=${to.path}`)
            NProgress.done()
        }
    }
})

router.afterEach(() => {
    // finish progress bar
    NProgress.done()
})
