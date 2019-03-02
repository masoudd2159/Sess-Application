package ir.ac.sku.www.sessapplication.models;

import android.content.Context;

import com.android.volley.Request;
import com.google.gson.Gson;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;

import ir.ac.sku.www.sessapplication.API.MyConfig;
import ir.ac.sku.www.sessapplication.utils.Handler;
import ir.ac.sku.www.sessapplication.utils.HttpManager;
import ir.ac.sku.www.sessapplication.utils.WebService;

public class JournalsModel {

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

        private Integer id;
        private String title;
        private Integer version;
        private String file;
        private String picture;
        private String createdAt;
        private String updatedAt;


        public Integer getId() {
            return id;
        }

        public void setId(Integer id) {
            this.id = id;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public Integer getVersion() {
            return version;
        }

        public void setVersion(Integer version) {
            this.version = version;
        }

        public String getFile() {
            return file;
        }

        public void setFile(String file) {
            this.file = file;
        }

        public String getPicture() {
            return picture;
        }

        public void setPicture(String picture) {
            this.picture = picture;
        }

        public String getCreatedAt() {
            return createdAt;
        }

        public void setCreatedAt(String createdAt) {
            this.createdAt = createdAt;
        }

        public String getUpdatedAt() {
            return updatedAt;
        }

        public void setUpdatedAt(String updatedAt) {
            this.updatedAt = updatedAt;
        }
    }


    public static void fetchFromWeb(Context context, HashMap<String, String> params, final Handler handler) {
        final Gson gson = new Gson();

        WebService webService = new WebService(context);
        String myURL = MyConfig.STUDENT_JOURNALS + "?" + HttpManager.enCodeParameters(params);
        webService.request(myURL, Request.Method.GET, new Handler() {
            @Override
            public void onResponse(boolean ok, Object obj) {
                if (ok) {
                    JournalsModel journalsModel = gson.fromJson(new String(obj.toString().getBytes(StandardCharsets.ISO_8859_1), StandardCharsets.UTF_8), JournalsModel.class);
                    if (journalsModel.getOk()) {
                        handler.onResponse(true, journalsModel);
                    }
                }
            }
        });
    }
}
