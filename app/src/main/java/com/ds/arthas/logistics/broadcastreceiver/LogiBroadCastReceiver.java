package com.ds.arthas.logistics.broadcastreceiver;

import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import com.ds.arthas.logistics.DsApplication;
import com.ds.arthas.logistics.activity.MainActivity;
import com.ds.arthas.logistics.utils.Logger;
import com.xiaomi.mipush.sdk.ErrorCode;
import com.xiaomi.mipush.sdk.MiPushClient;
import com.xiaomi.mipush.sdk.MiPushCommandMessage;
import com.xiaomi.mipush.sdk.MiPushMessage;
import com.xiaomi.mipush.sdk.PushMessageReceiver;

import java.util.List;

/**
 * Created by Administrator on 2016/7/22 0022.
 * onReceivePassThroughMessage用来接收服务器发送的透传消息，
 * onNotificationMessageClicked用来接收服务器发来的通知栏消息（用户点击通知栏时触发），
 * onNotificationMessageArrived用来接收服务器发来的通知栏消息（消息到达客户端时触发，并且可以接收应用在前台时不弹出通知的通知消息），
 * onCommandResult用来接收客户端向服务器发送命令消息后返回的响应，
 * onReceiveRegisterResult用来接受客户端向服务器发送注册命令消息后返回的响应。
 */
public class LogiBroadCastReceiver extends PushMessageReceiver {
    @Override
    public void onReceivePassThroughMessage(Context context, MiPushMessage miPushMessage) {
        super.onReceivePassThroughMessage(context, miPushMessage);
        Log.d("mp","1");
    }

    @Override
    public void onNotificationMessageClicked(Context context, MiPushMessage miPushMessage) {
        super.onNotificationMessageClicked(context, miPushMessage);
        Log.d("mp","2");

        ActivityManager am= (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningTaskInfo> list=am.getRunningTasks(100);
        if(list!=null&&list.size()>0){
            ComponentName name=list.get(0).topActivity;
            Logger.d("==","activity name :"+name.getClassName());

            if("com.ds.arthas.logistics.activity.MainActivity".equals(name.getClassName())){
                Logger.d("==","true");
                DsApplication.getInstance().getLf().refresh();
            }else{
                for (ActivityManager.RunningTaskInfo info:list
                     ) {
                    if (info.topActivity.getPackageName().equals(context.getPackageName())){
                        am.moveTaskToFront(info.id,0);
                        return;
                    }
                }
                Logger.d("==","false");
                Intent intent=new Intent();
                intent.setClassName(context,"com.ds.arthas.logistics.activity.MainActivity");
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_SINGLE_TOP);
                context.startActivity(intent);
            }

        }


    }

    @Override
    public void onNotificationMessageArrived(Context context, MiPushMessage miPushMessage) {
        super.onNotificationMessageArrived(context, miPushMessage);
        Log.d("mp","3");
        ActivityManager am= (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningTaskInfo> list=am.getRunningTasks(100);
        if(list!=null&&list.size()>0){
            ComponentName name=list.get(0).topActivity;
            Logger.d("==","activity name :"+name.getClassName());

            if("com.ds.arthas.logistics.activity.MainActivity".equals(name.getClassName())){
                Logger.d("==","true");
                DsApplication.getInstance().getLf().refresh();
            }

        }

    }

    @Override
    public void onCommandResult(Context context, MiPushCommandMessage miPushCommandMessage) {
        super.onCommandResult(context, miPushCommandMessage);
        String commend=miPushCommandMessage.getCommand();
        List<String> args=miPushCommandMessage.getCommandArguments();
        if(MiPushClient.COMMAND_REGISTER.equals(commend)){
            if(miPushCommandMessage.getResultCode()== ErrorCode.SUCCESS){
                DsApplication.setRegId(args.get(0));
            }
        }
        Log.d("mp","4"+miPushCommandMessage.toString());
    }

    @Override
    public void onReceiveRegisterResult(Context context, MiPushCommandMessage miPushCommandMessage) {
        super.onReceiveRegisterResult(context, miPushCommandMessage);
        Log.d("mp","5");
    }
}
