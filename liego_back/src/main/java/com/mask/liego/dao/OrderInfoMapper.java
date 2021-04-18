package com.mask.liego.dao;

import com.mask.liego.entity.OrderInfo;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface OrderInfoMapper {
    public void insertOrderInfo(OrderInfo orderInfo);

    public void updateTime(OrderInfo orderInfo);

    public void updateStatus(OrderInfo orderInfo);

    public void deleteOrderInfoByOid(int oid);

    public OrderInfo getOrderInfoByOid(int oid);

    public List<OrderInfo> getOrderInfos();

    public List<OrderInfo> getOrderInfosBySeller(int uid);

    public List<OrderInfo> getOrderInfosByBuyer(int uid);
}
