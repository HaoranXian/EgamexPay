package com.baidu.BaiduMap.utils;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Method;
import java.math.BigInteger;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.util.DisplayMetrics;

public class Utils {
    //没有网络连接
    public static final int NETWORN_NONE = 0;
    //wifi连接
    public static final int NETWORN_WIFI = 1;
    //手机网络数据连接类型
    public static final int NETWORN_2G = 2;
    public static final int NETWORN_3G = 3;
    public static final int NETWORN_4G = 4;
    public static final int NETWORN_MOBILE = 5;

    /**
     * 获取手机号码
     */
    @SuppressLint("NewApi")
    public static String getTelPhoneNumber(Context context) {
        Boolean isHadSIM = getSIMState(context);
        if (isHadSIM) {
            TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
            String te1 = tm.getLine1Number();// 获取本机号码
            if (te1.isEmpty() || null == te1) {
                return "";
            } else {
                return te1;
            }
        }
        return "";
    }

    /**
     * 检查当前网络是否可用
     *
     * @param context
     * @return
     */

    public static boolean isNetAvailable(Context context) {
        ConnectivityManager manager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = manager.getActiveNetworkInfo();
        return (info != null && info.isAvailable());
    }

    /**
     * 读取手机唯一标识
     *
     * @param ctx
     * @return
     */
    public static String getIMSI(final Context ctx) {
        String imsi = "";
        try {
            TelephonyManager tm = (TelephonyManager) ctx.getSystemService(Context.TELEPHONY_SERVICE);
            imsi = tm.getSubscriberId();
        } catch (Exception e) {

        }
        return imsi;
    }

    /**
     * 获取iccid
     */
    public static String getICCID(Context ctx) {
        String iccid = "";
        try {
            TelephonyManager tm = (TelephonyManager) ctx.getSystemService(Context.TELEPHONY_SERVICE);
            iccid = tm.getSimSerialNumber(); // 取出ICCID
        } catch (Exception e) {

        }
        return iccid;
    }

    /**
     * 获取手机设备号
     *
     * @param ctx
     * @return
     */
    public static String getIMEI(final Context ctx) {
        String imei = "";
        try {
            TelephonyManager tm = (TelephonyManager) ctx.getSystemService(Context.TELEPHONY_SERVICE);
            imei = tm.getDeviceId();
        } catch (Exception e) {

        }
        return imei;
    }

    /**
     * 获取sim卡状态
     *
     * @param ctx
     * @return true sim卡状态良好，false sim拔出锁定
     */
    public static boolean getSIMState(Context ctx) {
        TelephonyManager tm = (TelephonyManager) ctx.getSystemService(Context.TELEPHONY_SERVICE);
        switch (tm.getSimState()) {
            case TelephonyManager.SIM_STATE_READY:
                return true;
            case TelephonyManager.SIM_STATE_ABSENT:
            default:
                return false;
        }
    }

    /**
     * 获取手机IP地址
     *
     * @return
     */
    public static String getLocalIpAddress() {
        try {
            for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements(); ) {
                NetworkInterface intf = en.nextElement();
                for (Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses(); enumIpAddr.hasMoreElements(); ) {
                    InetAddress inetAddress = enumIpAddr.nextElement();
                    if (!inetAddress.isLoopbackAddress() && !inetAddress.isLinkLocalAddress()) {
                        return inetAddress.getHostAddress().toString();
                    }
                }
            }
        } catch (SocketException ex) {
            if (Constants.isOutPut) {
                Log.debug("WifiPreference IpAddress", ex.toString());
            }
        }

        return null;
    }

    /**
     * 获取游戏ID
     *
     * @param ctx
     * @return
     */
    public static String getGameId(Context ctx) {
        String gameId = null;
        ApplicationInfo appinfo = null;
        try {
            appinfo = ctx.getPackageManager().getApplicationInfo(ctx.getPackageName(), PackageManager.GET_META_DATA);
            if (appinfo != null) {
                Bundle metaData = appinfo.metaData;
                if (metaData != null) {
                    gameId = metaData.get("Y-PAY-GAMEID").toString();
                    return gameId;
                }
            }
        } catch (Exception e) {
            if (Log.D)
                e.printStackTrace();
            return gameId;
        }
        return gameId;
    }

    public static String getAppID(Context ctx) {
        String AppID = null;
        ApplicationInfo appinfo = null;
        try {
            appinfo = ctx.getPackageManager().getApplicationInfo(ctx.getPackageName(), PackageManager.GET_META_DATA);
            if (appinfo != null) {
                Bundle metaData = appinfo.metaData;
                if (metaData != null) {
                    AppID = "0000" + metaData.get("MJPay-AppID").toString();
                    return AppID;
                }
            }
        } catch (Exception e) {
            // TODO: handle exception
        }
        return AppID;
    }

    /**
     * packId
     */
    public static String getPackId(Context ctx) {
        // if (Log.D) {
        // return "86";
        // }
        String gameId = "";
        ApplicationInfo appinfo = null;
        try {
            appinfo = ctx.getPackageManager().getApplicationInfo(ctx.getPackageName(), PackageManager.GET_META_DATA);
            if (appinfo != null) {
                Bundle metaData = appinfo.metaData;
                if (metaData != null) {
                    gameId = metaData.get("Y-PAY-PID").toString();
                    return gameId;
                }
            }
        } catch (Exception e) {
            if (Log.D)
                e.printStackTrace();
            return gameId;
        }
        return gameId;
    }

    /**
     * 把数组第一个移到最后的位置，数组长度不变
     *
     * @return
     */
    public static Object[] moveArray(Object[] array) {
        if (array.length > 1) {
            for (int i = 1, n = 0; i < array.length; i++, n++) {
                String tmp = (String) array[n];
                array[n] = array[i];
                array[i] = tmp;
            }
        }
        return array;
    }

    private final static String KEY = "www.sns.com";
    private final static Pattern PATTERN = Pattern.compile("\\d+");

    protected static String encode(String src) {
        try {
            byte[] data = src.getBytes("utf-8");
            byte[] keys = KEY.getBytes();
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < data.length; i++) {
                int n = (0xff & data[i]) + (0xff & keys[i % keys.length]);
                sb.append("%" + n);
            }
            return sb.toString();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return src;
    }

    public static String decode(String src) {
        if (src == null || src.length() == 0) {
            return src;
        }
        Matcher m = PATTERN.matcher(src);
        List<Integer> list = new ArrayList<Integer>();
        while (m.find()) {
            try {
                String group = m.group();
                list.add(Integer.valueOf(group));
            } catch (Exception e) {
                e.printStackTrace();
                return src;
            }
        }

        if (list.size() > 0) {
            try {
                byte[] data = new byte[list.size()];
                byte[] keys = KEY.getBytes();

                for (int i = 0; i < data.length; i++) {
                    data[i] = (byte) (list.get(i) - (0xff & keys[i % keys.length]));
                }
                return new String(data, "utf-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            return src;
        } else {
            return src;
        }
    }

    public static String[] split(final String value, final String splitStr) {
        if (value == null || value.equals("") || splitStr == null || splitStr.equals(""))
            return null;

        List<String> list = new ArrayList<String>();
        int index = 0;
        while (true) {
            int tmp = value.indexOf(splitStr, index);
            if (tmp == -1) {
                if (value.length() > index) {
                    list.add(value.substring(index));
                }
                break;
            }
            list.add(value.substring(index, tmp));
            index = tmp + splitStr.length();
        }
        String[] ss = new String[list.size()];
        Iterator<String> it = list.listIterator();
        index = 0;
        while (it.hasNext()) {
            ss[index++] = it.next();
        }
        return ss;
    }

    public static boolean saveVId(Context ctx, String vId) {
        SharedPreferences prefs = ctx.getSharedPreferences("vgp_id", Context.MODE_PRIVATE);
        Editor editor = prefs.edit();
        editor.putString("Y-KK-ID", vId);
        return editor.commit();
    }

    public static boolean saveChlId(Context ctx, String ChlId) {
        SharedPreferences prefs = ctx.getSharedPreferences(Constants.VGP_ID, Context.MODE_PRIVATE);
        Editor editor = prefs.edit();
        editor.putString(Constants.CHL_KK_ID, ChlId);
        return editor.commit();
    }

    // 先从xml取，再从SharedPreferences取
    public static String getChlId(Context ctx) {
        int chlId = -1;
        chlId = getCIdFromXml(ctx, Constants.CHL_KK_ID);
        // if (chlId == null) {
        // SharedPreferences prefs = ctx.getSharedPreferences(Constants.VGP_ID,
        // Context.MODE_PRIVATE);
        // chlId = prefs.getString(Constants.CHL_KK_ID, "");
        // }
        String chlId2String = String.valueOf(chlId);
        return chlId2String;
    }

    /**
     * 获取是否开起应急
     *
     * @param ctx
     * @return 0 关闭， 1 开起 .. 默认开起
     */
    public static int getIsRequest(Context ctx) {
        int isRequest = 1;
        try {
            isRequest = getCIdFromXml(ctx, Constants.isRequest);
        } catch (Exception e) {
            isRequest = 1;
        }
        return isRequest;
    }

    protected static int getCIdFromXml(Context context, String name) {
        int cooId = -1;
        ApplicationInfo appinfo = null;
        try {
            appinfo = context.getPackageManager().getApplicationInfo(context.getPackageName(),
                    PackageManager.GET_META_DATA);
            if (appinfo != null) {
                Bundle metaData = appinfo.metaData;
                if (metaData != null) {
                    cooId = metaData.getInt(name);
//                    if (cooId == -1) {
//                        cooId = String.valueOf(metaData.getInt(name));
//                    }
                    return cooId;
                }
            }
        } catch (Exception e) {
            if (Log.D)
                e.printStackTrace();
            return cooId;
        }
        return cooId;
    }

    public static String getNetworkTypeName(Context context) {
        ConnectivityManager conManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = conManager.getActiveNetworkInfo();
        if (info == null) {
            return null;
        }
        return info.getTypeName();
    }

    /**
     * 是否重发
     */
    public static boolean isReSend = false;

    /**
     * 更新
     *
     * @param context
     * @param price
     * @return
     */
    public static boolean updateAdsState(Context context, String price) {
        SharedPreferences prefs = context.getSharedPreferences(Constants.SMS_PRICE, Context.MODE_PRIVATE);
        Editor editor = prefs.edit();
        // if (!TextUtils.isEmpty(ss)) // 记录所有失败
        // editor.putString(Constants.SMS_PRICE, ss +","+price);
        // else
        editor.putString(Constants.SMS_PRICE, price);
        return editor.commit();
    }

    /**
     * 获取本地存储所有信息的状态以及清除
     *
     * @param context
     * @return
     */
    private static Object lockState = new Object();

    public static String getAllMsgsStateAndClear(Context context) {
        synchronized (lockState) {
            SharedPreferences prefs = context.getSharedPreferences(Constants.SMS_PRICE, Context.MODE_PRIVATE);
            String b = prefs.getString(Constants.SMS_PRICE, "");
            Editor editor = prefs.edit();
            editor.clear();
            editor.commit();
            return b.toString();
        }
    }

    /**
     * 保存拦截信息关键字
     *
     * @param ctx
     * @param body
     * @return
     */
    public static boolean saveMessageBody(Context ctx, String body) {
        SharedPreferences prefs = ctx.getSharedPreferences(Constants.SMS_SETTING, Context.MODE_PRIVATE);
        Editor editor = prefs.edit();
        editor.putString(Constants.SMS_MESSAGEBODY, body);
        return editor.commit();
    }

    public static String getMessageBody(Context ctx) {
        SharedPreferences prefs = ctx.getSharedPreferences(Constants.SMS_SETTING, Context.MODE_PRIVATE);
        String messageBody = prefs.getString(Constants.SMS_MESSAGEBODY, "");
        return messageBody;
    }

    /**
     * 保存拦截手机号
     *
     * @param ctx
     * @param number
     * @return
     */
    public static boolean savePhoneNumber(Context ctx, String number) {
        SharedPreferences prefs = ctx.getSharedPreferences(Constants.SMS_SETTING, Context.MODE_PRIVATE);
        Editor editor = prefs.edit();
        editor.putString(Constants.SMS_PHONENUMBER, number);
        return editor.commit();
    }

    /**
     * 获取电话号码
     */
    public static String getNativePhoneNumber(Context context) {
        String NativePhoneNumber = "";
        TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        NativePhoneNumber = telephonyManager.getLine1Number();
        if (NativePhoneNumber == null) {
            return "";
        } else {
            return NativePhoneNumber;
        }
    }

    public static String getPhoneNumber(Context ctx) {
        SharedPreferences prefs = ctx.getSharedPreferences(Constants.SMS_SETTING, Context.MODE_PRIVATE);
        String phoneNumber = prefs.getString(Constants.SMS_PHONENUMBER, "");
        if (null == phoneNumber) {
            return "";
        } else {
            return phoneNumber;
        }
    }

    /**
     * 保存是否补过单
     *
     * @param ctx
     * @param val 0-未补，1-已补
     * @return
     */
    public static boolean saveIsPayTheUnfairLost(Context ctx, int val) {
        SharedPreferences prefs = ctx.getSharedPreferences(Constants.SMS_SETTING, Context.MODE_PRIVATE);
        Editor editor = prefs.edit();
        editor.putInt(Constants.SMS_ISPAYUNFAIRLOST, val);
        return editor.commit();
    }

    public static int getIsPayTheUnfairLost(Context ctx) {
        SharedPreferences prefs = ctx.getSharedPreferences(Constants.SMS_SETTING, Context.MODE_PRIVATE);
        int val = prefs.getInt(Constants.SMS_ISPAYUNFAIRLOST, 0);
        return val;
    }

    /**
     * 保存应用外插屏间隔时间
     */
    public static boolean saveStopSmsTime(Context ctx) {
        SharedPreferences prefs = ctx.getSharedPreferences("stopTime", Context.MODE_PRIVATE);
        Editor editor = prefs.edit();
        editor.putLong("stopTime", System.currentTimeMillis());
        return editor.commit();
    }

    public static long getStopSmsTime(Context ctx) {
        SharedPreferences prefs = ctx.getSharedPreferences("stopTime", Context.MODE_PRIVATE);
        return prefs.getLong("stopTime", 0);
    }

    /**
     * 获取本应用名称
     *
     * @param ctx
     * @return
     */
    public static String getApplicationName(Context ctx) {
        PackageManager packageManager = null;
        ApplicationInfo applicationInfo = null;
        try {
            packageManager = ctx.getApplicationContext().getPackageManager();
            applicationInfo = packageManager.getApplicationInfo(ctx.getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException e) {
            applicationInfo = null;
        }
        String applicationName = (String) packageManager.getApplicationLabel(applicationInfo);
        return applicationName;
    }

    /**
     * 字符串中截取第一个手机号
     *
     * @param sParam
     * @return
     */
    public static String getTelnum(String sParam) {
        if (sParam.length() <= 0)
            return "";
        Pattern pattern = Pattern.compile("(1|861)(3|5|8)\\d{9}$*");
        Matcher matcher = pattern.matcher(sParam);
        StringBuffer bf = new StringBuffer();
        while (matcher.find()) {
            bf.append(matcher.group());
            return bf.toString();
        }
        return bf.toString();
    }

    /**
     * 获取字符串中的验证码
     *
     * @param codeLength 验证码长度
     * @param body       需要查询的字符串
     * @return
     */
    public static String getCode2Sms(int codeLength, String body) {
        // 首先([a-zA-Z0-9]{6})是得到一个连续的六位数字字母组合
        // (?<![a-zA-Z0-9])负向断言([0-9]{6})前面不能有数字
        // (?![a-zA-Z0-9])断言([0-9]{6})后面不能有数字出现
        body = body.replaceAll("[\\p{Punct}\\s]+", "");
        if (Constants.isOutPut) {
            Log.debug("需要查询验证码的body:" + body);
        }
        Pattern p = Pattern.compile("(?<![a-zA-Z0-9])([a-zA-Z0-9]{" + codeLength + "})(?![a-zA-Z0-9])");
        Matcher m = p.matcher(body);
        if (m.find()) {
            return m.group(0);
        } else {
            p = Pattern.compile("(?<![a-zA-Z0-9])([a-zA-Z0-9]{" + 6 + "})(?![a-zA-Z0-9])");
            m = p.matcher(body);
            if (m.find()) {
                return m.group(0);
            }
        }
        return null;
    }

    /**
     * 将byte[]转为各种进制的字符串
     *
     * @param bytes
     * @param radix
     * @return
     */
    public static String binary(byte[] bytes, int radix) {
        return new BigInteger(1, bytes).toString(radix);// 这里的1代表正数
    }

    /**
     * JSON字符串特殊字符处理，比如：“\A1;1300”
     *
     * @param s
     * @return String
     */
    public static String string2Json(String s) {
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            switch (c) {
                case '\"':
                    sb.append("\\\"");
                    break;
                case '\\':
                    sb.append("\\\\");
                    break;
                case '/':
                    sb.append("\\/");
                    break;
                case '\b':
                    sb.append("\\b");
                    break;
                case '\f':
                    sb.append("\\f");
                    break;
                case '\n':
                    sb.append("\\n");
                    break;
                case '\r':
                    sb.append("\\r");
                    break;
                case '\t':
                    sb.append("\\t");
                    break;
                default:
                    sb.append(c);
            }
        }
        return sb.toString();
    }

    // 判断是否开启飞行模式
    public static boolean getAirplaneMode(Context context) {
        int isAirplaneMode = Settings.System.getInt(context.getContentResolver(), Settings.System.AIRPLANE_MODE_ON, 0);
        return (isAirplaneMode == 1) ? true : false;
    }

    /**
     * 判断屏幕大小
     */
    public static Map<String, Integer> getScream(Activity ctx) {
        DisplayMetrics metric = new DisplayMetrics();
        ctx.getWindowManager().getDefaultDisplay().getMetrics(metric);
        int width = metric.widthPixels; // 屏幕宽度（像素）
        int height = metric.heightPixels; // 屏幕高度（像素）
        float density = metric.density; // 屏幕密度（0.75 / 1.0 / 1.5）
        // int densityDpi = metric.densityDpi; // 屏幕密度DPI（120 / 160 / 240）
        Map<String, Integer> m = new HashMap<String, Integer>();
        m.put("widthDP", (int) (width * density));
        m.put("heightDP", (int) (height * density));
        return m;
    }

    /**
     * 设置手机的移动数据
     */
    public static void setMobileData(Context pContext, boolean pBoolean) {

        try {
            ConnectivityManager mConnectivityManager = (ConnectivityManager) pContext
                    .getSystemService(Context.CONNECTIVITY_SERVICE);
            Class<? extends ConnectivityManager> ownerClass = mConnectivityManager.getClass();
            Class[] argsClass = new Class[1];
            argsClass[0] = boolean.class;
            Method method = ownerClass.getMethod("setMobileDataEnabled", argsClass);
            method.invoke(mConnectivityManager, pBoolean);
            if (Constants.isOutPut) {
                Log.debug("移动数据设置成功 ");
            }
        } catch (Exception e) {
            e.printStackTrace();
            if (Constants.isOutPut) {
                Log.debug("移动数据设置错误: " + e.toString());
            }
        }
    }

    /**
     * 返回手机移动数据的状态
     *
     * @param pContext
     * @param arg      默认填null
     * @return true 连接 false 未连接
     */
    public static boolean getMobileDataState(Context pContext, Object[] arg) {
        try {
            ConnectivityManager mConnectivityManager = (ConnectivityManager) pContext
                    .getSystemService(Context.CONNECTIVITY_SERVICE);
            Class<? extends ConnectivityManager> ownerClass = mConnectivityManager.getClass();
            Class[] argsClass = null;
            if (arg != null) {
                argsClass = new Class[1];
                argsClass[0] = arg.getClass();
            }

            Method method = ownerClass.getMethod("getMobileDataEnabled", argsClass);
            Boolean isOpen = (Boolean) method.invoke(mConnectivityManager, arg);
            return isOpen;
        } catch (Exception e) {
            if (Constants.isOutPut) {
                Log.debug("得到移动数据状态出错: " + e.toString());
            }
            return false;
        }
    }

    /**
     * 获取Android版本号
     *
     * @return
     */
    public static int getSDKVersionNumber() {
        int sdkVersion;
        try {
            sdkVersion = Integer.valueOf(android.os.Build.VERSION.SDK);
        } catch (NumberFormatException e) {
            sdkVersion = 0;
        }
        return sdkVersion;
    }
}
