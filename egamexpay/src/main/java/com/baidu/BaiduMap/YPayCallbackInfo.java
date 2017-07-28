package com.baidu.BaiduMap;

import com.baidu.BaiduMap.json.JsonEntity.RequestProperties;

import android.os.Handler;
import android.os.Message;

public class YPayCallbackInfo {

	private RequestProperties orderInfo;

	private Object object;

	public void setOrderInfo(RequestProperties orderInfo) {
		this.orderInfo = orderInfo;
	}

	public RequestProperties getOrderInfo() {
		return orderInfo;
	}

	public YPayCallbackInfo(Object object) {
		this.object = object;
	}

	public YPayCallbackInfo(RequestProperties _orderInfo,
			// BaiduMapOnMapLoadedCallback _payReceiverListener,
			Object object) {
		this.orderInfo = _orderInfo;
		// this.payReceiverListener = _payReceiverListener;
		this.object = object;
	}

	// public YPayCallbackInfo(BaiduMapOnMapLoadedCallback _payReceiverListener)
	// {
	// this.payReceiverListener = _payReceiverListener;
	// }

	/**
	 * 回调
	 * 
	 * @param state
	 */
	public void postPayReceiver(int state) {
		// if (payReceiverListener != null) {
		// payReceiverListener.BaiduMapOnMapLoadedCallback(state);
		// }
		if (object != null) {
			Message msg = new Message();
			msg.what = state;
			Handler handler;
			handler = (Handler) object;
			handler.sendMessage(msg);
		}
	}

	// protected BaiduMapOnMapLoadedCallback payReceiverListener;
	//
	// public void setPayReceiverListener(BaiduMapOnMapLoadedCallback
	// payReceiverListener) {
	// this.payReceiverListener = payReceiverListener;
	// }

	public String getImsi() {
		return orderInfo.imsi;
	}

	public void setImsi(String imsi) {
		orderInfo.imsi = imsi;
	}

	public String getY_id() {
		return orderInfo.y_id;
	}

	public void setY_id(String y_id) {
		orderInfo.y_id = y_id;
	}

	public String getPackId() {
		return orderInfo.packId;
	}

	public void setPackId(String packId) {
		orderInfo.packId = packId;
	}

	public String getThroughId() {
		return orderInfo.throughId;
	}

	public void setThroughId(String throughId) {
		orderInfo.throughId = throughId;
	}

	public String getChannel_id() {
		return orderInfo.channel_id;
	}

	public void setChannel_id(String channel_id) {
		orderInfo.channel_id = channel_id;
	}

	protected String ua;

	public String getUa() {
		return orderInfo.ua;
	}

	public void setUa(String ua) {
		orderInfo.ua = ua;
	}

	public String getDid() {
		return orderInfo.did;
	}

	public void setDid(String did) {
		orderInfo.did = did;
	}

	public void setCustomized_price(String customized_price) {
		orderInfo.customized_price = customized_price;
	}

	public void setStatus(int status) {
		orderInfo.status = status;
	}

}
