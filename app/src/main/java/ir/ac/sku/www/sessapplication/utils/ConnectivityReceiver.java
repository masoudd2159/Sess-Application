package ir.ac.sku.www.sessapplication.utils;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class ConnectivityReceiver extends BroadcastReceiver {

    public static ConnectivityReceiverListener listener;

    @Override
    public void onReceive(Context context, Intent intent) {
        boolean isConnected = ConnectivityReceiver.isConnected(context);

        if (listener != null) {
            listener.onNetworkConnectionChange(isConnected);
        }
    }

    private static boolean isConnected(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = null;
        if (connectivityManager != null) {
            networkInfo = connectivityManager.getActiveNetworkInfo();
        }
        return (networkInfo != null && networkInfo.isConnectedOrConnecting());
    }

    public interface ConnectivityReceiverListener {
        void onNetworkConnectionChange(boolean isConnected);
    }

    public void setListener(ConnectivityReceiver.ConnectivityReceiverListener listener) {
        ConnectivityReceiver.listener = listener;
    }
}
