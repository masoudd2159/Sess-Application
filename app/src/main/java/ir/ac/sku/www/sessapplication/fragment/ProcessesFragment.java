package ir.ac.sku.www.sessapplication.fragment;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.HashMap;
import java.util.Map;

import ir.ac.sku.www.sessapplication.R;
import ir.ac.sku.www.sessapplication.adapter.ProcessesFragmentAdapter;
import ir.ac.sku.www.sessapplication.model.StudentRequests;
import ir.ac.sku.www.sessapplication.utils.MyHandler;

public class ProcessesFragment extends Fragment {

    private RecyclerView recyclerView;
    private View rootView;
    private ProgressDialog progressDialog;
    private RelativeLayout relativeLayout;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_process, container, false);
        this.rootView = rootView;
        recyclerView = rootView.findViewById(R.id.fragmentProcesses_RecyclerView);
        relativeLayout = rootView.findViewById(R.id.fragmentProcesses_RelativeLayout);

        progressDialog = new ProgressDialog(rootView.getContext());
        progressDialog.setMessage("لطفا منتظر بمانید!");
        progressDialog.setCancelable(false);
        progressDialog.setButton(DialogInterface.BUTTON_NEGATIVE, "انصراف", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        progressDialog.show();

        prepareData();
        return rootView;
    }

    private void prepareData() {
        Map<String, String> params = new HashMap<>();
        params.put("type", "test");

        StudentRequests.fetchFromWeb(rootView.getContext(), (HashMap<String, String>) params, new MyHandler() {
            @Override
            public void onResponse(boolean ok, Object obj) {
                progressDialog.dismiss();
                if (ok) {
                    StudentRequests studentRequests = (StudentRequests) obj;
                    showData(studentRequests);
                }
            }
        });


        Drawable background = relativeLayout.getBackground();
        background.setAlpha(75);
    }

    @SuppressLint("WrongConstant")
    private void showData(StudentRequests studentRequests) {
        ProcessesFragmentAdapter adapter = new ProcessesFragmentAdapter(studentRequests.getResult().getRequests());
        int resId = R.anim.layout_animation_from_right;
        LayoutAnimationController animation = AnimationUtils.loadLayoutAnimation(rootView.getContext(), resId);
        recyclerView.setLayoutAnimation(animation);
        recyclerView.setLayoutManager(new LinearLayoutManager(rootView.getContext(), LinearLayoutManager.VERTICAL, false));
        recyclerView.setAdapter(adapter);
    }
}
