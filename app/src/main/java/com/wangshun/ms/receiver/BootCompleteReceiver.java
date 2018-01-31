package com.wangshun.ms.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.telephony.TelephonyManager;
import android.text.TextUtils;

import java.lang.reflect.MalformedParameterizedTypeException;

/**
 * 监听手机启动的广播
 */
public class BootCompleteReceiver extends BroadcastReceiver {
//    public BootCompleteReceiver() {
//    }

    @Override
    public void onReceive(Context context, Intent intent) {

        SharedPreferences sp = context.getSharedPreferences("config", Context.MODE_PRIVATE);

        boolean protect = sp.getBoolean("protect", false);
        //只有在防盗保护开启的前提下，才进行Sim卡判断
        if (protect) {

            //获取绑定的sim卡
            String sim = sp.getString("sim", null);
            if (!TextUtils.isEmpty(sim)) {
                //获取当前手机的sim卡
                TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
                //拿到当前手机的sim卡
                String currentSim = tm.getSimSerialNumber();
                if (sim.equals(currentSim)) {
                    System.out.println("手机安全");
                } else {
                    System.out.println("手机sim卡已经发生变化，发送报警短信！！");

                }

            }
        }


    }


}
