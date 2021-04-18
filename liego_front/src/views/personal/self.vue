<template>
    <!--    这里是显示商品管理列表的地方-->
    <div class="app-container">
        <el-header>
            <el-button type="text" @click="dialogFormVisible = true"
                >发布商品</el-button
            >

            <el-dialog
                title="发布/编辑商品"
                :visible.sync="dialogFormVisible"
                width="600px"
            >
                <el-form :model="form" :rules="goodsRules">
                    <el-form-item
                        label="商品名称"
                        :label-width="formLabelWidth"
                        prop="gname"
                    >
                        <el-input
                            v-model="form.gname"
                            autocomplete="off"
                            maxlength="20"
                        ></el-input>
                    </el-form-item>
                    <el-form-item
                        label="商品类别"
                        :label-width="formLabelWidth"
                        prop="gtype"
                    >
                        <el-select
                            v-model="form.gtype"
                            placeholder="请选择商品类别"
                            id="codeShare"
                        >
                            <el-option
                                v-for="item in options"
                                :key="item.value"
                                :label="item.label"
                                :value="item.value"
                            >
                            </el-option>
                        </el-select>
                    </el-form-item>
                    <el-form-item
                        label="商品单价"
                        :label-width="formLabelWidth"
                        prop="price"
                    >
                        <el-input
                            v-model="form.price"
                            autocomplete="off"
                            :change="check_price()"
                        ></el-input>
                    </el-form-item>
                    <el-form-item
                        label="商品数量"
                        :label-width="formLabelWidth"
                        prop="count"
                    >
                        <el-input
                            v-model="form.count"
                            autocomplete="off"
                            oninput="value=value.replace(/[^\d]/g, '')"
                        ></el-input>
                    </el-form-item>
                    <el-form-item
                        label="商品描述"
                        :label-width="formLabelWidth"
                        prop="descrip_text"
                    >
                        <el-input
                            type="textarea"
                            v-model="form.descrip_text"
                            autocomplete="off"
                            maxlength="100"
                        ></el-input>
                    </el-form-item>
                    <el-form-item
                        label="商品图片"
                        :label-width="formLabelWidth"
                        prop="descrip_img1"
                    >
                        <el-row>
                            <el-col :span="4">
                                <el-upload
                                    class="avatar-uploader"
                                    action="http://localhost:8080/UploadImg"
                                    :headers="uploadHeaders"
                                    accept="image/jpg,image/jpeg"
                                    :show-file-list="false"
                                    :before-upload="beforeUpload"
                                    :on-success="handleSuccess1"
                                >
                                    <img
                                        v-if="form.descrip_img1"
                                        :src="form.descrip_img1"
                                        class="avatar"
                                    />
                                    <i
                                        v-else
                                        class="el-icon-plus avatar-uploader-icon"
                                    ></i>
                                </el-upload>
                            </el-col>
                            <el-col :span="4">
                                <el-upload
                                    class="avatar-uploader"
                                    action="http://localhost:8080/UploadImg"
                                    accept="image/jpg,image/jpeg"
                                    :headers="uploadHeaders"
                                    :before-upload="beforeUpload"
                                    :show-file-list="false"
                                    :on-success="handleSuccess2"
                                >
                                    <img
                                        v-if="form.descrip_img2"
                                        :src="form.descrip_img2"
                                        class="avatar"
                                    />
                                    <i
                                        v-else
                                        class="el-icon-plus avatar-uploader-icon"
                                    ></i>
                                </el-upload>
                            </el-col>
                            <el-upload
                                class="avatar-uploader"
                                action="http://localhost:8080/UploadImg"
                                accept="image/jpg,image/jpeg"
                                :headers="uploadHeaders"
                                :show-file-list="false"
                                :before-upload="beforeUpload"
                                :on-success="handleSuccess3"
                            >
                                <img
                                    v-if="form.descrip_img3"
                                    :src="form.descrip_img3"
                                    class="avatar"
                                />
                                <i
                                    v-else
                                    class="el-icon-plus avatar-uploader-icon"
                                ></i>
                            </el-upload>
                        </el-row>
                    </el-form-item>
                </el-form>
                <div slot="footer" class="dialog-footer">
                    <el-button @click="dialogFormVisible = false"
                        >取 消</el-button
                    >
                    <el-button type="primary" @click="hadlePublish()"
                        >确 定</el-button
                    >
                </div>
            </el-dialog>
        </el-header>
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
            <el-table
                :data="searchResult"
                border
                fit
                highlight-current-row
                v-loading="loading"
                element-loading-text="Loading"
            >
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
                    prop="gtypr"
                    sortable=""
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
                    align="center"
                    prop="count"
                    sortable
                    label="商品数目"
                    width="auto"
                >
                    <template slot-scope="scope">
                        {{ scope.row.count }}
                    </template>
                </el-table-column>
                <el-table-column
                    prop="ptime"
                    sortable
                    align="center"
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
                            type="primary"
                            @click="
                                editGoods(scope.row.gid);
                                edit = true;
                                dialogFormVisible = true;
                            "
                        >
                            编辑
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
            <el-table
                :data="list"
                border
                fit
                highlight-current-row
                v-loading="loading"
                element-loading-text="Loading"
            >
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
                    prop="gtypr"
                    sortable=""
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
                    align="center"
                    prop="count"
                    sortable
                    label="商品数目"
                    width="auto"
                >
                    <template slot-scope="scope">
                        {{ scope.row.count }}
                    </template>
                </el-table-column>
                <el-table-column
                    prop="ptime"
                    sortable
                    align="center"
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
                            type="primary"
                            @click="
                                editGoods(scope.row.gid);
                                edit = true;
                                dialogFormVisible = true;
                            "
                        >
                            编辑
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
import { getUid, getToken } from "@/utils/auth";
import { throttle } from "@/utils/flow";
export default {
    data() {
        return {
            goodsRules: {
                gname: [
                    {
                        required: true,
                        trigger: "blur",
                        message: "请输入商品名称"
                    }
                ],
                gtype: [
                    {
                        required: true,
                        trigger: "blur",
                        message: "请输入商品类型"
                    }
                ],
                price: [
                    {
                        required: true,
                        trigger: "blur",
                        message: "请输入商品价格"
                    }
                ],
                count: [
                    {
                        required: true,
                        trigger: "blur",
                        message: "请输入商品数量"
                    }
                ],
                descrip_text: [
                    {
                        required: true,
                        trigger: "blur",
                        message: "请输入商品描述"
                    }
                ],
                descrip_img1: [
                    {
                        required: true,
                        trigger: "blur",
                        message: "请上传至少一张商品图片"
                    }
                ]
            },
            list: [],
            searchResult: [],
            search: "",
            dialogTableVisible: false,
            dialogFormVisible: false,
            edit: false,
            form: {
                gid: "",
                gname: "",
                gtype: "",
                price: "",
                count: "",
                descrip_text: "",
                descrip_img1: "",
                descrip_img2: "",
                descrip_img3: ""
            },
            uploadHeaders: {
                token: getToken()
            },
            formLabelWidth: "120px",
            options: [
                {
                    label: "电子产品",
                    value: 0
                },
                {
                    label: "学习用品",
                    value: 1
                },
                {
                    label: "体育器材",
                    value: 2
                },
                {
                    label: "闲杂物品",
                    value: 3
                }
            ],
            number: ""
        };
    },
    created() {
        this.loading = true;
        this.fetchData();
        this.loading = false;
    },
    methods: {
        check_price: function() {
            var price = "" + this.form.price;
            price = price
                .replace(/[^\d.]/g, "") // 清除“数字”和“.”以外的字符
                .replace(/\.{2,}/g, ".") // 只保留第一个. 清除多余的
                .replace(".", "$#$")
                .replace(/\./g, "")
                .replace("$#$", ".")
                .replace(/^(\-)*(\d+)\.(\d\d).*$/, "$1$2.$3"); // 只能输入两个小数
            if (price.indexOf(".") < 0 && price != "") {
                // 以上已经过滤，此处控制的是如果没有小数点，首位不能为类似于 01、02的金额
                price = parseFloat(price);
            }
            this.form.price = price;
        },
        searchAction: throttle(function(e) {
            const inputValue = e.target.value;
            console.log(inputValue);
            this.searchResult = this.list.filter(good => {
                console.log(good);
                if (good.gname.includes(inputValue)) {
                    return good;
                }
            });
        }, 500),
        post(url, data) {
            return new Promise((resolve, reject) => {
                var token = getToken();
                console.log(token);
                console.log(url);
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
        upload(param) {
            const formData = new FormData();
            formData.append("file", param.file);
            this.post("http://localhost:8080/UploadImg", formData).then(
                r => {
                    console.log(r);
                }
            );
        },
        handleSuccess1(res, file) {
            console.log("uoload success", res);
            this.form.descrip_img1 = "http://localhost:8080" + res.data.url;
            console.log("img1", this.form.descrip_img1);
        },
        handleSuccess2(res, file) {
            console.log("uoload success", res);
            this.form.descrip_img2 = "http://localhost:8080" + res.data.url;
            console.log("img2", this.form.descrip_img2);
        },
        handleSuccess3(res, file) {
            console.log("uoload success", res);
            this.form.descrip_img3 = "http://localhost:8080" + res.data.url;
            console.log("img3", this.form.descrip_img3);
        },
        beforeUpload(file) {
            const isJPG = file.type === "image/jpeg";
            const isLt2M = file.size / 1024 / 1024 < 2;

            if (!isJPG) {
                this.$message.error("上传图片只能是 JPG 格式!");
            }
            if (!isLt2M) {
                this.$message.error("上传图片大小不能超过 2MB!");
            }
            return isJPG && isLt2M;
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
        hadlePublish() {
            if (this.edit != true) {
                var uid = getUid();
                var data = {
                    uid: uid,
                    gname: this.form.gname,
                    gtype: this.form.gtype,
                    price: this.form.price,
                    count: this.form.count,
                    descrip_text: this.form.descrip_text,
                    descrip_img1: this.form.descrip_img1,
                    descrip_img2: this.form.descrip_img2,
                    descrip_img3: this.form.descrip_img3
                };
                console.log(data);
                this.dialogFormVisible = false;
                this.post("http://localhost:8080/PubGoods", data)
                    .then(res => {
                        console.log(res);
                        if (res.data.status == 0) {
                            this.$message({
                                message: "发布成功",
                                type: "success"
                            });
                            this.threeGo();
                        } else {
                            this.$message({
                                message: "发布失败",
                                type: "warning"
                            });
                        }
                    })
                    .catch(err => {
                        this.$message.error(err);
                    });
            } else {
                var uid = getUid();
                var data = {
                    uid: uid,
                    gid: this.form.gid,
                    gname: this.form.gname,
                    gtype: this.form.gtype,
                    price: this.form.price,
                    count: this.form.count,
                    descrip_text: this.form.descrip_text,
                    descrip_img1: this.form.descrip_img1,
                    descrip_img2: this.form.descrip_img2,
                    descrip_img3: this.form.descrip_img3
                };
                console.log(data);
                this.dialogFormVisible = false;
                this.edit = false;
                this.post("http://localhost:8080/UpdateGoods", data)
                    .then(res => {
                        console.log(res);
                        if (res.data.status == 0) {
                            this.$message({
                                message: "修改成功",
                                type: "success"
                            });
                            this.threeGo();
                        } else {
                            this.$message({
                                message: "修改失败",
                                type: "warning"
                            });
                        }
                    })
                    .catch(err => {
                        this.$message.error(err);
                    });
            }
        },
        editGoods(gid) {
            var gid = gid;
            var vm = this;
            var data = {
                gid: gid
            };
            this.post("http://localhost:8080/ViewGoodsInfoW", data).then(
                function(resp) {
                    console.info(resp.data);
                    vm.form = resp.data.data;
                    console.info(vm.form);
                    var numbers = $("#codeShare").find("option"); //获取select下拉框的所有值
                    for (var j = 0; j < numbers.length; j++) {
                        if ($(numbers[j]).val() == vm.form.gtype) {
                            $(numbers[j]).porp("selected", "selected");
                        }
                    }
                }
            );
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
            var uid = getUid();
            console.log("self goods uid", uid);
            var data = { uid: uid };
            console.log(data);
            this.post("http://localhost:8080/ViewGoodsInfosByUid", data)
                .then(res => {
                    console.info(res.data.data);
                    vm.list = res.data.data;
                })
                .catch(err => {
                    console.info("请求错误", err.response.message);
                });
        }
    }
};
</script>
<style>
.avatar-uploader .el-upload {
    border: 1px dashed #d9d9d9;
    border-radius: 6px;
    cursor: pointer;
    position: relative;
    overflow: hidden;
}
.avatar-uploader .el-upload:hover {
    border-color: #409eff;
}
.avatar-uploader-icon {
    font-size: 32px;
    color: #8c939d;
    width: 64px;
    height: 64px;
    line-height: 64px;
    text-align: center;
}
.avatar {
    width: 64px;
    height: 64px;
    display: block;
}
</style>
