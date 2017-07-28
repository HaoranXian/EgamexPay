package com.baidu.BaiduMap.channel;

public interface IPayChannelListener {
	void onPaySucceeded();
	void onPayFailed();
	void onPayCanceled();
}
