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
public class SettingItemView extends LinearLayout {

    private TextView tvTitle;
    private TextView tvDesc;
    private CheckBox cbStatus;
    private String NAMESPACE = "http://schemas.android.com/apk/res/com.wangshun.ms";
    private String mTitle;
    private String mDescOn;
    private String mDescOff;

    public SettingItemView(Context context) {
        super(context);
        initView();
    }

    public SettingItemView(Context context, AttributeSet attrs) {
        super(context, attrs);


//        int attributeCount = attrs.getAttributeCount();
//        for (int i=0;i<attributeCount;i++){
//            String attributeName = attrs.getAttributeName(i);
//            String attributeValue = attrs.getAttributeValue(i);
//            System.out.println("attributeName:"+attributeName+"********"+"attributeValue"+attributeValue);
//        }

        //根据属性名称获取属性的值
        mTitle = attrs.getAttributeValue(NAMESPACE, "itemTitle");
        mDescOn = attrs.getAttributeValue(NAMESPACE, "desc_on");
        mDescOff = attrs.getAttributeValue(NAMESPACE, "desc_off");

        initView();
    }

    public SettingItemView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initView();
    }

    /**
     * 初始化布局
     */
    private void initView() {
        //将自定义的布局文件设置给当前的SettingItemView
        View view = View.inflate(getContext(), R.layout.view_setting_item, this);
        tvTitle = (TextView) view.findViewById(R.id.tv_title);
        tvDesc = (TextView) view.findViewById(R.id.tv_desc);
        cbStatus = (CheckBox) view.findViewById(R.id.cb_status);

        setTitle(mTitle);//设置标题

    }

    public void setTitle(String title) {
        tvTitle.setText(title);
    }

    public void setDesc(String desc) {
        tvDesc.setText(desc);
    }

    /**
     * 返回勾选状态
     *
     * @return
     */
    public boolean isChecked() {
        return cbStatus.isChecked();
    }

    public void setChecked(boolean check) {
        cbStatus.setChecked(check);
        //根据选择的状态，更新文本描述
        if (check) {
            setDesc(mDescOn);
        } else {
            setDesc(mDescOff);
        }
    }
}
