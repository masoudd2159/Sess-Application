package ir.ac.sku.www.sessapplication.activity.home;

import android.os.Bundle;
import android.view.animation.AnimationUtils;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import ir.ac.sku.www.sessapplication.R;
import ir.ac.sku.www.sessapplication.adapters.LeaderAdapter;
import ir.ac.sku.www.sessapplication.base.BaseActivity;
import ir.ac.sku.www.sessapplication.model.LeaderModel;
import ir.ac.sku.www.sessapplication.utils.Tools;

public class LeaderActivity extends BaseActivity {

    @BindView(R.id.leaderActivity_RecyclerView) RecyclerView recyclerView;

    @Override protected int getLayoutResource() {
        return R.layout.activity_leader;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitleToolbar("نهاد نمایندگی مقام معظم رهبری");
        initComponent();
    }

    private void initComponent() {
        List<LeaderModel> leaderModels = Arrays
                .asList(new Gson().fromJson(readTextFile(getResources().openRawResource(R.raw.nahad)), LeaderModel[].class));

        recyclerView.setLayoutAnimation(AnimationUtils.loadLayoutAnimation(this, R.anim.layout_animation_from_right));
        recyclerView.setLayoutManager(new LinearLayoutManager(LeaderActivity.this, LinearLayoutManager.VERTICAL, false));
        recyclerView.setHasFixedSize(true);
        LeaderAdapter leaderAdapter = new LeaderAdapter(this, leaderModels);
        recyclerView.setAdapter(leaderAdapter);
        leaderAdapter.setOnItemClickListener((view, obj, position) -> {
            if (!(obj.getWebsite().equals(""))) {
                Tools.openWebViewActivity(this, obj.getWebsite());
            }
        });
    }

    public String readTextFile(InputStream inputStream) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        byte[] buf = new byte[1024];
        int len;
        try {
            while ((len = inputStream.read(buf)) != -1) {
                outputStream.write(buf, 0, len);
            }
            outputStream.close();
            inputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return outputStream.toString();
    }
}