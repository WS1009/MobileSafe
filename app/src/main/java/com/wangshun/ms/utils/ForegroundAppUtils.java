package com.wangshun.ms.utils;

import android.app.ActivityManager;
import android.app.usage.UsageStats;
import android.app.usage.UsageStatsManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Build;
import android.provider.Settings;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by ThinkPad on 2018-02-04.
 */

public class ForegroundAppUtils {
    //判断当前是不是桌面
    public static boolean isHome(Context context) {
        String topPackageName = getTopActivityName(context);
        //此处做了判断如果得到的是null的话就返回一个true
        return topPackageName == null ? true : getHomesPackageName(context).contains(topPackageName);
    }

    //得到桌面的包名，包括第三方桌面
    public static List<String> getHomesPackageName(Context context) {
        List<String> homePackageNames = new ArrayList<>();
        PackageManager packageManager = context.getPackageManager();
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        List<ResolveInfo> resolveInfo = packageManager.queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY);
        for (ResolveInfo info : resolveInfo) {
            homePackageNames.add(info.activityInfo.packageName);
        }
        return homePackageNames;
    }

    //得到栈顶Activity的名字，注意此处要进行判断，
    // Android在5.0以后Google把getRunningTasks的方法给屏蔽掉了，所以要分开处理
    public static String getTopActivityName(Context context) {
        String topActivityPackageName;
        ActivityManager manager = (ActivityManager) context.getSystemService(context.ACTIVITY_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            //此处要判断用户的安全权限有没有打开，如果打开了就进行获取栈顶Activity的名字的方法
            //当然，我们的要求是如果没打开就不获取了，要不然跳转会影响用户的体验
            if (isSecurityPermissionOpen(context)) {
                UsageStatsManager mUsageStatsManager = (UsageStatsManager) context.getSystemService(Context.USAGE_STATS_SERVICE);
                long endTime = System.currentTimeMillis();
                long beginTime = endTime - 1000 * 60 * 2;
                UsageStats recentStats = null;

                List<UsageStats> queryUsageStats = mUsageStatsManager.queryUsageStats(UsageStatsManager.INTERVAL_BEST, beginTime, endTime);
                if (queryUsageStats == null || queryUsageStats.isEmpty()) {
                    return null;
                }

                for (UsageStats usageStats : queryUsageStats) {
                    if (recentStats == null || recentStats.getLastTimeUsed() < usageStats.getLastTimeUsed()) {
                        recentStats = usageStats;
                    }
                }
                topActivityPackageName = recentStats.getPackageName();
                return topActivityPackageName;
            } else {
                return null;
            }
        } else {
            List<ActivityManager.RunningTaskInfo> taskInfos = manager.getRunningTasks(1);
            if (taskInfos.size() > 0)
                topActivityPackageName = taskInfos.get(0).topActivity.getPackageName();
            else
                return null;
            return topActivityPackageName;
        }
    }

    //判断用户对应的安全权限有没有打开
    public static boolean isSecurityPermissionOpen(Context context) {
        long endTime = System.currentTimeMillis();
        UsageStatsManager usageStatsManager = (UsageStatsManager) context.getApplicationContext().getSystemService("usagestats");
        List<UsageStats> queryUsageStats = usageStatsManager.queryUsageStats(UsageStatsManager.INTERVAL_BEST, 0, endTime);
        if (queryUsageStats == null || queryUsageStats.isEmpty()) {
            return false;
        }
        return true;
    }


    public static String getCurrentPkgName(Context context) {

        ActivityManager.RunningAppProcessInfo currentInfo = null;

        Field field = null;

        int START_TASK_TO_FRONT = 2;

        String pkgName = "CurrentNULL";

        try {

            field = ActivityManager.RunningAppProcessInfo.class.getDeclaredField("processState");

        } catch (Exception e) {

            e.printStackTrace();

        }

        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);

        List<ActivityManager.RunningAppProcessInfo> appList = am.getRunningAppProcesses();

        for (ActivityManager.RunningAppProcessInfo app : appList) {

            if (app.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND) {

                Integer state = null;

                try {

                    state = field.getInt(app);

                } catch (Exception e) {

                    e.printStackTrace();

                }

                if (state != null && state == START_TASK_TO_FRONT) {

                    currentInfo = app;

                    break;

                }

            }

        }

        if (currentInfo != null) {

            pkgName = currentInfo.processName;

        }

        return pkgName;

    }


}
