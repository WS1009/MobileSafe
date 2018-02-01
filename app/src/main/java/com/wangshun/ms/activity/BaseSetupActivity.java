package com.wangshun.ms.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import com.wangshun.ms.R;

/**
 * 设置引导页的基类
 * 不需要在清单文件中注册，因为不需要界面展示
 * author：WS
 * version：1.0
 * Created  on 2016/4/13 at 22:52.
 */

public abstract class BaseSetupActivity extends Activity {
    private GestureDetector mDetector;
    public SharedPreferences mPref;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mPref = getSharedPreferences("config", MODE_PRIVATE);
        //手势识别器
        mDetector = new GestureDetector(this,new GestureDetector.SimpleOnGestureListener(){

            /**
             *监听手势划动事件
             * @param e1表示滑动的起点
             * @param e2表示滑动的终点
             * @param velocityX表示滑动的水平速度
             * @param velocityY表示滑动的垂直速度
             * @return
             */
            @Override
            public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
                //向右滑，上一页
                if ((e2.getRawX()-e1.getRawX())>100){
                    showPreviousPage();
                    return true;
                }
                //向左滑，下一页
                if ((e1.getRawX()-e2.getRawX())>100){
                    showNextPage();
                    return true;
                }
                //判断纵向滑动是否过大，过大的话不允许切换界面
                if (Math.abs(e1.getRawY()-e2.getRawY())>100){
                    Toast.makeText(BaseSetupActivity.this,"不能这样滑奥！",Toast.LENGTH_SHORT).show();
                    return true;
                }
                //判断滑动是否过慢
                if (Math.abs(velocityX)<100){
                    Toast.makeText(BaseSetupActivity.this,"滑动速度过慢！",Toast.LENGTH_SHORT).show();
                    return true;
                }





                return super.onFling(e1, e2, velocityX, velocityY);
            }
        });

    }


    /**
     * 展示上一页,子类必须实现
     */
    public abstract void showPreviousPage();
    /**
     * 展示下一页，子类必须实现
     */
    public abstract void showNextPage();

    //点击下一页按钮
    public void next(View view) {
        showNextPage();
    }

    //点击上一页按钮
    public void previous(View view) {
        showPreviousPage();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        //委托手势识别器处理划动事件
        mDetector.onTouchEvent(event);
        return super.onTouchEvent(event);

    }
}
