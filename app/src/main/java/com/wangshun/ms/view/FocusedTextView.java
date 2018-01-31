package com.wangshun.ms.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * 获取焦点的textview
 * author：WS
 * version：1.0
 * Created  on 2016/4/11 at 20:51.
 */
public class FocusedTextView extends TextView {

    //有属性的话会走此方法
    public FocusedTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }
    //有style样式的话会走此方法
    public FocusedTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }
    //用代码new的时候走此方法
    public FocusedTextView(Context context) {
        super(context);
    }

    /**
     * 表示有没有获取焦点
     * 跑马灯要运行，首先要调用此函数，判断是否有焦点
     * 所以强制获取焦点
     * @return
     */
    @Override
    public boolean isFocused() {
        return true;
    }
}
