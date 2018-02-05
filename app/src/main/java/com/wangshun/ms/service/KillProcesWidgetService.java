package com.wangshun.ms.service;

import android.app.PendingIntent;
import android.app.Service;
import android.appwidget.AppWidgetManager;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
import android.text.format.Formatter;
import android.util.Log;
import android.widget.RemoteViews;

import com.wangshun.ms.R;
import com.wangshun.ms.receiver.MyAppWidget;
import com.wangshun.ms.utils.SystemInfoUtils;

import java.util.Timer;
import java.util.TimerTask;

/**
 *      清理桌面小控件的服务
 **/
public class KillProcesWidgetService extends Service {

	private AppWidgetManager widgetManager;
	private Timer timer;
	private TimerTask timerTask;
	private TimerTaskReceiver mTimerTaskReceiver;

	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		//管理进程总数和可用内存更新
		startTimer();

		//注册开锁、解锁广播接收者
		IntentFilter intentFilter = new IntentFilter();
		//开锁的action
		intentFilter.addAction(Intent.ACTION_SCREEN_ON);
		//解锁的action
		intentFilter.addAction(Intent.ACTION_SCREEN_OFF);

		mTimerTaskReceiver = new TimerTaskReceiver();
		registerReceiver(mTimerTaskReceiver,intentFilter);
	}

	private class TimerTaskReceiver extends BroadcastReceiver{

		@Override
		public void onReceive(Context context, Intent intent) {
			String action = intent.getAction();
			if (action.equals(Intent.ACTION_SCREEN_ON)){
				//开启定时更新任务
				startTimer();
			}else{
				if (timer!=null) {
					//关闭定时更新任务
					timer.cancel();
					timer=null;
				}
			}
		}
	}

	private void startTimer() {
		//桌面小控件的管理者
		widgetManager = AppWidgetManager.getInstance(this);

		//每隔5秒钟更新一次桌面
		//初始化定时器
		timer = new Timer();

		//初始化一个定时任务
		timerTask = new TimerTask() {

			@Override
			public void run() {

				Log.i("TimerTask", "run: 定时任务");

				//这个是把当前的布局文件添加进行
				/**
				 * 初始化一个远程的view
				 * Remote 远程
				 */
				RemoteViews remoteViews = new RemoteViews(getPackageName(), R.layout.process_widget);

				/**
				 * 需要注意。这个里面findedViewByid这个方法
				 * 设置当前文本里面一共有多少个进程
				 */
				int processCount = SystemInfoUtils.getProcessCount(getApplicationContext());

				//设置文本
				remoteViews.setTextViewText(R.id.process_count,"正在运行的软件:" + String.valueOf(processCount));

				//获取到当前手机上面的可用内存
				long availMem = SystemInfoUtils.getAvailMem(getApplicationContext());

				remoteViews.setTextViewText(R.id.process_memory, "可用内存:" +Formatter.formatFileSize(getApplicationContext(), availMem));


				//点击窗体小部件进入HomeActivity
				Intent entryHomeIntent=new Intent("aaa.bbb.ccc");
				entryHomeIntent.addCategory("android.intent.category.DEFAULT");
				//FLAG_CANCEL_CURRENT：点击窗体小部件之后下拉栏的通知消失
				PendingIntent pendingIntent=PendingIntent.getActivity(getApplicationContext(),0,entryHomeIntent,PendingIntent.FLAG_CANCEL_CURRENT);
				remoteViews.setOnClickPendingIntent(R.id.ll_root,pendingIntent);


				//清理所有进程
				Intent killProcessIntent = new Intent();
				//发送一个隐式意图
				killProcessIntent.setAction("com.wangshun.ms.killAllProcess");
				//通过延期的意图发送一个广播，希望系统里有地方接受这个广播，进行相应的处理
				PendingIntent killProcessPendingIntent = PendingIntent.getBroadcast(getApplicationContext(), 0, killProcessIntent, 0);
				//设置点击事件
				remoteViews.setOnClickPendingIntent(R.id.btn_clear, killProcessPendingIntent);


				//第一个参数表示上下文
				//第二个参数表示当前有哪一个广播进行去处理当前的桌面小控件
				ComponentName provider = new ComponentName(getApplicationContext(), MyAppWidget.class);


				//更新桌面
				widgetManager.updateAppWidget(provider, remoteViews);

			}
		};
		//从0开始。每隔5秒钟更新一次
		timer.schedule(timerTask, 0, 5000);
	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		//优化代码
		if(timer != null || timerTask != null){
			timer.cancel();
			timerTask.cancel();
			timer = null;
			timerTask = null;
		}
		if (mTimerTaskReceiver!=null){
			unregisterReceiver(mTimerTaskReceiver);
			mTimerTaskReceiver=null;
		}

	}


}
