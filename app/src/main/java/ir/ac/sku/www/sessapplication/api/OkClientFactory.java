package ir.ac.sku.www.sessapplication.api;

import android.app.Application;

import androidx.annotation.NonNull;

import java.io.File;
import java.util.concurrent.TimeUnit;

import okhttp3.Cache;
import okhttp3.OkHttpClient;

public class OkClientFactory {
    // Cache size for the OkHttpClient

    private static final int DISK_CACHE_SIZE = 50 * 1024 * 1024; // 50MB
    private static final int OK_HTTP_TIMEOUT = 180; // seconds

    private OkClientFactory() {
    }

    @NonNull
    public static OkHttpClient provideOkHttpClient(final Application app) {
        // Install an HTTP cache in the application cache directory.
        File cacheDir = new File(app.getCacheDir(), "http");
        Cache cache = new Cache(cacheDir, DISK_CACHE_SIZE);
        OkHttpClient.Builder builder;


        builder = new OkHttpClient.Builder()
                .connectTimeout(OK_HTTP_TIMEOUT, TimeUnit.SECONDS)
                .writeTimeout(OK_HTTP_TIMEOUT, TimeUnit.SECONDS)
                .readTimeout(OK_HTTP_TIMEOUT, TimeUnit.SECONDS)
                .cache(cache);

        return builder.build();
    }
}
