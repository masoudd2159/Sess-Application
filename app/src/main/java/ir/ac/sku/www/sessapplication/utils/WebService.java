package ir.ac.sku.www.sessapplication.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.widget.Toast;

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

import java.io.UnsupportedEncodingException;

import ir.ac.sku.www.sessapplication.API.PreferenceName;
import ir.ac.sku.www.sessapplication.R;
import ir.ac.sku.www.sessapplication.models.IsOk;

public class WebService {
    private Context context;
    private RequestQueue queue;
    private Gson gson;
    private SharedPreferences preferencesCookie;
    private IsOk isOk;

    public WebService(Context context) {
        this.context = context;

        preferencesCookie = context.getSharedPreferences(PreferenceName.COOKIE_PREFERENCE_NAME, Context.MODE_PRIVATE);
        queue = Volley.newRequestQueue(context);
        gson = new Gson();
        isOk = new IsOk();
    }

    public void request(final String url, final int method, final Handler handler) {
        String URI = url + "&cookie=" + preferencesCookie.getString(PreferenceName.COOKIE_PREFERENCE_COOKIE, "NULL");
        StringRequest stringRequest = new StringRequest(method,
                URI,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            isOk = gson.fromJson(new String(response.getBytes("ISO-8859-1"), "UTF-8"), IsOk.class);
                            if (isOk.isOk()) {
                                handler.onResponse(true, response);
                            } else if (!isOk.isOk()) {
                                if (Integer.parseInt(isOk.getDescription().getErrorCode()) > 0) {
                                    HttpManager.unsuccessfulOperation(context, isOk.getDescription().getErrorText());
                                } else if (Integer.parseInt(isOk.getDescription().getErrorCode()) < 0) {
                                    SignIn signIn = new SignIn(context);
                                    signIn.SignInDialog(new Handler() {
                                        @Override
                                        public void onResponse(boolean ok, Object obj) {
                                            if (ok) {
                                                request(url, method, new Handler() {
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
                        } catch (UnsupportedEncodingException e) {
                            e.printStackTrace();
                            handler.onResponse(false, null);
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                handler.onResponse(false, null);

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
        });
        queue.add(stringRequest);
    }
}
