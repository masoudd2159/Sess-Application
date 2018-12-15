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
import android.support.v4.content.ContextCompat;
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
import com.google.gson.Gson;

import java.io.ByteArrayOutputStream;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

import ir.ac.sku.www.sessapplication.API.MyConfig;
import ir.ac.sku.www.sessapplication.API.MyLog;
import ir.ac.sku.www.sessapplication.API.PreferenceName;
import ir.ac.sku.www.sessapplication.R;
import ir.ac.sku.www.sessapplication.models.LoginInformation;
import ir.ac.sku.www.sessapplication.models.SendInformation;
import ir.ac.sku.www.sessapplication.utils.CheckSignUpPreferenceManager;
import ir.ac.sku.www.sessapplication.utils.CustomToastExit;
import ir.ac.sku.www.sessapplication.utils.CustomToastSuccess;
import ir.ac.sku.www.sessapplication.utils.HttpManager;
import ir.ac.sku.www.sessapplication.utils.MyActivity;
import pl.droidsonroids.gif.GifImageView;

public class LoginActivity extends MyActivity {

    private EditText user;
    private EditText password;
    private GifImageView gifImageViewEnter;
    private Button enter;
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

    //my Class Model
    private LoginInformation loginInformation;
    private SendInformation sendInformation;
    private boolean doubleBackToExitPressedOnce = false;

    @SuppressLint("LongLogTag")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Log.i(MyLog.LOGIN_ACTIVITY, "Create Login Activity");

        //allocate classes
        loginInformation = new LoginInformation();
        sendInformation = new SendInformation();

        //create queue for API Request
        queue = Volley.newRequestQueue(LoginActivity.this);

        //use Gson Lib
        gson = new Gson();
        manager = new CheckSignUpPreferenceManager(LoginActivity.this);
        preferencesUsernameAndPassword = getSharedPreferences(PreferenceName.USERNAME_AND_PASSWORD_PREFERENCE_NAME, MODE_PRIVATE);
        preferencesCookie = getSharedPreferences(PreferenceName.COOKIE_PREFERENCE_NAME, MODE_PRIVATE);
        preferencesName = getSharedPreferences(PreferenceName.NAME_PREFERENCE_NAME, MODE_PRIVATE);
        preferencesUserImage = getSharedPreferences(PreferenceName.USER_IMAGE_PREFERENCE_NAME, MODE_PRIVATE);

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
                    Log.i(MyLog.LOGIN_ACTIVITY, "Click on Enter Button");
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

        /*user.setText("951406115");
        password.setText("masoud76");*/
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
        user = findViewById(R.id.loginActivity_Username);
        password = findViewById(R.id.loginActivity_Password);
        gifImageViewEnter = findViewById(R.id.loginActivity_GifImageViewEnter);
        enter = findViewById(R.id.loginActivity_Enter);
        scrollView = findViewById(R.id.loginActivity_ScrollView);
        loginView = findViewById(R.id.loginActivity_View);
        //captcha = findViewById(R.id.loginActivity_ImageViewCaptcha);
        //gifImageViewCaptcha = findViewById(R.id.loginActivity_GifImageViewCaptcha);
        //securityTag = findViewById(R.id.loginActivity_EditTextCaptcha);
    }

    @SuppressLint("LongLogTag")
    private void getLoginInformation(final boolean where) {

        progressDialog.setMessage("لطفا منتظر بمانید!");
        progressDialog.setCancelable(false);
        progressDialog.show();

        Map<String, String> params = new HashMap<>();
        params.put("test", "test");

        LoginInformation.fetchFromWeb(LoginActivity.this, (HashMap<String, String>) params, new ir.ac.sku.www.sessapplication.utils.Handler() {
            @Override
            public void onResponse(boolean ok, Object obj) {
                progressDialog.dismiss();
                if (ok) {
                    loginInformation = (LoginInformation) obj;
                    if (loginInformation.isOk()) {
                        if (where) {
                            sendParamsPost();
                        }
                    }
                }
            }
        });
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
                HttpManager.noInternetAccess(LoginActivity.this);
            } else {
                getLoginInformation(true);
            }
        } else if (loginInformation.getCookie() != null) {

            user.setEnabled(false);
            password.setEnabled(false);
            enter.setVisibility(View.INVISIBLE);
            gifImageViewEnter.setVisibility(View.VISIBLE);

            Log.i(MyLog.LOGIN_ACTIVITY, "Run Function send Params Post");
            StringRequest stringRequest = new StringRequest(Request.Method.POST,
                    MyConfig.SEND_INFORMATION,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            Log.i(MyLog.LOGIN_ACTIVITY, "get Login Info");
                            try {
                                sendInformation = gson.fromJson(new String(response.getBytes("ISO-8859-1"), "UTF-8"), SendInformation.class);

                                if (sendInformation.isOk()) {
                                    Log.i(MyLog.LOGIN_ACTIVITY, "All Params True");

                                    Intent intent = new Intent(LoginActivity.this, BottomBarActivity.class).putExtra("InstantMessage", sendInformation.getResult());

                                    @SuppressLint("CommitPrefEdits") SharedPreferences.Editor editorUserPass = preferencesUsernameAndPassword.edit();
                                    editorUserPass.putString(PreferenceName.USERNAME_AND_PASSWORD_PREFERENCE_USERNAME, user.getText().toString().trim());
                                    editorUserPass.putString(PreferenceName.USERNAME_AND_PASSWORD_PREFERENCE_PASSWORD, password.getText().toString().trim());
                                    editorUserPass.apply();

                                    @SuppressLint("CommitPrefEdits") SharedPreferences.Editor editorCookie = preferencesCookie.edit();
                                    editorCookie.putString(PreferenceName.COOKIE_PREFERENCE_COOKIE, loginInformation.getCookie());
                                    editorCookie.apply();

                                    @SuppressLint("CommitPrefEdits") SharedPreferences.Editor editorName = preferencesName.edit();
                                    editorName.putString(PreferenceName.NAME_PREFERENCE_USERNAME, sendInformation.getResult().getUserInformation().getName());
                                    editorName.apply();

                                    getUserImage();

                                    manager.setStartSignUpPreference(false);

                                    startActivity(intent);

                                    CustomToastSuccess.success(LoginActivity.this, "خوش آمدید " + sendInformation.getResult().getUserInformation().getName(), Toast.LENGTH_SHORT).show();

                                    finish();

                                } else if (!sendInformation.isOk()) {
                                    getLoginInformation(false);
                                    Toast.makeText(LoginActivity.this, sendInformation.getDescription().getErrorText(), Toast.LENGTH_SHORT).show();

                                    user.setEnabled(true);
                                    password.setEnabled(true);

                                    user.setText("");
                                    password.setText("");

                                    user.requestFocus();

                                    enter.setVisibility(View.VISIBLE);
                                    gifImageViewEnter.setVisibility(View.INVISIBLE);
                                }
                            } catch (UnsupportedEncodingException e) {
                                e.printStackTrace();
                            }

                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Log.i(MyLog.LOGIN_ACTIVITY, error.getMessage());
                            new AlertDialog.Builder(LoginActivity.this)
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
                    Log.i(MyLog.LOGIN_ACTIVITY, "Send Login Info");
                    Map<String, String> params = new HashMap<String, String>();
                    params.put("cookie", loginInformation.getCookie());
                    params.put("username", "S" + user.getText().toString().trim());
                    params.put("password", password.getText().toString().trim());
                    //params.put("captcha", securityTag.getText().toString().trim());
                    return params;
                }
            };
            queue.add(stringRequest);
            Log.i(MyLog.LOGIN_ACTIVITY, "Request Possess Added To queue");
        }
    }

    @SuppressLint("LongLogTag")
    private void getUserImage() {
        Map<String, String> params = new HashMap<>();
        params.put("cookie", loginInformation.getCookie());
        String URI = MyConfig.USER_IMAGE + "?" + HttpManager.enCodeParameters(params);

        ImageRequest imageRequest = new ImageRequest(URI,
                new Response.Listener<Bitmap>() {
                    @Override
                    public void onResponse(Bitmap response) {
                        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                        response.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
                        byte[] b = byteArrayOutputStream.toByteArray();
                        String encodedImage = Base64.encodeToString(b, Base64.DEFAULT);
                        @SuppressLint("CommitPrefEdits") SharedPreferences.Editor editorUserImage = preferencesUserImage.edit();
                        editorUserImage.putString(PreferenceName.USER_IMAGE_PREFERENCE_IMAGE, encodedImage);
                        editorUserImage.apply();
                    }
                },
                125,
                125,
                ImageView.ScaleType.FIT_CENTER,
                Bitmap.Config.ARGB_8888,
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.i(MyLog.LOGIN_ACTIVITY, "ERROR : " + error.getMessage());
                        new AlertDialog.Builder(LoginActivity.this)
                                .setTitle("ERROR")
                                .setMessage(error.getMessage())
                                .setPositiveButton("Ok", null)
                                .setIcon(android.R.drawable.ic_dialog_alert)
                                .show();
                    }
                });
        queue.add(imageRequest);
        Log.i(MyLog.LOGIN_ACTIVITY, "Request Possess Added To queue");
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

    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
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
        Log.i(MyLog.LOGIN_ACTIVITY, "Click on Enter Button");
        if (HttpManager.isNOTOnline(LoginActivity.this)) {
            Log.i(MyLog.LOGIN_ACTIVITY, "OFFLine");
            HttpManager.noInternetAccess(LoginActivity.this);
        } else {
            Log.i(MyLog.LOGIN_ACTIVITY, "OnLine");
            getLoginInformation(false);
        }

    }
}
