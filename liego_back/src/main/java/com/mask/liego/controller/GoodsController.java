package com.mask.liego.controller;

import com.mask.liego.entity.*;
import com.mask.liego.service.GoodsService;
import com.mask.liego.service.UserService;
import com.mask.liego.tools.ApiResponse;
import com.mask.liego.tools.ImageUpload;
import com.mask.liego.tools.TokenUse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cglib.beans.BeanMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.util.*;

@Slf4j
@RestController
public class GoodsController {
    @Resource
    private GoodsService goodsService;
    @Resource
    private UserService userService;

    public void log(String s) {
        System.out.println(s);
    }

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

    @PostMapping(path = "PubGoods")
    @ResponseBody
    public Map publishGoods(HttpServletRequest request, @RequestBody Map<String, Object> body) {
        log("PubGoods");
        int s = 0;
        ApiResponse res = new ApiResponse();
        try {
            Description description = new Description(
                    body.get("descrip_text").toString(),
                    body.get("descrip_img1").toString(),
                    body.get("descrip_img2").toString(),
                    body.get("descrip_img3").toString());
            GoodsInfo goodsInfo = new GoodsInfo(
                    body.get("gname").toString(),
                    Integer.parseInt(body.get("gtype").toString()),
                    Integer.parseInt(body.get("uid").toString()),
                    Double.parseDouble(body.get("price").toString()),
                    Integer.parseInt(body.get("count").toString()),
                    0);
            if (getUid(request) == goodsInfo.getUid()) {
                s = goodsService.publishGoods(goodsInfo, description);
            }
            String[] msg = new String[]{"发布成功", "用户不存在"};
            res.setStatus(s);
            res.setMessage(msg[s]);
        } catch (Exception e) {
            e.printStackTrace();
            res.setErr();
        }
        return res.raw();
    }

    @PostMapping(path = "ViewGoodsInfo")
    @ResponseBody
    public Map viewGoodsInfo(@RequestBody Map<String, Integer> body) {
        log("ViewGoodsInfo");
        ApiResponse res = new ApiResponse();
        try {
            GoodsInfo goodsInfo = null;
            goodsInfo = goodsService.getGoodsInfo(body.get("gid"));
            if (goodsInfo == null) {
                res.setStatus(1);
                res.setMessage("商品不存在");
            } else {
                res.setData(goodsInfo);
            }
        } catch (Exception e) {
            res.setErr();
        }
        return res.raw();
    }

    @PostMapping(path = "ViewGoodsInfoW")
    @ResponseBody
    public Map viewGoodsInfoW(@RequestBody Map<String, Integer> body) {
        log("ViewGoodsInfo");
        ApiResponse res = new ApiResponse();
        GoodsInfo goodsInfo = null;
        GoodsPublish goodsPublish = null;
        try {
            goodsInfo = goodsService.getGoodsInfo(body.get("gid"));
            if (goodsInfo == null) {
                res.setStatus(1);
                res.setMessage("商品不存在");
            } else {
                goodsPublish = goodsService.getGoodsPublish(goodsInfo.getGid());
                assert goodsPublish != null;
                Description d = goodsService.getDescription(goodsInfo.getDescriptionID());
                Map<String, Object> m = new HashMap<>();
                m.put("gid", goodsInfo.getGid());
                m.put("gname", goodsInfo.getGname());
                m.put("gtype", goodsInfo.getGtype());
                m.put("uid", goodsInfo.getUid());
                m.put("price", goodsInfo.getPrice());
                m.put("count", goodsInfo.getCount());
                m.put("disaplay", goodsPublish.getDisplay());
                m.put("descriptionID", goodsInfo.getDescriptionID());
                m.put("descrip_text", d.getDescrip_text());
                m.put("descrip_img1", d.getDescrip_img1());
                m.put("descrip_img2", d.getDescrip_img2());
                m.put("descrip_img3", d.getDescrip_img3());
                res.setData(m);
            }
        } catch (Exception e) {
            res.setErr();
        }
        return res.raw();
    }

    @PostMapping(path = "ViewGoodsInfosByUid")
    @ResponseBody
    public Map viewGoodsInfosByUid(HttpServletRequest request,
                                   @RequestBody Map<String, Integer> body) {
        log("ViewGoodsInfosByUid");
        ApiResponse res = new ApiResponse();
        try {
            List<GoodsInfo> glist = new LinkedList<>();
            glist = goodsService.getGoodsInfosByUid(body.get("uid"));
            List<Map<String, Object>> r = new LinkedList<Map<String, Object>>();
            for (GoodsInfo g : glist) {
                BeanMap m1 = BeanMap.create(g);
                Map<String, Object> m2 = new HashMap<>();
                for (Object k : m1.keySet()) {
                    m2.put(k.toString(), m1.get(k));
                }
                m2.put("ptime", goodsService.getGoodsPublish(g.getGid()).getPubTime());
                r.add(m2);
            }
            res.setData(r);
        } catch (Exception e) {
            res.setErr();
        }
        return res.raw();
    }

    @PostMapping(path = "ViewGoodsInfosW")
    @ResponseBody
    public Map ViewGoodsInfosW(@RequestBody Map<String, Object> body) {
        log("ViewGoodsInfos");
        ApiResponse res = new ApiResponse();
        try {
            List<GoodsInfo> glist = null;
            glist = goodsService.getGoodsInfos();
            List<Map<String, Object>> r = new LinkedList<Map<String, Object>>();
            for (GoodsInfo g : glist) {
                BeanMap m1 = BeanMap.create(g);
                Description d = goodsService.getDescription(g.getDescriptionID());
                Map<String, Object> m2 = new HashMap<>();
                for (Object k : m1.keySet()) {
                    m2.put(k.toString(), m1.get(k));
                }
                m2.put("ptime", goodsService.getGoodsPublish(g.getGid()).getPubTime());
                m2.put("descrip_text", d.getDescrip_text());
                m2.put("descrip_img1", d.getDescrip_img1());
                m2.put("descrip_img2", d.getDescrip_img2());
                m2.put("descrip_img3", d.getDescrip_img3());
                r.add(m2);
            }
            res.setData(r);
        } catch (Exception e) {
            res.setErr();
        }
        return res.raw();
    }

    @PostMapping(path = "ViewGoodsInfos")
    @ResponseBody
    public Map viewGoodsInfos(HttpServletRequest request, @RequestBody Map<String, Object> body) {
        log("ViewGoodsInfos");
        ApiResponse res = new ApiResponse();
        try {
            List<GoodsInfo> glist = new LinkedList<>();
            if (getAuth(request) == 0) {
                glist = goodsService.getGoodsInfos();
            }
            List<Map<String, Object>> r = new LinkedList<Map<String, Object>>();
            for (GoodsInfo g : glist) {
                BeanMap m1 = BeanMap.create(g);
                Map<String, Object> m2 = new HashMap<>();
                for (Object k : m1.keySet()) {
                    m2.put(k.toString(), m1.get(k));
                }
                m2.put("ptime", goodsService.getGoodsPublish(g.getGid()).getPubTime());
                r.add(m2);
            }
            res.setData(r);
        } catch (Exception e) {
            res.setErr();
        }
        return res.raw();
    }

    @PostMapping(path = "ViewGoodsPub")
    @ResponseBody
    public Map viewGoodsPub(HttpServletRequest request, @RequestBody Map<String, Integer> body) {
        log("ViewGoodsPub");
        ApiResponse res = new ApiResponse();
        try {
            GoodsPublish goodsPublish = null;
            if (getUid(request) == body.get("uid")) {
                goodsPublish = goodsService.getGoodsPublish(body.get("uid"));
            }
            res.setData(goodsPublish);
        } catch (Exception e) {
            res.setErr();
        }
        return res.raw();
    }

    @PostMapping(path = "ViewDescription")
    @ResponseBody
    public Map viewDescription(@RequestBody Map<String, Integer> body) {
        log("ViewDescription");
        ApiResponse res = new ApiResponse();
        try {
            Description description = null;
            description = goodsService.getDescription(body.get("gid"));
            res.setData(description);
        } catch (Exception e) {
            res.setErr();
        }
        return res.raw();
    }

    @PostMapping(path = "DelGoods")
    @ResponseBody
    public Map delGoods(HttpServletRequest request, @RequestBody Map<String, Integer> body) {
        log("DelGoods");
        ApiResponse res = new ApiResponse();
        try {
            int s = 0;
            int uid = getUid(request);
            if (goodsService.getGoodsInfo(body.get("gid")).getUid() == uid) {
                s = goodsService.deleteGoods(body.get("gid"));
            }
            String[] msg = new String[]{"删除成功", "商品不存在"};
            res.setStatus(s);
            res.setMessage(msg[s]);
        } catch (Exception e) {
            res.setErr();
        }
        return res.raw();
    }

    @PostMapping(path = "LeaveMsg")
    @ResponseBody
    public Map leaveMsg(HttpServletRequest request, @RequestBody Map<String, String> body) {
        log("LeaveMsg");
        ApiResponse res = new ApiResponse();
        try {
            int s = 0;
            MsgInfo msgInfo = new MsgInfo(
                    Integer.parseInt(body.get("gid")),
                    Integer.parseInt(body.get("uid")),
                    body.get("msg"));
            if (getUid(request) == Integer.parseInt(body.get("uid"))) {
                s = goodsService.leaveMsg(msgInfo);
            }
            String[] msg = new String[]{"留言成功", "商品不存在", "用户不存在"};
            res.setStatus(s);
            res.setMessage(msg[s]);
        } catch (Exception e) {
            res.setErr();
        }
        return res.raw();
    }

    @PostMapping(path = "ReplyMsg")
    @ResponseBody
    public Map replyMsg(HttpServletRequest request, @RequestBody Map<String, String> body) {
        log("ReplyMsg");
        ApiResponse res = new ApiResponse();
        try {
            int s = 0;
            RplInfo rplInfo = new RplInfo(
                    Integer.parseInt(body.get("mid")),
                    Integer.parseInt(body.get("uid")),
                    body.get("msg"));
            if (getUid(request) == Integer.parseInt(body.get("uid"))) {
                s = goodsService.replyMsg(rplInfo);
            }
            String[] msg = new String[]{"回复成功", "留言不存在", "用户不存在"};
            res.setStatus(s);
            res.setMessage(msg[s]);
        } catch (Exception e) {
            res.setErr();
        }
        return res.raw();
    }

    @PostMapping(path = "ViewMsg")
    @ResponseBody
    public Map viewMsg(@RequestBody Map<String, Integer> body) {
        log("ViewMsg");
        ApiResponse res = new ApiResponse();
        try {
            MsgInfo msgInfo = null;
            msgInfo = goodsService.getMsg(body.get("mid"));
            res.setData(msgInfo);
        } catch (Exception e) {
            res.setErr();
        }
        return res.raw();
    }

    @PostMapping(path = "ViewMsgs")
    @ResponseBody
    public Map viewMsgs(@RequestBody Map<String, Integer> body) {
        log("ViewMsgs");
        ApiResponse res = new ApiResponse();
        try {
            List<MsgInfo> list;
            list = goodsService.getMsgs(body.get("gid"));
            List<Map> data = new LinkedList<>();
            for (MsgInfo m : list) {
                Map<String, Object> mo = new HashMap<>();
                mo.put("mid", m.getMid());
                mo.put("uid", m.getUid());
                UserInfo u = userService.getUserInfo(m.getUid());
                if (u == null) {
                    mo.put("username", "User");
                } else {
                    mo.put("username", u.getUsername());
                }
                mo.put("msg", m.getMsg());
                mo.put("mtime", m.getMtime());
                data.add(mo);
            }
            res.setData(data);
        } catch (Exception e) {
            e.printStackTrace();
            res.setErr();
        }
        return res.raw();
    }

    @PostMapping(path = "ViewAllMsgs")
    @ResponseBody
    public Map viewAllMsgs(HttpServletRequest request, @RequestBody Map<String, Object> body) {
        log("ViewAllMsgs");
        ApiResponse res = new ApiResponse();
        try {
            List<MsgInfo> list = new LinkedList<>();
            if (getAuth(request) == 0) {
                list = goodsService.getAllMsgs();
            }
            List<Map<String, Object>> r = new LinkedList<Map<String, Object>>();
            for (MsgInfo msg : list) {
                BeanMap m1 = BeanMap.create(msg);
                Map<String, Object> m2 = new HashMap<>();
                for (Object k : m1.keySet()) {
                    m2.put(k.toString(), m1.get(k));
                }
                m2.put("mtime", m2.get("mtime"));
                r.add(m2);
            }
            res.setData(r);
        } catch (Exception e) {
            res.setErr();
        }
        return res.raw();
    }

    @PostMapping(path = "ViewRpl")
    @ResponseBody
    public Map viewRpl(@RequestBody Map<String, Integer> body) {
        ApiResponse res = new ApiResponse();
        try {
            RplInfo rplInfo;
            rplInfo = goodsService.getRpl(body.get("rid"));
            res.setData(rplInfo);
        } catch (Exception e) {
            res.setErr();
        }
        return res.raw();
    }

    @PostMapping(path = "ViewRpls")
    @ResponseBody
    public Map viewRpls(@RequestBody Map<String, Integer> body) {
        log("ViewRpls");
        ApiResponse res = new ApiResponse();
        try {
            List<RplInfo> list = null;
            list = goodsService.getRpls(body.get("mid"));
            List<Map> data = new LinkedList<>();
            for (RplInfo r : list) {
                Map<String, Object> ro = new HashMap<>();
                ro.put("rid", r.getRid());
                ro.put("uid", r.getUid());
                ro.put("mid", r.getMid());
                UserInfo u = userService.getUserInfo(r.getUid());
                if (u == null) {
                    ro.put("username", "User");
                } else {
                    ro.put("username", u.getUsername());
                }
                ro.put("msg", r.getMsg());
                ro.put("rtime", r.getRtime());
                data.add(ro);
            }
            res.setData(data);
        } catch (Exception e) {
            res.setErr();
        }
        return res.raw();
    }

    @PostMapping(path = "ViewAllRpls")
    @ResponseBody
    public Map viewAllRpls(HttpServletRequest request, @RequestBody Map<String, Object> body) {
        log("ViewAllRpls");
        ApiResponse res = new ApiResponse();
        try {
            List<RplInfo> list = new LinkedList<>();
            if (getAuth(request) == 0) {
                list = goodsService.getAllRpls();
            }
            List<Map<String, Object>> r = new LinkedList<Map<String, Object>>();
            for (RplInfo rpl : list) {
                BeanMap m1 = BeanMap.create(rpl);
                Map<String, Object> m2 = new HashMap<>();
                for (Object k : m1.keySet()) {
                    m2.put(k.toString(), m1.get(k));
                }
                m2.put("rtime", m2.get("rtime"));
                r.add(m2);
            }
            res.setData(r);
        } catch (Exception e) {
            res.setErr();
        }
        return res.raw();
    }

    @PostMapping(path = "DelMsg")
    @ResponseBody
    public Map delMsg(HttpServletRequest request, @RequestBody Map<String, Integer> body) {
        log("DelMsg");
        ApiResponse res = new ApiResponse();
        try {
            int s = 0;
            int uid = getUid(request);
            if (goodsService.getMsg(body.get("mid")).getUid() == uid || getAuth(request) == 0) {
                s = goodsService.delMsg(body.get("mid"));
            }
            res.setStatus(s);
            String[] msg = new String[]{"删除成功", "留言不存在"};
            res.setMessage(msg[s]);
        } catch (Exception e) {
            res.setErr();
        }
        return res.raw();
    }

    @PostMapping(path = "DelRpl")
    @ResponseBody
    public Map delRpl(HttpServletRequest request, @RequestBody Map<String, Integer> body) {
        ApiResponse res = new ApiResponse();
        try {
            int s = 0;
            int uid = getUid(request);
            if (goodsService.getRpl(body.get("rid")).getUid() == uid || getAuth(request) == 0) {
                s = goodsService.delRpl(body.get("rid"));
            }
            res.setStatus(s);
            String[] msg = new String[]{"删除成功", "回复不存在"};
            res.setMessage(msg[s]);
        } catch (Exception e) {
            res.setErr();
        }
        return res.raw();
    }

    @PostMapping(path = "UpdateGoods")
    @ResponseBody
    public Map updateGoods(HttpServletRequest request, @RequestBody Map<String, String> body) {
        log("UpdateGoods");
        ApiResponse res = new ApiResponse();
        try {
            int s = 0;
            Description description = new Description(
                    body.get("descrip_text"),
                    body.get("descrip_img1"),
                    body.get("descrip_img2"),
                    body.get("descrip_img3"));
            GoodsInfo goodsInfo = new GoodsInfo(
                    body.get("gname"),
                    Integer.parseInt(body.get("gtype")),
                    Integer.parseInt(body.get("uid")),
                    Double.parseDouble(body.get("price")),
                    Integer.parseInt(body.get("count")),
                    0);
            System.out.println(body.get("descrip_img2"));
            goodsInfo.setGid(Integer.parseInt(body.get("gid")));
            if (getUid(request) == Integer.parseInt(body.get("uid")) &&
                    goodsService.getGoodsInfo(Integer.parseInt(body.get("gid"))).getUid() == getUid(request)) {
                s = goodsService.updateGoodsInfo(goodsInfo) |
                        goodsService.updateDescription(goodsInfo.getGid(), description);
            }
            String[] msg = new String[]{"更新成功", "更新失败"};
            res.setStatus(s);
            res.setMessage(msg[s]);
        } catch (Exception e) {
            res.setErr();
        }
        return res.raw();
    }

    @PostMapping(path = "UploadImg")
    @ResponseBody
    public Map uploadImg(@RequestParam("file") MultipartFile file) {
        log("UploadImg");
        ApiResponse res = new ApiResponse();
        try {
            String path = ImageUpload.imageUpload(file);
            File f = new File(path);
            int s = -1;
            if (f.exists()) {
                s = 0;
            } else {
                s = 1;
            }
            String[] msg = new String[]{"上传成功", "上传失败"};
            res.setStatus(s);
            res.setMessage(msg[s]);
            Map<String, String> data = new HashMap<>();
            data.put("url", path);
            res.setData(data);
        } catch (Exception e) {
            res.setErr();
        }
        return res.raw();
    }
}
