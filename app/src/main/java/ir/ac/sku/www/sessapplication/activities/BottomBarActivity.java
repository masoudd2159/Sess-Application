package ir.ac.sku.www.sessapplication.activities;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ImageView;

import com.roughike.bottombar.BottomBar;
import com.roughike.bottombar.OnTabSelectListener;


import ir.ac.sku.www.sessapplication.R;
import ir.ac.sku.www.sessapplication.fragment.FoodReservationFragment;
import ir.ac.sku.www.sessapplication.fragment.HomeFragment;
import ir.ac.sku.www.sessapplication.fragment.MessagesFragment;
import ir.ac.sku.www.sessapplication.fragment.ProcessesFragment;
import ir.ac.sku.www.sessapplication.models.LoginInformation;
import ir.ac.sku.www.sessapplication.utils.MyActivity;

public class BottomBarActivity extends MyActivity {

    HomeFragment homeFragment;
    FoodReservationFragment foodReservationFragment;
    ProcessesFragment processesFragment;
    MessagesFragment messagesFragment;

    LoginInformation loginInformation;


    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bottom_bar);
        BottomBar bottomBar = findViewById(R.id.bottomBar);
        loginInformation = new LoginInformation();

        homeFragment = new HomeFragment();
        foodReservationFragment = new FoodReservationFragment();
        processesFragment = new ProcessesFragment();
        messagesFragment = new MessagesFragment();

        getLoginInformation();
        foodReservationFragment = FoodReservationFragment.newInstance(loginInformation.getCookie());

        bottomBar.setOnTabSelectListener(new OnTabSelectListener() {
            @SuppressLint("CommitTransaction")
            @Override
            public void onTabSelected(int tabId) {
                switch (tabId) {

                    case R.id.tab_Home:
                        getSupportFragmentManager()
                                .beginTransaction()
                                .replace(R.id.frame_fragmentHolder, homeFragment)
                                .commit();
                        break;


                    case R.id.tab_FoodReservation:
                        getSupportFragmentManager()
                                .beginTransaction()
                                .replace(R.id.frame_fragmentHolder, foodReservationFragment)
                                .commit();
                        break;


                    case R.id.tab_Processes:
                        getSupportFragmentManager()
                                .beginTransaction()
                                .replace(R.id.frame_fragmentHolder, processesFragment)
                                .commit();
                        break;


                    case R.id.tab_Messages:
                        getSupportFragmentManager()
                                .beginTransaction()
                                .replace(R.id.frame_fragmentHolder, messagesFragment)
                                .commit();
                        break;
                }
            }
        });
    }

    private void getLoginInformation() {
        Bundle extras = getIntent().getExtras();

        if (extras != null) {
            if (extras.containsKey("ok")) {
                loginInformation.setOk(extras.getBoolean("ok"));
            }
            if (extras.containsKey("cookie")) {
                loginInformation.setCookie(extras.getString("cookie"));
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.logo_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_logo:
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse("https://www.sku.ac.ir/"));
                startActivity(intent);
                break;
        }
        return true;
    }
}
