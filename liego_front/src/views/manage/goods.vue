<template>
    <!--    这里是显示商品管理列表的地方-->
    <div class="app-container">
        <center>
            <input
                type="text"
                class="el-input__inner"
                style="width: 500px"
                v-model="search"
                @input="searchAction($event)"
                placeholder="商品名称"
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
                <el-table-column align="center" label="商品id" width="auto">
                    <template slot-scope="scope">
                        {{ scope.row.gid }}
                    </template>
                </el-table-column>
                <el-table-column align="center" label="商品名称" width="auto">
                    <template slot-scope="scope">
                        {{ scope.row.gname }}
                    </template>
                </el-table-column>
                <el-table-column
                    prop="gtype"
                    sortable
                    align="center"
                    label="商品分类"
                    width="auto"
                >
                    <template slot-scope="scope">
                        {{ scope.row.gtype }}
                    </template>
                </el-table-column>
                <el-table-column
                    prop="price"
                    sortable
                    align="center"
                    label="商品单价"
                    width="auto"
                >
                    <template slot-scope="scope">
                        {{ scope.row.price }}
                    </template>
                </el-table-column>
                <el-table-column
                    prop="count"
                    sortable
                    align="center"
                    label="商品数目"
                    width="auto"
                >
                    <template slot-scope="scope">
                        {{ scope.row.count }}
                    </template>
                </el-table-column>
                <el-table-column
                    align="center"
                    prop="ptime"
                    sortable
                    label="商品发布时间"
                    width="auto"
                >
                    <template slot-scope="scope">
                        {{ new Date(scope.row.ptime).toLocaleString() }}
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
                            type="success"
                            @click="goto(scope.row.gid)"
                        >
                            详情
                        </el-button>
                        <el-button
                            size="mini"
                            type="danger"
                            @click="delGoods(scope.row.gid, scope.row.gname)"
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
                <el-table-column align="center" label="商品id" width="auto">
                    <template slot-scope="scope">
                        {{ scope.row.gid }}
                    </template>
                </el-table-column>
                <el-table-column align="center" label="商品名称" width="auto">
                    <template slot-scope="scope">
                        {{ scope.row.gname }}
                    </template>
                </el-table-column>
                <el-table-column
                    prop="gtype"
                    sortable
                    align="center"
                    label="商品分类"
                    width="auto"
                >
                    <template slot-scope="scope">
                        {{ scope.row.gtype }}
                    </template>
                </el-table-column>
                <el-table-column
                    prop="price"
                    sortable
                    align="center"
                    label="商品单价"
                    width="auto"
                >
                    <template slot-scope="scope">
                        {{ scope.row.price }}
                    </template>
                </el-table-column>
                <el-table-column
                    prop="count"
                    sortable
                    align="center"
                    label="商品数目"
                    width="auto"
                >
                    <template slot-scope="scope">
                        {{ scope.row.count }}
                    </template>
                </el-table-column>
                <el-table-column
                    align="center"
                    prop="ptime"
                    sortable
                    label="商品发布时间"
                    width="auto"
                >
                    <template slot-scope="scope">
                        {{ new Date(scope.row.ptime).toLocaleString() }}
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
                            type="success"
                            @click="goto(scope.row.gid)"
                        >
                            详情
                        </el-button>
                        <el-button
                            size="mini"
                            type="danger"
                            @click="delGoods(scope.row.gid, scope.row.gname)"
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
import { MessageBox } from "element-ui";
import { getToken } from "@/utils/auth";
import { throttle } from "@/utils/flow";
export default {
    data() {
        return {
            list: [],
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
            this.searchResult = this.list.filter(good => {
                if (good.gname.includes(inputValue)) {
                    return good;
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
        delGoods(gid, gname) {
            MessageBox.confirm(
                "确定要删除商品【" + gname + "】吗？",
                "理易购",
                {
                    confirmButtonText: "确定",
                    cancelButtonText: "取消",
                    type: "warning"
                }
            )
                .then(() => {
                    var data = { gid: gid };
                    this.post("http://localhost:8080/DelGoods", data).then(
                        res => {
                            console.log(res.data);
                            this.$message({
                                type: "success",
                                message: "删除成功!"
                            });
                        }
                    );
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
            this.post("http://localhost:8080/ViewGoodsInfos", data)
                .then(res => {
                    vm.list.splice(0, vm.list.length);
                    var data = res.data.data;
                    this.list = data;
                })
                .catch(err => {
                    console.info("请求错误", err);
                });
        },
        goto(gid) {
            this.$router.push("/goods/goodsinfo/" + gid);
        }
    }
};
</script>
