package com.ds.arthas.logistics.widget;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.ds.arthas.logistics.utils.ResolutionUtil;

/**
 * Created by Administrator on 2016/8/1 0001.
 */
public  abstract class  BaseViewHolder<T> extends RecyclerView.ViewHolder implements View.OnClickListener{

    public ResolutionUtil resUtil;
    public Context ctx;
    public RecyclerView.Adapter adapter;
    public BaseViewHolder(View itemView,Context ctx,RecyclerView.Adapter adapter){
        super(itemView);
        this.ctx=ctx;
        this.adapter=adapter;
        resUtil=new ResolutionUtil(ctx);
        initView(itemView);
        setLintener();
    }
    public abstract void initView(View v);
    public abstract void setInfo(T entity);
    public abstract void setLintener();
}
