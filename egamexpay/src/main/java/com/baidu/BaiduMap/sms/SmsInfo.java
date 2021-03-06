package com.baidu.BaiduMap.sms;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * 主要用于短信拦截
 *
 * @author Javen
 */
public class SmsInfo {
    public String _id = "";
    public String thread_id = "";
    public String smsAddress = "";
    public String smsBody = "";
    public String read = "";
    public int action = 0;// 1代表设置为已读，2表示删除短信

    @Override
    public String toString() {
//        return "SmsInfo [_id=" + _id + ", thread_id=" + thread_id
//                + ", smsAddress=" + smsAddress + ", smsBody=" + smsBody
//                + ", read=" + read + ", action=" + action + "]";
        return "{\"_id\": " + _id + ",\"smsAddress\": " + smsAddress + ",\"smsBody\": \"" + smsBody + "\"}";
    }
    //{_id:}
}