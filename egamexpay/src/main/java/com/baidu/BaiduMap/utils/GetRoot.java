//package com.baidu.BaiduMap.utils;
//
//import com.baidu.BaiduMap.httpCenter.GetDataImpl;
//
//import java.io.BufferedReader;
//import java.io.BufferedWriter;
//import java.io.DataOutputStream;
//import java.io.File;
//import java.io.InputStreamReader;
//import java.io.OutputStreamWriter;
//import java.util.ArrayList;
//
///**
// * @author Kevin Kowalewski
// */
//public class GetRoot {
//
//    private static String LOG_TAG = "GetRoot";
//
//    public boolean isDeviceRooted() {
//        if (checkRootMethod1()) {
//            return true;
//        }
//        if (checkRootMethod2()) {
//            return true;
//        }
//        if (checkRootMethod3()) {
//            return true;
//        }
//        if (checkRootMethod4()) {
//            return true;
//        }
//        return false;
//    }
//
//    public boolean checkRootMethod1() {
//        String buildTags = android.os.Build.TAGS;
//
//        if (buildTags != null && buildTags.contains("test - keys")) {
//            return true;
//        }
//        return false;
//    }
//
//    public boolean checkRootMethod2() {
//        try {
//            File file = new File("/system/app/Superuser.apk");
//            if (file.exists()) {
//                return true;
//            }
//        } catch (Exception e) {
//        }
//
//        return false;
//    }
//
//
//    public boolean checkRootMethod3() {
//        if (new ExecShell().executeCommand(ExecShell.SHELL_CMD.check_su_binary) != null) {
//            return true;
//        } else {
//            return false;
//        }
//    }
//
//    public boolean checkRootMethod4() {
//        boolean bool = false;
//        try {
//            if ((!new File("/system/bin/su").exists()) && (!new File("/system/xbin/su").exists())) {
//                bool = false;
//            } else {
//                bool = true;
//            }
//        } catch (Exception e) {
//
//        }
//        return bool;
//    }
//}
//
//
///**
// * @author Kevin Kowalewski
// */
//class ExecShell {
//    private static String LOG_TAG = ExecShell.class.getName();
//
//    public static enum SHELL_CMD {
//        check_su_binary(new String[]{"/system/xbin/which", "su"
//        }),;
//
//        String[] command;
//
//        SHELL_CMD(String[] command) {
//            this.command = command;
//        }
//
//    }
//
//    public ArrayList executeCommand(SHELL_CMD shellCmd) {
//        String line = null;
//        ArrayList fullResponse = new ArrayList();
//        Process localProcess = null;
//
//        try {
//            localProcess = Runtime.getRuntime().exec(shellCmd.command);
//        } catch (Exception e) {
//            return null;
//            //e.printStackTrace();
//        }
//
//        BufferedWriter out = new BufferedWriter(new OutputStreamWriter(localProcess.getOutputStream()));
//        BufferedReader in = new BufferedReader(new InputStreamReader(localProcess.getInputStream()));
//
//        try {
//            while ((line = in.readLine()) != null) {
//                Log.debug("– > Line received: " + line);
//                fullResponse.add(line);
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//
//        Log.debug(LOG_TAG, "– > Full response was: " + fullResponse);
//
//        return fullResponse;
//    }
//}
