package com.ljq.demo.pay.common.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * @Description: 文件工具类
 * @Author: junqiang.lu
 * @Date: 2018/7/4
 */
public final class FileUtil {

    private FileUtil(){}

    /**
     * 从输入流中读取字符数据
     *
     * @param inputStream servlet request inputStream
     * @return
     * @throws IOException
     */
    public static String getStringFromStream(InputStream inputStream) throws IOException {
        if(inputStream == null){
            return null;
        }
        StringBuffer sb = new StringBuffer();
        String s ;
        BufferedReader in = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"));
        while ((s = in.readLine()) != null){
            sb.append(s);
        }
        in.close();
        inputStream.close();

        return sb.toString();
    }


}
