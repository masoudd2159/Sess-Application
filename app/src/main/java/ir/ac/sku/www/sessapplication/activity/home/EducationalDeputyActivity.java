package ir.ac.sku.www.sessapplication.activity.home;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;

import ir.ac.sku.www.sessapplication.R;
import ir.ac.sku.www.sessapplication.activity.MainActivity;
import ir.ac.sku.www.sessapplication.utils.helper.ManagerHelper;
import ir.ac.sku.www.sessapplication.utils.MyActivity;

public class EducationalDeputyActivity extends MyActivity {

    ImageView img_Attendance;
    ImageView img_Professors;
    ImageView img_WeeklySchedule;

    private CardView processes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_educational_deputy);

        TextView title = findViewById(R.id.educationalDeputyActivity_ToolbarTitle);
        Toolbar toolbar = (Toolbar) findViewById(R.id.educationalDeputyActivity_ToolBar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        toolbar.setTitle("");
        toolbar.setSubtitle("");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        title.setTypeface(Typeface.createFromAsset(getAssets(), "fonts/Lalezar.ttf"));

        init();

        /*img_Attendance.setColorFilter(ManagerHelper.getBlackWhiteFilter());
        img_Professors.setColorFilter(ManagerHelper.getBlackWhiteFilter());
        img_WeeklySchedule.setColorFilter(ManagerHelper.getBlackWhiteFilter());*/

        processes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                intent.putExtra("position", 2);
                intent.putExtra("title", "پیگیری فرایند ها");
                startActivity(intent);
            }
        });

    }

    private void init() {
        img_Attendance = findViewById(R.id.educationalDeputyActivity_Attendance_ImageView);
        img_Professors = findViewById(R.id.educationalDeputyActivity_Professors_ImageView);
        img_WeeklySchedule = findViewById(R.id.educationalDeputyActivity_WeeklySchedule_ImageView);

        processes = findViewById(R.id.educationalDeputyActivity_Processes);
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
