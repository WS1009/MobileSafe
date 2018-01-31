package com.wangshun.ms.activity;


import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;
import com.wangshun.ms.R;
import com.wangshun.ms.bean.Virus;
import com.wangshun.ms.db.dao.AntivirusDao;
import com.wangshun.ms.utils.StreamUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;


public class SplashActivity extends Activity {


    private static final int CODE_UPDATE_DIALOG = 0;
    private static final int CODE_URL_ERROR = 1;
    private static final int CODE_NET_ERROR = 2;
    private static final int CODE_JSON_ERROR = 3;
    private static final int CODE_ENTER_HOME = 4;
    private TextView tvVersion;
    private TextView tvProgress;//下载进度
    private String mVersionName;//版本名,服务器返回的信息
    private int mVersionCode;//版本号
    private String mDesc;//版本描述
    private String mDownloadUrl;//下载地址
    private AntivirusDao dao;

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {

            switch (msg.what) {
                case CODE_UPDATE_DIALOG:
                    showUpdateDialog();
                    break;
                case CODE_URL_ERROR:
                    Toast.makeText(SplashActivity.this, "URL错误", Toast.LENGTH_SHORT).show();
                    enterHome();
                    break;
                case CODE_NET_ERROR:
                    Toast.makeText(SplashActivity.this, "NET错误", Toast.LENGTH_SHORT).show();
                    enterHome();
                    break;
                case CODE_JSON_ERROR:
                    Toast.makeText(SplashActivity.this, "JSON解析错误", Toast.LENGTH_SHORT).show();
                    enterHome();
                    break;
                case CODE_ENTER_HOME:
                    enterHome();
                    break;
                default:
                    break;

            }

        }
    };
    private SharedPreferences mPref;
    private RelativeLayout rlRoot;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        tvVersion = (TextView) findViewById(R.id.tv_version);
        rlRoot = (RelativeLayout) findViewById(R.id.rl_root);

        tvVersion.setText("版本号：" + getVersionName());


        tvProgress = (TextView) findViewById(R.id.tv_progress);//默认隐藏

        mPref = getSharedPreferences("config", MODE_PRIVATE);

        //拷贝资产目录下的数据库文件
        copyDB("address.db");

        //拷贝资产目录下的病毒数据库文件
        copyDB("antivirus.db");

        //创建快捷方式
        createShortcut();
        //更新病毒数据库
        updataVirus();

        //判断是否需要自动更新
        boolean autoUpdate = mPref.getBoolean("auto_update", true);
        if (autoUpdate) {
            checkVersion();
        } else {
            //延时两秒之后发送消息
            mHandler.sendEmptyMessageDelayed(CODE_ENTER_HOME, 2000);
        }

        //渐变的动画效果
        AlphaAnimation anim = new AlphaAnimation(0.3f, 1f);
        anim.setDuration(2000);
        rlRoot.startAnimation(anim);

    }

    /**
     * 获取版本名
     *
     * @return
     */
    private String getVersionName() {
        PackageManager packageManager = getPackageManager();
        try {
            //获取包的内容
            PackageInfo packageInfo = packageManager.getPackageInfo(getPackageName(), 0);
//            int versionCode = packageInfo.versionCode;

            String versionName = packageInfo.versionName;

//            System.out.println("versionCode" + versionCode + "versionName" + versionName);
            return versionName;

        } catch (PackageManager.NameNotFoundException e) {
            //没有找到包名是走此异常
            e.printStackTrace();
        }


        return "";
    }

    /**
     * 获取版本号
     *
     * @return
     */
    private int getVersionCode() {
        PackageManager packageManager = getPackageManager();
        try {
            //获取包的内容
            PackageInfo packageInfo = packageManager.getPackageInfo(getPackageName(), 0);

            int versionCode = packageInfo.versionCode;

//            String versionName = packageInfo.versionName;

//            System.out.println("versionCode" + versionCode + "versionName" + versionName);
            return versionCode;

        } catch (PackageManager.NameNotFoundException e) {
            //没有找到包名是走此异常
            e.printStackTrace();
        }


        return -1;
    }

    /**
     * 从服务器获取版本信息进行校验
     */
    private void checkVersion() {

        final long startTime = System.currentTimeMillis();

        //启动子线程异步加载数据
        new Thread() {
            @Override
            public void run() {

                Message msg = Message.obtain();
                HttpURLConnection conn = null;

                try {
                    //本机地址用localhost,模拟器用http://10.0.2.2:8080
                    URL url = new URL("http://10.0.2.2:8080/update.json");
                    conn = (HttpURLConnection) url.openConnection();
                    conn.setRequestMethod("GET");//设置请求方法
                    conn.setConnectTimeout(5000);//设置连接超时
                    conn.setReadTimeout(5000);//设置响应超时，连接上了但是服务器迟迟不给数据
                    conn.connect();//连接服务器
                    int responseCode = conn.getResponseCode();
                    if (responseCode == 200) {
                        InputStream inputStream = conn.getInputStream();
                        String result = StreamUtils.readFromStream(inputStream);
                        System.out.println("网络返回结果：" + result);
                        //解析json
                        JSONObject jo = new JSONObject(result);
                        mVersionName = jo.getString("versionName");
                        mVersionCode = jo.getInt("versionCode");
                        mDesc = jo.getString("description");
                        mDownloadUrl = jo.getString("downloadUrl");
                        System.out.println("-------" + mVersionName + "**" + mVersionCode + "**" + mDesc + "**" + mDownloadUrl);

                        if (mVersionCode > getVersionCode()) {//判断是否有更新
                            //服务器的version大于本地的version
                            //说明有更新，弹出升级对话框
                            msg.what = CODE_UPDATE_DIALOG;


                        } else {
                            //没有版本更新
                            msg.what = CODE_ENTER_HOME;
                        }

                    }


                } catch (MalformedURLException e) {
                    //url错误异常
                    msg.what = CODE_URL_ERROR;
                    e.printStackTrace();
                } catch (IOException e) {
                    //网络错误异常
                    msg.what = CODE_NET_ERROR;
                    e.printStackTrace();
                } catch (JSONException e) {
                    //json解析异常
                    msg.what = CODE_JSON_ERROR;
                    e.printStackTrace();
                } finally {
                    long endTime = System.currentTimeMillis();
                    long timeUsed = endTime - startTime;//访问网络花费的时间
                    if (timeUsed < 2000) {
                        //强制休眠一段时间，展示闪屏页
                        try {
                            Thread.sleep(2000 - timeUsed);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }

                    mHandler.sendMessage(msg);
                    if (conn != null) {
                        conn.disconnect();//关闭网络连接
                    }

                }

            }
        }.start();


    }

    /**
     * 升级对话框
     */
    private void showUpdateDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("最新版本：" + mVersionName);
        builder.setMessage(mDesc);
        builder.setPositiveButton("立即更新", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                System.out.println("立即更新");
                download();
            }
        });

        builder.setNegativeButton("以后再说", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                enterHome();
            }
        });

        // builder.setCancelable(false);//不让用户点击返回，用户体验不好
        //设置取消的监听，用户点击返回键时会触发
        builder.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                enterHome();
            }
        });

        builder.show();

    }

    /**
     * 下载apk文件
     */
    protected void download() {
        //MEDIA_MOUNTED：sd卡已经挂载，可用
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            tvProgress.setVisibility(View.VISIBLE);//限时进度
            String target = Environment.getExternalStorageDirectory() + "update.apk";

            //XUtils
            HttpUtils utils = new HttpUtils();
            utils.download(mDownloadUrl, target, new RequestCallBack<File>() {
                //下载文件的进度
                @Override
                public void onLoading(long total, long current, boolean isUploading) {
                    super.onLoading(total, current, isUploading);

                    System.out.println("下载文件进度：" + current + "/" + total);
                    tvProgress.setText("下载文件进度：" + current * 100 + "/" + total + "%");
                }

                //下载成功
                @Override
                public void onSuccess(ResponseInfo<File> responseInfo) {
                    System.out.println("下载成功");
                    //跳转到系统的下载界面
                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    intent.addCategory(Intent.CATEGORY_DEFAULT);
                    intent.setDataAndType(Uri.fromFile(responseInfo.result),
                            "application/vnd.android.package-archive");
                    // startActivity(intent);
                    startActivityForResult(intent, 0);// 如果用户取消安装的话,
                    // 会返回结果,回调方法onActivityResult

                }

                //下载失败
                @Override
                public void onFailure(HttpException e, String s) {
                    Toast.makeText(SplashActivity.this, "下载失败", Toast.LENGTH_SHORT).show();
                }
            });


        } else {
            Toast.makeText(SplashActivity.this, "没有找到sd卡", Toast.LENGTH_SHORT).show();
        }

    }

    /**
     * 如果用户取消安装，会调用此方法
     *
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        enterHome();
        super.onActivityResult(requestCode, resultCode, data);
    }

    /**
     * 进入主页面
     */
    private void enterHome() {
        startActivity(new Intent(SplashActivity.this, HomeActivity.class));
        finish();
    }

    /**
     * 拷贝数据库
     */
    private void copyDB(String dbName) {
        //getFilesDir():获取文件路径
        //获取要拷贝的目标地址
        File destFile = new File(getFilesDir(), dbName);

        if (destFile.exists()){
            System.out.println("数据库"+dbName+"已经存在");
            return;
        }


        FileOutputStream out=null;
        InputStream in=null;
        try {
             in = getAssets().open(dbName);
             out = new FileOutputStream(destFile);

            int len=0;
            byte[] buffer = new byte[1024];
            while ((len=in.read(buffer))!=-1){
                out.write(buffer,0,len);
            }


        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            try {
                in.close();
                out.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }

    /**
     * 进行更新病毒数据库
     */
    private void updataVirus() {

        dao = new AntivirusDao();

        //联网从服务器获取到最新数据的md5的特征码

        HttpUtils httpUtils = new HttpUtils();

        String url = "http://192.168.13.126:8080/virus.json";

//        httpUtils.send(HttpRequest.HttpMethod.GET, url, new RequestCallBack<String>() {
//
//            @Override
//            public void onFailure(HttpException arg0, String arg1) {
//                // TODO Auto-generated method stub
//
//            }
//
//            @Override
//            public void onSuccess(ResponseInfo<String> arg0) {
//                System.out.println(arg0.result);
////				﻿{"md5":"51dc6ba54cbfbcff299eb72e79e03668","desc":"蝗虫病毒赶快卸载"}
//
//                try {
//                    JSONObject jsonObject = new JSONObject(arg0.result);
//
//                    Gson gson = new Gson();
//                    //解析json
//                    Virus virus = gson.fromJson(arg0.result, Virus.class);
//
////					String md5 = jsonObject.getString("md5");
////
////					String desc = jsonObject.getString("desc");
//
//                    dao.addVirus(virus.md5, virus.desc);
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//
//            }
//
//
//        });
    }


    /**
     * 快捷方式
     */

    private void createShortcut() {

        Intent intent = new Intent();

        intent.setAction("com.android.launcher.action.INSTALL_SHORTCUT");
        //如果设置为true表示可以创建重复的快捷方式
        intent.putExtra("duplicate", false);

        /**
         * 1 干什么事情
         * 2 你叫什么名字
         * 3你长成什么样子
         */
        intent.putExtra(Intent.EXTRA_SHORTCUT_ICON, BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher));
        intent.putExtra(Intent.EXTRA_SHORTCUT_NAME, "黑马手机卫士");
        //干什么事情
        /**
         * 这个地方不能使用显示意图
         * 必须使用隐式意图
         */
        Intent shortcut_intent = new Intent();

        shortcut_intent.setAction("aaa.bbb.ccc");

        shortcut_intent.addCategory("android.intent.category.DEFAULT");

        intent.putExtra(Intent.EXTRA_SHORTCUT_INTENT, shortcut_intent);

        sendBroadcast(intent);

    }


}
