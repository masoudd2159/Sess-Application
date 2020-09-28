package ir.ac.sku.www.sessapplication.activity.utils;

import android.annotation.SuppressLint;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;

import androidx.appcompat.widget.Toolbar;

import com.mapbox.mapboxsdk.Mapbox;
import com.mapbox.mapboxsdk.maps.MapView;

import ir.ac.sku.www.sessapplication.R;
import ir.ac.sku.www.sessapplication.utils.MyActivity;

public class ParsMapActivity extends MyActivity {

    @SuppressLint("WrongConstant")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Mapbox.getInstance(this, "parsimap.accessToken");
        setContentView(R.layout.activity_pars_map);
        initToolbar();
        MapView mapView = findViewById(R.id.parsMap_MapView);
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(mapboxMap -> mapboxMap.setStyle("https://www.parsimap.com/styles/street.json"));
    }

    private void initToolbar() {
        TextView title = findViewById(R.id.parsMap_ToolbarTitle);
        Toolbar toolbar = (Toolbar) findViewById(R.id.parsMap_ToolBar);
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
}