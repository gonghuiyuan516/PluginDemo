package com.vip.plugindemo;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.IntentFilter;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.os.Handler;
import android.util.Log;

import java.io.File;
import java.lang.reflect.Method;

import dalvik.system.DexClassLoader;

public class PluginManager {
    private static final String TAG = PluginManager.class.getSimpleName();
    private static PluginManager instance;
    private Context context;
    private DexClassLoader dexClassLoader;
    private Resources pluginResource;

    private PluginManager(Context context) {
        this.context = context;
    }

    public static PluginManager getInstance(Context context) {
        if (instance == null) {
            synchronized (PluginManager.class) {
                if (instance == null) {
                    instance = new PluginManager(context);
                }
            }
        }
        return instance;
    }

    public void loadPlugin(final Handler handler, final String path) {
        new Thread(() -> {
            try {
                File file = new File(path);
                Log.e(TAG, "File " + file.getAbsolutePath());
                if (!file.exists()) {
                    Log.e(TAG, "插件不存在");
                    return;
                }
                File pluginDir = context.getDir("plugin", Context.MODE_PRIVATE);
                //加载插件的class
                dexClassLoader = new DexClassLoader(path, pluginDir.getAbsolutePath(), null, context.getClassLoader());

                pluginResource = CreateResourceBloc.INSTANCE.create(path,context);

                if (dexClassLoader != null) {
                    handler.sendEmptyMessage(666);
                } else {
                    handler.sendEmptyMessage(0);
                }
            } catch (Exception e) {
                e.printStackTrace();
                handler.sendEmptyMessage(0);
            }
        }).start();

    }

    public Resources getResource() {
        return pluginResource;
    }

    public DexClassLoader getClassLoader() {
        return dexClassLoader;
    }
}
