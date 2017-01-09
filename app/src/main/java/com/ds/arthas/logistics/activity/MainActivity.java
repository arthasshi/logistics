package com.ds.arthas.logistics.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.os.PersistableBundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutCompat;
import android.support.v7.widget.Toolbar;
import android.telephony.PhoneStateListener;
import android.telephony.SignalStrength;
import android.telephony.TelephonyManager;
import android.transition.Explode;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ds.arthas.logistics.Constant.ApiConstant;
import com.ds.arthas.logistics.Controller.ApiController;
import com.ds.arthas.logistics.DsApplication;
import com.ds.arthas.logistics.R;
import com.ds.arthas.logistics.broadcastreceiver.NetBroadCastReceiver;
import com.ds.arthas.logistics.entity.UserInfoEntity;
import com.ds.arthas.logistics.entity.VersionEntity;
import com.ds.arthas.logistics.event.LocalEvent;
import com.ds.arthas.logistics.fragment.AboutFragment;
import com.ds.arthas.logistics.fragment.BaseFragment;
import com.ds.arthas.logistics.fragment.HistoryFragment;
import com.ds.arthas.logistics.fragment.LogisticsFragment;
import com.ds.arthas.logistics.fragment.CancelFragment;
import com.ds.arthas.logistics.fragment.ShopListFragment;
import com.ds.arthas.logistics.LocationService;
import com.ds.arthas.logistics.utils.Logger;
import com.ds.arthas.logistics.utils.SharedPreferencesUtils;

import org.greenrobot.eventbus.EventBus;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;

public class MainActivity extends BaseActivity {

    private Toolbar toolBar;
    private ActionBarDrawerToggle abd;
    private DrawerLayout drawer;
    private RelativeLayout.LayoutParams rl;
    private NavigationView menu;
    private FrameLayout container;
    private BaseFragment curFragment;
    private AboutFragment aboutFragment;
    private LogisticsFragment logisticsFragment;
    private HistoryFragment historyFragment;
    private CancelFragment reportFragment;
    private ShopListFragment shopListFragment;
    private ApiController controller;
    private NetBroadCastReceiver nbcr;
    private MyListener ml;
    private TelephonyManager tm;
    private VersionEntity ve;
    private String path;
    ProgressDialog loading=null;
    private Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case 1:
                    loading.show();
                    break;
                case 2:
                    loading.dismiss();
                    break;
                case 3:
                    loading.setProgress(msg.arg1);
                    break;
            }
        }
    };



    //    private TextView netStat;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);
        Intent intent=new Intent(this, LocationService.class);
        startService(intent);
        Explode explode = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            explode = new Explode();
            explode.setDuration(500);
            getWindow().setExitTransition(explode);
            getWindow().setEnterTransition(explode);
        }
        IntentFilter filter=new IntentFilter("android.net.conn.CONNECTIVITY_CHANGE");
        nbcr=new NetBroadCastReceiver();
        registerReceiver(nbcr,filter);
        ml=new MyListener();
        tm= (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
        tm.listen(ml,PhoneStateListener.LISTEN_SIGNAL_STRENGTHS);
        DsApplication.getInstance().getActivitys().add(this);
        initView();
        showLoading();
        initData();
        setListener();
    }
    ProgressBar pb;
    private void showLoading() {
        loading = new ProgressDialog(this);
        loading.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        loading.setMessage("下载中，请稍候...");
        loading.setCancelable(false);
        loading.setCanceledOnTouchOutside(false);


    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(nbcr);
        tm.listen(ml,PhoneStateListener.LISTEN_NONE);
    }

    @Override
    protected void onResume() {
        super.onResume();
        tm.listen(ml,PhoneStateListener.LISTEN_SIGNAL_STRENGTHS);

    }

    @Override
    public void onPostCreate(Bundle savedInstanceState, PersistableBundle persistentState) {
        super.onPostCreate(savedInstanceState, persistentState);

    }

    private void setListener() {
        menu.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {
                toolBar.setTitle(item.getTitle());
                menu.setCheckedItem(item.getItemId());
                switch (item.getItemId()){
                    case R.id.nav_delivery:
                        curFragment=logisticsFragment;
                        break;
                    case R.id.nav_1:
                        curFragment=historyFragment;
                        break;
                    case R.id.nav_2:
                        curFragment=reportFragment;
                        break;
                    case R.id.nav_3:
                        curFragment=shopListFragment;
                        break;
                    case R.id.navi_about:
                        curFragment=aboutFragment;
                        menu.setCheckedItem(R.id.navi_about);
                        break;
                }
                if(curFragment!=null){
                    switchFragment();
                }
                drawer.closeDrawer(menu);
                return false;
            }
        });

        Toolbar.OnMenuItemClickListener tomc=new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()){
                    case R.id.menu_main_setting:

                    Intent intent=new Intent(MainActivity.this,SettingActivity.class);
                        startActivity(intent);
                        break;

                }
                return false;
            }
        };

        toolBar.setOnMenuItemClickListener(tomc);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu,menu);
        return true;
    }

    private TextView userName;
    private void initView(){

        toolBar= (Toolbar) findViewById(R.id.tb_main_title);
        toolBar.setTitleTextColor(getResources().getColor(R.color.write));
        toolBar.setTitle("物流配送");
        toolBar.setSubtitleTextColor(Color.YELLOW);

        setSupportActionBar(toolBar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        drawer= (DrawerLayout) findViewById(R.id.drawer_layout);
        abd=new ActionBarDrawerToggle(this,drawer,toolBar,R.string.app_name,R.string.app_name);
        drawer.addDrawerListener(abd);
        abd.syncState();

        container= (FrameLayout) findViewById(R.id.fl_main_container);
        menu= (NavigationView) findViewById(R.id.nv_main_drawermenu);
        userName= (TextView) menu.getHeaderView(0).findViewById(R.id.tv_nv_username);
    }
    private void initData(){


        path= Environment.getExternalStorageDirectory().getPath()+"/ds/";
        getNewVersion();

        DsApplication.getInstance().setAccept(SharedPreferencesUtils.getBoolean(this,"accept",false));
        logisticsFragment=new LogisticsFragment();
        aboutFragment =new AboutFragment();
        shopListFragment=new ShopListFragment();
        historyFragment=new HistoryFragment();
        reportFragment=new CancelFragment();
        FragmentTransaction ft=getSupportFragmentManager().beginTransaction();
        ft.add(R.id.fl_main_container,logisticsFragment);
        ft.commitAllowingStateLoss();
        menu.setCheckedItem(R.id.nav_delivery);

        curFragment=logisticsFragment;
        controller =new ApiController(this, new ApiController.NetWorkCallBack() {
            @Override
            public <T> void response(T entity) {
                UserInfoEntity uie= (UserInfoEntity) entity;
                userName.setText(uie.getNick()+" ("+uie.getPhone()+")");
                LocalEvent le=new LocalEvent();
                if("1".equals(uie.getWorking())){
                    DsApplication.getInstance().setAccept(true);
                    SharedPreferencesUtils.saveBoolean(MainActivity.this,"accept",true);

                    le.setWorking(true);
                }else{
                    DsApplication.getInstance().setAccept(false);
                    SharedPreferencesUtils.saveBoolean(MainActivity.this,"accept",false);
                    le.setWorking(false);

                }
                EventBus.getDefault().post(le);

            }

            @Override
            public <T> void response(ArrayList<T> list) {

            }

            @Override
            public void statusCode(String code) {

            }

            @Override
            public void msg(String msg) {
                Logger.d("==",msg);

            }
        },false);
        HashMap<String,String> map=new HashMap<>();
        map.put("uid", SharedPreferencesUtils.getString(this, "uid", ""));
        map.put("token", SharedPreferencesUtils.getString(this, "token", ""));
        controller.getUserInfo(map);
    }
    public void switchFragment(){
        FragmentTransaction ft=getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.fl_main_container,curFragment);
        ft.commitAllowingStateLoss();
    }

    public void getNewVersion() {
       ApiController ctr=new ApiController(this, new ApiController.NetWorkCallBack() {
           @Override
           public <T> void response(T entity) {
               if(entity!=null){
                   ve= (VersionEntity) entity;
                   showNewVAlert();
               }

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
        try {
            PackageInfo pi=getPackageManager().getPackageInfo("com.ds.arthas.logistics",0);
            int code =pi.versionCode;
            ctr.getNewVersion(code);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

    }

    private void showNewVAlert() {
        AlertDialog dialog = new AlertDialog.Builder(this).create();
        dialog.setTitle("提示：");
        dialog.setMessage("发现新版本，是否更新？");
        dialog.setCanceledOnTouchOutside(false);
        dialog.setButton(AlertDialog.BUTTON_NEGATIVE, "取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        dialog.setButton(AlertDialog.BUTTON_POSITIVE, "确定", new DialogInterface.OnClickListener() {
            @Override

            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                new Thread(new Runnable() {
                    @Override
                    public void run() {

                        downloadNewVersion();

                    }
                }).start();
            }
        });
        dialog.show();

    }

    public class MyListener extends PhoneStateListener {
        @Override
        public void onSignalStrengthsChanged(SignalStrength signalStrength) {
            super.onSignalStrengthsChanged(signalStrength);
            String[] parts=signalStrength.toString().split(" ");
            String type=DsApplication.getInstance().getNetWorkStat();
            Logger.d("logistics",String.valueOf(signalStrength.toString()));
            String showTips="";
            if("wifi".equals(type)){
                toolBar.setSubtitle("");
            }else if("4G".equals(type)){
                if(Integer.parseInt(parts[9])<-90){
                    toolBar.setSubtitle("网络状态不佳");
                }
            }else{
                toolBar.setSubtitle("网络状态不佳");
            }
        }


    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
    }
//    }

    private double everySize;
    public void downloadNewVersion(){
        handler.sendEmptyMessage(1);
        String apkName=System.currentTimeMillis()+".apk";
        try {
            URL url=new URL(ApiConstant.DOWNLOAD+ve.getFile());
            HttpURLConnection conn= (HttpURLConnection) url.openConnection();
            conn.setDoInput(true);
            conn.setConnectTimeout(1000000);
            conn.setReadTimeout(100000);
            int code = conn.getResponseCode();
            if(200==code){
                InputStream is=conn.getInputStream();
                double totalSize = conn.getContentLength() / 1024;
                loading.setMax((int) totalSize);
                Logger.e("==","length total" + totalSize);

//                byte[] apkData=readInputStream(is);
                File saveDir= new File(path);
                if(!saveDir.exists()){
                    saveDir.mkdir();
                }
                File apk=new File(saveDir+File.separator+apkName);
                FileOutputStream fos=new FileOutputStream(apk);
                byte[] buffer =new byte[1024*8];
                int len=0;
                while ((len=is.read(buffer))!=-1){
                    fos.write(buffer,0,len);
                    everySize+=len;
                    Message msg=Message.obtain();
                    msg.arg1= (int) ((everySize/1024));
                    msg.what=3;
                    handler.sendMessage(msg);
                    Logger.e("==",msg.arg1+"" );
                }
                fos.flush();
                openFile(apk);
                handler.sendEmptyMessage(2);
                everySize=0;
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public byte[] readInputStream (InputStream is) throws IOException {
        byte[] buffer =new byte[1024];
        int len=0;
        ByteArrayOutputStream baos=new ByteArrayOutputStream();
        while ((len=is.read(buffer))!=-1){
            baos.write(buffer,0,len);
        }
        baos.close();
        return  baos.toByteArray();
    }
    private void openFile(File file) {
        // TODO Auto-generated method stub
        Intent intent = new Intent();
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setAction(android.content.Intent.ACTION_VIEW);
        intent.setDataAndType(Uri.fromFile(file),
                "application/vnd.android.package-archive");
        startActivity(intent);
    }
}
