package com.ds.arthas.logistics.activity;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.Toast;

import com.ds.arthas.logistics.Controller.ApiController;
import com.ds.arthas.logistics.DsApplication;
import com.ds.arthas.logistics.R;
import com.ds.arthas.logistics.event.LocalEvent;
import com.ds.arthas.logistics.utils.Logger;
import com.ds.arthas.logistics.utils.SharedPreferencesUtils;
import com.sdsmdg.tastytoast.TastyToast;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Administrator on 2016/7/22 0022.
 */
public class SettingActivity extends  BaseActivity {
    private Button logout;
    private Switch accept;
    private ApiController controller;
    private boolean isWorking;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        DsApplication.getInstance().getActivitys().add(this);
        initView();
        initData();
        setListener();
    }

//    @Override
//    public void onCreate(Bundle savedInstanceState, PersistableBundle persistentState) {
//        super.onCreate(savedInstanceState, persistentState);
//        ;
//    }

    private void setListener() {
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferencesUtils.removeStr(SettingActivity.this,"uid");
                SharedPreferencesUtils.removeStr(SettingActivity.this,"token");
                Intent intent=new Intent(SettingActivity.this,LoginActivity.class);
                Toast.makeText(SettingActivity.this, ""+DsApplication.getInstance().getActivitys().size(), Toast.LENGTH_SHORT).show();
                for (Activity a:DsApplication.getInstance().getActivitys()
                     ) {
                    a.finish();
                }
                DsApplication.getInstance().getActivitys().clear();
                startActivity(intent);
            }
        });
//        accept.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
//                isWorking=b;
//                changeStatus(b?"1":"0");
//
//            }
//        });
        accept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isWorking=accept.isChecked();
                changeStatus(accept.isChecked()?"1":"0");
            }
        });
//        accept.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                if(accept.isChecked()){
//
//                }else{
//
//                }
//            }
//        });
    }

    private boolean isloading;
    public void changeStatus(String status){
        if(!isloading){
            HashMap<String ,String> map=new HashMap<>();
            map.put("uid", SharedPreferencesUtils.getString(this, "uid", ""));
            map.put("token", SharedPreferencesUtils.getString(this, "token", ""));
            map.put("working", status);
            TastyToast.makeText(SettingActivity.this,"状态更改中",TastyToast.LENGTH_SHORT,TastyToast.DEFAULT);
            controller.changeWork(map);
            isloading=true;
        }

    }
    private void initData() {
        controller=new ApiController(this, new ApiController.NetWorkCallBack() {
            @Override
            public <T> void response(T entity) {

            }

            @Override
            public <T> void response(ArrayList<T> list) {

            }

            @Override
            public void statusCode(String code) {
                isloading=false;
                if("0".equals(code)){
                    TastyToast.makeText(SettingActivity.this,"状态更改成功",TastyToast.LENGTH_SHORT,TastyToast.SUCCESS);
                    DsApplication.getInstance().setAccept(isWorking);
                    Logger.d("==","if this display means the working status is :"+isWorking);
                    SharedPreferencesUtils.saveBoolean(SettingActivity.this,"accept",isWorking);
                }else{
                    TastyToast.makeText(SettingActivity.this,"状态更改失败",TastyToast.LENGTH_SHORT,TastyToast.ERROR);
                    accept.setChecked(!isWorking);
                    SharedPreferencesUtils.saveBoolean(SettingActivity.this,"accept",accept.isChecked());
                }

            }

            @Override
            public void msg(String msg) {

            }
        },false);
    }

    private void initView() {
        logout= (Button) findViewById(R.id.btn_setting_logout);
        accept= (Switch) findViewById(R.id.switch_setting);
        accept.setSwitchPadding(300);
        accept.setChecked(SharedPreferencesUtils.getBoolean(this,"accept",false));
    }
}
