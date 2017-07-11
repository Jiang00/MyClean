package com.supers.clean.junk.presenter;

import android.annotation.TargetApi;
import android.app.ActivityManager;
import android.app.usage.UsageEvents;
import android.app.usage.UsageStatsManager;
import android.content.Context;
import android.os.Build;
import android.util.Log;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

/**
 * Created by song on 16/4/21.
 */
public class GetTopApp {
    Context context;
    Field processState = null;

    public GetTopApp(Context context) {
        this.context = context;
    }

    public String execute() {
        String packageName = null;
        if ( Build.VERSION.SDK_INT <= 19) {
            try {
                List<ActivityManager.RunningTaskInfo> lst = ((ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE)).getRunningTasks(1);
                if (lst != null && lst.size() > 0) {
                    ActivityManager.RunningTaskInfo runningTaskInfo = lst.get(0);
                    if (runningTaskInfo.numRunning > 0 && runningTaskInfo.topActivity != null) {
                        packageName = runningTaskInfo.topActivity.getPackageName();
                        Log.e("float", "1==" + packageName);
                    }
                }
            } catch (Exception e) {

            }
        } else {
            try {
                packageName = getTopPackage();
                Log.e("float", "2==" + packageName);
                if (packageName != null) {
                    return packageName;
                }
                packageName = getActivePackages();
                if (packageName != null) {
                    Log.e("float", "3==" + packageName);
                    return packageName;
                }

                List<ActivityManager.RunningTaskInfo> lst = ((ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE)).getRunningTasks(1);
                if (lst != null && lst.size() > 0) {
                    ActivityManager.RunningTaskInfo runningTaskInfo = lst.get(0);
                    if (runningTaskInfo.numRunning > 0 && runningTaskInfo.topActivity != null) {
                        packageName = runningTaskInfo.topActivity.getPackageName();
                        Log.e("float", "1==" + packageName);
                    }
                }

                packageName = getForegroundApp();
                Log.e("float", "4==" + packageName);
            } catch (Exception e) {
            }
        }
        return packageName;
    }

    private String getActivePackages() throws NoSuchFieldException, IllegalAccessException {
        if (processState == null) {
            processState = ActivityManager.RunningAppProcessInfo.class.getDeclaredField("processState");
            processState.setAccessible(true);
        }
        final List<ActivityManager.RunningAppProcessInfo> processInfos = ((ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE)).getRunningAppProcesses();
        if (processInfos != null) {
            for (ActivityManager.RunningAppProcessInfo processInfo : processInfos) {
                if (processInfo.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND) {
                    return processInfo.processName;
                }
                int anInt = processState.getInt(processInfo);
                if (anInt == 2) {
                    return processInfo.pkgList[0];
                }
            }
        }
        return null;
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private String getTopPackage() {
        String packageName = null;
        UsageStatsManager sUsageStatsManager = (UsageStatsManager) context.getSystemService(Context.USAGE_STATS_SERVICE);
        long endTime = System.currentTimeMillis();
        long beginTime = endTime - 10000;
        UsageEvents.Event event = new UsageEvents.Event();
        UsageEvents usageEvents = sUsageStatsManager.queryEvents(beginTime, endTime);
        while (usageEvents.hasNextEvent()) {
            usageEvents.getNextEvent(event);
            if (event.getEventType() == UsageEvents.Event.MOVE_TO_FOREGROUND) {
                packageName = event.getPackageName();
            }
        }
        return packageName;
    }


    private UsageStatsManager mUsageStatsManager;
    private Comparator mRecentComp;

    /**
     * first app user
     */
    public static final int AID_APP = 10000;

    /**
     * offset for uid ranges for each user
     */
    public static final int AID_USER = 100000;

    static HashMap<String, Boolean> excludes = new HashMap<>();

    static {
        excludes.put("com.android.systemui", true);
        excludes.put("android.process.acore", true);
        excludes.put("android.process.media", true);
    }

    public static String getForegroundApp() {
        File[] files = new File("/proc").listFiles();
        int lowestOomScore = Integer.MAX_VALUE;
        String foregroundProcess = null;

        for (File file : files) {
            int pid;
            String name = file.getName();
            try {
                pid = Integer.parseInt(name);
            } catch (NumberFormatException e) {
                continue;
            }

            try {
                String cgroup = read(String.format("/proc/%d/cgroup", pid));

                if (cgroup.contains("bg_non_interactive")) {
                    continue;
                }

                if (!cgroup.endsWith(name)) {
                    continue;
                }

                int uid = Integer.parseInt(cgroup.substring(cgroup.indexOf("uid_") + 4, cgroup.lastIndexOf("/")));
                if (uid >= 1000 && uid <= 1038) {
                    // system process
                    continue;
                }

                int appId = uid - AID_APP;
                // loop until we get the correct user id.
                // 100000 is the offset for each user.
                while (appId > AID_USER) {
                    appId -= AID_USER;
                }

                if (appId < 0) {
                    continue;
                }

                String cmdline = read(String.format("/proc/%d/cmdline", pid));
                if (excludes.containsKey(cmdline)) {
                    continue;
                }

                // u{user_id}_a{app_id} is used on API 17+ for multiple user account support.
                // String uidName = String.format("u%d_a%d", userId, appId);

                File oomScoreAdj = new File(String.format("/proc/%d/oom_score_adj", pid));
                if (oomScoreAdj.canRead()) {
                    int oomAdj = Integer.parseInt(read(oomScoreAdj.getAbsolutePath()));
                    if (oomAdj != 0) {
                        continue;
                    }
                }

                int oomscore = Integer.parseInt(read(String.format("/proc/%d/oom_score", pid)));
                if (oomscore < lowestOomScore) {
                    lowestOomScore = oomscore;
                    foregroundProcess = cmdline;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return foregroundProcess;
    }

    private static String read(String path) throws IOException {
        BufferedReader reader = null;
        try {
            StringBuilder output = new StringBuilder();
            reader = new BufferedReader(new FileReader(path));
            for (String line = reader.readLine(); line != null; line = reader.readLine()) {
                output.append(line);
            }
            return output.toString().trim();
        } finally {
            if (reader != null) {
                reader.close();
            }
        }
    }
}
