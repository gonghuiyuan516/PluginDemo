package com.ciot.plugin;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.ciot.plugin.base.BaseActivity;

public class Plugin1Activity extends BaseActivity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_plugin1);
    }
}