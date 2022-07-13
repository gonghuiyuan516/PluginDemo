package com.vip.plugindemo;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.Nullable;

import com.vip.lib_transfer.IActivityInterface;

import java.lang.reflect.Constructor;

public class ProxyActivity extends Activity {

    private IActivityInterface pluginActivity1;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //真正的加载插件里面的Activity
        String className = getIntent().getStringExtra("className");
        try {
            Log.d("vip",className);

            Class<?> pluginActivity1Clazz = getClassLoader().loadClass(className);
            Constructor<?> constructor = pluginActivity1Clazz.getConstructor(new Class[]{});
            pluginActivity1 = (IActivityInterface) constructor.newInstance(new Object[]{});
            pluginActivity1.insertAppContext(this);
            Bundle bundle = new Bundle();
            bundle.putString("value", "我是宿主传递过来的字符串");
            pluginActivity1.onCreate(bundle);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    protected void onStart() {
        super.onStart();
        pluginActivity1.onStart();

    }

    @Override
    protected void onResume() {
        super.onResume();
        pluginActivity1.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        pluginActivity1.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
        pluginActivity1.onStop();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        pluginActivity1.onRestart();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        pluginActivity1.onDestroy();
    }

    @Override
    public Resources getResources() {
        return PluginManager.getInstance(this).getResource();
    }

    @Override
    public ClassLoader getClassLoader() {
        return PluginManager.getInstance(this).getClassLoader();
    }

    @Override
    public void startActivity(Intent intent) {
        String className = intent.getStringExtra("className");
        //自己跳自己
        Intent newIntent = new Intent(this, this.getClass());
        newIntent.putExtra("className", className);
        super.startActivity(newIntent);
    }

}