<template>
    <el-container>
        <el-header> </el-header>

        <el-main v-loading="loading" element-loading-text="Loading">
            <el-row>
                <el-col
                    :span="8"
                    class="col1"
                    style="width: 320px;height: 320px"
                >
                    <div class="grid-content bg-purple">
                        <el-carousel>
                            <el-carousel-item v-for="img in imgs" :key="img">
                                <img :src="img" class="proimg" />
                            </el-carousel-item>
                        </el-carousel>
                    </div>
                </el-col>

                <el-col :span="12" style="margin-left:-30px;">
                    <div class="grid-content bg-purple-light">
                        <el-row>
                            <el-col :span="10" style="line-height: 15px">
                                <div class="grid-content bg-purple-dark">
                                    <h4>商品名称：{{ product.gname }}</h4>
                                </div>
                            </el-col>
                        </el-row>
                        <el-row>
                            <el-col :span="15" style="line-height: 15px">
                                <div class="grid-content bg-purple-dark">
                                    <a :href="'/#/user/' + product.uid"
                                        >查看卖家</a
                                    >
                                </div>
                            </el-col>
                        </el-row>
                        <el-row>
                            <el-col :span="15" style="height: 85px">
                                <div class="grid-content bg-purple-dark">
                                    商品描述：{{ product.descrip_text }}
                                </div>
                            </el-col>
                        </el-row>

                        <el-row>
                            <el-col :span="10">
                                <div class="grid-content bg-purple-dark">
                                    <h4>单价：{{ product.price }}￥</h4>
                                </div>
                            </el-col>
                        </el-row>

                        <el-row>
                            <el-col :span="17" style="margin-bottom: 10px">
                                <div class="grid-content bg-purple-dark">
                                    <h4>数目: {{ product.count }}</h4>
                                </div>
                            </el-col>
                        </el-row>

                        <el-row>
                            <el-col :span="10">
                                <el-button
                                    type="danger"
                                    @click="gotocart(product.gid)"
                                    :disabled="product.count < 1"
                                    >{{
                                        product.count < 1
                                            ? "已售罄"
                                            : "立即购买"
                                    }}
                                </el-button>
                            </el-col>
                        </el-row>
                    </div>
                </el-col>
            </el-row>
            <div class="message">
                <el-button type="text" @click="onsubmit()">我要留言</el-button>
            </div>
            <div class="table">
                <el-table
                    :data="msgs"
                    :show-header="false"
                    style="margin-top: 50px"
                    v-if="show"
                >
                    <el-table-column>
                        <template slot-scope="scope">
                            <div>
                                <a :href="'/#/user/' + scope.row.uid">{{
                                    scope.row.username
                                }}</a>
                                <div style="float: right">
                                    {{ scope.row.mtime }}
                                </div>
                            </div>
                            <div style="margin-left: 5px">
                                {{ scope.row.msg }}
                            </div>
                            <div>
                                <el-button
                                    type="text"
                                    style="color: #97a8be;float: right;line-height:2px;margin-top:-2px"
                                    @click="fetchReplyMessage(scope.row.mid)"
                                    >查看所有回复</el-button
                                >

                                <el-button
                                    type="text"
                                    style="color: #97a8be;line-height:2px;float: right;margin-top:-2px;margin-right: 20px"
                                    @click="onreply(scope.row.mid)"
                                    >回复</el-button
                                >
                            </div>
                            <div>
                                <el-table
                                    :data="scope.row.rpls"
                                    :show-header="false"
                                    v-if="showr"
                                >
                                    <el-table-column>
                                        <template slot-scope="scope1">
                                            <div>
                                                <a
                                                    :href="
                                                        '/#/user/' +
                                                            scope1.row.uid
                                                    "
                                                    >{{
                                                        scope1.row.username
                                                    }}</a
                                                >
                                                <div style="float: right">
                                                    {{ scope1.row.rtime }}
                                                </div>
                                            </div>
                                            <div style="margin-left: 5px">
                                                {{ scope1.row.msg }}
                                            </div>
                                        </template>
                                    </el-table-column>
                                </el-table>
                            </div>
                        </template>
                    </el-table-column>
                </el-table>
            </div>
        </el-main>
        <el-footer> </el-footer>
    </el-container>
</template>

<script>
import { getUid, getToken } from "@/utils/auth";
import { formatDateTime, addZero } from "@/utils/time";
export default {
    data() {
        return {
            product: {
                gid: "",
                descrip_img1: "",
                descrip_img2: "",
                descrip_img3: "",
                gname: "",
                price: "",
                count: "",
                descrip_text: ""
            },

            imgs: [],
            messages: {
                mid: "",
                msg: "",
                mtime: "",
                username: ""
            },
            msgs: [],
            rmessages: {
                mid: "",
                msg: "",
                rtime: "",
                username: ""
            },
            loading: true,
            disVisible: true,
            show: false,
            showr: false
        };
    },
    created() {
        this.fetchDataById();
    },
    methods: {
        post(url, data) {
            return new Promise((resolve, reject) => {
                var token = getToken();
                this.$axios({
                    method: "post",
                    url: url,
                    crossDomain: true,
                    data: data,
                    headers: { token: token }
                })
                    .then(response => {
                        // 成功返回
                        resolve({ data: response.data });
                    })
                    .catch(error => {
                        // 失败返回
                        console.log(error);
                        reject({ data: error });
                    });
            });
        },
        fetchDataById() {
            //取得详情
            var gid = this.$route.params.gid;
            var vm = this;
            var data = {
                gid: gid
            };
            this.axios({
                method: "post",
                url: "http://localhost:8080/ViewGoodsInfoW",
                crossDomain: true, // 同源策略
                data: data //请求的表单数据     //查询
            }).then(function(resp) {
                vm.loading = true;
                console.info(resp.data);
                var r = resp.data;
                if (r.status == 0) {
                    vm.product = resp.data.data;
                    if (resp.data.data.descrip_img1 != "")
                        vm.imgs.push(resp.data.data.descrip_img1);
                    if (resp.data.data.descrip_img2 != "")
                        vm.imgs.push(resp.data.data.descrip_img2);
                    if (resp.data.data.descrip_img3 != "")
                        vm.imgs.push(resp.data.data.descrip_img3);
                    vm.fetchMessage();
                } else {
                    vm.loading = false;
                    vm.$router.push("/");
                }
            });
        },

        fetchMessage() {
            var vm = this;
            var gid = this.$route.params.gid;
            var data = {
                gid: gid
            };
            vm.$axios({
                method: "post",
                url: "http://localhost:8080/ViewMsgs", //首页list接口
                crossDomain: true, // 同源策略
                data: data //请求的表单数据
            })
                .then(function(resp) {
                    console.info(resp.data);
                    var r = resp.data;
                    if (r.status == 0) {
                        vm.messages = r.data;
                        if (vm.messages.length == 0) vm.show = false;
                        else {
                            vm.show = true;
                            for (var i = 0; i < vm.messages.length; i++) {
                                vm.messages[i].mtime = new Date(
                                    vm.messages[i].mtime
                                ).toLocaleString();
                                vm.msgs.push({
                                    uid: vm.messages[i].uid,
                                    username: vm.messages[i].username,
                                    msg: vm.messages[i].msg,
                                    mid: vm.messages[i].mid,
                                    mtime: vm.messages[i].mtime,
                                    rpls: []
                                });
                            }
                            console.info(vm.messages);
                        }
                    }
                })
                .catch(err => {
                    console.info("报错的信息", err);
                });

            this.loading = false;
        },

        fetchReplyMessage(mid) {
            var vm = this;
            var mmid = mid;
            var data = {
                mid: mmid
            };
            this.axios({
                method: "post",
                url: "http://localhost:8080/ViewRpls", //首页list接口
                crossDomain: true, // 同源策略
                data: data //请求的表单数据
            })
                .then(function(resp) {
                    console.info(resp.data);
                    var r = resp.data;
                    if (r.status == 0) {
                        vm.rmessages = r.data;
                        if (vm.rmessages.length == 0) {
                            vm.showr = false;
                            vm.$message.warning("无回复");
                        }
                        vm.showr = true;
                        var mid = vm.rmessages[0].mid;
                        console.log(mid);
                        for (var i = 0; i < vm.msgs.length; i++) {
                            if (vm.msgs[i].mid == mid) {
                                vm.msgs[i].rpls.length = 0;
                                for (var j = 0; j < vm.rmessages.length; j++) {
                                    vm.rmessages[j].rtime = new Date(
                                        vm.rmessages[j].rtime
                                    ).toLocaleString();
                                    vm.msgs[i].rpls.push({
                                        uid: vm.rmessages[j].uid,
                                        rid: vm.rmessages[j].rid,
                                        msg: vm.rmessages[j].msg,
                                        rtime: vm.rmessages[j].rtime,
                                        username: vm.rmessages[j].username
                                    });
                                }
                                console.log(vm.msgs);
                            }
                        }
                        console.info(vm.msgs);
                    }
                })
                .catch(err => {
                    console.info("报错的信息", err);
                });
            vm.show = true;
        },
        handleChange(val) {
            console.log(val);
        },
        gotocart(gid) {
            this.$router.push("/goods/cart/" + gid);
        },

        threeGo() {
            const TIME_COUNT = 2;
            if (!this.timer) {
                this.number = TIME_COUNT;
                this.show = false;
                this.timer = setInterval(() => {
                    if (this.number > 0 && this.number <= TIME_COUNT) {
                        this.number--;
                    } else {
                        this.show = true;
                        clearInterval(this.timer);
                        this.timer = null;
                        //跳转的页面写在此处
                        this.$router.go(0);
                    }
                }, 1000);
            }
        },

        onsubmit() {
            this.$prompt("请输入留言", "提示", {
                confirmButtonText: "确定",
                cancelButtonText: "取消"
            })
                .then(({ value }) => {
                    var vm = this;
                    var gid = this.$route.params.gid;
                    var uid = getUid();
                    var msg = value;
                    var data = {
                        gid: gid,
                        uid: uid,
                        msg: msg
                    };
                    this.post("http://localhost:8080/LeaveMsg", data)
                        .then(function(resp) {
                            console.log(resp);
                            var r = resp.data;
                            if (r.status == 0) {
                                vm.$message.success("提交成功");
                                this.threeGo();
                            }
                        })
                        .catch(err => {
                            console.info("报错的信息", err.response.message);
                        });
                })
                .catch(() => {
                    this.$message({
                        type: "info",
                        message: "取消留言"
                    });
                });
        },

        onreply(mid) {
            this.$prompt("请输入回复", "提示", {
                confirmButtonText: "确定",
                cancelButtonText: "取消"
            })
                .then(({ value }) => {
                    var vm = this;
                    var mmid = mid;
                    console.info(mmid);
                    var uid = getUid();
                    var msg = value;
                    var data = {
                        mid: mmid,
                        uid: uid,
                        msg: msg
                    };
                    console.info(data);
                    this.post("http://localhost:8080/ReplyMsg", data)
                        .then(function(resp) {
                            console.log(resp);
                            var r = resp.data;
                            if (r.status == 0) {
                                vm.$message.success("回复成功");
                                this.threeGo();
                            }
                        })
                        .catch(err => {
                            console.info("报错的信息", err.response.message);
                        });
                })
                .catch(() => {
                    this.$message({
                        type: "info",
                        message: "取消回复"
                    });
                });
        }
    }
};
</script>

<style scoped>
.el-main {
    height: 50%;
    width: 86%;
    margin-left: 90px;
}
.table {
    margin-top: -5px;
    margin-left: 30px;
    width: 900px;
}

.el-header {
    text-align: right;
    line-height: 60px;
    width: 900px;
}

.el-footer {
    color: #333;
    line-height: 50px;
    width: 100%;
}

.el-scrollbar__wrap {
    overflow-x: hidden;
}

.el-row {
    margin-top: -5px;
    margin-left: 90px;
}
.el-col {
    border-radius: 4px;
    line-height: 22px;
    text-align: left;
}

.message {
    width: 310px;
    margin-top: -10px;
    float: right;
}

.proimg {
    width: 320px;
    height: 320px;
    text-align: center;
}

.bg-purple {
    width: 320px;
    background: #d3dce6;
}
.bg-purple-light {
}
.grid-content {
    border-radius: 4px;
    min-height: 36px;
}
.row-bg {
    padding: 10px 0;
    background-color: #f9fafc;
}
</style>
