package ir.ac.sku.www.sessapplication.utils;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.provider.Settings;
import android.util.Log;

import ir.ac.sku.www.sessapplication.API.MyLog;

public class IntentHelper {

    static void openWiFiSettingScreen(Context context) {
        Log.i(MyLog.SESS, "Open WiFi Setting Screen");
        context.startActivity(new Intent(Settings.ACTION_WIFI_SETTINGS));
    }

    static void openDataUsageScreen(Context context) {
        Log.i(MyLog.SESS, "Open Data Usage Screen");
        Intent intent = new Intent();
        intent.setComponent(new ComponentName("com.android.settings",
                "com.android.settings.Settings$DataUsageSummaryActivity"));
        context.startActivity(intent);
    }
}
