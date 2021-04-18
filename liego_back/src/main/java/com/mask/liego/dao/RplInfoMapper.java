package com.mask.liego.dao;

import com.mask.liego.entity.RplInfo;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface RplInfoMapper {
    public void insertRplInfo(RplInfo rplInfo);

    public void deleteRplInfoByRid(int rid);

    public void deleteRplInfoByMid(int mid);

    public RplInfo getRplInfoByRid(int rid);

    public List<Integer> getRidByMid(int mid);

    public List<RplInfo> getRplInfosByMid(int mid);

    public List<RplInfo> getAllRpls();
}
