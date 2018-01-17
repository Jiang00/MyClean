package com.security.cleaner.utils;

import android.app.Activity;
import android.app.PendingIntent;
import android.bluetooth.BluetoothAdapter;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.graphics.SurfaceTexture;
import android.hardware.Camera;
import android.media.AudioManager;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiManager;
import android.os.PowerManager;
import android.os.Vibrator;
import android.provider.Settings;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;

/**
 * 各种开关
 */
public class ASwitchControl {
    private static Camera.Parameters parameters;
    //    private static WifiManager wifiManager;
//    private static AudioManager audioManager;
    private static Camera camera = null;

    /**
     * 移动网络开关
     */
    public static void setMobileDataEnabled(Context context, boolean enabled) {
        ConnectivityManager conMgr = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        Class<?> conMgrClass = null; // ConnectivityManager类
        Field iConMgrField = null; // ConnectivityManager类中的字段
        Object iConMgr = null; // IConnectivityManager类的引用
        Class<?> iConMgrClass = null; // IConnectivityManager类
        Method setMobileDataEnabledMethod = null; // setMobileDataEnabled方法

        try {
            // 取得ConnectivityManager类
            conMgrClass = Class.forName(conMgr.getClass().getName());
            // 取得ConnectivityManager类中的对象mService
            iConMgrField = conMgrClass.getDeclaredField("mService");
            // 设置mService可访问
            iConMgrField.setAccessible(true);
            // 取得mService的实例化类IConnectivityManager
            iConMgr = iConMgrField.get(conMgr);
            // 取得IConnectivityManager类
            iConMgrClass = Class.forName(iConMgr.getClass().getName());
            // 取得IConnectivityManager类中的setMobileDataEnabled(boolean)方法
            setMobileDataEnabledMethod = iConMgrClass.getDeclaredMethod("setMobileDataEnabled", Boolean.TYPE);
            // 设置setMobileDataEnabled方法可访问
            setMobileDataEnabledMethod.setAccessible(true);
            // 调用setMobileDataEnabled方法
            setMobileDataEnabledMethod.invoke(iConMgr, enabled);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (SecurityException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    public static void setMobileData(Context pContext, boolean pBoolean) {
        try {
            ConnectivityManager mConnectivityManager = (ConnectivityManager) pContext.getSystemService(Context.CONNECTIVITY_SERVICE);
            Class ownerClass = mConnectivityManager.getClass();
            Class[] argsClass = new Class[1];
            argsClass[0] = boolean.class;
            Method method = ownerClass.getMethod("setMobileDataEnabled", argsClass);
            method.invoke(mConnectivityManager, pBoolean);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static int switchWifi(Context context) {
        WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        boolean wifiState = wifiManager.isWifiEnabled();
        int wifiImageId = 0;
        if (wifiState) {
//            wifiManager.setWifiEnabled(false);
            wifiImageId = 1;
        } else {
//            wifiManager.setWifiEnabled(true);
            wifiImageId = 2;
        }
        wifiManager.setWifiEnabled(!wifiState);
        return wifiImageId;
    }


    public static void switchSound(Context context) {
        Log.d("TEST", "switchSound");
        AudioManager audioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
        if (audioManager.getRingerMode() == AudioManager.RINGER_MODE_NORMAL) {
            audioManager.setRingerMode(AudioManager.RINGER_MODE_SILENT);
        } else {
            audioManager.setRingerMode(AudioManager.RINGER_MODE_NORMAL);
        }
        Log.d("TEST", audioManager.getRingerMode() + "");
    }


    public static int switchVibrate(Context context) {
        AudioManager audioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
        int vibrateImageId = 0;
        if (audioManager.getRingerMode() == AudioManager.RINGER_MODE_VIBRATE) {
            audioManager.setRingerMode(AudioManager.RINGER_MODE_NORMAL);
            vibrateImageId = 1;
        } else {
            audioManager.setRingerMode(AudioManager.RINGER_MODE_VIBRATE);
            Vibrator vibrator = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
            long[] pattern = {0, 200}; // 停止 开启 停止 开启
            vibrator.vibrate(pattern, -1); //重复两次上面的pattern 如果只想震动一次，index设为-1
            vibrateImageId = 2;
        }
        return vibrateImageId;
    }


    public static boolean getWifiApState(Context mContext) {
        boolean wifiApState = false;
        WifiManager wifiManager = (WifiManager) mContext.getSystemService(Context.WIFI_SERVICE);
        try {
            Method method = wifiManager.getClass().getMethod("getWifiApState");
            int i = (int) method.invoke(wifiManager);

            if (i == 13) {
                wifiApState = true;
            } else if (i == 11) {
                wifiApState = false;
            }
//            return wifiApState;
            Log.d("TEST", "wifiApState" + wifiApState);
        } catch (Exception e) {
//            Log.e(TAG,"Cannot get WiFi AP state" + e);
//            return false;
            e.printStackTrace();
        }
        return wifiApState;
    }

    // wifi热点开关
    public static int switchHotspot(Context context) {
        WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        int hotspotImageId = 0;
        boolean enabled = getWifiApState(context);
        if (enabled) {
            hotspotImageId = 1;
        } else {
            wifiManager.setWifiEnabled(false);
            hotspotImageId = 2;
        }
        try {
            //热点的配置类
            WifiConfiguration apConfig = new WifiConfiguration();
            //配置热点的名称(可以在名字后面加点随机数什么的)
//            apConfig.SSID = "YRCCONNECTION";
            //配置热点的密码
//            apConfig.preSharedKey="12122112";
            //通过反射调用设置热点
            Method method = wifiManager.getClass().getMethod(
                    "setWifiApEnabled", WifiConfiguration.class, Boolean.TYPE);
            //返回热点打开状态
            method.invoke(wifiManager, apConfig, !enabled);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return hotspotImageId;
    }


    public static int switchRotate(Context context) {
        int rotateState = Settings.System.getInt(context.getContentResolver(), Settings.System.ACCELEROMETER_ROTATION, 0);
        int rotateImageId = 0;
        if (rotateState == 0) {
            Settings.System.putInt(context.getContentResolver(), Settings.System.ACCELEROMETER_ROTATION, 1);
            rotateImageId = 1;
        } else {
            Settings.System.putInt(context.getContentResolver(), Settings.System.ACCELEROMETER_ROTATION, 0);
            rotateImageId = 2;
        }
        return rotateImageId;
    }

    //蓝牙开关

    public static int switchBlueTooth() {
        BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        int blueToothImageId = 0;
        if (mBluetoothAdapter == null) {
//            Toast.makeText(this, "本机没有找到蓝牙硬件或驱动！", Toast.LENGTH_SHORT).show();
        }
        // 如果本地蓝牙没有开启，则开启
        if (!mBluetoothAdapter.isEnabled()) {
            // 我们通过startActivityForResult()方法发起的Intent将会在onActivityResult()回调方法中获取用户的选择，比如用户单击了Yes开启，
            // 那么将会收到RESULT_OK的结果，
            // 如果RESULT_CANCELED则代表用户不愿意开启蓝牙
            // 用enable()方法来开启，无需询问用户(实惠无声息的开启蓝牙设备),这时就需要用到android.permission.BLUETOOTH_ADMIN权限。
            mBluetoothAdapter.enable();
            blueToothImageId = 1;
            // mBluetoothAdapter.disable();//关闭蓝牙
        } else {
            mBluetoothAdapter.disable();
            blueToothImageId = 2;
        }
        return blueToothImageId;
    }


    public static void turnGPSOn(Context context) {
        Intent intent = new Intent("android.location.GPS_ENABLED_CHANGE");
        intent.putExtra("enabled", true);
        context.sendBroadcast(intent);

        String provider = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.LOCATION_PROVIDERS_ALLOWED);
        if (!provider.contains("gps")) { //if gps is disabled
            final Intent poke = new Intent();
            poke.setClassName("com.android.settings", "com.android.settings.widget.SettingsAppWidgetProvider");
            poke.addCategory(Intent.CATEGORY_ALTERNATIVE);
            poke.setData(Uri.parse("3"));
            context.sendBroadcast(poke);
        }
    }

    public static void toggleGPS(Context context) {
        Intent gpsIntent = new Intent();
        gpsIntent.setClassName("com.android.settings",
                "com.android.settings.widget.SettingsAppWidgetProvider");
        gpsIntent.addCategory("android.intent.category.ALTERNATIVE");
        gpsIntent.setData(Uri.parse("custom:3"));
        try {
            PendingIntent.getBroadcast(context, 0, gpsIntent, 0).send();
        } catch (PendingIntent.CanceledException e) {
            e.printStackTrace();
        }
    }

    //
    public static int switchAdaptiveBrightness(Context context) {
        int screenMode = 0;
        int adaptiveBrightnessImageId = 0;
        try {
            screenMode = Settings.System.getInt(context.getContentResolver(), Settings.System.SCREEN_BRIGHTNESS_MODE);
        } catch (Settings.SettingNotFoundException e) {
            e.printStackTrace();
        }
        if (screenMode == Settings.System.SCREEN_BRIGHTNESS_MODE_AUTOMATIC) {
            Settings.System.putInt(context.getContentResolver(), Settings.System.SCREEN_BRIGHTNESS_MODE, Settings.System.SCREEN_BRIGHTNESS_MODE_MANUAL);
            adaptiveBrightnessImageId = 1;
        } else {
            Settings.System.putInt(context.getContentResolver(), Settings.System.SCREEN_BRIGHTNESS_MODE, Settings.System.SCREEN_BRIGHTNESS_MODE_AUTOMATIC);
            adaptiveBrightnessImageId = 2;
        }
        return adaptiveBrightnessImageId;
    }


    public static void setBrightness(Context context) {

//        Intent intent = new Intent(context, SetBrightnessActivity.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//        context.startActivity(intent);

    }

    static ConnectivityManager connManager;

    @SuppressWarnings({"unused", "unchecked"})
    public static boolean isMobileDataOn(Context context) {
        Boolean isOpen = false;
        if (connManager == null) {
            connManager = (ConnectivityManager) context
                    .getSystemService(Context.CONNECTIVITY_SERVICE);
        }
        try {
            String methodName = "getMobileDataEnabled";
            Class cmClass = connManager.getClass();
            Method method = cmClass.getMethod(methodName, (Class) null);
            isOpen = (Boolean) method.invoke(connManager, (Class) null);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return isOpen;

    }

    @SuppressWarnings({"unused", "unchecked"})
    public static void MobileDataSwitchUtils(Context context) {
        if (connManager == null) {
            connManager = (ConnectivityManager) context
                    .getSystemService(Context.CONNECTIVITY_SERVICE);
        }
        try {

            String methodName = "getMobileDataEnabled";
            Class cmClass = connManager.getClass();
            // Boolean isOpen = null;
            Method method = cmClass.getMethod(methodName, (Class) null);

            // isOpen = (Boolean) method.invoke(connManager, null);

            Class<?> conMgrClass = Class.forName(connManager.getClass()
                    .getName());
            // 得到ConnectivityManager类的成员变量mService（ConnectivityService类型）
            Field iConMgrField = conMgrClass.getDeclaredField("mService");
            iConMgrField.setAccessible(true);
            // mService成员初始化
            Object iConMgr = iConMgrField.get(connManager);
            // 得到mService对应的Class对象
            Class<?> iConMgrClass = Class.forName(iConMgr.getClass().getName());
            /*
             * 得到mService的setMobileDataEnabled(该方法在android源码的ConnectivityService类中实现
             * )， 该方法的参数为布尔型，所以第二个参数为Boolean.TYPE
             */
            Method setMobileDataEnabledMethod = iConMgrClass.getDeclaredMethod(
                    "setMobileDataEnabled", Boolean.TYPE);
            setMobileDataEnabledMethod.setAccessible(true);
            /*
             * 调用ConnectivityManager的setMobileDataEnabled方法（方法是隐藏的），
             * 实际上该方法的实现是在ConnectivityService(系统服务实现类)中的
             */

            if (isMobileDataOn(context)) {
//                Toast.makeText(context, "关闭数据连接", Toast.LENGTH_SHORT).show();
            } else {
//                Toast.makeText(context, "开启数据连接", Toast.LENGTH_SHORT).show();
            }
            setMobileDataEnabledMethod.invoke(iConMgr, !isMobileDataOn(context));

        } catch (Exception e) {
            // TODO: handle exception
        }
    }

    public static int getScreenBrightness(Activity activity) {
        int nowBrightnessValue = 0;
        ContentResolver resolver = activity.getContentResolver();
        try {
            nowBrightnessValue = Settings.System.getInt(
                    resolver, Settings.System.SCREEN_BRIGHTNESS);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return nowBrightnessValue;
    }

    private static final int LIGHT_NORMAL = 64;
    private static final int LIGHT_50_PERCENT = 127;
    private static final int LIGHT_75_PERCENT = 191;
    private static final int LIGHT_100_PERCENT = 255;
    private static final int LIGHT_AUTO = 0;
    private static final int LIGHT_ERR = -1;

    public static void brightnessSwitchUtils(Context context) {
        int light = 0;
        ContentResolver cr = context.getContentResolver();
        try {
            boolean auto = Settings.System.getInt(cr,
                    Settings.System.SCREEN_BRIGHTNESS_MODE) == Settings.System.SCREEN_BRIGHTNESS_MODE_AUTOMATIC;

            if (!auto) {
                light = Settings.System.getInt(cr,
                        Settings.System.SCREEN_BRIGHTNESS, -1);
                if (light > 0 && light <= LIGHT_NORMAL) {
                    light = LIGHT_NORMAL;
                } else if (light > LIGHT_NORMAL && light <= LIGHT_50_PERCENT) {
                    light = LIGHT_50_PERCENT;
                } else if (light > LIGHT_50_PERCENT
                        && light <= LIGHT_75_PERCENT) {
                    light = LIGHT_75_PERCENT;
                } else {
                    light = LIGHT_100_PERCENT;
                }
            } else {
                light = LIGHT_AUTO;
            }

            switch (light) {
                case LIGHT_NORMAL:
                    light = LIGHT_50_PERCENT - 1;
                    Toast.makeText(context, "正常亮度", Toast.LENGTH_SHORT).show();
                    break;
                case LIGHT_50_PERCENT:
                    light = LIGHT_75_PERCENT - 1;
                    Toast.makeText(context, "较高亮度", Toast.LENGTH_SHORT).show();
                    break;
                case LIGHT_75_PERCENT:
                    light = LIGHT_100_PERCENT - 1;
                    Toast.makeText(context, "高亮度", Toast.LENGTH_SHORT).show();
                    break;
                case LIGHT_100_PERCENT:
                    light = LIGHT_NORMAL - 1;
                    Settings.System.putInt(cr,
                            Settings.System.SCREEN_BRIGHTNESS_MODE,
                            Settings.System.SCREEN_BRIGHTNESS_MODE_AUTOMATIC);
                    Toast.makeText(context, "自动亮度", Toast.LENGTH_SHORT).show();
                    break;
                case LIGHT_AUTO:
                    light = LIGHT_NORMAL - 1;
                    Settings.System.putInt(cr,
                            Settings.System.SCREEN_BRIGHTNESS_MODE,
                            Settings.System.SCREEN_BRIGHTNESS_MODE_MANUAL);
                    Toast.makeText(context, "低亮度", Toast.LENGTH_SHORT).show();

                    break;
                case LIGHT_ERR:
                    light = LIGHT_NORMAL - 1;
                    break;

            }

            changeAppBrightness(context, light);
            Settings.System.putInt(cr,
                    Settings.System.SCREEN_BRIGHTNESS, light);

        } catch (Settings.SettingNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

    public static void changeAppBrightness(Context context, int brightness) {
        Window window = ((Activity) context).getWindow();
        WindowManager.LayoutParams lp = window.getAttributes();
        if (brightness == -1) {
            lp.screenBrightness = WindowManager.LayoutParams.BRIGHTNESS_OVERRIDE_NONE;
        } else {
            lp.screenBrightness = (brightness <= 0 ? 1 : brightness) / 255f;
        }
        window.setAttributes(lp);
    }

    private static PowerManager mPowerManager;

    private static void setLight(Context context, int light) {
        try {
            if (mPowerManager == null) {
                mPowerManager = (PowerManager) context
                        .getSystemService(Context.POWER_SERVICE);
            }

            Class<?> pmClass = Class
                    .forName(mPowerManager.getClass().getName());
            // 得到PowerManager类中的成员mService（mService为PowerManagerService类型）
            Field field = pmClass.getDeclaredField("mService");
            field.setAccessible(true);
            // 实例化mService
            Object iPM = field.get(mPowerManager);
            // 得到PowerManagerService对应的Class对象
            Class<?> iPMClass = Class.forName(iPM.getClass().getName());
            /*
             * 得到PowerManagerService的函数setBacklightBrightness对应的Method对象，
             * PowerManager的函数setBacklightBrightness实现在PowerManagerService中
             */
            Method method = iPMClass.getDeclaredMethod(
                    "setBacklightBrightness", int.class);
            method.setAccessible(true);
            // 调用实现PowerManagerService的setBacklightBrightness
            method.invoke(iPM, light);

        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    //shoudiantong
    public static boolean switchFlashlight() {
        boolean flashLightState = false;
        if (camera == null) {
            try {
                camera = Camera.open();
            } catch (Exception e) {
                return false;
            }
        }
        try {
            camera.stopPreview();
        } catch (Exception e) {
            try {
                camera = Camera.open();
                parameters = camera.getParameters();
            } catch (Exception e1) {
                return false;
            }
        }
        try {
            parameters = camera.getParameters();
        } catch (Exception e) {
            try {
                camera.release();
                camera = Camera.open();
                parameters = camera.getParameters();
            } catch (Exception e1) {
                return false;
            }
        }

        String s = parameters.getFlashMode();
        if (s == null) {
            return false;
        }
        if (s.equals(Camera.Parameters.FLASH_MODE_OFF)) {
            openLight();
            flashLightState = true;
        } else {
            closeLight();
            flashLightState = false;
        }


        return flashLightState;
    }

    //shoudiantong
    public static boolean getSwitchFlashlight() {
        boolean flashLightState = false;
        if (camera == null) {
            try {
                camera = Camera.open();
            } catch (Exception e) {
                return false;
            }
        } else {
            try {
                camera.stopPreview();
                camera.release();
                camera = Camera.open();
            } catch (Exception e) {
                return false;
            }
        }
        try {
            camera.stopPreview();
            parameters = camera.getParameters();
            String s = parameters.getFlashMode();
            if (s == null) {
                return false;
            }
            if (s.equals(Camera.Parameters.FLASH_MODE_OFF)) {
                flashLightState = false;
            } else {
                flashLightState = true;
            }
        } catch (Exception e) {
            return false;
        }

        return flashLightState;

//        mParameters.setFlashMode(Camera.Parameters.FLASH_MODE_TORCH);
//        camera.setParameters(mParameters);
//        camera.startPreview();
//        camera.autoFocus(new Camera.AutoFocusCallback() {
//            public void onAutoFocus(boolean success, Camera camera) {
//            }
//        });
    }

    public static void turnLightOn(Camera mCamera) {
        if (mCamera == null) {
            return;
        }
        Camera.Parameters parameters = mCamera.getParameters();
        if (parameters == null) {
            return;
        }
        List<String> flashModes = parameters.getSupportedFlashModes();
        // Check if camera flash exists
        if (flashModes == null) {
            // Use the screen as a flashlight (next best thing)
            return;
        }
        String flashMode = parameters.getFlashMode();
        if (!Camera.Parameters.FLASH_MODE_TORCH.equals(flashMode)) {
            // Turn on the flash
            if (flashModes.contains(Camera.Parameters.FLASH_MODE_TORCH)) {
                parameters.setFlashMode(Camera.Parameters.FLASH_MODE_TORCH);
                mCamera.setParameters(parameters);
            } else {
            }
        }
    }


    public static void turnLightOff(Camera mCamera) {
        if (mCamera == null) {
            return;
        }
        Camera.Parameters parameters = mCamera.getParameters();
        if (parameters == null) {
            return;
        }
        List<String> flashModes = parameters.getSupportedFlashModes();
        String flashMode = parameters.getFlashMode();
        // Check if camera flash exists
        if (flashModes == null) {
            return;
        }
        if (!Camera.Parameters.FLASH_MODE_OFF.equals(flashMode)) {
            // Turn off the flash
            if (flashModes.contains(Camera.Parameters.FLASH_MODE_OFF)) {
                parameters.setFlashMode(Camera.Parameters.FLASH_MODE_OFF);
                mCamera.setParameters(parameters);
            } else {
//                Log.e(TAG, "FLASH_MODE_OFF not supported");
            }
        }
        mCamera.release();
        mCamera = null;
    }

    protected static void openLight() {
//        TransitionDrawable drawable = (TransitionDrawable) imageView_bg.getDrawable();
//        drawable.startTransition(400);
        //是否让前一个图片消失
//        drawable.setCrossFadeEnabled(true);
//        imageView_bg.setTag(true);
        try {
//            camera = Camera.open();
            int textureId = 0;
            camera.setPreviewTexture(new SurfaceTexture(textureId));
            camera.startPreview();

//            parameters = camera.getParameters();
            parameters.setFlashMode(parameters.FLASH_MODE_TORCH);
            camera.setParameters(parameters);
        } catch (Exception e) {
            Log.i("打开闪光灯失败：", e.toString() + "");
        }

    }


    protected static void closeLight() {
//        TransitionDrawable drawable = (TransitionDrawable) imageView_bg.getDrawable();

//        if ((Boolean) imageView_bg.getTag()) {
//            drawable.reverseTransition(400);
        //是否让前一个图片消失
//            drawable.setCrossFadeEnabled(true);
//            imageView_bg.setTag(false);
        if (camera != null) {
//                parameters = camera.getParameters();
            parameters.setFlashMode(Camera.Parameters.FLASH_MODE_OFF);
            camera.setParameters(parameters);
            camera.stopPreview();
            camera.release();
            camera = null;
        }
//        }
    }

}
