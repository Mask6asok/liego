package com.mask.liego.tools;

import java.util.HashMap;
import java.util.Map;

public class ApiResponse {

    private Map<String, Object> res;

    public ApiResponse() {
        res = new HashMap<>();
        res.put("status", 0);
        res.put("message", "");
        res.put("data", null);
    }

    public Map<String, Object> raw() {
        return res;
    }

    public void setErr() {
        res.put("status", 1);
        res.put("message", "未知错误");
        res.put("data", null);
    }


    public void setStatus(int status) {
        res.put("status", status);
    }

    public void setMessage(String msg) {
        res.put("message", msg);
    }

    public void setData(Object da) {
        res.put("data", da);
    }

}
