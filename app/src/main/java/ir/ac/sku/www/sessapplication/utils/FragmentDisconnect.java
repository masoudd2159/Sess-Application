package ir.ac.sku.www.sessapplication.utils;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import ir.ac.sku.www.sessapplication.R;

public class FragmentDisconnect extends DialogFragment {

    Button wifi;
    Button data;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.disconnect, container, false);

        wifi = rootView.findViewById(R.id.disconnect_WiFi);
        data = rootView.findViewById(R.id.disconnect_MobileData);

        wifi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                IntentHelper.openWiFiSettingScreen(rootView.getContext());
            }
        });

        data.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                IntentHelper.openDataUsageScreen(rootView.getContext());
            }
        });
        return rootView;
    }
}
