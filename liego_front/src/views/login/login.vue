<template>
    <div class="login-container">
        <el-form
            ref="loginForm"
            :model="loginForm"
            :rules="loginRules"
            class="login-form"
            auto-complete="on"
            label-position="left"
        >
            <div class="title-container">
                <h3 class="title">用户登陆</h3>
            </div>
            <el-form-item prop="username">
                <span class="svg-container">
                    <svg-icon icon-class="user" />
                </span>
                <el-input
                    ref="username"
                    v-model="loginForm.username"
                    placeholder="Username"
                    name="username"
                    type="text"
                    tabindex="1"
                    auto-complete="on"
                    maxlength="14"
                />
            </el-form-item>
            <el-form-item prop="password">
                <span class="svg-container">
                    <svg-icon icon-class="password" />
                </span>
                <el-input
                    :key="passwordType"
                    ref="password"
                    v-model="loginForm.password"
                    :type="passwordType"
                    placeholder="Password"
                    name="password"
                    tabindex="2"
                    auto-complete="on"
                    @keyup.enter.native="handleLogin"
                    maxlength="20"
                />

                <span class="show-pwd" @click="showPwd">
                    <svg-icon
                        :icon-class="
                            passwordType === 'password' ? 'eye' : 'eye-open'
                        "
                    />
                </span>
            </el-form-item>
            <el-form-item prop="code" class="code">
                <span class="svg-container">
                    <svg-icon icon-class="example" />
                </span>
                <el-input
                    type="text"
                    id="code"
                    v-model="loginForm.code"
                    placeholder="Verification"
                    oninput="value=value.replace(/[^\d]/g, '')"
                    maxlength="4"
                />
            </el-form-item>
            <div class="login-code" @click="refreshCode">
                <!--验证码组件-->
                <s-identify :identifyCode="identifyCode"></s-identify>
            </div>

            <el-button
                :loading="loading"
                type="primary"
                style="width:100%;margin-bottom:30px;"
                @click.native.prevent="handleLogin"
                >登陆
            </el-button>
            <div class="tips">
                <span><a href="/#/forget">忘记密码</a></span>
                <span><a href="/#/register">注册用户</a></span>
            </div>
        </el-form>
    </div>
</template>

<script>
import { getToken, setToken, removeToken } from "@/utils/auth";
import { encode, decode } from "js-base64";
import { resetRouter } from "@/router";
import SIdentify from "@/sidentify/sidentify";

export default {
    components: { SIdentify },
    name: "Login",
    data() {
        const validatePassword = (rule, value, callback) => {
            if (value.length < 6) {
                callback(
                    new Error("The password can not be less than 6 digits")
                );
            } else {
                callback();
            }
        };
        return {
            loginForm: {
                username: "",
                password: "",
                code: ""
            },
            identifyCodes: "1234567890",
            identifyCode: "",
            loginRules: {
                username: [
                    {
                        required: true,
                        trigger: "blur"
                        // validator: validateUsername
                    }
                ],
                password: [
                    {
                        required: true,
                        trigger: "blur",
                        validator: validatePassword
                    }
                ],
                code: [
                    {
                        required: true,
                        trigger: "blur"
                    }
                ]
            },
            loading: false,
            passwordType: "password",
            redirect: undefined
        };
    },
    created() {
        this.refreshCode();
    },

    watch: {
        $route: {
            handler: function(route) {
                this.redirect = route.query && route.query.redirect;
            },
            immediate: true
        }
    },
    methods: {
        randomNum(min, max) {
            return Math.floor(Math.random() * (max - min) + min);
        },

        refreshCode() {
            this.identifyCode = "";
            this.makeCode(this.identifyCodes, 4);
        },
        makeCode(o, l) {
            for (let i = 0; i < l; i++) {
                this.identifyCode += this.identifyCodes[
                    this.randomNum(0, this.identifyCodes.length)
                ];
            }
            console.log(this.identifyCode);
        },
        showPwd() {
            if (this.passwordType === "password") {
                this.passwordType = "";
            } else {
                this.passwordType = "password";
            }
            this.$nextTick(() => {
                this.$refs.password.focus();
            });
        },
        handleLogin() {
            if (
                this.loginForm.username == "" ||
                this.loginForm.password == "" ||
                this.loginForm.code == ""
            )
                this.$message.error("输入不能为空");
            else {
                if (this.loginForm.code != this.identifyCode)
                    this.$message.error("验证码错误");
                else {
                    this.loading = true;
                    var data = {
                        username: this.loginForm.username,
                        password: this.loginForm.password
                    };
                    this.$axios({
                        url: "http://localhost:8080/LoginUser", //请求的地址
                        method: "post", //请求的方式
                        crossDomain: true,
                        data: data //请求的表单数据
                    })
                        .then(res => {
                            var r = res.data;
                            if (r.status == 0) {
                                // 登陆成功的标志
                                setToken(res.data.data.token); // 保存token
                                this.$router.push({
                                    path: this.redirect || "/home"
                                });
                                this.loading = false;
                            } else {
                                this.$message.error("用户名或密码错误");
                            }
                        })
                        .catch(err => {
                            console.info("报错的信息", err);
                        });
                }
            }
        }
    }
};
</script>

<style lang="scss">
/* 修复input 背景不协调 和光标变色 */
/* Detail see https://github.com/PanJiaChen/vue-element-admin/pull/927 */

$bg: #283443;
$light_gray: #fff;
$cursor: #fff;

@supports (-webkit-mask: none) and (not (cater-color: $cursor)) {
    .login-container .el-input input {
        color: $cursor;
    }
}

/* reset element-ui css */
.login-container {
    .el-input {
        display: inline-block;
        height: 47px;
        width: 85%;

        input {
            background: transparent;
            border: 0px;
            -webkit-appearance: none;
            border-radius: 0px;
            padding: 12px 5px 12px 15px;
            color: $light_gray;
            height: 47px;
            caret-color: $cursor;

            &:-webkit-autofill {
                box-shadow: 0 0 0px 1000px $bg inset !important;
                -webkit-text-fill-color: $cursor !important;
            }
        }
    }

    .el-form-item {
        border: 1px solid rgba(255, 255, 255, 0.1);
        background: rgba(0, 0, 0, 0.1);
        border-radius: 5px;
        color: #454545;
    }
}
</style>

<style lang="scss" scoped>
$bg: #2d3a4b;
$dark_gray: #889aa4;
$light_gray: #eee;
.code {
    width: 70%;
}
.login-code {
    float: right;
    margin-top: -68px;
}
.login-container {
    min-height: 100%;
    width: 100%;
    background-color: $bg;
    overflow: hidden;

    .login-form {
        position: relative;
        width: 520px;
        max-width: 100%;
        padding: 160px 35px 0;
        margin: 0 auto;
        overflow: hidden;
    }

    .tips {
        font-size: 14px;
        color: #fff;
        margin-bottom: 10px;

        span {
            &:first-of-type {
                margin-right: 16px;
            }
        }
    }

    .svg-container {
        padding: 6px 5px 6px 15px;
        color: $dark_gray;
        vertical-align: middle;
        width: 30px;
        display: inline-block;
    }

    .title-container {
        position: relative;

        .title {
            font-size: 26px;
            color: $light_gray;
            margin: 0px auto 40px auto;
            text-align: center;
            font-weight: bold;
        }
    }

    .show-pwd {
        position: absolute;
        right: 10px;
        top: 7px;
        font-size: 16px;
        color: $dark_gray;
        cursor: pointer;
        user-select: none;
    }
}
</style>
