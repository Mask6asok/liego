<template>
    <!--    这里是显示商品管理列表的地方-->
    <div class="app-container">
        <el-tabs v-model="activeName" type="card">
            <el-tab-pane label="我卖出的" name="first">
                <el-table
                    :data="list1"
                    border
                    fit
                    highlight-current-row
                    v-loading="loading"
                    element-loading-text="Loading"
                >
                    <el-table-column align="center" label="序号" width="100%">
                        <template slot-scope="scope">
                            {{ scope.$index + 1 }}
                        </template>
                    </el-table-column>
                    <el-table-column align="center" label="订单id" width="100%">
                        <template slot-scope="scope">
                            {{ scope.row.orderid }}
                        </template>
                    </el-table-column>
                    <el-table-column align="center" label="商品id" width="150%">
                        <template slot-scope="scope">
                            {{ scope.row.gid }}
                        </template>
                    </el-table-column>
                    <el-table-column align="center" label="卖方id" width="150%">
                        <template slot-scope="scope">
                            {{ scope.row.sellerID }}
                        </template>
                    </el-table-column>
                    <el-table-column align="center" label="买方id" width="150%">
                        <template slot-scope="scope">
                            {{ scope.row.buyerID }}
                        </template>
                    </el-table-column>
                    <el-table-column align="center" label="数量" width="150%">
                        <template slot-scope="scope">
                            {{ scope.row.gcount }}
                        </template>
                    </el-table-column>
                    <el-table-column align="center" label="总价" width="150%">
                        <template slot-scope="scope">
                            {{ scope.row.ocost }}
                        </template>
                    </el-table-column>
                    <el-table-column
                        prop="status"
                        sortable
                        align="center"
                        label="交易状态"
                        width="auto"
                    >
                        <template slot-scope="scope">
                            {{ scope.row.statusm }}
                        </template>
                    </el-table-column>
                    <el-table-column
                        prop="time"
                        sortable=""
                        align="center"
                        label="更新时间"
                        width="auto"
                    >
                        <template slot-scope="scope">
                            {{ new Date(scope.row.time).toLocaleString() }}
                        </template>
                    </el-table-column>
                    <el-table-column
                        label="操作"
                        align="center"
                        width="auto"
                        class-name="small-padding fixed-width"
                    >
                        <template slot-scope="scope">
                            <el-button
                                size="mini"
                                type="danger"
                                :disabled="!(scope.row.status == 0)"
                                @click="process(scope.row.orderid)"
                            >
                                处理
                            </el-button>
                        </template>
                    </el-table-column>
                </el-table>
            </el-tab-pane>
            <el-tab-pane label="我买到的" name="second">
                <el-table :data="list2" border fit highlight-current-row>
                    <el-table-column align="center" label="序号" width="100%">
                        <template slot-scope="scope">
                            {{ scope.$index + 1 }}
                        </template>
                    </el-table-column>
                    <el-table-column align="center" label="订单id" width="100%">
                        <template slot-scope="scope">
                            {{ scope.row.orderid }}
                        </template>
                    </el-table-column>
                    <el-table-column align="center" label="商品id" width="150%">
                        <template slot-scope="scope">
                            {{ scope.row.gid }}
                        </template>
                    </el-table-column>
                    <el-table-column align="center" label="卖方id" width="150%">
                        <template slot-scope="scope">
                            {{ scope.row.sellerID }}
                        </template>
                    </el-table-column>
                    <el-table-column align="center" label="买方id" width="150%">
                        <template slot-scope="scope">
                            {{ scope.row.buyerID }}
                        </template>
                    </el-table-column>
                    <el-table-column align="center" label="数量" width="150%">
                        <template slot-scope="scope">
                            {{ scope.row.gcount }}
                        </template>
                    </el-table-column>
                    <el-table-column align="center" label="总价" width="150%">
                        <template slot-scope="scope">
                            {{ scope.row.ocost }}
                        </template>
                    </el-table-column>
                    <el-table-column
                        prop="status"
                        sortable
                        align="center"
                        label="交易状态"
                        width="auto"
                    >
                        <template slot-scope="scope">
                            {{ scope.row.statusm }}
                        </template>
                    </el-table-column>
                    <el-table-column
                        prop="time"
                        sortable=""
                        align="center"
                        label="更新时间"
                        width="auto"
                    >
                        <template slot-scope="scope">
                            {{ new Date(scope.row.time).toLocaleString() }}
                        </template>
                    </el-table-column>
                    <el-table-column
                        label="操作"
                        align="center"
                        width="auto"
                        class-name="small-padding fixed-width"
                    >
                        <template slot-scope="scope">
                            <el-button
                                size="mini"
                                type="danger"
                                :disabled="!(scope.row.status == 1)"
                                @click="finish(scope.row.orderid)"
                            >
                                完成
                            </el-button>
                        </template>
                    </el-table-column>
                </el-table>
            </el-tab-pane>
        </el-tabs>
    </div>
</template>

<script>
import { getUid, getToken } from "@/utils/auth";
import { formatDateTime } from "@/utils/time";
export default {
    data() {
        return {
            list1: [],
            list2: [],
            activeName: "first",
            msgs: ["已下单", "已处理", "已完成"],
            number: "",
            loading: true
        };
    },
    created() {
        this.fetchDataSell();
        this.fetchDataBuy();
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
        fetchDataSell() {
            this.loading = true;
            var vm = this;
            var uid = getUid();
            var data = {
                uid: uid
            };
            this.post("http://localhost:8080/ViewOrdersBySeller", data)
                .then(res => {
                    vm.list1 = res.data.data;
                    for (var i = 0; i < vm.list1.length; i++) {
                        vm.list1[i].statusm = this.msgs[vm.list1[i].status];
                    }
                    console.log(vm.list1);
                })
                .catch(err => {
                    console.info("请求错误", err.response.message);
                });
            this.loading = false;
        },
        fetchDataBuy() {
            this.loading = true;
            var vm = this;
            var uid = getUid();
            var data = {
                uid: uid
            };
            this.post("http://localhost:8080/ViewOrdersByBuyer", data)
                .then(res => {
                    vm.list2 = res.data.data;
                    for (var i = 0; i < vm.list2.length; i++) {
                        vm.list2[i].statusm = this.msgs[vm.list2[i].status];
                    }
                    console.log(vm.list2);
                })
                .catch(err => {
                    console.info("请求错误", err.response.message);
                });
            this.loading = false;
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
                        this.$router.go(0);
                    }
                }, 1000);
            }
        },
        process(oid) {
            var uid = getUid();
            var data = {
                uid: uid,
                oid: oid
            };
            var vm = this;
            console.log(data);
            this.post("http://localhost:8080/ProcessOrder", data)
                .then(function(resp) {
                    console.log(resp);
                    var r = resp.data;
                    if (r.status == 0) {
                        console.log(this);
                        vm.$message({
                            type: "success",
                            message: "处理成功!"
                        });
                        vm.threeGo();
                    } else {
                        vm.$message({
                            type: "warning",
                            message: "处理失败!"
                        });
                    }
                })
                .catch(err => {
                    console.info("请求错误", err);
                });
        },
        finish(oid) {
            var uid = getUid();
            var data = {
                oid: oid,
                uid: uid
            };
            var vm = this;
            this.post("http://localhost:8080/FinishOrder", data)
                .then(function(resp) {
                    console.log(resp);
                    var r = resp.data;
                    if (r.status == 0) {
                        vm.$message({
                            type: "success",
                            message: "处理成功!"
                        });
                        vm.threeGo();
                    } else {
                        vm.$message({
                            type: "warning",
                            message: "处理失败!"
                        });
                    }
                })
                .catch(err => {
                    console.info("请求错误", err.response.message);
                });
        }
    }
};
</script>
