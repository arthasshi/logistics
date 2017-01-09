package com.ds.arthas.logistics.utils;

import android.support.v7.util.DiffUtil;

import com.ds.arthas.logistics.entity.LogiEntity;

import java.util.ArrayList;


/**
 * Created by Administrator on 2016/10/9 0009.
 */

public  class DiffCallBack extends DiffUtil.Callback  {
    private ArrayList<LogiEntity> newList=new ArrayList<>();
    private ArrayList<LogiEntity> oldList=new ArrayList<>();
    public DiffCallBack (ArrayList<LogiEntity> oldList,ArrayList<LogiEntity> newList){
        this.newList.addAll(newList);
        this.oldList.addAll(oldList);

    }

    @Override
    public int getOldListSize() {
        return oldList==null?0:oldList.size();
    }

    @Override
    public int getNewListSize() {
        return newList==null?0:newList.size();
    }

    @Override
    public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
        return oldList.get(oldItemPosition).getId().equals(newList.get(newItemPosition).getId());
    }

    @Override
    public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
        return false;
    }


}
