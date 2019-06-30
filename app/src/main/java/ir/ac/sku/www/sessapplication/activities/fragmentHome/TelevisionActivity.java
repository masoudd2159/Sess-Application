package ir.ac.sku.www.sessapplication.activities.fragmentHome;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;

import ir.ac.sku.www.sessapplication.R;
import ir.ac.sku.www.sessapplication.activities.television.Channel3Activity;
import ir.ac.sku.www.sessapplication.utils.HttpManager;
import ir.ac.sku.www.sessapplication.utils.MyActivity;

public class TelevisionActivity extends MyActivity {

    private CardView channel_3;

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

        channel_3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (HttpManager.isNOTOnline(TelevisionActivity.this)) {
                    HttpManager.noInternetAccess(TelevisionActivity.this);
                } else {
                    startActivity(new Intent(TelevisionActivity.this, Channel3Activity.class));
                }
            }
        });
    }

    private void init() {
        channel_3 = findViewById(R.id.televisionActivity_tv3);
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
