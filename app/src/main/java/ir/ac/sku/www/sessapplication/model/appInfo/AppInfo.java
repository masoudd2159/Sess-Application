package ir.ac.sku.www.sessapplication.model.appInfo;

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
import ir.ac.sku.www.sessapplication.utils.MyHandler;
import ir.ac.sku.www.sessapplication.utils.helper.ManagerHelper;
import rx.Observable;

public class AppInfo {

    @SerializedName("ok")
    @Expose
    private boolean ok;
    @SerializedName("result")
    @Expose
    private Result result;

    public static void fetchFromWeb(@NonNull Context context, HashMap<String, String> params, MyHandler handler) {
        if (ManagerHelper.checkInternetServices(context)) {
            Observable<JsonObject> observable = ApiFactory.createProvideApiService(AppInfoService.class).getAppInfo();
            RxApiCallHelper.call(observable, new RxApiCallback<JsonObject>() {
                @Override
                public void onSuccess(JsonObject jsonObject) {
                    Log.i(MyLog.SESS + AppInfo.class.getSimpleName(), "Object Successfully Receive");
                    AppInfo response = new Gson().fromJson(jsonObject, AppInfo.class);
                    if (response.isOk()) {
                        handler.onResponse(true, response);
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

    public Result getResult() {
        return result;
    }

    public void setResult(Result result) {
        this.result = result;
    }

    public class Result {
        @SerializedName("androidMinimumVersion")
        @Expose
        private int androidMinimumVersion;
        @SerializedName("androidLatestVersion")
        @Expose
        private int androidLatestVersion;
        @SerializedName("downloadAndroidUrl")
        @Expose
        private String downloadAndroidUrl;
        @SerializedName("iosMinimumVersion")
        @Expose
        private int iosMinimumVersion;
        @SerializedName("iosLatestVersion")
        @Expose
        private int iosLatestVersion;
        @SerializedName("downloadIosUrl")
        @Expose
        private String downloadIosUrl;
        @SerializedName("updateMessage")
        @Expose
        private String updateMessage;
        @SerializedName("forceUpdateMessage")
        @Expose
        private String forceUpdateMessage;
        @SerializedName("appUrl")
        @Expose
        private String appUrl;
        @SerializedName("developmentTeamUrl")
        @Expose
        private String developmentTeamUrl;
        @SerializedName("contactSupportId")
        @Expose
        private String contactSupportId;
        @SerializedName("guestLogin")
        @Expose
        private boolean guestLogin;

        public int getAndroidMinimumVersion() {
            return androidMinimumVersion;
        }

        public void setAndroidMinimumVersion(int androidMinimumVersion) {
            this.androidMinimumVersion = androidMinimumVersion;
        }

        public int getAndroidLatestVersion() {
            return androidLatestVersion;
        }

        public void setAndroidLatestVersion(int androidLatestVersion) {
            this.androidLatestVersion = androidLatestVersion;
        }

        public String getDownloadAndroidUrl() {
            return downloadAndroidUrl;
        }

        public void setDownloadAndroidUrl(String downloadAndroidUrl) {
            this.downloadAndroidUrl = downloadAndroidUrl;
        }

        public int getIosMinimumVersion() {
            return iosMinimumVersion;
        }

        public void setIosMinimumVersion(int iosMinimumVersion) {
            this.iosMinimumVersion = iosMinimumVersion;
        }

        public int getIosLatestVersion() {
            return iosLatestVersion;
        }

        public void setIosLatestVersion(int iosLatestVersion) {
            this.iosLatestVersion = iosLatestVersion;
        }

        public String getDownloadIosUrl() {
            return downloadIosUrl;
        }

        public void setDownloadIosUrl(String downloadIosUrl) {
            this.downloadIosUrl = downloadIosUrl;
        }

        public String getUpdateMessage() {
            return updateMessage;
        }

        public void setUpdateMessage(String updateMessage) {
            this.updateMessage = updateMessage;
        }

        public String getForceUpdateMessage() {
            return forceUpdateMessage;
        }

        public void setForceUpdateMessage(String forceUpdateMessage) {
            this.forceUpdateMessage = forceUpdateMessage;
        }

        public String getAppUrl() {
            return appUrl;
        }

        public void setAppUrl(String appUrl) {
            this.appUrl = appUrl;
        }

        public String getDevelopmentTeamUrl() {
            return developmentTeamUrl;
        }

        public void setDevelopmentTeamUrl(String developmentTeamUrl) {
            this.developmentTeamUrl = developmentTeamUrl;
        }

        public String getContactSupportId() {
            return contactSupportId;
        }

        public void setContactSupportId(String contactSupportId) {
            this.contactSupportId = contactSupportId;
        }

        public boolean isGuestLogin() {
            return guestLogin;
        }

        public void setGuestLogin(boolean guestLogin) {
            this.guestLogin = guestLogin;
        }
    }
}