package ir.ac.sku.www.sessapplication.model;

import android.content.Context;
import android.os.Build;
import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.RequiresApi;

import com.android.volley.Request;
import com.google.gson.Gson;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;

import ir.ac.sku.www.sessapplication.api.MyConfig;
import ir.ac.sku.www.sessapplication.utils.HttpManager;
import ir.ac.sku.www.sessapplication.utils.MyHandler;
import ir.ac.sku.www.sessapplication.utils.WebService;

public class AppInfo implements Parcelable {
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

    public static class Result implements Parcelable {
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
        private Boolean guestLogin;

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

        public Boolean getGuestLogin() {
            return guestLogin;
        }

        public void setGuestLogin(Boolean guestLogin) {
            this.guestLogin = guestLogin;
        }


        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeInt(this.androidMinimumVersion);
            dest.writeInt(this.androidLatestVersion);
            dest.writeString(this.downloadAndroidUrl);
            dest.writeInt(this.iosMinimumVersion);
            dest.writeInt(this.iosLatestVersion);
            dest.writeString(this.downloadIosUrl);
            dest.writeString(this.updateMessage);
            dest.writeString(this.forceUpdateMessage);
            dest.writeString(this.appUrl);
            dest.writeString(this.developmentTeamUrl);
            dest.writeString(this.contactSupportId);
            dest.writeValue(this.guestLogin);
        }

        public Result() {
        }

        protected Result(Parcel in) {
            this.androidMinimumVersion = in.readInt();
            this.androidLatestVersion = in.readInt();
            this.downloadAndroidUrl = in.readString();
            this.iosMinimumVersion = in.readInt();
            this.iosLatestVersion = in.readInt();
            this.downloadIosUrl = in.readString();
            this.updateMessage = in.readString();
            this.forceUpdateMessage = in.readString();
            this.appUrl = in.readString();
            this.developmentTeamUrl = in.readString();
            this.contactSupportId = in.readString();
            this.guestLogin = (Boolean) in.readValue(Boolean.class.getClassLoader());
        }

        public static final Creator<Result> CREATOR = new Creator<Result>() {
            @Override
            public Result createFromParcel(Parcel source) {
                return new Result(source);
            }

            @Override
            public Result[] newArray(int size) {
                return new Result[size];
            }
        };
    }

    public static void fetchFromWeb(Context context, HashMap<String, String> params, final MyHandler handler) {
        final Gson gson = new Gson();

        WebService webService = new WebService(context);
        String myURL = MyConfig.APP_INFO + "?" + HttpManager.enCodeParameters(params);
        webService.request(myURL, Request.Method.GET, new MyHandler() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onResponse(boolean ok, Object obj) {
                if (ok) {
                    AppInfo appInfo = null;
                    try {
                        appInfo = gson.fromJson(new String(obj.toString().getBytes("ISO-8859-1"), "UTF-8"), AppInfo.class);
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }
                    if (appInfo.getOk()) {
                        handler.onResponse(true, appInfo);
                    }
                }
            }
        });
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(this.ok);
        dest.writeParcelable(this.result, flags);
    }

    public AppInfo() {
    }

    protected AppInfo(Parcel in) {
        this.ok = (Boolean) in.readValue(Boolean.class.getClassLoader());
        this.result = in.readParcelable(Result.class.getClassLoader());
    }

    public static final Creator<AppInfo> CREATOR = new Creator<AppInfo>() {
        @Override
        public AppInfo createFromParcel(Parcel source) {
            return new AppInfo(source);
        }

        @Override
        public AppInfo[] newArray(int size) {
            return new AppInfo[size];
        }
    };
}
