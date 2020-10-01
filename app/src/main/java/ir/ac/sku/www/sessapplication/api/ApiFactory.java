package ir.ac.sku.www.sessapplication.api;

import com.google.gson.GsonBuilder;

import ir.ac.sku.www.sessapplication.utils.MyApplication;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class ApiFactory {

    private static Retrofit provideApi() {
        return new Retrofit.Builder()
                .baseUrl(ApplicationAPI.BASE_URL)
                .client(MyApplication.getAppInstance().getOkHttpClient())
                .addConverterFactory(GsonConverterFactory.create(new GsonBuilder().setLenient().create()))
                .addConverterFactory(ScalarsConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .build();
    }

    public static <S> S createProvideApiService(Class<S> serviceClass) {
        return provideApi().create(serviceClass);
    }
}
