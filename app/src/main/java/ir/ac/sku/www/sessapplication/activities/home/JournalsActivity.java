package ir.ac.sku.www.sessapplication.activities.home;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.TextView;

import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.HashMap;

import ir.ac.sku.www.sessapplication.R;
import ir.ac.sku.www.sessapplication.adapters.JournalsAdapter;
import ir.ac.sku.www.sessapplication.models.JournalModel;
import ir.ac.sku.www.sessapplication.utils.MyHandler;
import ir.ac.sku.www.sessapplication.utils.MyActivity;

public class JournalsActivity extends MyActivity {


    private RecyclerView recyclerView;
    private JournalsAdapter adapter;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_journals);

        TextView title = findViewById(R.id.journalsActivity_ToolbarTitle);
        Toolbar toolbar = (Toolbar) findViewById(R.id.journalsActivity_ToolBar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        toolbar.setTitle("");
        toolbar.setSubtitle("");
        title.setText(getIntent().getStringExtra("title"));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        title.setTypeface(Typeface.createFromAsset(getAssets(), "fonts/Lalezar.ttf"));

        progressDialog = new ProgressDialog(JournalsActivity.this);
        progressDialog.setMessage("لطفا منتظر بمانید!");
        progressDialog.setCancelable(false);
        progressDialog.show();

        recyclerView = (RecyclerView) findViewById(R.id.journalsActivity_RecyclerView);

        getDataFromServer();
    }

    private void getDataFromServer() {
        HashMap<String, String> params = new HashMap<>();
        params.put("id", String.valueOf(getIntent().getExtras().getInt("id")));
        params.put("year", getIntent().getStringExtra("year"));
        JournalModel.fetchFromWeb(JournalsActivity.this, params, new MyHandler() {
            @Override
            public void onResponse(boolean ok, Object obj) {
                progressDialog.dismiss();

                if (ok) {
                    JournalModel journalsModel = (JournalModel) obj;
                    showData(journalsModel);
                }
            }
        });
    }

    @SuppressLint("WrongConstant")
    private void showData(JournalModel journalsModel) {
        adapter = new JournalsAdapter(JournalsActivity.this, journalsModel, getIntent().getStringExtra("title"));
        int resId = R.anim.layout_animation_from_right;
        LayoutAnimationController animation = AnimationUtils.loadLayoutAnimation(JournalsActivity.this, resId);
        recyclerView.setLayoutAnimation(animation);
        recyclerView.setLayoutManager(new LinearLayoutManager(JournalsActivity.this, LinearLayoutManager.VERTICAL, false));
        recyclerView.setAdapter(adapter);
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
