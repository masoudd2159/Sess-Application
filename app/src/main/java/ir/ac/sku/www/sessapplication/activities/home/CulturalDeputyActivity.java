package ir.ac.sku.www.sessapplication.activities.home;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;

import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;

import ir.ac.sku.www.sessapplication.R;
import ir.ac.sku.www.sessapplication.utils.MyActivity;

public class CulturalDeputyActivity extends MyActivity {

    private CardView journals;
    private CardView competitions;
    private CardView newbie;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cultural_deputy);
        init();

        TextView title = findViewById(R.id.CulturalDeputyActivity_ToolbarTitle);
        Toolbar toolbar = (Toolbar) findViewById(R.id.CulturalDeputyActivity_ToolBar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        toolbar.setTitle("");
        toolbar.setSubtitle("");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        title.setTypeface(Typeface.createFromAsset(getAssets(), "fonts/Lalezar.ttf"));

        journals.setOnClickListener(v -> startActivity(new Intent(CulturalDeputyActivity.this, JournalTotalActivity.class)));

        competitions.setOnClickListener(v -> startActivity(new Intent(CulturalDeputyActivity.this, CompetitionsActivity.class)));

        newbie.setOnClickListener(v -> startActivity(new Intent(CulturalDeputyActivity.this, NewbieActivity.class)));

    }

    private void init() {
        journals = findViewById(R.id.CulturalDeputyActivity_journals);
        competitions = findViewById(R.id.CulturalDeputyActivity_Competitions);
        newbie = findViewById(R.id.CulturalDeputyActivity_Newbie);
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
