package ir.ac.sku.www.sessapplication.activity.home;

import android.graphics.Typeface;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.widget.Toolbar;

import ir.ac.sku.www.sessapplication.R;
import ir.ac.sku.www.sessapplication.utils.helper.ManagerHelper;
import ir.ac.sku.www.sessapplication.utils.MyActivity;

public class ResearchDeputyActivity extends MyActivity {

    ImageView img_IndustryRelations;
    ImageView img_SeminarsAndConferences;
    ImageView img_InformationTechnologyCenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_research_deputy);

        TextView title = findViewById(R.id.researchDeputyActivity_ToolbarTitle);
        Toolbar toolbar = (Toolbar) findViewById(R.id.researchDeputyActivity_ToolBar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        toolbar.setTitle("");
        toolbar.setSubtitle("");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        title.setTypeface(Typeface.createFromAsset(getAssets(), "fonts/Lalezar.ttf"));

        init();

        img_IndustryRelations.setColorFilter(ManagerHelper.getBlackWhiteFilter());
        img_SeminarsAndConferences.setColorFilter(ManagerHelper.getBlackWhiteFilter());
        img_InformationTechnologyCenter.setColorFilter(ManagerHelper.getBlackWhiteFilter());

    }

    private void init() {
        img_IndustryRelations = findViewById(R.id.researchDeputyActivity_IndustryRelations_ImageView);
        img_SeminarsAndConferences = findViewById(R.id.researchDeputyActivity_SeminarsAndConferences_ImageView);
        img_InformationTechnologyCenter = findViewById(R.id.researchDeputyActivity_InformationTechnologyCenter_ImageView);
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
