package com.ds.arthas.logistics.broadcastreceiver;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.telephony.PhoneStateListener;
import android.telephony.SignalStrength;
import android.telephony.TelephonyManager;
import android.widget.Toast;

import com.ds.arthas.logistics.DsApplication;

/**
 * Created by Administrator on 2016/10/26 0026.
 */

public class NetBroadCastReceiver extends BroadcastReceiver {

    private ConnectivityManager cm;
    private NetworkInfo ni;
    private String strNetworkType;

    private Context ctx;
    @Override
    public void onReceive(Context context, Intent intent) {
        this.ctx=context;
         String action=intent.getAction();
        if(action.equals(ConnectivityManager.CONNECTIVITY_ACTION)){
            cm= (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            ni=cm.getActiveNetworkInfo();
            if(ni!=null&&ni.isAvailable()){
                String name=ni.getTypeName();
                if(ni.getType()==ConnectivityManager.TYPE_WIFI){
                    strNetworkType = "wifi";
                }else if(ni.getType()==ConnectivityManager.TYPE_MOBILE){

                    int subtype=ni.getSubtype();
                    String _strSubTypeName=ni.getSubtypeName();
                    switch (subtype){
                        case TelephonyManager.NETWORK_TYPE_GPRS:
                        case TelephonyManager.NETWORK_TYPE_EDGE:
                        case TelephonyManager.NETWORK_TYPE_CDMA:
                        case TelephonyManager.NETWORK_TYPE_1xRTT:
                        case TelephonyManager.NETWORK_TYPE_IDEN: //api<8 : replace by 11
                            strNetworkType = "2G";
                            break;
                        case TelephonyManager.NETWORK_TYPE_UMTS:
                        case TelephonyManager.NETWORK_TYPE_EVDO_0:
                        case TelephonyManager.NETWORK_TYPE_EVDO_A:
                        case TelephonyManager.NETWORK_TYPE_HSDPA:
                        case TelephonyManager.NETWORK_TYPE_HSUPA:
                        case TelephonyManager.NETWORK_TYPE_HSPA:
                        case TelephonyManager.NETWORK_TYPE_EVDO_B: //api<9 : replace by 14
                        case TelephonyManager.NETWORK_TYPE_EHRPD:  //api<11 : replace by 12
                        case TelephonyManager.NETWORK_TYPE_HSPAP:  //api<13 : replace by 15
                            strNetworkType = "3G";
                            break;
                        case TelephonyManager.NETWORK_TYPE_LTE:    //api<11 : replace by 13
                            strNetworkType = "4G";
                            break;
                        default:
                            // http://baike.baidu.com/item/TD-SCDMA 中国移动 联通 电信 三种3G制式
                            if (_strSubTypeName.equalsIgnoreCase("TD-SCDMA") || _strSubTypeName.equalsIgnoreCase("WCDMA") || _strSubTypeName.equalsIgnoreCase("CDMA2000"))
                            {
                                strNetworkType = "3G";
                            }
                            else
                            {
                                strNetworkType = _strSubTypeName;
                            }

                            break;
                    }

                }else if(ni.getType()==ConnectivityManager.TYPE_ETHERNET){
                    strNetworkType = "以太网";
                }
            }else{
                strNetworkType="网络断开连接";
            }
            DsApplication.getInstance().setNetWorkStat(strNetworkType);
        }
    }


}
