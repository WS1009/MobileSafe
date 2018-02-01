package com.wangshun.ms.receiver;

import android.app.admin.DeviceAdminReceiver;
import android.app.admin.DevicePolicyManager;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

/**
 * 设备管理器
 */
public class AdminReceiver extends DeviceAdminReceiver {
    private DevicePolicyManager mDPM;
    private ComponentName mDeviceAdminSample;

    public AdminReceiver() {

    }

    @Override
    public void onReceive(Context context, Intent intent) {
        mDPM = (DevicePolicyManager) context.getSystemService(Context.DEVICE_POLICY_SERVICE);// 获取设备策略服务
        mDeviceAdminSample = new ComponentName(context, AdminReceiver.class);// 设备管理组件

        if (mDPM.isAdminActive(mDeviceAdminSample)) {// 判断设备管理器是否已经激活
            mDPM.lockNow();// 立即锁屏
            //锁屏同时去重新设置密码，防止别人直接用密码打开手机
            //0标识没有进入应用就设置密码
            mDPM.resetPassword("", 0);
        } else {
            Toast.makeText(context, "必须先激活设备管理器!", Toast.LENGTH_SHORT).show();
        }

    }
}
