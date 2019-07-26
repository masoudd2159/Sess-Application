package ir.ac.sku.www.sessapplication.activities;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.widget.Toolbar;

import com.google.android.material.appbar.AppBarLayout;

import es.voghdev.pdfviewpager.library.RemotePDFViewPager;
import es.voghdev.pdfviewpager.library.adapter.PDFPagerAdapter;
import es.voghdev.pdfviewpager.library.remote.DownloadFile;
import es.voghdev.pdfviewpager.library.util.FileUtil;
import ir.ac.sku.www.sessapplication.R;
import ir.ac.sku.www.sessapplication.utils.MyActivity;

public class ShowPDFActivity extends MyActivity implements DownloadFile.Listener {
    RemotePDFViewPager remotePDFViewPager;
    PDFPagerAdapter adapter;
    LinearLayout root;
    ProgressDialog progressDialog;
    AppBarLayout appBarLayout;
    TextView title;
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_pdf);

        title = findViewById(R.id.showPDF_ToolbarTitle);
        toolbar = findViewById(R.id.showPDF_ToolBar);
        appBarLayout = findViewById(R.id.showPDF_AppBar);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        toolbar.setTitle("");
        toolbar.setSubtitle("");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        title.setText(getIntent().getStringExtra("PDFName"));
        title.setTypeface(Typeface.createFromAsset(getAssets(), "fonts/Lalezar.ttf"));

        root = (LinearLayout) findViewById(R.id.showPDF_RootLinearLayout);

        progressDialog = new ProgressDialog(ShowPDFActivity.this);
        progressDialog.setMessage("لطفا منتظر بمانید!");
        progressDialog.setCancelable(false);
        progressDialog.show();

        setDownloadButtonListener();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (adapter != null) {
            adapter.close();
        }
    }

    protected void setDownloadButtonListener() {
        final Context ctx = this;
        final DownloadFile.Listener listener = this;
        remotePDFViewPager = new RemotePDFViewPager(ctx, getUrl(), listener);
        remotePDFViewPager.setId(R.id.showPDF_PDFViewPager);
    }

    protected String getUrl() {
        return getIntent().getStringExtra("pdf");
    }

    public static void open(Context context) {
        Intent i = new Intent(context, ShowPDFActivity.class);
        context.startActivity(i);
    }

    public void updateLayout() {
        root.removeAllViewsInLayout();
        root.addView(remotePDFViewPager, LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
    }

    @Override
    public void onSuccess(String url, String destinationPath) {
        adapter = new PDFPagerAdapter(this, FileUtil.extractFileNameFromURL(url));
        remotePDFViewPager.setAdapter(adapter);
        updateLayout();
        progressDialog.dismiss();
    }

    @Override
    public void onFailure(Exception e) {
        e.printStackTrace();
    }

    @Override
    public void onProgressUpdate(int progress, int total) {

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
