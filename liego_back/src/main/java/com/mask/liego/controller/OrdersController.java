package com.mask.liego.controller;

import com.mask.liego.entity.GoodsInfo;
import com.mask.liego.entity.OrderInfo;
import com.mask.liego.service.OrderService;
import com.mask.liego.tools.ApiResponse;
import com.mask.liego.tools.TokenUse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

@Slf4j
@Controller
public class OrdersController {
    @Resource
    private OrderService orderService;

    private int getUid(HttpServletRequest request) {
        String token = request.getHeader("token");
        return TokenUse.getUid(token);
    }

    private String getUsername(HttpServletRequest request) {
        String token = request.getHeader("token");
        return TokenUse.getUsername(token);
    }

    private int getAuth(HttpServletRequest request) {
        String token = request.getHeader("token");
        return TokenUse.getAuthority(token);
    }


    public void log(String s) {
        System.out.println(s);
    }

    @PostMapping(path = "LunchOrder")
    @ResponseBody
    public Map lunchOrder(HttpServletRequest request, @RequestBody Map<String, String> body) {
        log("LunchOrder");
        ApiResponse res = new ApiResponse();
        try {
            int s = 0;
            OrderInfo orderInfo = new OrderInfo();
            orderInfo.setGid(Integer.parseInt(body.get("gid")));
            orderInfo.setSellerID(Integer.parseInt(body.get("sellerID")));
            orderInfo.setBuyerID(Integer.parseInt(body.get("buyerID")));
            orderInfo.setGcount(Integer.parseInt(body.get("gcount")));
            orderInfo.setOcost(Double.parseDouble(body.get("ocost")));
            orderInfo.setStatus(0);
            if (getUid(request) == Integer.parseInt(body.get("buyerID"))) {
                s = orderService.lunchOrder(orderInfo);
            }
            String[] msg = new String[]{"下单成功", "商品不存在", "用户不存在", "用户不匹配",
                    "无法购买自己的商品", "余额不足", "商品不足"};
            res.setStatus(s);
            ;
            res.setMessage(msg[s]);
        } catch (Exception e) {
            res.setErr();
        }
        return res.raw();
    }

    @PostMapping(path = "ViewOrders")
    @ResponseBody
    public Map viewOrders(HttpServletRequest request, @RequestBody Map<String, String> body) {
        log("ViewOrders");
        ApiResponse res = new ApiResponse();
        try {
            List<OrderInfo> list = new LinkedList<>();
            if (getAuth(request) == 0) {
                list = orderService.viewOrders();
            }
            res.setData(list);
        } catch (Exception e) {
            res.setErr();
        }
        return res.raw();
    }

    @PostMapping(path = "ViewOrdersBySeller")
    @ResponseBody
    public Map MapviewOrdersBySeller(HttpServletRequest request, @RequestBody Map<String, Integer> body) {
        log("ViewOrdersBySeller");
        ApiResponse res = new ApiResponse();
        try {
            List<OrderInfo> list = new LinkedList<>();
            if (getUid(request) == body.get("uid")) {
                list = orderService.viewOrdersBySeller(body.get("uid"));
            }
            res.setData(list);
        } catch (Exception e) {
            res.setErr();
        }
        return res.raw();
    }

    @PostMapping(path = "ViewOrdersByBuyer")
    @ResponseBody
    public Map viewOrdersByBuyer(HttpServletRequest request, @RequestBody Map<String, Integer> body) {
        log("ViewOrdersByBuyer");
        ApiResponse res = new ApiResponse();
        try {
            List<OrderInfo> list = new LinkedList<>();
            if (getUid(request) == body.get("uid")) {
                list = orderService.viewOrdersByBuyer(body.get("uid"));
            }
            res.setData(list);
        } catch (Exception e) {
            res.setErr();
        }
        return res.raw();
    }

    @PostMapping(path = "ViewGoodsBySeller")
    @ResponseBody
    public Map viewGoodsBySeller(HttpServletRequest request, @RequestBody Map<String, Integer> body) {
        log("ViewGoodsBySeller");
        ApiResponse res = new ApiResponse();
        try {
            List<GoodsInfo> list = null;
            if (getUid(request) == body.get("uid")) {
                list = orderService.viewGoodsBySeller(body.get("uid"));
            }
            res.setData(list);
        } catch (Exception e) {
            res.setErr();
        }
        return res.raw();
    }

    @PostMapping(path = "ViewGoodsByBuyer")
    @ResponseBody
    public Map viewGoodsByBuyer(HttpServletRequest request, @RequestBody Map<String, Integer> body) {
        log("ViewGoodsByBuyer");
        ApiResponse res = new ApiResponse();
        try {
            List<GoodsInfo> list = null;
            if (getUid(request) == body.get("uid")) {
                list = orderService.viewGoodsByBuyer(body.get("uid"));
            }
            res.setData(list);
        } catch (Exception e) {
            res.setErr();
        }
        return res.raw();
    }

    @PostMapping(path = "ProcessOrder")
    @ResponseBody
    public Map processOrder(HttpServletRequest request, @RequestBody Map<String, Integer> body) {
        ApiResponse res = new ApiResponse();
        try {
            int oid = body.get("oid");
            int uid = getUid(request);
            OrderInfo o = orderService.viewOrder(oid);
            int s = 2;
            System.out.println(getAuth(request));
            if ((uid == body.get("uid") && o.getSellerID() == uid) || getAuth(request) == 0) {
                s = orderService.processOrder(oid, uid);
            }
            String[] msg = new String[]{"订单处理成功", "订单不存在", "用户不匹配"};
            res.setStatus(s);
            res.setMessage(msg[s]);
        } catch (Exception e) {
            e.printStackTrace();
            res.setErr();
        }
        return res.raw();
    }

    @PostMapping(path = "FinishOrder")
    @ResponseBody
    public Map finishOrder(HttpServletRequest request, @RequestBody Map<String, Integer> body) {
        ApiResponse res = new ApiResponse();
        try {
            int oid = body.get("oid");
            int uid = getUid(request);
            int s = 2;
            OrderInfo o = orderService.viewOrder(oid);
            if ((uid == body.get("uid") && o.getBuyerID() == uid) || getAuth(request) == 0) {
                s = orderService.finishOrder(oid, uid);
            }
            String[] msg = new String[]{"订单结束成功", "订单不存在", "用户不匹配"};
            res.setStatus(s);
            res.setMessage(msg[s]);
        } catch (Exception e) {
            e.printStackTrace();
            res.setErr();
        }
        return res.raw();
    }
}
