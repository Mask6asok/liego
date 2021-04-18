<template>
    <div class="app-container">
        <div v-loading="loading" element-loading-text="Loading">
            <el-row>
                <el-col :span="5">
                    <el-table :data="user">
                        <el-table-column label="个人信息" prop="da">
                            <svg-icon icon-class="password" />
                            <template slot-scope="scope">
                                <el-row>
                                    <el-col :span="12">{{
                                        scope.row.da
                                    }}</el-col>
                                    <el-col :span="12">{{
                                        scope.row.db
                                    }}</el-col>
                                </el-row>
                            </template>
                        </el-table-column>
                    </el-table>
                </el-col>
                <el-col :span="5" offset="1">
                    <el-table :data="data1">
                        <el-table-column align="center" label="我发布的">
                            <template slot-scope="scope">
                                <a @click="goto(scope.row.gid)">{{
                                    scope.row.gname
                                }}</a>
                            </template>
                        </el-table-column>
                    </el-table>
                </el-col>
                <el-col :span="5" offset="1">
                    <el-table :data="data2">
                        <el-table-column
                            align="center"
                            prop="da"
                            label="我卖出的"
                        >
                            <template slot-scope="scope">
                                <a @click="goto(scope.row.gid)">{{
                                    scope.row.gname
                                }}</a>
                            </template>
                        </el-table-column>
                    </el-table>
                </el-col>
                <el-col :span="5" offset="1">
                    <el-table :data="data3" max-height="250">
                        <el-table-column
                            align="center"
                            prop="da"
                            label="我购买的"
                        >
                            <template slot-scope="scope">
                                <a @click="goto(scope.row.gid)">{{
                                    scope.row.gname
                                }}</a>
                            </template>
                        </el-table-column>
                    </el-table>
                </el-col>
            </el-row>
        </div>
    </div>
</template>
<script>
import { getUserName, getUid, getToken } from "@/utils/auth";
export default {
    data() {
        return {
            user: [],
            data1: [],
            data2: [],
            data3: [],
            loading: true
        };
    },
    created() {
        this.loading = true;
        this.fetchUserInfo();
        this.fetchGoodsPub();
        this.fetchGoodsSell();
        this.fetchGoodsBuy();
    },
    methods: {
        post(url, data) {
            return new Promise((resolve, reject) => {
                var token = getToken();
                this.$axios({
                    method: "post",
                    url: url,
                    // crossDomain: true,
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
        fetchUserInfo() {
            var uname = getUserName();
            var data = { username: uname };
            this.post("http://localhost:8080/ViewUser", data).then(res => {
                this.user.splice(0, this.user.length);
                this.user.push({ da: "用户ID", db: res.data.data.uid });
                this.user.push({ da: "用户名", db: res.data.data.username });
                this.user.push({ da: "学号", db: res.data.data.sno });
                this.user.push({ da: "手机号", db: res.data.data.phone });
                this.user.push({ da: "余额", db: res.data.data.balance });
            });
        },
        fetchGoodsPub() {
            var uid = getUid();
            var data = { uid: uid };
            this.post(
                "http://localhost:8080/ViewGoodsInfosByUid",
                data
            ).then(res => {
                this.data1.splice(0, this.data1.length);
                data = res.data.data;
                console.log(data);
                for (var i = 0; i < data.length; i++) {
                    if (data[i] != null) this.data1.push(data[i]);
                }
            });
        },
        fetchGoodsSell() {
            var uid = getUid();
            var data = { uid: uid };
            this.post("http://localhost:8080/ViewGoodsBySeller", data)
                .then(res => {
                    // console.log(res.data);
                    this.data2.splice(0, this.data2.length);
                    data = res.data.data;
                    console.log(data);
                    for (var i = 0; i < data.length; i++) {
                        if (data[i] != null) this.data2.push(data[i]);
                    }
                })
                .catch(err => {
                    console.log(err);
                });
        },
        fetchGoodsBuy() {
            var uid = getUid();
            var data = { uid: uid };
            this.post("http://localhost:8080/ViewGoodsByBuyer", data)
                .then(res => {
                    // console.log(res.data);
                    this.data3.splice(0, this.data3.length);
                    data = res.data.data;
                    console.log(data);
                    for (var i = 0; i < data.length; i++) {
                        if (data[i] != null) this.data3.push(data[i]);
                    }
                    this.loading = false;
                })
                .catch(err => {
                    console.log(err);
                });
        },
        goto(gid) {
            this.$router.push("/goods/goodsinfo/" + gid);
        }
    }
};
</script>
