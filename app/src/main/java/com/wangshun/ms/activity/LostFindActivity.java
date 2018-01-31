package com.wangshun.ms.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.wangshun.ms.R;

public class LostFindActivity extends Activity {

    private TextView tvSafePhone;
    private ImageView ivProtect;

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
