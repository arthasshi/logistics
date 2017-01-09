package com.ds.arthas.logistics.widget;

import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.Spanned;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ds.arthas.logistics.Controller.ApiController;
import com.ds.arthas.logistics.DsApplication;
import com.ds.arthas.logistics.R;
import com.ds.arthas.logistics.activity.MapActivity;
import com.ds.arthas.logistics.adapter.LogiAdapter;
import com.ds.arthas.logistics.entity.LogiEntity;
import com.ds.arthas.logistics.utils.DisplayUtils;
import com.ds.arthas.logistics.utils.StringUtils;
import com.sdsmdg.tastytoast.TastyToast;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Administrator on 2016/8/1 0001.
 */
public class LogiItemViewHolder extends BaseViewHolder{

    public TextView name;
    private TextView address;
    public CardView cv;
    private TextView stat;
    private LinearLayout.LayoutParams lp;
    private RelativeLayout.LayoutParams rp;
    private LinearLayout moveContainer;
    private LinearLayout goodList;
    private TextView price;
    private TextView orderId;
    private TextView orderType;
    private TextView shopName;
    private TextView shopAddress;
    private LinearLayout statsContainer;
    private TextView stat1;
    private TextView stat2;
    private TextView stat3;
    private TextView stat4;
    private TextView orderRemark;

    private TextView addTime;

    private PopupWindow ensure;
    private boolean isOpen;


    private LogiEntity le;

    public LogiItemViewHolder(View itemView, Context ctx,RecyclerView.Adapter adapter) {
        super(itemView,ctx,adapter);
    }

    @Override
    public void initView(View item) {
        name= (TextView) itemView.findViewById(R.id.tv_item_logi_name);
        cv= (CardView) itemView.findViewById(R.id.c_item_logi);
        lp= (LinearLayout.LayoutParams) cv.getLayoutParams();
        lp.width= RecyclerView.LayoutParams.MATCH_PARENT;
        lp.height=RecyclerView.LayoutParams.WRAP_CONTENT;
        stat= (TextView) itemView.findViewById(R.id.tv_item_logi_logistat);
        moveContainer= (LinearLayout) itemView.findViewById(R.id.ll_item_logi_move);
        goodList= (LinearLayout) itemView.findViewById(R.id.ll_item_logi_orderlist);
        rp= (RelativeLayout.LayoutParams) goodList.getLayoutParams();
//        rp.height=resUtil.px2dp2px(30,false);
        address= (TextView) itemView.findViewById(R.id.tv_item_logi_address);
        address.setTextSize(resUtil.px2sp2px(30));
        address.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);
        price= (TextView) itemView.findViewById(R.id.tv_order_price);
        orderId= (TextView) itemView.findViewById(R.id.tv_item_order_num);
        orderType= (TextView) itemView.findViewById(R.id.tv_item_logi_logitype);
        shopName= (TextView) itemView.findViewById(R.id.tv_item_logi_shopname);
        shopAddress= (TextView) itemView.findViewById(R.id.tv_item_logi_shopaddress);
        shopAddress.setTextSize(resUtil.px2sp2px(30));
        shopAddress.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);
        statsContainer= (LinearLayout) itemView.findViewById(R.id.ll_item_logi_status);
        stat1= (TextView) itemView.findViewById(R.id.tv_item_logi_stat1);
        stat2= (TextView) itemView.findViewById(R.id.tv_item_logi_stat2);
        stat3= (TextView) itemView.findViewById(R.id.tv_item_logi_stat3);
        stat4= (TextView) itemView.findViewById(R.id.tv_item_logi_stat4);
        orderRemark= (TextView) itemView.findViewById(R.id.tv_item_logi_tip);
        addTime= (TextView) itemView.findViewById(R.id.tv_item_logi_addtime);
    }

    @Override
    public void setInfo(Object entity) {
        le= (LogiEntity) entity;
        String[] delivery_info=le.getCustomeraddress().split(" ");

        orderId.setText("订单编号："+le.getOid());
        price.setText("订单总价："+le.getPrice()+"元");

        if(delivery_info.length==3){
            name.setText(delivery_info[0]+"  "+delivery_info[2]);
            address.setText("配送地址："+delivery_info[1]);
        }else if(delivery_info.length>3){
            name.setText(delivery_info[0]+" "+delivery_info[delivery_info.length-1]);
            address.setText("配送地址："+le.getCustomeraddress());
        }else{
            name.setText("服务器参数错误");
            address.setText("配送地址："+le.getCustomeraddress());
        }


        goodList.removeAllViews();
        shopName.setText(le.getShopname()+"  "+le.getShopphone());
        shopAddress.setText("取货地址："+le.getShopaddress());
        if(le.getOaddtime()!=null&&!"".equals(le.getOaddtime())){
//            Spanned html= Html.fromHtml("下单时间：<font color= '#FF0000'><big>"+StringUtils.getDateTime(Long.parseLong(le.getOaddtime())*1000)+"</big></font>");
            addTime.setText("下单时间："+ StringUtils.getDateTime(Long.parseLong(le.getOaddtime())*1000));
//            addTime.setText(html);
        }
        for (int i=0;i<le.getGoods().size();i++){
            TextView tv=new TextView(ctx);
            tv.setTextColor(Color.parseColor("#333333"));
            LogiEntity.GoodsBean good=le.getGoods().get(i);
            tv.setText(good.getGname()+"*"+good.getNum()+"   ￥"+good.getPrice()+"\n"+"商品描述:"+good.getProperties());
            goodList.addView(tv);
        }
        switch (le.getStatus()){
            case "-1":
                statsContainer.setVisibility(View.INVISIBLE);
                stat.setText("已取消");
                stat.setEnabled(false);
                break;
            case "1":
                statsContainer.setVisibility(View.VISIBLE);
                stat1.setTextColor(Color.GREEN);
                stat2.setTextColor(Color.BLACK);
                stat3.setTextColor(Color.BLACK);
                stat4.setTextColor(Color.BLACK);
                stat.setText("接单");
                stat.setEnabled(true);
//                stat.setTag(position);
                break;
            case "2":
                statsContainer.setVisibility(View.VISIBLE);
                stat1.setTextColor(Color.GREEN);
               stat2.setTextColor(Color.GREEN);
                stat3.setTextColor(Color.BLACK);
                stat4.setTextColor(Color.BLACK);
                stat.setText("已取货");
                stat.setEnabled(true);
//                stat.setTag(position);
                break;
            case "3":
                statsContainer.setVisibility(View.VISIBLE);
                stat1.setTextColor(Color.GREEN);
                stat2.setTextColor(Color.GREEN);
                stat3.setTextColor(Color.GREEN);
                stat4.setTextColor(Color.BLACK);
                stat.setText("配送完成");
                stat.setEnabled(true);
//               stat.setTag(position);
                break;
            case "4":
                statsContainer.setVisibility(View.VISIBLE);
                stat1.setTextColor(Color.GREEN);
                stat2.setTextColor(Color.GREEN);
                stat3.setTextColor(Color.GREEN);
                stat4.setTextColor(Color.GREEN);
                stat.setText("订单结束");
                stat.setEnabled(false);
                break;
            default:
                break;
        }

        orderRemark.setText(("".equals(le.getRemark())||le.getRemark()==null)?"暂无备注":le.getRemark());
        if(1==le.getPaytype()){
            orderType.setText("货到付款");
        }else{
            orderType.setText("已支付");
        }
//        orderType.setText((1==le.getPaytype())?"货到付款":"已支付");
    }


    @Override
    public void setLintener() {
        stat.setOnClickListener(this);
//        moveContainer.setOnClickListener(this);
//        goodList.setOnClickListener(this);
        shopAddress.setOnClickListener(this);
        address.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.tv_item_logi_logistat:
                showEnsurePop(view);
//                Intent intent=new Intent(ctx, MapActivity.class);
//                ctx.startActivity(intent);
                break;
            case R.id.tv_pop_ensure_cancel:
                if(ensure!=null){
                    ensure.dismiss();
                }
                break;
            case R.id.tv_pop_ensure_ensure:
                if(ensure!=null){
                    ensure.dismiss();
//                    ((TextView)view).setText("已完成")
                }
                break;
//            case R.id.ll_item_logi_orderlist:
//            case R.id.ll_item_logi_move:
//                rotateIcon(view);
//                if(isOpen){
//                    reSetItemView(view);
//                }else{
//                    RelativeLayout.LayoutParams rp= (RelativeLayout.LayoutParams) goodList.getLayoutParams();
//                    rp.height= RecyclerView.LayoutParams.WRAP_CONTENT;
//                    isOpen=true;
//                    Log.d("==","move click ");
//                    adapter.notifyDataSetChanged();
//                }
//                break;
            case R.id.tv_item_logi_address:
                toMapActivity(address.getText().toString());
                break;
            case R.id.tv_item_logi_shopaddress:
                toMapActivity(shopAddress.getText().toString());
                break;
        }
    }

    public void  toMapActivity(String str){
        Intent intent=new Intent(ctx, MapActivity.class);
        intent.putExtra("searchStr",str);
        ctx.startActivity(intent);
    }
    private ApiController controller;
    private void sendActive(int stat,String remark,String id){
        if(controller==null){
            controller=new ApiController(ctx, new ApiController.NetWorkCallBack() {
                @Override
                public <T> void response(T entity) {

                }

                @Override
                public <T> void response(ArrayList<T> list) {

                }

                @Override
                public void statusCode(String code) {
                    if("0".equals(code)){
                        ensure.dismiss();
                        DsApplication.getInstance().getLf().refresh();
                        TastyToast.makeText(ctx,"状态修改成功...",TastyToast.LENGTH_SHORT,TastyToast.SUCCESS);
                    }
                }

                @Override
                public void msg(String msg) {
                    Toast.makeText(ctx, msg, Toast.LENGTH_SHORT).show();
                }
            },false);
        }

        HashMap<String,String> map=new HashMap<>();
        map.put("uid", DsApplication.getUid());
        map.put("token",DsApplication.getToken());
        map.put("status",(stat+1)+"");
        map.put("id",id);
        map.put("remark",remark);
        controller.sendActive(map);
        TastyToast.makeText(ctx,"操作中...",TastyToast.LENGTH_SHORT,TastyToast.DEFAULT);

    }
    EditText input;
    private void showEnsurePop( View v) {
        if(ensure==null){
            View view= LayoutInflater.from(ctx).inflate(R.layout.pop_ensure,null);
            input= (EditText) view.findViewById(R.id.et_pop_ensure_input);
            TextView cancel= (TextView) view.findViewById(R.id.tv_pop_ensure_cancel);
            TextView sure= (TextView) view.findViewById(R.id.tv_pop_ensure_ensure);
            TextView title= (TextView) view.findViewById(R.id.tv_pop_ensure_title);
            switch (le.getStatus()){
                case "1":
                    title.setText("确定接单？");
                    break;
                case "2":
                    title.setText("确定已取货？");
                    break;
                case "3":
                    title.setText("确定已送达？");
                    break;
            }

            ensure=new PopupWindow(view,resUtil.px2dp2px(500,true), RecyclerView.LayoutParams.WRAP_CONTENT);
            ensure.setOutsideTouchable(false);
            ensure.setFocusable(true);
            ensure.setInputMethodMode(PopupWindow.INPUT_METHOD_FROM_FOCUSABLE);
            ensure.setBackgroundDrawable(new ColorDrawable());

            cancel.setOnClickListener(this);
            sure.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    sendActive(Integer.parseInt(le.getStatus()),input.getText().toString(),le.getId());
                }
            });
            ensure.setOnDismissListener(new PopupWindow.OnDismissListener() {
                @Override
                public void onDismiss() {
                    DisplayUtils.showOrHidePopwindow(false,((Activity)ctx));
                }
            });

        }
        input.setText("");
        ensure.showAtLocation(v, Gravity.CENTER,0,0);
        DisplayUtils.showOrHidePopwindow(true,((Activity)ctx));

    }
    public void reSetItemView(View view){
        RelativeLayout.LayoutParams rp= (RelativeLayout.LayoutParams) goodList.getLayoutParams();
        rp.height=resUtil.px2dp2px(30,false);
        adapter.notifyDataSetChanged();
        isOpen=false;

    }
    public void rotateIcon(View view){
        ObjectAnimator oa;
        if (isOpen){
            oa= ObjectAnimator.ofFloat(((LinearLayout)view).getChildAt(0), "rotationX",0f,180f,0f);
        }else{
            oa= ObjectAnimator.ofFloat(((LinearLayout)view).getChildAt(0), "rotationX",0f,180f,180f);
        }
        oa.setDuration(300);
        oa.start();
    }
}
