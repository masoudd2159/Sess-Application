package ir.ac.sku.www.sessapplication.activities.fragmentHome;

import android.graphics.Typeface;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.widget.Toolbar;

import ir.ac.sku.www.sessapplication.R;
import ir.ac.sku.www.sessapplication.utils.HttpManager;
import ir.ac.sku.www.sessapplication.utils.MyActivity;

public class OfficeDeputyActivity extends MyActivity {

    ImageView img_AbsenceOfEmployees;
    ImageView img_Automation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_office_deputy);

        TextView title = findViewById(R.id.officeDeputyActivity_ToolbarTitle);
        Toolbar toolbar = (Toolbar) findViewById(R.id.officeDeputyActivity_ToolBar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        toolbar.setTitle("");
        toolbar.setSubtitle("");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        title.setTypeface(Typeface.createFromAsset(getAssets(), "fonts/Lalezar.ttf"));

        init();

        img_AbsenceOfEmployees.setColorFilter(HttpManager.getBlackWhiteFilter());
        img_Automation.setColorFilter(HttpManager.getBlackWhiteFilter());

    }

    private void init() {
        img_AbsenceOfEmployees = findViewById(R.id.officeDeputyActivity_AbsenceOfEmployees_ImageView);
        img_Automation = findViewById(R.id.officeDeputyActivity_Automation_ImageView);
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
