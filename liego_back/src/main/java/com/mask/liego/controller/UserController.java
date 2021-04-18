package com.mask.liego.controller;

import com.mask.liego.entity.UserInfo;
import com.mask.liego.entity.UserLogin;
import com.mask.liego.service.UserService;
import com.mask.liego.tools.ApiResponse;
import com.mask.liego.tools.TokenUse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
public class UserController {
    @Resource
    private UserService service;

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

    @PostMapping(path = "/RegisterUser") // Register后应该跳到login页面
    @ResponseBody
    public Map registerUser(@RequestBody Map<String, String> body) {
        log("RegisterUser");
        ApiResponse res = new ApiResponse();
        try {
            int s = 0;
            UserLogin login = new UserLogin(body.get("username"), body.get("password"));
            UserInfo info = new UserInfo(body.get("username"), body.get("sno"), body.get("phone"));
            s = service.registerUser(login, info);
            res.setStatus(s);
            String[] msg = new String[]{"注册成功", "用户名已存在", "学号已存在", "系统错误"};
            res.setMessage(msg[s]);
        } catch (Exception e) {
            res.setErr();
        }
        return res.raw();
    }

    public void log(String s) {
        System.out.println(s);
    }

    @PostMapping(path = "/LoginUser") // 正确登陆后应该返回一个cookies或者token，或者使用session？
    @ResponseBody
    public Map loginUser(@RequestBody Map<String, String> body) {
        log("LoginUser");
        ApiResponse res = new ApiResponse();
        try {
            int s = 0;
            String token = "";
            UserLogin login = new UserLogin(body.get("username"), body.get("password"));
            s = service.login(login); // 返回登陆状态
            if (s == 0) {
                UserLogin u1 = service.getUserLogin(login.getUsername());
                int auth = u1.getAuthority();
                int uid = u1.getUid();
                token = TokenUse.sign(uid, login.getUsername(), auth);
            }
            res.setStatus(s);
            String[] msg = new String[]{"登陆成功", "用户不存在", "密码错误"};
            res.setMessage(msg[s]);
            Map<String, String> data = new HashMap<>();
            data.put("token", token);
            res.setData(data);
        } catch (Exception e) {
            res.setErr();
        }
        return res.raw();
    }

    @PostMapping(path = "/ResetUser") // Reset后应该跳到login页面
    public Map resetUser(@RequestBody Map<String, String> body) {
        log("ResetUser");
        ApiResponse res = new ApiResponse();
        try {
            int s = 0;
            UserLogin login = new UserLogin(body.get("username"), body.get("password"));
            UserInfo info = new UserInfo();
            info.setUsername(body.get("username"));
            info.setSno(body.get("sno"));
            info.setPhone(body.get("phone"));
            s = service.resetPassword(login, info); // 返回重置状态
            res.setStatus(s);
            String[] msg = new String[]{"重置成功", "用户不存在", "用户信息错误"};
            res.setMessage(msg[s]);
            System.out.println(msg[s]);
        } catch (Exception e) {
            e.printStackTrace();
            res.setErr();
        }
        return res.raw();
    }

    @PostMapping(path = "/ViewUserE")
    @ResponseBody
    public Map viewUserE(HttpServletRequest request, @RequestBody Map<String, String> body) {
        ApiResponse res = new ApiResponse();
        try {
            UserInfo userInfo = null;
            userInfo = service.getUserInfo(Integer.parseInt(body.get("uid")));
            if (userInfo == null) {
                res.setStatus(1);
                res.setMessage("用户不存在");
            } else {
                Map<String, Object> data = new HashMap<>();
                data.put("uid", userInfo.getUid());
                data.put("username", userInfo.getUsername());
                data.put("phone", userInfo.getPhone());
                res.setData(data);
            }
        } catch (Exception e) {
            res.setErr();
        }
        return res.raw();
    }

    @PostMapping(path = "/ViewUser") // 返回用户信息
    @ResponseBody
    public Map viewUser(HttpServletRequest request, @RequestBody Map<String, String> body) {
        log("ViewUser");
        ApiResponse res = new ApiResponse();
        try {
            UserInfo userInfo = null;
            if (getUsername(request).equals(body.get("username"))) { // 只能查看自己的信息
                userInfo = service.getUserInfo(body.get("username"));
            }
            if (userInfo == null) {
                res.setStatus(1);
                res.setMessage("用户不存在");
            } else {
                res.setData(userInfo);
            }
        } catch (Exception e) {
            res.setErr();
        }
        return res.raw();
    }

    @PostMapping(path = "/ViewUsers") // 返回用户信息
    @ResponseBody
    public Map viewUsers(HttpServletRequest request, @RequestBody Map<String, String> body) {
        log("ViewUsers");
        ApiResponse res = new ApiResponse();
        try {
            List<UserInfo> list = new LinkedList<>();
            if (getAuth(request) == 0) { // 管理员权限
                list = service.getUserInfos();
            }
            res.setData(list);
        } catch (Exception e) {
            res.setErr();
        }
        return res.raw();
    }

    @PostMapping(path = "/DelUser")
    @ResponseBody
    public Map delUser(HttpServletRequest request, @RequestBody Map<String, String> body) {
        log("DelUser");
        ApiResponse res = new ApiResponse();
        try {
            int s = 0;
            if (getAuth(request) == 0) {
                s = service.delUser(body.get("username"));
            }
            String[] msgs = new String[]{"删除成功", "删除失败"};
            res.setStatus(s);
            res.setMessage(msgs[s]);
        } catch (Exception e) {
            res.setErr();
        }
        return res.raw();
    }

    @PostMapping(path = "/Charge")
    @ResponseBody
    public Map charge(HttpServletRequest request, @RequestBody Map<String, String> body) {
        log("Charge");
        ApiResponse res = new ApiResponse();
        try {
            int s = 0;
            if (getAuth(request) == 0) {
                UserInfo u = new UserInfo();
                u.setUid(Integer.parseInt(body.get("uid")));
                u.setBalance(Double.parseDouble(body.get("balance")));
                s = service.updateBalance(u);
            }
            String[] msg = new String[]{"充值成功", "充值失败"};
            res.setStatus(s);
            res.setMessage(msg[s]);
        } catch (Exception e) {
            res.setErr();
        }
        return res.raw();
    }

}
