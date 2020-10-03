package ir.ac.sku.www.sessapplication.model.information;

import com.google.gson.JsonObject;

import java.util.Map;

import ir.ac.sku.www.sessapplication.api.ApplicationAPI;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.QueryMap;
import rx.Observable;

public interface informationService {
    @GET(ApplicationAPI.INFORMATION)
    Observable<JsonObject> getLoginInformation();

    @POST(ApplicationAPI.INFORMATION)
    Observable<JsonObject> postSendInformation(@QueryMap Map<String, String> params);
}
