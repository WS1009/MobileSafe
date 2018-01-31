package com.wangshun.ms.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.wangshun.ms.R;

/**
 * 设置中心的自定义控件
 * author：WS
 * version：1.0
 * Created  on 2016/4/11 at 21:42.
 */
public class SettingClickView extends LinearLayout {

    private TextView tvTitle;
    private TextView tvDesc;


    public SettingClickView(Context context) {
        super(context);
        initView();
    }

    public SettingClickView(Context context, AttributeSet attrs) {
        super(context, attrs);


        initView();
    }

    public SettingClickView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initView();
    }

    /**
     * 初始化布局
     */
    private void initView() {
        //将自定义的布局文件设置给当前的SettingClickView
        View view = View.inflate(getContext(), R.layout.view_setting_click, this);
        tvTitle = (TextView) view.findViewById(R.id.tv_title);
        tvDesc = (TextView) view.findViewById(R.id.tv_desc);


    }

    public void setTitle(String title) {
        tvTitle.setText(title);
    }

    public void setDesc(String desc) {
        tvDesc.setText(desc);
    }


}
