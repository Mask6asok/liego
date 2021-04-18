package com.mask.liego.dao;

import com.mask.liego.entity.UserLogin;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface UserLoginMapper {
    // 查询用户的方法
    UserLogin getUserLoginByUid(int uid);

    UserLogin getUserLoginByUsername(String username);

    List<UserLogin> getUserLoginList();

    // 更新用户的方法（修改密码）
    void updateUserLogin(UserLogin userLogin);

    // 删除用户的方法
    void deleteUserLoginByUsername(String username);

    void deleteUserLoginByUid(int uid);

    // 新增用户的方法
    int insertUserLogin(UserLogin userLogin);
}