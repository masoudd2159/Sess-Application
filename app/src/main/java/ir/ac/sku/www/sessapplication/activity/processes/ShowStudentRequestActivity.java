package ir.ac.sku.www.sessapplication.activity.processes;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.HashMap;

import ir.ac.sku.www.sessapplication.R;
import ir.ac.sku.www.sessapplication.adapters.StudentRequestAdapter;
import ir.ac.sku.www.sessapplication.model.StudentRequestDetails;
import ir.ac.sku.www.sessapplication.utils.MyHandler;
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

        StudentRequestDetails.fetchFromWeb(ShowStudentRequestActivity.this, params, new MyHandler() {
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

    @SuppressLint("WrongConstant")
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
