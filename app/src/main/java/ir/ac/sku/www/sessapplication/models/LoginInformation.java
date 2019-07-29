package ir.ac.sku.www.sessapplication.models;

import android.content.Context;

import com.android.volley.Request;
import com.google.gson.Gson;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;

import ir.ac.sku.www.sessapplication.API.MyConfig;
import ir.ac.sku.www.sessapplication.utils.MyHandler;
import ir.ac.sku.www.sessapplication.utils.HttpManager;
import ir.ac.sku.www.sessapplication.utils.WebService;

public class LoginInformation {
    private boolean ok;
    private String cookie;

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

    public static void fetchFromWeb(Context context, HashMap<String, String> params, final MyHandler handler) {
        final Gson gson = new Gson();

        WebService webService = new WebService(context);
        String myURL = MyConfig.LOGIN_INFORMATION + "?" + HttpManager.enCodeParameters(params);
        webService.request(myURL, Request.Method.GET, new MyHandler() {
            @Override
            public void onResponse(boolean ok, Object obj) {
                if (ok) {
                    try {
                        LoginInformation loginInformation = gson.fromJson(new String(obj.toString().getBytes("ISO-8859-1"), "UTF-8"), LoginInformation.class);
                        if (loginInformation.isOk()) {
                            handler.onResponse(true, loginInformation);
                        }
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                        handler.onResponse(false, null);
                    }
                } else {
                    handler.onResponse(false, null);
                }
            }
        });
    }
}
