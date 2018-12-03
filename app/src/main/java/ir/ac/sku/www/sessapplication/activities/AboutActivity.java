package ir.ac.sku.www.sessapplication.activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;
import ir.ac.sku.www.sessapplication.API.MyConfig;
import ir.ac.sku.www.sessapplication.API.MyLog;
import ir.ac.sku.www.sessapplication.API.PreferenceName;
import ir.ac.sku.www.sessapplication.R;
import ir.ac.sku.www.sessapplication.utils.CheckSignUpPreferenceManager;
import ir.ac.sku.www.sessapplication.utils.HttpManager;
import ir.ac.sku.www.sessapplication.utils.MyActivity;

public class AboutActivity extends MyActivity {

    private Button logout;
    private CircleImageView profile;
    private TextView username;

    private CheckSignUpPreferenceManager manager;
    private SharedPreferences preferencesUsernameAndPassword;
    private SharedPreferences preferencesCookie;
    private SharedPreferences preferencesName;
    private SharedPreferences preferencesUserImage;

    private RequestQueue queue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        changeStatusBarColor();

        logout = findViewById(R.id.aboutActivity_Logout);
        profile = findViewById(R.id.aboutActivity_ImageProfile);
        username = findViewById(R.id.aboutActivity_Username);

        queue = Volley.newRequestQueue(AboutActivity.this);

        manager = new CheckSignUpPreferenceManager(AboutActivity.this);
        preferencesUsernameAndPassword = getSharedPreferences(PreferenceName.USERNAME_AND_PASSWORD_PREFERENCE_NAME, MODE_PRIVATE);
        preferencesCookie = getSharedPreferences(PreferenceName.COOKIE_PREFERENCE_NAME, MODE_PRIVATE);
        preferencesName = getSharedPreferences(PreferenceName.NAME_PREFERENCE_NAME, MODE_PRIVATE);
        preferencesUserImage = getSharedPreferences(PreferenceName.USER_IMAGE_PREFERENCE_NAME, MODE_PRIVATE);

        String userImage = preferencesUserImage.getString(PreferenceName.USER_IMAGE_PREFERENCE_IMAGE, null);

        if (userImage != null) {
            byte[] b = Base64.decode(userImage, Base64.DEFAULT);
            Bitmap bitmap = BitmapFactory.decodeByteArray(b, 0, b.length);
            profile.setImageBitmap(bitmap);
        }

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                manager.setStartSignUpPreference(true);

                SharedPreferences.Editor editorCookie = preferencesCookie.edit();
                editorCookie.putString(PreferenceName.COOKIE_PREFERENCE_COOKIE, null);
                editorCookie.apply();

                startActivity(new Intent(AboutActivity.this, SplashScreenActivity.class));
                finish();
            }
        });

        username.setText(preferencesName.getString(PreferenceName.NAME_PREFERENCE_USERNAME, null));

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
}
