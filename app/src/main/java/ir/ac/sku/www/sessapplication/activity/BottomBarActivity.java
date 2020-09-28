package ir.ac.sku.www.sessapplication.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.coordinatorlayout.widget.CoordinatorLayout;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.mikhaellopez.circularimageview.CircularImageView;

import butterknife.BindView;
import ir.ac.sku.www.sessapplication.R;
import ir.ac.sku.www.sessapplication.activity.about.AboutActivity;
import ir.ac.sku.www.sessapplication.api.MyConfig;
import ir.ac.sku.www.sessapplication.api.MyLog;
import ir.ac.sku.www.sessapplication.base.BaseActivity;
import ir.ac.sku.www.sessapplication.fragment.FoodReservationFragment;
import ir.ac.sku.www.sessapplication.fragment.HomeFragment;
import ir.ac.sku.www.sessapplication.fragment.MessagesFragment;
import ir.ac.sku.www.sessapplication.fragment.ProcessesFragment;
import ir.ac.sku.www.sessapplication.fragment.SignInDialogFragment;
import ir.ac.sku.www.sessapplication.fragment.dialogfragment.DialogFragmentInstantMessage;
import ir.ac.sku.www.sessapplication.model.SendInformation;
import ir.ac.sku.www.sessapplication.utils.CustomToastExit;
import ir.ac.sku.www.sessapplication.utils.HttpManager;
import ir.ac.sku.www.sessapplication.utils.SharedPreferencesUtils;
import ir.ac.sku.www.sessapplication.utils.Tools;

public class BottomBarActivity
        extends BaseActivity
        implements SignInDialogFragment.UserInterface, BottomNavigationView.OnNavigationItemSelectedListener {

    //* Content View
    @BindView(R.id.Bottom_navigation_view) BottomNavigationView bottomBar;
    @BindView(R.id.fragment_holder) CoordinatorLayout fragmentHolder;

    // ToolBar View
    @BindView(R.id.toolbar_image_profile) CircularImageView imageViewProfile;
    @BindView(R.id.toolbar_full_name) TextView fullName;
    @BindView(R.id.toolbar_dash) View dash;

    private SharedPreferencesUtils preferencesUtils;

    private boolean doubleBackToExitPressedOnce = false;

    @Override
    protected int getLayoutResource() {
        return R.layout.activity_bottom_bar;
    }

    @Override
    protected View getLayoutSnackBar() {
        return fragmentHolder;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        preferencesUtils = new SharedPreferencesUtils(this);

        updateUserInformation();
        setUpInstantMessage();

        bottomBar.setOnNavigationItemSelectedListener(this);

        if (savedInstanceState == null) bottomBar.setSelectedItemId(R.id.tab_Home);
    }

    private void setUpInstantMessage() {
        SendInformation.Result instantMessage = (SendInformation.Result) getIntent().getParcelableExtra("DialogFragmentInstantMessage");
        if (instantMessage != null && instantMessage.getInstantMessage().size() > 0) {
            Log.i(MyLog.SESS + TAG, instantMessage.getUserInformation().getName() + " " + instantMessage.getUserInformation().getFamily());
            Log.i(MyLog.SESS + TAG, "Instant Message : " + instantMessage.getInstantMessage().size());
            new DialogFragmentInstantMessage(instantMessage).show(getSupportFragmentManager(), "DialogFragmentInstantMessage");
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        if (HttpManager.isNOTOnline(this)) {
            Log.i(MyLog.SESS + TAG, "Device Offline");
            HttpManager.noInternetAccess(this);
        } else {
            Log.i(MyLog.SESS + TAG, "Device Online");
            switch (item.getItemId()) {

                //Home
                case R.id.tab_Home:
                    Log.i(MyLog.SESS + TAG, "On Tab Select Item : Tab Home");
                    getSupportFragmentManager()
                            .beginTransaction()
                            .replace(R.id.fragment_holder, new HomeFragment())
                            .commit()
                    ;
                    return true;

                //Food Reservation
                case R.id.tab_FoodReservation:
                    Log.i(MyLog.SESS + TAG, "On Tab Select Item : Tab Food Reservation");
                    getSupportFragmentManager()
                            .beginTransaction()
                            .replace(R.id.fragment_holder, new FoodReservationFragment())
                            .commit()
                    ;
                    return true;

                //Processes
                case R.id.tab_Processes:
                    Log.i(MyLog.SESS + TAG, "On Tab Select Item : Tab Processes");
                    getSupportFragmentManager()
                            .beginTransaction()
                            .replace(R.id.fragment_holder, new ProcessesFragment())
                            .commit()
                    ;
                    return true;

                //Messages
                case R.id.tab_Messages:
                    Log.i(MyLog.SESS + TAG, "On Tab Select Item : Tab Messages");
                    getSupportFragmentManager()
                            .beginTransaction()
                            .replace(R.id.fragment_holder, new MessagesFragment())
                            .commit()
                    ;
                    return true;
            }
        }
        return false;
    }

    public void moreOnClickListener(View view) {
        Log.i(MyLog.SESS + TAG, "Open AboutActivity");
        startActivity(new Intent(BottomBarActivity.this, AboutActivity.class));
    }

    @Override
    public void onBackPressed() {
        Log.i(MyLog.SESS + TAG, "onBackPressed");
        if (doubleBackToExitPressedOnce) {
            Log.i(MyLog.SESS + TAG, "Exit form App");
            super.onBackPressed();
            finish();
            finishAffinity();
            finishAndRemoveTask();
            System.exit(0);
            return;
        }

        this.doubleBackToExitPressedOnce = true;
        CustomToastExit.exit(BottomBarActivity.this, getResources().getString(R.string.exit_message), Toast.LENGTH_SHORT).show();
        new Handler(Looper.getMainLooper()).postDelayed(() -> doubleBackToExitPressedOnce = false, 2000);
    }

    @Override
    public void addUserPersonalInfo(String name, String family, String cookie) {
        Log.i(MyLog.SESS + TAG, "Update User Information From Sign in Dialog");
        updateUserInformation(name, family, cookie);
    }

    @SuppressLint("SetTextI18n")
    private void updateUserInformation(String name, String family, String cookie) {
        Log.i(MyLog.SESS + TAG, "Update User Information");
        Tools.displayImageOriginal(this, imageViewProfile, MyConfig.IMAGE + cookie);
        fullName.setText(name + " " + family);
    }

    @SuppressLint("SetTextI18n")
    private void updateUserInformation() {
        Log.i(MyLog.SESS + TAG, "Update User Information NO Argument");
        if (preferencesUtils.getFirstName() == null && preferencesUtils.getLastName() == null) {
            Tools.displayImageOriginal(this, imageViewProfile, R.drawable.ic_university);
            fullName.setText(getResources().getString(R.string.shahrekord_university));
        } else {
            fullName.setText(preferencesUtils.getFirstName() + " " + preferencesUtils.getLastName());
            if (preferencesUtils.getImageBitmap() != null)
                Tools.displayImageOriginal(this, imageViewProfile, preferencesUtils.getImageBitmap());
            else
                Tools.displayImageOriginal(this, imageViewProfile, MyConfig.IMAGE + preferencesUtils.getCookie());
        }
    }
}