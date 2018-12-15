package ir.ac.sku.www.sessapplication.models;

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
        private String androidMinimumVersion;
        private String androidLatestVersion;
        private String iosMinimumVersion;
        private String iosLatestVersion;
        private String updateMessage;
        private String forceUpdateMessage;
        private String appUrl;
        private String developmentTeamUrl;
        private String contactSupportId;

        public String getAndroidMinimumVersion() {
            return androidMinimumVersion;
        }

        public void setAndroidMinimumVersion(String androidMinimumVersion) {
            this.androidMinimumVersion = androidMinimumVersion;
        }

        public String getAndroidLatestVersion() {
            return androidLatestVersion;
        }

        public void setAndroidLatestVersion(String androidLatestVersion) {
            this.androidLatestVersion = androidLatestVersion;
        }

        public String getIosMinimumVersion() {
            return iosMinimumVersion;
        }

        public void setIosMinimumVersion(String iosMinimumVersion) {
            this.iosMinimumVersion = iosMinimumVersion;
        }

        public String getIosLatestVersion() {
            return iosLatestVersion;
        }

        public void setIosLatestVersion(String iosLatestVersion) {
            this.iosLatestVersion = iosLatestVersion;
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
}
