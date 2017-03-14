package com.eos.manager;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;

import com.eos.gallery.view.TileBitmapDrawable;
import com.eos.manager.db.SecurityPreference;
import com.eos.manager.lib.BaseApp;
import com.eos.manager.lib.datatype.SDataType;
import com.eos.manager.lib.io.ImageMaster;
import com.eos.manager.asyncmanager.SecurityImgManager;
import com.eos.manager.meta.MApps;
import com.eos.manager.meta.SecuritProfiles;
import com.eos.manager.meta.SecurityMyPref;
import com.squareup.leakcanary.LeakCanary;
import com.squareup.leakcanary.RefWatcher;
//import com.squareup.leakcanary.LeakCanary;
//import com.squareup.leakcanary.RefWatcher;

import java.util.Locale;

/**
 * Created by SongHualin on 5/6/2015.
 */
public class App extends BaseApp{

    @Override
    public void onCreate() {
        super.onCreate();

        if (requireEarlyReturn()) {
            return;
        }

        watcher = LeakCanary.install(this);
        SDataType.init(this);

        if (SecurityMyPref.isEnglish()){
            if (getResources().getConfiguration().locale != Locale.ENGLISH){
                Configuration cfg = getResources().getConfiguration();
                    cfg.locale = Locale.ENGLISH;
                getResources().updateConfiguration(cfg, getResources().getDisplayMetrics());
            }
        }
        AppsCore.init(this, SecurityImgManager.ROOT);
        ImageManager.initialize(this);
        ImageMaster.imageCache = TileBitmapDrawable.initCache(this);
        SecurityImgManager.cache = ImageMaster.imageCache;
        SecurityPreference.initialize(this);
//        Start.start(this);
        startService(new Intent(this, AppLockEosService.class));

        MApps.init();
        SecuritProfiles.init();
    }

    static RefWatcher watcher;

    public static RefWatcher getWatcher() {
        return watcher;
    }

    public static SharedPreferences getSharedPreferences() {
        return App.getContext().getSharedPreferences("cf", MODE_MULTI_PROCESS);
    }
}
