package com.wangshun.ms.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.wangshun.ms.R;
import com.wangshun.ms.service.AddressService;
import com.wangshun.ms.service.CallSafeService;
import com.wangshun.ms.service.WatchDogService;
import com.wangshun.ms.utils.ServiceStatusUtils;
import com.wangshun.ms.view.SettingClickView;
import com.wangshun.ms.view.SettingItemView;

/**
 * 设置中心
 */
public class SettingActivity extends Activity {

    private SettingItemView sivUpdate;
    private SharedPreferences mPref;
    private SettingItemView sivAddress;
    private SettingClickView scvAddressStyle;//修改提示框的显示风格
    private SettingClickView scvAddressLocation;//修改提示框的显示位置
    private SettingItemView sivCallSafe;//黑名单

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        mPref = getSharedPreferences("config", MODE_PRIVATE);

        initUpdateView();
        initAddressView();
        initAddressStyle();
        initAddressLocation();
        initBlackView();
        initAppLock();
    }

    /**
     * 初始化程序锁方法
     */
    private void initAppLock() {
        final SettingItemView siv_app_lock = (SettingItemView) findViewById(R.id.siv_app_lock);
        boolean isRunning = ServiceStatusUtils.isServiceRunning(this, "com.wangshun.ms.service.WatchDogService");
        siv_app_lock.setChecked(isRunning);

        siv_app_lock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean isCheck = siv_app_lock.isChecked();
                siv_app_lock.setChecked(!isCheck);
                if(!isCheck){
                    //开启服务
                    startService(new Intent(getApplicationContext(), WatchDogService.class));
                }else{
                    //关闭服务
                    stopService(new Intent(getApplicationContext(), WatchDogService.class));
                }
            }
        });
    }


    /**
     * 初始化黑名单
     */
    private void initBlackView() {
        sivCallSafe = (SettingItemView) findViewById(R.id.siv_callsafe);
        boolean serviceRunning = ServiceStatusUtils.isServiceRunning(this, "com.wangshun.ms.service.CallSafeService");
        //根据黑名单服务是否开启更新CheckBox
        if (serviceRunning) {
            sivCallSafe.setChecked(true);
        } else {
            sivCallSafe.setChecked(false);
        }

        sivCallSafe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (sivCallSafe.isChecked()) {
                    sivCallSafe.setChecked(false);
                    //停止黑名单服务
                    stopService(new Intent(SettingActivity.this, CallSafeService.class));
                } else {
                    sivCallSafe.setChecked(true);
                    //开启黑名单服务
                    startService(new Intent(SettingActivity.this, CallSafeService.class));
                }

            }
        });

    }


    /**
     * 初始化自动更新的开关
     */
    private void initUpdateView() {
        sivUpdate = (SettingItemView) findViewById(R.id.siv_update);
//        sivUpdate.setTitle("自动更新设置");

        boolean autoUpdate = mPref.getBoolean("auto_update", true);
        if (autoUpdate) {
//            sivUpdate.setDesc("自动更新已开启");
            sivUpdate.setChecked(true);
        } else {
//            sivUpdate.setDesc("自动更新已关闭");
            sivUpdate.setChecked(false);
        }


        sivUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //判断当前的勾选状态
                if (sivUpdate.isChecked()) {
                    //设置不勾选
                    sivUpdate.setChecked(false);
//                    sivUpdate.setDesc("自动更新已关闭");
                    //更新sp
                    mPref.edit().putBoolean("auto_update", false).commit();
                } else {
                    //设置勾选
                    sivUpdate.setChecked(true);
//                    sivUpdate.setDesc("自动更新已开启");
                    //更新sp
                    mPref.edit().putBoolean("auto_update", true).commit();
                }
            }
        });
    }


    /**
     * 初始化归属地显示
     */
    private void initAddressView() {
        sivAddress = (SettingItemView) findViewById(R.id.siv_address);
        boolean serviceRunning = ServiceStatusUtils.isServiceRunning(this, "com.wangshun.ms.service.AddressService");
        //根据归属地服务是否开启更新CheckBox
        if (serviceRunning) {
            sivAddress.setChecked(true);
        } else {
            sivAddress.setChecked(false);
        }

        sivAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (sivAddress.isChecked()) {
                    sivAddress.setChecked(false);
                    //停止归属地服务
                    stopService(new Intent(SettingActivity.this, AddressService.class));
                } else {
                    sivAddress.setChecked(true);
                    //开启归属地服务
                    startService(new Intent(SettingActivity.this, AddressService.class));
                }

            }
        });
    }


    final String[] items = new String[] { "半透明", "活力橙", "卫士蓝", "金属灰", "苹果绿" };


    /**
     * 修改提示框的显示风格
     */
    private void initAddressStyle() {
        scvAddressStyle = (SettingClickView) findViewById(R.id.scv_address_style);

        scvAddressStyle.setTitle("归属地提示框风格");

        int style = mPref.getInt("address_style", 0);// 读取保存的style
        scvAddressStyle.setDesc(items[style]);

        scvAddressStyle.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                showSingleChooseDialog();
            }
        });

    }


    /**
     * 弹出风格的单选框
     */
    private void showSingleChooseDialog() {
        AlertDialog.Builder builder=new AlertDialog.Builder(this);
        builder.setIcon(R.mipmap.ic_launcher);
        builder.setTitle("归属地提示框风格");

        int style = mPref.getInt("address_style", 0);// 读取保存的style

        builder.setSingleChoiceItems(items, style, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mPref.edit().putInt("address_style", which).commit();// 保存选择的风格
                        dialog.dismiss();// 让dialog消失

                        scvAddressStyle.setDesc(items[which]);// 更新组合控件的描述信息
                    }
                });

        builder.setNegativeButton("取消", null);
        builder.show();

    }


    /**
     * 修改归属地显示位置
     */
    private void initAddressLocation() {
        scvAddressLocation = (SettingClickView) findViewById(R.id.scv_address_location);
        scvAddressLocation.setTitle("归属地提示框显示位置");
        scvAddressLocation.setDesc("设置归属地提示框的显示位置");

        scvAddressLocation.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                startActivity(new Intent(SettingActivity.this,
                        DragViewActivity.class));
            }
        });
    }
}
