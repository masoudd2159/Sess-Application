package ir.ac.sku.www.sessapplication.fragment.dialogfragment;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import butterknife.BindView;
import ir.ac.sku.www.sessapplication.R;
import ir.ac.sku.www.sessapplication.api.MyLog;
import ir.ac.sku.www.sessapplication.base.BaseDialogFragment;

public class DialogFragmentNoInternetAccess extends BaseDialogFragment {

    @BindView(R.id.disconnect_WiFi) Button wifi;
    @BindView(R.id.disconnect_MobileData) Button data;

    @Override public int getLayoutResource() {
        return R.layout.custom_disconnect;
    }

    @Override
    public void onViewCreated(@NonNull View rootView, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(rootView, savedInstanceState);
        wifi.setOnClickListener(view -> {
            Log.i(MyLog.SESS + TAG, "on Wi-Fi Click");
            openWiFiSettingScreen(baseActivity);
        });

        data.setOnClickListener(view -> {
            Log.i(MyLog.SESS + TAG, "on Mobile Data Click");
            openDataUsageScreen(baseActivity);
        });
    }


    private void openWiFiSettingScreen(Context context) {
        Log.i(MyLog.SESS + TAG, "Open WiFi Setting Screen");
        context.startActivity(new Intent(Settings.ACTION_WIFI_SETTINGS));
    }

    private void openDataUsageScreen(Context context) {
        Log.i(MyLog.SESS + TAG, "Open Data Usage Screen");
        context.startActivity(new Intent().setComponent(new ComponentName("com.android.settings", "com.android.settings.Settings$DataUsageSummaryActivity")));
    }

    @Override
    public void onResume() {
        super.onResume();
        changeDialogSize((int) (getDisplayMetrics().widthPixels * 0.8), ViewGroup.LayoutParams.WRAP_CONTENT);
    }
}
