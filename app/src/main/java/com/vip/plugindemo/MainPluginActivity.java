package com.vip.plugindemo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import java.io.File;

public class MainPluginActivity extends AppCompatActivity {
    private String path = "/sdcard/plugin-debug.apk";
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            if (msg.what == 666) {
                Log.d("vip", "加载插件成功！");
            } else if (msg.what == 0) {
                Log.d("vip", "加载插件失败，请检查插件是否存在！");
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_plugin);
        findViewById(R.id.btn_test).setOnClickListener(v -> {
            //获取插件包的Activity
            PackageManager packageManager = getPackageManager();
            PackageInfo packageArchiveInfo = packageManager.getPackageArchiveInfo(path, PackageManager.GET_ACTIVITIES);
            //获取在manifest文件中注册的第一个activity
            ActivityInfo activity = packageArchiveInfo.activities[0];
            Intent intent = new Intent(this, ProxyActivity.class);
            intent.putExtra("className", activity.name);
            Log.d("activity.name", activity.name);
             startActivity(intent);
        });
        PluginManager.getInstance(this).loadPlugin(handler, path);
    }


}