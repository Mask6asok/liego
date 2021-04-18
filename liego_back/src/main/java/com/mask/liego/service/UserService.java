package com.mask.liego.service;

import com.mask.liego.dao.UserInfoMapper;
import com.mask.liego.dao.UserLoginMapper;
import com.mask.liego.entity.UserInfo;
import com.mask.liego.entity.UserLogin;
import com.mask.liego.tools.Crypto;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

@Service
public class UserService {
    @Resource
    private UserLoginMapper userLoginMapper;
    @Resource
    private UserInfoMapper userInfoMapper;

    /**
     * 注册用户服务，前端传进用户新建用户的数据，后端先检验数据合法性再新建表项
     *
     * @param login 包含用户名与密码
     * @param info  包含用户名，学号，手机号等信息
     * @return res 注册结果，整形
     */
    @Transactional
    public int registerUser(UserLogin login, UserInfo info) {
        int res = -1;
        do {
            if (userLoginMapper.getUserLoginByUsername(login.getUsername()) != null ||
                    userInfoMapper.getUserInfoByUsername(info.getUsername()) != null) {
                res = 1; // 用户名已存在
                break;
            }
            if (userInfoMapper.getUserInfoBySno(info.getSno()) != null) {
                res = 2; // 学号已存在
                break;
            }
            if (userInfoMapper.getUserInfoByPhone(info.getPhone()) != null) {
                res = 3; // 手机号已存在
                break;
            }
            login.setPassword(Crypto.getMD5(login.getPassword()));
            int a = userLoginMapper.insertUserLogin(login); // 插入userLogin表
            if (userLoginMapper.getUserLoginByUsername(login.getUsername()) == null) {
//                System.out.println("userLogin表无新增项");
                res = 4; // userLogin表无新增项
                break;
            }
            userInfoMapper.insertUserInfo(info); // 插入userInfo表
            if (userInfoMapper.getUserInfoByUsername(info.getUsername()) == null) {
                res = 4; // userInfo表无新增项
                break;
            }
            res = 0; // 注册成功
        } while (false);
        return res;
    }

    /**
     * 登陆用户服务，查询用户名与密码是否在库中匹配
     *
     * @param login 登陆信息，包含用户名和密码
     * @return res 登陆结果，整形
     */
    public int login(UserLogin login) {
        int res = -1;
        UserLogin loginInDB = null;
        do {
            loginInDB = userLoginMapper.getUserLoginByUsername(login.getUsername());
            if (loginInDB == null) {
                res = 1; // 用户不存在
                break;
            }
            if (!loginInDB.getPassword().equalsIgnoreCase(Crypto.getMD5(login.getPassword()))) {
                res = 2; // 密码错误
                break;
            }
            res = 0;
        } while (false);
        return res;
    }

    /**
     * 重置密码服务，将用户名对应的表项的密码更新
     *
     * @param login 登陆信息，包含要更改的用户名与新的密码
     * @return res 重置结果，整形
     */
    public int resetPassword(UserLogin login, UserInfo info) {
        int res = -1;
        UserLogin loginInDB = null;
        UserInfo infoInDB = null;
        do {
            loginInDB = userLoginMapper.getUserLoginByUsername(login.getUsername());
            infoInDB = userInfoMapper.getUserInfoByUsername(info.getUsername());
            if (loginInDB == null || infoInDB == null) {
                res = 1; // 用户不存在
                break;
            }
            if (!infoInDB.getPhone().equals(info.getPhone()) || !infoInDB.getSno().equals(info.getSno())) {
                res = 2; // 信息错误
                break;
            }
            login.setPassword(Crypto.getMD5(login.getPassword()));
            userLoginMapper.updateUserLogin(login);
            res = 0;
        } while (false);
        return res;
    }

    /**
     * 获取用户信息的服务，将传进的uid获取DB中信息
     *
     * @param uid 待获取用户id
     * @return 用户信息
     */
    public UserInfo getUserInfo(int uid) {
        UserInfo userInfo = null;
        userInfo = userInfoMapper.getUserInfoByUid(uid);
        return userInfo;
    }

    /**
     * 获取用户信息的服务，根据用户名获取用户信息
     *
     * @param username 待获取用户名
     * @return 用户信息
     */
    public UserInfo getUserInfo(String username) {
        UserInfo userInfo = null;
        userInfo = userInfoMapper.getUserInfoByUsername(username);
        return userInfo;
    }

    public UserLogin getUserLogin(String username) {
        return userLoginMapper.getUserLoginByUsername(username);
    }

    /**
     * 获取所有用户信息
     *
     * @return 用户信息列表
     */
    public List<UserInfo> getUserInfos() {
        List<UserInfo> list = null;
        list = userInfoMapper.getUserInfos();
        return list;
    }

    /**
     * 删除用户的服务，根据传进的uid，删除DB中的表项
     *
     * @param uid 待删除用户id
     * @return 删除结果
     */
    @Transactional
    public int delUser(int uid) {
        int res = -1;
        do {
            if (userInfoMapper.getUserInfoByUid(uid) == null) {
                res = 1; // 用户不存在
                break;
            }
            userInfoMapper.deleteUserInfoByUid(uid);
            userLoginMapper.deleteUserLoginByUid(uid);
            res = 0;
        } while (false);
        return res;
    }

    /**
     * 删除用户的服务，根据用户名删除用户
     *
     * @param username 待删除用户名
     * @return 删除结果
     */
    @Transactional
    public int delUser(String username) {
        int res = -1;
        UserInfo u = null;
        do {
            u = userInfoMapper.getUserInfoByUsername(username);
            if (u == null) {
                res = 1; // 用户不存在
                break;
            }
            int uid = u.getUid();
            userInfoMapper.deleteUserInfoByUid(uid);
            userLoginMapper.deleteUserLoginByUid(uid);
            res = 0;
        } while (false);
        return res;
    }

    public int updateBalance(UserInfo userInfo) {
        int res = -1;
        do {
            if (userInfoMapper.getUserInfoByUid(userInfo.getUid()) == null) {
                res = 1;
                break;
            }
            userInfoMapper.updateBalance(userInfo);
            res = 0;
        } while (false);
        return res;
    }
}
