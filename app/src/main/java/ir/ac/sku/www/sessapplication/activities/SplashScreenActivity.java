package ir.ac.sku.www.sessapplication.activities;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import ir.ac.sku.www.sessapplication.API.MyLog;
import ir.ac.sku.www.sessapplication.API.PreferenceName;
import ir.ac.sku.www.sessapplication.R;
import ir.ac.sku.www.sessapplication.models.AppInfo;
import ir.ac.sku.www.sessapplication.models.JournalModel;
import ir.ac.sku.www.sessapplication.utils.CheckSignUpPreferenceManager;
import ir.ac.sku.www.sessapplication.utils.Handler;
import ir.ac.sku.www.sessapplication.utils.HttpManager;
import ir.ac.sku.www.sessapplication.utils.MyActivity;

public class SplashScreenActivity extends MyActivity {

    //static method
    private static final int SPLASH_TIME = 700;                                                    //millisecond

    //Splash Screen Views
    private Button tryAgain;
    private TextView fullName;
    private SharedPreferences preferencesName;
    private Dialog dialog;
    private Button update;
    private TextView showMessage;
    private ProgressDialog mProgressDialog;
    private AppInfo appInfo;


    //Utils
    private CheckSignUpPreferenceManager checkSignUpPreferenceManager;

    //onCreate
    @SuppressLint({"LongLogTag", "SetTextI18n"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        //Log Splash Screen
        Log.i(MyLog.SPLASH_SCREEN_ACTIVITY, "___Splash Screen___");

        preferencesName = getSharedPreferences(PreferenceName.NAME_PREFERENCE_NAME, MODE_PRIVATE);

        //find View
        tryAgain = findViewById(R.id.buttonTryAgain_SplashScreen);
        fullName = findViewById(R.id.splashScreen_FullName);

        fullName.setText(preferencesName.getString(PreferenceName.NAME_PREFERENCE_FIRST_NAME, "") + " " + preferencesName.getString(PreferenceName.NAME_PREFERENCE_LAST_NAME, ""));

        //Allocate MyUtils
        checkSignUpPreferenceManager = new CheckSignUpPreferenceManager(SplashScreenActivity.this);

        //my Functions
        changeStatusBarColor();
        appInfo = new AppInfo();
        getDataFromServer();
    }

    @SuppressLint("LongLogTag")
    private void changeStatusBarColor() {
        Log.i(MyLog.SPLASH_SCREEN_ACTIVITY, "Change Status Bar");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.TRANSPARENT);
        }
    }

    @SuppressLint("LongLogTag")
    private void getDataFromServer() {
        Log.i(MyLog.SPLASH_SCREEN_ACTIVITY, "Check Online");
        if (HttpManager.isNOTOnline(this)) {
            Log.i(MyLog.SPLASH_SCREEN_ACTIVITY, "OFFLine");
            HttpManager.noInternetAccess(this);
            tryAgain.setVisibility(View.VISIBLE);
            tryAgain.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.i(MyLog.SPLASH_SCREEN_ACTIVITY, "Clicked on Try Again");
                    if (HttpManager.isNOTOnline(SplashScreenActivity.this)) {
                        Log.i(MyLog.SPLASH_SCREEN_ACTIVITY, "OFFLine");
                        HttpManager.noInternetAccess(SplashScreenActivity.this);
                    } else {
                        Log.i(MyLog.SPLASH_SCREEN_ACTIVITY, "OnLine");
                        tryAgain.setVisibility(View.INVISIBLE);
                        getDataFromServer();
                    }
                }
            });
        } else {
            AppInfo.fetchFromWeb(SplashScreenActivity.this, null, new Handler() {
                @Override
                public void onResponse(boolean ok, Object obj) {
                    if (ok) {
                        appInfo = (AppInfo) obj;
                        checkOnLine();
                    }
                }
            });
        }
    }

    @SuppressLint("LongLogTag")
    private void checkOnLine() {
        Log.i(MyLog.SPLASH_SCREEN_ACTIVITY, "Check Online");
        if (HttpManager.isNOTOnline(this)) {
            Log.i(MyLog.SPLASH_SCREEN_ACTIVITY, "OFFLine");
            HttpManager.noInternetAccess(this);
            tryAgain.setVisibility(View.VISIBLE);
            tryAgain.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.i(MyLog.SPLASH_SCREEN_ACTIVITY, "Clicked on Try Again");
                    if (HttpManager.isNOTOnline(SplashScreenActivity.this)) {
                        Log.i(MyLog.SPLASH_SCREEN_ACTIVITY, "OFFLine");
                        HttpManager.noInternetAccess(SplashScreenActivity.this);
                    } else {
                        Log.i(MyLog.SPLASH_SCREEN_ACTIVITY, "OnLine");
                        tryAgain.setVisibility(View.INVISIBLE);
                        checkOnLine();
                    }
                }
            });
        } else {
            Log.i(MyLog.SPLASH_SCREEN_ACTIVITY, "OnLine");
            try {
                PackageInfo pInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
                int version = pInfo.versionCode;
                if (version < appInfo.getResult().getAndroidLatestVersion()) {
                    checkVersion();
                } else {
                    new BackgroundTask().execute();
                }
            } catch (PackageManager.NameNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

    private void checkVersion() {
        dialog = new Dialog(SplashScreenActivity.this);
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        dialog.setContentView(R.layout.custom_update);
        update = dialog.findViewById(R.id.customUpdate_UpdateButton);
        showMessage = dialog.findViewById(R.id.customUpdate_ShowMessage);

        try {
            PackageInfo pInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
            int version = pInfo.versionCode;
            if (version < appInfo.getResult().getAndroidMinimumVersion()) {
                dialog.setCancelable(false);
                showMessage.setText(appInfo.getResult().getForceUpdateMessage());
            } else {
                dialog.setCancelable(true);
                showMessage.setText(appInfo.getResult().getUpdateMessage());
            }
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        update.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("LongLogTag")
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(appInfo.getResult().getDownloadAndroidUrl()));
                startActivity(intent);
            }
        });

        dialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                new BackgroundTask().execute();
            }
        });

        dialog.show();
    }

    private class BackgroundTask extends AsyncTask {

        Intent intentLoginActivity;
        Intent intentBottomBarActivity;

        @SuppressLint("LongLogTag")
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Log.i(MyLog.SPLASH_SCREEN_ACTIVITY, "Create new Intent");
            intentLoginActivity = new Intent(SplashScreenActivity.this, LoginActivity.class);
            intentBottomBarActivity = new Intent(SplashScreenActivity.this, BottomBarActivity.class);
        }

        @SuppressLint("LongLogTag")
        @Override
        protected Object doInBackground(Object[] objects) {
            Log.i(MyLog.SPLASH_SCREEN_ACTIVITY, "do In Background");
            try {
                Thread.sleep(SPLASH_TIME);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return null;
        }

        @SuppressLint("LongLogTag")
        @Override
        protected void onPostExecute(Object o) {
            super.onPostExecute(o);
            Log.i(MyLog.SPLASH_SCREEN_ACTIVITY, "on Post Execute");
            if (checkSignUpPreferenceManager.startSignUpPreference()) {
                Log.i(MyLog.SPLASH_SCREEN_ACTIVITY, "Check SignUp Preference Manager : " + checkSignUpPreferenceManager.startSignUpPreference());
                Log.i(MyLog.SPLASH_SCREEN_ACTIVITY, "Start Activity : Login Activity");
                startActivity(intentLoginActivity);
            } else if (!checkSignUpPreferenceManager.startSignUpPreference()) {
                Log.i(MyLog.SPLASH_SCREEN_ACTIVITY, "Check SignUp Preference Manager : " + checkSignUpPreferenceManager.startSignUpPreference());
                Log.i(MyLog.SPLASH_SCREEN_ACTIVITY, "Start Activity : Bottom Bar Activity");
                startActivity(intentBottomBarActivity);
            }
            finish();
        }
    }
}
