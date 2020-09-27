package ir.ac.sku.www.sessapplication.activities.messages;

import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.Toolbar;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.snackbar.Snackbar;
import com.google.gson.Gson;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

import ir.ac.sku.www.sessapplication.API.MyConfig;
import ir.ac.sku.www.sessapplication.API.PreferenceName;
import ir.ac.sku.www.sessapplication.R;
import ir.ac.sku.www.sessapplication.adapters.TargetsAdapter;
import ir.ac.sku.www.sessapplication.models.GetInfoForSend;
import ir.ac.sku.www.sessapplication.models.MSGSendMessage;
import ir.ac.sku.www.sessapplication.utils.MyHandler;
import ir.ac.sku.www.sessapplication.utils.HttpManager;
import ir.ac.sku.www.sessapplication.utils.MyActivity;
import ir.ac.sku.www.sessapplication.utils.SignIn;

public class SendMessageActivity extends MyActivity {

    private TextView date;
    private EditText title;
    private EditText text;
    private NumberPicker sendType;
    private NumberPicker messageType;
    private RecyclerView targets;

    private RequestQueue queue;
    private Gson gson;
    private SharedPreferences preferencesUserInformation;
    private MSGSendMessage message;
    private GetInfoForSend getInfoForSend;
    private TargetsAdapter adapter;
    private CoordinatorLayout coordinatorLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_message);

        TextView title = findViewById(R.id.sendMessageActivity_ToolbarTitle);
        Toolbar toolbar = (Toolbar) findViewById(R.id.sendMessageActivity_ToolBar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        toolbar.setTitle("");
        toolbar.setSubtitle("");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        title.setTypeface(Typeface.createFromAsset(getAssets(), "fonts/Lalezar.ttf"));

        getInfoForSend = new GetInfoForSend();
        getInfoForSend = getIntent().getParcelableExtra("GetInfoForSend");

        init();
        preferencesUserInformation = getSharedPreferences(PreferenceName.PREFERENCE_USER_INFORMATION, MODE_PRIVATE);
        queue = Volley.newRequestQueue(SendMessageActivity.this);
        gson = new Gson();
        message = new MSGSendMessage();

        setItems();
    }

    private void init() {
        date = findViewById(R.id.sendMessageActivity_TextViewDate);
        targets = findViewById(R.id.sendMessageActivity_RecyclerViewTargets);
        sendType = findViewById(R.id.sendMessageActivity_PickerSendType);
        messageType = findViewById(R.id.sendMessageActivity_PickerMessageType);
        title = findViewById(R.id.sendMessageActivity_EditTextTitle);
        text = findViewById(R.id.sendMessageActivity_EditText_Text);
        coordinatorLayout = findViewById(R.id.sendMessageActivity_CoordinatorLayout);
    }

    private void setItems() {

        date.setText(getInfoForSend.getResult().getDate());

        showDataOfTargets(getInfoForSend);

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

    private void showDataOfTargets(GetInfoForSend getInfoForSend) {
        adapter = new TargetsAdapter(SendMessageActivity.this, getInfoForSend);
        int resId = R.anim.layout_animation_from_right;
        LayoutAnimationController animation = AnimationUtils.loadLayoutAnimation(SendMessageActivity.this, resId);
        targets.setLayoutAnimation(animation);
        targets.setLayoutManager(new LinearLayoutManager(SendMessageActivity.this, LinearLayoutManager.HORIZONTAL, true));
        targets.setAdapter(adapter);
    }

    private void sendParamsPost() {
        if (HttpManager.isNOTOnline(SendMessageActivity.this)) {
            HttpManager.noInternetAccess(SendMessageActivity.this);
        } else {

            StringRequest stringRequest = new StringRequest(Request.Method.POST,
                    MyConfig.MSG_SEND_MESSAGES,
                    new Response.Listener<String>() {
                        @RequiresApi(api = Build.VERSION_CODES.KITKAT)
                        @Override
                        public void onResponse(String response) {
                            message = gson.fromJson(new String(response.getBytes(StandardCharsets.ISO_8859_1), StandardCharsets.UTF_8), MSGSendMessage.class);
                            if (message.getOk()) {
                                HttpManager.successfulOperation(SendMessageActivity.this, message.getResult());
                                title.setText("");
                                text.setText("");
                            } else if (!message.getOk()) {
                                if (Integer.parseInt(message.getDescription().getErrorCode()) > 0) {
                                    HttpManager.unsuccessfulOperation(SendMessageActivity.this, message.getDescription().getErrorText());
                                } else if (Integer.parseInt(message.getDescription().getErrorCode()) < 0) {
                                    SignIn signIn = new SignIn(SendMessageActivity.this);
                                    signIn.SignInDialog(new MyHandler() {
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
                    params.put("cookie", preferencesUserInformation.getString(PreferenceName.PREFERENCE_COOKIE, null));
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuItem menuItem = menu.add("ارسال");
        menuItem.setShowAsAction(menuItem.SHOW_AS_ACTION_ALWAYS);
        menuItem.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                if (text.getText().toString().trim().equals(""))
                    Snackbar.make(coordinatorLayout, "لطفا پیام خود را بنویسید", Snackbar.LENGTH_SHORT).show();
                else
                    sendParamsPost();
                return false;
            }
        });

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}
