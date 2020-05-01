package ir.ac.sku.www.sessapplication.activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;

import ir.ac.sku.www.sessapplication.API.MyLog;
import ir.ac.sku.www.sessapplication.API.PreferenceName;
import ir.ac.sku.www.sessapplication.R;
import ir.ac.sku.www.sessapplication.models.GetInfoForSend;
import ir.ac.sku.www.sessapplication.utils.CheckSignUpPreferenceManager;
import ir.ac.sku.www.sessapplication.utils.MyActivity;
import ir.ac.sku.www.sessapplication.utils.MyHandler;

public class AboutActivity extends MyActivity {

    private Button logout;
    private Button bugReport;
    private Button email;
    private ImageView profile;
    private TextView username;
    private TextView majorUser;

    private CheckSignUpPreferenceManager manager;
    private SharedPreferences preferencesUserInformation;

    @SuppressLint({"SetTextI18n"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        changeStatusBarColor();
        init();

        manager = new CheckSignUpPreferenceManager(AboutActivity.this);
        preferencesUserInformation = getSharedPreferences(PreferenceName.PREFERENCE_USER_INFORMATION, MODE_PRIVATE);

        String userImage = preferencesUserInformation.getString(PreferenceName.PREFERENCE_IMAGE, null);
        String fristName = preferencesUserInformation.getString(PreferenceName.PREFERENCE_FIRST_NAME, "");
        String lastName = preferencesUserInformation.getString(PreferenceName.PREFERENCE_LAST_NAME, "");
        String major = preferencesUserInformation.getString(PreferenceName.PREFERENCE_MAJOR, "");

        if (fristName.equals("") && lastName.equals("")) {
            username.setText("مهمان");
        } else {
            username.setText(fristName + " " + lastName);
        }

        majorUser.setText(major);

        if (userImage != null) {
            byte[] b = Base64.decode(userImage, Base64.DEFAULT);
            Bitmap bitmap = BitmapFactory.decodeByteArray(b, 0, b.length);
            profile.setImageBitmap(bitmap);
        }

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                manager.setStartSignUpPreference(true);
                preferencesUserInformation.edit().clear().apply();
                startActivity(new Intent(AboutActivity.this, SplashScreenActivity.class));
                finish();
            }
        });

        email.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent emailIntent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts("mailto", "app@sku.ac.ir", null));
                emailIntent.putExtra(Intent.EXTRA_SUBJECT, "گزارش مشکلات اندروید");
                startActivity(Intent.createChooser(emailIntent, "ارسال ایمیل به پشتیبانی"));
            }
        });

        bugReport.setEnabled(true);
        bugReport.setClickable(true);

        bugReport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                infoForSend();
                bugReport.setEnabled(false);
                bugReport.setClickable(false);

            }
        });

    }

    private void init() {
        logout = findViewById(R.id.aboutActivity_Logout);
        bugReport = findViewById(R.id.aboutActivity_BugReport);
        profile = findViewById(R.id.aboutActivity_ImageProfile);
        username = findViewById(R.id.aboutActivity_Username);
        majorUser = findViewById(R.id.aboutActivity_Major);
        email = findViewById(R.id.aboutActivity_Email);
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

    private void infoForSend() {
        HashMap<String, String> params = new HashMap<>();
        params.put("id", "tapp");
        params.put("stNumber", "");

        GetInfoForSend.fetchFromWeb(AboutActivity.this, params, new MyHandler() {
            @Override
            public void onResponse(boolean ok, Object obj) {
                if (ok) {
                    Intent intent = new Intent(AboutActivity.this, SendMessageActivity.class);
                    intent.putExtra("GetInfoForSend", (Parcelable) (GetInfoForSend) obj);
                    intent.putExtra("id", "tapp");
                    intent.putExtra("studentNumber", "");
                    startActivity(intent);

                    bugReport.setEnabled(true);
                    bugReport.setClickable(true);
                }
            }
        });
    }
}
