package ir.ac.sku.www.sessapplication.utils;

import android.app.Application;
import android.content.Context;

import io.github.inflationx.calligraphy3.CalligraphyConfig;
import io.github.inflationx.calligraphy3.CalligraphyInterceptor;
import io.github.inflationx.viewpump.ViewPump;
import ir.ac.sku.www.sessapplication.R;

public class MyApplication extends Application {

    private static Context context;
    private static MyApplication instance;

    @Override
    public void onCreate() {
        super.onCreate();

        ViewPump.init(ViewPump.builder()
                .addInterceptor(new CalligraphyInterceptor(
                        new CalligraphyConfig.Builder()
                                .setDefaultFontPath("fonts/IRANSansMobile(FaNum).ttf")
                                .setFontAttrId(R.attr.fontPath)
                                .build()))
                .build());

        instance = this;
    }

    public static Context getContext() {
        return context;
    }

    public static void setContext(Context context) {
        MyApplication.context = context;
    }

    public static synchronized MyApplication getInstance() {
        return instance;
    }

    public void setConnectivityReceiverListener(ConnectivityReceiver.ConnectivityReceiverListener listener) {
        ConnectivityReceiver.listener = listener;
    }
}
