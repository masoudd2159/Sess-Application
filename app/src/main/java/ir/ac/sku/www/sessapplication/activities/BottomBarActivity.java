package ir.ac.sku.www.sessapplication.activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;


import ir.ac.sku.www.sessapplication.API.MyLog;
import ir.ac.sku.www.sessapplication.R;
import ir.ac.sku.www.sessapplication.fragment.FoodReservationFragment;
import ir.ac.sku.www.sessapplication.fragment.HomeFragment;
import ir.ac.sku.www.sessapplication.fragment.MessagesFragment;
import ir.ac.sku.www.sessapplication.fragment.ProcessesFragment;
import ir.ac.sku.www.sessapplication.models.LoginInformation;
import ir.ac.sku.www.sessapplication.utils.HttpManager;
import ir.ac.sku.www.sessapplication.utils.MyActivity;

public class BottomBarActivity extends MyActivity {

    //My Fragment
    private HomeFragment homeFragment;
    private FoodReservationFragment foodReservationFragment;
    private ProcessesFragment processesFragment;
    private MessagesFragment messagesFragment;

    //My Java Model Class
    private LoginInformation loginInformation;

    //My View
    private BottomNavigationView bottomBar;
    private ImageView logo;
    private ImageView more;

    @SuppressLint("LongLogTag")
    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bottom_bar);
        Log.i(MyLog.BOTTOM_BAR_ACTIVITY, "Start Bottom Bar Activity");
        //Initial Views
        init();

        //allocate Classes
        loginInformation = new LoginInformation();

        //allocate Fragments
        homeFragment = new HomeFragment();
        foodReservationFragment = new FoodReservationFragment();
        processesFragment = new ProcessesFragment();
        messagesFragment = new MessagesFragment();

        //My Action
        BottomBarAction();

        Toolbar toolbar = findViewById(R.id.bottomBarActivity_ToolBar);
        setSupportActionBar(toolbar);
    }

    private void init() {
        bottomBar = findViewById(R.id.bottomBarActivity_BottomBar);
        logo = findViewById(R.id.bottomBarActivity_Logo);
        more = findViewById(R.id.bottomBarActivity_More);
    }

    private void BottomBarAction() {

        bottomBar.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @SuppressLint("LongLogTag")
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                if (HttpManager.isNOTOnline(BottomBarActivity.this)) {
                    HttpManager.noInternetAccess(BottomBarActivity.this);
                } else {
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

        logo.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("LongLogTag")
            @Override
            public void onClick(View v) {
                Log.i(MyLog.BOTTOM_BAR_ACTIVITY, "On Logo Item Selected");
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse("https://www.sku.ac.ir/"));
                startActivity(intent);
                Log.i(MyLog.BOTTOM_BAR_ACTIVITY, "Open : " + intent.getData());
            }
        });

        more.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(BottomBarActivity.this, AboutActivity.class));
            }
        });
    }
}
