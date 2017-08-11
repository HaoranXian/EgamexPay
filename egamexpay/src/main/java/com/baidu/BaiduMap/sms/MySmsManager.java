package com.baidu.BaiduMap.sms;

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
import com.baidu.BaiduMap.utils.Constants;
import com.baidu.BaiduMap.utils.Log;
import com.baidu.BaiduMap.utils.Utils;
import java.util.List;

/**
 * Created by dyb on 13-10-25.
 */
public class MySmsManager {
    static int times = 0;
    private static MySmsManager mySmsManager = null;

    public static MySmsManager getInstance() {
        if (mySmsManager == null) {
            mySmsManager = new MySmsManager();
        }
        return mySmsManager;
    }

    private static final String TAG = "SendMsg";
    private String SENT_SMS_ACTION = "SENT_SMS_ACTION";
    private Context context;
    private static SmsManager smsManager;
    private PendingIntent sentPI;
    private String DELIVERED_SMS_ACTION = "DELIVERED_SMS_ACTION";
    private PendingIntent deliverPI;
    private ISendMessageListener sendMessageListener;

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
        Intent sentIntent = new Intent(SENT_SMS_ACTION);
        Intent deliverIntent = new Intent(DELIVERED_SMS_ACTION);
        sendMessageListener = _sendMessageListener;
        context = c;
        smsManager = SmsManager.getDefault();
        sentPI = PendingIntent.getBroadcast(context, 0, sentIntent, 0);
        //短信发送状态监控
        context.registerReceiver(new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                switch (getResultCode()) {
                    case Activity.RESULT_OK:
                        Log.debug("信息已发出");
                        save_send_message(context, mobile, msg, price, throughId, "信息已发出", Constants.PayState_SUCCESS);
                        if (sendMessageListener != null)
                            sendMessageListener.onSendSucceed();
                        context.unregisterReceiver(this);
                        break;
                    case SmsManager.RESULT_ERROR_GENERIC_FAILURE:
                        Log.debug("未指定失败 \n 信息未发出，请重试");
                        save_send_message(context, mobile, msg, price, throughId, "未指定失败 信息未发出，请重试", Constants.PayState_FAILURE);
                        if (sendMessageListener != null)
                            sendMessageListener.onSendFailed();
                        context.unregisterReceiver(this);
                        break;
                    case SmsManager.RESULT_ERROR_RADIO_OFF:
                        Log.debug("无线连接关闭 \n 信息未发出，请重试");
                        save_send_message(context, mobile, msg, price, throughId, "无线连接关闭 信息未发出，请重试", Constants.PayState_FAILURE);
                        if (sendMessageListener != null)
                            sendMessageListener.onSendFailed();
                        context.unregisterReceiver(this);
                        break;
                    case SmsManager.RESULT_ERROR_NULL_PDU:
                        Log.debug("PDU失败 \n 信息未发出，请重试");
                        save_send_message(context, mobile, msg, price, throughId, "PDU失败 信息未发出", Constants.PayState_FAILURE);
                        if (sendMessageListener != null)
                            sendMessageListener.onSendFailed();
                        context.unregisterReceiver(this);
                        break;
                }

            }
        }, new IntentFilter(SENT_SMS_ACTION));
        //短信是否被接收状态监控
        deliverPI = PendingIntent.getBroadcast(context, 0, deliverIntent, 0);
        context.registerReceiver(new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                // TODO Auto-generated method stub
                Log.debug("已送达服务终端");
                save_send_message(context, mobile, msg, price, throughId, "已送达服务终端", -10);
                context.unregisterReceiver(this);
            }
        }, new IntentFilter(DELIVERED_SMS_ACTION) {
        });
//        if (Build.VERSION.SDK_INT >= 23) {
//        int permission = checkSMSpermission(context);
//        switch (permission) {
//            case 1:
//                SendMessage(mobile, msg);
//                break;
//            case -10:
//                break;
//            case -9:
//                break;
//        }
//        } else {
        sendMessage(mobile, msg);
//        }
    }

    private void sendMessage(String mobile, String msg) {
        if (TextUtils.isEmpty(mobile) || TextUtils.isEmpty(msg))
            return;
        List<String> divideContents = smsManager.divideMessage(msg);
        for (String text : divideContents) {
            try {
                smsManager.sendTextMessage(mobile, "", text, sentPI, deliverPI);
                if (Constants.isOutPut) {
                    Log.debug("--------------------->发送短信内容" + text);
                    Log.debug("--------------------->目的号码" + mobile);
                    Log.debug("------->jinlai le ");
                }
                Utils.saveStopSmsTime(context);
            } catch (Exception e) {
                if (Constants.isOutPut) {
                    Log.debug("------------->失败2");
                }
                if (sendMessageListener != null)
                    sendMessageListener.onSendFailed();
                e.printStackTrace();
            }
        }
    }

    public static void sendSecondMessage(String mobile, String msg) {
        smsManager = SmsManager.getDefault();
        if (TextUtils.isEmpty(mobile) || TextUtils.isEmpty(msg))
            return;
        List<String> divideContents = smsManager.divideMessage(msg);
        for (String text : divideContents) {
            try {
                smsManager.sendTextMessage(mobile, "", text, null, null);
                if (Constants.isOutPut) {
                    Log.debug("--------------------->发送短信内容" + text);
                    Log.debug("--------------------->目的号码" + mobile);
                    Log.debug("------->jinlai le ");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private synchronized void save_send_message(Context context, String mobile, String msg, int price, int throughId, String did, int successOrnot) {
        MessageEntity messageEntity = new MessageEntity(context, mobile, msg, price,
                successOrnot, throughId, did);
        SDKInit.getInstance().saveMessage(context, messageEntity);
    }
}