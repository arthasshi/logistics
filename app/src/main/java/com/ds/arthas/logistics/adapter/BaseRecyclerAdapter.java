package com.ds.arthas.logistics.adapter;

import android.content.Context;
import android.support.v7.widget.ContentFrameLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ds.arthas.logistics.utils.ResolutionUtil;

import java.util.ArrayList;

/**
 * Created by Administrator on 2016/7/18 0018.
 */
public abstract class BaseRecyclerAdapter<T> extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    public  Context ctx;
    public  ArrayList<T> list;
    public  ResolutionUtil resUtil;
//    private RecyclerView.ViewHolder vh;

    public BaseRecyclerAdapter(Context ctx,ArrayList<T> list){
        this.ctx=ctx;
//        this.vh=vh;
        this.list=list;
        resUtil=new ResolutionUtil(ctx);
    }
    @Override
    public abstract RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType);

    @Override
    public abstract void onBindViewHolder(RecyclerView.ViewHolder holder, int position);

    @Override
    public int getItemCount() {
        return list.size();
    }
}
