package com.ciot.plugin;

import android.os.Bundle;
import android.util.Log;

import com.vip.lib_transfer.BaseActivity;

public class Plugin1Activity extends BaseActivity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("vip", "onCreate " + R.layout.activity_plugin1);
        appActivity.setContentView(R.layout.activity_plugin1);
    }
}