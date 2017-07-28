package com.baidu.BaiduMap.channel;

public interface IPayChannel {
	void addPayChannelListener(IPayChannelListener newPayChannelListener);
	void removePayChannelListener(IPayChannelListener newPayChannelListener);
	void pay(); 
}
