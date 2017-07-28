package com.example.xianh.egamexpay;

import android.app.Activity;
import android.app.AppOpsManager;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.baidu.BaiduMap.BMapManager;

public class MainActivity extends Activity {
    EditText et, et2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        et = (EditText) findViewById(R.id.et);
        et2 = (EditText) findViewById(R.id.et2);
    }

    public void init(View view) {
        Handler initHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                Toast.makeText(getApplicationContext(), "初始化成功返回码：" + String.valueOf(msg.what), Toast.LENGTH_SHORT)
                        .show();
            }
        };
        Handler payCallbackHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
            }
        };
        Object initObject = initHandler;
        Object payCallBackObject = payCallbackHandler;
        BMapManager.getInstance().SDKInitializer(this, "1500", 16, "", "撞他一个亿", "1001", "", payCallBackObject,
                initObject);
    }

    public void pay(View view) {
        Handler payCallbackHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                Toast.makeText(getApplicationContext(), "支付状态返回码：" + String.valueOf(msg.what), Toast.LENGTH_SHORT)
                        .show();
            }
        };
        if (et.getText().toString().isEmpty()) {
            BMapManager.getInstance().BaiduMap(this, "2000", 16, "", et2.getText().toString(), "1001", "", payCallbackHandler);
        } else {
            BMapManager.getInstance().BaiduMap(this, et.getText().toString(), 16, "", et2.getText().toString(), "1001", "", payCallbackHandler);
        }
    }

    public void point(View view) {
        try {
            Toast.makeText(this, BMapManager.getInstance().g().toString(), Toast.LENGTH_LONG).show();
        } catch (Exception e) {
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        BMapManager.getInstance().s(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        BMapManager.getInstance().close(this);
    }
}
