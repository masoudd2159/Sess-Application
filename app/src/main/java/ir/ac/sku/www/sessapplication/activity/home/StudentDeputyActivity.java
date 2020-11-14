package ir.ac.sku.www.sessapplication.activity.home;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;

import androidx.cardview.widget.CardView;

import butterknife.BindView;
import ir.ac.sku.www.sessapplication.R;
import ir.ac.sku.www.sessapplication.activity.MainActivity;
import ir.ac.sku.www.sessapplication.base.BaseActivity;
import ir.ac.sku.www.sessapplication.utils.Tools;
import ir.ac.sku.www.sessapplication.utils.helper.ManagerHelper;

@SuppressLint("NonConstantResourceId")
public class StudentDeputyActivity extends BaseActivity {

    @BindView(R.id.studentDeputyActivity_Clinic) CardView cardViewClinic;
    @BindView(R.id.studentDeputyActivity_Commission) CardView cardViewCommission;
    @BindView(R.id.studentDeputyActivity_CounselingCenter) CardView cardViewCounselingCenter;
    @BindView(R.id.studentDeputyActivity_DormitoryOffice) CardView cardViewDormitoryOffice;
    @BindView(R.id.studentDeputyActivity_FoodReservation) CardView cardViewFoodReservation;
    @BindView(R.id.studentDeputyActivity_NutritionOffice) CardView cardViewNutritionOffice;
    @BindView(R.id.studentDeputyActivity_PhysicalEducation) CardView cardViewPhysicalEducation;
    @BindView(R.id.studentDeputyActivity_WelfareOffice) CardView cardViewWelfareOffice;

    @Override
    protected int getLayoutResource() {
        return R.layout.activity_student_deputy;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitleToolbar(getResources().getString(R.string.student_deputy));
        setUpView();
    }

    private void setUpView() {
        cardViewFoodReservation.setOnClickListener(v -> {
            if (ManagerHelper.checkInternetServices(this)) {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                intent.putExtra("position", 1);
                intent.putExtra("title", "رزرو غذا");
                startActivity(intent);
            }
        });

        cardViewClinic.setOnClickListener(v -> {
            Tools.openWebViewActivity(this, "https://www.sku.ac.ir/Vice-President/student/Page/4083");
        });

        cardViewCommission.setOnClickListener(v -> {
            Tools.openWebViewActivity(this, "https://www.sku.ac.ir/Vice-President/student/Page/4084");
        });

        cardViewCounselingCenter.setOnClickListener(v -> {
            Tools.openWebViewActivity(this, "https://www.sku.ac.ir/Vice-President/student/Page/2013");
        });

        cardViewDormitoryOffice.setOnClickListener(v -> {
            Tools.openWebViewActivity(this, "https://www.sku.ac.ir/Vice-President/student/Page/4081");
        });

        cardViewNutritionOffice.setOnClickListener(v -> {
            Tools.openWebViewActivity(this, "https://www.sku.ac.ir/Vice-President/student/Page/4082");
        });

        cardViewPhysicalEducation.setOnClickListener(v -> {
            Tools.openWebViewActivity(this, "https://www.sku.ac.ir/office/pe");
        });

        cardViewWelfareOffice.setOnClickListener(v -> {
            Tools.openWebViewActivity(this, "https://www.sku.ac.ir/Vice-President/student/Page/4090");
        });
    }
}
