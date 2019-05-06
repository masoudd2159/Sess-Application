package ir.ac.sku.www.sessapplication.models;

import android.content.Context;
import android.os.Build;
import android.support.annotation.RequiresApi;

import com.android.volley.Request;
import com.google.gson.Gson;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;

import ir.ac.sku.www.sessapplication.API.MyConfig;
import ir.ac.sku.www.sessapplication.utils.Handler;
import ir.ac.sku.www.sessapplication.utils.HttpManager;
import ir.ac.sku.www.sessapplication.utils.WebService;

public class AppInfo {
    private Boolean ok;
    private Result result;

    public Boolean getOk() {
        return ok;
    }

    public void setOk(Boolean ok) {
        this.ok = ok;
    }

    public Result getResult() {
        return result;
    }

    public void setResult(Result result) {
        this.result = result;
    }

    public class Result {
        private int androidMinimumVersion;
        private int androidLatestVersion;
        private String downloadAndroidUrl;
        private int iosMinimumVersion;
        private int iosLatestVersion;
        private String downloadIosUrl;
        private String updateMessage;
        private String forceUpdateMessage;
        private String appUrl;
        private String developmentTeamUrl;
        private String contactSupportId;

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
    }

    public static void fetchFromWeb(Context context, HashMap<String, String> params, final Handler handler) {
        final Gson gson = new Gson();

        WebService webService = new WebService(context);
        String myURL = MyConfig.APP_INFO + "?" + HttpManager.enCodeParameters(params);
        webService.request(myURL, Request.Method.GET, new Handler() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onResponse(boolean ok, Object obj) {
                if (ok) {
                    AppInfo appInfo = gson.fromJson(new String(obj.toString().getBytes(StandardCharsets.ISO_8859_1), StandardCharsets.UTF_8), AppInfo.class);
                    if (appInfo.getOk()) {
                        handler.onResponse(true, appInfo);
                    }
                }
            }
        });
    }
}
