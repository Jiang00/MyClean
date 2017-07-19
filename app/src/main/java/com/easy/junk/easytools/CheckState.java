package com.easy.junk.easytools;

import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.location.LocationManager;
import android.media.AudioManager;
import android.net.ConnectivityManager;
import android.net.wifi.WifiManager;
import android.provider.Settings;

import java.lang.reflect.Method;

/**
 */
public class CheckState {



    public static boolean vibrateState(Context context) {
        boolean vibrateState = false;
        int vibrateMode = getAudioManager(context).getRingerMode();
        if (vibrateMode == AudioManager.RINGER_MODE_VIBRATE) {
            vibrateState = true;
        }
        return vibrateState;
    }

    public static boolean rotateState(Context context) {
        boolean rotateState = false;
        int rotateMode = Settings.System.getInt(context.getContentResolver(), Settings.System.ACCELEROMETER_ROTATION, 0);
        if (rotateMode == 1) {
            rotateState = true;
        }
        return rotateState;
    }

    public static boolean bluetoothState() {
        BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        return bluetoothAdapter.isEnabled();
    }

    public static boolean wifiState(Context context) {
        WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        return wifiManager.isWifiEnabled();
    }

    private static AudioManager getAudioManager(Context context) {
        AudioManager audioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
        return audioManager;
    }
    public static boolean autoBrightnessState(Context context) {
        boolean autoBrightnessState = false;
        try {
            int screenMode = Settings.System.getInt(context.getContentResolver(), Settings.System.SCREEN_BRIGHTNESS_MODE);
            if (screenMode == Settings.System.SCREEN_BRIGHTNESS_MODE_AUTOMATIC) {
                autoBrightnessState = true;
            }
        } catch (Settings.SettingNotFoundException e) {
            e.printStackTrace();
        }
        return autoBrightnessState;
    }
    public static boolean soundState(Context context) {
        boolean soundState = false;
        int soundMode = getAudioManager(context).getRingerMode();
        if (soundMode == AudioManager.RINGER_MODE_NORMAL) {
            soundState = true;
        }
        return soundState;
    }
    public static boolean networkState(Context context, Object[] arg) {
        try {
            ConnectivityManager mConnectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            Class ownerClass = mConnectivityManager.getClass();
            Class[] argsClass = null;
            if (arg != null) {
                argsClass = new Class[1];
                argsClass[0] = arg.getClass();
            }
            Method method = ownerClass.getMethod("getMobileDataEnabled", argsClass);
            Boolean isOpen = (Boolean) method.invoke(mConnectivityManager, arg);
            return isOpen;
        } catch (Exception e) {
            return false;
        }
    }



    public static boolean airplaneState(Context context) {
        boolean airplaneState = false;
        int isAirplaneMode = Settings.System.getInt(context.getContentResolver(), Settings.System.AIRPLANE_MODE_ON, 0);
        if (isAirplaneMode == 1) {
            airplaneState = true;
        }
        return airplaneState;
    }

    public static boolean gpsState(Context context) {
        LocationManager locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        // 通过GPS卫星定位，定位级别可以精确到街（通过24颗卫星定位，在室外和空旷的地方定位准确、速度快）
        boolean gpsState = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        return gpsState;
    }
}
