package ir.ac.sku.www.sessapplication.activities;

import android.app.ProgressDialog;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.TextView;

import ir.ac.sku.www.sessapplication.R;
import ir.ac.sku.www.sessapplication.adapters.CompetitionsAdapter;
import ir.ac.sku.www.sessapplication.models.CompetitionsModel;
import ir.ac.sku.www.sessapplication.utils.Handler;
import ir.ac.sku.www.sessapplication.utils.MyActivity;

public class CompetitionsActivity extends MyActivity {

    private RecyclerView recyclerView;
    private CompetitionsAdapter adapter;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_competitions);

        TextView title = findViewById(R.id.CompetitionsActivity_ToolbarTitle);
        Toolbar toolbar = (Toolbar) findViewById(R.id.CompetitionsActivity_ToolBar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        toolbar.setTitle("");
        toolbar.setSubtitle("");
        title.setTypeface(Typeface.createFromAsset(getAssets(), "fonts/Lalezar.ttf"));

        progressDialog = new ProgressDialog(CompetitionsActivity.this);
        progressDialog.setMessage("لطفا منتظر بمانید!");
        progressDialog.setCancelable(false);
        progressDialog.show();

        recyclerView = (RecyclerView) findViewById(R.id.CompetitionsActivity_RecyclerView);

        getDataFromServer();
    }

    private void getDataFromServer() {
        CompetitionsModel.fetchFromWeb(CompetitionsActivity.this, null, new Handler() {
            @Override
            public void onResponse(boolean ok, Object obj) {
                progressDialog.dismiss();

                if (ok) {
                    CompetitionsModel competitionsModel = (CompetitionsModel) obj;
                    showData(competitionsModel);
                }
            }
        });
    }

    private void showData(CompetitionsModel competitionsModel) {
        adapter = new CompetitionsAdapter(CompetitionsActivity.this, competitionsModel);
        int resId = R.anim.layout_animation_from_right;
        LayoutAnimationController animation = AnimationUtils.loadLayoutAnimation(CompetitionsActivity.this, resId);
        recyclerView.setLayoutAnimation(animation);
        recyclerView.setLayoutManager(new LinearLayoutManager(CompetitionsActivity.this, LinearLayoutManager.VERTICAL, false));
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
