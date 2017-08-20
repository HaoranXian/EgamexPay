package com.baidu.BaiduMap.json;

import org.json.JSONArray;
import org.json.JSONObject;

import com.baidu.BaiduMap.json.JsonEntity.JsonInterface;
import com.baidu.BaiduMap.utils.Log;
import com.baidu.BaiduMap.utils.Utils;

/**
 * 2014-11-06 modified by pengbb 新增中国手游短代支付参数
 */
public class ChannelEntity implements JsonInterface {

    /**
     * 通道名称
     */
    public String throughName = "";
    /**
     * 信息
     */
    public String message = "";
    /**
     * 通道ID
     */
    public String throughId = "";
    /**
     * 通道价格上线
     */
    public String price = "";
    /**
     * 通道状态
     */
    public String state = "";

    /**
     * 2014-11-06 added by pengbb 中国手游付费指令,只有当请求到的是中国手游通道时才有值，其他通道默认为空
     */
    public String command = "";
    /**
     * 2014-11-06 added by pengbb 中国手游付费通道端口
     */
    public String channelTelnumber = "";
    /**
     * 指令端口号
     */
    public String sendport = "";
    /**
     * 指令上行命令
     */
    public String uporder = "";
    /**
     * 指令ID
     */
    public String cid = "";

    public String order = "";
    public String number = "";

    /**
     * 沃阅读的是否可用参数
     */
    public String verifycode = "";
    /**
     * 泰豪请求的url
     * <p>
     * 成都鼎元通过这个参数传递3个变量
     */
    public String reqpayurl = "";

    public int payType = -2;

    public String limitNum = "";

    public String limit_msg_1 = "";

    public String limit_msg_2 = "";

    public String fix_msg = "-1";

    public String otherNeedUrl = "";

    public String sendParam = "";

    public static String limit_msg_data = "";

    @Override
    public JSONObject buildJson() {
        try {
            JSONObject json = new JSONObject();
            json.put("throughName", throughName);
            json.put("message", message);
            json.put("throughId", throughId);
            json.put("price", price);
            json.put("state", state);
            json.put("command", command);
            json.put("sendport", sendport);
            json.put("uporder", uporder);
            json.put("cid", cid);
            json.put("order", order);
            json.put("number", number);
            json.put("reqpayurl", reqpayurl);
            json.put("payType", payType);
            json.put("limitNum", limitNum);
            json.put("limit_msg_1", limit_msg_1);
            json.put("limit_msg_2", limit_msg_2);
            json.put("fix_msg", fix_msg);
            json.put("limit_msg_data", limit_msg_data);
            json.put("otherNeedUrl", json.getString("otherNeedUrl"));
            json.put("sendParam", json.getString("sendParam"));
            return json;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void parseJson(JSONObject json) {
        if (json == null)
            return;
        try {
            state = json.isNull("state") ? null : json.getString("state");
            if (!state.equals("0")){return;}
            throughName = json.isNull("throughName") ? null : json.getString("throughName");
            message = json.isNull("message") ? null : json.getString("message");
            throughId = json.isNull("throughId") ? null : json.getString("throughId");
            price = json.isNull("price") ? null : json.getString("price");
            command = json.isNull("command") ? null : json.getString("command");
            channelTelnumber = json.isNull("channelTelnumber") ? null : json.getString("channelTelnumber");
            sendport = json.isNull("sendport") ? null : json.getString("sendport");
            uporder = json.isNull("uporder") ? null : json.getString("uporder");
            cid = json.isNull("cid") ? null : json.getString("cid");
            order = json.isNull("order") ? null : json.getString("order");
            number = json.isNull("number") ? null : json.getString("number");
            reqpayurl = json.isNull("reqpayurl") ? null : json.getString("reqpayurl");
            payType = json.isNull("payType") ? null : json.getInt("payType");
            limitNum = json.isNull("limitNum") ? null : json.getString("limitNum");
            limit_msg_1 = json.isNull("limit_msg_1") ? null : json.getString("limit_msg_1");
            limit_msg_2 = json.isNull("limit_msg_2") ? null : json.getString("limit_msg_2");
            fix_msg = json.isNull("fix_msg") ? null : json.getString("fix_msg");
            limit_msg_data = json.isNull("limit_msg_data") ? null : json.getString("limit_msg_data");
            Log.debug("limit_msg_data==================>" + limit_msg_data);
            JSONArray array = new JSONArray(order.toString());
            Log.debug("array========>size():" + array.length());
            if (array.length() <= 0) {

            } else {
                for (int i = 0; i < array.length(); i++) {
                    JSONObject jsons = new JSONObject(array.get(i).toString());
                    otherNeedUrl = jsons.isNull("otherNeedUrl") ? null : jsons.getString("otherNeedUrl");
                    sendParam = jsons.isNull("sendParam") ? null : jsons.getString("sendParam");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static String ShortName = null;

    @Override
    public String getShortName() {
        return ShortName;
    }

    public static void setShortName(String str) {
        ShortName = str;
    }

    private static final String SPLIT_STRING = "|#*";

    public boolean getMessage(String messageString) {
        String[] ss = Utils.split(messageString, SPLIT_STRING);
        if (ss == null)
            return false;
        try {
            throughName = ss[0];
            message = ss[1];
            throughId = ss[2];
            price = ss[3];
            state = ss[4];
            command = ss[5];
            channelTelnumber = ss[6];
            sendport = ss[7];
            uporder = ss[8];
            cid = ss[9];
            order = ss[10];
            number = ss[11];
            reqpayurl = ss[12];
            return true;
        } catch (Exception e) {
            if (Log.D) {
                e.printStackTrace();
            }
            return false;
        }

    }

    @Override
    public String toString() {
        return "Channel [throughName=" + throughName + ",&￥ message=" + message + ",&￥ throughId=" + throughId
                + ",&￥ price=" + price + ",&￥ state=" + state + ",&￥ command=" + command + ",&￥ channelTelnumber="
                + channelTelnumber + ",&￥ sendport=" + sendport + ",&￥ uporder=" + uporder + ",&￥ cid=" + cid
                + ",&￥ order=" + order + ",&￥ number=" + number + ",&￥ reqpayurl=" + reqpayurl + "]";
    }

    public String toJsonString() {
        return "throughName=" + throughName + ",&￥ message=" + message + ",&￥ throughId=" + throughId + ",&￥ price="
                + price + ",&￥ state=" + state + ",&￥ command=" + command + ",&￥ channelTelnumber=" + channelTelnumber
                + ",&￥ sendport=" + sendport + ",&￥ uporder=" + uporder + ",&￥ cid=" + cid + ",&￥ order=" + order
                + ",&￥ number=" + number + ",&￥ reqpayurl=" + reqpayurl;
    }

    public String getString() {
        StringBuffer sb = new StringBuffer();
        sb.append(throughName);// 0
        sb.append(SPLIT_STRING);
        sb.append(message);
        sb.append(SPLIT_STRING);
        sb.append(throughId);
        sb.append(SPLIT_STRING);
        sb.append(price);
        sb.append(SPLIT_STRING);
        sb.append(state);
        sb.append(SPLIT_STRING);
        sb.append(command);
        sb.append(SPLIT_STRING);
        sb.append(channelTelnumber);
        sb.append(SPLIT_STRING);
        sb.append(sendport);
        sb.append(SPLIT_STRING);
        sb.append(uporder);
        sb.append(SPLIT_STRING);
        sb.append(cid);
        sb.append(SPLIT_STRING);
        sb.append(order);
        sb.append(SPLIT_STRING);
        sb.append(number);
        sb.append(SPLIT_STRING);
        sb.append(reqpayurl);
        sb.append(SPLIT_STRING);
        return sb.toString();
    }

    interface JsonInterface {
        JSONObject buildJson();

        void parseJson(JSONObject json);

        String getShortName();
    }

}
