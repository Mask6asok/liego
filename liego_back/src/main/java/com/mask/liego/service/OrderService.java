package com.mask.liego.service;

import com.mask.liego.dao.GoodsInfoMapper;
import com.mask.liego.dao.OrderInfoMapper;
import com.mask.liego.dao.UserInfoMapper;
import com.mask.liego.dao.UserLoginMapper;
import com.mask.liego.entity.GoodsInfo;
import com.mask.liego.entity.OrderInfo;
import com.mask.liego.entity.UserInfo;
import com.mask.liego.entity.UserLogin;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.sql.Timestamp;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

@Service
public class OrderService {
    @Resource
    private OrderInfoMapper orderInfoMapper;
    @Resource
    private UserInfoMapper userInfoMapper;
    @Resource
    private GoodsInfoMapper goodsInfoMapper;
    @Resource
    private UserLoginMapper userLoginMapper;

    // TODO : 权限操作的任务，是否要把操作用户的ID传进service层，由service层来负责权限验证，或是由controller层来负责？

    /**
     * 生成订单的服务，由传进的参数，在DB中生成对应的表项
     *
     * @param orderInfo 订单参数
     * @return 生成结果 整形
     */
    @Transactional
    public int lunchOrder(OrderInfo orderInfo) {
        int res = -1;
        do {
            if (goodsInfoMapper.getGoodsInfoByGid(orderInfo.getGid()) == null) {
                res = 1; // 商品不存在
                break;
            }
            if (userInfoMapper.getUserInfoByUid(orderInfo.getSellerID()) == null ||
                    userInfoMapper.getUserInfoByUid(orderInfo.getBuyerID()) == null) {
                res = 2; // 用户不存在
                break;
            }
            if (goodsInfoMapper.getGoodsInfoByGid(orderInfo.getGid()).getUid() != orderInfo.getSellerID()) {
                res = 3; // 卖方不匹配
                break;
            }
            if (orderInfo.getSellerID() == orderInfo.getBuyerID()) {
                res = 4; // 不能买自己的
                break;
            }
            UserInfo u = userInfoMapper.getUserInfoByUid(orderInfo.getBuyerID());
            if (u.getBalance() < orderInfo.getOcost()) {
                res = 5; // 钱不够
                break;
            }
            GoodsInfo g = goodsInfoMapper.getGoodsInfoByGid(orderInfo.getGid());
            if (g.getPrice() * orderInfo.getGcount() < orderInfo.getOcost()) {
                res = 5; // 订单金额数量不符
                break;
            }
            if (g.getCount() < orderInfo.getGcount()) {
                res = 6; // 剩余商品数量不够
                break;
            }
            u.setBalance(u.getBalance() - orderInfo.getOcost());
            userInfoMapper.updateBalance(u);
            g.setCount(g.getCount() - orderInfo.getGcount());
            goodsInfoMapper.updateCount(g);
            orderInfo.setStatus(0);
            orderInfo.setTime(new Timestamp(new Date().getTime()));
            orderInfoMapper.insertOrderInfo(orderInfo);
            res = 0;
        } while (false);
        return res;
    }

    /**
     * 处理订单的服务，根据传进的订单id号，更新订单状态
     *
     * @param orderId 待更新订单id号
     * @return 处理结果
     */
    public int processOrder(int orderId, int uid) {
        int res = -1;
        OrderInfo infoInDB = null;
        do {
            infoInDB = orderInfoMapper.getOrderInfoByOid(orderId);
            if (infoInDB == null) {
                res = 1; // 订单不存在
                break;
            }
            if (infoInDB.getSellerID() != uid && userLoginMapper.getUserLoginByUid(uid).getAuthority() != 0) {
                res = 2; // 用户不匹配
                break;
            }
            infoInDB.setStatus(1);
            infoInDB.setTime(new Timestamp(new Date().getTime()));
            orderInfoMapper.updateStatus(infoInDB);
            orderInfoMapper.updateTime(infoInDB);
            res = 0;
        } while (false);
        return res;
    }

    /**
     * 完成订单的服务，根据传进的订单id号，更新订单状态
     *
     * @param orderId 待更新订单id号
     * @return 更新结果
     */
    @Transactional
    public int finishOrder(int orderId, int uid) {
        int res = -1;
        OrderInfo infoInDB = null;
        do {
            infoInDB = orderInfoMapper.getOrderInfoByOid(orderId);
            if (infoInDB == null) {
                res = 1; // 订单不存在
                break;
            }
            if (infoInDB.getBuyerID() != uid) {
                res = 2; // 用户不匹配
                break;
            }
            infoInDB.setStatus(2);
            infoInDB.setTime(new Timestamp(new Date().getTime()));
            orderInfoMapper.updateStatus(infoInDB);
            orderInfoMapper.updateTime(infoInDB);
            UserInfo u = userInfoMapper.getUserInfoByUid(infoInDB.getSellerID());
            if (u != null) {
                u.setBalance(infoInDB.getOcost());
                userInfoMapper.updateBalance(u);
            }
            res = 0;
        } while (false);
        return res;
    }

    /**
     * 获取订单的服务，根据传进的订单id号，取得订单信息
     *
     * @param orderId 待获取订单id号
     * @return 订单信息
     */
    public OrderInfo viewOrder(int orderId) {
        OrderInfo orderInfo = null;
        orderInfo = orderInfoMapper.getOrderInfoByOid(orderId);
        return orderInfo;
    }

    public List<OrderInfo> viewOrders() {
        List<OrderInfo> list;
        list = orderInfoMapper.getOrderInfos();
        return list;
    }

    public List<OrderInfo> viewOrdersBySeller(int uid) {
        return orderInfoMapper.getOrderInfosBySeller(uid);
    }

    public List<OrderInfo> viewOrdersByBuyer(int uid) {
        return orderInfoMapper.getOrderInfosByBuyer(uid);
    }

    public List<GoodsInfo> viewGoodsBySeller(int uid) {
        List<GoodsInfo> list = new LinkedList<GoodsInfo>();
        List<OrderInfo> ol = viewOrdersBySeller(uid);
        System.out.println(ol.size());
        for (OrderInfo o : ol) {
            GoodsInfo g = goodsInfoMapper.getGoodsInfoByGid(o.getGid());
            if (g != null) {
                list.add(g);
            }
        }
        return list;
    }

    public List<GoodsInfo> viewGoodsByBuyer(int uid) {
        List<GoodsInfo> list = new LinkedList<GoodsInfo>();
        List<OrderInfo> ol = viewOrdersByBuyer(uid);
        for (OrderInfo o : ol) {
            GoodsInfo g = goodsInfoMapper.getGoodsInfoByGid(o.getGid());
            if (g != null) {
                list.add(g);
            }
        }
        return list;
    }

}
