package ir.ac.sku.www.sessapplication.activities;

import android.app.ProgressDialog;
import android.graphics.drawable.Drawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.HashMap;
import java.util.Map;

import ir.ac.sku.www.sessapplication.R;
import ir.ac.sku.www.sessapplication.adapters.ProcessesFragmentAdapter;
import ir.ac.sku.www.sessapplication.adapters.StudentRequestAdapter;
import ir.ac.sku.www.sessapplication.models.StudentRequestDetails;
import ir.ac.sku.www.sessapplication.models.StudentRequests;
import ir.ac.sku.www.sessapplication.utils.Handler;
import ir.ac.sku.www.sessapplication.utils.MyActivity;

public class ShowStudentRequestActivity extends MyActivity {

    private TextView title;
    private RecyclerView recyclerView;
    private StudentRequestAdapter adapter;
    private ProgressDialog progressDialog;
    private LinearLayout linearLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_student_request);
        init();

        progressDialog = new ProgressDialog(ShowStudentRequestActivity.this);
        progressDialog.setMessage("لطفا منتظر بمانید!");
        progressDialog.setCancelable(false);
        progressDialog.show();

        getDetailsFromServer(getIntent().getStringExtra("ident"));


        Drawable background = recyclerView.getBackground();
        background.setAlpha(63);
    }

    private void init() {
        recyclerView = findViewById(R.id.showStudentRequest_RecyclerView);
        title = findViewById(R.id.showStudentRequest_TextViewTitle);
        linearLayout = findViewById(R.id.showStudentRequest_LinearLayout);
    }

    private void getDetailsFromServer(String ident) {
        HashMap<String, String> params = new HashMap<>();
        params.put("ident", ident);

        StudentRequestDetails.fetchFromWeb(ShowStudentRequestActivity.this, params, new Handler() {
            @Override
            public void onResponse(boolean ok, Object obj) {
                progressDialog.dismiss();

                if (ok) {
                    StudentRequestDetails studentRequestDetails = (StudentRequestDetails) obj;
                    showData(studentRequestDetails);
                }
            }
        });
    }

    private void showData(StudentRequestDetails studentRequestDetails) {
        title.setText(studentRequestDetails.getResult().getTitle());
        adapter = new StudentRequestAdapter(studentRequestDetails);
        int resId = R.anim.layout_animation_from_right;
        LayoutAnimationController animation = AnimationUtils.loadLayoutAnimation(ShowStudentRequestActivity.this, resId);
        recyclerView.setLayoutAnimation(animation);
        recyclerView.setLayoutManager(new LinearLayoutManager(ShowStudentRequestActivity.this, LinearLayoutManager.VERTICAL, false));
        recyclerView.setAdapter(adapter);
    }
}
