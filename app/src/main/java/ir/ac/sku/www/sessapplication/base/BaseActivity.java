package ir.ac.sku.www.sessapplication.base;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.IntentFilter;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.material.snackbar.Snackbar;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.github.inflationx.viewpump.ViewPumpContextWrapper;
import ir.ac.sku.www.sessapplication.R;
import ir.ac.sku.www.sessapplication.api.MyLog;
import ir.ac.sku.www.sessapplication.utils.ColoredSnackBar;
import ir.ac.sku.www.sessapplication.utils.ConnectivityReceiver;
import ir.ac.sku.www.sessapplication.utils.SharedPreferencesUtils;

@SuppressLint("Registered")
public abstract class BaseActivity
        extends AppCompatActivity
        implements ConnectivityReceiver.ConnectivityReceiverListener {

    public String TAG = getClass().getSimpleName();
    public SharedPreferencesUtils preferencesUtils;
    @Nullable @BindView(R.id.toolbar_title) TextView title;
    @Nullable @BindView(R.id.toolbar) Toolbar toolbar;
    @Nullable @BindView(R.id.layout_content) View snackBarLayout;
    private ConnectivityReceiver receiver;
    private boolean starter = false;

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(ViewPumpContextWrapper.wrap(newBase));
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutResource());

        receiver = new ConnectivityReceiver();
        preferencesUtils = new SharedPreferencesUtils(this);

        ButterKnife.bind(this);

        initToolbar();
    }

    @Override protected void onResume() {
        super.onResume();
        receiver.setListener(this);
        registerReceiver(receiver, new IntentFilter("android.net.conn.CONNECTIVITY_CHANGE"));
    }

    @Override protected void onDestroy() {
        super.onDestroy();
        receiver.setListener(null);
        unregisterReceiver(receiver);
    }

    @Override
    public void onNetworkConnectionChange(boolean isConnected) {
        if (starter && !isConnected) {
            Log.i(MyLog.SESS + TAG, "Network NOT Connected");
            if (snackBarLayout != null)
                ColoredSnackBar.error(Snackbar.make(snackBarLayout, getResources().getString(R.string.connection_fail), Snackbar.LENGTH_SHORT)).show();
            else
                Toast.makeText(this, getResources().getString(R.string.connection_fail), Toast.LENGTH_SHORT).show();
        } else starter = true;
    }

    private void initToolbar() {
        if (toolbar != null) {
            Log.i(MyLog.SESS + TAG, "initial Toolbar");
            setSupportActionBar(toolbar);

            toolbar.setTitle("");
            toolbar.setSubtitle("");

            if (title != null) {
                title.setTypeface(Typeface.createFromAsset(getAssets(), "fonts/Lalezar.ttf"));
            }

            if (getSupportActionBar() != null) {
                getSupportActionBar().setDisplayHomeAsUpEnabled(true);
                getSupportActionBar().setDisplayShowTitleEnabled(false);
            }
        }
    }

    public void setTitleToolbar(String titleToolbar) {
        if (title != null) {
            Log.i(MyLog.SESS + TAG, "set Toolbar Title To " + titleToolbar);
            title.setText(titleToolbar);
        }
    }

    protected abstract int getLayoutResource();

    public void changeStatusBarColor() {
        Log.i(MyLog.SESS + TAG, "Change Status Bar");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}
