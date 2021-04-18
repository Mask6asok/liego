package com.mask.liego.dao;

import com.mask.liego.entity.UserInfo;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface UserInfoMapper {
    public UserInfo getUserInfoByUid(int uid);

    public UserInfo getUserInfoByUsername(String username);

    public UserInfo getUserInfoBySno(String sno);

    public UserInfo getUserInfoByPhone(String phone);

    public List<UserInfo> getUserInfos();

    public void insertUserInfo(UserInfo userInfo);

    public void updateUserInfo(UserInfo userInfo);

    public void updateBalance(UserInfo userInfo);

    public void deleteUserInfoByUsername(String username);

    public void deleteUserInfoByUid(int uid);

}