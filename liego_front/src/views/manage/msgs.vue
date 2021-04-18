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
                placeholder="留言内容"
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
                <el-table-column align="center" label="留言id" width="auto">
                    <template slot-scope="scope">
                        {{ scope.row.mid }}
                    </template>
                </el-table-column>
                <el-table-column align="center" label="留言商品id" width="auto">
                    <template slot-scope="scope">
                        {{ scope.row.gid }}
                    </template>
                </el-table-column>
                <el-table-column align="center" label="留言用户id" width="auto">
                    <template slot-scope="scope">
                        {{ scope.row.uid }}
                    </template>
                </el-table-column>
                <el-table-column align="center" label="留言内容" width="auto">
                    <template slot-scope="scope">
                        {{ scope.row.msg }}
                    </template>
                </el-table-column>
                <el-table-column
                    prop="mtime"
                    sortable
                    align="center"
                    label="留言时间"
                    width="auto"
                >
                    <template slot-scope="scope">
                        {{ new Date(scope.row.mtime).toLocaleString() }}
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
                            type="danger"
                            @click="delMsg(scope.row.mid)"
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
                <el-table-column align="center" label="留言id" width="auto">
                    <template slot-scope="scope">
                        {{ scope.row.mid }}
                    </template>
                </el-table-column>
                <el-table-column align="center" label="留言商品id" width="auto">
                    <template slot-scope="scope">
                        {{ scope.row.gid }}
                    </template>
                </el-table-column>
                <el-table-column align="center" label="留言用户id" width="auto">
                    <template slot-scope="scope">
                        {{ scope.row.uid }}
                    </template>
                </el-table-column>
                <el-table-column align="center" label="留言内容" width="auto">
                    <template slot-scope="scope">
                        {{ scope.row.msg }}
                    </template>
                </el-table-column>
                <el-table-column
                    prop="mtime"
                    sortable
                    align="center"
                    label="留言时间"
                    width="auto"
                >
                    <template slot-scope="scope">
                        {{ new Date(scope.row.mtime).toLocaleString() }}
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
                            type="danger"
                            @click="delMsg(scope.row.mid)"
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
            this.searchResult = this.list.filter(m => {
                if (m.msg.includes(inputValue)) {
                    return m;
                }
            });
            console.info(this.searchResult);
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
        delMsg(mid) {
            MessageBox.confirm("确定要删除该条留言吗？", "理易购", {
                confirmButtonText: "确定",
                cancelButtonText: "取消",
                type: "warning"
            })
                .then(() => {
                    var data = { mid: mid };
                    this.post("http://localhost:8080/DelMsg", data).then(
                        res => {
                            console.log(res.data);
                        }
                    );
                    this.$message({
                        type: "success",
                        message: "删除成功!"
                    });
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
            this.post("http://localhost:8080/ViewAllMsgs", data)
                .then(res => {
                    // console.info(res.data)
                    // console.info(new Date(res.data.mtime))
                    vm.list = res.data.data;
                })
                .catch(err => {
                    console.info("请求错误", err.response.message);
                });
        }
    }
};
</script>
