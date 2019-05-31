package ir.ac.sku.www.sessapplication.activities;

import android.animation.ArgbEvaluator;
import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.util.Base64;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.Transformation;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.target.Target;
import com.bumptech.glide.request.transition.Transition;
import com.google.gson.Gson;

import java.io.ByteArrayOutputStream;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import ir.ac.sku.www.sessapplication.API.MyConfig;
import ir.ac.sku.www.sessapplication.API.MyLog;
import ir.ac.sku.www.sessapplication.API.PreferenceName;
import ir.ac.sku.www.sessapplication.R;
import ir.ac.sku.www.sessapplication.models.AppInfo;
import ir.ac.sku.www.sessapplication.models.LoginInformation;
import ir.ac.sku.www.sessapplication.models.SendInformation;
import ir.ac.sku.www.sessapplication.utils.CheckSignUpPreferenceManager;
import ir.ac.sku.www.sessapplication.utils.CustomToastExit;
import ir.ac.sku.www.sessapplication.utils.CustomToastSuccess;
import ir.ac.sku.www.sessapplication.utils.Handler;
import ir.ac.sku.www.sessapplication.utils.HttpManager;
import ir.ac.sku.www.sessapplication.utils.MyActivity;
import ir.ac.sku.www.sessapplication.utils.WebService;
import pl.droidsonroids.gif.GifImageView;

public class LoginActivity extends MyActivity {

    private SwipeRefreshLayout swipeRefreshLayout;
    private EditText user;
    private EditText password;
    private GifImageView gifImageViewEnter;
    private Button enter;
    private Button guest;
    private ScrollView scrollView;
    private View loginView;
    private ProgressDialog progressDialog;

    //Required libraries
    private RequestQueue queue;
    private Gson gson;
    private CheckSignUpPreferenceManager manager;
    private SharedPreferences preferencesUsernameAndPassword;
    private SharedPreferences preferencesCookie;
    private SharedPreferences preferencesName;
    private SharedPreferences preferencesUserImage;
    private SharedPreferences preferencesMajor;

    //my Class Model
    private LoginInformation loginInformation;
    private SendInformation sendInformation;
    private AppInfo appInfo;
    private boolean doubleBackToExitPressedOnce = false;

    @SuppressLint("LongLogTag")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Log.i(MyLog.LOGIN_ACTIVITY, "___Login Activity___");

        //allocate classes
        loginInformation = new LoginInformation();
        sendInformation = new SendInformation();
        Intent intentAppInfo = getIntent();
        appInfo = intentAppInfo.getParcelableExtra("AppInfo");

        //create queue for API Request
        queue = Volley.newRequestQueue(LoginActivity.this);

        //use Gson Lib
        gson = new Gson();
        manager = new CheckSignUpPreferenceManager(LoginActivity.this);
        preferencesUsernameAndPassword = getSharedPreferences(PreferenceName.USERNAME_AND_PASSWORD_PREFERENCE_NAME, MODE_PRIVATE);
        preferencesCookie = getSharedPreferences(PreferenceName.COOKIE_PREFERENCE_NAME, MODE_PRIVATE);
        preferencesName = getSharedPreferences(PreferenceName.NAME_PREFERENCE_NAME, MODE_PRIVATE);
        preferencesUserImage = getSharedPreferences(PreferenceName.USER_IMAGE_PREFERENCE_NAME, MODE_PRIVATE);
        preferencesMajor = getSharedPreferences(PreferenceName.MAJOR_PREFERENCE_NAME, MODE_PRIVATE);

        progressDialog = new ProgressDialog(LoginActivity.this);

        //my Functions
        changeStatusBarColor();
        init();

        //animate Text View
        View.OnFocusChangeListener focusChangeListener = new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    animateOnFocus(v);
                } else {
                    animateOnFocusLost(v);
                }
            }
        };

        user.setOnFocusChangeListener(focusChangeListener);
        password.setOnFocusChangeListener(focusChangeListener);


        password.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    Log.i(MyLog.LOGIN_ACTIVITY, "Click on Editor Action Listener");

                    if (HttpManager.isNOTOnline(LoginActivity.this)) {
                        Log.i(MyLog.LOGIN_ACTIVITY, "OFFLine");
                        HttpManager.noInternetAccess(LoginActivity.this);
                    } else {
                        Log.i(MyLog.LOGIN_ACTIVITY, "OnLine");
                        sendParamsPost();
                    }
                }
                return false;
            }
        });

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                Log.i(MyLog.LOGIN_ACTIVITY, "Swipe Refresh Layout");

                if (HttpManager.isNOTOnline(LoginActivity.this)) {
                    Log.i(MyLog.LOGIN_ACTIVITY, "OFFLine");
                    HttpManager.noInternetAccess(LoginActivity.this);
                } else {
                    Log.i(MyLog.LOGIN_ACTIVITY, "OnLine");
                    getLoginInformation(false);
                    swipeRefreshLayout.setRefreshing(false);
                }
            }
        });

        guest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (appInfo.getResult().getGuestLogin()) {
                    Intent intent = new Intent(LoginActivity.this, BottomBarActivity.class);

                    preferencesUsernameAndPassword.edit().clear().apply();
                    preferencesCookie.edit().clear().apply();
                    preferencesName.edit().clear().apply();
                    preferencesUserImage.edit().clear().apply();
                    preferencesMajor.edit().clear().apply();


                    startActivity(intent);

                    CustomToastSuccess.success(LoginActivity.this, " خوش آمدید ", Toast.LENGTH_SHORT).show();

                    finish();
                } else {
                    HttpManager.unsuccessfulOperation(LoginActivity.this, "لطفا از طریق شناسه کاربری و رمز عبور خود وارد شوید.");
                }
            }
        });
    }

    @SuppressLint("LongLogTag")
    private void changeStatusBarColor() {
        Log.i(MyLog.LOGIN_ACTIVITY, "Change Status Bar");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.TRANSPARENT);
        }
    }

    @SuppressLint("LongLogTag")
    private void init() {
        swipeRefreshLayout = findViewById(R.id.loginActivity_SwipeRefreshLayout);
        user = findViewById(R.id.loginActivity_Username);
        password = findViewById(R.id.loginActivity_Password);
        gifImageViewEnter = findViewById(R.id.loginActivity_GifImageViewEnter);
        enter = findViewById(R.id.loginActivity_Enter);
        scrollView = findViewById(R.id.loginActivity_ScrollView);
        loginView = findViewById(R.id.loginActivity_View);
        guest = findViewById(R.id.loginActivity_ButtonGuest);
    }

    @SuppressLint("LongLogTag")
    private void getLoginInformation(final boolean where) {
        Log.i(MyLog.LOGIN_ACTIVITY, "Run Request getLoginInformation Function");

        progressDialog.setMessage("لطفا منتظر بمانید!");
        progressDialog.setCancelable(false);
        progressDialog.show();


        StringRequest request = new StringRequest(MyConfig.LOGIN_INFORMATION,
                new Response.Listener<String>() {
                    @SuppressLint("NewApi")
                    @Override
                    public void onResponse(String response) {
                        progressDialog.dismiss();

                        Log.i(MyLog.LOGIN_ACTIVITY, "get JSON Cookie From Server");
                        loginInformation = gson.fromJson(new String(response.getBytes(StandardCharsets.ISO_8859_1), StandardCharsets.UTF_8), LoginInformation.class);

                        if (loginInformation.isOk()) {
                            Log.i(MyLog.LOGIN_ACTIVITY, "Cookie : " + loginInformation.getCookie());

                            if (HttpManager.isNOTOnline(LoginActivity.this)) {
                                Log.i(MyLog.LOGIN_ACTIVITY, "OFFLine");
                                HttpManager.noInternetAccess(LoginActivity.this);
                            } else {
                                Log.i(MyLog.LOGIN_ACTIVITY, "OnLine");
                                if (where) {
                                    sendParamsPost();
                                }
                            }
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.i(MyLog.LOGIN_ACTIVITY, "Login Information Volley Error : " + error.getMessage());
                        new AlertDialog.Builder(LoginActivity.this)
                                .setTitle("ERROR")
                                .setMessage(error.getMessage())
                                .setPositiveButton("Ok", null)
                                .setIcon(android.R.drawable.ic_dialog_alert)
                                .show();
                    }
                });
        queue.add(request);
        Log.i(MyLog.LOGIN_ACTIVITY, "Request Cookie Possess Added To queue");
    }

    @SuppressLint("LongLogTag")
    public void onEnterClickListener(View view) {
        Log.i(MyLog.LOGIN_ACTIVITY, "Click on Enter Button");

        if (HttpManager.isNOTOnline(LoginActivity.this)) {
            Log.i(MyLog.LOGIN_ACTIVITY, "OFFLine");
            HttpManager.noInternetAccess(LoginActivity.this);
        } else {
            Log.i(MyLog.LOGIN_ACTIVITY, "OnLine");
            sendParamsPost();
        }
    }

    @SuppressLint("LongLogTag")
    private void sendParamsPost() {
        if (loginInformation.getCookie() == null) {
            if (HttpManager.isNOTOnline(LoginActivity.this)) {
                Log.i(MyLog.LOGIN_ACTIVITY, "OFFLine");
                HttpManager.noInternetAccess(LoginActivity.this);
            } else {
                Log.i(MyLog.LOGIN_ACTIVITY, "OnLine");
                getLoginInformation(true);
            }
        } else if (loginInformation.getCookie() != null) {

            user.setEnabled(false);
            password.setEnabled(false);
            enter.setVisibility(View.INVISIBLE);
            gifImageViewEnter.setVisibility(View.VISIBLE);

            Log.i(MyLog.LOGIN_ACTIVITY, "Run Function Send Params Post");

            HashMap<String, String> params = new HashMap<String, String>();
            params.put("cookie", loginInformation.getCookie());
            params.put("username", user.getText().toString().trim());
            params.put("password", password.getText().toString().trim());

            WebService webService = new WebService(LoginActivity.this);
            webService.requestPost(MyConfig.SEND_INFORMATION, Request.Method.POST, params, new Handler() {
                @SuppressLint("NewApi")
                @Override
                public void onResponse(boolean ok, Object obj) {
                    Log.i(MyLog.LOGIN_ACTIVITY, "get Login Info");
                    sendInformation = gson.fromJson(new String(obj.toString().getBytes(StandardCharsets.ISO_8859_1), StandardCharsets.UTF_8), SendInformation.class);

                    if (sendInformation.isOk()) {
                        Log.i(MyLog.LOGIN_ACTIVITY, "All Params True");

                        Intent intent = new Intent(LoginActivity.this, BottomBarActivity.class).putExtra("InstantMessage", sendInformation.getResult());

                        @SuppressLint("CommitPrefEdits") SharedPreferences.Editor editorUserPass = preferencesUsernameAndPassword.edit();
                        editorUserPass.putString(PreferenceName.USERNAME_AND_PASSWORD_PREFERENCE_USERNAME, user.getText().toString().trim());
                        editorUserPass.putString(PreferenceName.USERNAME_AND_PASSWORD_PREFERENCE_PASSWORD, password.getText().toString().trim());
                        editorUserPass.apply();

                        Log.i(MyLog.LOGIN_ACTIVITY, "Username : " + user.getText().toString().trim());
                        Log.i(MyLog.LOGIN_ACTIVITY, "Password : " + password.getText().toString().trim());

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

                        getUserImage();

                        manager.setStartSignUpPreference(false);

                        startActivity(intent);

                        CustomToastSuccess.success(LoginActivity.this, " خوش آمدید " + sendInformation.getResult().getUserInformation().getName() + " " + sendInformation.getResult().getUserInformation().getFamily(), Toast.LENGTH_SHORT).show();

                        finish();

                    } else if (!sendInformation.isOk()) {
                        Log.i(MyLog.LOGIN_ACTIVITY, "Some Parameters False");

                        Log.i(MyLog.LOGIN_ACTIVITY, "Error Code : " + sendInformation.getDescription().getErrorCode());
                        Log.i(MyLog.LOGIN_ACTIVITY, "Error Text : " + sendInformation.getDescription().getErrorText());

                        HttpManager.unsuccessfulOperation(LoginActivity.this, sendInformation.getDescription().getErrorText());
                        getLoginInformation(false);

                        user.setEnabled(true);
                        password.setEnabled(true);

                        user.setText("");
                        password.setText("");

                        user.requestFocus();

                        enter.setVisibility(View.VISIBLE);
                        gifImageViewEnter.setVisibility(View.INVISIBLE);
                    }
                }
            });
        }
    }

    @SuppressLint("LongLogTag")
    private void getUserImage() {
        Glide.with(LoginActivity.this)
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

    private void animateOnFocus(View v) {
        final CardView first_container = (CardView) v.getParent();
        final CardView second_container = (CardView) first_container.getParent();

        final int first_curr_radius = (int) getResources().getDimension(R.dimen.first_card_radius);
        final int first_target_radius = (int) getResources().getDimension(R.dimen.first_card_radius_on_focus);

        final int second_curr_radius = (int) getResources().getDimension(R.dimen.second_card_radius);
        final int second_target_radius = (int) getResources().getDimension(R.dimen.second_card_radius_on_focus);

        final int first_curr_color = ContextCompat.getColor(this, android.R.color.transparent);
        final int first_target_color = R.color.blue;

        final int second_curr_color = ContextCompat.getColor(this, R.color.backgroundEditText);
        final int second_target_color = ContextCompat.getColor(this, android.R.color.white);

        ValueAnimator first_anim = new ValueAnimator();
        first_anim.setIntValues(first_curr_color, first_target_color);
        first_anim.setEvaluator(new ArgbEvaluator());
        first_anim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                first_container.setCardBackgroundColor((Integer) animation.getAnimatedValue());
            }
        });

        ValueAnimator second_anim = new ValueAnimator();
        second_anim.setIntValues(second_curr_color, second_target_color);
        second_anim.setEvaluator(new ArgbEvaluator());
        second_anim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                second_container.setCardBackgroundColor((Integer) animation.getAnimatedValue());
            }
        });

        Animation a = new Animation() {
            @Override
            protected void applyTransformation(float interpolatedTime, Transformation t) {
                int diff_radius = first_curr_radius - first_target_radius;
                int radius = first_target_radius + (int) (diff_radius - (diff_radius * interpolatedTime));

                first_container.setRadius(radius);
                first_container.requestLayout();

                diff_radius = second_curr_radius - second_target_radius;
                radius = second_target_radius + (int) (diff_radius - (diff_radius * interpolatedTime));

                second_container.setRadius(radius);
                second_container.requestLayout();

            }

            @Override
            public boolean willChangeBounds() {
                return true;
            }
        };

        a.setDuration(200);
        first_anim.setDuration(200);
        second_anim.setDuration(200);

        first_anim.start();
        second_anim.start();
        first_container.startAnimation(a);
    }

    private void animateOnFocusLost(View v) {
        final CardView first_container = (CardView) v.getParent();
        final CardView second_container = (CardView) first_container.getParent();

        final int first_curr_radius = (int) getResources().getDimension(R.dimen.first_card_radius_on_focus);
        final int first_target_radius = (int) getResources().getDimension(R.dimen.first_card_radius);

        final int second_curr_radius = (int) getResources().getDimension(R.dimen.second_card_radius_on_focus);
        final int second_target_radius = (int) getResources().getDimension(R.dimen.second_card_radius);

        final int first_curr_color = R.color.blue;
        final int first_target_color = ContextCompat.getColor(this, android.R.color.transparent);

        final int second_curr_color = ContextCompat.getColor(this, android.R.color.white);
        final int second_target_color = ContextCompat.getColor(this, R.color.backgroundEditText);

        ValueAnimator first_anim = new ValueAnimator();
        first_anim.setIntValues(first_curr_color, first_target_color);
        first_anim.setEvaluator(new ArgbEvaluator());
        first_anim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                first_container.setCardBackgroundColor((Integer) animation.getAnimatedValue());
            }
        });

        ValueAnimator second_anim = new ValueAnimator();
        second_anim.setIntValues(second_curr_color, second_target_color);
        second_anim.setEvaluator(new ArgbEvaluator());
        second_anim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                second_container.setCardBackgroundColor((Integer) animation.getAnimatedValue());
            }
        });

        Animation a = new Animation() {
            @Override
            protected void applyTransformation(float interpolatedTime, Transformation t) {
                int diff_radius = first_curr_radius - first_target_radius;
                int radius = first_target_radius + (int) (diff_radius - (diff_radius * interpolatedTime));

                first_container.setRadius(radius);
                first_container.requestLayout();

                diff_radius = second_curr_radius - second_target_radius;
                radius = second_target_radius + (int) (diff_radius - (diff_radius * interpolatedTime));

                second_container.setRadius(radius);
                second_container.requestLayout();

            }

            @Override
            public boolean willChangeBounds() {
                return true;
            }
        };

        a.setDuration(200);
        first_anim.setDuration(200);
        second_anim.setDuration(200);

        first_anim.start();
        second_anim.start();
        first_container.startAnimation(a);

    }

    @SuppressLint({"LongLogTag", "NewApi"})
    @Override
    public void onBackPressed() {
        Log.i(MyLog.LOGIN_ACTIVITY, "onBackPressed");
        if (doubleBackToExitPressedOnce) {
            Log.i(MyLog.LOGIN_ACTIVITY, "Exit form App");
            super.onBackPressed();
            finish();
            finishAffinity();
            finishAndRemoveTask();
            System.exit(0);
            return;
        }

        this.doubleBackToExitPressedOnce = true;

        CustomToastExit.exit(LoginActivity.this, "برای خروج برنامه دو بار کلیک بازگشت را فشار دهید", Toast.LENGTH_SHORT).show();

        new android.os.Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce = false;
            }
        }, 2000);
    }

    @SuppressLint("LongLogTag")
    @Override
    protected void onResume() {
        super.onResume();
        Log.i(MyLog.LOGIN_ACTIVITY, "onResume");

        if (HttpManager.isNOTOnline(LoginActivity.this)) {
            Log.i(MyLog.LOGIN_ACTIVITY, "OFFLine");
            HttpManager.noInternetAccess(LoginActivity.this);
        } else {
            Log.i(MyLog.LOGIN_ACTIVITY, "OnLine");
            getLoginInformation(false);
        }
    }
}
