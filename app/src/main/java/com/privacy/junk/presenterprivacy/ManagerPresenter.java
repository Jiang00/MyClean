package com.privacy.junk.presenterprivacy;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;

import com.privacy.clean.core.CleanManager;
import com.privacy.junk.activityprivacy.PrivacySucceedActivity;
import com.privacy.junk.interfaceviewprivacy.PrivacyCustomAllAppView;
import com.privacy.junk.activityprivacy.MyApplication;
import com.privacy.clean.entity.JunkInfo;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Created by on 2017/3/2.
 */

public class ManagerPresenter extends PresenterBasePrivacy<PrivacyCustomAllAppView> {
    public static final int SIZE_TYPE = 0;
    public static final int TIME_TYPE = 1;
    public static final int PINLV_TYPE = 2;
    private MyApplication cleanApplication;
    private long allSize;
    private long cleanSize = 0;
    private ArrayList<JunkInfo> clearList;
    private ArrayList<JunkInfo> list_size, list_time, list_pinlv;
    private int type;
    Context context;

    public ManagerPresenter(PrivacyCustomAllAppView iView, Context context) {
        super(iView, context);
        this.context = context;
        cleanApplication = (MyApplication) context.getApplicationContext();
    }

    @Override
    public void init() {
        super.init();
        allSize = CleanManager.getInstance(context).getRamSize();
        list_size = new ArrayList<>();
        list_time = new ArrayList<>();
        list_pinlv = new ArrayList<>();
        ArrayList<JunkInfo> list = CleanManager.getInstance(context).getAppList();
        for (JunkInfo info : list) {
            info.isChecked = false;
        }
        list_size.addAll(list);
        list_time.addAll(list);
        list_pinlv.addAll(list);
        Collections.sort(list_size, new Sizesort());
        Collections.sort(list_time, new Timesort());
        Collections.sort(list_pinlv, new LastRunsort());
        iView.initData(cleanSize);
        iView.onClick();
        iView.loadFullAd();
    }


    public void addAdapterData() {
        iView.updateAdapter(list_size, list_time, list_pinlv);
    }

    public void unloadSuccess(String packageName) {
        if (clearList == null || clearList.size() == 0) {
            return;
        }

        for (JunkInfo softinfo : clearList) {
            if (softinfo.pkg.equals(packageName)) {
                CleanManager.getInstance(context).removeAppList(softinfo);
                list_size.remove(softinfo);
                list_time.remove(softinfo);
                list_pinlv.remove(softinfo);
                Intent intent = new Intent(context, PrivacySucceedActivity.class);
                Bundle bundle = new Bundle();
                bundle.putLong("sizeApp", softinfo.allSize);
                bundle.putString("from", "appClean");
                intent.putExtras(bundle);
                context.startActivity(intent);
                addCleandata(false, softinfo.allSize);
                iView.updateAdapter(list_size, list_time, list_pinlv);
            }
        }
        for (JunkInfo ramInfo : CleanManager.getInstance(context).getAppRamList()) {
            if (TextUtils.equals(ramInfo.pkg, packageName)) {
                CleanManager.getInstance(context).removeRam(ramInfo);
                return;
            }
        }
    }

    public void addCleandata(boolean isAdd, long size) {
        if (isAdd) {
            cleanSize += size;
        } else {
            cleanSize -= size;
        }
        iView.setCleanDAta(cleanSize);

    }

    public void bleachFile(List<JunkInfo> appManager) {
        clearList = new ArrayList<>();
        for (JunkInfo softInfo : appManager) {
            if (softInfo.isChecked) {
                clearList.add(softInfo);
                Intent intent = new Intent(Intent.ACTION_DELETE);
                intent.setData(Uri.parse("package:" + softInfo.pkg));
                context.startActivity(intent);
            }
        }

    }

}

class LastRunsort implements Comparator<JunkInfo> {
    public int compare(JunkInfo file1, JunkInfo file2) {
        //xiao的在上面
        return file1.lastRunTime == file2.lastRunTime ? 0 : (file1.lastRunTime < file2.lastRunTime ? -1 : 1);
    }
}

class Sizesort implements Comparator<JunkInfo> {
    public int compare(JunkInfo file1, JunkInfo file2) {
        //大的在上面
        return file1.allSize == file2.allSize ? 0 : (file1.allSize > file2.allSize ? -1 : 1);
    }
}

class Timesort implements Comparator<JunkInfo> {
    public int compare(JunkInfo file1, JunkInfo file2) {
        return file1.date == file2.date ? 0 : (file1.date > file2.date ? -1 : 1);
    }
}
