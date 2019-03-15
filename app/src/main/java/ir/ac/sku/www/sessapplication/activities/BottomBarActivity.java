package ir.ac.sku.www.sessapplication.activities;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import com.bumptech.glide.Glide;
import com.roughike.bottombar.BottomBar;

import de.hdodenhof.circleimageview.CircleImageView;
import ir.ac.sku.www.sessapplication.API.MyConfig;
import ir.ac.sku.www.sessapplication.API.MyLog;
import ir.ac.sku.www.sessapplication.API.PreferenceName;
import ir.ac.sku.www.sessapplication.R;
import ir.ac.sku.www.sessapplication.fragment.FoodReservationFragment;
import ir.ac.sku.www.sessapplication.fragment.HomeFragment;
import ir.ac.sku.www.sessapplication.fragment.MessagesFragment;
import ir.ac.sku.www.sessapplication.fragment.ProcessesFragment;
import ir.ac.sku.www.sessapplication.models.LoginInformation;
import ir.ac.sku.www.sessapplication.models.SendInformation;
import ir.ac.sku.www.sessapplication.utils.CustomToastExit;
import ir.ac.sku.www.sessapplication.utils.HttpManager;
import ir.ac.sku.www.sessapplication.utils.InstantMessage;
import ir.ac.sku.www.sessapplication.utils.MyActivity;
import ir.ac.sku.www.sessapplication.utils.MyApplication;

public class BottomBarActivity extends MyActivity {

    //My Fragment
    private HomeFragment homeFragment;
    private FoodReservationFragment foodReservationFragment;
    private ProcessesFragment processesFragment;
    private MessagesFragment messagesFragment;

    //My View
    private BottomNavigationView bottomBar;
    //private ImageView logo;
    private ImageView more;
    private CircleImageView profile;
    private int mMenuId;
    private TextView fullName;
    private TextView slash;

    private SharedPreferences preferencesCookie;
    private SharedPreferences preferencesName;
    private SharedPreferences preferencesUserImage;

    private SendInformation.Result instantMessage;
    private boolean doubleBackToExitPressedOnce = false;

    @SuppressLint({"LongLogTag", "SetTextI18n"})
    @Override

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bottom_bar);
        //Initial Views
        init();

        Log.i(MyLog.BOTTOM_BAR_ACTIVITY, "___Bottom Bar Activity___");

        preferencesCookie = BottomBarActivity.this.getSharedPreferences(PreferenceName.COOKIE_PREFERENCE_NAME, Context.MODE_PRIVATE);
        preferencesName = getSharedPreferences(PreferenceName.NAME_PREFERENCE_NAME, MODE_PRIVATE);
        preferencesUserImage = getSharedPreferences(PreferenceName.USER_IMAGE_PREFERENCE_NAME, MODE_PRIVATE);


        instantMessage = new SendInformation.Result();

        instantMessage = (SendInformation.Result) getIntent().getParcelableExtra("InstantMessage");

        if (instantMessage != null) {

            Glide
                    .with(BottomBarActivity.this)
                    .load("http://app.sku.ac.ir/api/v1/user/image?cookie=" + preferencesCookie.getString(PreferenceName.COOKIE_PREFERENCE_COOKIE, null))
                    .into(profile);

            if (instantMessage.getInstantMessage().size() > 0) {
                Log.i(MyLog.BOTTOM_BAR_ACTIVITY, "Instant Message Size: " + instantMessage.getInstantMessage().size());
                InstantMessage message = new InstantMessage(BottomBarActivity.this, instantMessage);
                message.showInstantMessageDialog();
            }
        } else {
            String userImage = preferencesUserImage.getString(PreferenceName.USER_IMAGE_PREFERENCE_IMAGE, null);

            if (userImage != null) {
                byte[] b = Base64.decode(userImage, Base64.DEFAULT);
                Bitmap bitmap = BitmapFactory.decodeByteArray(b, 0, b.length);
                profile.setImageBitmap(bitmap);
            }
        }

        if (preferencesName.getString(PreferenceName.NAME_PREFERENCE_FIRST_NAME, null) == null && preferencesName.getString(PreferenceName.NAME_PREFERENCE_LAST_NAME, null) == null) {
            slash.setVisibility(View.GONE);
            profile.setVisibility(View.GONE);
            fullName.setText(" ");
        } else {
            fullName.setText(preferencesName.getString(PreferenceName.NAME_PREFERENCE_FIRST_NAME, " ") + " " + preferencesName.getString(PreferenceName.NAME_PREFERENCE_LAST_NAME, " "));
        }

        //allocate Fragments
        homeFragment = new HomeFragment();
        foodReservationFragment = new FoodReservationFragment();
        processesFragment = new ProcessesFragment();
        messagesFragment = new MessagesFragment();


        //My Action
        BottomBarAction();

        Toolbar toolbar = findViewById(R.id.bottomBarActivity_ToolBar);
        setSupportActionBar(toolbar);

        if (savedInstanceState == null) {
            bottomBar.setSelectedItemId(R.id.tab_Home);
        }
    }

    private void init() {
        bottomBar = findViewById(R.id.bottomBarActivity_BottomBar);
        //logo = findViewById(R.id.bottomBarActivity_Logo);
        more = findViewById(R.id.bottomBarActivity_More);
        profile = findViewById(R.id.bottomBarActivity_ImageProfile);
        fullName = findViewById(R.id.bottomBarActivity_FullName);
        slash = findViewById(R.id.bottomBarActivity_Slash);
    }

    private void BottomBarAction() {

        bottomBar.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @SuppressLint("LongLogTag")
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                if (HttpManager.isNOTOnline(BottomBarActivity.this)) {
                    Log.i(MyLog.BOTTOM_BAR_ACTIVITY, "OFF_LINE");
                    HttpManager.noInternetAccess(BottomBarActivity.this);
                } else {
                    Log.i(MyLog.BOTTOM_BAR_ACTIVITY, "ON_LINE");
                    switch (menuItem.getItemId()) {
                        //Home
                        case R.id.tab_Home:
                            Log.i(MyLog.BOTTOM_BAR_ACTIVITY, "On Tab Select Item : Tab Home");
                            getSupportFragmentManager()
                                    .beginTransaction()
                                    .replace(R.id.bottomBarActivity_FragmentHolder, homeFragment)
                                    .commit();
                            return true;
                        //Food Reservation
                        case R.id.tab_FoodReservation:
                            Log.i(MyLog.BOTTOM_BAR_ACTIVITY, "On Tab Select Item : Tab Food Reservation");
                            getSupportFragmentManager()
                                    .beginTransaction()
                                    .replace(R.id.bottomBarActivity_FragmentHolder, foodReservationFragment)
                                    .commit();
                            return true;
                        //Processes
                        case R.id.tab_Processes:
                            Log.i(MyLog.BOTTOM_BAR_ACTIVITY, "On Tab Select Item : Tab Processes");
                            getSupportFragmentManager()
                                    .beginTransaction()
                                    .replace(R.id.bottomBarActivity_FragmentHolder, processesFragment)
                                    .commit();
                            return true;
                        //Messages
                        case R.id.tab_Messages:
                            Log.i(MyLog.BOTTOM_BAR_ACTIVITY, "On Tab Select Item : Tab Messages");
                            getSupportFragmentManager()
                                    .beginTransaction()
                                    .replace(R.id.bottomBarActivity_FragmentHolder, messagesFragment)
                                    .commit();
                            return true;
                        //Default
                        default:
                            Log.i(MyLog.BOTTOM_BAR_ACTIVITY, "On Tab Select Item : Tab Home");
                            getSupportFragmentManager()
                                    .beginTransaction()
                                    .replace(R.id.bottomBarActivity_FragmentHolder, homeFragment)
                                    .commit();
                            return true;
                    }
                }
                return false;
            }
        });

        /*logo.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("LongLogTag")
            @Override
            public void onClick(View v) {
                Log.i(MyLog.BOTTOM_BAR_ACTIVITY, "On Logo Item ClickListener");

                *//*Log.i(MyLog.BOTTOM_BAR_ACTIVITY, "Clear Cookie");
                SharedPreferences.Editor editorCookie = preferencesCookie.edit();
                editorCookie.putString(PreferenceName.COOKIE_PREFERENCE_COOKIE, null);
                editorCookie.apply();*//*

                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse("https://www.sku.ac.ir/"));
                startActivity(intent);
                Log.i(MyLog.BOTTOM_BAR_ACTIVITY, "Open : " + intent.getData());
            }
        });

        logo.setOnLongClickListener(new View.OnLongClickListener() {
            @SuppressLint("LongLogTag")
            @Override
            public boolean onLongClick(View v) {
                Log.i(MyLog.BOTTOM_BAR_ACTIVITY, "On Logo Item LongClickListener");
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse("http://app.sku.ac.ir/admin/login"));
                startActivity(intent);
                Log.i(MyLog.BOTTOM_BAR_ACTIVITY, "Open : " + intent.getData());
                return false;
            }
        });*/

        more.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("LongLogTag")
            @Override
            public void onClick(View v) {
                Log.i(MyLog.BOTTOM_BAR_ACTIVITY, "Open AboutActivity");
                startActivity(new Intent(BottomBarActivity.this, AboutActivity.class));
            }
        });
    }


    @SuppressLint({"LongLogTag", "NewApi"})
    @Override
    public void onBackPressed() {
        Log.i(MyLog.LOGIN_ACTIVITY, "onBackPressed");
        if (doubleBackToExitPressedOnce) {
            Log.i(MyLog.LOGIN_ACTIVITY, "Exit form App");
            super.onBackPressed();
            finish();
            finishAffinity();
            finishAndRemoveTask();
            System.exit(0);
            return;
        }

        this.doubleBackToExitPressedOnce = true;

        CustomToastExit.exit(BottomBarActivity.this, "برای خروج برنامه دو بار کلید بازگشت را فشار دهید", Toast.LENGTH_SHORT).show();

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce = false;
            }
        }, 2000);
    }
}
