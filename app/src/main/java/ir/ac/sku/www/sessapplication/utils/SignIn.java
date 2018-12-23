package ir.ac.sku.www.sessapplication.utils;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

import ir.ac.sku.www.sessapplication.API.MyConfig;
import ir.ac.sku.www.sessapplication.API.MyLog;
import ir.ac.sku.www.sessapplication.API.PreferenceName;
import ir.ac.sku.www.sessapplication.R;
import ir.ac.sku.www.sessapplication.activities.BottomBarActivity;
import ir.ac.sku.www.sessapplication.activities.LoginActivity;
import ir.ac.sku.www.sessapplication.adapters.FoodReservationAdapter;
import ir.ac.sku.www.sessapplication.fragment.FoodReservationFragment;
import ir.ac.sku.www.sessapplication.models.LoginInformation;
import ir.ac.sku.www.sessapplication.models.SendInformation;
import pl.droidsonroids.gif.GifImageView;

public class SignIn {

    //Required libraries
    private Gson gson;
    private RequestQueue queue;
    private Context context;

    //Preferences
    private SharedPreferences preferencesUsernameAndPassword;
    private SharedPreferences preferencesCookie;

    //my Class Model
    private LoginInformation loginInformation;
    private SendInformation sendInformation;

    @SuppressLint("LongLogTag")
    public SignIn(Context context) {

        Log.i(MyLog.SIGN_IN, "Sign In : Constructor");

        this.context = context;

        loginInformation = new LoginInformation();
        sendInformation = new SendInformation();

        gson = new Gson();
        queue = Volley.newRequestQueue(context);

        preferencesUsernameAndPassword = context.getSharedPreferences(PreferenceName.USERNAME_AND_PASSWORD_PREFERENCE_NAME, Context.MODE_PRIVATE);
        preferencesCookie = context.getSharedPreferences(PreferenceName.COOKIE_PREFERENCE_NAME, Context.MODE_PRIVATE);
    }

    @SuppressLint("LongLogTag")
    public void SignInDialog(final Handler handler) {
        Log.i(MyLog.SIGN_IN, "Sign In : SignInDialog");
        getLoginInformation(handler);
    }

    @SuppressLint("LongLogTag")
    private void getLoginInformation(final Handler handler) {
        Log.i(MyLog.SIGN_IN, "Run Request Cookie Function");

        StringRequest request = new StringRequest(MyConfig.LOGIN_INFORMATION,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        Log.i(MyLog.SIGN_IN, "get JSON Cookie From Server");
                        loginInformation = gson.fromJson(response, LoginInformation.class);

                        if (loginInformation.isOk()) {
                            Log.i(MyLog.SIGN_IN, "Cookie : " + loginInformation.getCookie());

                            if (HttpManager.isNOTOnline(context)) {
                                Log.i(MyLog.SIGN_IN, "OFFLine");
                                HttpManager.noInternetAccess(context);
                            } else {
                                Log.i(MyLog.SIGN_IN, "OnLine");
                                sendParamsPost(handler);
                            }
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.i(MyLog.SIGN_IN, "ERROR" + error.getMessage());
                        new AlertDialog.Builder(context)
                                .setTitle("ERROR")
                                .setMessage(error.getMessage())
                                .setPositiveButton("Ok", null)
                                .setIcon(android.R.drawable.ic_dialog_alert)
                                .show();
                    }
                });
        queue.add(request);
        Log.i(MyLog.SIGN_IN, "Request Cookie Possess Added To queue");
    }

    @SuppressLint("LongLogTag")
    private void sendParamsPost(final Handler handler) {
        if (loginInformation.getCookie() == null) {
            if (HttpManager.isNOTOnline(context)) {
                HttpManager.noInternetAccess(context);
            } else {
                getLoginInformation(handler);
            }
        } else if (loginInformation.getCookie() != null) {
            Log.i(MyLog.SIGN_IN, "Run Function send Params Post");

            Map<String, String> params = new HashMap<String, String>();
            params.put("cookie", loginInformation.getCookie());
            params.put("username", "S" + preferencesUsernameAndPassword.getString(PreferenceName.USERNAME_AND_PASSWORD_PREFERENCE_USERNAME, null));
            params.put("password", preferencesUsernameAndPassword.getString(PreferenceName.USERNAME_AND_PASSWORD_PREFERENCE_PASSWORD, null));

            WebService webService = new WebService(context);
            webService.requestPost(MyConfig.SEND_INFORMATION, Request.Method.POST, (HashMap<String, String>) params, new Handler() {
                @Override
                public void onResponse(boolean ok, Object obj) {
                    Log.i(MyLog.SIGN_IN, "get Login Info");
                    try {
                        sendInformation = gson.fromJson(new String(obj.toString().getBytes("ISO-8859-1"), "UTF-8"), SendInformation.class);
                        if (sendInformation.isOk()) {
                            Log.i(MyLog.SIGN_IN, "All Params True");
                            @SuppressLint("CommitPrefEdits") SharedPreferences.Editor editorCookie = preferencesCookie.edit();
                            editorCookie.putString(PreferenceName.COOKIE_PREFERENCE_COOKIE, loginInformation.getCookie());
                            editorCookie.apply();
                            handler.onResponse(true, null);

                            InstantMessage instantMessage = new InstantMessage(context, sendInformation.getResult());
                            instantMessage.showInstantMessageDialog();

                        } else if (!sendInformation.isOk()) {
                            Toast.makeText(context, sendInformation.getDescription().getErrorText(), Toast.LENGTH_SHORT).show();
                        }
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }
                }
            });
        }
    }
}
