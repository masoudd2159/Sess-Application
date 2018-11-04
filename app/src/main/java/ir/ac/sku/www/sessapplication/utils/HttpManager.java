package ir.ac.sku.www.sessapplication.utils;

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

    public static boolean isNOTOnline(Context context) {
        Log.i(MyLog.SESS, "Check device is Online");
        ConnectivityManager manager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = manager.getActiveNetworkInfo();
        return networkInfo == null || !networkInfo.isConnected();
    }

    public static void noInternetAccess(final Context context) {
        Log.i(MyLog.SESS, "Open Dialog NO Internet Access");
        final Dialog dialog = new Dialog(context);
        dialog.setContentView(R.layout.custom_disconnect);

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

    public static void unsuccessfulOperation(Context context, String unsuccessfulMessage) {
        Log.i(MyLog.SESS, "Open Dialog NO Internet Access");
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
