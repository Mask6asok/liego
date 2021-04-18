package com.mask.liego.dao;

import com.mask.liego.entity.MsgInfo;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface MsgInfoMapper {
    public void insertMsgInfo(MsgInfo msgInfo);

    public void deleteMsgInfoByMid(int mid);

    public MsgInfo getMsgInfoByMid(int mid);

    public List<MsgInfo> getMsgInfosByGid(int gid);

    public List<Integer> getMidByGid(int gid);

    public List<MsgInfo> getAllMsgs();
}
