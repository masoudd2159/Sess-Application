package ir.ac.sku.www.sessapplication.activities.home;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;

import ir.ac.sku.www.sessapplication.R;
import ir.ac.sku.www.sessapplication.activities.MainActivity;
import ir.ac.sku.www.sessapplication.utils.MyActivity;

public class StudentDeputyActivity extends MyActivity {

    private CardView cardView_FoodReservation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_deputy);

        TextView title = findViewById(R.id.studentDeputyActivity_ToolbarTitle);
        Toolbar toolbar = (Toolbar) findViewById(R.id.studentDeputyActivity_ToolBar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        toolbar.setTitle("");
        toolbar.setSubtitle("");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        title.setTypeface(Typeface.createFromAsset(getAssets(), "fonts/Lalezar.ttf"));

        init();

        cardView_FoodReservation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                intent.putExtra("position", 1);
                intent.putExtra("title", "رزرو غذا");
                startActivity(intent);
            }
        });

    }

    private void init() {
        cardView_FoodReservation = findViewById(R.id.studentDeputyActivity_FoodReservation);
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
