package com.wangshun.ms.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import com.wangshun.ms.db.dao.AddressDao;

/**
 * 监听去电的广播接收者
 */
public class OutCallReceiver extends BroadcastReceiver {
    public OutCallReceiver() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        //获取去电的电话号码
        String number = getResultData();
        String address = AddressDao.getAddress(number);
        Toast.makeText(context,address,Toast.LENGTH_SHORT).show();
    }


}
