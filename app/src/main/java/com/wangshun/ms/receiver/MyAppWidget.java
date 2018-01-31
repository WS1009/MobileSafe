package com.wangshun.ms.receiver;

import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;

import com.wangshun.ms.service.KillProcesWidgetService;

public class MyAppWidget extends AppWidgetProvider {

	/**
	 *
	 * 创建桌面小部件的步骤： 1、需要在清单文件里面配置元数据 
	 * 2、需要配置当前元数据里面要使用到的XML 
	 * 		res/xml 
	 * 3、需要配置一个广播接收者
	 * 4、实现一个桌面小部件的XML（根据需求设计layout） 
	 *
	 * 第一次创建的时候才会调用当前的生命周期的方法
	 *
	 * 当前的广播的生命周期只有10秒钟。 不能做耗时的操作
	 */
	@Override
	public void onEnabled(Context context) {
		// TODO Auto-generated method stub
		super.onEnabled(context);
		System.out.println("onEnabled");

		// 开启服务，需要一直在后台定时刷新
		Intent intent = new Intent(context, KillProcesWidgetService.class);
		context.startService(intent);
	}

	/**
	 * 当桌面上面所有的桌面小控件都删除的时候才调用当前这个方法
	 */
	@Override
	public void onDisabled(Context context) {
		// TODO Auto-generated method stub
		super.onDisabled(context);
		Intent intent = new Intent(context, KillProcesWidgetService.class);
		context.stopService(intent);
		System.out.println("onDisabled");
	}
}
