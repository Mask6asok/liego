<template>
    <!--    这里是显示商品管理列表的地方-->
    <div class="app-container">
        <el-table :data="list" border fit highlight-current-row>
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
                    <a :href="'/#/goods/goodsinfo/' + scope.row.gid">{{
                        scope.row.gid
                    }}</a>
                </template>
            </el-table-column>
            <el-table-column align="center" label="卖方id" width="150%">
                <template slot-scope="scope">
                    <a :href="'/#/goods/goodsinfo/' + scope.row.sellerID">{{
                        scope.row.sellerID
                    }}</a>
                </template>
            </el-table-column>
            <el-table-column align="center" label="买方id" width="150%">
                <template slot-scope="scope">
                    <a :href="'/#/goods/goodsinfo/' + scope.row.buyerID">{{
                        scope.row.buyerID
                    }}</a>
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
                sortable
                prop="status"
                align="center"
                label="交易状态"
                width="250%"
            >
                <template slot-scope="scope">
                    {{ scope.row.statusm }}
                </template>
            </el-table-column>
            <el-table-column
                sortable
                prop="time"
                align="center"
                label="更新时间"
                width="250%"
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
    </div>
</template>

<script>
import { getToken, getUid } from "@/utils/auth";
export default {
    data() {
        return {
            list: [],
            msgs: ["已下单", "已处理", "已完成"]
        };
    },
    created() {
        this.fetchData();
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
        fetchData() {
            var vm = this;
            var data = {};
            this.post("http://localhost:8080/ViewOrders", data)
                .then(res => {
                    vm.list = res.data.data;
                    for (var i = 0; i < vm.list.length; i++) {
                        vm.list[i].statusm = this.msgs[vm.list[i].status];
                    }
                    console.log(vm.list);
                })
                .catch(err => {
                    console.info("请求错误", err);
                });
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
