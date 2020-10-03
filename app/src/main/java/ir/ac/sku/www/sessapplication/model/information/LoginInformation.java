package ir.ac.sku.www.sessapplication.model.information;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.HashMap;

import ir.ac.sku.www.sessapplication.R;
import ir.ac.sku.www.sessapplication.api.ApiFactory;
import ir.ac.sku.www.sessapplication.api.MyLog;
import ir.ac.sku.www.sessapplication.api.RxApiCallHelper;
import ir.ac.sku.www.sessapplication.api.RxApiCallback;
import ir.ac.sku.www.sessapplication.model.appInfo.AppInfo;
import ir.ac.sku.www.sessapplication.utils.MyHandler;
import ir.ac.sku.www.sessapplication.utils.helper.ManagerHelper;
import rx.Observable;

public class LoginInformation {

    @SerializedName("ok")
    @Expose
    private boolean ok;

    @SerializedName("cookie")
    @Expose
    private String cookie;

    @SerializedName("description")
    @Expose
    private Description description;

    public static void fetchFromWeb(@NonNull Context context, HashMap<String, String> params, final MyHandler handler) {
        if (ManagerHelper.checkInternetServices(context)) {
            Observable<JsonObject> observable = ApiFactory.createProvideApiService(informationService.class).getLoginInformation();
            RxApiCallHelper.call(observable, new RxApiCallback<JsonObject>() {
                @Override
                public void onSuccess(JsonObject jsonObject) {
                    Log.i(MyLog.SESS + LoginInformation.class.getSimpleName(), "Object Successfully Receive");
                    LoginInformation response = new Gson().fromJson(jsonObject, LoginInformation.class);
                    if (response.isOk()) {
                        handler.onResponse(true, response);
                    } else {
                        Log.i(MyLog.SESS + LoginInformation.class.getSimpleName()
                                , "Response False : <" + response.getDescription().getErrorCode() + "> " + response.getDescription().getErrorText());
                        ManagerHelper.unsuccessfulOperation(context, response.getDescription().getErrorText());
                    }
                }

                @Override
                public void onFailed(String errorMsg) {
                    Log.i(MyLog.SESS + AppInfo.class.getSimpleName(), errorMsg);
                    Toast.makeText(context, R.string.unable_to_connect_to_the_server, Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    public boolean isOk() {
        return ok;
    }

    public void setOk(boolean ok) {
        this.ok = ok;
    }

    public String getCookie() {
        return cookie;
    }

    public void setCookie(String cookie) {
        this.cookie = cookie;
    }

    public Description getDescription() {
        return description;
    }

    public void setDescription(Description description) {
        this.description = description;
    }

    public class Description {
        @SerializedName("errorText")
        @Expose
        private String errorText;

        @SerializedName("errorCode")
        @Expose
        private String errorCode;

        public String getErrorText() {
            return errorText;
        }

        public void setErrorText(String errorText) {
            this.errorText = errorText;
        }

        public String getErrorCode() {
            return errorCode;
        }

        public void setErrorCode(String errorCode) {
            this.errorCode = errorCode;
        }
    }
}
