import Vue from 'vue'
import Router from 'vue-router'
import { getUserAuth } from "@/utils/auth"
Vue.use(Router)

/* Layout */
import Layout from '@/layout'

/**
 * Note: sub-menu only appear when route children.length >= 1
 * Detail see: https://panjiachen.github.io/vue-element-admin-site/guide/essentials/router-and-nav.html
 *
 * hidden: true                   if set true, item will not show in the sidebar(default is false)
 * alwaysShow: true               if set true, will always show the root menu
 *                                if not set alwaysShow, when item has more than one children route,
 *                                it will becomes nested mode, otherwise not show the root menu
 * redirect: noRedirect           if set noRedirect will no redirect in the breadcrumb
 * name:'router-name'             the name is used by <keep-alive> (must set!!!)
 * meta : {
    roles: ['admin','editor']    control the page roles (you can set multiple roles)
    title: 'title'               the name show in sidebar and breadcrumb (recommend set)
    icon: 'svg-name'/'el-icon-x' the icon show in the sidebar
    breadcrumb: false            if set false, the item will hidden in breadcrumb(default is true)
    activeMenu: '/example/list'  if set path, the sidebar will highlight the path you set
  }
 */

/**
 * constantRoutes
 * a base page that does not have permission requirements
 * all roles can be accessed
 */


export var constantRoutes = [
    {
        path: '/login',
        component: () => import('@/views/login/login'),
        hidden: true
    },
    {
        path: '/register',
        component: () => import("@/views/login/register"),
        hidden: true
    },
    {
        path: '/forget',
        component: () => import("@/views/login/forget"),
        hidden: true
    },
    {
        path: '/404',
        component: () => import('@/views/404'),
        hidden: true
    },
    {
        path: '/goods',
        hidden: true,
        component: Layout,
        children: [
            {
                path: 'goodsinfo/:gid',      //获取gid
                name: 'goodsinfo',
                component: () => import('@/views/goods/goodsinfo'),
                meta: { title: '商品详情', icon: 'form' }
            }
        ]
    },
    {
        path: '/goods',
        hidden: true,
        component: Layout,
        children: [
            {
                path: 'cart/:gid',
                name: 'cart',
                component: () => import('@/views/goods/cart'),
                meta: { title: '下单', icon: 'table' }
            }
        ]
    },
    {
        path: '/',
        component: Layout,
        children: [{
            path: '/',
            name: 'store',
            component: () => import('@/views/goods/store'),
            meta: { title: '理易宝-商城', icon: 'dashboard' }
        }]
    },
    {
        path: '/personal',
        component: Layout,
        children: [
            {
                path: '/home',
                name: 'home',
                component: () => import('@/views/personal/person'),
                meta: { title: '个人中心', icon: 'user' }
            }
        ]
    },
    {
        path: '/personal',
        component: Layout,
        hidden: true,
        children: [
            {
                path: '/user/:uid',
                name: 'user',
                component: () => import('@/views/personal/user'),
                meta: { title: '用户信息' }
            }
        ]
    },
    {
        path: '/personal',
        component: Layout,
        children: [
            {
                path: '/self',
                name: 'self',
                component: () => import('@/views/personal/self'),
                meta: { title: '我的发布', icon: 'goods' }
            }
        ]
    },
    {
        path: '/personal',
        component: Layout,
        children: [
            {
                path: '/myorders',
                name: 'myorders',
                component: () => import('@/views/personal/myorders'),
                meta: { title: '我的订单', icon: 'order' }
            }
        ]
    },
    {
        path: '/personal',
        hidden: true,
        component: Layout,
        children: [
            {
                path: '/editgoods',
                name: 'editgoods',
                component: () => import('@/views/personal/editgoods'),
                meta: { title: '编辑', icon: 'order' }
            }
        ]
    },
    {
        path: '/monitor',
        name: "后台管理",
        meta: { title: '后台管理', icon: 'form' },
        component: Layout,
        children: [
            {
                path: '/users',
                name: '用户管理',
                component: () => import('@/views/manage/users'),
                meta: { title: '用户管理', icon: 'user' }
            },
            {
                path: '/goodss',
                name: '商品管理',
                component: () => import('@/views/manage/goods'),
                meta: { title: '商品管理', icon: 'goods' }
            },
            {
                path: '/message',
                name: '留言管理',
                component: () => import('@/views/manage/msgs'),
                meta: { title: '留言管理', icon: 'msg' }
            },
            {
                path: '/reply',
                name: '回复管理',
                component: () => import('@/views/manage/rpls'),
                meta: { title: '回复管理', icon: 'rpl' }
            },
            {
                path: '/orders',
                name: '订单管理',
                component: () => import('@/views/manage/orders'),
                meta: { title: '订单管理', icon: 'order' }
            }
        ]
    },

    { path: '*', redirect: '/', hidden: true }
];

const createRouter = () => new Router({
    // mode: 'history', // require service support
    scrollBehavior: () => ({ y: 0 }),
    routes: constantRoutes
})

const router = createRouter()

// Detail see: https://github.com/vuejs/vue-router/issues/1234#issuecomment-357941465
export function resetRouter() {
    const newRouter = createRouter()
    router.matcher = newRouter.matcher // reset router
}

export default router
