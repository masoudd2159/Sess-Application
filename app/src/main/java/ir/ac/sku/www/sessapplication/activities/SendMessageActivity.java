package ir.ac.sku.www.sessapplication.activities;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;

import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import ir.ac.sku.www.sessapplication.API.MyConfig;
import ir.ac.sku.www.sessapplication.API.PreferenceName;
import ir.ac.sku.www.sessapplication.R;
import ir.ac.sku.www.sessapplication.models.GetInfoForSend;
import ir.ac.sku.www.sessapplication.models.IsOk;
import ir.ac.sku.www.sessapplication.models.MSGSendMessage;
import ir.ac.sku.www.sessapplication.utils.Handler;
import ir.ac.sku.www.sessapplication.utils.HttpManager;
import ir.ac.sku.www.sessapplication.utils.MyActivity;
import ir.ac.sku.www.sessapplication.utils.SignIn;

public class SendMessageActivity extends MyActivity {

    private TextView date;
    private TextView name;
    private TextView position;
    private EditText title;
    private EditText text;
    private NumberPicker sendType;
    private NumberPicker messageType;

    private RequestQueue queue;
    private Gson gson;
    private SharedPreferences preferencesCookie;
    private IsOk isOk;
    private GetInfoForSend getInfoForSend;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_message);

        SendMessageActivity.this.setTitle("پیام جدید");

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        getInfoForSend = new GetInfoForSend();
        getInfoForSend = getIntent().getParcelableExtra("GetInfoForSend");

        init();
        preferencesCookie = getSharedPreferences(PreferenceName.COOKIE_PREFERENCE_NAME, Context.MODE_PRIVATE);
        queue = Volley.newRequestQueue(SendMessageActivity.this);
        gson = new Gson();
        isOk = new IsOk();

        setItems();
    }

    private void setItems() {

        date.setText(getInfoForSend.getResult().getDate());

        if (getInfoForSend.getResult().getTarget().get(0) != null) {
            name.setText(getInfoForSend.getResult().getTarget().get(0).getName());
            position.setText(getInfoForSend.getResult().getTarget().get(0).getPosition());
        }

        String[] valueType = new String[getInfoForSend.getResult().getTypes().size()];
        for (int i = 0; i < getInfoForSend.getResult().getTypes().size(); i++) {
            valueType[i] = getInfoForSend.getResult().getTypes().get(i).getName();
        }

        sendType.setMinValue(0);
        sendType.setMaxValue(getInfoForSend.getResult().getTypes().size() - 1);
        sendType.setDisplayedValues(valueType);

        String[] valuePriorities = new String[getInfoForSend.getResult().getPriorities().size()];
        for (int i = 0; i < getInfoForSend.getResult().getPriorities().size(); i++) {
            valuePriorities[i] = getInfoForSend.getResult().getPriorities().get(i).getName();
        }

        messageType.setMinValue(0);
        messageType.setMaxValue(getInfoForSend.getResult().getPriorities().size() - 1);
        messageType.setDisplayedValues(valuePriorities);
    }

    private void init() {
        date = findViewById(R.id.sendMessageActivity_TextViewDate);
        name = findViewById(R.id.sendMessageActivity_TextViewName);
        position = findViewById(R.id.sendMessageActivity_TextViewPosition);
        sendType = findViewById(R.id.sendMessageActivity_PickerSendType);
        messageType = findViewById(R.id.sendMessageActivity_PickerMessageType);
        title = findViewById(R.id.sendMessageActivity_EditTextTitle);
        text = findViewById(R.id.sendMessageActivity_EditText_Text);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuItem menuItem = menu.add("ارسال");
        menuItem.setShowAsAction(menuItem.SHOW_AS_ACTION_ALWAYS);
        menuItem.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                sendParamsPost();
                return false;
            }
        });

        return super.onCreateOptionsMenu(menu);
    }

    private void sendParamsPost() {
        if (HttpManager.isNOTOnline(SendMessageActivity.this)) {
            HttpManager.noInternetAccess(SendMessageActivity.this);
        } else {

            StringRequest stringRequest = new StringRequest(Request.Method.POST,
                    MyConfig.MSG_SEND_MESSAGES,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            isOk = gson.fromJson(new String(response.getBytes(StandardCharsets.ISO_8859_1), StandardCharsets.UTF_8), IsOk.class);
                            if (isOk.isOk()) {
                                MSGSendMessage message = new MSGSendMessage();
                                title.setText("");
                                text.setText("");
                                HttpManager.successfulOperation(SendMessageActivity.this, message.getResult());
                            } else if (!isOk.isOk()) {
                                if (Integer.parseInt(isOk.getDescription().getErrorCode()) > 0) {
                                    HttpManager.unsuccessfulOperation(SendMessageActivity.this, isOk.getDescription().getErrorText());
                                } else if (Integer.parseInt(isOk.getDescription().getErrorCode()) < 0) {
                                    SignIn signIn = new SignIn(SendMessageActivity.this);
                                    signIn.SignInDialog(new Handler() {
                                        @Override
                                        public void onResponse(boolean ok, Object obj) {
                                            if (ok) {

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
                            new AlertDialog.Builder(SendMessageActivity.this)
                                    .setTitle("ERROR")
                                    .setMessage(error.getMessage())
                                    .setPositiveButton("Ok", null)
                                    .setIcon(android.R.drawable.ic_dialog_alert)
                                    .show();
                        }
                    }
            ) {
                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> params = new HashMap<String, String>();
                    params.put("cookie", Objects.requireNonNull(preferencesCookie.getString(PreferenceName.COOKIE_PREFERENCE_COOKIE, "NULL")));
                    params.put("id", getIntent().getStringExtra("id"));
                    params.put("stNumber", getIntent().getStringExtra("studentNumber"));
                    params.put("subject", title.getText().toString().trim());
                    params.put("text", text.getText().toString().trim());
                    params.put("type", getInfoForSend.getResult().getTypes().get(sendType.getValue()).getValue());
                    params.put("priority", getInfoForSend.getResult().getPriorities().get(messageType.getValue()).getValue());
                    return params;
                }
            };
            queue.add(stringRequest);
        }
    }
}
