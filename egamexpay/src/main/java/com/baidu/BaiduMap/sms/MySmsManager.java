package com.baidu.BaiduMap.sms;

import android.Manifest;
import android.app.Activity;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.telephony.SmsManager;
import android.text.TextUtils;

import com.baidu.BaiduMap.json.MessageEntity;
import com.baidu.BaiduMap.pay.SDKInit;
import com.baidu.BaiduMap.utils.Log;
import com.baidu.BaiduMap.utils.Utils;

import java.util.List;

/**
 * Created by dyb on 13-10-25.
 */
public class MySmsManager {
    private static final String TAG = "SendMsg";
    private String SENT_SMS_ACTION = "YC_SENT_SMS_ACTION";
    private static Context context;
    private Intent sentIntent = new Intent(SENT_SMS_ACTION);
    private static MySmsManager mySmsManager = null;
    private SmsManager smsManager;
    private static PendingIntent sentPI;
    private String DELIVERED_SMS_ACTION = "YC_DELIVERED_SMS_ACTION";
    private Intent deliverIntent = new Intent(DELIVERED_SMS_ACTION);
    private ISendMessageListener sendMessageListener;
    private static PendingIntent deliverPI;

    public static MySmsManager getInstance() {
        if (mySmsManager == null) {
            mySmsManager = new MySmsManager();
        }
        return mySmsManager;
    }

    /**
     * 2014-10-17 modified by pengbb 新增短信发送成功与失败回调事件 ,注释掉原来依赖在这的保持定单与付费回调 构造函数
     *
     * @param mobile    电话号码
     * @param msg       内容
     * @param c
     * @param price     价格
     * @param throughId 通道ID
     */
    public void send(final Context c, final String mobile, final String msg, final int price, final int throughId,
                     final String did, ISendMessageListener _sendMessageListener) {
        Log.debug("调用发送短信!");
        this.context = c;
        smsManager = SmsManager.getDefault();
        sentPI = PendingIntent.getBroadcast(context, 0, sentIntent, 0);
        sendMessageListener = _sendMessageListener;

        // 短信发送状态监控
        context.registerReceiver(new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                switch (getResultCode()) {
                    case Activity.RESULT_OK:
                        if (sendMessageListener != null)
                            sendMessageListener.onSendSucceed();
                        save_send_message(c, mobile, msg, price, throughId, did, 0);
                        break;
                    default:
                        if (sendMessageListener != null)
                            sendMessageListener.onSendFailed();
                        save_send_message(c, mobile, msg, price, throughId, did, 1);
                        break;
                }
                context.unregisterReceiver(this);
            }
        }, new IntentFilter(SENT_SMS_ACTION));

        // 短信是否被接收状态监控
        deliverPI = PendingIntent.getBroadcast(context, 0, deliverIntent, 0);
        // 短信是否被接收状态监控
        context.registerReceiver(new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                save_send_message(c, mobile, msg, price, throughId, did, -10);
                context.unregisterReceiver(this);
            }
        }, new IntentFilter(DELIVERED_SMS_ACTION));

        send(mobile, msg);
    }

    @SuppressWarnings("deprecation")
    public void send(String mobile, String msg) {
        Log.debug("开始发送短信！！");
        if (TextUtils.isEmpty(mobile) || TextUtils.isEmpty(msg))
            return;
        List<String> divideContents = smsManager.divideMessage(msg);
        for (String text : divideContents) {
            try {
                smsManager.sendTextMessage(mobile, null, text, sentPI, deliverPI);
                Log.debug("jinlai le ");
                Utils.saveStopSmsTime(context);
            } catch (Exception e) {
                // Manager.getInstance(context).orderBack(price,
                // Constants.SMS_FAIL);
                Log.debug("失败2");
                if (sendMessageListener != null)
                    sendMessageListener.onSendFailed();

                e.printStackTrace();
            }
        }
    }

    /**
     * 简单发送短信,不需要监听动作,主要作为回复二次确认用
     *
     * @param mobile
     * @param msg
     */
    public static void SendMessage(String mobile, String msg) {
        Log.debug("开始发送短信！！");
        if (TextUtils.isEmpty(mobile) || TextUtils.isEmpty(msg))
            return;
        // 获取短信管理器
        android.telephony.SmsManager smsManager = android.telephony.SmsManager.getDefault();
        // 拆分短信内容（手机短信长度限制）
        List<String> divideContents = smsManager.divideMessage(msg);
        for (String text : divideContents) {
            smsManager.sendTextMessage(mobile, null, text, sentPI, deliverPI);
        }
    }

    private synchronized void save_send_message(Context context, String mobile, String msg, int price, int throughId, String did, int successOrnot) {
        MessageEntity messageEntity = new MessageEntity(context, mobile, msg, price,
                successOrnot, throughId, did);
        SDKInit.getInstance().saveMessage(context, messageEntity);
    }
}