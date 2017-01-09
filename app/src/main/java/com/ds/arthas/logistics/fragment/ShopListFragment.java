package com.ds.arthas.logistics.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ds.arthas.logistics.R;

/**
 * Created by Administrator on 2016/7/18 0018.
 */
public class ShopListFragment extends BaseFragment {
    private View rootView;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView=LayoutInflater.from(ctx).inflate(R.layout.fragment_shoplist,null);
        removeParent(rootView);
        return rootView;
    }
}

