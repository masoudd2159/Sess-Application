package ir.ac.sku.www.sessapplication.activity.home;

import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import androidx.annotation.ColorInt;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.Objects;

import butterknife.BindView;
import ir.ac.sku.www.sessapplication.R;
import ir.ac.sku.www.sessapplication.api.MyLog;
import ir.ac.sku.www.sessapplication.base.BaseActivity;
import ir.ac.sku.www.sessapplication.fragment.onlineshop.CategoryFragment;
import ir.ac.sku.www.sessapplication.fragment.onlineshop.OfferFragment;
import ir.ac.sku.www.sessapplication.fragment.onlineshop.ProfileFragment;
import ir.ac.sku.www.sessapplication.fragment.onlineshop.SearchFragment;

public class OnlineShopActivity extends BaseActivity {
    final Fragment fragment1 = new OfferFragment();
    final Fragment fragment2 = new CategoryFragment();
    final Fragment fragment3 = new SearchFragment();
    final Fragment fragment4 = new ProfileFragment();
    final FragmentManager fm = getSupportFragmentManager();
    @BindView(R.id.floatingActionButton) FloatingActionButton fabAdd;
    @BindView(R.id.bottomNavigationView) BottomNavigationView bottomNavigation;
    Fragment active = fragment1;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener = item -> {
        switch (item.getItemId()) {
            case R.id.tab_offer:
                fm.beginTransaction().hide(active).show(fragment1).commit();
                active = fragment1;
                return true;

            case R.id.tab_grouping:
                fm.beginTransaction().hide(active).show(fragment2).commit();
                active = fragment2;
                return true;

            case R.id.tab_search:
                fm.beginTransaction().hide(active).show(fragment3).commit();
                active = fragment3;
                return true;

            case R.id.tab_profile:
                fm.beginTransaction().hide(active).show(fragment4).commit();
                active = fragment4;
                return true;
        }
        return false;
    };

    //region test
    @Override protected int getLayoutResource() {
        return R.layout.activity_online_shop;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        fabAdd.setColorFilter(Color.WHITE);
        changeStatusBarColor();


        bottomNavigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        fm.beginTransaction().add(R.id.layout_content, fragment4, "4").hide(fragment4).commit();
        fm.beginTransaction().add(R.id.layout_content, fragment3, "3").hide(fragment3).commit();
        fm.beginTransaction().add(R.id.layout_content, fragment2, "2").hide(fragment2).commit();
        fm.beginTransaction().add(R.id.layout_content, fragment1, "1").commit();
    }

    //endregion

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        View v = getCurrentFocus();
        boolean ret = super.dispatchTouchEvent(event);
        if (v instanceof EditText) {
            View w = getCurrentFocus();
            int[] scrCoords = new int[2];
            if (w != null) {
                w.getLocationOnScreen(scrCoords);
            }
            float x = 0;
            if (w != null) {
                x = event.getRawX() + w.getLeft() - scrCoords[0];
            }
            float y = 0;
            if (w != null) {
                y = event.getRawY() + w.getTop() - scrCoords[1];
            }
            if (w != null && event.getAction() == MotionEvent.ACTION_UP && (x < w.getLeft() || x >= w.getRight() || y < w.getTop() || y > w.getBottom())) {
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                if (imm != null) {
                    imm.hideSoftInputFromWindow(Objects.requireNonNull(getWindow().getCurrentFocus()).getWindowToken(), 0);
                }
            }
        }
        return ret;
    }

    @Override
    public void changeStatusBarColor() {
        Log.i(MyLog.SESS + TAG, "Change Status Bar");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);//  set status text dark
            getWindow().setStatusBarColor(ContextCompat.getColor(OnlineShopActivity.this,R.color.white));// set status background white
        }
    }
}