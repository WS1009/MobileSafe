package com.wangshun.ms.activity;

import android.app.Activity;
import android.app.admin.DevicePolicyManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.wangshun.ms.R;
import com.wangshun.ms.receiver.AdminReceiver;

/**
 * 第3个设置向导页
 * 设置安全号码
 */
public class Setup3Activity extends BaseSetupActivity {

    private EditText etPhone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup3);
        etPhone = (EditText) findViewById(R.id.et_phone);
        //不用null，不做空指针判断
        String safe_phone = mPref.getString("safe_phone", "");
        etPhone.setText(safe_phone);
    }

    @Override
    public void showPreviousPage() {

        startActivity(new Intent(this, Setup2Activity.class));
        finish();
        //两个界面之间的切换动画
        overridePendingTransition(R.anim.tran_previous_in, R.anim.tran_previous_out);//进入动画和退出动画
    }

    @Override
    public void showNextPage() {
        //过滤空格
        String phone = etPhone.getText().toString().trim();
        if (TextUtils.isEmpty(phone)) {
            Toast.makeText(this, "安全号码不能为空！", Toast.LENGTH_SHORT).show();
            return;
        }
        //保存安全号码
        mPref.edit().putString("safe_phone", phone).commit();
        startActivity(new Intent(this, Setup4Activity.class));
        finish();
        //两个界面之间的切换动画
        overridePendingTransition(R.anim.tran_in, R.anim.tran_out);//进入动画和退出动画
    }

    /**
     * 选择联系人
     *
     * @param view
     */
    public void selectContact(View view) {
        Intent intent = new Intent(this, ContactActivity.class);
        startActivityForResult(intent, 0);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (resultCode == Activity.RESULT_OK) {

            String phone = data.getStringExtra("phone");
            //替换-和空格
            phone = phone.replaceAll("-", "").replaceAll(" ", "");
            //把电话号码设置给输入框
            etPhone.setText(phone);
        }

        super.onActivityResult(requestCode, resultCode, data);


    }


}
