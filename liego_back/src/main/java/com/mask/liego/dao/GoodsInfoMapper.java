package com.mask.liego.dao;

import com.mask.liego.entity.GoodsInfo;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface GoodsInfoMapper {
    public void insertGoosInfo(GoodsInfo goodsInfo);

    public GoodsInfo getGoodsInfoByGid(int gid);

    public List<GoodsInfo> getGoodsInfos();

    // 发布的
    public List<GoodsInfo> getGoodsInfosByUid(int uid);



    public GoodsInfo getGoodsInfoByGnameAndUid(String gname, int uid);

    public void deleteGoodsInfoByGid(int gid);

    public void updateGname(GoodsInfo goodsInfo);

    public void updateType(GoodsInfo goodsInfo);

    public void updatePrice(GoodsInfo goodsInfo);

    public void updateCount(GoodsInfo goodsInfo);

    public void updateDescriptionID(GoodsInfo goodsInfo);
}
