package ir.ac.sku.www.sessapplication.activities;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.SearchView;
import android.widget.TextView;

import java.util.HashMap;

import ir.ac.sku.www.sessapplication.R;
import ir.ac.sku.www.sessapplication.adapters.JournalsAdapter;
import ir.ac.sku.www.sessapplication.adapters.PhoneBookAdapter;
import ir.ac.sku.www.sessapplication.models.JournalModel;
import ir.ac.sku.www.sessapplication.models.PhoneBookModel;
import ir.ac.sku.www.sessapplication.models.TotalJournalsModel;
import ir.ac.sku.www.sessapplication.utils.Handler;
import ir.ac.sku.www.sessapplication.utils.MyActivity;

public class PhoneBookActivity extends MyActivity implements SearchView.OnQueryTextListener {
    private SearchView searchView;
    private RecyclerView recyclerView;
    private ProgressDialog progressDialog;
    private PhoneBookAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phone_book);

        TextView title = findViewById(R.id.phoneBook_ToolbarTitle);
        Toolbar toolbar = (Toolbar) findViewById(R.id.phoneBook_ToolBar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        toolbar.setTitle("");
        toolbar.setSubtitle("");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        title.setTypeface(Typeface.createFromAsset(getAssets(), "fonts/Lalezar.ttf"));

        searchView = findViewById(R.id.phoneBook_SearchView);
        recyclerView = findViewById(R.id.phoneBook_RecyclerView);

        searchView.setFocusable(true);
        searchView.setIconified(false);
        searchView.requestFocusFromTouch();
        searchView.setOnQueryTextListener(this);
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        progressDialog = new ProgressDialog(PhoneBookActivity.this);
        progressDialog.setMessage("لطفا منتظر بمانید!");
        progressDialog.setCancelable(false);
        progressDialog.setButton(DialogInterface.BUTTON_NEGATIVE, "انصراف", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        progressDialog.show();
        getDataFromServer(query);
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        return false;
    }

    private void getDataFromServer(String query) {
        HashMap<String, String> params = new HashMap<>();
        params.put("name", query);
        PhoneBookModel.fetchFromWeb(PhoneBookActivity.this, params, new Handler() {
            @Override
            public void onResponse(boolean ok, Object obj) {
                progressDialog.dismiss();

                if (ok) {
                    PhoneBookModel phoneBookModel = (PhoneBookModel) obj;
                    showData(phoneBookModel);
                }
            }
        });
    }

    private void showData(PhoneBookModel phoneBookModel) {
        adapter = new PhoneBookAdapter(PhoneBookActivity.this, phoneBookModel);
        int resId = R.anim.layout_animation_from_right;
        LayoutAnimationController animation = AnimationUtils.loadLayoutAnimation(PhoneBookActivity.this, resId);
        recyclerView.setLayoutAnimation(animation);
        recyclerView.setLayoutManager(new LinearLayoutManager(PhoneBookActivity.this, LinearLayoutManager.VERTICAL, false));
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
