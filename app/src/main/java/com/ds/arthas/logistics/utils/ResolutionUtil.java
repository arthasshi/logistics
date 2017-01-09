package com.ds.arthas.logistics.utils;

import android.content.Context;
import android.content.res.Configuration;
import android.util.DisplayMetrics;
import android.widget.Toast;

public class ResolutionUtil {
	
	/**
	 * 设备屏幕的基准宽度,高度
	 */
	public static final int WINDOWS_STANDARD_SIZE_WIDTH = 640;
	public static final int WINDOWS_STANDARD_SIZE_HIGH = 1136;
	
	
	//设备的宽和高
	public static  int WINDOWS_DISPLAY_SIZE_WIDTH;
	public static  int WINDOWS_DISPLAY_SIZE_HIGH;
	
	
	/**
	 * 标准密度
	 */
	private static final float DEFAULTDENSITY = 160;
	
	/**
	 * 标准缩放大小
	 */
	private static final float DEFAULTFONTDESITY = 1.0F;
	
	/**
	 * 屏幕密度
	 */
	private float density;
	
	/**
	 * 当前屏幕的字体缩放比例
	 */
	private float fontDesity;
	
	/**
	 * 当前屏幕和标准屏幕(720P)的比例
	 */
	private float scale;
	
	/**
	 * 当前设备宽度 
	 */
	private int deviceWidth;

	/**
	 * 当前设备高度
	 */
	private int deviceHeight;
	
	/**
	 * 横向屏幕比例
	 */
	private float scaleWidth ;
	
	/**
	 * 竖向屏幕比例
	 */
	private float scaleHeight;
	
	public ResolutionUtil(Context ctx){
		DisplayMetrics dm = ctx.getResources().getDisplayMetrics();
		this.deviceWidth  = dm.widthPixels;
		this.deviceHeight = dm.heightPixels;
		WINDOWS_DISPLAY_SIZE_WIDTH = dm.widthPixels;
		WINDOWS_DISPLAY_SIZE_HIGH = dm.heightPixels;
		density = dm.densityDpi;
		fontDesity = dm.scaledDensity;
		if(deviceWidth > deviceHeight){
			scaleWidth = (float)deviceWidth / WINDOWS_STANDARD_SIZE_HIGH;
			scaleHeight = (float)deviceHeight / WINDOWS_STANDARD_SIZE_WIDTH;
		}else{
			scaleWidth = (float)deviceWidth / WINDOWS_STANDARD_SIZE_WIDTH;
			scaleHeight = (float)deviceHeight / WINDOWS_STANDARD_SIZE_HIGH;
		}
		
	}
	
	public int getWidth(){
		return deviceWidth;
	}
	
	public int getHeight(){
		return deviceHeight;
	}
	
	public int px2dp2px(float pxVlaue,boolean isWidth){
		float dp = pxVlaue / (density / DEFAULTDENSITY);
		if(isWidth)
			return (int) (dp * (density / DEFAULTDENSITY) * scaleWidth);
			
		return (int) (dp * (density / DEFAULTDENSITY) * scaleHeight);
	}
	
/*	public int px2dp2px(float pxVlaue){
		//px = dp * (density / 160)   px = dp * 1;  96      dp * 
		//dp = px / (density / 160)
		float dp = pxVlaue / (density / DEFAULTDENSITY);
		int px = (int) (dp * (density / DEFAULTDENSITY));
		return px;
	}*/
	
	public int px2sp2px(float spVlaue){  //1.0   2.0
		float dp = spVlaue / fontDesity;
		int px = (int) (dp * (fontDesity / DEFAULTFONTDESITY) / fontDesity * scaleWidth);
		return px;
	}
	
    /**
     * 将dip转为px值
     * @param context
     * @param dipValue
     * @return
     */
    public int dip2px(float dipValue){ 
		return (int)((int)(dipValue * scale) * density +0.5);
	} 

	public int px2dip(Context context, float pxValue) {
		return (int)((int)(pxValue * scale) / density + 0.5 );
	}
}
