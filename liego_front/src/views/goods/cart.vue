<template>
    <el-header>
        <el-table
            v-loading="loading"
            element-loading-text="Loading"
            ref="multipleTable"
            :data="product"
            tooltip-effect="dark"
            @select="onTableSelect"
            @selection-change="handleSelectionChange"
            border
        >
            <el-table-column type="selection" width="55"></el-table-column>
            <el-table-column align="center" label="图片" width="120">
                <img
                    :src="product[0].descrip_img1"
                    style="width: 100px;height: 120px"
                />
            </el-table-column>
            <el-table-column align="center" label="商品名称" width="180">
                {{ product[0].gname }}
            </el-table-column>
            <el-table-column align="center" label="商品单价" width="180">
                {{ product[0].price }}
            </el-table-column>
            <el-table-column align="center" label="购买数量" width="200">
                <el-input-number
                    v-model="num"
                    @change="handleChange"
                    :min="0"
                    :max="product[0].count"
                ></el-input-number>
            </el-table-column>
        </el-table>

        <div align="center" class="totalmoney" style="margin-top: 20px;">
            订单金额：{{ total }}
            <el-button type="danger" style="margin-left: 20px" @click="submit()"
                >提交订单</el-button
            >
        </div>
    </el-header>
</template>

<script>
import { getUserName, getUid, getToken } from "@/utils/auth";

export default {
    data() {
        return {
            num: 1,
            product: [],
            multipleSelection: [],
            total: 0,
            Currentid: "",
            number: "",
            flag: false,
            loading: true,
            rush: []
        };
    },
    created() {
        this.fetchDataById();
    },

    watch: {
        product: function() {
            if (this.flag == true)
                this.$nextTick(function() {
                    this.$refs.multipleTable.toggleRowSelection(
                        this.product[0],
                        true
                    );
                });
        }
    },

    methods: {
        rerush() {
            this.product.push(this.rush);
            console.info(this.product);
            this.product.splice(1, 1);
        },

        onTableSelect(rows, row) {
            let selected = rows.length && rows.indexOf(row) !== -1;
            this.flag = selected; // true就是选中，0或者false是取消选中
        },

        handleSelectionChange(val) {
            this.total = 0;
            this.multipleSelection = val;
            this.multipleSelection.forEach(row => {
                this.total += row.price * this.num;
            });
        },

        handleChange(value) {
            console.log(value);
            this.rerush();
            this.total = 0;
            this.multipleSelection.forEach(row => {
                this.total += row.price * this.num;
            });
        },
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
            this.loading = true;
            //取得详情
            var gid = this.$route.params.gid;
            var vm = this;
            var data1 = {
                gid: gid
            };
            this.post("http://localhost:8080/ViewGoodsInfoW", data1).then(
                function(resp) {
                    var r = resp.data;
                    if (r.status == 0) {
                        vm.product = [];
                        vm.product.push(resp.data.data);
                        vm.rush.push(resp.data.data);
                        console.info(vm.product);
                    } else {
                        vm.loading = false;
                        vm.$router.push("/");
                    }
                }
            );

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
                        //跳转的页面写在此处
                        this.$router.push({
                            path: "/myorders"
                        });
                    }
                }, 1000);
            }
        },

        submit() {
            var vm = this;
            if (vm.total == 0) this.$message.error("请选择");
            else {
                var gid = this.$route.params.gid;
                var buyerid = getUid();
                var uid = vm.product[0].uid;
                console.info(buyerid);
                console.info(uid);

                if (buyerid == uid)
                    this.$message.error("自己不能购买自己的商品");
                else {
                    var data = {
                        gid: gid,
                        sellerID: uid,
                        buyerID: buyerid,
                        gcount: vm.product[0].count,
                        ocost: vm.total
                    };
                    this.post("http://localhost:8080/LunchOrder", data).then(
                        function(resp) {
                            console.info(resp.data);
                            if (resp.data.status == 0) {
                                vm.$message.success("请求成功");
                                vm.threeGo();
                            } else if (resp.data.status == 5) {
                                vm.$message.error("购买失败,请查看余额");
                            } else {
                                vm.$message.error("购买失败");
                            }
                        }
                    );
                }
            }
        }
    }
};
</script>

<style scoped>
.el-header {
    width: 100%;
}

.totalmoney {
    margin-left: 100px;
}

.line {
    float: right;
    width: 100%;
    height: 1px;
    margin-top: -0.5em;
    background: #d4c4c4;
    position: relative;
    text-align: center;
}

.el-table {
    margin: 5px 0 0 100px;
    width: 736px;
}
</style>
