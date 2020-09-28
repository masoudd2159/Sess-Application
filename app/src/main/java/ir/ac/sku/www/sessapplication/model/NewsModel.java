package ir.ac.sku.www.sessapplication.model;

import android.annotation.SuppressLint;
import android.content.Context;

import com.android.volley.Request;
import com.google.gson.Gson;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.List;

import ir.ac.sku.www.sessapplication.api.MyConfig;
import ir.ac.sku.www.sessapplication.utils.MyHandler;
import ir.ac.sku.www.sessapplication.utils.HttpManager;
import ir.ac.sku.www.sessapplication.utils.WebService;

public class NewsModel {

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
        private List<Pin> pins = null;
        private List<News> news = null;

        public List<Pin> getPins() {
            return pins;
        }

        public void setPins(List<Pin> pins) {
            this.pins = pins;
        }

        public List<News> getNews() {
            return news;
        }

        public void setNews(List<News> news) {
            this.news = news;
        }

        public class Pin {
            private String title;
            private String text;
            private String ident;
            private String link;
            private String image;

            public String getTitle() {
                return title;
            }

            public void setTitle(String title) {
                this.title = title;
            }

            public String getText() {
                return text;
            }

            public void setText(String text) {
                this.text = text;
            }

            public String getIdent() {
                return ident;
            }

            public void setIdent(String ident) {
                this.ident = ident;
            }

            public String getLink() {
                return link;
            }

            public void setLink(String link) {
                this.link = link;
            }

            public String getImage() {
                return image;
            }

            public void setImage(String image) {
                this.image = image;
            }
        }

        public class News {

            private String title;
            private String text;
            private String ident;
            private String link;
            private String image;

            public String getTitle() {
                return title;
            }

            public void setTitle(String title) {
                this.title = title;
            }

            public String getText() {
                return text;
            }

            public void setText(String text) {
                this.text = text;
            }

            public String getIdent() {
                return ident;
            }

            public void setIdent(String ident) {
                this.ident = ident;
            }

            public String getLink() {
                return link;
            }

            public void setLink(String link) {
                this.link = link;
            }

            public String getImage() {
                return image;
            }

            public void setImage(String image) {
                this.image = image;
            }
        }
    }

    public static void fetchFromWeb(Context context, HashMap<String, String> params, final MyHandler handler) {
        final Gson gson = new Gson();

        WebService webService = new WebService(context);
        String myURL = MyConfig.STUDENT_NEWS + "?" + HttpManager.enCodeParameters(params);
        webService.request(myURL, Request.Method.GET, new MyHandler() {
            @Override
            public void onResponse(boolean ok, Object obj) {
                if (ok) {
                    @SuppressLint({"NewApi", "LocalSuppress"}) NewsModel newsModel = null;
                    try {
                        newsModel = gson.fromJson(new String(obj.toString().getBytes("ISO-8859-1"), "UTF-8"), NewsModel.class);
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }
                    if (newsModel.getOk()) {
                        handler.onResponse(true, newsModel);
                    }
                }
            }
        });
    }
}
