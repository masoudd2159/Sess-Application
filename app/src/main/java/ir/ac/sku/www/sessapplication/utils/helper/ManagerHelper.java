package ir.ac.sku.www.sessapplication.utils.helper;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;

import com.airbnb.lottie.LottieAnimationView;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Map;

import ir.ac.sku.www.sessapplication.R;
import ir.ac.sku.www.sessapplication.api.MyLog;
import ir.ac.sku.www.sessapplication.fragment.dialogfragment.DialogFragmentNoInternetAccess;

public class ManagerHelper {

    @SuppressLint("LongLogTag")
    public static boolean isInternet(Context context) {
        Log.i(MyLog.HTTP_MANAGER, "Check device is Online");
        ConnectivityManager manager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = manager.getActiveNetworkInfo();
        return networkInfo == null || !networkInfo.isConnected();
    }

    public static boolean isInternetAvailable(Context context) {
        if (context != null) {
            Log.i(MyLog.SESS + ManagerHelper.class.getSimpleName(), "Check device for Internet Available");
            ConnectivityManager manager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            return manager.getActiveNetworkInfo() != null
                    && manager.getActiveNetworkInfo().isAvailable()
                    && manager.getActiveNetworkInfo().isConnected();
        }
        return false;
    }

    @SuppressLint("LongLogTag")
    public static void noInternetAccess(Context context) {
        Log.i(MyLog.SESS + ManagerHelper.class.getSimpleName(), "Open Dialog NO Internet Access");
        FragmentManager fragmentManager = ((FragmentActivity) context).getSupportFragmentManager();
        new DialogFragmentNoInternetAccess().show(fragmentManager, "DialogFragmentNoInternetAccess");
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
        TextView message = dialog.findViewById(R.id.customSuccessful_TextViewResult);
        LottieAnimationView animationView = dialog.findViewById(R.id.customSuccessful_AnimationViewSuccessful);

        animationView.setAnimation("success.json");
        animationView.playAnimation();
        animationView.loop(false);

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

    public static ColorMatrixColorFilter getBlackWhiteFilter() {
        ColorMatrix matrix = new ColorMatrix();
        matrix.setSaturation(0);
        return new ColorMatrixColorFilter(matrix);
    }

    public static boolean checkInternetServices(Context context){
        if (isInternetAvailable(context)) {
            return true;
        } else {
            noInternetAccess(context);
            return false;
        }
    }

    public void checkLogin(Context context, String errorText, String errorCode) {
    }
}
