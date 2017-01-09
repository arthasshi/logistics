package com.ds.arthas.logistics.utils;


import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
public class SharedPreferencesUtils {
	public static final String SP_NAME = "config";
	public static final String USER_INFO="userinfo";
	public static final String CACHE="cache";
	private static SharedPreferences sp;
	private static SharedPreferences spForUserInfo;
	
	public static void saveUserInfo(Context ctx,String key,String value){
		if(spForUserInfo==null){
			spForUserInfo=ctx.getSharedPreferences(USER_INFO, Context.MODE_MULTI_PROCESS);
		}
		spForUserInfo.edit().putString(key, value).commit();
	}
	public static String getUserInfo(Context ctx,String key,String def){
		if(spForUserInfo==null){
			spForUserInfo=ctx.getSharedPreferences(USER_INFO, Context.MODE_MULTI_PROCESS);
		}
		return spForUserInfo.getString(key, def);
	}
	public static void saveBoolean(Context ct, String key, boolean value) {
		if (sp == null)
			sp = ct.getSharedPreferences(SP_NAME, ct.MODE_MULTI_PROCESS );
		sp.edit().putBoolean(key, value).commit();
	}

	public static boolean getBoolean(Context ct, String key, boolean defValue) {
		if (sp == null)
			sp = ct.getSharedPreferences(SP_NAME, ct.MODE_MULTI_PROCESS);
		return sp.getBoolean(key, defValue);

	}
	public static void saveInt(Context ct, String key, int value) {
		if (sp == null)
			sp = ct.getSharedPreferences(SP_NAME, ct.MODE_MULTI_PROCESS);
		sp.edit().putInt(key, value).commit();
	}

	public static int getInt(Context ct, String key, int defValue) {
		if (sp == null)
			sp = ct.getSharedPreferences(SP_NAME, ct.MODE_MULTI_PROCESS);
		return sp.getInt(key, defValue);

	}
	public static void saveString(Context ct, String key, String value) {
		if (sp == null)
			sp = ct.getSharedPreferences(SP_NAME, ct.MODE_MULTI_PROCESS);
		sp.edit().putString(key, value).commit();
	}

	public static String getString(Context ct, String key, String defValue) {
		if (sp == null)
			sp = ct.getSharedPreferences(SP_NAME, ct.MODE_MULTI_PROCESS);
		return sp.getString(key, defValue);
	}
	public static void saveCacheJson(Context ctx,String key,String json){
		if(sp==null)
			sp=ctx.getSharedPreferences(CACHE	,Context.MODE_MULTI_PROCESS);
		sp.edit().putString(key, json).commit();
	}
	public static String getCacheJson(Context ctx,String key,String defJson){
		if(sp==null)
			sp=ctx.getSharedPreferences(CACHE	,Context.MODE_MULTI_PROCESS);
		return sp.getString(key, defJson);
	}
	
	
	public static void createSPForLogin(Context context,String accessToken,String openID,String expries_in){
		if(context==null){
			return;
		}
		SharedPreferences pref=context.getSharedPreferences(SP_NAME, Context.MODE_MULTI_PROCESS);
		Editor editor=pref.edit();
//		editor.putString(ApiConstant.ACCESSTOKEN, accessToken);
//		editor.putString(ApiConstant.OPENID, openID);
//		editor.putString(ApiConstant.EXPIRES, expries_in);
		editor.commit();
	}
	public static void createSPForLogin(Context context,String json){
		if(context==null){
			return;
		}
		SharedPreferences pref=context.getSharedPreferences(SP_NAME, Context.MODE_MULTI_PROCESS);
		Editor editor=pref.edit();
//		editor.putString(ApiConstant.USERINFO, json);
		editor.commit();
	}
	
	public static void removeStr(Context context,String key){
		if (sp == null)
			sp = context.getSharedPreferences(SP_NAME, 0);
		sp.edit().remove(key).commit();
	}
	
	public static void clear(Context context,String spname) {
		if (null == context) {
			return;
		}
		if(spname==null){
			spname=SP_NAME;
		}
		SharedPreferences pref = context.getSharedPreferences(
				spname, context.MODE_MULTI_PROCESS);
		Editor editor = pref.edit();
		editor.clear();
		editor.apply();
	}
	
}
