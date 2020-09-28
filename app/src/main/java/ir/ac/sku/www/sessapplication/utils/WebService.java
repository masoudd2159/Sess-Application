package ir.ac.sku.www.sessapplication.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

import ir.ac.sku.www.sessapplication.api.MyLog;
import ir.ac.sku.www.sessapplication.api.PreferenceName;
import ir.ac.sku.www.sessapplication.model.IsOk;

public class WebService {
    private Context context;
    private RequestQueue queue;
    private Gson gson;
    private SharedPreferences preferencesUserInformation;
    private IsOk isOk;


    @SuppressLint("LongLogTag")
    public WebService(Context context) {
        Log.i(MyLog.WEB_SERVICE, "Constructor");

        this.context = context;
        Log.i(MyLog.WEB_SERVICE, "Context : " + String.valueOf(context));

        preferencesUserInformation = context.getSharedPreferences(PreferenceName.PREFERENCE_USER_INFORMATION, Context.MODE_PRIVATE);
        queue = Volley.newRequestQueue(context);
        gson = new Gson();
        isOk = new IsOk();
    }


    @SuppressLint("LongLogTag")
    public void request(final String url, final int method, final MyHandler handler) {

        Log.i(MyLog.WEB_SERVICE, "Request");

        Log.i(MyLog.WEB_SERVICE, "Handler : ‌" + String.valueOf(handler));

        String myURL = url + "&cookie=" + preferencesUserInformation.getString(PreferenceName.PREFERENCE_COOKIE, "NULL");
        Log.i(MyLog.WEB_SERVICE, "URL : " + myURL);

        StringRequest stringRequest = new StringRequest(method,
                myURL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        isOk = gson.fromJson(new String(response.getBytes(StandardCharsets.ISO_8859_1), StandardCharsets.UTF_8), IsOk.class);
                        Log.i(MyLog.WEB_SERVICE, "OK : " + isOk.isOk());

                        if (isOk.isOk()) {
                            handler.onResponse(true, response);
                        } else if (!isOk.isOk()) {

                            Log.i(MyLog.WEB_SERVICE, "Error Code : " + isOk.getDescription().getErrorCode());
                            Log.i(MyLog.WEB_SERVICE, "Error Text : " + isOk.getDescription().getErrorText());

                            if (Integer.parseInt(isOk.getDescription().getErrorCode()) > 0) {
                                HttpManager.unsuccessfulOperation(context, isOk.getDescription().getErrorText());
                            } else if (Integer.parseInt(isOk.getDescription().getErrorCode()) < 0) {
                                SignIn signIn = new SignIn(context);
                                signIn.SignInDialog(new MyHandler() {
                                    @Override
                                    public void onResponse(boolean ok, Object obj) {
                                        if (ok) {
                                            request(url, method, new MyHandler() {
                                                @Override
                                                public void onResponse(boolean ok, Object obj) {
                                                    handler.onResponse(ok, obj);
                                                }
                                            });
                                        }
                                    }
                                });
                            }
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.i(MyLog.WEB_SERVICE, "VolleyError : " + error.getMessage());

                        request(url, method, handler);

                        /*if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                            HttpManager.unsuccessfulOperation(context, "اینترنت شما ضعیف است!");
                        } else if (error instanceof AuthFailureError) {
                            HttpManager.unsuccessfulOperation(context, "AuthFailureError");
                        } else if (error instanceof ServerError) {
                            HttpManager.unsuccessfulOperation(context, "سرور در حال حاظر از دسترس خارج است!");
                        } else if (error instanceof NetworkError) {
                            HttpManager.unsuccessfulOperation(context, "NetworkError");
                        } else if (error instanceof ParseError) {
                            HttpManager.unsuccessfulOperation(context, "ParseError");
                        }*/
                    }
                });

        queue.add(stringRequest);
    }

    @SuppressLint("LongLogTag")
    public void requestPost(final String url, final int method, final HashMap<String, String> params, final MyHandler handler) {
        Log.i(MyLog.WEB_SERVICE, "Request Post");
        Log.i(MyLog.WEB_SERVICE, "Handler : ‌" + String.valueOf(handler));
        Log.i(MyLog.WEB_SERVICE, "URL : " + url);
        Log.i(MyLog.WEB_SERVICE, "Params : " + params);


        StringRequest stringRequest = new StringRequest(method,
                url,
                new Response.Listener<String>() {
                    @SuppressLint("NewApi")
                    @Override
                    public void onResponse(String response) {
                        isOk = gson.fromJson(new String(response.getBytes(StandardCharsets.ISO_8859_1), StandardCharsets.UTF_8), IsOk.class);
                        Log.i(MyLog.WEB_SERVICE, "OK : " + isOk.isOk());

                        if (isOk.isOk()) {
                            handler.onResponse(true, response);
                        } else if (!isOk.isOk()) {

                            Log.i(MyLog.WEB_SERVICE, "Error Code : " + isOk.getDescription().getErrorCode());
                            Log.i(MyLog.WEB_SERVICE, "Error Text : " + isOk.getDescription().getErrorText());

                            if (Integer.parseInt(isOk.getDescription().getErrorCode()) > 0) {
                                handler.onResponse(true, response);
                            } else if (Integer.parseInt(isOk.getDescription().getErrorCode()) < 0) {
                                SignIn signIn = new SignIn(context);
                                signIn.SignInDialog(new MyHandler() {
                                    @Override
                                    public void onResponse(boolean ok, Object obj) {
                                        if (ok) {
                                            requestPost(url, method, params, new MyHandler() {
                                                @Override
                                                public void onResponse(boolean ok, Object obj) {
                                                    handler.onResponse(ok, obj);
                                                }
                                            });
                                        }
                                    }
                                });
                            }
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                        Log.i(MyLog.WEB_SERVICE, "VolleyError : " + error.getMessage());

                        //  handler.onResponse(false, null);

                        if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                            HttpManager.unsuccessfulOperation(context, "اینترنت شما ضعیف است!");
                        } else if (error instanceof AuthFailureError) {
                            HttpManager.unsuccessfulOperation(context, "AuthFailureError");
                        } else if (error instanceof ServerError) {
                            HttpManager.unsuccessfulOperation(context, "سرور در حال حاظر از دسترس خارج است!");
                        } else if (error instanceof NetworkError) {
                            HttpManager.unsuccessfulOperation(context, "NetworkError");
                        } else if (error instanceof ParseError) {
                            HttpManager.unsuccessfulOperation(context, "ParseError");
                        }

                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() {
                return params;
            }
        };
        queue.add(stringRequest);
    }
}
