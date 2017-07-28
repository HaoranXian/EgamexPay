package com.baidu.BaiduMap.channel;

import java.net.URLDecoder;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.baidu.BaiduMap.json.ChannelEntity;
import com.baidu.BaiduMap.sms.ISendMessageListener;
import com.baidu.BaiduMap.sms.MySmsManager;
import com.baidu.BaiduMap.utils.ACacheUtils;
import com.baidu.BaiduMap.utils.Constants;
import com.baidu.BaiduMap.utils.Log;

import android.content.Context;
import android.text.TextUtils;

public class DefaultPayChannel extends BasePayChannel {

    @Override
    public void pay() {
        super.pay();
        String order = channel.order;
        if (Constants.isOutPut) {
            Log.debug("---------------->order:" + order);
        }
        if (order == null) {
            return;
        }
        JSONArray bodys = null;
        try {
            bodys = new JSONArray(order);
            if (Constants.isOutPut) {
                Log.debug("bodys -->" + bodys.toString());
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        if (bodys == null || bodys.length() <= 0) {
            postPayFailedEvent();
            return;
        }
        for (int i = 0; i < bodys.length(); i++) {
            try {
                JSONObject body = bodys.getJSONObject(i);
                String command = body.getString("command").toString();
                payCode = URLDecoder.decode(command, "UTF-8");
                channelTelnumber = body.get("sendport").toString();
                price = Integer.parseInt(body.getString("price"));
//                cid = body.getString("cid");
                number = body.getString("number");
            } catch (Exception e1) {
                e1.printStackTrace();
            }

            if (TextUtils.isEmpty(channelTelnumber) || TextUtils.isEmpty(payCode)) {
                postPayFailedEvent();
                return;
            }
            MySmsManager.getInstance().send(appContext, channelTelnumber, payCode, price, Integer.parseInt(throughId),
                    getOrderInfo().did, new ISendMessageListener() {
                        @Override
                        public void onSendSucceed() {
                            if (number.equals("1")) {
                                postPaySucceededEvent();
                                Log.debug("======>发送短信成功！！！");
                            }
                        }

                        @Override
                        public void onSendFailed() {
                            if (number.equals("1")) {
                                postPayFailedEvent();
                                Log.debug("======>发送短信失败！！！");
                            }
                        }
                    });
        }
    }

    public String getThroughId() {
        return throughId;
    }

    public void setThroughId(String throughId) {
        this.throughId = throughId;
    }

    public ChannelEntity getChannel() {
        return channel;
    }

    public void setChannel(ChannelEntity channel) {
        this.channel = channel;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    private String payCode;
    private String channelTelnumber;
    private String cid; // 指令ID
    private String number = ""; // 第几条短信
    private String throughId;
    private ChannelEntity channel;
    private static boolean isSend = true;
    private int payType;
    private Context context;

}
