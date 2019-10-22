package ir.ac.sku.www.sessapplication.activities;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.util.Base64;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.coordinatorlayout.widget.CoordinatorLayout;

import com.bumptech.glide.Glide;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.snackbar.Snackbar;

import co.ronash.pushe.Pushe;
import de.hdodenhof.circleimageview.CircleImageView;
import ir.ac.sku.www.sessapplication.API.MyLog;
import ir.ac.sku.www.sessapplication.API.PreferenceName;
import ir.ac.sku.www.sessapplication.R;
import ir.ac.sku.www.sessapplication.fragment.FoodReservationFragment;
import ir.ac.sku.www.sessapplication.fragment.HomeFragment;
import ir.ac.sku.www.sessapplication.fragment.MessagesFragment;
import ir.ac.sku.www.sessapplication.fragment.ProcessesFragment;
import ir.ac.sku.www.sessapplication.models.SendInformation;
import ir.ac.sku.www.sessapplication.utils.ColoredSnackBar;
import ir.ac.sku.www.sessapplication.utils.ConnectivityReceiver;
import ir.ac.sku.www.sessapplication.utils.CustomToastExit;
import ir.ac.sku.www.sessapplication.utils.HttpManager;
import ir.ac.sku.www.sessapplication.utils.InstantMessage;
import ir.ac.sku.www.sessapplication.utils.MyActivity;

public class BottomBarActivity extends MyActivity implements ConnectivityReceiver.ConnectivityReceiverListener {

    //My Fragment
    private HomeFragment homeFragment;
    private FoodReservationFragment foodReservationFragment;
    private ProcessesFragment processesFragment;
    private MessagesFragment messagesFragment;

    //My View
    private BottomNavigationView bottomBar;
    private ImageView more;
    private ImageView profile;
    private CardView cardViewProfile;
    private TextView fullName;
    private View slash;
    private CoordinatorLayout fragmentHolder;


    private SharedPreferences preferencesCookie;
    private SharedPreferences preferencesName;
    private SharedPreferences preferencesUserImage;

    private SendInformation.Result instantMessage;
    private ConnectivityReceiver receiver;
    private boolean doubleBackToExitPressedOnce = false;
    private boolean starter = false;

    @SuppressLint({"LongLogTag", "SetTextI18n"})
    @Override

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bottom_bar);
        Pushe.initialize(this, true);
        //Initial Views
        init();

        receiver = new ConnectivityReceiver();

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
            cardViewProfile.setVisibility(View.GONE);
            profile.setVisibility(View.GONE);
            slash.setVisibility(View.GONE);
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

    @Override
    protected void onResume() {
        super.onResume();
        receiver.setListener(this);
        registerReceiver(receiver, new IntentFilter("android.net.conn.CONNECTIVITY_CHANGE"));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        receiver.setListener(null);
        unregisterReceiver(receiver);
    }

    private void init() {
        bottomBar = findViewById(R.id.bottomBarActivity_BottomBar);
        //logo = findViewById(R.id.bottomBarActivity_Logo);
        more = findViewById(R.id.bottomBarActivity_More);
        profile = findViewById(R.id.bottomBarActivity_ImageProfile);
        cardViewProfile = findViewById(R.id.bottomBarActivity_CardViewProfile);
        fullName = findViewById(R.id.bottomBarActivity_FullName);
        slash = findViewById(R.id.bottomBarActivity_Slash);
        fragmentHolder = findViewById(R.id.bottomBarActivity_FragmentHolder);
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

    @Override
    public void onNetworkConnectionChange(boolean isConnected) {
        if (starter) {
            if (isConnected) {
                //ColoredSnackBar.success(Snackbar.make(fragmentHolder, "شما به اینترنت متصل شدید.", Snackbar.LENGTH_SHORT)).show();
            } else {
                ColoredSnackBar.error(Snackbar.make(fragmentHolder, "مشکلی در اتصال به اینترنت به وجود آمده!", Snackbar.LENGTH_SHORT)).show();
            }
        } else {
            starter = true;
        }
    }
}
