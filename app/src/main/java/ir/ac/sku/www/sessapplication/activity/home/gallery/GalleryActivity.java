package ir.ac.sku.www.sessapplication.activity.home.gallery;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.view.animation.AnimationUtils;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import ir.ac.sku.www.sessapplication.R;
import ir.ac.sku.www.sessapplication.adapters.GalleryAdapter;
import ir.ac.sku.www.sessapplication.base.BaseActivity;
import ir.ac.sku.www.sessapplication.fragment.dialogfragment.DialogFragmentShowImage;
import ir.ac.sku.www.sessapplication.model.AlbumModel;

@SuppressLint("NonConstantResourceId")
public class GalleryActivity extends BaseActivity implements GalleryAdapter.ItemClickListener {
    @BindView(R.id.recyclerView) RecyclerView recyclerView;
    private int itemPosition;
    private List<AlbumModel> albumModels = new ArrayList<>();

    @Override protected int getLayoutResource() {
        return R.layout.activity_gallery;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        itemPosition = getIntent().getIntExtra("position", 0);
        setUpData();
    }

    private void setUpData() {
        albumModels = Arrays.asList(new Gson().fromJson(readTextFile(getResources().openRawResource(R.raw.album)), AlbumModel[].class));
        setTitleToolbar(albumModels.get(itemPosition).getSection());
        recyclerView.setLayoutAnimation(AnimationUtils.loadLayoutAnimation(this, R.anim.layout_animation_from_right));
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        recyclerView.setHasFixedSize(true);
        GalleryAdapter adapter = new GalleryAdapter(this, albumModels.get(itemPosition));
        adapter.setClickListener(this);
        recyclerView.setAdapter(adapter);
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

    @Override
    public void onItemClick(View view, int position) {
        new DialogFragmentShowImage(albumModels.get(itemPosition).getPictures().get(position)).show(getSupportFragmentManager(), "DialogFragmentShowImage");
    }
}