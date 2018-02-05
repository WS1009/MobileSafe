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
    //命名空间与布局文件中的保持一致
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
        //将自定义的布局文件设置给当前的SettingItemView，
        // root为是否将此控件挂载在父控件上，所以为this
        //也就是要将R.layout.view_setting_item中的控件放到SettingItemView中,或者代码如下也行
//        View view = View.inflate(getContext(), R.layout.view_setting_item, null);
//        this.addView(view);

        View settingItemView = View.inflate(getContext(), R.layout.view_setting_item, this);
        //以下三种写法都对
        tvTitle = (TextView) settingItemView.findViewById(R.id.tv_title);
        tvDesc = (TextView) this.findViewById(R.id.tv_desc);
        cbStatus = (CheckBox) findViewById(R.id.cb_status);

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
