package com.supers.clean.junk.presenter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;

import com.supers.clean.junk.view.AppManagerView;
import com.supers.clean.junk.activity.MyApplication;
import com.supers.clean.junk.entity.JunkInfo;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Created by on 2017/3/2.
 */

public class ManagerPresenter extends BasePresenter<AppManagerView> {
    public static final int SIZE_TYPE = 0;
    public static final int TIME_TYPE = 1;
    public static final int PINLV_TYPE = 2;
    private MyApplication cleanApplication;
    private long allSize;
    private long cleanSize = 0;
    private ArrayList<JunkInfo> clearList;
    private ArrayList<JunkInfo> list_size, list_time, list_pinlv;
    private int type;

    public ManagerPresenter(AppManagerView iView, Context context) {
        super(iView, context);
        cleanApplication = (MyApplication) context.getApplicationContext();
    }

    @Override
    public void init() {
        super.init();

        allSize = cleanApplication.getRamSize();
        list_size = new ArrayList<>();
        list_time = new ArrayList<>();
        list_pinlv = new ArrayList<>();
        list_size.addAll(cleanApplication.getListMng());
        list_time.addAll(cleanApplication.getListMng());
        list_pinlv.addAll(cleanApplication.getListMng());
        Collections.sort(list_size, new Sizesort());
        Collections.sort(list_time, new Timesort());
        Collections.sort(list_pinlv, new LastRunsort());
        iView.initData(cleanSize);
        iView.onClick();
        iView.loadFullAd();
    }

    public void addCleandata(boolean isAdd, long size) {
        if (isAdd) {
            cleanSize += size;
        } else {
            cleanSize -= size;
        }
        iView.setCleanDAta(cleanSize);

    }

    public void addAdapterData() {
        iView.updateAdapter(list_size, list_time, list_pinlv);
    }


    public void bleachFile(List<JunkInfo> appManager) {
        clearList = new ArrayList<>();
        for (JunkInfo softInfo : appManager) {
            if (softInfo.isChecked) {
                clearList.add(softInfo);
                Intent intent = new Intent(Intent.ACTION_DELETE);
                intent.setData(Uri.parse("package:" + softInfo.packageName));
                context.startActivity(intent);
            }
        }

    }

    public void unloadSuccess(String packageName) {
        if (clearList == null || clearList.size() == 0) {
            return;
        }
        for (JunkInfo softinfo : clearList) {
            if (softinfo.packageName.equals(packageName)) {
                cleanApplication.removeAppManager(softinfo);
                list_size.remove(softinfo);
                list_time.remove(softinfo);
                list_pinlv.remove(softinfo);
                addCleandata(false, softinfo.size);
                iView.updateAdapter(list_size, list_time, list_pinlv);
            }
        }
    }

}

class Sizesort implements Comparator<JunkInfo> {
    public int compare(JunkInfo file1, JunkInfo file2) {
        //大的在上面
        return file1.size == file2.size ? 0 : (file1.size > file2.size ? -1 : 1);
    }
}

class Timesort implements Comparator<JunkInfo> {
    public int compare(JunkInfo file1, JunkInfo file2) {
        return file1.time == file2.time ? 0 : (file1.time > file2.time ? -1 : 1);
    }
}

class LastRunsort implements Comparator<JunkInfo> {
    public int compare(JunkInfo file1, JunkInfo file2) {
        //xiao的在上面
        return file1.lastRunTime == file2.lastRunTime ? 0 : (file1.lastRunTime < file2.lastRunTime ? -1 : 1);
    }
}