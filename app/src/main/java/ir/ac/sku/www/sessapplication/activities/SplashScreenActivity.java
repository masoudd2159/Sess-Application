package ir.ac.sku.www.sessapplication.activities;

import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
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
import com.google.gson.GsonBuilder;

import ir.ac.sku.www.sessapplication.API.Config;
import ir.ac.sku.www.sessapplication.R;
import ir.ac.sku.www.sessapplication.models.LoginInformation;
import ir.ac.sku.www.sessapplication.parser.LoginInformationJSONParser;
import ir.ac.sku.www.sessapplication.utils.HttpManager;
import ir.ac.sku.www.sessapplication.utils.MyActivity;

public class SplashScreenActivity extends MyActivity {

    private static final int SPLASH_TIME = 1500;  //millisecond

    RequestQueue queue;

    Button tryAgain;
    Gson gson;
    LoginInformation loginInformation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        loginInformation = new LoginInformation();
        gson = new Gson();
        tryAgain = findViewById(R.id.buttonTryAgain_SplashScreen);
        changeStatusBarColor();
        requestProcess();
        new BackgroundTask().execute();
    }

    private void changeStatusBarColor() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.TRANSPARENT);
        }
    }

    private void requestProcess() {
        queue = Volley.newRequestQueue(SplashScreenActivity.this);
        if (HttpManager.isOnline(this)) {
            HttpManager.noInternetAccess(this);
            tryAgain.setVisibility(View.VISIBLE);
            tryAgain.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (HttpManager.isOnline(SplashScreenActivity.this)) {
                        HttpManager.noInternetAccess(SplashScreenActivity.this);
                    } else {
                        requestItems();
                        tryAgain.setVisibility(View.GONE);
                    }
                }
            });
        } else {
            requestItems();
        }
    }

    private void requestItems() {
        StringRequest request = new StringRequest(Config.LOGIN_INFORMATION,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        loginInformation = gson.fromJson(response, LoginInformation.class);
                        // loginInformation = new LoginInformationJSONParser().parserJSON(response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(SplashScreenActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
        queue.add(request);
    }

    private class BackgroundTask extends AsyncTask {

        Intent intent;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            intent = new Intent(SplashScreenActivity.this, LoginActivity.class);
        }

        @Override
        protected Object doInBackground(Object[] objects) {
            try {
                Thread.sleep(SPLASH_TIME);
                intent.putExtra("ok", loginInformation.isOk());
                intent.putExtra("cookie", loginInformation.getCookie());
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Object o) {
            super.onPostExecute(o);
            if (loginInformation.isOk()) {
                startActivity(intent);
                finish();
            }
        }
    }

}
