package ir.ac.sku.www.sessapplication.activities;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;

import com.roughike.bottombar.BottomBar;
import com.roughike.bottombar.OnTabSelectListener;


import ir.ac.sku.www.sessapplication.API.MyLog;
import ir.ac.sku.www.sessapplication.R;
import ir.ac.sku.www.sessapplication.fragment.FoodReservationFragment;
import ir.ac.sku.www.sessapplication.fragment.HomeFragment;
import ir.ac.sku.www.sessapplication.fragment.MessagesFragment;
import ir.ac.sku.www.sessapplication.fragment.ProcessesFragment;
import ir.ac.sku.www.sessapplication.models.LoginInformation;
import ir.ac.sku.www.sessapplication.utils.MyActivity;
import ir.ac.sku.www.sessapplication.utils.TypefaceSpan;
import pl.droidsonroids.gif.GifImageView;

public class BottomBarActivity extends MyActivity {

    //My Fragment
    private HomeFragment homeFragment;
    private FoodReservationFragment foodReservationFragment;
    private ProcessesFragment processesFragment;
    private MessagesFragment messagesFragment;

    //My Java Model Class
    private LoginInformation loginInformation;

    //My View
    private BottomBar bottomBar;

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

        //my Functions
        getLoginInformation();

        //Fragment New Instance
        Log.i(MyLog.BOTTOM_BAR_ACTIVITY, "Cookie : " + loginInformation.getCookie());
        foodReservationFragment = FoodReservationFragment.newInstance(loginInformation.getCookie());

        //My Action
        BottomBarAction();
    }

    private void init() {
        bottomBar = findViewById(R.id.bottomBar);
    }

    @SuppressLint("LongLogTag")
    private void getLoginInformation() {
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            if (extras.containsKey("cookie")) {
                loginInformation.setCookie(extras.getString("cookie"));
                Log.i(MyLog.BOTTOM_BAR_ACTIVITY, "Bundle Cookie : " + extras.getString("cookie"));
            }
        }
    }

    private void BottomBarAction() {
        bottomBar.setOnTabSelectListener(new OnTabSelectListener() {
            @SuppressLint({"CommitTransaction", "LongLogTag"})
            @Override
            public void onTabSelected(int tabId) {
                switch (tabId) {
                    //Home
                    case R.id.tab_Home:
                        Log.i(MyLog.BOTTOM_BAR_ACTIVITY, "On Tab Select Item : Tab Home");
                        getSupportFragmentManager()
                                .beginTransaction()
                                .replace(R.id.frame_fragmentHolder, homeFragment)
                                .commit();
                        break;
                    //Food Reservation
                    case R.id.tab_FoodReservation:
                        Log.i(MyLog.BOTTOM_BAR_ACTIVITY, "On Tab Select Item : Tab Food Reservation");
                        getSupportFragmentManager()
                                .beginTransaction()
                                .replace(R.id.frame_fragmentHolder, foodReservationFragment)
                                .commit();
                        break;
                    //Processes
                    case R.id.tab_Processes:
                        Log.i(MyLog.BOTTOM_BAR_ACTIVITY, "On Tab Select Item : Tab Processes");
                        getSupportFragmentManager()
                                .beginTransaction()
                                .replace(R.id.frame_fragmentHolder, processesFragment)
                                .commit();
                        break;
                    //Messages
                    case R.id.tab_Messages:
                        Log.i(MyLog.BOTTOM_BAR_ACTIVITY, "On Tab Select Item : Tab Messages");
                        getSupportFragmentManager()
                                .beginTransaction()
                                .replace(R.id.frame_fragmentHolder, messagesFragment)
                                .commit();
                        break;
                    //Default
                    default:
                        Log.i(MyLog.BOTTOM_BAR_ACTIVITY, "On Tab Select Item : Tab Home");
                        getSupportFragmentManager()
                                .beginTransaction()
                                .replace(R.id.frame_fragmentHolder, homeFragment)
                                .commit();
                        break;
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.logo_menu, menu);
        return true;
    }

    @SuppressLint("LongLogTag")
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_logo:
                Log.i(MyLog.BOTTOM_BAR_ACTIVITY, "On Logo Item Selected");
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse("https://www.sku.ac.ir/"));
                startActivity(intent);
                Log.i(MyLog.BOTTOM_BAR_ACTIVITY, "Open : " + intent.getData());
                break;
        }
        return true;
    }

}
