package ir.ac.sku.www.sessapplication.model;

import android.content.Context;
import android.os.Build;

import androidx.annotation.RequiresApi;

import com.android.volley.Request;
import com.google.gson.Gson;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.List;

import ir.ac.sku.www.sessapplication.api.ApplicationAPI;
import ir.ac.sku.www.sessapplication.utils.helper.ManagerHelper;
import ir.ac.sku.www.sessapplication.utils.MyHandler;
import ir.ac.sku.www.sessapplication.utils.WebService;

public class CompetitionsModel {
    private Boolean ok;
    private List<Result> result = null;

    public Boolean getOk() {
        return ok;
    }

    public void setOk(Boolean ok) {
        this.ok = ok;
    }

    public List<Result> getResult() {
        return result;
    }

    public void setResult(List<Result> result) {
        this.result = result;
    }

    public class Result {
        private String title;
        private String description;
        private Boolean userRegistered;
        private Boolean canRegister;
        private String url;
        private String picture;
        private List<File> files = null;


        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public Boolean getUserRegistered() {
            return userRegistered;
        }

        public void setUserRegistered(Boolean userRegistered) {
            this.userRegistered = userRegistered;
        }

        public Boolean getCanRegister() {
            return canRegister;
        }

        public void setCanRegister(Boolean canRegister) {
            this.canRegister = canRegister;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public String getPicture() {
            return picture;
        }

        public void setPicture(String picture) {
            this.picture = picture;
        }

        public List<File> getFiles() {
            return files;
        }

        public void setFiles(List<File> files) {
            this.files = files;
        }

        public class File {
            private String link;
            private String description;

            public String getLink() {
                return link;
            }

            public void setLink(String link) {
                this.link = link;
            }

            public String getDescription() {
                return description;
            }

            public void setDescription(String description) {
                this.description = description;
            }
        }
    }

    public static void fetchFromWeb(Context context, HashMap<String, String> params, final MyHandler handler) {
        final Gson gson = new Gson();

        WebService webService = new WebService(context);
        String myURL = ApplicationAPI.STUDENT_COMPETITIONS + "?" + ManagerHelper.enCodeParameters(params);
        webService.request(myURL, Request.Method.GET, new MyHandler() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onResponse(boolean ok, Object obj) {
                if (ok) {
                    CompetitionsModel competitionsModel = null;
                    try {
                        competitionsModel = gson.fromJson(new String(obj.toString().getBytes("ISO-8859-1"), "UTF-8"), CompetitionsModel.class);
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }
                    if (competitionsModel.getOk()) {
                        handler.onResponse(true, competitionsModel);
                    }
                }
            }
        });
    }
}
