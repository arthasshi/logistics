package com.ds.arthas.logistics.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.ds.arthas.logistics.Controller.ApiController;
import com.ds.arthas.logistics.R;
import com.ds.arthas.logistics.adapter.LogiAdapter;
import com.ds.arthas.logistics.entity.LogiEntity;
import com.ds.arthas.logistics.utils.SharedPreferencesUtils;
import com.sdsmdg.tastytoast.TastyToast;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Administrator on 2016/7/18 0018.
 */
public class CancelFragment extends BaseFragment {

    private View rootView;
    private RecyclerView list;
    private LogiAdapter adapter;
    private ArrayList<LogiEntity> datas=new ArrayList<>();
    private ApiController controller;
    private LinearLayoutManager lm;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView=LayoutInflater.from(ctx).inflate(R.layout.fragment_report,null);
        removeParent(rootView);
        initView();
        initData();
        setListener();
        return rootView;
    }
    public void refresh() {
            datas.clear();
        TastyToast.makeText(ctx,"数据加载中",Toast.LENGTH_LONG,TastyToast.DEFAULT);
            HashMap<String, String> map = new HashMap<>();
            map.put("uid", SharedPreferencesUtils.getString(ctx, "uid", ""));
            map.put("token", SharedPreferencesUtils.getString(ctx, "token", ""));
            map.put("status", "-1");
            controller.getLogiList(map);


    }

    private void setListener() {
    }

    private void initData() {
        controller = new ApiController(ctx, new ApiController.NetWorkCallBack() {
            @Override
            public <T> void response(T entity) {

            }

            @Override
            public <T> void response(ArrayList<T> list) {

                datas.addAll((ArrayList<LogiEntity>) list);
                adapter.notifyDataSetChanged();
            }


            @Override
            public void statusCode(String code) {

            }

            @Override
            public void msg(String msg) {
                Toast.makeText(ctx, msg, Toast.LENGTH_SHORT).show();
            }
        }, false);
        refresh();
    }

    private void initView() {
        list= (RecyclerView) rootView.findViewById(R.id.rv_cancel_list);
        lm=new LinearLayoutManager(ctx);
        list.setItemAnimator(new DefaultItemAnimator());
        list.setLayoutManager(lm);
        adapter=new LogiAdapter(ctx,datas);
        list.setAdapter(adapter);
    }
}
