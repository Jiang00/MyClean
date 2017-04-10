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
    private ArrayList<JunkInfo> list;
    private int type;

    public ManagerPresenter(AppManagerView iView, Context context) {
        super(iView, context);
        cleanApplication = (MyApplication) context.getApplicationContext();
    }

    @Override
    public void init() {
        super.init();
        iView.loadFullAd();
        allSize = cleanApplication.getRamSize();
        list = cleanApplication.getListMng();
        Collections.sort(list, new Sizesort());
        for (JunkInfo info : list) {
            info.isChecked = false;
//            if (info.isChecked) {
//                cleanSize += info.size;
//            }
        }
        iView.initData(cleanSize);
        iView.onClick();
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
        addAppAdapterData();
    }

    public void sortList(int type) {
        this.type = type;
        switch (type) {
            case SIZE_TYPE:
                Collections.sort(list, new Sizesort());
                break;
            case TIME_TYPE:
                Collections.sort(list, new Timesort());
                break;
            case PINLV_TYPE:
                Collections.sort(list, new LastRunsort());
                break;
        }
        iView.addAppManagerdata(list);
    }

    public void addAppAdapterData() {
        iView.addAppManagerdata(list);
    }

    public void restart() {
        if (type == PINLV_TYPE) {
            iView.reStart();
        }
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
                list.remove(softinfo);
                addCleandata(false, softinfo.size);
                iView.addAppManagerdata(list);
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