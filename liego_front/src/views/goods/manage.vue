<template>
    <!--    这里是显示商品管理列表的地方-->
    <div class="app-container">
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
            <el-table-column align="center" label="商品分类" width="auto">
                <template slot-scope="scope">
                    {{ scope.row.gtype }}
                </template>
            </el-table-column>
            <el-table-column align="center" label="商品单价" width="auto">
                <template slot-scope="scope">
                    {{ scope.row.price }}
                </template>
            </el-table-column>
            <el-table-column align="center" label="商品数目" width="auto">
                <template slot-scope="scope">
                    {{ scope.row.count }}
                </template>
            </el-table-column>
            <el-table-column align="center" label="商品发布时间" width="auto">
                <template slot-scope="scope">
                    {{ scope.row.ptime }}
                </template>
            </el-table-column>

            <el-table-column
                label="操作"
                align="center"
                width="auto"
                class-name="small-padding fixed-width"
            >
                <template slot-scope="scope">
                    <el-button size="mini" type="success">
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
</template>

<script>
import { MessageBox } from "element-ui";
export default {
    data() {
        return {
            list: []
        };
    },
    created() {
        this.fetchData();
    },
    methods: {
        editproduct(gid) {
            this.router.push("/editproduct/index/" + gid);
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
                    this.$axios({
                        url: "http://localhost:8080/DelGoods",
                        method: "post",
                        data: data,
                        headers: { "Content-Type": "application/json" }
                    }).then(res => {
                        console.log(res.data);
                    });
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
            this.$axios({
                url: "http://localhost:8080/ViewGoodsInfos", //请求的地址
                method: "post", //请求的方式
                data: '{"none":"none"}',
                headers: { "Content-Type": "application/json" },
                xhrFields: { withCredentials: true }
            })
                .then(res => {
                    console.info(res.data);
                    console.info(typeof res.data);
                    console.info(typeof vm.list);
                    vm.list = res.data;
                })
                .catch(err => {
                    console.info("请求错误", err.response.message);
                });
        }
    }
};
</script>
