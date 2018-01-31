package com.wangshun.ms.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;

import com.wangshun.ms.R;
import com.wangshun.ms.service.KillProcessService;
import com.wangshun.ms.utils.SharedPreferencesUtils;
import com.wangshun.ms.utils.SystemInfoUtils;

/**
 *任务管理器的设置界面
 **/
public class TaskManagerSettingActivity extends Activity {

	private SharedPreferences sp;
	private CheckBox cb_status_kill_process;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		initUI();

	}

	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
		if(SystemInfoUtils.isServiceRunning(TaskManagerSettingActivity.this, "com.itheima.mobileguard.services.KillProcessService")){
			cb_status_kill_process.setChecked(true);
		}else{
			cb_status_kill_process.setChecked(false);
		}
	}

	private void initUI() {
		setContentView(R.layout.activity_task_manager_setting);
		CheckBox cb_status = (CheckBox) findViewById(R.id.cb_status);

		//设置是否选中
		cb_status.setChecked(SharedPreferencesUtils.getBoolean(TaskManagerSettingActivity.this, "is_show_system", false));

		cb_status.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				SharedPreferencesUtils.saveBoolean(TaskManagerSettingActivity.this, "is_show_system", isChecked);
			}
		});

		//定时清理进程

		cb_status_kill_process = (CheckBox) findViewById(R.id.cb_status_kill_process);

		final Intent intent = new Intent(this,KillProcessService.class);

		cb_status_kill_process.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

				if(isChecked){
					startService(intent);
				}else{
					stopService(intent);
				}
			}
		});

	}


}
