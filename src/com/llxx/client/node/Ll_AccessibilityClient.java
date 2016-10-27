package com.llxx.client.node;

import java.util.concurrent.ArrayBlockingQueue;

import org.json.JSONObject;

import com.llxx.client.command.CommandManager;
import com.llxx.client.command.CommandRun;
import com.llxx.socket.config.Configs;
import com.llxx.socket.loger.Ll_Loger;
import com.llxx.socket.service.Ll_AccessibilityService;
import com.llxx.socket.wrap.bean.Ll_Message;

public class Ll_AccessibilityClient implements Runnable
{

    public static final String TAG = "Ll_AccessibilityClient";
    private static ArrayBlockingQueue<Ll_Message> mBlockingQueue = new ArrayBlockingQueue<>(
            100);

    private boolean isKeepListener = true;
    Ll_AccessibilityService mAccessibilityService;

    public Ll_AccessibilityClient(Ll_AccessibilityService accessibilityService)
    {
        mAccessibilityService = accessibilityService;
    }

    public void run()
    {
        try
        {
            while (isKeepListener)
            {
                Ll_Message message = mBlockingQueue.take();
                if (message != null)
                {
                    JSONObject object = new JSONObject(message.getMessage());
                    String action = object.optString("action", "");
                    Class<? extends CommandRun> command = CommandManager.mProtocols
                            .get(action);

                    if (Configs.DEBUG_ACCESSIBLITY_CLIENT_RECEIVE)
                        Ll_Loger.d(TAG, "parseMessage -> action : " + action
                                + ", protocol ->" + command);
                    if (command != null)
                    {
                        try
                        {
                            // 1. 设置消息 2.解析消息 3.运行命令 4.通过服务器直接发送到客户端
                            CommandRun mProtocol = command.newInstance();
                            mProtocol.setMessage(message);
                            mProtocol.prase();
                            mProtocol.setRunOk(mProtocol
                                    .runCommand(mAccessibilityService));
                            // send(mProtocol.getResult(mAccessibilityService));
                            mAccessibilityService.sendMessageByHash(
                                    mProtocol.getResult(mAccessibilityService),
                                    mProtocol.getClientHash());
                        }
                        catch (InstantiationException e)
                        {
                            e.printStackTrace();
                        }
                        catch (IllegalAccessException e)
                        {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }
        catch (Throwable ex)
        {
            ex.printStackTrace();
        }
    }

    public void stop()
    {
        isKeepListener = false;
    }

    /**
     * 添加信息
     * @param message
     * @return
     */
    public static final boolean addMessage(Ll_Message message)
    {
        return mBlockingQueue.add(message);
    }

}
