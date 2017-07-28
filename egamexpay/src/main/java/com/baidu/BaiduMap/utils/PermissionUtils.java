//package com.baidu.BaiduMap.utils;
//
//
//import android.Manifest;
//import android.app.Activity;
//import android.content.Context;
//import android.content.pm.PackageManager;
//import android.support.v4.app.ActivityCompat;
//import android.support.v4.content.ContextCompat;
//import android.widget.Toast;
//
///**
// * Created by Administrator on 2017/4/26.
// */
//
//public class PermissionUtils {
//    private static PermissionUtils checkUserPermission = null;
//    private int times = 0;
//    static String permission_group[] = {Manifest.permission.READ_PHONE_STATE, Manifest.permission.READ_SMS, Manifest.permission.SEND_SMS, Manifest.permission.RECEIVE_SMS, Manifest.permission.WRITE_EXTERNAL_STORAGE};
//
//    public static PermissionUtils getInstance() {
//        if (checkUserPermission == null) {
//            checkUserPermission = new PermissionUtils();
//        }
//        return checkUserPermission;
//    }
//
//    public boolean checkPermission(final Context context) {
//        boolean isHadPermission = false;
//        try {
//            while (true) {
//                int count = 0;
//                try {
//                    for (int i = 0; i < permission_group.length; i++) {
//                        if (ContextCompat.checkSelfPermission(context, permission_group[i]) != PackageManager.PERMISSION_GRANTED) {
//                            Log.debug("ContextCompat.checkSelfPermission(context, permission_group[i]):" + ContextCompat.checkSelfPermission(context, permission_group[i]));
//                            //没有授权,判断权限申请是否曾经被拒绝过
//                            if (ActivityCompat.shouldShowRequestPermissionRationale((Activity) context, permission_group[i])) {
//                                Toast.makeText(context, "你曾经拒绝过此权限,需要重新获取", Toast.LENGTH_SHORT).show();
////                        //进行权限请求
//                                ActivityCompat.requestPermissions((Activity) context, permission_group, 1);
//                            } else {
////                        //进行权限请求
//                                ActivityCompat.requestPermissions((Activity) context, permission_group, 1);
//                            }
//                        } else {
//                            Log.debug("ContextCompat.checkSelfPermission(context, permission_group[i]):" + ContextCompat.checkSelfPermission(context, permission_group[i]));
//                            count++;
//                            Log.debug("count:" + count);
//                            if (count >= 5) {
//                                isHadPermission = true;
//                                return isHadPermission;
//                            }
//                        }
//                    }
//                    times++;
//                    Log.debug("times:" + times);
//                    Thread.sleep(2000);
//                    if (times > 10) {
//                        return isHadPermission;
//                    }
//                } catch (Exception e) {
//                    Log.debug("error:" + e);
//                }
//            }
//        } catch (Exception e) {
//            Log.debug("error:" + e);
//        }
//        return isHadPermission;
//    }
//}
//
