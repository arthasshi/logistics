package com.ds.arthas.logistics.utils;

import android.app.Activity;
import android.os.Handler;
import android.view.WindowManager;


/**
 * Created by Administrator on 2016/7/18 0018.
 */
public class DisplayUtils {
    public static void showOrHidePopwindow(final boolean isShow, final Activity act) {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                WindowManager.LayoutParams lp =act.getWindow().getAttributes();
                if (isShow) {
                    lp.alpha = 0.8f;
                } else {
                    lp.alpha = 1.0f;
                }
                act.getWindow().setAttributes(lp);
            }
        }, 20);
    }
}
