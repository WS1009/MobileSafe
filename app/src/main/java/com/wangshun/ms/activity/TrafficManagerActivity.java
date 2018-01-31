package com.wangshun.ms.activity;

import android.app.Activity;
import android.os.Bundle;

import com.wangshun.ms.R;

public class TrafficManagerActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);



//		//获取到手机的下载的流量
//		long mobileRxBytes = TrafficStats.getMobileRxBytes();
//		//获取到手机上传的流量
//		long mobileTxBytes = TrafficStats.getMobileTxBytes();


		setContentView(R.layout.activity_traffic);

		
	}
	
	
}
