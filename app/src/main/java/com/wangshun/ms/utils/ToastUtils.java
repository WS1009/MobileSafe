package com.wangshun.ms.utils;

import android.content.Context;
import android.widget.Toast;

/**
 * author：WS
 * version：1.0
 * Created  on 2016/1/27 at 10:42.
 */
public class ToastUtils {
    public static void showToast(Context ctx,String text){
        Toast.makeText(ctx,text, Toast.LENGTH_SHORT).show();
    }

}
