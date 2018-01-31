package com.wangshun.ms.utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * author：WS
 * version：1.0
 * Created  on 2016/1/26 at 13:23.
 */
public class MD5Utils {

    public static String encode(String password){

        try {
            //获取MD5对象
            MessageDigest instance = MessageDigest.getInstance("MD5");
            //对字符串进行加密，返回字节数组
            byte[] digest = instance.digest(password.getBytes());

            StringBuffer sb = new StringBuffer();
            for (byte b : digest) {
                //获取字节低八位有效值
                int i = b & 0xff;
                //将整数转换为16进制
                String hexString = Integer.toHexString(i);
                //将长度为1时，补零
                if (hexString.length() < 2) {
                    hexString = "0" + hexString;
                }
                //MD5永远是32位
                sb.append(hexString);
            }

            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            //没有该算法时抛出此异常
            e.printStackTrace();

        }

        return "";
    }



    }



