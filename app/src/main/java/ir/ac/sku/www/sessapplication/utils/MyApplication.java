package ir.ac.sku.www.sessapplication.utils;

import androidx.multidex.MultiDexApplication;

import io.github.inflationx.calligraphy3.CalligraphyConfig;
import io.github.inflationx.calligraphy3.CalligraphyInterceptor;
import io.github.inflationx.viewpump.ViewPump;
import ir.ac.sku.www.sessapplication.R;
import ir.ac.sku.www.sessapplication.api.OkClientFactory;
import okhttp3.OkHttpClient;

public class MyApplication extends MultiDexApplication {

    private static MyApplication appInstance;
    private static OkHttpClient mOkHttpClient;

    public static MyApplication getAppInstance() {
        return appInstance;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        initializeOkHttpClient();

        ViewPump.init(ViewPump.builder()
                .addInterceptor(new CalligraphyInterceptor(
                        new CalligraphyConfig.Builder()
                                .setDefaultFontPath("fonts/IRANSansMobile(FaNum).ttf")
                                .setFontAttrId(R.attr.fontPath)
                                .build()))
                .build());
    }

    public OkHttpClient getOkHttpClient() {
        return mOkHttpClient;
    }

    private void initializeOkHttpClient() {
        appInstance = this;
        mOkHttpClient = OkClientFactory.provideOkHttpClient(this);
    }
}
