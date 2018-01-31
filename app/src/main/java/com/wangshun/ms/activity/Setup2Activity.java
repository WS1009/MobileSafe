package com.wangshun.ms.activity;

import android.content.Intent;
import android.os.Bundle;

import android.telephony.TelephonyManager;
import android.text.TextUtils;

import android.view.View;

import com.wangshun.ms.R;
import com.wangshun.ms.utils.ToastUtils;
import com.wangshun.ms.view.SettingItemView;

/**
 * 第二个设置向导页
 * 手机卡绑定
 */
public class Setup2Activity extends BaseSetupActivity {


    private SettingItemView sivSim;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup2);

        sivSim = (SettingItemView) findViewById(R.id.siv_sim);
        String sim = mPref.getString("sim", null);

        if (!TextUtils.isEmpty(sim)){
            sivSim.setChecked(true);
        }else{
            sivSim.setChecked(false);
        }

        sivSim.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (sivSim.isChecked()){
                    sivSim.setChecked(false);
                    //删除已经绑定的sim卡
                    mPref.edit().remove("sim").commit();
                }else{
                    sivSim.setChecked(true);
                    //保存sim卡的信息
                    TelephonyManager tm = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
                    //获取Sim卡的序列号
                    String simSerialNumber = tm.getSimSerialNumber();
                    System.out.println("simSerialNumber"+simSerialNumber);
                    //将Sim卡序列号保存在sp中
                    mPref.edit().putString("sim",simSerialNumber).commit();
                }
            }
        });
    }

    /**
     * 展示上一页
     */
    @Override
    public void showPreviousPage() {

        startActivity(new Intent(this, Setup1Activity.class));
        finish();
        //两个界面之间的切换动画
        overridePendingTransition(R.anim.tran_previous_in, R.anim.tran_previous_out);//进入动画和退出动画
    }

    /**
     * 展示下一页
     */
    @Override
    public void showNextPage() {
        //如果sim卡没有绑定，就不允许进入下一个页面
        String sim = mPref.getString("sim", null);

        if (!TextUtils.isEmpty(sim)){
            ToastUtils.showToast(this,"必须绑定sim卡");
        }

        startActivity(new Intent(this, Setup3Activity.class));
        finish();
        //两个界面之间的切换动画
        overridePendingTransition(R.anim.tran_in, R.anim.tran_out);//进入动画和退出动画

    }


}
