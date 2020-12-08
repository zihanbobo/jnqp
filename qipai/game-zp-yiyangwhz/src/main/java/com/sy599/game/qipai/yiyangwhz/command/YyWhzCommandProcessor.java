package com.sy599.game.qipai.yiyangwhz.command;

import com.sy599.game.character.Player;
import com.sy599.game.gcommand.BaseCommand;
import com.sy599.game.qipai.yiyangwhz.bean.YyWhzPlayer;
import com.sy599.game.util.ObjectUtil;
import com.sy599.game.websocket.netty.coder.MessageUnit;
import com.sy599.game.qipai.AbstractBaseCommandProcessor;
import com.sy599.game.qipai.yiyangwhz.command.com.YyWhzComCommand;
import com.sy599.game.qipai.yiyangwhz.command.play.YyWhzPlayCommand;
import com.sy599.game.util.LogUtil;
import com.sy599.game.websocket.constant.WebSocketMsgType;

import java.util.HashMap;
import java.util.Map;

public class YyWhzCommandProcessor extends AbstractBaseCommandProcessor {
    private static YyWhzCommandProcessor processor = new YyWhzCommandProcessor();
    private static Map<Short, Class<? extends BaseCommand>> commandMap = new HashMap<Short, Class<? extends BaseCommand>>();
    private static Map<Class<?>, Short> msgClassToMsgTypeMap = new HashMap<Class<?>, Short>();

    static {
        commandMap.put(WebSocketMsgType.cs_com, YyWhzComCommand.class);
        commandMap.put(WebSocketMsgType.cs_play, YyWhzPlayCommand.class);
    }

    public void process(YyWhzPlayer player, MessageUnit message) {
        int code = 0;
        try {
            BaseCommand action = ObjectUtil.newInstance(commandMap.get(message.getMsgType()));
            action.setPlayer(player);
            action.execute(player, message);
        } catch (Exception e) {
            LogUtil.e("socket err: " + player.getUserId() + " " + message.getMsgType() + " " + LogUtil.printlnLog(message.getMessage()), e);
            code = -1;
        } finally {
            if (code != 0) {
            }
        }
    }

    public short getMsgType(Class<?> clazz) {
        if (msgClassToMsgTypeMap.containsKey(clazz)) {
            return msgClassToMsgTypeMap.get(clazz);
        }
        return 0;
    }

    @Override
    public void process(Player player, MessageUnit message) {
        try {
            if (player == null || !(player instanceof YyWhzPlayer)) {
                return;
            }
            YyWhzPlayer yjGhzPlayer = (YyWhzPlayer) player;
            BaseCommand action = ObjectUtil.newInstance(commandMap.get(message.getMsgType()));
            action.setPlayer(player);
            action.execute(yjGhzPlayer, message);
        } catch (Exception e) {
            StringBuilder sb = new StringBuilder("CommandProcessor|NxGhz|error");
            if (player != null) {
                sb.append("|").append(player.getUserId());
                sb.append("|").append(player.getSeat());
                sb.append("|").append(player.getPlayingTableId());
            }
            if (message != null) {
                sb.append("|").append(message.getMsgType());
                sb.append("|").append(LogUtil.printlnLog(message.getMessage()));
            }
            sb.append("|").append(e.getMessage());
            LogUtil.errorLog.error(sb.toString(), e);
        }
    }

    public static AbstractBaseCommandProcessor getInstance() {
        return processor;
    }

}
