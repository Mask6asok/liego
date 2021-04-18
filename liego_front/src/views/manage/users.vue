<template>
    <!--    这里是显示用户管理列表的页面-->
    <div class="app-container">
        <center>
            <input
                type="text"
                class="el-input__inner"
                style="width: 500px"
                v-model="search"
                @input="searchAction($event)"
                placeholder="用户名/学号"
            />
        </center>
        <br />
        <div v-if="searchResult.length > 0">
            <el-table :data="searchResult" border fit highlight-current-row>
                <el-table-column align="center" label="序号" width="auto">
                    <template slot-scope="scope">
                        {{ scope.$index + 1 }}
                    </template>
                </el-table-column>
                <el-table-column align="center" label="用户id" width="auto">
                    <template slot-scope="scope">
                        {{ scope.row.uid }}
                    </template>
                </el-table-column>
                <el-table-column align="center" label="用户名" width="auto">
                    <template slot-scope="scope">
                        {{ scope.row.username }}
                    </template>
                </el-table-column>
                <el-table-column align="center" label="学号" width="auto">
                    <template slot-scope="scope">
                        {{ scope.row.sno }}
                    </template>
                </el-table-column>
                <el-table-column align="center" label="手机号" width="auto">
                    <template slot-scope="scope">
                        {{ scope.row.phone }}
                    </template>
                </el-table-column>
                <el-table-column
                    prop="balance"
                    sortable
                    align="center"
                    label="余额"
                    width="auto"
                >
                    <template slot-scope="scope">
                        {{ scope.row.balance }}
                    </template>
                </el-table-column>
                <el-table-column
                    label="操作"
                    align="center"
                    width="235"
                    class-name="small-padding fixed-width"
                >
                    <template slot-scope="scope">
                        <el-button
                            size="mini"
                            type="primary"
                            @click="charge(scope.row.uid)"
                            >充值
                        </el-button>
                        <el-button
                            size="mini"
                            type="danger"
                            @click="delUser(scope.row.uid, scope.row.username)"
                        >
                            删除
                        </el-button>
                    </template>
                </el-table-column>
            </el-table>
        </div>
        <div v-else>
            <el-table :data="list" border fit highlight-current-row>
                <el-table-column align="center" label="序号" width="auto">
                    <template slot-scope="scope">
                        {{ scope.$index + 1 }}
                    </template>
                </el-table-column>
                <el-table-column align="center" label="用户id" width="auto">
                    <template slot-scope="scope">
                        {{ scope.row.uid }}
                    </template>
                </el-table-column>
                <el-table-column align="center" label="用户名" width="auto">
                    <template slot-scope="scope">
                        {{ scope.row.username }}
                    </template>
                </el-table-column>
                <el-table-column align="center" label="学号" width="auto">
                    <template slot-scope="scope">
                        {{ scope.row.sno }}
                    </template>
                </el-table-column>
                <el-table-column align="center" label="手机号" width="auto">
                    <template slot-scope="scope">
                        {{ scope.row.phone }}
                    </template>
                </el-table-column>
                <el-table-column
                    prop="balance"
                    sortable
                    align="center"
                    label="余额"
                    width="auto"
                >
                    <template slot-scope="scope">
                        {{ scope.row.balance }}
                    </template>
                </el-table-column>

                <!--            <el-table-column align="center" label="用户权限" width="80">-->
                <!--                <template slot-scope="scope">-->
                <!--                    {{ scope.row.right}}-->
                <!--                </template>-->
                <!--            </el-table-column>-->

                <el-table-column
                    label="操作"
                    align="center"
                    width="235"
                    class-name="small-padding fixed-width"
                >
                    <template slot-scope="scope">
                        <el-button
                            size="mini"
                            type="primary"
                            @click="charge(scope.row.uid)"
                            >充值
                        </el-button>
                        <el-button
                            size="mini"
                            type="danger"
                            @click="delUser(scope.row.uid, scope.row.username)"
                        >
                            删除
                        </el-button>
                    </template>
                </el-table-column>
            </el-table>
        </div>
    </div>
</template>

<script>
import { throttle } from "@/utils/flow";
import { MessageBox } from "element-ui";
import { getToken } from "@/utils/auth";

export default {
    data() {
        return {
            list: [],
            number: "",
            searchResult: [],
            search: ""
        };
    },
    created() {
        this.fetchData();
    },
    methods: {
        searchAction: throttle(function(e) {
            const inputValue = e.target.value;
            this.searchResult = this.list.filter(user => {
                if (
                    user.username.includes(inputValue) ||
                    user.sno.includes(inputValue)
                ) {
                    return user;
                }
            });
        }, 500),

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
        editproduct(gid) {
            this.router.push("/edituser/index/" + uid);
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
        delUser(uid, name) {
            MessageBox.confirm("确定要删除用户【" + name + "】吗？", "理易购", {
                confirmButtonText: "确定",
                cancelButtonText: "取消",
                type: "warning"
            })
                .then(() => {
                    var data = { username: name };
                    this.post("http://localhost:8080/DelUser", data).then(
                        res => {
                            console.log(res.data);
                        }
                    );
                    this.$message({
                        type: "success",
                        message: "删除成功!"
                    });
                    this.threeGo();
                })
                .catch(() => {
                    this.$message({
                        type: "info",
                        message: "已取消删除"
                    });
                });
        },
        fetchData() {
            var vm = this;
            var data = {};
            this.post("http://localhost:8080/ViewUsers", data)
                .then(res => {
                    vm.list.splice(0, vm.list.length);
                    var data = res.data.data;
                    console.log(data);
                    this.list = data;
                })
                .catch(err => {
                    console.info("请求错误", err.response.message);
                });
        },

        charge(uid) {
            this.$prompt("请输入充值金额", "提示", {
                confirmButtonText: "确定",
                cancelButtonText: "取消",
                inputPattern: /^([1-9][0-9]*)$/,
                inputErrorMessage: "格式不正确"
            })
                .then(({ value }) => {
                    var vm = this;
                    var data = {
                        uid: uid,
                        balance: value
                    };
                    this.post("http://localhost:8080/Charge/", data)
                        .then(function(resp) {
                            console.log(resp);
                            var r = resp.data;
                            if (r.status == 0) {
                                vm.$message({
                                    type: "success",
                                    message: "充值成功!"
                                });
                                vm.threeGo();
                            }
                        })
                        .catch(err => {
                            console.info("报错的信息", err.response.message);
                        });
                })
                .catch(() => {
                    this.$message({
                        type: "info",
                        message: "取消充值"
                    });
                });
        }
    }
};
</script>
