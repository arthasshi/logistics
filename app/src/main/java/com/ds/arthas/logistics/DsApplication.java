package com.ds.arthas.logistics;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.content.Context;
import android.os.Process;
import android.util.Log;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.ds.arthas.logistics.fragment.LogisticsFragment;
import com.marswin89.marsdaemon.DaemonApplication;
import com.marswin89.marsdaemon.DaemonConfigurations;
import com.xiaomi.channel.commonutils.logger.LoggerInterface;
import com.xiaomi.mipush.sdk.Logger;
import com.xiaomi.mipush.sdk.MiPushClient;


import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/7/19 0019.
 */
public class DsApplication extends DaemonApplication {

    public static RequestQueue queue;
    public static final String APP_ID = "2882303761517492919";
    public static final String APP_KEY = "5371749222919";
    public static final String TAG = "com.ds.arthas.logistics";
    public static String regId="";
    private static String uid;
    private static String token;
    private LogisticsFragment lf;
    private boolean isAccept;

    private String netWorkStat;

    private ArrayList<Activity> activitys=new ArrayList<>();
    @Override
    public void onCreate() {
        super.onCreate();
//        isAccept = SharedPreferencesUtils.getBoolean(this,"accept",false);
        dsApplication=this;
        queue= Volley.newRequestQueue(this);

        //初始化push推送服务
        if(shouldInit()) {
            MiPushClient.registerPush(this, APP_ID, APP_KEY);

        }
        MiPushClient.setAlias(this,"arthas123",null);
        MiPushClient.checkManifest(this);
        //打开Log
        LoggerInterface newLogger = new LoggerInterface() {

            @Override
            public void setTag(String tag) {
                // ignore
            }

            @Override
            public void log(String content, Throwable t) {
                Log.d(TAG, content, t);
            }

            @Override
            public void log(String content) {
                Log.d(TAG, content);
            }
        };
        Logger.setLogger(this, newLogger);
    }

    private static  DsApplication dsApplication;
    public static DsApplication getInstance(){
        if ( dsApplication== null) {
            synchronized (DsApplication.class) {
                if (dsApplication == null) {
                    dsApplication = new DsApplication();
                }
            }
        }
        return dsApplication;
    }
    private boolean shouldInit() {
        ActivityManager am = ((ActivityManager) getSystemService(Context.ACTIVITY_SERVICE));
        List<RunningAppProcessInfo> processInfos = am.getRunningAppProcesses();
        String mainProcessName = getPackageName();
        int myPid = Process.myPid();
        for (RunningAppProcessInfo info : processInfos) {
            if (info.pid == myPid && mainProcessName.equals(info.processName)) {
                return true;
            }
        }
        return false;
    }

    public static String getRegId() {
        return regId;
    }

    public static void setRegId(String regId) {
        DsApplication.regId = regId;
    }

    public RequestQueue getQueue() {
        return queue;
    }

    public void setQueue(RequestQueue queue) {
        this.queue = queue;
    }

    public static String getUid() {
        return uid;
    }

    public static void setUid(String uid) {
        DsApplication.uid = uid;
    }

    public static String getToken() {
        return token;
    }

    public static void setToken(String token) {
        DsApplication.token = token;
    }

    public LogisticsFragment getLf() {
        return lf;
    }

    public void setLf(LogisticsFragment lf) {
        this.lf = lf;
    }

    public ArrayList<Activity> getActivitys() {
        return activitys;
    }

    public void setActivitys(ArrayList<Activity> activitys) {
        this.activitys = activitys;
    }

    public boolean isAccept() {
        return isAccept;
    }

    public void setAccept(boolean accept) {
        isAccept = accept;
    }

    public String getNetWorkStat() {
        return netWorkStat;
    }

    public void setNetWorkStat(String netWorkStat) {
        this.netWorkStat = netWorkStat;
    }

    @Override
    protected DaemonConfigurations getDaemonConfigurations() {
        DaemonConfigurations.DaemonConfiguration configuration1 = new DaemonConfigurations.DaemonConfiguration(
                "com.ds.arthas.logistics:localtion",
                LocationService.class.getCanonicalName(),
                LoactionReceiver.class.getCanonicalName());

        DaemonConfigurations.DaemonConfiguration configuration2 = new DaemonConfigurations.DaemonConfiguration(
                "com.ds.arthas.logistics:localtion2",
                Location2Service.class.getCanonicalName(),
                Location2Receiver.class.getCanonicalName());

        DaemonConfigurations.DaemonListener listener = new MyDaemonListener();
        //return new DaemonConfigurations(configuration1, configuration2);//listener can be null
        return new DaemonConfigurations(configuration1, configuration2, listener);
    }
    class MyDaemonListener implements DaemonConfigurations.DaemonListener{
        @Override
        public void onPersistentStart(Context context) {
        }

        @Override
        public void onDaemonAssistantStart(Context context) {
        }

        @Override
        public void onWatchDaemonDaed() {
        }
    }
}
