package com.ds.arthas.logistics.activity;

import android.content.Intent;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.transition.Explode;
import android.transition.Transition;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.ds.arthas.logistics.Controller.ApiController;
import com.ds.arthas.logistics.DsApplication;
import com.ds.arthas.logistics.R;
import com.ds.arthas.logistics.entity.UserEntity;
import com.ds.arthas.logistics.utils.Logger;
import com.ds.arthas.logistics.utils.Md5Utils;
import com.ds.arthas.logistics.utils.SharedPreferencesUtils;
import com.sdsmdg.tastytoast.TastyToast;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Queue;

public class LoginActivity extends BaseActivity {

    private TextInputLayout til_username;
    private TextInputLayout til_password;
    private Button login;
    private RelativeLayout container;
    private StringRequest request;
    private Intent intent;
    private String url="http://192.168.3.60/Api.php/LogisticsLogin/logisticslogin";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        if(!"".equals(SharedPreferencesUtils.getString(this,"token",""))){
            DsApplication.setUid(SharedPreferencesUtils.getString(this,"uid",""));
            DsApplication.setToken(SharedPreferencesUtils.getString(this,"token",""));
            intent=new Intent(this,MainActivity.class);
            startActivity(intent);

//            TastyToast.makeText(getApplicationContext(),"登录成功...",TastyToast.LENGTH_LONG,TastyToast.SUCCESS);
            finish();
        }
        setContentView(R.layout.activity_login);
//        if(getSupportActionBar()!=null){
//            getSupportActionBar().hide();
//        }
        initView();
        initData();
        setListener();

    }


    private void setListener() {
        container.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                imm.hideSoftInputFromWindow(view.getWindowToken(),0);
                return false;
            }
        });

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                til_password.setError(null);
                til_username.setError(null);
                String username=til_username.getEditText().getText().toString();
                String password=til_password.getEditText().getText().toString();
                if(username==null||"".equals(username)){
                    til_username.setError("用户名不能为空");
                    return;
                }
                if(password==null||"".equals(password)){
                    til_password.setError("密码不能为空");
                    return;
                }
                TastyToast.makeText(getApplicationContext(),"登录中...",TastyToast.LENGTH_SHORT,TastyToast.DEFAULT);
                HashMap<String,String> map=new HashMap<>();
                map.put("username",username);
                String pwdMd5=Md5Utils.MD5Encode(password);
                map.put("password",pwdMd5);
                map.put("pushid", DsApplication.getRegId());
                Logger.d("login","username:"+username+"--pwd:"+pwdMd5+"--pushid:"+DsApplication.getRegId());
                controller.login(map);
//                intent=new Intent(LoginActivity.this,MainActivity.class);
//                startActivity(intent);
//                finish();

            }
        });
    }

    private ApiController controller;
    private void initData() {
        controller=new ApiController(this, new ApiController.NetWorkCallBack() {
            @Override
            public <T> void response(T entity) {
                UserEntity ue= (UserEntity) entity;
                SharedPreferencesUtils.saveString(LoginActivity.this,"token",ue.getToken());
                SharedPreferencesUtils.saveString(LoginActivity.this,"uid",ue.getUid());
                DsApplication.setUid(ue.getUid());
                DsApplication.setToken(ue.getToken());

                Explode explode = null;
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
                    explode = new Explode();
                    explode.setDuration(500);
                    explode.addListener(new Transition.TransitionListener() {
                        @Override
                        public void onTransitionStart(Transition transition) {

                        }

                        @Override
                        public void onTransitionEnd(Transition transition) {
                            finish();
                        }

                        @Override
                        public void onTransitionCancel(Transition transition) {

                        }

                        @Override
                        public void onTransitionPause(Transition transition) {

                        }

                        @Override
                        public void onTransitionResume(Transition transition) {

                        }
                    });

                    getWindow().setExitTransition(explode);
                    getWindow().setEnterTransition(explode);
                    ActivityOptionsCompat oc2 = ActivityOptionsCompat.makeSceneTransitionAnimation(LoginActivity.this);
                    Intent i2 = new Intent(LoginActivity.this,MainActivity.class);
                    startActivity(i2, oc2.toBundle());

                }else{
                                    intent=new Intent(LoginActivity.this,MainActivity.class);
                startActivity(intent);
                finish();
                }

                TastyToast.makeText(getApplicationContext(),"登录成功...",TastyToast.LENGTH_SHORT,TastyToast.SUCCESS);
            }

            @Override
            public <T> void response(ArrayList<T> list) {

            }


            @Override
            public void statusCode(String code) {

            }

            @Override
            public void msg(String msg) {
                Toast.makeText(LoginActivity.this,msg,Toast.LENGTH_LONG).show();
            }
        },false);
//        request= new StringRequest(Request.Method.POST,url, new Response.Listener<String>() {
//            @Override
//            public void onResponse(String s) {
//                Log.d("==","success:"+s);
//                Toast.makeText(LoginActivity.this, "success:"+s, Toast.LENGTH_SHORT).show();
//            }
//        }, new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError volleyError) {
//                Log.e("==","error");
//            }
//        }){
//            @Override
//            protected Map<String, String> getParams() throws AuthFailureError {
//                HashMap<String,String> map=new HashMap<>();
//                map.put("username","sdx123123");
//                String pwdMd5=Md5Utils.MD5Encode(Md5Utils.MD5Encode("123456"));
//                map.put("password","123456");
//                return map;
//
//            }
//        };
    }

    private void initView() {
        til_username= (TextInputLayout) findViewById(R.id.til_login_username);
        til_password= (TextInputLayout) findViewById(R.id.til_loginpassword);

        login= (Button) findViewById(R.id.btn_login_login);
        container= (RelativeLayout) findViewById(R.id.login_container);

        til_password.setHint("密码：");
        til_username.setHint("用户名：");
    }



}
