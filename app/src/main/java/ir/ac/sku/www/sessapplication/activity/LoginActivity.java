package ir.ac.sku.www.sessapplication.activity;

import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.Transformation;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.vectordrawable.graphics.drawable.ArgbEvaluator;

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
import java.nio.charset.StandardCharsets;
import java.util.HashMap;

import ir.ac.sku.www.sessapplication.R;
import ir.ac.sku.www.sessapplication.api.ApplicationAPI;
import ir.ac.sku.www.sessapplication.api.MyLog;
import ir.ac.sku.www.sessapplication.base.BaseActivity;
import ir.ac.sku.www.sessapplication.model.LoginInformation;
import ir.ac.sku.www.sessapplication.model.SendInformation;
import ir.ac.sku.www.sessapplication.utils.CustomToastExit;
import ir.ac.sku.www.sessapplication.utils.CustomToastSuccess;
import ir.ac.sku.www.sessapplication.utils.MyHandler;
import ir.ac.sku.www.sessapplication.utils.WebService;
import ir.ac.sku.www.sessapplication.utils.helper.ManagerHelper;

public class LoginActivity extends BaseActivity {

    private SwipeRefreshLayout swipeRefreshLayout;
    private EditText user;
    private EditText password;
    private LottieAnimationView gifImageViewEnter;
    private Button enter;
    private Button guest;
    private ScrollView scrollView;
    private View loginView;
    private ProgressDialog progressDialog;

    //Required libraries
    private RequestQueue queue;
    private Gson gson;


    //my Class Model
    private LoginInformation loginInformation;
    private SendInformation sendInformation;
    private boolean appInfo;
    private boolean doubleBackToExitPressedOnce = false;

    @Override protected int getLayoutResource() {
        return R.layout.activity_login;
    }

    @SuppressLint({"LongLogTag", "CommitPrefEdits"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i(MyLog.LOGIN_ACTIVITY, "___Login Activity___");

        //allocate classes
        loginInformation = new LoginInformation();
        sendInformation = new SendInformation();
        appInfo = getIntent().getBooleanExtra("isGuestLogin", true);

        //create queue for API Request
        queue = Volley.newRequestQueue(LoginActivity.this);

        //use Gson Lib
        gson = new Gson();

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

                    if (ManagerHelper.isInternet(LoginActivity.this)) {
                        Log.i(MyLog.LOGIN_ACTIVITY, "OFFLine");
                        ManagerHelper.noInternetAccess(LoginActivity.this);
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

                if (ManagerHelper.isInternet(LoginActivity.this)) {
                    Log.i(MyLog.LOGIN_ACTIVITY, "OFFLine");
                    ManagerHelper.noInternetAccess(LoginActivity.this);
                } else {
                    Log.i(MyLog.LOGIN_ACTIVITY, "OnLine");
                    getLoginInformation(false);
                    swipeRefreshLayout.setRefreshing(false);
                    enter.setVisibility(View.VISIBLE);
                    gifImageViewEnter.setVisibility(View.INVISIBLE);
                }
            }
        });

        guest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (appInfo) {
                    Intent intent = new Intent(LoginActivity.this, BottomBarActivity.class);
                    preferencesUtils.clearSharedPreferences();
                    startActivity(intent);

                    CustomToastSuccess.success(LoginActivity.this, " خوش آمدید ", Toast.LENGTH_SHORT).show();

                    finish();
                } else {
                    ManagerHelper.unsuccessfulOperation(LoginActivity.this, "لطفا از طریق شناسه کاربری و رمز عبور خود وارد شوید.");
                }
            }
        });
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


        StringRequest request = new StringRequest(ApplicationAPI.LOGIN_INFORMATION,
                new Response.Listener<String>() {
                    @SuppressLint("NewApi")
                    @Override
                    public void onResponse(String response) {
                        progressDialog.dismiss();
                        Log.i(MyLog.LOGIN_ACTIVITY, "get JSON Cookie From Server");
                        loginInformation = gson.fromJson(new String(response.getBytes(StandardCharsets.ISO_8859_1), StandardCharsets.UTF_8), LoginInformation.class);

                        if (loginInformation.isOk()) {
                            Log.i(MyLog.LOGIN_ACTIVITY, "Cookie : " + loginInformation.getCookie());

                            if (ManagerHelper.isInternet(LoginActivity.this)) {
                                Log.i(MyLog.LOGIN_ACTIVITY, "OFFLine");
                                ManagerHelper.noInternetAccess(LoginActivity.this);
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

        if (ManagerHelper.isInternet(LoginActivity.this)) {
            Log.i(MyLog.LOGIN_ACTIVITY, "OFFLine");
            ManagerHelper.noInternetAccess(LoginActivity.this);
        } else {
            Log.i(MyLog.LOGIN_ACTIVITY, "OnLine");
            sendParamsPost();
        }
    }

    @SuppressLint("LongLogTag")
    private void sendParamsPost() {
        if (loginInformation.getCookie() == null) {
            if (ManagerHelper.isInternet(LoginActivity.this)) {
                Log.i(MyLog.LOGIN_ACTIVITY, "OFFLine");
                ManagerHelper.noInternetAccess(LoginActivity.this);
            } else {
                Log.i(MyLog.LOGIN_ACTIVITY, "OnLine");
                getLoginInformation(true);
            }
        } else if (loginInformation.getCookie() != null) {

            user.setEnabled(false);
            password.setEnabled(false);
            enter.setVisibility(View.INVISIBLE);
            gifImageViewEnter.setVisibility(View.VISIBLE);
            gifImageViewEnter.setAnimation("loading_3.json");
            gifImageViewEnter.playAnimation();
            gifImageViewEnter.loop(true);

            Log.i(MyLog.LOGIN_ACTIVITY, "Run Function Send Params Post");

            HashMap<String, String> params = new HashMap<String, String>();
            params.put("cookie", loginInformation.getCookie());
            params.put("username", user.getText().toString().trim());
            params.put("password", password.getText().toString().trim());

            WebService webService = new WebService(LoginActivity.this);
            webService.requestPost(ApplicationAPI.SEND_INFORMATION, Request.Method.POST, params, new MyHandler() {
                @SuppressLint("NewApi")
                @Override
                public void onResponse(boolean ok, Object obj) {
                    Log.i(MyLog.LOGIN_ACTIVITY, "get Login Info");
                    sendInformation = gson.fromJson(new String(obj.toString().getBytes(StandardCharsets.ISO_8859_1), StandardCharsets.UTF_8), SendInformation.class);
                    if (sendInformation.isOk()) {
                        Log.i(MyLog.LOGIN_ACTIVITY, "All Params True");
                        Intent intent = new Intent(LoginActivity.this, BottomBarActivity.class).putExtra("DialogFragmentInstantMessage", sendInformation.getResult());

                        Log.i(MyLog.LOGIN_ACTIVITY, "Username : " + user.getText().toString().trim());
                        Log.i(MyLog.LOGIN_ACTIVITY, "Password : " + password.getText().toString().trim());

                        preferencesUtils.setCookie(loginInformation.getCookie());
                        preferencesUtils.setFirstName(sendInformation.getResult().getUserInformation().getName());
                        preferencesUtils.setLastName(sendInformation.getResult().getUserInformation().getFamily());
                        preferencesUtils.setMajor(sendInformation.getResult().getUserInformation().getMajor());
                        preferencesUtils.setPassword(password.getText().toString().trim());
                        preferencesUtils.setUsername(user.getText().toString().trim());

                        getUserImage();
                        preferencesUtils.setStartKey(false);
                        startActivity(intent);

                        CustomToastSuccess.success(LoginActivity.this, " خوش آمدید " + sendInformation.getResult().getUserInformation().getName() + " " + sendInformation.getResult().getUserInformation().getFamily(), Toast.LENGTH_SHORT).show();

                        finish();

                    } else if (!sendInformation.isOk()) {
                        Log.i(MyLog.LOGIN_ACTIVITY, "Some Parameters False");

                        Log.i(MyLog.LOGIN_ACTIVITY, "Error Code : " + sendInformation.getDescription().getErrorCode());
                        Log.i(MyLog.LOGIN_ACTIVITY, "Error Text : " + sendInformation.getDescription().getErrorText());

                        ManagerHelper.unsuccessfulOperation(LoginActivity.this, sendInformation.getDescription().getErrorText());
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
        Glide
                .with(LoginActivity.this)
                .asBitmap()
                .load(sendInformation.getResult().getUserInformation().getImage() + "?cookie=" + loginInformation.getCookie())
                .into(new SimpleTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                        resource.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
                        preferencesUtils.setImage(Base64.encodeToString(byteArrayOutputStream.toByteArray(), Base64.DEFAULT));
                    }
                });
    }

    @SuppressLint("RestrictedApi")
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

    @SuppressLint("RestrictedApi")
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

        if (ManagerHelper.isInternet(LoginActivity.this)) {
            Log.i(MyLog.LOGIN_ACTIVITY, "OFFLine");
            ManagerHelper.noInternetAccess(LoginActivity.this);
        } else {
            Log.i(MyLog.LOGIN_ACTIVITY, "OnLine");
            getLoginInformation(false);
        }
    }
}
