package com.mask.liego.service;

import com.mask.liego.dao.*;
import com.mask.liego.entity.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

@Service
public class GoodsService {
    @Resource
    private GoodsInfoMapper goodsInfoMapper;
    @Resource
    private GoodsPublishMapper goodsPublishMapper;
    @Resource
    private DescriptionMapper descriptionMapper;
    @Resource
    private MsgInfoMapper msgInfoMapper;
    @Resource
    private RplInfoMapper rplInfoMapper;
    @Resource
    private UserInfoMapper userInfoMapper;

    /**
     * 发布商品服务，传进商品信息，在数据库中生产对应表项
     *
     * @param goodsInfo   商品信息
     * @param description 描述信息
     * @return res 发布结果，整形
     */
    @Transactional
    public int publishGoods(GoodsInfo goodsInfo, Description description) {
        int res = -1;
        do {
            if (userInfoMapper.getUserInfoByUid(goodsInfo.getUid()) == null) {
                res = 1; // 发布者不存在
                break;
            }
            descriptionMapper.insertDescription(description); // description DB
            goodsInfo.setDescriptionID(description.getDescriptionID());
            goodsInfoMapper.insertGoosInfo(goodsInfo); // goodsInfo DB
            GoodsPublish p = new GoodsPublish(goodsInfo.getGid(), new Timestamp(new Date().getTime()));
            goodsPublishMapper.insertGoodsPublish(p); // goodsPublish DB
            res = 0;
        } while (false);
        return res;
    }

    /**
     * 获取商品详细信息服务，根据指定商品id，获取商品详细信息
     *
     * @param gid 待查询商品的id
     * @return 返回商品详细信息
     */
    public GoodsInfo getGoodsInfo(int gid) {
        GoodsInfo infoInDB = null;
        infoInDB = goodsInfoMapper.getGoodsInfoByGid(gid);
        return infoInDB;
    }

    public List<GoodsInfo> getGoodsInfos() {
        List<GoodsInfo> list = null;
        list = goodsInfoMapper.getGoodsInfos();
        return list;
    }
    public List<GoodsInfo> getGoodsInfosByUid(int uid) {
        return goodsInfoMapper.getGoodsInfosByUid(uid);
    }
    /**
     * 获取商品发布信息服务，根据指定商品id，获取商品发布信息
     *
     * @param gid 待查询商品的id
     * @return 返回商品发布信息
     */
    public GoodsPublish getGoodsPublish(int gid) {
        GoodsPublish infoInDB = null;
        infoInDB = goodsPublishMapper.getGoodsPublishByGid(gid);
        return infoInDB;
    }

    /**
     * 获取商品描述信息服务，根据指定商品id，获取商品描述信息
     *
     * @param gid 待查询商品的id
     * @return 返回商品的描述信息
     */
    public Description getDescription(int gid) {
        Description description = null;
        do {
            GoodsInfo info = goodsInfoMapper.getGoodsInfoByGid(gid);
            if (info == null) {
                break;
            }
            int did = info.getDescriptionID();
            description = descriptionMapper.getDescriptionByDid(did);
        } while (false);
        return description;
    }

    /**
     * 更新商品详细信息服务，改变参数中uid对应表项的值
     *
     * @param goodsInfo 新的商品信息
     * @return 更新结果 整形
     */
    @Transactional
    public int updateGoodsInfo(GoodsInfo goodsInfo) {
        int res = -1;
        GoodsInfo infoInDB = null;
        do {
            infoInDB = goodsInfoMapper.getGoodsInfoByGid(goodsInfo.getGid()); // 获取DB中表项
            if (infoInDB == null) {
                res = 1; //不存在此商品
                break;
            }
            if (infoInDB.getUid() != goodsInfo.getUid()) {
                res = 2; // 商品主人不符
                break;
            }
            // 根据goodsInfo与infoInDB的不同，更新不同的地方
            if (infoInDB.getCount() != goodsInfo.getCount()) {
                goodsInfoMapper.updateCount(goodsInfo);
            }
            if (BigDecimal.valueOf(infoInDB.getPrice()).compareTo(BigDecimal.valueOf(goodsInfo.getPrice())) != 0) {
                goodsInfoMapper.updatePrice(goodsInfo);
            }
            if (!infoInDB.getGname().equalsIgnoreCase(goodsInfo.getGname())) {
                goodsInfoMapper.updateGname(goodsInfo);
            }
            if (infoInDB.getGtype() != goodsInfo.getGtype()) {
                goodsInfoMapper.updateType(goodsInfo);
            }
            res = 0;
        } while (false);
        return res;
    }

    /**
     * 更新商品描述信息服务，根据传进的商品id和描述信息，更新描述信息
     *
     * @param gid         待更新描述信息的商品的id
     * @param description 新的描述信息
     * @return 更新结果 整形
     */
    @Transactional
    public int updateDescription(int gid, Description description) {
        int res = -1;
        GoodsInfo infoInDB = null;
        Description desInDB = null;
        do {
            infoInDB = goodsInfoMapper.getGoodsInfoByGid(gid);
            if (infoInDB == null) {
                res = 1; // 该商品不存在
                break;
            }
            int did = infoInDB.getDescriptionID();
            desInDB = descriptionMapper.getDescriptionByDid(did);
            if (desInDB == null) {
                res = 2; // 原描述不存在
                break;
            }
            description.setDescriptionID(did);
            if (!desInDB.getDescrip_text().equalsIgnoreCase(description.getDescrip_text())) {
                descriptionMapper.updateDescripText(description);
            }
            if (!desInDB.getDescrip_img1().equals(description.getDescrip_img1())) {
                descriptionMapper.updateDescripImg1(description);
            }
            if (!desInDB.getDescrip_img2().equals(description.getDescrip_img2())) {
                descriptionMapper.updateDescripImg2(description);
            }
            if (!desInDB.getDescrip_img3().equals(description.getDescrip_img3())) {
                descriptionMapper.updateDescripImg3(description);
            }
            res = 0;
        } while (false);
        return res;
    }

    /**
     * 删除商品服务，通过传进的商品id，删除与此商品有关的表项
     * 包括goodsInfo goodsPublish description msgInfo rplInfo
     *
     * @param gid 待删除商品id
     * @return 删除结果
     */
    @Transactional
    public int deleteGoods(int gid) {
        int res = -1;
        GoodsInfo goodsInfo = null;
        do {
            goodsInfo = goodsInfoMapper.getGoodsInfoByGid(gid);
            if (goodsInfo == null) {
                res = 1; // 表项不存在
                break;
            }
            goodsInfoMapper.deleteGoodsInfoByGid(gid);
            if (goodsPublishMapper.getGoodsPublishByGid(gid) == null) {
                res = 1; //
                break;
            }
            goodsPublishMapper.deleteGoodsPublishByGid(gid);
            int did = goodsInfo.getDescriptionID();
            if (descriptionMapper.getDescriptionByDid(did) == null) {
                res = 1; //
                break;
            }
            descriptionMapper.deleteDescriptionByDid(did);
            List<Integer> midList = msgInfoMapper.getMidByGid(gid); // 该商品下的留言id列表
            for (int mid : midList) {
                msgInfoMapper.deleteMsgInfoByMid(mid);
                List<Integer> ridList = rplInfoMapper.getRidByMid(mid); // 该留言下的回复id列表
                for (int rid : ridList) {
                    rplInfoMapper.deleteRplInfoByRid(rid);
                }
            }
            res = 0;
        } while (false);
        return res;
    }

    /**
     * 商品留言服务，根据传进来的留言参数，在DB中生成对应表项
     *
     * @param msgInfo 留言参数
     * @return 留言结果 整形
     */
    public int leaveMsg(MsgInfo msgInfo) {
        int res = -1;
        do {
            if (goodsInfoMapper.getGoodsInfoByGid(msgInfo.getGid()) == null) {
                res = 1; // 商品不存在
                break;
            }
            if (userInfoMapper.getUserInfoByUid(msgInfo.getUid()) == null) {
                res = 2; // 留言者不存在
                break;
            }
            msgInfo.setMtime(new Timestamp(new Date().getTime()));
            msgInfoMapper.insertMsgInfo(msgInfo);
            res = 0;
        } while (false);
        return res;
    }

    /**
     * 删除留言的服务
     *
     * @param mid 待删除留言id
     * @return 删除结果
     */
    @Transactional
    public int delMsg(int mid) {
        int res = -1;
        do {
            if (msgInfoMapper.getMsgInfoByMid(mid) == null) {
                res = 1; // 留言不存在
                break;
            }
            msgInfoMapper.deleteMsgInfoByMid(mid);
            rplInfoMapper.deleteRplInfoByMid(mid);
            res = 0;
        } while (false);
        return res;
    }

    /**
     * 获取留言信息的服务
     *
     * @param mid 待获取留言id
     * @return id对应的留言信息
     */
    public MsgInfo getMsg(int mid) {
        MsgInfo msgInfo = null;
        msgInfo = msgInfoMapper.getMsgInfoByMid(mid);
        return msgInfo;
    }

    /**
     * 获取商品下所有留言信息的服务
     *
     * @param gid 商品id
     * @return 该商品的所有留言信息
     */
    public List<MsgInfo> getMsgs(int gid) {
        List<MsgInfo> list = null;
        do {
            if (goodsInfoMapper.getGoodsInfoByGid(gid) == null) {
                break; // 没有此商品
            }
            list = msgInfoMapper.getMsgInfosByGid(gid);
        } while (false);
        return list;
    }

    public List<MsgInfo> getAllMsgs() {
        return msgInfoMapper.getAllMsgs();
    }

    /**
     * 回复留言的服务
     *
     * @param rplInfo 回复参数
     * @return 回复结果 整形
     */
    public int replyMsg(RplInfo rplInfo) {
        int res = -1;
        do {
            if (msgInfoMapper.getMsgInfoByMid(rplInfo.getMid()) == null) {
                res = 1; // 回复的留言不存在
                break;
            }
            if (userInfoMapper.getUserInfoByUid(rplInfo.getUid()) == null) {
                res = 2; // 回复者不存在
                break;
            }
            rplInfo.setRtime(new Timestamp(new Date().getTime()));
            rplInfoMapper.insertRplInfo(rplInfo);
            res = 0;
        } while (false);
        return res;
    }

    /**
     * 删除回复的服务
     *
     * @param rid 待删除回复id
     * @return 删除结果 整形
     */
    public int delRpl(int rid) {
        int res = -1;
        do {
            if (rplInfoMapper.getRplInfoByRid(rid) == null) {
                res = 1; // 留言不存在
                break;
            }
            rplInfoMapper.deleteRplInfoByRid(rid);
            res = 0;
        } while (false);
        return res;
    }

    /**
     * 获取回复的服务
     *
     * @param rid 待获取回复的id
     * @return 回复信息
     */
    public RplInfo getRpl(int rid) {
        RplInfo rplInfo = null;
        rplInfo = rplInfoMapper.getRplInfoByRid(rid);
        return rplInfo;
    }

    /**
     * 获取一个留言下所有回复的服务
     *
     * @param mid 留言id
     * @return 该留言的回复列表
     */
    public List<RplInfo> getRpls(int mid) {
        List<RplInfo> list = null;
        do {
            if (msgInfoMapper.getMsgInfoByMid(mid) == null) {
                break; // 没有此留言
            }
            list = rplInfoMapper.getRplInfosByMid(mid);
        } while (false);
        return list;
    }

    public List<RplInfo> getAllRpls() {
        return rplInfoMapper.getAllRpls();
    }
}
