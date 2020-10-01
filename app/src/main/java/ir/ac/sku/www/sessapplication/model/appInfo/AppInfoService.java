package ir.ac.sku.www.sessapplication.model.appInfo;

import com.google.gson.JsonObject;

import ir.ac.sku.www.sessapplication.api.ApplicationAPI;
import retrofit2.http.GET;
import rx.Observable;

public interface AppInfoService {
    @GET(ApplicationAPI.APP_INFO)
    Observable<JsonObject> getAppInfo();
}
