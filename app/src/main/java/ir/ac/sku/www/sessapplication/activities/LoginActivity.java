package ir.ac.sku.www.sessapplication.activities;

import android.animation.ArgbEvaluator;
import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.Transformation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
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
import ir.ac.sku.www.sessapplication.R;
import ir.ac.sku.www.sessapplication.models.LoginInformation;
import ir.ac.sku.www.sessapplication.models.SendInformation;
import ir.ac.sku.www.sessapplication.models.UsernamePassword;
import ir.ac.sku.www.sessapplication.utils.CustomToastSuccess;
import ir.ac.sku.www.sessapplication.utils.HttpManager;
import ir.ac.sku.www.sessapplication.utils.MyActivity;
import pl.droidsonroids.gif.GifImageView;

public class LoginActivity extends MyActivity {

    //static Variable
    private static final String PREFERENCE_NAME = "LoginInformation";

    //Login Activity Views
    private EditText user;
    private EditText password;
    private ImageView captcha;
    private GifImageView gifImageView;
    private EditText securityTag;
    private Button enter;

    //Required libraries
    private RequestQueue queue;
    private Gson gson;

    //my Class Model
    private LoginInformation loginInformation;
    private SendInformation sendInformation;
    private UsernamePassword usernamePassword;

    @SuppressLint("LongLogTag")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Log.i(MyLog.LOGIN_ACTIVITY, "Create Login Activity");

        //allocate classes
        loginInformation = new LoginInformation();
        sendInformation = new SendInformation();
        usernamePassword = new UsernamePassword();

        //create queue for API Request
        queue = Volley.newRequestQueue(LoginActivity.this);

        //use Gson Lib
        gson = new Gson();

        //my Functions
        changeStatusBarColor();
        init();
        getLoginInformation();

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
        securityTag.setOnFocusChangeListener(focusChangeListener);

        //get ReCaptcha
        captcha.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i(MyLog.LOGIN_ACTIVITY, "Get Another Captcha");
                captcha.setImageBitmap(null);
                gifImageView.setVisibility(View.VISIBLE);
                getLoginInformation();
            }
        });

     /*   user.setText("951406115");
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
        Log.i(MyLog.LOGIN_ACTIVITY, "Allocate Views");
        user = findViewById(R.id.userLogin);
        password = findViewById(R.id.passwordLogin);
        captcha = findViewById(R.id.imageViewCaptcha_LoginActivity);
        gifImageView = findViewById(R.id.loadingGif);
        securityTag = findViewById(R.id.securityTagLogin);
        enter = findViewById(R.id.enter_LoginActivity);
    }

    @SuppressLint("LongLogTag")
    private void getLoginInformation() {
        Log.i(MyLog.LOGIN_ACTIVITY, "Run Request Cookie Function");
        StringRequest request = new StringRequest(MyConfig.LOGIN_INFORMATION,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.i(MyLog.LOGIN_ACTIVITY, "get JSON Cookie From Server");
                        loginInformation = gson.fromJson(response, LoginInformation.class);
                        if (loginInformation.isOk()) {
                            Log.i(MyLog.LOGIN_ACTIVITY, "Cookie : " + loginInformation.getCookie());
                            getCaptcha();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.i(MyLog.LOGIN_ACTIVITY, "ERROR" + error.getMessage());
                        Toast.makeText(LoginActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
        queue.add(request);
        Log.i(MyLog.LOGIN_ACTIVITY, "Request Cookie Possess Added To queue");
    }

    @SuppressLint("LongLogTag")
    private void getCaptcha() {
        Log.i(MyLog.LOGIN_ACTIVITY, "Run Captcha Function");
        String captchaCookieURL = MyConfig.CAPTCHA_PICTURE + loginInformation.getCookie();

        ImageRequest imageRequest = new ImageRequest(captchaCookieURL,
                new Response.Listener<Bitmap>() {
                    @Override
                    public void onResponse(final Bitmap response) {
                        Log.i(MyLog.LOGIN_ACTIVITY, "get Captcha From Server");
                        gifImageView.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                Log.i(MyLog.LOGIN_ACTIVITY, "InVisible Gif Image View And Get Captcha");
                                gifImageView.setVisibility(View.INVISIBLE);
                                captcha.setImageBitmap(response);
                            }
                        }, 300);
                    }
                },
                (int) (getResources().getDimension(R.dimen.edit_text_width)),
                (int) (getResources().getDimension(R.dimen.second_card_height)),
                ImageView.ScaleType.FIT_CENTER,
                Bitmap.Config.ARGB_8888,
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.i(MyLog.LOGIN_ACTIVITY, "ERROR : " + error.getMessage());
                        Toast.makeText(LoginActivity.this, "ERROR : " + error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
        queue.add(imageRequest);
        Log.i(MyLog.LOGIN_ACTIVITY, "Request Possess Added To queue");
    }

    @SuppressLint("LongLogTag")
    public void onEnterClickListener(View view) {
        Log.i(MyLog.LOGIN_ACTIVITY, "Click on Enter Button");
        if (HttpManager.isNOTOnline(this)) {
            Log.i(MyLog.LOGIN_ACTIVITY, "OFFLine");
            HttpManager.noInternetAccess(this);
        } else {
            Log.i(MyLog.LOGIN_ACTIVITY, "OnLine");
            sendParamsPost();
            UsernamePassword.setUsername(user.getText().toString().trim());
            Log.i(MyLog.LOGIN_ACTIVITY, "Username : " + UsernamePassword.getUsername());
            UsernamePassword.setPassword(password.getText().toString().trim());
            Log.i(MyLog.LOGIN_ACTIVITY, "Password : " + UsernamePassword.getPassword());
        }
    }

    @SuppressLint("LongLogTag")
    private void sendParamsPost() {
        Log.i(MyLog.LOGIN_ACTIVITY, "Run Function send Params Post");
        StringRequest stringRequest = new StringRequest(Request.Method.POST,
                MyConfig.SEND_INFORMATION,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.i(MyLog.LOGIN_ACTIVITY, "get Login Info");
                        try {
                            sendInformation = gson.fromJson(new String(response.getBytes("ISO-8859-1"), "UTF-8"),
                                    SendInformation.class);
                            if (sendInformation.isOk()) {
                                Log.i(MyLog.LOGIN_ACTIVITY, "All Params True");
                                Intent intent = new Intent(LoginActivity.this, BottomBarActivity.class);
                                intent.putExtra("cookie", loginInformation.getCookie());
                                startActivity(intent);
                                CustomToastSuccess.success(LoginActivity.this, "خوش آمدید " + sendInformation.getResult().getUserInformation().getName(),
                                        Toast.LENGTH_SHORT).show();
                                finish();
                            } else if (!sendInformation.isOk()) {
                                Log.i(MyLog.LOGIN_ACTIVITY, "Some Parameter is False");
                                Toast.makeText(LoginActivity.this, sendInformation.getDescription().getErrorText(), Toast.LENGTH_SHORT).show();
                                if (sendInformation.getDescription().getErrorCode().equals("1")) {
                                    Log.i(MyLog.LOGIN_ACTIVITY, "Username or password is incorrect");
                                    user.setText("");
                                    password.setText("");
                                } else if (sendInformation.getDescription().getErrorCode().equals("2")) {
                                    Log.i(MyLog.LOGIN_ACTIVITY, "Captcha is Wrong");
                                    securityTag.setText("");
                                } else if (Integer.parseInt(sendInformation.getDescription().getErrorCode()) < 0) {
                                    Log.i(MyLog.LOGIN_ACTIVITY, "Lost Cookie");
                                    finish();
                                    System.exit(0);
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
                params.put("captcha", securityTag.getText().toString().trim());
                params.put("username", "S" + user.getText().toString().trim());
                params.put("password", password.getText().toString().trim());
                return params;
            }
        };
        queue.add(stringRequest);
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
}
