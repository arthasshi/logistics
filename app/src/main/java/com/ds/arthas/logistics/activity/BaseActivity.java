package com.ds.arthas.logistics.activity;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.inputmethod.InputMethodManager;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.ds.arthas.logistics.DsApplication;
import com.ds.arthas.logistics.utils.ResolutionUtil;

/**
 * Created by Administrator on 2016/7/14 0014.
 */
public class BaseActivity extends AppCompatActivity {
    public InputMethodManager imm;
    public  RequestQueue queue;
    public ResolutionUtil resUtil;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        imm= (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        queue= Volley.newRequestQueue(this);
        DsApplication.getInstance().setQueue(queue);
        resUtil=new ResolutionUtil(this);


    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
