package com.wangshun.ms.utils;

import android.app.Activity;
import android.widget.Toast;

public class UIUtils {
	public static void showToast(final Activity context,final String msg){
		//主线程
		if("main".equals(Thread.currentThread().getName())){
			Toast.makeText(context, msg, 1).show();
		}else{
			//自线程
			context.runOnUiThread(new Runnable() {
				@Override
				public void run() {
					Toast.makeText(context, msg, 1).show();
				}
			});
		}
	}
}
