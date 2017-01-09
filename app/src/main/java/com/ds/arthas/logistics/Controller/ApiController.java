package com.ds.arthas.logistics.Controller;

import android.content.Context;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.ds.arthas.logistics.Constant.ApiConstant;
import com.ds.arthas.logistics.DsApplication;
import com.ds.arthas.logistics.entity.LogiEntity;
import com.ds.arthas.logistics.entity.UserEntity;
import com.ds.arthas.logistics.entity.UserInfoEntity;
import com.ds.arthas.logistics.entity.VersionEntity;
import com.ds.arthas.logistics.utils.Logger;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONStringer;
import org.json.JSONTokener;

import java.security.interfaces.DSAKey;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2016/7/19 0019.
 */
public class ApiController {

    private Context ctx;
    private NetWorkCallBack callBack;
    private boolean isCache;
    private Gson gson;
    public ApiController(Context ctx,NetWorkCallBack callBack,boolean isCache){
        this.ctx=ctx;
        this.callBack=callBack;
        this.isCache=isCache;
        gson=new Gson();
    }
//------------------------------------下面创建接口请求方法------------------------------------------------------------------
    //login api
    public void login(HashMap<String,String> map){
        String url= ApiConstant.LOGIN;
        post(url, UserEntity.class,map);
    }
    public void getLogiList(HashMap<String,String> map){
        String url=ApiConstant.LOGILIST;
        post(url,LogiEntity.class,map);

    }

    public void sendActive(HashMap<String,String> map){
        String url=ApiConstant.SENDACTIVE;
        post(url,LogiEntity.class,map);
    }

    public void uploadGps(HashMap<String,String> map){
        String url=ApiConstant.LOCALUPLOAD;
        post(url,null,map);
    }

    public void getUserInfo(HashMap<String,String> map){
        String url=ApiConstant.GETUSERINFO;
        post(url, UserInfoEntity.class,map);
    }
    public void changeWork(HashMap<String,String> map){
        String url=ApiConstant.CHNAGEWORK;
        post(url,null,map);
    }


    public void getNewVersion(int versioncode){
        String url=String.format(ApiConstant.GETNEWVERSION,versioncode);
        get(url, VersionEntity.class);
    }


    //--------------------------------------看什么看 没见过分割线啊---------------------------------------------------------------------
    public <T> void get(final String url,final Class<T> cls){

        StringRequest request= new StringRequest(url,new Response.Listener<String>(){

            @Override
            public void onResponse(String s) {
                Logger.d("==","get:"+s);
                String errorCode;
//                String msg;
                Object json;
                try {
                    JSONObject jo=new JSONObject(s);
                    errorCode=jo.getString("errorcode");
                    callBack.statusCode(errorCode);
                   if("0".equals(errorCode)){
                        json=new JSONTokener(jo.getString("data")).nextValue();
                       if(json instanceof JSONObject){
                           JSONObject obj= (JSONObject) json;
                           T entity=gson.fromJson(obj.toString(),(Class<T>)cls);
                           callBack.response(entity);
                       }else if(json instanceof JSONArray){
                           JSONArray ja= (JSONArray) json;
                           ArrayList<T> list=new ArrayList<>();
                           for (int i=0;i<ja.length();i++){
                               list.add(gson.fromJson(ja.get(i).toString(),(Class<T>)cls));
                           }
                            callBack.response(list);
                       }
                   }else{
                       callBack.msg(jo.getString("msg"));
                   }

                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {

            }
        });
        DsApplication.queue.add(request);
    }

    public <T> void post(final String url, final Class<T> cls, final HashMap<String,String> map){
        Iterator i=map.keySet().iterator();
        while (i.hasNext()){
            String s= (String) i.next();
            Logger.d("==",s+":"+map.get(s));
        }

        StringRequest request=new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                Logger.d("==",s);
                String errorCode;
//                String msg;
                Object json;
                try {
                    JSONObject jo=new JSONObject(s);
                    errorCode=jo.getString("errorcode");
                    callBack.statusCode(errorCode);
                    if("0".equals(errorCode)){
                        json=new JSONTokener(jo.getString("data")).nextValue();
                        if(json instanceof JSONObject){
                            JSONObject obj= (JSONObject) json;
                            T entity=gson.fromJson(obj.toString(),(Class<T>)cls);
                            callBack.response(entity);
                            Logger.d("==","ENTITY");
                        }else if(json instanceof JSONArray){
                            JSONArray ja= (JSONArray) json;
                            ArrayList<T> list=new ArrayList<>();
                            for (int i=0;i<ja.length();i++){
                                list.add(gson.fromJson(ja.get(i).toString(),(Class<T>)cls));
                            }
                            Logger.d("==","list");
                            callBack.response(list);
                        }
                    }else{
                        callBack.msg(jo.getString("msg"));
                    }

                } catch (Exception e){
                    e.printStackTrace();
                }


            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Log.d("==","1"+volleyError.toString());
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                return map;
            }
        };
        request.setRetryPolicy(new DefaultRetryPolicy(5000,DefaultRetryPolicy.DEFAULT_MAX_RETRIES,DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        if(DsApplication.queue!=null){
            DsApplication.queue.add(request);
        }

    }

    public interface NetWorkCallBack{
        <T>void response(T entity);
        <T>void response(ArrayList<T> list);
        void statusCode(String code);
        void msg(String msg);
    }
}
