package com.sy599.game.gcommand.com;

import com.alibaba.fastjson.JSONObject;
import com.sy599.game.character.Player;
import com.sy599.game.common.constant.LangMsg;
import com.sy599.game.common.constant.RedBagSourceType;
import com.sy599.game.db.bean.RedBagInfo;
import com.sy599.game.db.bean.RedBagReceiveRecord;
import com.sy599.game.db.bean.RedBagSystemInfo;
import com.sy599.game.db.bean.activityRecord.UserActivityRecord;
import com.sy599.game.db.dao.RedBagInfoDao;
import com.sy599.game.db.enums.CardSourceType;
import com.sy599.game.gcommand.BaseCommand;
import com.sy599.game.gcommand.com.activity.ShareActivityCmd;
import com.sy599.game.manager.RedBagManager;
import com.sy599.game.msg.serverPacket.ComMsg;
import com.sy599.game.staticdata.bean.ActivityConfig;
import com.sy599.game.staticdata.bean.LuckyRedBagAcitivityConfig;
import com.sy599.game.util.LangHelp;
import com.sy599.game.util.LogUtil;
import com.sy599.game.util.TimeUtil;
import com.sy599.game.websocket.constant.WebSocketMsgType;
import com.sy599.game.websocket.netty.coder.MessageUnit;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 转盘抽奖活动
 */
public class LuckyRedbagCommand extends BaseCommand {

    @Override
    public void execute(Player player, MessageUnit message) throws Exception {
        ComMsg.ComReq req = (ComMsg.ComReq) this.recognize(ComMsg.ComReq.class, message);
        List<Integer> reqParams = req.getParamsList();
        int requestType = reqParams.get(0);// params参数第0位 表示请求的操作类型 0打开 1领取
        UserActivityRecord userActivityRecord = player.getMyActivity().getUserActivityRecord();
        int dayBureau = userActivityRecord.getLuckRedbagDayBureau();  //当天对局数
        JSONObject sendObj = new JSONObject();
        int isShared = ShareActivityCmd.isShared(player, "");
        int[] reward = canLuckyDraw(player, isShared, dayBureau);
        int count = 0;
        for(int rew : reward)
        {
            count += rew;
        }
        LuckyRedBagAcitivityConfig configInfo = (LuckyRedBagAcitivityConfig) ActivityConfig.getActivityConfigInfo(ActivityConfig.activity_lucky_redbag);
        if(requestType == 0) { // 打开活动界面
            sendObj.put("myRedBag", getTotalRedbag(player.getUserId()));// 从数据库查询当前累计红包金额
            //System.err.println("myRedBag: "+getTotalRedbag(player.getUserId()));
            sendObj.put("canDraw", count);
            RedBagSystemInfo systemInfo = RedBagManager.getInstance().getInstance().getRedBagSystemInfo();
            List<RedBagReceiveRecord> receiveRecords = new ArrayList<>();
            List<RedBagReceiveRecord> all = new ArrayList<>(systemInfo.getReceiveRecordList());
            for(RedBagReceiveRecord item : all) {
                receiveRecords.add(item);
            }
            sendObj.put("receiveRecords" , receiveRecords);
        } else if(requestType == 1){
            String activityEndDateStr = configInfo.getParams();
            Date endDate = new Date(TimeUtil.parseTimeInMillis(activityEndDateStr));
            if(System.currentTimeMillis() >= endDate.getTime()) {
                player.writeErrMsg(LangHelp.getMsg(LangMsg.code_203));
                return;
            }
            if(count <= 0) {
                player.writeErrMsg(LangHelp.getMsg(LangMsg.code_209));
                return;
            }
            Date nowDate = new Date();
            Date todayStartTime = TimeUtil.getDateByString(TimeUtil.formatDayTime2(nowDate) + " 00:00:00", "yyyy-MM-dd HH:mm:ss");
            Date todayEndTime = TimeUtil.getDateByString(TimeUtil.formatDayTime2(nowDate) + " 23:59:59", "yyyy-MM-dd HH:mm:ss");
            int threeNum = RedBagInfoDao.getInstance().getRedbagNum(5.88F, todayStartTime, todayEndTime);
            int fourNum = RedBagInfoDao.getInstance().getRedbagNum(18.88F, todayStartTime, todayEndTime);  //指定金额的红包数量[4 5] 等级 数量
            List<Integer> filter = new ArrayList<>();
            int randomGrade;
            if(threeNum >= 5 || fourNum >= 7) {
                if(threeNum >= 5) {
                    filter.add(3);
                }
                if(fourNum >= 7) {
                    filter.add(4);
                }
                filter.add(5);
                filter.add(6);
                randomGrade = configInfo.randomGrade(filter);
            } else {
                randomGrade = configInfo.randomGrade();  //随机红包等级
            }
            float[] gradeInfo = configInfo.getGrades().get(randomGrade);
            int redBagType = (int)gradeInfo[1];
            float redBag = gradeInfo[2];
            if(redBagType == 1 && redBag > 0) {
                player.changeCards((int)redBag, 0, true, -1, CardSourceType.activity_luckRedBag);
                player.writeComMessage(WebSocketMsgType.res_code_com, "恭喜您获得：钻石x" + (int)redBag);
                LogUtil.msgLog.info(player.getName() + "恭喜您获得：钻石x" + (int)redBag);
            }
            if((redBagType == 1 || redBagType == 2) && redBag > 0) {  //领取记录
                RedBagInfo redBagInfo = new RedBagInfo(player.getUserId(), redBagType, redBag, new Date(), null, RedBagSourceType.lucky_redbag);
                RedBagInfoDao.getInstance().saveRedBagInfo(redBagInfo);
                if(redBagType == 2) {// 记录跑马灯
                    RedBagManager.getInstance().addRedBagReceiveRecord(new RedBagReceiveRecord(player.getName(), redBag));
                }
            }

            if(reward[0] == 1)
            {
                userActivityRecord.setLuckyRedbagShareReceiveTime(System.currentTimeMillis());  //更新分享领取时间
                reward[0] = 0;
            } else if(reward[1] == 1) {
                userActivityRecord.setLuckyRedbagGameReceiveTime(System.currentTimeMillis()); //更新对局领取时间
                reward[1] = 0;
            }
            count--;
            player.getMyActivity().updateActivityRecord(userActivityRecord);
            sendObj.put("grade", randomGrade);
            sendObj.put("myRedBag",((float)(Math.round(getTotalRedbag(player.getUserId())*100))/100));// 从数据库查询当前累计红包金额
            //System.err.println("myRedbag: "+((float)(Math.round(getTotalRedbag(player.getUserId())*100))/100));
            sendObj.put("canDraw", count);
            RedBagSystemInfo systemInfo = RedBagManager.getInstance().getRedBagSystemInfo();
            List<RedBagReceiveRecord> receiveRecords = new ArrayList<>();
            List<RedBagReceiveRecord> all = new ArrayList<>(systemInfo.getReceiveRecordList());
            for(RedBagReceiveRecord item : all) {
                receiveRecords.add(item);
            }
            sendObj.put("receiveRecords" , receiveRecords);
        }

        sendObj.put("params", configInfo.getParams());
        sendObj.put("desc", configInfo.getDesc());
        sendObj.put("startTime", TimeUtil.formatTime(configInfo.getStartTime()));
        sendObj.put("endTime", TimeUtil.formatTime(configInfo.getEndTime()));
        sendObj.put("rewards", configInfo.getRewards());
        sendObj.put("requestType", requestType);
        player.writeComMessage(WebSocketMsgType.cs_luck_redbag, sendObj.toJSONString(), requestType);
    }

    private float getTotalRedbag(long userId) {
        float totalAmount = 0.0f;
        List<RedBagInfo> list = RedBagInfoDao.getInstance().getUserRedBagInfos(userId);
        if(list != null && !list.isEmpty()) {
            for(RedBagInfo info : list) {
                if(info.getRedBagType() == 2 && info.getDrawDate() == null) {
                    totalAmount += info.getRedbag();
                }
            }
        }
        DecimalFormat fnum = new DecimalFormat("#0.00");
        return Float.parseFloat(fnum.format(totalAmount));
    }


    public static void setInnings(Player player)
    {
        if(ActivityConfig.isActivityOpen(ActivityConfig.activity_lucky_redbag))  //益阳千分转盘抽奖活动开启
        {
            UserActivityRecord userActivityRecord = player.getMyActivity().getUserActivityRecord();
            long time = userActivityRecord.getLuckRedbagBureauTime();  //最新大局完结时间
            int dayBureau = userActivityRecord.getLuckRedbagDayBureau(); //当天完成大局数
            if(TimeUtil.isSameDay(time, System.currentTimeMillis()))
            {
                userActivityRecord.setLuckRedbagDayBureau(dayBureau + 1);  //更新当天大局数
            }
            else
            {
                userActivityRecord.setLuckRedbagDayBureau(1);  //重置新一天大局数
            }
            userActivityRecord.setLuckRedbagBureauTime(System.currentTimeMillis());  //更新玩家最新完成大局时间
            player.getMyActivity().updateActivityRecord(userActivityRecord);
        }
    }

    /**
     * 是否登陆打开转盘抽奖活动
     * @param player
     * @return
     */
    public static int canOpenLuckyDraw(Player player) {
        if(!ActivityConfig.isActivityOpen(ActivityConfig.activity_lucky_redbag)) {
            return 2;
        }
        LuckyRedBagAcitivityConfig configInfo = (LuckyRedBagAcitivityConfig) ActivityConfig.getActivityConfigInfo(ActivityConfig.activity_lucky_redbag);
        String activityEndDateStr = configInfo.getParams();
        Date endDate = new Date(TimeUtil.parseTimeInMillis(activityEndDateStr));
        if(System.currentTimeMillis() >= endDate.getTime())// 活动结束不能领取
            return 0;
        int isShared = ShareActivityCmd.isShared(player, "");
        int dayBureau =  player.getMyActivity().getUserActivityRecord().getLuckRedbagDayBureau();  //当天对局数
        int[] reward = canLuckyDraw(player, isShared, dayBureau);
        int count = 0;
        for(int rew : reward)
        {
            count += rew;
        }
        if(isShared == 0 || count > 0) {
            return 1;
        } else
            return 0;
    }

    public static int[] canLuckyDraw(Player player, int isShared, int dayBureau) {
        int[] reward = new int[2];
        for(int i = 0; i < reward.length; i++)
        {
            reward[i] = 0;
        }
        LuckyRedBagAcitivityConfig configInfo = (LuckyRedBagAcitivityConfig) ActivityConfig.getActivityConfigInfo(ActivityConfig.activity_lucky_redbag);
        if(configInfo == null)
            return reward;
        String activityEndDateStr = configInfo.getParams();
        Date endDate = new Date(TimeUtil.parseTimeInMillis(activityEndDateStr));  //获取活动结束的时间
        if(System.currentTimeMillis() >= endDate.getTime()) // 活动结束不能领取
            return reward;
        UserActivityRecord  userActivityRecord = player.getMyActivity().getUserActivityRecord();
        int playBureau = configInfo.getPlayBureau();  //需要游戏局数
        boolean achieve = dayBureau >= playBureau;  //是否完成指定游戏局数
        long shareTime = userActivityRecord.getLuckyRedbagShareReceiveTime();  //获取最新分享领取时间
        long gameTime = userActivityRecord.getLuckyRedbagGameReceiveTime();  //最新对局领取时间
        if(isShared == 0 && !achieve) {// 没分享过且没完成指定局数
            return reward;
        } else{
            if(isShared != 0 && (shareTime == 0 || !TimeUtil.isSameDay(shareTime, System.currentTimeMillis())))
            {
                reward[0] = 1;
            }
            if(achieve && (gameTime == 0 || !TimeUtil.isSameDay(gameTime, System.currentTimeMillis())))
            {
                reward[1] = 1;
            }
        }
        return reward;
    }

    @Override
    public void setMsgTypeMap() {
    }
}
