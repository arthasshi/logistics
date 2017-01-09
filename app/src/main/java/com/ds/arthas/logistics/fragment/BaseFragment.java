package com.ds.arthas.logistics.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.View;
import android.view.ViewGroup;

import com.ds.arthas.logistics.utils.ResolutionUtil;

/**
 * Created by Administrator on 2016/7/15 0015.
 */
public class BaseFragment extends Fragment{

    public  Context ctx;
    public  ResolutionUtil resUtil;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.ctx=context;
        resUtil=new ResolutionUtil(context);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }
    /**
     * 这个方法是为了防止fragment的切换过程中出现的已经存在parents的情况
     * @param v
     */
    public void removeParent(View v){
        ViewGroup p= (ViewGroup) v.getParent();
        if(p!=null){
            p.removeAllViews();
        }
    }
}
