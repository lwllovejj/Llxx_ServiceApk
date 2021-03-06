/**
 * 
 */
package com.llxx.socket.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import com.llxx.client.node.Ll_AccessibilityClient;
import com.llxx.nodefinder.QueryController;
import com.llxx.socket.action.AccessibilityActionManager;
import com.llxx.socket.loger.Ll_Loger;
import com.llxx.utils.BinderUtils;

import android.accessibilityservice.AccessibilityService;
import android.accessibilityservice.AccessibilityServiceInfo;
import android.annotation.TargetApi;
import android.os.Build;
import android.text.TextUtils;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;

/**
 * @author fanxin, eachen
 * @date   2016年8月19日
 * @qq 	461051353
 * @describe 类描述
 */
public class Ll_AccessibilityService extends AccessibilityService
{
    static final boolean DEBUG_OUTPUT = true;
    static final String TAG = "Ll_AccessibilityService";
    BinderUtils mBinderUtils;
    // 添加网易阅读，这里后面的是要做成自动化处理
    String[] PACKAGES = { "com.llxx.service", "com.android.systemui", "com.android.packageinstaller" };
    HashMap<String, Boolean> mPackages = new HashMap<>();
    Thread mBinderThread;
    Ll_AccessibilityClient mBinderClient;
    QueryController mController;

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
    @Override
    protected void onServiceConnected()
    {
        super.onServiceConnected();
        // 通知控制器
        if (mController == null)
        {
            mController = new QueryController(this);
        }
        mBinderClient = new Ll_AccessibilityClient(this);
        mBinderThread = new Thread(mBinderClient);
        mBinderThread.start();
        mBinderUtils = new BinderUtils(getApplicationContext());
        mBinderUtils.bind();

        setPackage(PACKAGES);
    }

    @Override
    protected boolean onGesture(int gestureId)
    {
        Ll_Loger.i(TAG, "onGesture -> " + gestureId);
        return super.onGesture(gestureId);
    }

    @Override
    public void onAccessibilityEvent(AccessibilityEvent event)
    {
        try
        {
            mController.onAccessibilityEvent(event);

            // 处理自己的消息
            AccessibilityNodeInfo nodeInfo = event.getSource();
            String result = AccessibilityActionManager.processEvent(getApplicationContext(), event, nodeInfo);
            if (!TextUtils.isEmpty(result))
            {
                sendMessage(result);
                return;
            }
            // TODO Auto-generated method stub  
            int eventType = event.getEventType();
            String eventText = "nnnnnnnnnnnnnnnnnnnnnn";
            if (DEBUG_OUTPUT)
            {
                Ll_Loger.i(TAG, "==============Start====================");
            }

            // Ll_Loger.i(TAG, event.getBeforeText().toString());
            if (event.getClassName() != null)
                Ll_Loger.i(TAG, "class name: " + event.getClassName().toString());

            // Ll_Loger.i(TAG, event.getContentDescription().toString());
            // Ll_Loger.i(TAG, event.getText().toString());
            if (event.getPackageName() != null)
                Ll_Loger.i(TAG, "package name: " + event.getPackageName().toString());
            switch (eventType)
            {
            case AccessibilityEvent.TYPE_VIEW_CLICKED:
                eventText = "TYPE_VIEW_CLICKED";
                break;
            case AccessibilityEvent.TYPE_VIEW_FOCUSED:
                eventText = "TYPE_VIEW_FOCUSED";
                break;
            case AccessibilityEvent.TYPE_VIEW_LONG_CLICKED:
                eventText = "TYPE_VIEW_LONG_CLICKED";
                break;
            case AccessibilityEvent.TYPE_VIEW_SELECTED:
                eventText = "TYPE_VIEW_SELECTED";
                break;
            case AccessibilityEvent.TYPE_VIEW_TEXT_CHANGED:
                eventText = "TYPE_VIEW_TEXT_CHANGED";
                break;
            case AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED:
                eventText = "TYPE_WINDOW_STATE_CHANGED";
                break;
            case AccessibilityEvent.TYPE_NOTIFICATION_STATE_CHANGED:
                eventText = "TYPE_NOTIFICATION_STATE_CHANGED";

                break;
            case AccessibilityEvent.TYPE_TOUCH_EXPLORATION_GESTURE_END:
                eventText = "TYPE_TOUCH_EXPLORATION_GESTURE_END";
                break;
            case AccessibilityEvent.TYPE_ANNOUNCEMENT:
                eventText = "TYPE_ANNOUNCEMENT";
                break;
            case AccessibilityEvent.TYPE_TOUCH_EXPLORATION_GESTURE_START:
                eventText = "TYPE_TOUCH_EXPLORATION_GESTURE_START";
                break;
            case AccessibilityEvent.TYPE_VIEW_HOVER_ENTER:
                eventText = "TYPE_VIEW_HOVER_ENTER";
                break;
            case AccessibilityEvent.TYPE_VIEW_HOVER_EXIT:
                eventText = "TYPE_VIEW_HOVER_EXIT";
                break;
            case AccessibilityEvent.TYPE_VIEW_SCROLLED:
                eventText = "TYPE_VIEW_SCROLLED";
                break;
            case AccessibilityEvent.TYPE_VIEW_TEXT_SELECTION_CHANGED:
                eventText = "TYPE_VIEW_TEXT_SELECTION_CHANGED";
                break;
            case AccessibilityEvent.TYPE_WINDOW_CONTENT_CHANGED:
                eventText = "TYPE_WINDOW_CONTENT_CHANGED";
                break;
            }
            eventText = eventText + ":" + eventType;
            if (DEBUG_OUTPUT)

            {
                Ll_Loger.i(TAG, eventText);
                Ll_Loger.i(TAG, "=============END=====================");
            }
        }
        catch (Throwable e)
        {
            e.printStackTrace();
        }
    }

    @Override
    public void onInterrupt()
    {

    }

    /**
     * 发送消息
     * @param message
     */
    public void sendMessage(String message)
    {
        if (mBinderUtils.getService() != null)
        {
            try
            {
                mBinderUtils.getService().sendMessage(message);
            }
            catch (Throwable e)
            {
                e.printStackTrace();
            }
        }
    }

    /**
     * 发送消息
     * @param message
     */
    public void sendMessageByHash(String message, int hash)
    {
        if (mBinderUtils.getService() != null)
        {
            try
            {
                mBinderUtils.getService().sendMessageByHash(message, hash);
            }
            catch (Throwable e)
            {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onDestroy()
    {
        super.onDestroy();
        mBinderClient.stop();
    }

    /**
     * 获取查询器
     * @return
     */
    public QueryController getQueryController()
    {
        return mController;
    }

    /**
     * 设置监听的包
     * @param PACKAGES
     */
    public void setPackage(String[] PACKAGES)
    {
        ArrayList<String> mSetPackage = new ArrayList<>();
        for (int i = 0; i < PACKAGES.length; i++)
        {
            Ll_Loger.e(TAG, "is containsKey : " + PACKAGES[i] + " = " + mPackages.containsKey(PACKAGES[i]));
            if (mPackages.containsKey(PACKAGES[i]))
                continue;

            mPackages.put(PACKAGES[i], true);
            mSetPackage.add(PACKAGES[i]);
        }
        if (mSetPackage.size() != 0)
        {
            String sets[] = new String[mSetPackage.size()];
            mSetPackage.toArray(sets);
            Ll_Loger.e(TAG, "set packages is " + Arrays.toString(sets));
            String result[] = new String[mPackages.size()];
            this.PACKAGES = mPackages.keySet().toArray(result);
            Ll_Loger.e(TAG, "all packages is " + Arrays.toString(this.PACKAGES));
            AccessibilityServiceInfo accessibilityServiceInfo = new AccessibilityServiceInfo();
            accessibilityServiceInfo.packageNames = this.PACKAGES;
            accessibilityServiceInfo.eventTypes = AccessibilityEvent.TYPES_ALL_MASK;
            accessibilityServiceInfo.feedbackType = AccessibilityServiceInfo.FEEDBACK_SPOKEN;
            accessibilityServiceInfo.notificationTimeout = 100;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2)
            {
                accessibilityServiceInfo.flags = AccessibilityServiceInfo.FLAG_REPORT_VIEW_IDS;
            }
            setServiceInfo(accessibilityServiceInfo);
        }
        else
        {
            Ll_Loger.e(TAG, "skip set packages curr is : " + Arrays.toString(this.PACKAGES));
        }
    }
}
