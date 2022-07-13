package com.vip.plugindemo;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.IntentFilter;
import android.content.pm.ActivityInfo;
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
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    File file = new File(path);
                    Log.e(TAG, "File "+file.getAbsolutePath());
                    if (!file.exists()) {
                        Log.e(TAG, "插件不存在");
                        return;
                    }
                    File pluginDir = context.getDir("plugin", Context.MODE_PRIVATE);
                    //加载插件的class
                    dexClassLoader = new DexClassLoader(path, pluginDir.getAbsolutePath(), null, context.getClassLoader());
                    //加载插件的资源文件
                    //1、获取插件的AssetManager
                    AssetManager pluginAssetManager = AssetManager.class.newInstance();
                    Method addAssetPath = AssetManager.class.getMethod("addAssetPath", String.class);
                    addAssetPath.setAccessible(true);
                    addAssetPath.invoke(pluginAssetManager, path);
                    //2、获取宿主的Resources
                    Resources appResources = context.getResources();
                    //实例化插件的Resources
                    pluginResource = new Resources(pluginAssetManager, appResources.getDisplayMetrics(), appResources.getConfiguration());
                    if (dexClassLoader != null && pluginResource != null) {
                        dexClassLoader.loadClass("com.ciot.plugin.Plugin1Activity");
                        handler.sendEmptyMessage(666);
                    } else {
                        handler.sendEmptyMessage(0);
                    }
                }catch (Exception e) {
                    e.printStackTrace();
                    handler.sendEmptyMessage(0);
                }
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
