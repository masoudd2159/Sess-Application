package ir.ac.sku.www.sessapplication.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.util.Base64;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.google.gson.Gson;

import java.io.ByteArrayOutputStream;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;

import ir.ac.sku.www.sessapplication.api.MyConfig;
import ir.ac.sku.www.sessapplication.api.MyLog;
import ir.ac.sku.www.sessapplication.api.PreferenceName;
import ir.ac.sku.www.sessapplication.fragment.SignInDialogFragment;
import ir.ac.sku.www.sessapplication.fragment.dialogfragment.DialogFragmentInstantMessage;
import ir.ac.sku.www.sessapplication.model.LoginInformation;
import ir.ac.sku.www.sessapplication.model.SendInformation;

import static android.content.Context.MODE_PRIVATE;

public class SignIn {

    //Required libraries
    private Gson gson;
    private RequestQueue queue;
    private Context context;

    //Preferences
    private CheckSignUpPreferenceManager manager;
    private SharedPreferences preferencesUserInformation;
    private SharedPreferences.Editor editSharedPreferences;

    //my Class Model
    private LoginInformation loginInformation;
    private SendInformation sendInformation;

    @SuppressLint({"LongLogTag", "CommitPrefEdits"})
    public SignIn(Context context) {

        Log.i(MyLog.SIGN_IN, "Sign In : Constructor");

        this.context = context;

        loginInformation = new LoginInformation();
        sendInformation = new SendInformation();

        gson = new Gson();
        queue = Volley.newRequestQueue(context);

        manager = new CheckSignUpPreferenceManager(context);
        preferencesUserInformation = context.getSharedPreferences(PreferenceName.PREFERENCE_USER_INFORMATION, MODE_PRIVATE);
        editSharedPreferences = preferencesUserInformation.edit();
    }

    @SuppressLint("LongLogTag")
    public void SignInDialog(final MyHandler handler) {
        Log.i(MyLog.SIGN_IN, "Sign In : SignInDialog");
        if (preferencesUserInformation.getString(PreferenceName.PREFERENCE_USERNAME, null) == null && preferencesUserInformation.getString(PreferenceName.PREFERENCE_PASSWORD, null) == null) {
            FragmentManager fragmentManager = ((FragmentActivity) context).getSupportFragmentManager();
            SignInDialogFragment fragment = new SignInDialogFragment(handler);
            fragment.show(fragmentManager, "addUserPersonalInfo");
        } else {
            getLoginInformation(handler);
        }
    }

    @SuppressLint("LongLogTag")
    private void getLoginInformation(final MyHandler handler) {
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
    private void sendParamsPost(final MyHandler handler) {
        if (loginInformation.getCookie() == null) {
            if (HttpManager.isNOTOnline(context)) {
                HttpManager.noInternetAccess(context);
            } else {
                getLoginInformation(handler);
            }
        } else if (loginInformation.getCookie() != null) {
            Log.i(MyLog.SIGN_IN, "Run Function send Params Post");

            HashMap<String, String> params = new HashMap<String, String>();
            params.put("cookie", loginInformation.getCookie());
            params.put("username", preferencesUserInformation.getString(PreferenceName.PREFERENCE_USERNAME, null));
            params.put("password", preferencesUserInformation.getString(PreferenceName.PREFERENCE_PASSWORD, null));

            WebService webService = new WebService(context);
            webService.requestPost(MyConfig.SEND_INFORMATION, Request.Method.POST, params, new MyHandler() {
                @SuppressLint("NewApi")
                @Override
                public void onResponse(boolean ok, Object obj) {
                    Log.i(MyLog.SIGN_IN, "get Login Info");
                    sendInformation = gson.fromJson(new String(obj.toString().getBytes(StandardCharsets.ISO_8859_1), StandardCharsets.UTF_8), SendInformation.class);
                    if (sendInformation.isOk()) {
                        Log.i(MyLog.SIGN_IN, "All Params True");
                        editSharedPreferences.putString(PreferenceName.PREFERENCE_COOKIE, loginInformation.getCookie());
                        editSharedPreferences.apply();
                        handler.onResponse(true, null);

                        if (sendInformation.getResult().getInstantMessage().size() > 0) {
                            FragmentManager fragmentManager = ((FragmentActivity) context).getSupportFragmentManager();
                            new DialogFragmentInstantMessage(sendInformation.getResult()).show(fragmentManager, "DialogFragmentInstantMessage");
                        }

                        getUserImage();

                    } else if (!sendInformation.isOk()) {
                        Toast.makeText(context, sendInformation.getDescription().getErrorText(), Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }

    @SuppressLint("LongLogTag")
    private void getUserImage() {
        Glide.with(context)
                .asBitmap()
                .load(sendInformation.getResult().getUserInformation().getImage() + "?cookie=" + loginInformation.getCookie())
                .into(new SimpleTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                        resource.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
                        byte[] b = byteArrayOutputStream.toByteArray();
                        String encodedImage = Base64.encodeToString(b, Base64.DEFAULT);
                        editSharedPreferences.putString(PreferenceName.PREFERENCE_IMAGE, encodedImage);
                        editSharedPreferences.apply();
                    }
                });
    }
}
