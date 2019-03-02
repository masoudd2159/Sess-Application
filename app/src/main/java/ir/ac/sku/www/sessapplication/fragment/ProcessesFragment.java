package ir.ac.sku.www.sessapplication.fragment;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;

import java.util.HashMap;
import java.util.Map;

import ir.ac.sku.www.sessapplication.R;
import ir.ac.sku.www.sessapplication.adapters.ProcessesFragmentAdapter;
import ir.ac.sku.www.sessapplication.models.StudentRequests;
import ir.ac.sku.www.sessapplication.utils.Handler;

public class ProcessesFragment extends Fragment {

    private RecyclerView recyclerView;
    private View rootView;
    private ProgressDialog progressDialog;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_process, container, false);
        this.rootView = rootView;
        recyclerView = rootView.findViewById(R.id.fragmentProcesses_RecyclerView);

        progressDialog = new ProgressDialog(rootView.getContext());
        progressDialog.setMessage("لطفا منتظر بمانید!");
        progressDialog.setCancelable(false);
        progressDialog.show();

        prepareData();
        return rootView;
    }

    private void prepareData() {
        Map<String, String> params = new HashMap<>();
        params.put("type", "test");

        StudentRequests.fetchFromWeb(rootView.getContext(), (HashMap<String, String>) params, new Handler() {
            @Override
            public void onResponse(boolean ok, Object obj) {
                progressDialog.dismiss();
                if (ok) {
                    StudentRequests studentRequests = (StudentRequests) obj;
                    showData(studentRequests);
                }
            }
        });
    }

    private void showData(StudentRequests studentRequests) {
        ProcessesFragmentAdapter adapter = new ProcessesFragmentAdapter(studentRequests.getResult().getRequests());
        int resId = R.anim.layout_animation_from_right;
        LayoutAnimationController animation = AnimationUtils.loadLayoutAnimation(rootView.getContext(), resId);
        recyclerView.setLayoutAnimation(animation);
        recyclerView.setLayoutManager(new LinearLayoutManager(rootView.getContext(), LinearLayoutManager.VERTICAL, false));
        recyclerView.setAdapter(adapter);
    }
}
