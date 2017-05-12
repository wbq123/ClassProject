package com.elife.classproject.application;

import android.app.Application;
import android.content.Context;
import android.util.Log;

import com.baidu.mapapi.SDKInitializer;
import com.elife.classproject.player.SongModel;
import com.elife.classproject.sqlite.DatabaseHelper;
import com.squareup.leakcanary.LeakCanary;
import com.squareup.leakcanary.RefWatcher;

import java.util.List;

/**
 * Created by tzhang on 2016/9/2.
 */
public class MyApplication extends Application {

    public static final MyApplication MY_APPLICATION = new MyApplication();
    private static DatabaseHelper sDataBaseHelper;
    private static List<SongModel> sSongList;
    private int i = 0;

    public static DatabaseHelper getDbInstance() {
        return sDataBaseHelper;
    }

    public static List<SongModel> getSongList() {
        return sSongList;
    }

    public static void setSongList(List<SongModel> songList) {
        sSongList = songList;
    }

    // 使用 RefWatcher 监控那些本该被回收的对象  refWatcher.watch(schrodingerCat);
    private RefWatcher refWatcher;

    public static RefWatcher getRefWatcher(Context context) {
        MyApplication application = (MyApplication) context.getApplicationContext();
        return application.refWatcher;
    }

    // 当前应用程序的初始化操作
    @Override
    public void onCreate() {
        super.onCreate();
        Log.d("------------", "MyApplication");

        if (LeakCanary.isInAnalyzerProcess(this)) {
            // This process is dedicated to LeakCanary for heap analysis.
            // You should not init your app in this process.
            return;
        }

        //LeakCanary.install() 会返回一个预定义的 RefWatcher，同时也会启用一个 ActivityRefWatcher，用于自动监控调用 Activity.onDestroy() 之后泄露的 activity
        refWatcher = LeakCanary.install(this);

        sDataBaseHelper = DatabaseHelper.getInstance(this);// 能放到线程里面就放到线程里面
        SDKInitializer.initialize(getApplicationContext());
    }

    public int getI() {
        i++;
        return i;
    }

    public void setI(int i) {
        this.i = i;
    }

}
