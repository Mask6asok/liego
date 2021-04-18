<template>
    <div class="reg-container">
        <el-form
            ref="regForm"
            :model="regForm"
            :rules="regRules"
            class="reg-form"
            auto-complete="on"
            label-position="left"
        >
            <div class="title-container">
                <h3 class="title">重置密码</h3>
            </div>
            <el-form-item prop="username">
                <span class="svg-container">
                    <svg-icon icon-class="user" />
                </span>
                <el-input
                    ref="username"
                    v-model="regForm.username"
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
                    v-model="regForm.password"
                    :type="passwordType"
                    placeholder="Password"
                    name="password"
                    tabindex="2"
                    auto-complete="on"
                    @keyup.enter.native="handlereg"
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
            <el-form-item prop="sno">
                <span class="svg-container">
                    <svg-icon icon-class="sno" />
                </span>
                <el-input
                    ref="sno"
                    v-model="regForm.sno"
                    placeholder="verify sno"
                    name="sno"
                    type="text"
                    tabindex="3"
                    auto-complete="on"
                    maxlength="12"
                    oninput="value=value.replace(/[^\d]/g, '')"
                />
            </el-form-item>
            <el-form-item prop="phone">
                <span class="svg-container">
                    <svg-icon icon-class="phone" />
                </span>
                <el-input
                    ref="phone"
                    v-model="regForm.phone"
                    placeholder="verify phone"
                    name="phone"
                    type="text"
                    tabindex="4"
                    auto-complete="on"
                    oninput="value=value.replace(/[^\d]/g, '')"
                    maxlength="11"
                />
            </el-form-item>
            <el-button
                :loading="loading"
                type="primary"
                style="width:100%;margin-bottom:30px;"
                @click.native.prevent="handleReg"
                >注册
            </el-button>
        </el-form>
    </div>
</template>

<script>
import { getToken, setToken, removeToken } from "@/utils/auth";
import { encode, decode } from "js-base64";
import { adminRouter } from "@/router";
export default {
    name: "reg",
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
            regForm: {
                username: "",
                password: "",
                sno: "",
                phone: ""
            },
            regRules: {
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
                sno: [
                    {
                        required: true,
                        trigger: "blur"
                        // validator: validateUsername
                    }
                ],
                phone: [
                    {
                        required: true,
                        trigger: "blur"
                        // validator: validateUsername
                    }
                ]
            },
            loading: false,
            passwordType: "password",
            redirect: undefined
        };
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
        handleReg() {
            if (
                this.regForm.username == "" ||
                this.regForm.password == "" ||
                this.regForm.sno == "" ||
                this.regForm.phone == ""
            )
                this.$message.error("输入不能为空");
            else {
                this.loading = true;
                this.$axios({
                    url: "http://localhost:8080/ResetUser", //请求的地址
                    method: "post", //请求的方式
                    data: this.regForm, //请求的表单数据
                    crossDomain: true
                })
                    .then(res => {
                        console.log(res);
                        var r = res.data.status;
                        if (r == 0) {
                            this.$message({
                                message: "重置成功",
                                type: "success"
                            });
                            console.info(res.data);
                            this.$router.push("/login");
                            this.loading = false;
                        } else {
                            this.$message.error(res.data.message);
                            // this.$router.go(0);
                            this.loading = false;
                        }
                    })
                    .catch(err => {
                        console.info("报错的信息", err);
                    });
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
    .reg-container .el-input input {
        color: $cursor;
    }
}

/* reset element-ui css */
.reg-container {
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

.reg-container {
    min-height: 100%;
    width: 100%;
    background-color: $bg;
    overflow: hidden;

    .reg-form {
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
