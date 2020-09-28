package ir.ac.sku.www.sessapplication.base;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.IntentFilter;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.material.snackbar.Snackbar;

import butterknife.BindView;
import butterknife.ButterKnife;
import co.ronash.pushe.Pushe;
import io.github.inflationx.viewpump.ViewPumpContextWrapper;
import ir.ac.sku.www.sessapplication.R;
import ir.ac.sku.www.sessapplication.utils.ColoredSnackBar;
import ir.ac.sku.www.sessapplication.utils.ConnectivityReceiver;

@SuppressLint("Registered")
public abstract class BaseActivity
        extends AppCompatActivity
        implements ConnectivityReceiver.ConnectivityReceiverListener {

    public String TAG = getClass().getSimpleName();

    @Nullable @BindView(R.id.toolbar_title) TextView title;
    @Nullable @BindView(R.id.toolbar) Toolbar toolbar;

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

        Pushe.initialize(this, true);
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
        if (starter) {
            ColoredSnackBar.error(Snackbar.make(getLayoutSnackBar(), "مشکلی در اتصال به اینترنت به وجود آمده!", Snackbar.LENGTH_SHORT)).show();
        } else {
            starter = true;
        }
    }

    private void initToolbar() {
        if (toolbar != null) {
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

    protected abstract int getLayoutResource();

    protected abstract View getLayoutSnackBar();
}
