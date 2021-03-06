/**
 * 
 */
package com.llxx.socket.loger;

import com.llxx.service.BuildConfig;

import android.util.Log;

/**
 * @author fanxin, eachen
 * @date   2015年5月5日
 */
public class Ll_Loger
{
    public static boolean LOGED = BuildConfig.DEBUG;
    public static String LOG_BUILD = "llxx_log:";

    /**
     * verbose 日志
     * @param tag
     * @param msg
     */
    public static final void v(String tag, String msg)
    {
        if (LOGED)
            Log.v(tag, LOG_BUILD + msg);
    }

    /**
     * 信息日志
     * @param tag
     * @param msg
     */
    public static final void i(String tag, String msg)
    {
        if (LOGED)
            Log.i(tag, LOG_BUILD + msg);
    }
    
    /**
     * 信息日志
     * @param tag
     * @param msg
     */
    public static final void w(String tag, String msg)
    {
        if (LOGED)
            Log.w(tag, LOG_BUILD + msg);
    }

    /**
     * 错误日志
     * @param tag
     * @param msg
     */
    public static final void e(String tag, String msg)
    {
        Log.e(tag, LOG_BUILD + msg);
    }

    /**
     * 错误日志
     * @param tag
     * @param msg
     */
    public static final void d(String tag, String msg)
    {
        if (LOGED)
            Log.d(tag, LOG_BUILD + msg);
    }

    /**
     * 输出E
     * @param tag
     * @param msg
     */
    public static final void e(Object tag, Exception msg)
    {
        if (LOGED)
        {
            //            Writer writer = new StringWriter();
            //            PrintWriter printWriter = new PrintWriter(writer);
            //            msg.printStackTrace(printWriter);
            //            Throwable cause = msg.getCause();
            //            while (cause != null)
            //            {
            //                cause.printStackTrace(printWriter);
            //                cause = cause.getCause();
            //            }
            //            printWriter.close();
            //            String result = writer.toString();
            //              Log.e(tag.getClass().getSimpleName(), LOG_BUILD + result);

            StackTraceElement[] es = msg.getStackTrace();
            for (int i = 0; i < es.length; i++)
            {
                Log.e(tag.getClass().getSimpleName(),
                        LOG_BUILD + es[i].getClassName() + "." + es[i].getMethodName() + ":" + es[i].getLineNumber());
            }
        }
    }
}
