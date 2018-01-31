package com.wangshun.ms.utils;

import android.app.ActivityManager;
import android.content.Context;

import java.util.List;

/**
 * 服务状态的工具类
 * author：WS
 * version：1.0
 * Created  on 2016/4/24 at 21:43.
 */
public class ServiceStatusUtils {
    /**
     * 检测服务是否运行
     * @return
     */
    public static boolean isServiceRunning(Context context,String serviceName) {

        ActivityManager am= (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        //获取系统所有正在运行的服务，最多返回100个
        List<ActivityManager.RunningServiceInfo> runningServices = am.getRunningServices(100);
        for (ActivityManager.RunningServiceInfo runningServiceInfo:runningServices ) {
            //获取服务的名称
            String className = runningServiceInfo.service.getClassName();
            System.out.println(className);

            if (className.equals(serviceName)){
                //服务存在
                return true;
            }

        }

        return false;
    }


}
