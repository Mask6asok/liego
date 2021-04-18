<template>
    <div class="home">
        <div class="block" style="height: 350px">
            <el-carousel height="340px" class="carousel">
                <el-carousel-item v-for="img in this.imgs" :key="img">
                    <img src="@/assets/b1.jpg" class="carouselimg" />
                </el-carousel-item>
            </el-carousel>
        </div>

        <div class="inputs">
            <center>
                <input
                    type="text"
                    class="el-input__inner"
                    style="width: 500px"
                    v-model="search"
                    @input="searchAction($event)"
                    placeholder="搜索"
                />
            </center>
        </div>

        <div class="products">
            <div v-if="searchResult.length > 0">
                <div
                    v-for="product in searchResult"
                    :key="product"
                    class="product"
                >
                    <ul>
                        <li>
                            <img :src="product.descrip_img1" class="proimg" />
                        </li>
                        <li>
                            <h4>{{ product.gname }}</h4>
                        </li>
                        <li>
                            <h4>{{ product.price }}￥</h4>
                        </li>
                        <li>
                            <el-button
                                class="probutton"
                                type="success"
                                round
                                @click="gotoinfo(product.gid)"
                                >立即购买</el-button
                            >
                        </li>
                    </ul>
                </div>
            </div>

            <div v-else>
                <div
                    v-for="product in products"
                    :key="product"
                    class="product"
                    v-loading="loading"
                    element-loading-text="Loading"
                >
                    <ul>
                        <li>
                            <img :src="product.descrip_img1" class="proimg" />
                        </li>
                        <li>
                            <h4>{{ product.gname }}</h4>
                        </li>
                        <li>
                            <h4>{{ product.price }}￥</h4>
                        </li>
                        <li>
                            <el-button
                                class="probutton"
                                type="success"
                                round
                                @click="gotoinfo(product.gid)"
                                >商品详情</el-button
                            >
                        </li>
                    </ul>
                </div>
            </div>
        </div>
    </div>
</template>

<script>
import { throttle } from "@/utils/flow";
import Pagination from "@/components/Pagination"; // secondary package based on el-pagination
import { getToken } from "@/utils/auth";

export default {
    name: "dashboard",
    components: { Pagination },
    data() {
        return {
            imgs: ["@/assets/b1.jpg"],
            products: {
                gid: "",
                descrip_img1: "",
                gname: "",
                price: ""
            },
            searchResult: [],
            search: "",
            loading: true
        };
    },
    created() {
        this.getProducts();
    },

    methods: {
        getProducts() {
            this.loading = true;
            var vm = this;
            var data = {
                gid: this.products.gid,
                descrip_img1: this.products.descrip_img1,
                gname: this.products.gname,
                price: this.products.price
            };
            this.axios({
                method: "post",
                url: "http://localhost:8080/ViewGoodsInfosW", //首页list接口
                crossDomain: true, // 同源策略
                data: data //请求的表单数据
            })
                .then(function(resp) {
                    console.log(resp);
                    var r = resp.data;
                    if (r.status == 0) {
                        console.info(resp.data);
                        vm.products = resp.data.data;
                    }
                })
                .catch(err => {
                    console.info("报错的信息", err.response.message);
                });
            this.loading = false;
        },

        //前端搜索
        searchAction: throttle(function(e) {
            const inputValue = e.target.value;
            this.searchResult = this.products.filter(product => {
                if (product.gname.includes(inputValue)) {
                    return product;
                }
            });
        }, 500),

        gotoinfo(gid) {
            this.$router.push("/goods/goodsinfo/" + gid);
        }
    }
};
</script>
<style scoped>
.user {
    margin-top: 30px;
    float: right;
}

.searchWord {
    width: 88%;
}

ul {
    list-style: none;
    text-align: center;
}

.search {
    margin-top: 20px;
    margin-left: 20px;
    display: inline-block;
    width: 300px;
}

.block {
    margin-top: 20px;
}
.carousel {
    width: 70%;
    margin: 0 auto;
}
.carouselimg {
    width: 100%;
    height: 340px;
}

.searchWord {
    margin: 0 auto;
}

.product {
    display: inline-block;
    width: 30%;
    float: left;
}
.proimg {
    width: 250px;
    height: 250px;
}
.el-input__inner {
    margin: 0 auto;
    width: 500px;
}
</style>
