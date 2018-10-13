package ir.ac.sku.www.sessapplication.utils;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.provider.Settings;

public class IntentHelper {

    private static void openWiFiSettingScreen(Context context){
        context.startActivity(new Intent(Settings.ACTION_WIFI_SETTINGS));
    }

    private static void openDataUsageScreen(Context context){
        Intent intent = new Intent();
        intent.setComponent(new ComponentName("com.android.settings",
                "com.android.settings.Settings$DataUsageSummaryActivity"));
        context.startActivity(intent);
    }
}
