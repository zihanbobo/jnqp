package com.sy599.game.qipai.ddz.command.play;

import com.sy599.game.common.constant.SharedConstants.table_state;
import com.sy599.game.gcommand.BaseCommand;
import com.sy599.game.msg.serverPacket.PlayCardReqMsg.PlayCardReq;
import com.sy599.game.msg.serverPacket.PlayCardResMsg.PlayCardRes;
import com.sy599.game.msg.serverPacket.TableRes.ClosingInfoRes;
import com.sy599.game.qipai.ddz.bean.DdzPlayer;
import com.sy599.game.qipai.ddz.bean.DdzTable;
import com.sy599.game.websocket.constant.WebSocketMsgType;
import com.sy599.game.websocket.netty.coder.MessageUnit;

import java.util.ArrayList;
import java.util.List;

public class DdzPlayCommand extends BaseCommand<DdzPlayer> {
    @Override
    public void execute(DdzPlayer player, MessageUnit message) throws Exception {
        DdzTable table = player.getPlayingTable(DdzTable.class);
        if (table == null) {
            return;
        }
        if (table.getState() != table_state.play) {
            return;
        }

        if (player.getSeat() != table.getNowDisCardSeat()) {
            // 没轮到出牌
            return;
        }

        PlayCardReq playCard = (PlayCardReq) recognize(PlayCardReq.class, message);

        List<Integer> cards = new ArrayList<>(playCard.getCardIdsList());
        int action = playCard.getCardType();
        //List<Integer> disCardIds = table.getNowDisCardIds();
       // int action =0;

        if (!player.isAutoPlay()) {
            player.setAutoPlay(false, table);
            player.setLastOperateTime(System.currentTimeMillis());
        }
        table.playCommand(player,action, cards);

    }

    @Override
    public void setMsgTypeMap() {
        msgTypeMap.put(PlayCardRes.class, WebSocketMsgType.sc_playcardres);
        msgTypeMap.put(ClosingInfoRes.class, WebSocketMsgType.sc_closinginfores);
    }

}
