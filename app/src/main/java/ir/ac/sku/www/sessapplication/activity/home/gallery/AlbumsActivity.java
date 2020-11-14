package ir.ac.sku.www.sessapplication.activity.home.gallery;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.AnimationUtils;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import ir.ac.sku.www.sessapplication.R;
import ir.ac.sku.www.sessapplication.adapters.AlbumsAdapter;
import ir.ac.sku.www.sessapplication.base.BaseActivity;
import ir.ac.sku.www.sessapplication.model.AlbumModel;

@SuppressLint("NonConstantResourceId")
public class AlbumsActivity extends BaseActivity implements AlbumsAdapter.ItemClickListener {
    @BindView(R.id.recyclerView) RecyclerView recyclerView;


    @Override protected int getLayoutResource() {
        return R.layout.activity_albums;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitleToolbar(getResources().getString(R.string.sku_album));
        setUpData();
    }

    private void setUpData() {
        List<AlbumModel> albumModels = Arrays.asList(new Gson().fromJson(readTextFile(getResources().openRawResource(R.raw.album)), AlbumModel[].class));
        recyclerView.setLayoutAnimation(AnimationUtils.loadLayoutAnimation(this, R.anim.layout_animation_from_right));
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        recyclerView.setHasFixedSize(true);
        AlbumsAdapter adapter = new AlbumsAdapter(this, albumModels);
        adapter.setClickListener(this);
        recyclerView.setAdapter(adapter);
    }

    @Override public void onItemClick(View view, int position) {
        Intent intent = new Intent(AlbumsActivity.this, GalleryActivity.class);
        intent.putExtra("position", position);
        startActivity(intent);
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