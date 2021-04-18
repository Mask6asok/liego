package com.mask.liego.dao;

import com.mask.liego.entity.GoodsPublish;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface GoodsPublishMapper {
    public void insertGoodsPublish(GoodsPublish goodsPublish);

    public void updateDisplay(GoodsPublish goodsPublish);

    public void deleteGoodsPublishByGid(int gid);

    public GoodsPublish getGoodsPublishByGid(int gid);
}
