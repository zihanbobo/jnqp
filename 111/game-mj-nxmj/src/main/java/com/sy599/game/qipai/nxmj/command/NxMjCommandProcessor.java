package com.sy599.game.qipai.nxmj.command;

import com.sy599.game.character.Player;
import com.sy599.game.gcommand.BaseCommand;
import com.sy599.game.qipai.AbstractBaseCommandProcessor;
import com.sy599.game.qipai.nxmj.bean.NxMjPlayer;
import com.sy599.game.qipai.nxmj.command.com.NxMjComCommand;
import com.sy599.game.qipai.nxmj.command.play.NxMjPlayCommand;
import com.sy599.game.util.LogUtil;
import com.sy599.game.websocket.constant.WebSocketMsgType;
import com.sy599.game.websocket.netty.coder.MessageUnit;

import java.util.HashMap;
import java.util.Map;

public class NxMjCommandProcessor extends AbstractBaseCommandProcessor {
    private static NxMjCommandProcessor processor = new NxMjCommandProcessor();
    private static Map<Short, Class<? extends BaseCommand>> commandMap = new HashMap<>();
    private static Map<Class<?>, Short> msgClassToMsgTypeMap = new HashMap<Class<?>, Short>();

    static {
        commandMap.put(WebSocketMsgType.cs_com, NxMjComCommand.class);
        commandMap.put(WebSocketMsgType.cs_play, NxMjPlayCommand.class);

        try {
            for (Short type : commandMap.keySet()) {
                Class<? extends BaseCommand> cl = commandMap.get(type);
                BaseCommand action = cl.newInstance();
                Map<Class<?>, Short> msgTypeMap = action.getMsgTypeMap();
                if (msgTypeMap != null && !msgTypeMap.isEmpty()) {
                    for (Class<?> msgClass : msgTypeMap.keySet()) {
                        if (msgClassToMsgTypeMap.containsKey(msgClass)) {
                            throw new Exception("msgClassToMsgTypeMap err!!!!");

                        } else {
                            msgClassToMsgTypeMap.put(msgClass, msgTypeMap.get(msgClass));

                        }
                    }
                }
            }
        } catch (Exception e) {
            LogUtil.e("SocketAcitonProcessor err:", e);

        }

    }

    public static NxMjCommandProcessor getInstance() {
        return processor;
    }

    public short getMsgType(Class<?> clazz) {
        if (msgClassToMsgTypeMap.containsKey(clazz)) {
            return msgClassToMsgTypeMap.get(clazz);
        }
        return 0;
    }

    @Override
    public void process(Player player, MessageUnit message) {
        int code = 0;
        try {
            if (player == null || !(player instanceof NxMjPlayer)) {
                return;
            }
            NxMjPlayer csMajiangPlayer = player.getPlayer(processor);
            BaseCommand action = commandMap.get((short) message.getMsgType()).newInstance();
            action.setPlayer(csMajiangPlayer);
            action.execute(csMajiangPlayer, message);
        } catch (Exception e) {
            LogUtil.e("socket err: " + player.getUserId() + " " + message.getMsgType(), e);
            code = -1;
        } finally {
            if (code != 0) {
            }
        }
    }
}
