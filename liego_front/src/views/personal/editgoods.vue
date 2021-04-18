<template>
    <div class="app-container">
        <el-header>
            <el-button type="text" @click="dialogFormVisible = true"
                >发布商品</el-button
            >

            <el-dialog title="发布商品" :visible.sync="dialogFormVisible">
                <el-form :model="form">
                    <el-form-item
                        label="商品名称"
                        :label-width="formLabelWidth"
                    >
                        <el-input
                            v-model="form.gname"
                            autocomplete="off"
                        ></el-input>
                    </el-form-item>
                    <el-form-item
                        label="商品类别"
                        :label-width="formLabelWidth"
                    >
                        <el-select
                            v-model="form.gtype"
                            placeholder="请选择商品类别"
                        >
                            <el-option label="电子产品" value="0"></el-option>
                            <el-option label="学习用品" value="1"></el-option>
                            <el-option label="体育器材" value="2"></el-option>
                            <el-option label="闲杂物品" value="3"></el-option>
                        </el-select>
                    </el-form-item>
                    <el-form-item
                        label="商品单价"
                        :label-width="formLabelWidth"
                    >
                        <el-input
                            v-model="form.price"
                            autocomplete="off"
                        ></el-input>
                    </el-form-item>
                    <el-form-item
                        label="商品数量"
                        :label-width="formLabelWidth"
                    >
                        <el-input
                            v-model="form.count"
                            autocomplete="off"
                        ></el-input>
                    </el-form-item>
                    <el-form-item
                        label="商品描述"
                        :label-width="formLabelWidth"
                    >
                        <el-input
                            type="textarea"
                            v-model="form.descrip_text"
                            autocomplete="off"
                        ></el-input>
                    </el-form-item>
                    <el-form-item
                        label="商品图片"
                        :label-width="formLabelWidth"
                    >
                        <el-row>
                            <el-col :span="4">
                                <el-upload
                                    class="avatar-uploader"
                                    action="http://localhost:8080/UploadImg"
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
    </div>
</template>
<script>
var token = "";

export default {
    data() {
        return {
            dialogFormVisible: true,
            form: {
                gname: "",
                gtype: "",
                price: "",
                count: "",
                descrip_text: "",
                descrip_img1: "",
                descrip_img2: "",
                descrip_img3: ""
            },
            formLabelWidth: "120px"
        };
    },
    created() {
        this.fetchDataById();
    },
    methods: {
        fetchDataById() {
            var gid = this.$route.params.gid;
            var vm = this;
            var data = {
                gid: gid
            };
            this.axios({
                method: "post",
                url: "http://localhost:8080/ViewGoodsInfoW/",
                crossDomain: true, // 同源策略
                data: data //请求的表单数据     //查询
            }).then(function(resp) {
                console.info(resp.data);
                vm.form = resp.data.data;
                console.info(vm.form);
            });
        },

        handleSuccess1(res, file) {
            console.log("uoload success", res);
            this.descrip_img1 = URL.createObjectURL(file.raw);
            console.log("img1", this.descrip_img1);
        },
        handleSuccess2(res, file) {
            console.log("uoload success", res);
            this.descrip_img2 = URL.createObjectURL(file.raw);
            console.log("img2", this.descrip_img2);
        },
        handleSuccess3(res, file) {
            console.log("uoload success", res);
            this.descrip_img3 = URL.createObjectURL(file.raw);
            console.log("img3", this.descrip_img3);
        },
        beforeUpload(file) {
            const isJPG = file.type === "image/jpeg";
            const isLt2M = file.size / 1024 / 1024 < 2;

            if (!isJPG) {
                this.$message.error("上传头像图片只能是 JPG 格式!");
            }
            if (!isLt2M) {
                this.$message.error("上传头像图片大小不能超过 2MB!");
            }
            return isJPG && isLt2M;
        },
        getToken() {
            var data = {
                username: "Mask6asok",
                password: "Mask6asok"
            };
            this.$axios({
                url: "https://sm.ms/api/v2/token",
                method: "post",

                data: data
            })
                .then(res => {
                    console.log(res);
                })
                .catch(err => {
                    console.log(err);
                });
        },
        hadlePublish() {
            var data = {
                uid: 1,
                gname: this.form.gname,
                gtype: this.form.gtype,
                price: this.form.gprice,
                gcount: this.form.gcount,
                descrip_text: this.form.descrip_text,
                descrip_img1: this.form.descrip_img1,
                descrip_img2: this.form.descrip_img2,
                descrip_img3: this.form.descrip_img3
            };
            console.log(data);
            this.dialogFormVisible = false;
            this.$axios({
                url: "http://localhost:8080/PubGoods",
                method: "post",
                data: data
            })
                .then(res => {
                    console.log(res);
                    // if (res.data == 0) {
                    //     this.$message({
                    //         message: "发布成功",
                    //         type: "success"
                    //     });
                    // } else {
                    //     this.$message({
                    //         message: "发布失败",
                    //         type: "warning"
                    //     });
                    // }
                })
                .catch(err => {
                    this.$message.error(err);
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
