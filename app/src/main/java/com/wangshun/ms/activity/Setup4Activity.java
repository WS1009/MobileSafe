package com.wangshun.ms.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import com.wangshun.ms.R;

/**
 * 第4个设置向导页
 */
public class Setup4Activity extends BaseSetupActivity {


    private CheckBox cbProtect;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup4);

        cbProtect = (CheckBox) findViewById(R.id.cb_protect);
        //根据sp数据，更新CheckBox
        boolean protect = mPref.getBoolean("protect", false);
        if (protect){
            cbProtect.setChecked(true);
            cbProtect.setText("防盗保护已经开启");
        }else{
            cbProtect.setChecked(false);
            cbProtect.setText("防盗保护没有开启");
        }

        //当CheckBox发生变化时，回调此方法
        cbProtect.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (cbProtect.isChecked()) {
                    cbProtect.setText("防盗保护已经开启");
                    mPref.edit().putBoolean("protect",true).commit();
                } else {
                    cbProtect.setText("防盗保护没有开启");
                    mPref.edit().putBoolean("protect",false).commit();
                }
            }
        });
    }

    @Override
    public void showPreviousPage() {
        startActivity(new Intent(this, Setup3Activity.class));
        finish();


        //两个界面之间的切换动画
        overridePendingTransition(R.anim.tran_previous_in, R.anim.tran_previous_out);//进入动画和退出动画

    }

    @Override
    public void showNextPage() {
        startActivity(new Intent(this, LostFindActivity.class));
        finish();
        //更新sp，表示已经展示过设置向导了，下次进来就不展示了
        mPref.edit().putBoolean("configed", true).commit();

        //两个界面之间的切换动画
        overridePendingTransition(R.anim.tran_in, R.anim.tran_out);//进入动画和退出动画

    }


}
