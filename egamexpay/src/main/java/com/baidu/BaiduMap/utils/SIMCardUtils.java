//package com.baidu.BaiduMap.utils;
//
//import android.Manifest;
//import android.app.Activity;
//import android.content.ContentResolver;
//import android.content.Context;
//import android.content.pm.PackageManager;
//import android.database.Cursor;
//import android.net.Uri;
//import android.support.v4.app.ActivityCompat;
//import android.support.v4.content.ContextCompat;
//import android.telephony.TelephonyManager;
//
//import java.lang.reflect.Method;
//
//import static android.content.Context.TELEPHONY_SERVICE;
//
///**
// * Created by Administrator on 2017/4/27.
// */
//
//public class SIMCardUtils {
//
//    static int times = 0;
//    static int simCardID1 = -1;
//    static int simCardID2 = -1;
//
//    public static int checkSIMCard(Context ctx) {
//        while (true) {
//            try {
//                if (ContextCompat.checkSelfPermission(ctx, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
//                    ActivityCompat.requestPermissions((Activity) ctx, new String[]{Manifest.permission.READ_PHONE_STATE}, 1);
//                    Thread.sleep(2000);
//                    times++;
//                    if (times >= 10) {
//                        return -10;
//                    }
//                } else {
//                    simCardID1 = getCardinfo(0, ctx);
//                    simCardID2 = getCardinfo(1, ctx);
//                    String imsi1 = getIMSI(ctx, simCardID1);
//                    String imsi2 = getIMSI(ctx, simCardID2);
//                    //if imsi2 equals -100 means no simCard or only one simCard
//                    if (imsi2.equals("-100")) {
//                        if (imsi1.equals("-100")) {
//                            return -9;
//                        } else {
//                            return 1;
//                        }
//                    } else {
//                        //double simCard
//                        return 2;
//                    }
//                }
//            } catch (Exception e) {
//                Log.debug("eeee:?" + e);
//                return -9;
//            }
//        }
//    }
//
//
//    private static int getCardinfo(int simId, Context context) {
//        Uri uri = Uri.parse("content://telephony/siminfo");
//        Cursor cursor = null;
//        ContentResolver contentResolver = context.getContentResolver();
//        try {
//            cursor = contentResolver.query(uri, new String[]{"_id", "sim_id"}, "sim_id = ?", new String[]{String.valueOf(simId)}, null);
//            if (null != cursor) {
//                if (cursor.moveToFirst()) {
//                    Log.debug("=============>_id:" + cursor.getString(cursor.getColumnIndex("_id")));
//                    return cursor.getInt(cursor.getColumnIndex("_id"));
//                }
//            }
//        } catch (Exception e) {
//
//        } finally {
//            if (null != cursor) {
//                cursor.close();
//            }
//        }
//        return -1;
//    }
//
//    private static String getIMSI(Context context, int cardID) {
//        if (cardID == -1) {
//            return "-100";
//        }
//        String imsi = "-100";
//        try {
//            TelephonyManager manager = (TelephonyManager) context.getSystemService(TELEPHONY_SERVICE);
//            Class clazz = manager.getClass();
//            Method getImei = clazz.getDeclaredMethod("getImei", int.class);
//            Method getImsi = clazz.getDeclaredMethod("getSubscriberId", int.class);
//            //获得IMEI 1的信息：
//            Log.debug("=======>获得getImsi 0的信息:" + getImsi.invoke(manager, cardID));
//            //获得IMEI 1的信息：
//            Log.debug("=======>获得IMEI 0的信息:" + getImei.invoke(manager, cardID));
//            imsi = (String) getImsi.invoke(manager, cardID);
//        } catch (Exception e) {
//            Log.debug("=======>Exception:" + e);
//        }
//        return imsi;
//    }
//}
