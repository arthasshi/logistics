package com.ds.arthas.logistics.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.util.DiffUtil;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.Spanned;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.ds.arthas.logistics.Controller.ApiController;
import com.ds.arthas.logistics.DsApplication;
import com.ds.arthas.logistics.R;
import com.ds.arthas.logistics.adapter.LogiAdapter;
import com.ds.arthas.logistics.entity.LogiEntity;
import com.ds.arthas.logistics.utils.DiffCallBack;
import com.ds.arthas.logistics.utils.SharedPreferencesUtils;
import com.ds.arthas.logistics.widget.WrapContentLinearLayoutManager;
import com.sdsmdg.tastytoast.TastyToast;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;

import javax.security.auth.login.LoginException;


/**
 * Created by Administrator on 2016/7/15 0015.
 */
public class LogisticsFragment extends BaseFragment {
    private View rootView;
    private SwipeRefreshLayout refreshLayout;
    private RecyclerView list;
    private ArrayList<LogiEntity> datas = new ArrayList<>();
    private ArrayList<LogiEntity> oldList=new ArrayList<>();
    private ArrayList<LogiEntity> newList;
    private LogiAdapter adapter;

    private LinearLayout.LayoutParams lp;
    private ApiController controller;

    private int lastVisiableItem;
    private boolean isLoading;
    private ProgressBar loading;

    private TextView count;
//    private Toast toast;
private  Handler handler = new Handler() {
    @Override
    public void handleMessage(Message msg) {
        super.handleMessage(msg);
        switch (msg.what){
            case 1:
                DiffUtil.DiffResult result= (DiffUtil.DiffResult) msg.obj;
                result.dispatchUpdatesTo(adapter);
                oldList=newList;
                adapter.setDatas(datas);
                break;
        }
    }
};

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_logistics, null);
        DsApplication.getInstance().setLf(this);
        removeParent(rootView);
        initView();
        initData();
        setLinstener();
        return rootView;
    }

    public void refresh() {
        if (!isLoading) {
            datas.clear();
            isLoading = true;
//            toast.setText("数据更新中");
//            toast.show();
            TastyToast.makeText(ctx,"数据加载中",Toast.LENGTH_LONG,TastyToast.DEFAULT);
//            refreshLayout.setRefreshing(true);
            HashMap<String, String> map = new HashMap<>();
            map.put("uid", SharedPreferencesUtils.getString(ctx, "uid", ""));
            map.put("token", SharedPreferencesUtils.getString(ctx, "token", ""));
            map.put("status", "0");
            controller.getLogiList(map);
            stat=2;
        }


    }

    private int stat=-1;
//    public void loadMore() {
//        if (!isLoading) {
//            loading.setVisibility(View.VISIBLE);
//            isLoading = true;
//            HashMap<String, String> map = new HashMap<>();
//            map.put("uid", SharedPreferencesUtils.getString(ctx, "uid", ""));
//            map.put("token", SharedPreferencesUtils.getString(ctx, "token", ""));
//            map.put("lastid", datas.get(lastVisiableItem).getId());
//            map.put("status", "0");
//            controller.getLogiList(map);
//            stat=1;
//        }
//
//    }

    private void setLinstener() {
//        list.addOnScrollListener(new RecyclerView.OnScrollListener() {
//            @Override
//            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
//                super.onScrollStateChanged(recyclerView, newState);
//                if (newState == RecyclerView.SCROLL_INDICATOR_BOTTOM  && lastVisiableItem + 1 == adapter.getItemCount()) {
//                    loadMore();
//                }
//            }
//
//            @Override
//            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
//                super.onScrolled(recyclerView, dx, dy);
//                lastVisiableItem = lm.findLastVisibleItemPosition();
//            }
//
//        });
        list.setOnTouchListener(
                new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View v, MotionEvent event) {
                        if (isLoading) {
                            return true;
                        } else {
                            return false;
                        }
                    }
                }
        );
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refresh();
            }
        });
    }

    private void initData() {

        controller = new ApiController(ctx, new ApiController.NetWorkCallBack() {
            @Override
            public <T> void response(T entity) {

            }

            @Override
            public <T> void response(ArrayList<T> list) {

                newList= (ArrayList<LogiEntity>) list;
                Spanned html= Html.fromHtml("当前待派送订单：<font color= '#FF0000'><big>"+newList.size()+"</big></font>  条");
//                count.setText("当前待派送订单："+newList.size()+"条");
                count.setText(html);
                Collections.sort(newList,new LogiComp());

                if(stat==2){
                    DiffUtil.DiffResult result=DiffUtil.calculateDiff(new DiffCallBack(oldList, newList),true);
                    Message msg=handler.obtainMessage(1);
                    msg.obj=result;
                    msg.sendToTarget();
                    datas.addAll(newList);
                }else{
                    datas.addAll(newList);
                   adapter.notifyDataSetChanged();

                }


            }


            @Override
            public void statusCode(String code) {
                isLoading = false;
                loading.setVisibility(View.GONE);
                if (refreshLayout.isRefreshing()) {
                    refreshLayout.setRefreshing(false);

                }
            }

            @Override
            public void msg(String msg) {
                Toast.makeText(ctx, msg, Toast.LENGTH_SHORT).show();
            }
        }, false);
        refresh();


//        for (int i=0;i<10;i++){
//            datas.add(new LogiEntity());
//        }
//        adapter.notifyDataSetChanged();
    }

    private LinearLayoutManager lm;

    private void initView() {
        refreshLayout = (SwipeRefreshLayout) rootView.findViewById(R.id.srl_logi_refresh);
        list = (RecyclerView) rootView.findViewById(R.id.rv_logi_list);

        lm = new WrapContentLinearLayoutManager(ctx);

        list.setLayoutManager(lm);
        list.setItemAnimator(new DefaultItemAnimator());
        loading = (ProgressBar) rootView.findViewById(R.id.pb_logi_loading);
        adapter = new LogiAdapter(ctx, datas);
        list.setAdapter(adapter);
        count= (TextView) rootView.findViewById(R.id.tv_logi_count);

//        TViewUtil.EmptyViewBuilder.getInstance(ctx.getApplicationContext()).setEmptyText("暂无订单").setShowButton(false).bindView(list);
//        toast=new Toast(ctx);
//        toast.setGravity(Gravity.CENTER,0,0);
//        toast.setDuration(Toast.LENGTH_LONG);

    }

    class LogiComp implements Comparator<LogiEntity> {


        @Override
        public int compare(LogiEntity l1, LogiEntity l2) {
            int l1oid=0,l2oid=0;
            if(l1.getOid()!=null&&!"".equals(l1.getOid())){
                l1oid=Integer.parseInt(l1.getOid());
            }
            if(l2.getOid()!=null&&!"".equals(l2.getOid())){
                l2oid=Integer.parseInt(l2.getOid());
            }
            if(l1oid>l2oid){
                return -1;
            }else if(l1oid<l2oid){
                return  1;
            }else{
                return 0;
            }
        }
    }

}
