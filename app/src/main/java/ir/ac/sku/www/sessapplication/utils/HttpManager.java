package ir.ac.sku.www.sessapplication.utils;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Map;

import ir.ac.sku.www.sessapplication.API.MyLog;
import ir.ac.sku.www.sessapplication.R;

public class HttpManager {

    @SuppressLint("LongLogTag")
    public static boolean isNOTOnline(Context context) {
        Log.i(MyLog.HTTP_MANAGER, "Check device is Online");
        ConnectivityManager manager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = manager.getActiveNetworkInfo();
        return networkInfo == null || !networkInfo.isConnected();
    }

    @SuppressLint("LongLogTag")
    public static void noInternetAccess(final Context context) {
        Log.i(MyLog.HTTP_MANAGER, "Open Dialog NO Internet Access");
        final Dialog dialog = new Dialog(context);
        dialog.setContentView(R.layout.custom_disconnect);

        Button wifi = dialog.findViewById(R.id.disconnect_WiFi);
        Button data = dialog.findViewById(R.id.disconnect_MobileData);

        wifi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i(MyLog.HTTP_MANAGER, "on Wi-Fi Click");
                IntentHelper.openWiFiSettingScreen(context);
            }
        });

        data.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i(MyLog.HTTP_MANAGER, "on Mobile Data Click");
                IntentHelper.openDataUsageScreen(context);
            }
        });
        dialog.show();
    }

    @SuppressLint("LongLogTag")
    public static void unsuccessfulOperation(Context context, String unsuccessfulMessage) {
        Log.i(MyLog.HTTP_MANAGER, "Open Dialog Unsuccessful Operation");
        final Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        WindowManager.LayoutParams layoutParams = dialog.getWindow().getAttributes();
        layoutParams.dimAmount = 0.7f;
        dialog.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        dialog.setContentView(R.layout.custom_failed);

        Button close = dialog.findViewById(R.id.failedClose);
        TextView message = dialog.findViewById(R.id.failedTextView);

        message.setText(unsuccessfulMessage);

        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.show();
    }

    @SuppressLint("LongLogTag")
    public static void successfulOperation(Context context, String successfulMessage) {
        Log.i(MyLog.HTTP_MANAGER, "Open Dialog Successful Operation");
        final Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        WindowManager.LayoutParams layoutParams = dialog.getWindow().getAttributes();
        layoutParams.dimAmount = 0.7f;
        dialog.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        dialog.setContentView(R.layout.custom_successful);

        Button close = dialog.findViewById(R.id.customSuccessful_ButtonClose);
        TextView message = dialog.findViewById(R.id.customSuccessful_TextViewText);

        message.setText(successfulMessage);

        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.show();
    }

    public static String enCodeParameters(Map<String, String> params) {
        if (params != null) {
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
        }
        return "";
    }
}
