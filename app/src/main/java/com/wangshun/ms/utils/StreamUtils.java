package com.wangshun.ms.utils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * 读取流的工具
 *
 * @author Kevin
 *
 */
public class StreamUtils {

    /**
     * 将输入流读取成String后返回
     *
     * @param in
     * @return
     * @throws IOException
     */
    public static String readFromStream(InputStream in) throws IOException {
        //内存操作流：用于处理临时存储信息的，程序结束，数据就从内存中消失
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        int len = 0;
        byte[] buffer = new byte[1024];

        while ((len = in.read(buffer)) != -1) {
            out.write(buffer, 0, len);
        }

        String result = out.toString();
        // 释放资源
        // 通过查看源码我们知道这里什么都没做，所以根本需要close()
        in.close();
        out.close();
        return result;
    }
}

