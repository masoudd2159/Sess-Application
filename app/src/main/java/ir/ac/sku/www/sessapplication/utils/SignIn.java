package ir.ac.sku.www.sessapplication.utils;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
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
import ir.ac.sku.www.sessapplication.adapters.FoodReservationAdapter;
import ir.ac.sku.www.sessapplication.fragment.FoodReservationFragment;
import ir.ac.sku.www.sessapplication.models.LoginInformation;
import ir.ac.sku.www.sessapplication.models.SendInformation;
import pl.droidsonroids.gif.GifImageView;

public class SignIn {

    //Views
    private ImageView captcha;
    private ImageView reCaptcha;
    private ImageView close;
    private GifImageView gifImageViewCaptcha;
    private GifImageView gifImageViewEnter;
    private EditText securityTag;
    private Button enter;

    //Required libraries
    private RequestQueue queue;
    private Gson gson;
    private Context context;

    //Preferences
    private SharedPreferences preferencesUsernameAndPassword;
    private SharedPreferences preferencesCookie;

    //my Class Model
    private LoginInformation loginInformation;
    private SendInformation sendInformation;

    private Dialog dialog;

    private final Object lock = new Object();

    @SuppressLint("LongLogTag")
    public SignIn(Context context) {
        this.context = context;

        loginInformation = new LoginInformation();
        sendInformation = new SendInformation();

        queue = Volley.newRequestQueue(context);

        gson = new Gson();

        preferencesUsernameAndPassword = context.getSharedPreferences(PreferenceName.USERNAME_AND_PASSWORD_PREFERENCE_NAME, Context.MODE_PRIVATE);
        preferencesCookie = context.getSharedPreferences(PreferenceName.COOKIE_PREFERENCE_NAME, Context.MODE_PRIVATE);
    }

    public void SignInDialog(final Handler handler) {
        dialog = new Dialog(context);
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        WindowManager.LayoutParams layoutParams = dialog.getWindow().getAttributes();
        layoutParams.dimAmount = 0.7f;
        dialog.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.dialog_captcha);

        gifImageViewCaptcha = dialog.findViewById(R.id.dialogCaptcha_GifImageViewCaptcha);
        gifImageViewEnter = dialog.findViewById(R.id.dialogCaptcha_GifImageViewEnter);
        captcha = dialog.findViewById(R.id.dialogCaptcha_ImageViewCaptcha);
        reCaptcha = dialog.findViewById(R.id.dialogCaptcha_ReCaptcha);
        close = dialog.findViewById(R.id.dialogCaptcha_Close);
        securityTag = dialog.findViewById(R.id.dialogCaptcha_EditTextCaptcha);
        enter = dialog.findViewById(R.id.dialogCaptcha_Enter);

        getLoginInformation();

        enter.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("LongLogTag")
            @Override
            public void onClick(View v) {
                securityTag.setEnabled(false);
                enter.setVisibility(View.INVISIBLE);
                gifImageViewEnter.setVisibility(View.VISIBLE);

                Log.i(MyLog.DIALOG_CAPTCHA, "Click on Enter Button");
                if (HttpManager.isNOTOnline(context)) {
                    Log.i(MyLog.DIALOG_CAPTCHA, "OFFLine");
                    HttpManager.noInternetAccess(context);
                } else {
                    Log.i(MyLog.DIALOG_CAPTCHA, "OnLine");
                    sendParamsPost(handler);
                }
            }
        });

        captcha.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("LongLogTag")
            @Override
            public void onClick(View v) {
                Log.i(MyLog.DIALOG_CAPTCHA, "Get Another Captcha");
                captcha.setImageBitmap(null);
                gifImageViewCaptcha.setVisibility(View.VISIBLE);
                getLoginInformation();
            }
        });


        securityTag.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @SuppressLint("LongLogTag")
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    securityTag.setEnabled(false);
                    enter.setVisibility(View.INVISIBLE);
                    gifImageViewEnter.setVisibility(View.VISIBLE);
                    Log.i(MyLog.DIALOG_CAPTCHA, "Click on Enter Button");
                    if (HttpManager.isNOTOnline(context)) {
                        Log.i(MyLog.DIALOG_CAPTCHA, "OFFLine");
                        HttpManager.noInternetAccess(context);
                    } else {
                        Log.i(MyLog.DIALOG_CAPTCHA, "OnLine");
                        sendParamsPost(handler);
                    }
                }
                return false;
            }
        });

        reCaptcha.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("LongLogTag")
            @Override
            public void onClick(View v) {
                Log.i(MyLog.DIALOG_CAPTCHA, "Get Another Captcha");
                captcha.setImageBitmap(null);
                gifImageViewCaptcha.setVisibility(View.VISIBLE);
                getLoginInformation();
            }
        });

        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.show();
    }

    @SuppressLint("LongLogTag")
    private void getLoginInformation() {
        Log.i(MyLog.DIALOG_CAPTCHA, "Run Request Cookie Function");
        StringRequest request = new StringRequest(MyConfig.LOGIN_INFORMATION,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.i(MyLog.DIALOG_CAPTCHA, "get JSON Cookie From Server");
                        loginInformation = gson.fromJson(response, LoginInformation.class);
                        if (loginInformation.isOk()) {
                            Log.i(MyLog.DIALOG_CAPTCHA, "Cookie : " + loginInformation.getCookie());
                            getCaptcha();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.i(MyLog.DIALOG_CAPTCHA, "ERROR" + error.getMessage());
                        new AlertDialog.Builder(context)
                                .setTitle("ERROR")
                                .setMessage(error.getMessage())
                                .setPositiveButton("Ok", null)
                                .setIcon(android.R.drawable.ic_dialog_alert)
                                .show();
                    }
                });
        queue.add(request);
        Log.i(MyLog.DIALOG_CAPTCHA, "Request Cookie Possess Added To queue");
    }

    @SuppressLint("LongLogTag")
    private void getCaptcha() {
        Log.i(MyLog.DIALOG_CAPTCHA, "Run Captcha Function");
        String captchaCookieURL = MyConfig.CAPTCHA_PICTURE + loginInformation.getCookie();

        ImageRequest imageRequest = new ImageRequest(captchaCookieURL,
                new Response.Listener<Bitmap>() {
                    @Override
                    public void onResponse(final Bitmap response) {
                        Log.i(MyLog.DIALOG_CAPTCHA, "get Captcha From Server");
                        gifImageViewCaptcha.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                Log.i(MyLog.DIALOG_CAPTCHA, "InVisible Gif Image View And Get Captcha");
                                gifImageViewCaptcha.setVisibility(View.INVISIBLE);
                                captcha.setVisibility(View.VISIBLE);
                                captcha.setImageBitmap(response);
                            }
                        }, 300);
                    }
                },
                150,
                50,
                ImageView.ScaleType.FIT_CENTER,
                Bitmap.Config.ARGB_8888,
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.i(MyLog.DIALOG_CAPTCHA, "ERROR : " + error.getMessage());
                        new AlertDialog.Builder(context)
                                .setTitle("ERROR")
                                .setMessage(error.getMessage())
                                .setPositiveButton("Ok", null)
                                .setIcon(android.R.drawable.ic_dialog_alert)
                                .show();
                    }
                });
        queue.add(imageRequest);
        Log.i(MyLog.DIALOG_CAPTCHA, "Request Possess Added To queue");
    }

    @SuppressLint("LongLogTag")
    private void sendParamsPost(final Handler handler) {
        Log.i(MyLog.DIALOG_CAPTCHA, "Run Function send Params Post");
        StringRequest stringRequest = new StringRequest(Request.Method.POST,
                MyConfig.SEND_INFORMATION,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.i(MyLog.DIALOG_CAPTCHA, "get Login Info");
                        try {
                            sendInformation = gson.fromJson(new String(response.getBytes("ISO-8859-1"), "UTF-8"), SendInformation.class);
                            if (sendInformation.isOk()) {
                                Log.i(MyLog.DIALOG_CAPTCHA, "All Params True");
                                @SuppressLint("CommitPrefEdits") SharedPreferences.Editor editorCookie = preferencesCookie.edit();
                                editorCookie.putString(PreferenceName.COOKIE_PREFERENCE_COOKIE, loginInformation.getCookie());
                                editorCookie.apply();
                                handler.onResponse(true, null);
                                dialog.dismiss();

                                InstantMessage instantMessage = new InstantMessage(context, sendInformation);
                                instantMessage.showInstantMessageDialog();

                            } else if (!sendInformation.isOk()) {
                                Toast.makeText(context, sendInformation.getDescription().getErrorText(), Toast.LENGTH_SHORT).show();
                                if (Integer.parseInt(sendInformation.getDescription().getErrorCode()) > 0) {
                                    securityTag.setEnabled(true);
                                    enter.setVisibility(View.VISIBLE);
                                    gifImageViewEnter.setVisibility(View.INVISIBLE);
                                    Log.i(MyLog.DIALOG_CAPTCHA, "Some Parameter is False");
                                    if (sendInformation.getDescription().getErrorCode().equals("1")) {
                                        Log.i(MyLog.DIALOG_CAPTCHA, "Username or password is incorrect");
                                    } else if (sendInformation.getDescription().getErrorCode().equals("2")) {
                                        Log.i(MyLog.DIALOG_CAPTCHA, "Captcha is Wrong");
                                        securityTag.setText("");
                                    }
                                } else if (Integer.parseInt(sendInformation.getDescription().getErrorCode()) < 0) {
                                    Log.i(MyLog.DIALOG_CAPTCHA, "Lost Cookie");
                                    securityTag.setText("");
                                    captcha.setImageBitmap(null);
                                    securityTag.setEnabled(true);
                                    enter.setVisibility(View.VISIBLE);
                                    gifImageViewEnter.setVisibility(View.INVISIBLE);
                                    gifImageViewCaptcha.setVisibility(View.VISIBLE);
                                    getLoginInformation();
                                }
                            }
                        } catch (UnsupportedEncodingException e) {
                            e.printStackTrace();
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.i(MyLog.DIALOG_CAPTCHA, error.getMessage());
                        new AlertDialog.Builder(context)
                                .setTitle("ERROR")
                                .setMessage(error.getMessage())
                                .setPositiveButton("Ok", null)
                                .setIcon(android.R.drawable.ic_dialog_alert)
                                .show();
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() {
                Log.i(MyLog.DIALOG_CAPTCHA, "Send Login Info");
                Map<String, String> params = new HashMap<String, String>();
                params.put("cookie", loginInformation.getCookie());
                params.put("captcha", securityTag.getText().toString().trim());
                params.put("username", "S" + preferencesUsernameAndPassword.getString(PreferenceName.USERNAME_AND_PASSWORD_PREFERENCE_USERNAME, null));
                params.put("password", preferencesUsernameAndPassword.getString(PreferenceName.USERNAME_AND_PASSWORD_PREFERENCE_PASSWORD, null));
                return params;
            }
        };
        queue.add(stringRequest);
        Log.i(MyLog.DIALOG_CAPTCHA, "Request Possess Added To queue");
    }
}
