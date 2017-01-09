package com.ds.arthas.logistics.utils;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.text.TextUtils;

public class StringUtils {
	final static int BUFFER_SIZE = 4096;
	public final static String GB2312 = "gb2312";
	public final static String UTF_8 = "UTF-8";

	/** ��ȡ��ǰ�汾�� */
	public static String getVersion(Context context) {
		try {
			PackageManager manager = context.getPackageManager();// �õ���������
			PackageInfo info = manager.getPackageInfo(context.getPackageName(), 0);// ��ȡָ�������Ϣ
			return info.versionName;// ��ȡ�汾
		} catch (NameNotFoundException e) {
			e.printStackTrace();
			return "unkonwn";
		}
	}
	public static String ToDBC(String input) {
		   char[] c = input.toCharArray();
		   for (int i = 0; i< c.length; i++) {
		       if (c[i] == 12288) {
		         c[i] = (char) 32;
		         continue;
		       }if (c[i]> 65280&& c[i]< 65375)
		          c[i] = (char) (c[i] - 65248);
		       }
		   return new String(c);
		}
	/**
	 * ��InputStream����ת����String
	 * 
	 * @param is
	 * @return
	 */
	public static String converStreamToString(InputStream is) {

		if (is == null) {
			return null;
		}

		BufferedReader reader = new BufferedReader(new InputStreamReader(is));
		StringBuffer buffer = new StringBuffer();

		int count = 0;
		while (count < 3) {

			try {
				String line = null;
				while ((line = reader.readLine()) != null) {
					buffer.append(line);
				}
				break;
			} catch (IOException e) {
				count++;
			}
		}

		return buffer.toString();
	}

	/*
	 * String 转换InputStream 
	 */
	public static InputStream converStringToStream(String str){
		if(TextUtils.isEmpty(str)) return null;
		return new ByteArrayInputStream(str.getBytes());
		
	}
	
	/**
	 * ��InputStreamת����ĳ���ַ�����String
	 * 
	 * @param in
	 * @param encoding
	 * 
	 */
	public static String converStreamToString(InputStream in, String encoding) {

		ByteArrayOutputStream outStream = new ByteArrayOutputStream();
		byte[] data = new byte[BUFFER_SIZE];
		int count = -1;
		try {
			while ((count = in.read(data, 0, BUFFER_SIZE)) != -1) {
				outStream.write(data, 0, count);
			}
			data = null;
			return new String(outStream.toByteArray(), encoding);
		} catch (IOException e) {
			e.printStackTrace();
		}

		return null;

	}

	public static String timeString(int time) {
		time = time / 1000;
		String timeStr = null;
		int hour = 0;
		int minute = 0;
		int second = 0;
		if (time <= 0)
			return "00:00";
		else {
			minute = time / 60;
			if (minute < 60) {
				second = time % 60;
				timeStr = unitFormat(minute) + ":" + unitFormat(second);
			} else {
				hour = minute / 60;
				if (hour > 99)
					return "99:59:59";
				minute = minute % 60;
				second = time - hour * 3600 - minute * 60;
				timeStr = unitFormat(hour) + ":" + unitFormat(minute) + ":" + unitFormat(second);
			}
		}
		return timeStr;
	}

	public static String intTimeString(int time) {
		int miao = time % 60;
		int fen = time / 60;
		int hour = 0;
		if (fen >= 60) {
			hour = fen / 60;
			fen = fen % 60;
		}
		String timeString = "";
		String miaoString = "";
		String fenString = "";
		String hourString = "";
		if (miao < 10) {
			miaoString = "0" + miao;
		} else {
			miaoString = miao + "";
		}
		if (fen < 10) {
			fenString = "0" + fen;
		} else {
			fenString = fen + "";
		}
		if (hour < 10) {
			hourString = "0" + hour;
		} else {
			hourString = hour + "";
		}
		if (hour != 0) {
			timeString = hourString + ":" + fenString + ":" + miaoString;
		} else {
			timeString = fenString + ":" + miaoString;
		}
		return timeString;
	}
	
	public static boolean isEmpty(String str) {
		if(null != str)
		{
			if(str.length() > 4 )
			{
				return false;
			}
		}
		return null == str || "".equals(str)||"NULL".equals(str.toUpperCase());
	}
	
	public static boolean isEmptyStr(String str) {
		return null == str || "".equals(str);
	}
	
	public static boolean isEmptyArray(Object[] array, int len) {
		return null == array || array.length < len;
	}

	public static String unitFormat(int i) {
		String retStr = null;
		if (i >= 0 && i < 10)
			retStr = "0" + Integer.toString(i);
		else
			retStr = "" + i;
		return retStr;
	}

	public static String replaceBlank(String str) {
		String dest = "";
		if (str != null) {
			Pattern p = Pattern.compile("\\s*|\t|\r|\n");
			Matcher m = p.matcher(str);
			dest = m.replaceAll("");
		}
		return dest;
	}

	public static String getDateTime(long value) {
		System.out.println(value);
		Date date = new Date(value);
		SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd HH:mm");
		System.out.println(format.format(date));

		return format.format(date);
	}

	// Convert Unix timestamp to normal date style
	public static String TimeStamp2Date(String timestampString) {
		Long timestamp = Long.parseLong(timestampString) * 1000;
		String date = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(new Date(timestamp));
		return date;
	}

	public static double getM(double b) {
		double m;
		double kb;
		kb = b / 1024.0;
		m = kb / 1024.0;
		return m;
	}

	public static String getImage260_360Url(String originalUrl) {
		int dotIndex = originalUrl.lastIndexOf(".");
		String sizeUrlExe = originalUrl.substring(dotIndex, originalUrl.length());
		String sizeUrlHead = originalUrl.substring(0, dotIndex);
		String sizeNewUrl = sizeUrlHead + "_260_360" + sizeUrlExe;
		return sizeNewUrl;
	}
	
	/**
	 * 获取现在时间
	 * 
	 * @return 返回短时间字符串格式yyyy-MM-dd HH:mm:ss
	 */
	public static String getStringDate() {
		Date currentTime = new Date();
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String dateString = formatter.format(currentTime);
		return dateString;
	}
	
	/**
	 * 将时间date 2014-8-14转换为 8月14号
	 * @param date 2014-8-14
	 * @return x月x号
	 */
	public static String dateFormate(String date){
		String[] arr = date.split("-");
		String year = arr[0];
		String month = arr[1];
		String day = arr[2];
		
		return Integer.parseInt(month) + "月" + Integer.parseInt(day) + "日";
//		return date;
	}
	
	/**
	 * 将时间戳转换为自定义时间
	 * @param time
	 * @return
	 */
	public static String LongDateFormateStr(String time){
//		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd  HH:mm:ss");
		SimpleDateFormat format = new SimpleDateFormat("MM-dd  HH:mm");
		return format.format(Long.parseLong(time));
	}
	
	public static String BaseEncryption(String str){
		try {
//			return Base64.encodeToString(str.getBytes(UTF_8), Base64.NO_PADDING).toString().trim();
			return Base64.encodeToString(str.getBytes(UTF_8), Base64.NO_PADDING).toString().trim();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	
	
	  //时间戳格式转换
    public static String getChatTime(long timesamp) {
            String result = "";
            SimpleDateFormat sdf = new SimpleDateFormat("dd");
            Date today = new Date(System.currentTimeMillis());
            Date otherDay = new Date(timesamp);
            int temp = Integer.parseInt(sdf.format(today))
                            - Integer.parseInt(sdf.format(otherDay));
            switch (temp) {
            case 0:
                    result = "今天 " + getHourAndMin(timesamp);
                    break;
            case 1:
                    result = "昨天 " + getHourAndMin(timesamp);
                    break;
            case 2:
                    result = "前天 " + getHourAndMin(timesamp);
                    break;

            default:
                    result = getTime(timesamp);
                    break;
            }
            return result;
    }
    
    public static String getHourAndMin(long time) {
            SimpleDateFormat format = new SimpleDateFormat("HH:mm");
            return format.format(new Date(time));
    }
    
    public static String getTime(long time) {
            SimpleDateFormat format = new SimpleDateFormat("MM-dd HH:mm");
            return format.format(new Date(time));
    }
	
}
