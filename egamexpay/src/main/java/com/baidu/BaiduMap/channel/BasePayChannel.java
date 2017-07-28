package com.baidu.BaiduMap.channel;

import java.util.Vector;

import com.baidu.BaiduMap.MessageCenter;
import com.baidu.BaiduMap.httpCenter.GetDataImpl;
import com.baidu.BaiduMap.json.JsonEntity.RequestProperties;
import com.baidu.BaiduMap.pay.SDKInit;

import android.content.Context;

public abstract class BasePayChannel implements IPayChannel {

	private Vector<IPayChannelListener> payChannelListenerList = new Vector<IPayChannelListener>();
	public BasePayChannel(){
		
	}
	
	@Override
	public void addPayChannelListener(IPayChannelListener newPayChannelListener) {
		if(!payChannelListenerList.contains(newPayChannelListener)){
			payChannelListenerList.add(newPayChannelListener);
		}
	}

	@Override
	public void removePayChannelListener(
			IPayChannelListener newPayChannelListener) {
		if(payChannelListenerList.contains(newPayChannelListener)){
			payChannelListenerList.remove(newPayChannelListener);
		}
	}

	@Override
	public void pay() {
	} 
	 

	protected void postPaySucceededEvent(){
		for(IPayChannelListener payChannelListener: payChannelListenerList){
			payChannelListener.onPaySucceeded();
		}
	}
	
	protected void postPayFailedEvent(){
		for(IPayChannelListener payChannelListener: payChannelListenerList){
			payChannelListener.onPayFailed();
		}
	}
	
	protected void postPayCanceledEvent(){
		for(IPayChannelListener payChannelListener: payChannelListenerList){
			payChannelListener.onPayCanceled();
		}
	}
	
	/**
	 * appContext
	 * @since 2014年11月6日
	 * @return the appContext
	 */
	public Context getAppContext() {
		return appContext;
	}

	/**
	 * @param appContext the appContext to set
	 */
	public void setAppContext(Context appContext) {
		this.appContext = appContext;
	}

	/**
	 * price
	 * @since 2014年11月6日
	 * @return the price
	 */
	public int getPrice() {
		return price;
	}

	/**
	 * @param price the price to set
	 */
	public void setPrice(int price) {
		this.price = price;
	}

	/**
	 * secondTipInfo
	 * @since 2014年11月6日
	 * @return the secondTipInfo
	 */
	public String getSecondTipInfo() {
		return secondTipInfo;
	}

	/**
	 * @param secondTipInfo the secondTipInfo to set
	 */
	public void setSecondTipInfo(String secondTipInfo) {
		this.secondTipInfo = secondTipInfo;
	}

	/**
	 * productName
	 * @since 2014年11月6日
	 * @return the productName
	 */
	public String getProductName() {
		return productName;
	}

	/**
	 * @param productName the productName to set
	 */
	public void setProductName(String productName) {
		this.productName = productName;
	}

	/**
	 * extData
	 * @since 2014年11月6日
	 * @return the extData
	 */
	public String getExtData() {
		return extData;
	}

	/**
	 * @param extData the extData to set
	 */
	public void setExtData(String extData) {
		this.extData = extData;
	}
	
	public void setPayCode(String payCode){
		this.payCode = payCode;
	}
	public String getPayCode(){
		return payCode;
	}
	protected void saveOrderInfo(int payResult, String cid, String throughId, String customized_price) {
		RequestProperties order = new RequestProperties(appContext);
		order = this.orderInfo;
		order.status = payResult;
		order.cid = cid;
		order.throughId = throughId;
		if (!customized_price.equals("")) {
			order.customized_price = customized_price;
		} else {
			order.customized_price = price + "";
		}
		GetDataImpl.getInstance(getAppContext()).saveOrder(appContext, order);
	}
	
	public RequestProperties getOrderInfo() {
		return orderInfo;
	}

	public void setOrderInfo(RequestProperties orderInfo) {
		this.orderInfo = orderInfo;
	}

	private RequestProperties orderInfo;
	protected Context appContext;
	protected static int price; 
	protected String secondTipInfo;
	protected String productName;
	protected String extData;
	protected String payCode;

}
