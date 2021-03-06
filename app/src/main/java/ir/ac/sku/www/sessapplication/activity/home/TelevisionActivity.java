package ir.ac.sku.www.sessapplication.activity.home;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;

import ir.ac.sku.www.sessapplication.api.ApplicationAPI;
import ir.ac.sku.www.sessapplication.R;
import ir.ac.sku.www.sessapplication.utils.helper.ManagerHelper;
import ir.ac.sku.www.sessapplication.utils.MyActivity;

public class TelevisionActivity extends MyActivity {

    private CardView channel_1;
    private CardView channel_2;
    private CardView channel_3;
    private CardView channel_4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_television);

        TextView title = findViewById(R.id.televisionActivity_ToolbarTitle);
        Toolbar toolbar = (Toolbar) findViewById(R.id.televisionActivity_ToolBar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        toolbar.setTitle("");
        toolbar.setSubtitle("");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        title.setTypeface(Typeface.createFromAsset(getAssets(), "fonts/Lalezar.ttf"));

        init();

        channel_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ManagerHelper.isInternet(TelevisionActivity.this)) {
                    ManagerHelper.noInternetAccess(TelevisionActivity.this);
                } else {
                    Intent intentTV3 = new Intent(TelevisionActivity.this, ChannelActivity.class);
                    intentTV3.putExtra("URL", ApplicationAPI.CHANNEL_1);
                    startActivity(intentTV3);
                }
            }
        });

        channel_2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ManagerHelper.isInternet(TelevisionActivity.this)) {
                    ManagerHelper.noInternetAccess(TelevisionActivity.this);
                } else {
                    Intent intentTV3 = new Intent(TelevisionActivity.this, ChannelActivity.class);
                    intentTV3.putExtra("URL", ApplicationAPI.CHANNEL_2);
                    startActivity(intentTV3);
                }
            }
        });

        channel_3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ManagerHelper.isInternet(TelevisionActivity.this)) {
                    ManagerHelper.noInternetAccess(TelevisionActivity.this);
                } else {
                    Intent intentTV3 = new Intent(TelevisionActivity.this, ChannelActivity.class);
                    intentTV3.putExtra("URL", ApplicationAPI.CHANNEL_3);
                    startActivity(intentTV3);
                }
            }
        });
    }

    private void init() {
        channel_1 = findViewById(R.id.televisionActivity_tv1);
        channel_2 = findViewById(R.id.televisionActivity_tv2);
        channel_3 = findViewById(R.id.televisionActivity_tv3);
        channel_4 = findViewById(R.id.televisionActivity_tv4);
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
