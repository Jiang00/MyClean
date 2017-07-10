package com.supers.clean.junk.wifi;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.TrafficStats;
import android.text.TextUtils;
import android.util.Log;

import com.android.clean.callback.AppRamCallBack;
import com.android.clean.core.CleanManager;
import com.android.clean.entity.JunkInfo;
import com.android.clean.util.Util;
import com.jaredrummler.android.processes.AndroidProcesses;
import com.jaredrummler.android.processes.models.AndroidAppProcess;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Ivy on 2017/4/20.
 */

public class RunAppManager {

    private static RunAppManager mRunAppManager;

    private PackageManager pm;

    private Context mContext;

    private List<RunAppInfo> runAppInfoList = new ArrayList<>();

    private long lastTime = 0;

    public void setLoadListener(LoadListener mLoadListener) {
        this.mLoadListener = mLoadListener;
    }

    public void removeLoadListener() {
        this.mLoadListener = null;
    }

    private LoadListener mLoadListener;

    public interface LoadListener {
        void loadFinish(List<RunAppInfo> lastRunAppInfoList);
    }

    public List<RunAppInfo> getRunAppInfoList() {
        return runAppInfoList;
    }

    public List<RunAppInfo> getLastRunAppInfoList() {
        return lastRunAppInfoList;
    }

    private List<RunAppInfo> lastRunAppInfoList = new ArrayList<>();

    private RunAppManager(Context context) {
        mContext = context;
        pm = mContext.getPackageManager();
    }

    public static synchronized RunAppManager getInstance(Context context) {
        if (mRunAppManager == null) {
            mRunAppManager = new RunAppManager(context.getApplicationContext());
        }
        return mRunAppManager;
    }

    public void loadRunAppInfo() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                runAppInfoList.clear();
                final PackageManager pm = mContext.getPackageManager();
                CleanManager.getInstance(mContext).loadAppRam(new AppRamCallBack() {
                    @Override
                    public void loadFinished(List<JunkInfo> appRamList, List<String> whiteList, long totalSize) {
                        for (JunkInfo info : appRamList) {
                            try {
                                ApplicationInfo applicationInfo = pm.getApplicationInfo(info.pkg, PackageManager.GET_META_DATA | PackageManager.GET_SHARED_LIBRARY_FILES);
                                runAppInfoList.add(getAppInfo(applicationInfo, info.pid, info.processName));
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                        lastRunAppInfoList.clear();
                        lastRunAppInfoList.addAll(runAppInfoList);
                        lastTime = System.currentTimeMillis();
                        if (mLoadListener != null) {
                            mLoadListener.loadFinish(lastRunAppInfoList);
                        }
                    }
                });
            }
        }).start();
    }

    private RunAppInfo getAppInfo(ApplicationInfo app, int pid, String processName) {
        long currentTime = System.currentTimeMillis();
        long nowUidRxBytes = TrafficStats.getUidRxBytes(app.uid);
        long nowUidTxBytes = TrafficStats.getUidTxBytes(app.uid);
        if (nowUidRxBytes + nowUidTxBytes == 0 || nowUidRxBytes == -1 || nowUidTxBytes == -1) {
            nowUidRxBytes = getTotalBytesManual(app.uid, true);
            nowUidTxBytes = getTotalBytesManual(app.uid, false);
        }
        long downUidSpeed = 0;
        long upUidSpeed = 0;
        Log.e("Appinfo lastlist", lastRunAppInfoList.size() + "");
        for (RunAppInfo runAppInfo : lastRunAppInfoList) {
            if (TextUtils.equals(app.packageName, runAppInfo.getPkgName())) {
                downUidSpeed = ((nowUidRxBytes - runAppInfo.getLastRxBytes()) * 1000 / (currentTime == lastTime ? currentTime : currentTime
                        - lastTime));// 毫秒转换
                upUidSpeed = ((nowUidTxBytes - runAppInfo.getLastTxBytes()) * 1000 / (currentTime == lastTime ? currentTime : currentTime
                        - lastTime));
                break;
            }
        }
        Log.e("2233445566", nowUidRxBytes + "  " + nowUidTxBytes + "  " + downUidSpeed + "  " + upUidSpeed);
        RunAppInfo appInfo = new RunAppInfo();
        appInfo.setAppLabel((String) app.loadLabel(pm));
        appInfo.setAppIcon(app.loadIcon(pm));
        appInfo.setPkgName(app.packageName);
        appInfo.setPid(pid);
        appInfo.setProcessName(processName);
        appInfo.setChecked(true);
        appInfo.setLastRxBytes(nowUidRxBytes);
        appInfo.setLastTxBytes(nowUidTxBytes);
        appInfo.setUpspeed(Util.convertStorageWifi(upUidSpeed));
        appInfo.setDownspeed(Util.convertStorageWifi(downUidSpeed));
        return appInfo;
    }

    private Long getTotalBytesManual(int localUid, Boolean RxBytes) {
//        Log.e("BytesManual*****", "localUid:" + localUid);
        try {
            File dir = new File("/proc/uid_stat/");
            String[] children = dir.list();
            StringBuffer stringBuffer = new StringBuffer();
            for (int i = 0; i < children.length; i++) {
                stringBuffer.append(children[i]);
                stringBuffer.append("   ");
            }
//        Log.e("children*****", children.length + "");
//        Log.e("children22*****", stringBuffer.toString());
            if (!Arrays.asList(children).contains(String.valueOf(localUid))) {
                return 0L;
            }
            File uidFileDir = new File("/proc/uid_stat/" + String.valueOf(localUid));
            File uidActualFileReceived = new File(uidFileDir, "tcp_rcv");
            File uidActualFileSent = new File(uidFileDir, "tcp_snd");
            String textReceived = "0";
            String textSent = "0";
            try {
                BufferedReader brReceived = new BufferedReader(new FileReader(uidActualFileReceived));
                BufferedReader brSent = new BufferedReader(new FileReader(uidActualFileSent));
                String receivedLine;
                String sentLine;

                if ((receivedLine = brReceived.readLine()) != null) {
                    textReceived = receivedLine;
//                Log.e("receivedLine*****", "receivedLine:" + receivedLine);
                }
                if ((sentLine = brSent.readLine()) != null) {
                    textSent = sentLine;
//                Log.e("sentLine*****", "sentLine:" + sentLine);
                }
                brReceived.close();
                brSent.close();
            } catch (IOException e) {
                e.printStackTrace();
//            Log.e("IOException*****", e.toString());
            }
//        Log.e("BytesManualEnd*****", "localUid:" + localUid);
            if (RxBytes) {
                return Long.valueOf(textReceived).longValue();
            } else {
                return Long.valueOf(textSent).longValue();
            }
        } catch (Exception e) {
            return 0L;
        }

    }
}
