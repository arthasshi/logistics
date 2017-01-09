//package com.ds.arthas.logistics;
//
//import android.content.Context;
//import android.content.Intent;
//import android.graphics.Color;
//import android.view.WindowManager;
//import android.widget.TextView;
//
//import com.ds.arthas.logistics.activity.MainActivity;
//import com.ds.arthas.logistics.utils.Logger;
//
///**
// * Created by Administrator on 2016/8/19 0019.
// */
//public class CrashHandler implements Thread.UncaughtExceptionHandler {
//    public static CrashHandler instance;
//    private Context context;
//    //系统默认的UncaughtException处理类
//    private Thread.UncaughtExceptionHandler mDefaultHandler;
//    public static CrashHandler getInstance(){
//        if(instance==null){
//            instance=new CrashHandler();
//        }
//        return instance;
//    }
//    public void init(Context context) {
//       this. context = context;
//        //获取系统默认的UncaughtException处理器
//        mDefaultHandler = Thread.getDefaultUncaughtExceptionHandler();
//        //设置该CrashHandler为程序的默认处理器
//        Thread.setDefaultUncaughtExceptionHandler(this);
//    }
//    @Override
//    public void uncaughtException(Thread thread, Throwable throwable) {
//        if (!handleException(throwable) && mDefaultHandler != null) {
//            //如果用户没有处理则让系统默认的异常处理器来处理
//            mDefaultHandler.uncaughtException(thread, throwable);
//        } else {
//
//                Logger.d("==","11111111111");
//                Intent intent = new Intent();
//            intent.setClassName(context,"com.ds.arthas.logistics.activity.MainActivity");
//                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP |Intent.FLAG_ACTIVITY_NEW_TASK);
//                context.startActivity(intent);
//
//            //退出程序
////            android.os.Process.killProcess(android.os.Process.myPid());
////            System.exit(1);
//        }
//
//
//
//    }
//    private boolean handleException(Throwable ex) {
//        if (ex == null) {
//            return false;
//        }
////        WindowManager vm= (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
////        WindowManager.LayoutParams lp=new WindowManager.LayoutParams();
////        lp.height=200;
////        lp.width=400;
////        lp.format=1;
////        lp.flags= WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN;
////        lp.type= WindowManager.LayoutParams.TYPE_SYSTEM_OVERLAY;
////        TextView tv=new TextView(context);
////        tv.setText("11111111111");
////        tv.setBackgroundColor(Color.BLUE);
////        vm.addView(tv,lp);
////        Intent intent=new Intent();
////        intent.setClassName(context,"com.ds.arthas.logistics.LocationService");
////        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
////        context.startService(intent);
//        Intent intent = new Intent(context, MainActivity.class);
//        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP |
//                Intent.FLAG_ACTIVITY_NEW_TASK);
//        context.startActivity(intent);
//        return true;
//    }
//}
