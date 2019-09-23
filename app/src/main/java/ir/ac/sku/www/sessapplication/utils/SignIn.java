package ir.ac.sku.www.sessapplication.utils;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;

import com.airbnb.lottie.LottieAnimationView;
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
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;

import ir.ac.sku.www.sessapplication.API.MyConfig;
import ir.ac.sku.www.sessapplication.API.MyLog;
import ir.ac.sku.www.sessapplication.API.PreferenceName;
import ir.ac.sku.www.sessapplication.R;
import ir.ac.sku.www.sessapplication.models.LoginInformation;
import ir.ac.sku.www.sessapplication.models.SendInformation;

import static android.content.Context.MODE_PRIVATE;

public class SignIn {

    //Required libraries
    private Gson gson;
    private RequestQueue queue;
    private Context context;
    private Dialog dialog;

    //Preferences
    private CheckSignUpPreferenceManager manager;
    private SharedPreferences preferencesUsernameAndPassword;
    private SharedPreferences preferencesCookie;
    private SharedPreferences preferencesName;
    private SharedPreferences preferencesUserImage;
    private SharedPreferences preferencesMajor;

    //my Class Model
    private LoginInformation loginInformation;
    private SendInformation sendInformation;

    //dialog View
    private LottieAnimationView gifImageViewEnter;
    private EditText btn_Username;
    private EditText btn_Password;
    private ImageView close;
    private Button enter;

    @SuppressLint("LongLogTag")
    public SignIn(Context context) {

        Log.i(MyLog.SIGN_IN, "Sign In : Constructor");

        this.context = context;

        loginInformation = new LoginInformation();
        sendInformation = new SendInformation();

        gson = new Gson();
        queue = Volley.newRequestQueue(context);

        manager = new CheckSignUpPreferenceManager(context);
        preferencesUsernameAndPassword = context.getSharedPreferences(PreferenceName.USERNAME_AND_PASSWORD_PREFERENCE_NAME, MODE_PRIVATE);
        preferencesCookie = context.getSharedPreferences(PreferenceName.COOKIE_PREFERENCE_NAME, MODE_PRIVATE);
        preferencesName = context.getSharedPreferences(PreferenceName.NAME_PREFERENCE_NAME, MODE_PRIVATE);
        preferencesUserImage = context.getSharedPreferences(PreferenceName.USER_IMAGE_PREFERENCE_NAME, MODE_PRIVATE);
        preferencesMajor = context.getSharedPreferences(PreferenceName.MAJOR_PREFERENCE_NAME, MODE_PRIVATE);
    }

    @SuppressLint("LongLogTag")
    public void SignInDialog(final MyHandler handler) {
        Log.i(MyLog.SIGN_IN, "Sign In : SignInDialog");
        if (preferencesUsernameAndPassword.getString(PreferenceName.USERNAME_AND_PASSWORD_PREFERENCE_USERNAME, null) == null && preferencesUsernameAndPassword.getString(PreferenceName.USERNAME_AND_PASSWORD_PREFERENCE_PASSWORD, null) == null) {
            showUsernamePasswordDialog(handler);
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
            params.put("username", preferencesUsernameAndPassword.getString(PreferenceName.USERNAME_AND_PASSWORD_PREFERENCE_USERNAME, null));
            params.put("password", preferencesUsernameAndPassword.getString(PreferenceName.USERNAME_AND_PASSWORD_PREFERENCE_PASSWORD, null));

            WebService webService = new WebService(context);
            webService.requestPost(MyConfig.SEND_INFORMATION, Request.Method.POST, params, new MyHandler() {
                @SuppressLint("NewApi")
                @Override
                public void onResponse(boolean ok, Object obj) {
                    Log.i(MyLog.SIGN_IN, "get Login Info");
                    try {
                        sendInformation = gson.fromJson(new String(obj.toString().getBytes("ISO-8859-1"), "UTF-8"), SendInformation.class);
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }
                    if (sendInformation.isOk()) {
                        Log.i(MyLog.SIGN_IN, "All Params True");
                        @SuppressLint("CommitPrefEdits") SharedPreferences.Editor editorCookie = preferencesCookie.edit();
                        editorCookie.putString(PreferenceName.COOKIE_PREFERENCE_COOKIE, loginInformation.getCookie());
                        editorCookie.apply();
                        handler.onResponse(true, null);

                        InstantMessage instantMessage = new InstantMessage(context, sendInformation.getResult());
                        instantMessage.showInstantMessageDialog();

                        getUserImage();

                    } else if (!sendInformation.isOk()) {
                        Toast.makeText(context, sendInformation.getDescription().getErrorText(), Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }

    private void showUsernamePasswordDialog(final MyHandler handler) {
        dialog = new Dialog(context);
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        WindowManager.LayoutParams layoutParams = dialog.getWindow().getAttributes();
        layoutParams.dimAmount = 0.7f;
        dialog.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.dialog_username_password);

        gifImageViewEnter = dialog.findViewById(R.id.dialogUsernamePassword_GifImageViewEnter);
        btn_Username = dialog.findViewById(R.id.dialogUsernamePassword_EditTextUsername);
        btn_Password = dialog.findViewById(R.id.dialogUsernamePassword_EditTextPassword);
        close = dialog.findViewById(R.id.dialogUsernamePassword_Close);
        enter = dialog.findViewById(R.id.dialogUsernamePassword_Enter);

        enter.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("LongLogTag")
            @Override
            public void onClick(View v) {
                enter.setClickable(false);
                if (HttpManager.isNOTOnline(context)) {
                    HttpManager.noInternetAccess(context);
                } else {
                    enter.setVisibility(View.INVISIBLE);
                    gifImageViewEnter.setVisibility(View.VISIBLE);
                    gifImageViewEnter.setAnimation("loading_1.json");
                    gifImageViewEnter.playAnimation();
                    gifImageViewEnter.loop(true);
                    getLoginInformationUsernamePassword(handler, btn_Username.getText().toString().trim(), btn_Password.getText().toString().trim());
                }
            }
        });

        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                enter.setClickable(true);
            }
        });

        dialog.show();
    }


    private void getLoginInformationUsernamePassword(final MyHandler handler, final String username, final String password) {
        StringRequest request = new StringRequest(MyConfig.LOGIN_INFORMATION,
                new Response.Listener<String>() {
                    @SuppressLint("NewApi")
                    @Override
                    public void onResponse(String response) {
                        try {
                            loginInformation = gson.fromJson(new String(response.getBytes("ISO-8859-1"), "UTF-8"), LoginInformation.class);
                        } catch (UnsupportedEncodingException e) {
                            e.printStackTrace();
                        }

                        if (loginInformation.isOk()) {
                            sendParamsPostUsernamePassword(handler, username, password);
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        new AlertDialog.Builder(context)
                                .setTitle("ERROR")
                                .setMessage(error.getMessage())
                                .setPositiveButton("Ok", null)
                                .setIcon(android.R.drawable.ic_dialog_alert)
                                .show();
                    }
                });
        queue.add(request);
    }

    private void sendParamsPostUsernamePassword(final MyHandler handler, final String username, final String password) {
        if (loginInformation.getCookie() == null) {
            getLoginInformationUsernamePassword(handler, username, password);
        } else if (loginInformation.getCookie() != null) {

            HashMap<String, String> params = new HashMap<String, String>();
            params.put("cookie", loginInformation.getCookie());
            params.put("username", username);
            params.put("password", password);

            WebService webService = new WebService(context);
            webService.requestPost(MyConfig.SEND_INFORMATION, Request.Method.POST, params, new MyHandler() {
                @SuppressLint("NewApi")
                @Override
                public void onResponse(boolean ok, Object obj) {
                    try {
                        sendInformation = gson.fromJson(new String(obj.toString().getBytes("ISO-8859-1"), "UTF-8"), SendInformation.class);
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }
                    dialog.dismiss();

                    if (sendInformation.isOk()) {

                        @SuppressLint("CommitPrefEdits") SharedPreferences.Editor editorUserPass = preferencesUsernameAndPassword.edit();
                        editorUserPass.putString(PreferenceName.USERNAME_AND_PASSWORD_PREFERENCE_USERNAME, username);
                        editorUserPass.putString(PreferenceName.USERNAME_AND_PASSWORD_PREFERENCE_PASSWORD, password);
                        editorUserPass.apply();

                        @SuppressLint("CommitPrefEdits") SharedPreferences.Editor editorCookie = preferencesCookie.edit();
                        editorCookie.putString(PreferenceName.COOKIE_PREFERENCE_COOKIE, loginInformation.getCookie());
                        editorCookie.apply();

                        @SuppressLint("CommitPrefEdits") SharedPreferences.Editor editorName = preferencesName.edit();
                        editorName.putString(PreferenceName.NAME_PREFERENCE_FIRST_NAME, sendInformation.getResult().getUserInformation().getName());
                        editorName.putString(PreferenceName.NAME_PREFERENCE_LAST_NAME, sendInformation.getResult().getUserInformation().getFamily());
                        editorName.apply();

                        @SuppressLint("CommitPrefEdits") SharedPreferences.Editor editorMajor = preferencesMajor.edit();
                        editorMajor.putString(PreferenceName.MAJOR_PREFERENCE_MAJOR, sendInformation.getResult().getUserInformation().getMajor());
                        editorMajor.apply();

                        InstantMessage instantMessage = new InstantMessage(context, sendInformation.getResult());
                        instantMessage.showInstantMessageDialog();

                        getUserImage();

                        manager.setStartSignUpPreference(false);

                        getLoginInformation(handler);

                        CustomToastSuccess.success(context, " خوش آمدید " + sendInformation.getResult().getUserInformation().getName() + " " + sendInformation.getResult().getUserInformation().getFamily(), Toast.LENGTH_SHORT).show();


                    } else if (!sendInformation.isOk()) {
                        Toast.makeText(context, sendInformation.getDescription().getErrorText(), Toast.LENGTH_SHORT).show();
                        showUsernamePasswordDialog(handler);
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
                        @SuppressLint("CommitPrefEdits") SharedPreferences.Editor editorUserImage = preferencesUserImage.edit();
                        editorUserImage.putString(PreferenceName.USER_IMAGE_PREFERENCE_IMAGE, encodedImage);
                        editorUserImage.apply();
                    }
                });
    }
}
