package ir.ac.sku.www.sessapplication.activities;

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

import ir.ac.sku.www.sessapplication.R;
import ir.ac.sku.www.sessapplication.adapters.JournalTotalAdapter;
import ir.ac.sku.www.sessapplication.models.TotalJournalsModel;
import ir.ac.sku.www.sessapplication.utils.MyHandler;
import ir.ac.sku.www.sessapplication.utils.MyActivity;

public class JournalTotalActivity extends MyActivity {

    private RecyclerView recyclerView;
    private JournalTotalAdapter adapter;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_journal_total);

        TextView title = findViewById(R.id.journalTotalActivity_ToolbarTitle);
        Toolbar toolbar = (Toolbar) findViewById(R.id.journalTotalActivity_ToolBar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        toolbar.setTitle("");
        toolbar.setSubtitle("");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        title.setTypeface(Typeface.createFromAsset(getAssets(), "fonts/Lalezar.ttf"));

        progressDialog = new ProgressDialog(JournalTotalActivity.this);
        progressDialog.setMessage("لطفا منتظر بمانید!");
        progressDialog.setCancelable(false);
        progressDialog.show();

        recyclerView = (RecyclerView) findViewById(R.id.journalTotalActivity_RecyclerView);

        getDataFromServer();
    }

    private void getDataFromServer() {
        TotalJournalsModel.fetchFromWeb(JournalTotalActivity.this, null, new MyHandler() {
            @Override
            public void onResponse(boolean ok, Object obj) {
                progressDialog.dismiss();

                if (ok) {
                    TotalJournalsModel totalJournalsModel = (TotalJournalsModel) obj;
                    showData(totalJournalsModel);
                }
            }
        });
    }

    @SuppressLint("WrongConstant")
    private void showData(TotalJournalsModel totalJournalsModel) {
        adapter = new JournalTotalAdapter(JournalTotalActivity.this, totalJournalsModel);
        int resId = R.anim.layout_animation_from_right;
        LayoutAnimationController animation = AnimationUtils.loadLayoutAnimation(JournalTotalActivity.this, resId);
        recyclerView.setLayoutAnimation(animation);
        recyclerView.setLayoutManager(new LinearLayoutManager(JournalTotalActivity.this, LinearLayoutManager.VERTICAL, false));
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
