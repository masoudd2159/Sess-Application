package ir.ac.sku.www.sessapplication.activities;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.HashMap;
import java.util.Map;

import ir.ac.sku.www.sessapplication.R;
import ir.ac.sku.www.sessapplication.models.MSGMessageShow;
import ir.ac.sku.www.sessapplication.utils.Handler;
import ir.ac.sku.www.sessapplication.utils.MyActivity;

public class ShowMessageActivity extends MyActivity {

    private TextView type;
    private TextView subject;
    private TextView sender;
    private TextView date;
    private TextView target;
    private TextView text;
    private ImageView attachment;
    private MSGMessageShow messageShow;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_message);
        ShowMessageActivity.this.setTitle("پیام ها");
        progressDialog = new ProgressDialog(this);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        init();

        prepareData(getIntent().getStringExtra("ident"));
    }

    private void init() {
        type = findViewById(R.id.activityShowMessage_TextViewType);
        attachment = findViewById(R.id.activityShowMessage_ImageViewAttachment);
        subject = findViewById(R.id.activityShowMessage_TextViewSubject);
        sender = findViewById(R.id.activityShowMessage_TextViewSender);
        date = findViewById(R.id.activityShowMessage_TextViewDate);
        target = findViewById(R.id.activityShowMessage_TextViewTarget);
        text = findViewById(R.id.activityShowMessage_TextViewText);

        target.setMovementMethod(new ScrollingMovementMethod());
        text.setMovementMethod(new ScrollingMovementMethod());
    }

    private void prepareData(String ident) {
        progressDialog.setMessage("لطفا منتظر بمانید!");
        progressDialog.setCancelable(false);
        progressDialog.show();

        Map<String, String> params = new HashMap<>();
        params.put("ident", ident);

        MSGMessageShow.fetchFromWeb(ShowMessageActivity.this, (HashMap<String, String>) params, new Handler() {
            @Override
            public void onResponse(boolean ok, Object obj) {
                progressDialog.dismiss();
                if (ok) {
                    messageShow = (MSGMessageShow) obj;
                    prepareSlides(messageShow);
                }
            }
        });
    }

    @SuppressLint("SetTextI18n")
    private void prepareSlides(MSGMessageShow messageShow) {
        type.setText(messageShow.getResult().getPriority());
        subject.setText(messageShow.getResult().getSubject());
        sender.setText(messageShow.getResult().getSender());
        date.setText(messageShow.getResult().getDate() + " - " + messageShow.getResult().getTime());
        target.setText(messageShow.getResult().getTarget());
        text.setText(messageShow.getResult().getText());

        if (messageShow.getResult().getAttachment()) {
            attachment.setVisibility(View.VISIBLE);
        } else if (!messageShow.getResult().getAttachment()) {
            attachment.setVisibility(View.INVISIBLE);
        }

        if (messageShow.getResult().getPriority().equals("عادي")) {
            type.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_circle_normal, 0);
        }
        if (messageShow.getResult().getPriority().equals("ضروري")) {
            type.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_circle_essential, 0);
        }
        if (messageShow.getResult().getPriority().equals("مهم")) {
            type.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_circle_important, 0);
        }
        if (messageShow.getResult().getPriority().equals("خيلي مهم")) {
            type.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_circle_very_important, 0);
        }
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
