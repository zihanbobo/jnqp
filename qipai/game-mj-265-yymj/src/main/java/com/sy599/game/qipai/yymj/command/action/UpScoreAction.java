package com.sy599.game.qipai.yymj.command.action;

import com.sy599.game.qipai.yymj.bean.YyMjPlayer;
import com.sy599.game.qipai.yymj.bean.YyMjTable;
import com.sy599.game.qipai.yymj.command.AbsCodeCommandExecutor;
import com.sy599.game.websocket.constant.WebSocketMsgType;

/**
 * 飘分操作
 * @author Guang.OuYang
 * @date 2019/9/2-16:58
 */
public class UpScoreAction extends AbsCodeCommandExecutor<YyMjTable, YyMjPlayer> {
    @Override
    public Integer actionCode() {
        return WebSocketMsgType.req_code_piao_fen;  //缺省操作
    }

    @Override
    public GlobalCommonIndex globalCommonIndex() {
        return GlobalCommonIndex.COMMAND_INDEX;
    }

    @Override
    public void execute(YyMjTable table, YyMjPlayer player, CarryMessage carryMessage) {
//        if (table.getGameModel().getSpecialPlay().getKePiao() == 1) {
//            ComMsg.ComReq req = carryMessage.parseFrom(ComMsg.ComReq.class);
//            int paoFen = req.getParams(0);
//            player.setPiaoPoint(paoFen <= 0 ? 0 : paoFen);
//            ComMsg.ComRes.Builder com = SendMsgUtil.buildComRes(WebSocketMsgType.res_code_piao_fen, player.getSeat(), player.getPiaoPoint());
//            for (Player tableplayer : table.getSeatMap().values()) {// 推送客户端玩家抛分情况
//                tableplayer.writeSocket(com.build());
//            }
//            table.checkDeal();
//        }
    }

}
