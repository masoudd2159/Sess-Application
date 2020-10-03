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

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.mikhaellopez.circularimageview.CircularImageView;

import butterknife.BindView;
import co.ronash.pushe.Pushe;
import ir.ac.sku.www.sessapplication.R;
import ir.ac.sku.www.sessapplication.activity.about.AboutActivity;
import ir.ac.sku.www.sessapplication.api.ApplicationAPI;
import ir.ac.sku.www.sessapplication.api.MyLog;
import ir.ac.sku.www.sessapplication.base.BaseActivity;
import ir.ac.sku.www.sessapplication.fragment.FoodReservationFragment;
import ir.ac.sku.www.sessapplication.fragment.HomeFragment;
import ir.ac.sku.www.sessapplication.fragment.MessagesFragment;
import ir.ac.sku.www.sessapplication.fragment.ProcessesFragment;
import ir.ac.sku.www.sessapplication.fragment.dialogfragment.DialogFragmentSignIn;
import ir.ac.sku.www.sessapplication.model.information.SendInformation;
import ir.ac.sku.www.sessapplication.utils.CustomToastExit;
import ir.ac.sku.www.sessapplication.utils.Tools;
import ir.ac.sku.www.sessapplication.utils.helper.ManagerHelper;

public class BottomBarActivity
        extends BaseActivity
        implements DialogFragmentSignIn.UserInterface, BottomNavigationView.OnNavigationItemSelectedListener {

    //* Content View
    @BindView(R.id.Bottom_navigation_view) BottomNavigationView bottomBar;

    // ToolBar View
    @BindView(R.id.toolbar_image_profile) CircularImageView imageViewProfile;
    @BindView(R.id.toolbar_full_name) TextView textViewFullName;
    @BindView(R.id.toolbar_dash) View dash;

    private boolean doubleBackToExitPressedOnce = false;

    @Override
    protected int getLayoutResource() {
        return R.layout.activity_bottom_bar;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Pushe.initialize(this, true);

        updateUserInformation();

        bottomBar.setOnNavigationItemSelectedListener(this);

        if (savedInstanceState == null) bottomBar.setSelectedItemId(R.id.tab_Home);
    }

    @Override protected void onResume() {
        super.onResume();
        updateUserInformation();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        if (ManagerHelper.isInternet(this)) {
            Log.i(MyLog.SESS + TAG, "Device Offline");
            ManagerHelper.noInternetAccess(this);
        } else {
            Log.i(MyLog.SESS + TAG, "Device Online");
            switch (item.getItemId()) {

                //Home
                case R.id.tab_Home:
                    Log.i(MyLog.SESS + TAG, "On Tab Select Item : Tab Home");
                    getSupportFragmentManager()
                            .beginTransaction()
                            .replace(R.id.layout_content, new HomeFragment())
                            .commit()
                    ;
                    return true;

                //Food Reservation
                case R.id.tab_FoodReservation:
                    Log.i(MyLog.SESS + TAG, "On Tab Select Item : Tab Food Reservation");
                    getSupportFragmentManager()
                            .beginTransaction()
                            .replace(R.id.layout_content, new FoodReservationFragment())
                            .commit()
                    ;
                    return true;

                //Processes
                case R.id.tab_Processes:
                    Log.i(MyLog.SESS + TAG, "On Tab Select Item : Tab Processes");
                    getSupportFragmentManager()
                            .beginTransaction()
                            .replace(R.id.layout_content, new ProcessesFragment())
                            .commit()
                    ;
                    return true;

                //Messages
                case R.id.tab_Messages:
                    Log.i(MyLog.SESS + TAG, "On Tab Select Item : Tab Messages");
                    getSupportFragmentManager()
                            .beginTransaction()
                            .replace(R.id.layout_content, new MessagesFragment())
                            .commit()
                    ;
                    return true;
            }
        }
        return false;
    }

    public void onClickItemMore(View view) {
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

    @SuppressLint("SetTextI18n")
    private void updateUserInformation(SendInformation.Result.UserInformation userInformation, String cookie) {
        Log.i(MyLog.SESS + TAG, "Update User Information");
        if (userInformation.getName() == null && userInformation.getFamily() == null) {
            Tools.displayImageOriginal(this, imageViewProfile, R.drawable.ic_university);
            textViewFullName.setText(getResources().getString(R.string.shahrekord_university));
        } else {
            Tools.displayImageOriginal(this, imageViewProfile, ApplicationAPI.IMAGE + cookie);
            String fullName = userInformation.getName() + " " + userInformation.getFamily();
            switch (userInformation.getSex()) {
                case "مرد":
                    textViewFullName.setText("آقای " + fullName);
                    break;
                case "زن":
                    textViewFullName.setText("خانم " + fullName);
                    break;
                default:
                    textViewFullName.setText(fullName);
                    break;
            }
        }
    }

    @SuppressLint("SetTextI18n")
    private void updateUserInformation() {
        Log.i(MyLog.SESS + TAG, "Update User Information NO Argument");
        if (preferencesUtils.getFirstName() == null && preferencesUtils.getLastName() == null) {
            Tools.displayImageOriginal(this, imageViewProfile, R.drawable.ic_university);
            textViewFullName.setText(getResources().getString(R.string.shahrekord_university));
        } else {
            Tools.displayImageOriginal(this, imageViewProfile, preferencesUtils.getImageBitmap());
            String fullName = preferencesUtils.getFirstName() + " " + preferencesUtils.getLastName();
            switch (preferencesUtils.getSex()) {
                case "مرد":
                    textViewFullName.setText("آقای " + fullName);
                    break;
                case "زن":
                    textViewFullName.setText("خانم " + fullName);
                    break;
                default:
                    textViewFullName.setText(fullName);
                    break;
            }
        }
    }

    @Override
    public void addUserPersonalInfo(SendInformation.Result.UserInformation userInformation, String cookie) {
        Log.i(MyLog.SESS + TAG, "Update User Information From Sign in Dialog");
        updateUserInformation(userInformation, cookie);
    }
}