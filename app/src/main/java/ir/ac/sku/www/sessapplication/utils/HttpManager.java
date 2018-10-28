package ir.ac.sku.www.sessapplication.utils;

import android.app.Dialog;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Map;

import ir.ac.sku.www.sessapplication.API.MyLog;
import ir.ac.sku.www.sessapplication.R;

public class HttpManager {

    public static boolean isNOTOnline(Context context) {
        Log.i(MyLog.SESS, "Check device is Online");
        ConnectivityManager manager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = manager.getActiveNetworkInfo();
        return networkInfo == null || !networkInfo.isConnected();
    }

    public static void noInternetAccess(final Context context) {
        Log.i(MyLog.SESS, "Open Dialog NO Internet Access");
        final Dialog dialog = new Dialog(context);
        dialog.setContentView(R.layout.disconnect);

        Button wifi = dialog.findViewById(R.id.disconnect_WiFi);
        Button data = dialog.findViewById(R.id.disconnect_MobileData);

        wifi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                IntentHelper.openWiFiSettingScreen(context);
            }
        });

        data.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                IntentHelper.openDataUsageScreen(context);
            }
        });
        dialog.show();
    }

    public static String enCodeParameters(Map<String, String> params) {
        StringBuilder stringBuilder = new StringBuilder();
        try {
            for (String key : params.keySet()) {
                String value = params.get(key);
                if (stringBuilder.length() > 0) {
                    stringBuilder.append("&");
                }
                stringBuilder.append(key);
                stringBuilder.append("=");
                stringBuilder.append(URLEncoder.encode(value, "UTF-8"));
            }
            return stringBuilder.toString();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return "";
    }
}
