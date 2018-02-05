package com.wangshun.ms.receiver;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.wangshun.ms.service.KillProcesWidgetService;

/**
 * 窗体小部件的广播接收者
 */

public class MyAppWidget extends AppWidgetProvider {

    public static final String TAG = "Widget";

    @Override
    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);
    }

    /**
     * 创建桌面小部件的步骤：
     * 1、需要在清单文件里面配置元数据
     * 2、需要配置当前元数据里面要使用到的XML
     * res/xml
     * 3、需要配置一个广播接收者
     * 4、实现一个桌面小部件的XML（根据需求设计layout）
     * <p>
     * 第一次创建的时候才会调用当前的生命周期的方法
     * <p>
     * 当前的广播的生命周期只有10秒钟。 不能做耗时的操作
     */
    @Override
    public void onEnabled(Context context) {
        super.onEnabled(context);
        Log.i(TAG, "onEnabled: 当创建第一个窗体小部件的时候调用");

        // 开启服务，需要一直在后台定时刷新
        Intent intent = new Intent(context, KillProcesWidgetService.class);
        context.startService(intent);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        Log.i(TAG, "onUpdate: 当创建多一个窗体小部件的时候调用");
        super.onUpdate(context, appWidgetManager, appWidgetIds);
    }

    @Override
    public void onAppWidgetOptionsChanged(Context context, AppWidgetManager appWidgetManager, int appWidgetId, Bundle newOptions) {
        Log.i(TAG, "onAppWidgetOptionsChanged: 当创建窗体小部件和窗体小部件宽高发生改变的时候调用");
        super.onAppWidgetOptionsChanged(context, appWidgetManager, appWidgetId, newOptions);
    }




    @Override
    public void onDeleted(Context context, int[] appWidgetIds) {
        super.onDeleted(context, appWidgetIds);
        Log.i(TAG, "onDeleted: 当删除窗体小部件的时候调用 ");
    }

    /**
     * 当桌面上面所有的桌面小控件都删除的时候才调用当前这个方法
     */
    @Override
    public void onDisabled(Context context) {
        super.onDisabled(context);
        Intent intent = new Intent(context, KillProcesWidgetService.class);
        context.stopService(intent);
        Log.i(TAG, "onDisabled: 当删除最后一个桌面小控件的时候才调用");
    }
}
