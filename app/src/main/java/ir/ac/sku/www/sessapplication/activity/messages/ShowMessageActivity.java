package ir.ac.sku.www.sessapplication.activity.messages;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;

import com.uncopt.android.widget.text.justify.JustifiedTextView;
import java.util.HashMap;
import ir.ac.sku.www.sessapplication.R;
import ir.ac.sku.www.sessapplication.model.MSGMessageShow;
import ir.ac.sku.www.sessapplication.utils.MyHandler;
import ir.ac.sku.www.sessapplication.utils.MyActivity;

public class ShowMessageActivity extends MyActivity {

    private TextView type;
    private TextView subject;
    private TextView sender;
    private TextView date;
    private TextView read;
    private CardView cardViewType;
    private JustifiedTextView text;
    private JustifiedTextView target;
    private LinearLayout layoutRead;
    private ImageView attachment;
    private MSGMessageShow messageShow;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_message);

        TextView title = findViewById(R.id.activityShowMessage_ToolbarTitle);
        Toolbar toolbar = (Toolbar) findViewById(R.id.activityShowMessage_ToolBar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        toolbar.setTitle("");
        toolbar.setSubtitle("");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        title.setTypeface(Typeface.createFromAsset(getAssets(), "fonts/Lalezar.ttf"));


        progressDialog = new ProgressDialog(this);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        init();

        prepareData(getIntent().getStringExtra("ident"), getIntent().getStringExtra("type"));
    }

    private void init() {
        type = findViewById(R.id.activityShowMessage_TextViewType);
        attachment = findViewById(R.id.activityShowMessage_ImageViewAttachment);
        subject = findViewById(R.id.activityShowMessage_TextViewSubject);
        sender = findViewById(R.id.activityShowMessage_TextViewSender);
        date = findViewById(R.id.activityShowMessage_TextViewDate);
        target = findViewById(R.id.activityShowMessage_TextViewTarget);
        //text = findViewById(R.id.activityShowMessage_TextViewText);
        text = findViewById(R.id.activityShowMessage_TextViewText);
        read = findViewById(R.id.activityShowMessage_TextViewRead);
        layoutRead = findViewById(R.id.activityShowMessage_LayoutRead);
        cardViewType = findViewById(R.id.activityShowMessage_CardViewType);

        target.setMovementMethod(new ScrollingMovementMethod());
    }

    private void prepareData(String ident, String type) {
        progressDialog.setMessage("لطفا منتظر بمانید!");
        progressDialog.setCancelable(false);
        progressDialog.show();

        HashMap<String, String> params = new HashMap<>();
        params.put("ident", ident);
        params.put("type", type);

        MSGMessageShow.fetchFromWeb(ShowMessageActivity.this, params, new MyHandler() {
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

    @SuppressLint({"SetTextI18n", "NewApi"})
    private void prepareSlides(MSGMessageShow messageShow) {
        type.setText(messageShow.getResult().getPriority());
        subject.setText(messageShow.getResult().getSubject());
        sender.setText(messageShow.getResult().getSender());
        date.setText(messageShow.getResult().getDate() + " - " + messageShow.getResult().getTime());
        text.setText(messageShow.getResult().getText());

        if (getIntent().getStringExtra("type").equals("send")) {
            target.setText(messageShow.getResult().getReceiver());
            layoutRead.setVisibility(View.VISIBLE);
            if (messageShow.getResult().getRead()) {
                read.setText("خوانده شده");
            } else {
                read.setText("خوانده نشده");
                read.setTextColor(Color.RED);
            }

        } else {
            target.setText(messageShow.getResult().getTarget());
        }

        if (messageShow.getResult().getAttachment()) {
            attachment.setVisibility(View.VISIBLE);
        } else if (!messageShow.getResult().getAttachment()) {
            attachment.setVisibility(View.INVISIBLE);
        }

        if (messageShow.getResult().getPriority().equals("عادي")) {
            cardViewType.setCardBackgroundColor(0xFFA6A6A6);
        }
        if (messageShow.getResult().getPriority().equals("ضروري")) {
            cardViewType.setCardBackgroundColor(0xFF990000);
        }
        if (messageShow.getResult().getPriority().equals("مهم")) {
            cardViewType.setCardBackgroundColor(0xFF05813B);
        }
        if (messageShow.getResult().getPriority().equals("خيلي مهم")) {
            cardViewType.setCardBackgroundColor(0xFF001C56);
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
