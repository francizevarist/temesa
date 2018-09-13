package com.pos.utils;

import android.app.Application;
import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.Volley;
import com.pos.app.AppController;
import com.pos.volley.LruBitmapCache;
import com.tencent.bugly.Bugly;
import com.tencent.bugly.crashreport.CrashReport;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import rego.printlib.export.regoPrinter;
import com.pos.utils.preDefiniation.TransferMode;

public class ApplicationContext extends Application {
    private regoPrinter printer;
    private int myState = 0;
    private String printName = "RG-MTP58B";
    private int alignTypetext;

    private TransferMode printmode = TransferMode.TM_NONE;
    private boolean labelmark = true;


    //network context
    public static final String TAG = AppController.class.getSimpleName();

    private RequestQueue mRequestQueue;
    private ImageLoader mImageLoader;
    LruBitmapCache mLruBitmapCache;

    private static ApplicationContext mInstance;


    @Override
    public void onCreate() {
        super.onCreate();
        Log.d("APP", "onCreate");
        Context context = getApplicationContext();
        // 获取当前包名
        String packageName = context.getPackageName();
        // 获取当前进程名
        String processName = getProcessName(android.os.Process.myPid());
        // 设置是否为上报进程
        CrashReport.UserStrategy strategy = new CrashReport.UserStrategy(context);
        strategy.setUploadProcess(processName == null || processName.equals(packageName));
        // 初始化Bugly
        Bugly.init(getApplicationContext(), "78f51c2a65", true, strategy);

        //network
        mInstance = this;
    }


    public regoPrinter getObject() {
        return printer;
    }

    public int setAlignType(int n) {
        alignTypetext = n;
        return alignTypetext;

    }

    public int getAlignType() {
        return alignTypetext;
    }

    public void setObject() {
        printer = new regoPrinter(this);
    }

    public String getName() {
        return printName;
    }

    public void setName(String name) {
        printName = name;
    }

    public void setState(int state) {
        myState = state;
    }

    public int getState() {
        return myState;
    }

    public void setPrintway(int printway) {

        switch (printway) {
            case 0:
                printmode = TransferMode.TM_NONE;
                break;
            case 1:
                printmode = TransferMode.TM_DT_V1;
                break;
            default:
                printmode = TransferMode.TM_DT_V2;
                break;
        }

    }

    public int getPrintway() {
        return printmode.getValue();
    }

    public boolean getlabel() {
        return labelmark;
    }

    public void setlabel(boolean labelprint) {
        labelmark = labelprint;
    }

    /**
     * 将打印n行空行。n的值应该在0-255之间
     */

    public void PrintNLine(int lines) {
        // TODO Auto-generated method stub
        printer.ASCII_PrintBuffer(myState, new byte[]{0x1B, 0x66, 1,
                (byte) lines}, 4);

    }

    public void printSettings() {
        // 1D 28 41 00 00 00 02
        byte[] printdata = {0x1d, 0x28, 0x41, 0x00, 0x00, 0x00, 0x02};
        printer.ASCII_PrintBuffer(myState, printdata, printdata.length);
    }


    /**
     * 获取进程号对应的进程名
     *
     * @param pid 进程号
     * @return 进程名
     */
    private static String getProcessName(int pid) {
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new FileReader("/proc/" + pid + "/cmdline"));
            String processName = reader.readLine();
            if (!TextUtils.isEmpty(processName)) {
                processName = processName.trim();
            }
            return processName;
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        } finally {
            try {
                if (reader != null) {
                    reader.close();
                }
            } catch (IOException exception) {
                exception.printStackTrace();
            }
        }
        return null;
    }



    //Network context
    public static synchronized ApplicationContext getInstance() {
        return mInstance;
    }

    public RequestQueue getRequestQueue() {
        if (mRequestQueue == null) {
            mRequestQueue = Volley.newRequestQueue(getApplicationContext());
        }

        return mRequestQueue;
    }

    public ImageLoader getImageLoader() {
        getRequestQueue();
        if (mImageLoader == null) {
            getLruBitmapCache();
            mImageLoader = new ImageLoader(this.mRequestQueue, mLruBitmapCache);
        }

        return this.mImageLoader;
    }

    public LruBitmapCache getLruBitmapCache() {
        if (mLruBitmapCache == null)
            mLruBitmapCache = new LruBitmapCache();
        return this.mLruBitmapCache;
    }

    public <T> void addToRequestQueue(Request<T> req, String tag) {
        req.setTag(TextUtils.isEmpty(tag) ? TAG : tag);
        getRequestQueue().add(req);
    }

    public <T> void addToRequestQueue(Request<T> req) {
        req.setTag(TAG);
        getRequestQueue().add(req);
    }

    public void cancelPendingRequests(Object tag) {
        if (mRequestQueue != null) {
            mRequestQueue.cancelAll(tag);
        }
    }

}
