package ir.ac.sku.www.sessapplication.activity.home;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.List;

import ir.ac.sku.www.sessapplication.R;
import ir.ac.sku.www.sessapplication.activity.utils.ActivityWebView;
import ir.ac.sku.www.sessapplication.adapter.LeaderAdapter;
import ir.ac.sku.www.sessapplication.model.LeaderModel;
import ir.ac.sku.www.sessapplication.utils.MyActivity;

public class LeaderActivity extends MyActivity {

    private RecyclerView recyclerView;
    private LeaderAdapter leaderAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leader);

        recyclerView = findViewById(R.id.leaderActivity_RecyclerView);

        initToolbar();
        initComponent();
    }

    private void initComponent() {
        List<LeaderModel> leaderModels = Arrays
                .asList(new Gson().fromJson(readTextFile(getResources().openRawResource(R.raw.nahad)), LeaderModel[].class));

        recyclerView.setLayoutAnimation(AnimationUtils.loadLayoutAnimation(this, R.anim.layout_animation_from_right));
        recyclerView.setLayoutManager(new LinearLayoutManager(LeaderActivity.this, LinearLayoutManager.VERTICAL, false));
        recyclerView.setHasFixedSize(true);
        leaderAdapter = new LeaderAdapter(this, leaderModels);
        recyclerView.setAdapter(leaderAdapter);
        leaderAdapter.setOnItemClickListener((view, obj, position) -> {
            if (!(obj.getWebsite().equals(""))) {
                Intent intent = new Intent(LeaderActivity.this, ActivityWebView.class);
                intent.putExtra("key.EXTRA_OBJC", obj.getWebsite());
                startActivity(intent);
            }
        });
    }

    private void initToolbar() {
        TextView title = findViewById(R.id.leaderActivity_ToolbarTitle);
        Toolbar toolbar = (Toolbar) findViewById(R.id.leaderActivity_ToolBar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        toolbar.setTitle("");
        toolbar.setSubtitle("");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        title.setTypeface(Typeface.createFromAsset(getAssets(), "fonts/Lalezar.ttf"));
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    public String readTextFile(InputStream inputStream) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        byte[] buf = new byte[1024];
        int len;
        try {
            while ((len = inputStream.read(buf)) != -1) {
                outputStream.write(buf, 0, len);
            }
            outputStream.close();
            inputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return outputStream.toString();
    }
}