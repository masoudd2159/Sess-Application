package ir.ac.sku.www.sessapplication.activities;

import android.animation.ArgbEvaluator;
import android.animation.ValueAnimator;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.os.Bundle;
import android.support.v7.widget.CardView;
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
import ir.ac.sku.www.sessapplication.R;
import ir.ac.sku.www.sessapplication.models.LoginInformation;
import ir.ac.sku.www.sessapplication.models.SendInformation;
import ir.ac.sku.www.sessapplication.utils.CustomToast;
import ir.ac.sku.www.sessapplication.utils.MyActivity;
import pl.droidsonroids.gif.GifImageView;

public class LoginActivity extends MyActivity {

    private static final String PREFERENCE_NAME = "LoginInformation";

    private EditText user;
    private EditText password;
    private ImageView captcha;
    private GifImageView gifImageView;
    private EditText securityTag;
    private Button enter;

    private RequestQueue queue;
    private Gson gson;

    private LoginInformation loginInformation;
    private SendInformation sendInformation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        loginInformation = new LoginInformation();
        sendInformation = new SendInformation();

        queue = Volley.newRequestQueue(LoginActivity.this);
        gson = new Gson();

        changeStatusBarColor();
        getLoginInformation();
        init();
        getCaptcha();

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
    }

    private void changeStatusBarColor() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.TRANSPARENT);
        }
    }

    private void getLoginInformation() {
        Bundle extras = getIntent().getExtras();

        if (extras != null) {
            if (extras.containsKey("ok")) {
                loginInformation.setOk(extras.getBoolean("ok"));
            }
            if (extras.containsKey("cookie")) {
                loginInformation.setCookie(extras.getString("cookie"));
            }
        }
    }

    private void init() {
        user = findViewById(R.id.userLogin);
        password = findViewById(R.id.passwordLogin);
        captcha = findViewById(R.id.imageViewCaptcha_LoginActivity);
        gifImageView = findViewById(R.id.loadingGif);
        securityTag = findViewById(R.id.securityTagLogin);
        enter = findViewById(R.id.enter_LoginActivity);
    }

    private void getCaptcha() {
        String captchaCookieURL = MyConfig.CAPTCHA_PICTURE + loginInformation.getCookie();

        ImageRequest imageRequest = new ImageRequest(captchaCookieURL,
                new Response.Listener<Bitmap>() {
                    @Override
                    public void onResponse(final Bitmap response) {
                        gifImageView.postDelayed(new Runnable() {
                            @Override
                            public void run() {
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
                        Toast.makeText(LoginActivity.this, "ERROR : " + error, Toast.LENGTH_SHORT).show();
                    }
                });
        queue.add(imageRequest);
    }

    public void onEnterClickListener(View view) {
        sendParamsPost();
    }

    private void sendParamsPost() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST,
                MyConfig.SEND_INFORMATION,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            sendInformation = gson.fromJson(new String(response.getBytes("ISO-8859-1"), "UTF-8"),
                                    SendInformation.class);
                            if (sendInformation.isOk()) {
                                Intent intent = new Intent(LoginActivity.this, BottomBarActivity.class);
                                intent.putExtra("cookie", loginInformation.getCookie());
                                startActivity(intent);
                                CustomToast.success(LoginActivity.this, "خوش آمدید " + sendInformation.getResult().getUserInformation().getName(),
                                        Toast.LENGTH_LONG).show();
                                finish();
                            } else if (!sendInformation.isOk()) {
                                Toast.makeText(LoginActivity.this, sendInformation.getDescription().getErrorText(), Toast.LENGTH_SHORT).show();
                            }
                        } catch (UnsupportedEncodingException e) {
                            e.printStackTrace();
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
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
                Map<String, String> params = new HashMap<String, String>();
                params.put("cookie", loginInformation.getCookie());
                params.put("captcha", securityTag.getText().toString().trim());
                params.put("username", "S" + user.getText().toString().trim());
                params.put("password", password.getText().toString().trim());
                return params;
            }
        };
        queue.add(stringRequest);
    }

    private boolean isValidChecked(String str_user, String str_password, String str_securityTag) {
        if (str_user == null)
            str_user = user.getText().toString().trim();
        if (str_password == null)
            str_password = password.getText().toString().trim();
        if (str_securityTag == null)
            str_securityTag = securityTag.getText().toString().trim();

        if (!str_user.isEmpty() && str_user.length() < 9) {
            Toast.makeText(this, "نام کاربری را صحیح وارد کنید.", Toast.LENGTH_SHORT).show();
            user.requestFocus();
            return false;
        }
        if (!str_password.isEmpty()) {
            Toast.makeText(this, "رمز عبور خود را وارد کنید.", Toast.LENGTH_SHORT).show();
            password.requestFocus();
            return false;
        }
        if (!str_securityTag.isEmpty() && str_securityTag.length() < 5) {
            Toast.makeText(this, "عبارت امنیتی را تکمیل کنید.", Toast.LENGTH_SHORT).show();
            securityTag.requestFocus();
            return false;
        }
        return true;
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
