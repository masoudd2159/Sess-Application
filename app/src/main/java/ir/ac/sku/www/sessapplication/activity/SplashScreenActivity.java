package ir.ac.sku.www.sessapplication.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.airbnb.lottie.LottieAnimationView;

import butterknife.BindView;
import ir.ac.sku.www.sessapplication.R;
import ir.ac.sku.www.sessapplication.api.MyLog;
import ir.ac.sku.www.sessapplication.base.BaseActivity;
import ir.ac.sku.www.sessapplication.fragment.dialogfragment.DialogFragmentUpdate;
import ir.ac.sku.www.sessapplication.model.appInfo.AppInfo;
import ir.ac.sku.www.sessapplication.utils.helper.ManagerHelper;

public class SplashScreenActivity extends BaseActivity implements DialogFragmentUpdate.OnCancelListener {

    //static method
    private static final int SPLASH_TIME = 1700;                                                     //millisecond

    //Splash Screen Views
    @BindView(R.id.buttonTryAgain_SplashScreen) Button tryAgain;
    @BindView(R.id.splashScreen_FullName) TextView fullName;
    @BindView(R.id.splashScreen_AnimationView) LottieAnimationView animationView;

    @Override protected int getLayoutResource() {
        return R.layout.activity_splash_screen;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        changeStatusBarColor();
        SetUpView();
        getDataFromServer();
    }

    @SuppressLint("SetTextI18n")
    private void SetUpView() {
        animationView.setAnimation("loading_6.json");
        animationView.playAnimation();
        animationView.loop(true);
        if (preferencesUtils.getFirstName() != null && preferencesUtils.getLastName() != null)
            fullName.setText(preferencesUtils.getFirstName() + " " + preferencesUtils.getLastName());
        else
            fullName.setText("");
    }

    private void getDataFromServer() {
        if (ManagerHelper.isInternetAvailable(this)) {
            AppInfo.fetchFromWeb(this, null, (ok, obj) -> {
                if (ok) {
                    checkOnLine((AppInfo) obj);
                }
            });
        } else {
            Log.i(MyLog.SESS + TAG, "Device Offline");
            ManagerHelper.noInternetAccess(this);
            tryAgain.setVisibility(View.VISIBLE);
            tryAgain.setOnClickListener(v -> {
                if (ManagerHelper.checkInternetServices(this)) {
                    Log.i(MyLog.SESS + TAG, "Device Online");
                    tryAgain.setVisibility(View.INVISIBLE);
                    getDataFromServer();
                }
            });
        }
    }

    private void checkOnLine(AppInfo appInfo) {
        try {
            PackageInfo pInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
            if (pInfo.versionCode < appInfo.getResult().getAndroidLatestVersion()) {
                new DialogFragmentUpdate(appInfo).show(getSupportFragmentManager(), "DialogFragmentNoInternetAccess");
            } else {
                new SplashScreen().execute();
            }
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onCancelListener() {
        Log.i(MyLog.SESS + TAG, "on Cancel Listener");
        new SplashScreen().execute();
    }

    private class SplashScreen extends AsyncTask<Void, Void, Void> {
        private Intent intentBottomBarActivity;

        @Override
        protected Void doInBackground(Void... voids) {
            Log.i(MyLog.SESS + TAG, "do In Background");
            try {
                Thread.sleep(SPLASH_TIME);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Log.i(MyLog.SESS + TAG, "on Pre Execute");
            intentBottomBarActivity = new Intent(SplashScreenActivity.this, BottomBarActivity.class);
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            Log.i(MyLog.SESS + TAG, "on Post Execute");
            startActivity(intentBottomBarActivity);
            finish();
        }
    }
}