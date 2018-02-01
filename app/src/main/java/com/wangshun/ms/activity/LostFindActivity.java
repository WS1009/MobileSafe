package com.wangshun.ms.activity;

import android.app.Activity;
import android.app.admin.DevicePolicyManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.wangshun.ms.R;
import com.wangshun.ms.receiver.AdminReceiver;

public class LostFindActivity extends Activity {

    private TextView tvSafePhone;
    private ImageView ivProtect;

    private DevicePolicyManager mDPM;
    private ComponentName mDeviceAdminSample;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SharedPreferences mPref = getSharedPreferences("config", MODE_PRIVATE);
        //判断是否进入到设置向导
        boolean configed = mPref.getBoolean("configed", false);
        if (configed) {
            setContentView(R.layout.activity_lost_find);

            tvSafePhone = (TextView) findViewById(R.id.tv_safe_phone);
            //根据sp更新安全号码
            String safe_phone = mPref.getString("safe_phone", "");
            tvSafePhone.setText(safe_phone);
            ivProtect = (ImageView) findViewById(R.id.iv_protect);
            //根据sp更新保护锁
            boolean protect = mPref.getBoolean("protect", false);
            if (protect) {
                ivProtect.setImageResource(R.mipmap.lock);
            } else {
                ivProtect.setImageResource(R.mipmap.unlock);
            }


            //打开设备管理器，为了获取锁屏、设置密码的权限
            mDPM = (DevicePolicyManager) getSystemService(Context.DEVICE_POLICY_SERVICE);// 获取设备策略服务
            mDeviceAdminSample = new ComponentName(this, AdminReceiver.class);// 设备管理组件

//            Intent intent = new Intent(DevicePolicyManager.ACTION_ADD_DEVICE_ADMIN);
//            intent.putExtra(DevicePolicyManager.EXTRA_DEVICE_ADMIN, mDeviceAdminSample);
//            intent.putExtra(DevicePolicyManager.EXTRA_ADD_EXPLANATION, "设备管理器已经打开");
//            startActivity(intent);

        } else {
            //跳转到设置向导页
            startActivity(new Intent(LostFindActivity.this, Setup1Activity.class));
            finish();
        }
    }


    public void reEnter(View view) {
        startActivity(new Intent(this, Setup1Activity.class));
        finish();
    }
}
