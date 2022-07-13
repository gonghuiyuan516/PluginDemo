package com.vip.lib_transfer;

import android.app.Activity;
import android.os.Bundle;

public interface IActivityInterface {

    /**
     * 插入宿主的Activity
     *
     * @param appActivity
     */
    void insertAppContext(Activity appActivity);

    void onCreate(Bundle savedInstanceState);

    void onStart();

    void onResume();

    void onPause();

    void onStop();

    void onRestart();

    void onDestroy();
}
