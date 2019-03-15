package ir.ac.sku.www.sessapplication.activities;

import android.app.ProgressDialog;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.TextView;

import ir.ac.sku.www.sessapplication.R;
import ir.ac.sku.www.sessapplication.adapters.JournalTotalAdapter;
import ir.ac.sku.www.sessapplication.adapters.JournalsAdapter;
import ir.ac.sku.www.sessapplication.models.TotalJournalsModel;
import ir.ac.sku.www.sessapplication.utils.Handler;
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
        TotalJournalsModel.fetchFromWeb(JournalTotalActivity.this, null, new Handler() {
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
