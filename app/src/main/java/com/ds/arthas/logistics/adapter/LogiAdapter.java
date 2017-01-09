package com.ds.arthas.logistics.adapter;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v4.animation.AnimatorCompatHelper;
import android.support.v7.widget.CardView;
import android.support.v7.widget.DecorContentParent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewPropertyAnimator;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.amap.api.maps2d.model.Text;
import com.ds.arthas.logistics.Controller.ApiController;
import com.ds.arthas.logistics.DsApplication;
import com.ds.arthas.logistics.R;
import com.ds.arthas.logistics.activity.MapActivity;
import com.ds.arthas.logistics.entity.LogiEntity;
import com.ds.arthas.logistics.utils.DisplayUtils;
import com.ds.arthas.logistics.widget.LogiItemViewHolder;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Administrator on 2016/7/18 0018.
 */
public class LogiAdapter extends BaseRecyclerAdapter<LogiEntity>{


//    private PopupWindow ensure;
//    private boolean isOpen;
//    private LinearLayout moveLL;
//    private LinearLayout lGood;
    public LogiAdapter(Context ctx, ArrayList<LogiEntity> list) {
        super(ctx, list);
    }

    public void setDatas( ArrayList<LogiEntity> list){
        this.list=list;

    }
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(ctx).inflate(R.layout.item_logi,parent,false);
        return new LogiItemViewHolder(v,ctx,this);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ((LogiItemViewHolder)holder).setInfo(list.get(position));
//        moveLL=((ViewHoler)holder).moveContainer;
//        reSetItemView(((ViewHoler)holder).moveContainer);
    }


//
//    public class ViewHoler extends RecyclerView.ViewHolder{
//
//        public TextView name;
//        private TextView address;
//        public CardView cv;
//        private TextView stat;
//        private LinearLayout.LayoutParams lp;
//        private LinearLayout moveContainer;
//        private LinearLayout goodList;
//        private TextView price;
//        private TextView orderId;
//        private TextView orderType;
//        private TextView shopName;
//        private TextView shopAddress;
//        private LinearLayout statsContainer;
//        private TextView stat1;
//        private TextView stat2;
//        private TextView stat3;
//        private TextView stat4;
//        private TextView orderRemark;
//        public ViewHoler(View itemView) {
//            super(itemView);
//            name= (TextView) itemView.findViewById(R.id.tv_item_logi_name);
//            cv= (CardView) itemView.findViewById(R.id.c_item_logi);
//            lp= (LinearLayout.LayoutParams) cv.getLayoutParams();
//            lp.width= RecyclerView.LayoutParams.MATCH_PARENT;
//            lp.height=RecyclerView.LayoutParams.WRAP_CONTENT;
//            stat= (TextView) itemView.findViewById(R.id.tv_item_logi_logistat);
//            moveContainer= (LinearLayout) itemView.findViewById(R.id.ll_item_logi_move);
//            goodList= (LinearLayout) itemView.findViewById(R.id.ll_item_logi_orderlist);
//            address= (TextView) itemView.findViewById(R.id.tv_item_logi_address);
//            price= (TextView) itemView.findViewById(R.id.tv_order_price);
//            orderId= (TextView) itemView.findViewById(R.id.tv_item_order_num);
//            orderType= (TextView) itemView.findViewById(R.id.tv_item_logi_logitype);
//            shopName= (TextView) itemView.findViewById(R.id.tv_item_logi_shopname);
//            shopAddress= (TextView) itemView.findViewById(R.id.tv_item_logi_shopaddress);
//            statsContainer= (LinearLayout) itemView.findViewById(R.id.ll_item_logi_status);
//            stat1= (TextView) itemView.findViewById(R.id.tv_item_logi_stat1);
//            stat2= (TextView) itemView.findViewById(R.id.tv_item_logi_stat2);
//            stat3= (TextView) itemView.findViewById(R.id.tv_item_logi_stat3);
//            stat4= (TextView) itemView.findViewById(R.id.tv_item_logi_stat4);
//            orderRemark= (TextView) itemView.findViewById(R.id.tv_item_logi_tip);
//        }
//
//    }
}
