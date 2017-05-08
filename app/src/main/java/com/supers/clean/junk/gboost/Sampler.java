package com.supers.clean.junk.gboost;


import android.app.ActivityManager;
import android.content.Context;
import android.net.TrafficStats;
import android.os.Debug;
import android.os.Process;
import android.util.Log;

import com.supers.clean.junk.util.CommonUtil;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.RandomAccessFile;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Usage:
 * Sampler.getInstance().init(getApplicationContext(), 100L);
 * Sampler.getInstance().start();
 */
public class Sampler implements Runnable {

    private volatile static Sampler instance = null;
    private ScheduledExecutorService scheduler;
    private ActivityManager activityManager;
    private long freq;
    private Long lastCpuTime;
    private Long lastAppCpuTime;
    private RandomAccessFile procStatFile;
    private RandomAccessFile appStatFile;
    private long total = 0;
    private long idle = 0;
    private double usage = 0;
    private Context context;
    private long lastTotalRxBytes;
    private long lastTimeStamp;

    private Sampler() {
        scheduler = Executors.newSingleThreadScheduledExecutor();
    }

    public static Sampler getInstance() {
        if (instance == null) {
            synchronized (Sampler.class) {
                if (instance == null) {
                    instance = new Sampler();
                }
            }
        }
        return instance;
    }

    // freq为采样周期
    public void init(Context context, long freq) {
        activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        this.context = context;
        this.freq = freq;
    }

    public void start() {
        scheduler.scheduleWithFixedDelay(this, 0L, freq, TimeUnit.MILLISECONDS);
    }

    OnCpuUpdateListener onCpuUpdateListener;

    public void setOnCpuUpdateListener(OnCpuUpdateListener onCpuUpdateListener) {
        this.onCpuUpdateListener = onCpuUpdateListener;
    }

    public interface OnCpuUpdateListener {
        void cupUpdate(int cpu, long speed);
    }

    @Override
    public void run() {

        if (onCpuUpdateListener != null) {
            getCup();
            long speed = getNetWork();
            int cup = (int) usage;
            onCpuUpdateListener.cupUpdate(cup, speed);
        }
    }

    private void getCup() {
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream("/proc/stat")), 1000);
            String load = reader.readLine();
            reader.close();

            String[] toks = load.split(" ");

            long currTotal = Long.parseLong(toks[2]) + Long.parseLong(toks[3]) + Long.parseLong(toks[4]);
            long currIdle = Long.parseLong(toks[5]);

            this.usage = (currTotal - total) * 100.0f / (currTotal - total + currIdle - idle);
            this.total = currTotal;
            this.idle = currIdle;
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    private long getNetWork() {
        long nowTotalRxBytes = getTotalRxBytes(); // 获取当前数据总量
        long nowTimeStamp = System.currentTimeMillis(); // 当前时间
        long speed = ((nowTotalRxBytes - lastTotalRxBytes) * 1000 / (nowTimeStamp == lastTimeStamp ? nowTimeStamp : nowTimeStamp
                - lastTimeStamp));// 毫秒转换
//        gboost_network_size.setText(CommonUtil.convertStorageWifi(speed));
        lastTimeStamp = nowTimeStamp;
        lastTotalRxBytes = nowTotalRxBytes;
        return speed;
    }

    private long getTotalRxBytes() {
        // 得到整个手机的流量值
        return TrafficStats.getUidRxBytes(context.getApplicationInfo().uid) == TrafficStats.UNSUPPORTED ? 0
                : (TrafficStats.getTotalRxBytes());//
    }

    private double sampleCPU() {
        long cpuTime;
        long appTime;
        double sampleValue = 0.0D;
        try {
            if (procStatFile == null || appStatFile == null) {
                procStatFile = new RandomAccessFile("/proc/stat", "r");
                appStatFile = new RandomAccessFile("/proc/" + Process.myPid() + "/stat", "r");
            } else {
                procStatFile.seek(0L);
                appStatFile.seek(0L);
            }
            String procStatString = procStatFile.readLine();
            String appStatString = appStatFile.readLine();
            String procStats[] = procStatString.split(" ");
            String appStats[] = appStatString.split(" ");
            cpuTime = Long.parseLong(procStats[2]) + Long.parseLong(procStats[3])
                    + Long.parseLong(procStats[4]) + Long.parseLong(procStats[5])
                    + Long.parseLong(procStats[6]) + Long.parseLong(procStats[7])
                    + Long.parseLong(procStats[8]);
            appTime = Long.parseLong(appStats[13]) + Long.parseLong(appStats[14]);
            if (lastCpuTime == null && lastAppCpuTime == null) {
                lastCpuTime = cpuTime;
                lastAppCpuTime = appTime;
                return sampleValue;
            }
            sampleValue = ((double) (appTime - lastAppCpuTime) / (double) (cpuTime - lastCpuTime)) * 100D;
            lastCpuTime = cpuTime;
            lastAppCpuTime = appTime;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return sampleValue;
    }

    private double sampleMemory() {
        double mem = 0.0D;
        try {
            // 统计进程的内存信息 totalPss
            final Debug.MemoryInfo[] memInfo = activityManager.getProcessMemoryInfo(new int[]{Process.myPid()});
            if (memInfo.length > 0) {
                // TotalPss = dalvikPss + nativePss + otherPss, in KB
                final int totalPss = memInfo[0].getTotalPss();
                if (totalPss >= 0) {
                    // Mem in MB
                    mem = totalPss / 1024.0D;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return mem;
    }
}
