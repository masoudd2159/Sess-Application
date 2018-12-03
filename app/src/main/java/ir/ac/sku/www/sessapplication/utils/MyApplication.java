package ir.ac.sku.www.sessapplication.utils;

import android.app.Application;
import android.content.Context;

import ir.ac.sku.www.sessapplication.R;
import uk.co.chrisjenx.calligraphy.CalligraphyConfig;

public class MyApplication extends Application {

    private static Context context;

    @Override
    public void onCreate() {
        super.onCreate();

        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                .setDefaultFontPath("fonts/IRANSans(FaNum).ttf")
                .setFontAttrId(R.attr.fontPath)
                .build()
        );
    }

    public static Context getContext() {
        return context;
    }

    public static void setContext(Context context) {
        MyApplication.context = context;
    }
}
