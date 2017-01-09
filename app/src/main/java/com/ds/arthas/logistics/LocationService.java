package com.ds.arthas.logistics;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.CompoundButton;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.maps2d.AMap;
import com.amap.api.maps2d.LocationSource;
import com.amap.api.maps2d.model.LatLng;
import com.ds.arthas.logistics.Controller.ApiController;
import com.ds.arthas.logistics.event.LocalEvent;
import com.ds.arthas.logistics.utils.Logger;
import com.ds.arthas.logistics.utils.SharedPreferencesUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Administrator on 2016/8/1 0001.
 */
public class LocationService extends Service  implements LocationSource, AMapLocationListener,CompoundButton.OnCheckedChangeListener {
    private AMap aMap;
    private AMapLocationClientOption locaOpt;
    private AMapLocationClient locaC;
    private OnLocationChangedListener mListener;
    private LatLng ll;
    private ApiController controller;
    public static boolean stop;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        locaC=new AMapLocationClient(this.getApplicationContext());
        locaOpt=new AMapLocationClientOption();

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
//        Logger.d("==","localtion service is start ");

        if(locaC==null){
            locaC=new AMapLocationClient(this.getApplicationContext());
        }
        if(locaOpt==null){
            locaOpt=new AMapLocationClientOption();
        }
        locaOpt.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
        locaOpt.setInterval(30000);
        locaC.setLocationOption(locaOpt);
        setListener();
        return START_STICKY;
    }

    @Override
    public void onStart(Intent intent, int startId) {
        super.onStart(intent, startId);
    }

    @Override
    public void onTaskRemoved(Intent rootIntent) {
        Logger.d("==","service  task is  removed ");

//        Calendar cal=Calendar.getInstance();
//        Intent intent=new Intent(this, LocationService.class);
//        PendingIntent pi=PendingIntent.getService(this,0,intent,0);
//        AlarmManager am= (AlarmManager) getSystemService(Context.ALARM_SERVICE);
//        am.setRepeating(AlarmManager.RTC_WAKEUP,cal.getTimeInMillis()+1000,60000,pi);
//
//Intent intent=new Intent();
//        intent.setClassName(this,"com.ds.arthas.logistics.LocationService");
//        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//        startService(intent);

//        AlertDialog.Builder builder=new AlertDialog.Builder(this, R.style.AlertDialogCustom);
//        builder.setTitle("tips");
//        builder.setMessage("检测到服务正在被关闭 是否重启");
//        builder.setNegativeButton("sure", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialogInterface, int i) {
//
//            }
//        });
//        builder.setPositiveButton("cancel", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialogInterface, int i) {
//
//            }
//        });
//        AppCompatDialog dialog=builder.create();
//        dialog.getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_OVERLAY);
//        dialog.show();
//        WindowManager vm= (WindowManager) this.getSystemService(Context.WINDOW_SERVICE);
//        WindowManager.LayoutParams lp=new WindowManager.LayoutParams();
//        lp.height=200;
//        lp.width=400;
//        lp.format=1;
//        lp.flags= WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN;
//        lp.type= WindowManager.LayoutParams.TYPE_SYSTEM_OVERLAY;
//        TextView tv=new TextView(getApplicationContext());
//        tv.setText("11111111111");
//        tv.setBackgroundColor(Color.BLUE);
//        vm.addView(tv,lp);


//        super.onTaskRemoved(rootIntent);

    }

    private void setListener() {
        locaC.setLocationListener(this);
        locaC.startLocation();
    }
    public void uploadGps(){
        if(controller==null){
            controller=new ApiController(this, new ApiController.NetWorkCallBack() {
                @Override
                public <T> void response(T entity) {

                }

                @Override
                public <T> void response(ArrayList<T> list) {

                }

                @Override
                public void statusCode(String code) {

                }

                @Override
                public void msg(String msg) {

                }
            },false);
        }
        HashMap<String,String> map=new HashMap<>();
        map.put("uid", SharedPreferencesUtils.getString(this,"uid",""));
        map.put("token",SharedPreferencesUtils.getString(this,"token",""));
        map.put("gpsx",ll.latitude+"");
        map.put("gpsy",ll.longitude+"");
        controller.uploadGps(map);

    }

    @Override
    public void onLocationChanged(AMapLocation aMapLocation) {

        if(locaC!=null){

            if(aMapLocation!=null&&aMapLocation.getErrorCode()==0){
                ll=new LatLng(aMapLocation.getLatitude(),aMapLocation.getLongitude());
                    uploadGps();
            }else{
                String err= "定位失败," + aMapLocation.getErrorCode()+ ": " + aMapLocation.getErrorInfo();
                Log.e("11loca",err);
            }

        }else{
            Logger.d("11loca","gps succes2s");
        }
    }

    @Override
    public void activate(OnLocationChangedListener onLocationChangedListener) {
//        mListener=onLocationChangedListener;
//        if(locaC==null){
//            locaC = new AMapLocationClient(this);
//            locaOpt = new AMapLocationClientOption();
//            //设置定位监听
//            locaC.setLocationListener(this);
//            //设置为高精度定位模式
//            locaOpt.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
//            //设置定位参数
//            locaC.setLocationOption(locaOpt);
//            // 此方法为每隔固定时间会发起一次定位请求，为了减少电量消耗或网络流量消耗，
//            // 注意设置合适的定位时间的间隔（最小间隔支持为2000ms），并且在合适时间调用stopLocation()方法来取消定位请求
//            // 在定位结束后，在合适的生命周期调用onDestroy()方法
//            // 在单次定位情况下，定位无论成功与否，都无需调用stopLocation()方法移除请求，定位sdk内部会移除
//            locaC.startLocation();
//        }
    }

    @Override
    public void deactivate() {
        mListener = null;
        if (locaC != null) {
            locaC.stopLocation();
            locaC.onDestroy();
        }
        locaC = null;
    }

    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Logger.d("==","if this display means the localtion service is stop");
    }
}
