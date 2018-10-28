package ir.ac.sku.www.sessapplication.activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;

import ir.ac.sku.www.sessapplication.API.MyConfig;
import ir.ac.sku.www.sessapplication.API.MyLog;
import ir.ac.sku.www.sessapplication.R;
import ir.ac.sku.www.sessapplication.models.LoginInformation;
import ir.ac.sku.www.sessapplication.utils.HttpManager;
import ir.ac.sku.www.sessapplication.utils.MyActivity;

public class SplashScreenActivity extends MyActivity {

    //static method
    private static final int SPLASH_TIME = 1500;                                                    //millisecond

    //Splash Screen Views
    private Button tryAgain;

    //onCreate
    @SuppressLint("LongLogTag")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        //Log Splash Screen
        Log.i(MyLog.SPLASH_SCREEN, "Create Splash Screen");

        //find View
        tryAgain = findViewById(R.id.buttonTryAgain_SplashScreen);

        //my Functions
        changeStatusBarColor();
        checkOnLine();
    }

    @SuppressLint("LongLogTag")
    private void changeStatusBarColor() {
        Log.i(MyLog.SPLASH_SCREEN, "Change Status Bar");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.TRANSPARENT);
        }
    }

    @SuppressLint("LongLogTag")
    private void checkOnLine() {
        Log.i(MyLog.SPLASH_SCREEN, "Check Online");
        if (HttpManager.isNOTOnline(this)) {
            Log.i(MyLog.SPLASH_SCREEN, "OFFLine");
            HttpManager.noInternetAccess(this);
            tryAgain.setVisibility(View.VISIBLE);
            tryAgain.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.i(MyLog.SPLASH_SCREEN, "Clicked on Try Again");
                    if (HttpManager.isNOTOnline(SplashScreenActivity.this)) {
                        Log.i(MyLog.SPLASH_SCREEN, "OFFLine");
                        HttpManager.noInternetAccess(SplashScreenActivity.this);
                    } else {
                        Log.i(MyLog.SPLASH_SCREEN, "OnLine");
                        tryAgain.setVisibility(View.INVISIBLE);
                        checkOnLine();
                    }
                }
            });
        } else {
            Log.i(MyLog.SPLASH_SCREEN, "OnLine");
            new BackgroundTask().execute();
        }
    }

    private class BackgroundTask extends AsyncTask {

        Intent intent;

        @SuppressLint("LongLogTag")
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Log.i(MyLog.SPLASH_SCREEN, "Create new Intent");
            intent = new Intent(SplashScreenActivity.this, LoginActivity.class);
        }

        @SuppressLint("LongLogTag")
        @Override
        protected Object doInBackground(Object[] objects) {
            Log.i(MyLog.SPLASH_SCREEN, "do In Background");
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
            Log.i(MyLog.SPLASH_SCREEN, "on Post Execute");
            startActivity(intent);
            finish();
        }
    }
}
