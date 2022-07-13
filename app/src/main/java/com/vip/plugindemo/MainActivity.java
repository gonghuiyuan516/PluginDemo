package com.vip.plugindemo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

import dalvik.system.DexClassLoader;
import dalvik.system.PathClassLoader;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViewById(R.id.btn_plugin_test).setOnClickListener(view -> {
            mergeDex();
            invokeTest();
        });

//        PackageManager packageManager = context.getPackageManager();
//        PackageInfo packageArchiveInfo = packageManager.getPackageArchiveInfo(
//                loadPath,
//                PackageManager.GET_ACTIVITIES
//                        | PackageManager.GET_META_DATA
//                        | PackageManager.GET_SERVICES
//                        | PackageManager.GET_PROVIDERS
//                        | PackageManager.GET_SIGNATURES
//        );
//        packageArchiveInfo.applicationInfo.sourceDir = loadPath;
//        packageArchiveInfo.applicationInfo.publicSourceDir = loadPath;
//
//        try {
//            mResources = packageManager.getResourcesForApplication(packageArchiveInfo.applicationInfo);
//        } catch (PackageManager.NameNotFoundException e) {
//            // ...
//        }
    }

    public void mergeDex() {
        try {
            //获取BaseDexClassLoader中的pathList（DexPathList）
            Class<?> clazz = Class.forName("dalvik.system.BaseDexClassLoader");
            Field pathListField = clazz.getDeclaredField("pathList");
            pathListField.setAccessible(true);

            //获取DexPathList中的dexElements数组
            Class<?> dexPathListClass = Class.forName("dalvik.system.DexPathList");
            Field dexElementsField = dexPathListClass.getDeclaredField("dexElements");
            dexElementsField.setAccessible(true);

            //宿主的类加载器
            ClassLoader pathClassLoader = this.getClassLoader();
            //DexPathList类的对象
            Object hostPathList = pathListField.get(pathClassLoader);
            //宿主的dexElements
            Object[] hostDexElements = (Object[]) dexElementsField.get(hostPathList);

            String apkPath = "/sdcard/output.dex";
            //插件的类加载器
            ClassLoader dexClassLoader = new DexClassLoader(apkPath
                    ,this.getCacheDir().getAbsolutePath()
                    ,null
                    ,pathClassLoader);

            //DexPathList类的对象
            Object pluginPathList = pathListField.get(dexClassLoader);
            //宿主的dexElements
            Object[] pluginElements = (Object[]) dexElementsField.get(pluginPathList);


            //创建一个新数组
            Object[] newDexElements = (Object[]) Array.newInstance(hostDexElements.getClass().getComponentType()
                    ,hostDexElements.length + pluginElements.length);
            System.arraycopy(hostDexElements,0,newDexElements,0,hostDexElements.length);
            System.arraycopy(pluginElements,0,newDexElements,hostDexElements.length,pluginElements.length);

            //赋值
            dexElementsField.set(hostPathList,newDexElements);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void invokeTest() {
        
        try {
            Class<?> testClass = Class.forName("com.ciot.plugin.Test");
            Method printMethod = testClass.getMethod("print");
            printMethod.invoke(null);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}