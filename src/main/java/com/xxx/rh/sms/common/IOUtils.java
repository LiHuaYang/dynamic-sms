package com.xxx.rh.sms.common;

import com.alibaba.fastjson.JSONObject;

import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * 输入输出工具类
 * @author lihy
 * @version 1.0  2018/7/24
 */
public final class IOUtils {

    private IOUtils (){}

    public static String getResp(boolean flag, String msg) {
        JSONObject json = new JSONObject();
        json.put("Result", flag);
        json.put("Message", msg);
        return json.toJSONString();
    }

    public static String getFailResp() {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("Result", "false");
        jsonObject.put("Message", "发送失败");
        return jsonObject.toJSONString();
    }

    /**
     * 解析请求
     * @param request 输入流
     * @return 解析出的字符串，解析失败时，返回为 null
     * @author lihy  v1.0   2017/12/8
     */
    public static String parseRequst(HttpServletRequest request, String encoding){
        StringBuilder buffer = new StringBuilder();
        BufferedReader reader = null;
        try {
            request.setCharacterEncoding(encoding);
            reader = new BufferedReader(new InputStreamReader(request.getInputStream(), encoding));
            String line;
            while ((line = reader.readLine()) != null) {
                buffer.append(line);
            }
        } catch (Exception e) {
            return null;
        } finally {
            if (null != reader) {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return buffer.toString();
    }
}
