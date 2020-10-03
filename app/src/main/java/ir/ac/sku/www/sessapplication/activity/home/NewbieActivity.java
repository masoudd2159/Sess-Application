package ir.ac.sku.www.sessapplication.activity.home;

import android.content.res.AssetFileDescriptor;
import android.graphics.Typeface;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.PagerSnapHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.yarolegovich.discretescrollview.DSVOrientation;
import com.yarolegovich.discretescrollview.DiscreteScrollView;
import com.yarolegovich.discretescrollview.transform.ScaleTransformer;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import ir.ac.sku.www.sessapplication.R;
import ir.ac.sku.www.sessapplication.adapter.AssociationAdapter;
import ir.ac.sku.www.sessapplication.adapter.DepartmentsAdapter;
import ir.ac.sku.www.sessapplication.adapter.DetailsAdapter;
import ir.ac.sku.www.sessapplication.model.AssociationModel;
import ir.ac.sku.www.sessapplication.model.DepartmentsModel;
import ir.ac.sku.www.sessapplication.model.DetailsModel;
import ir.ac.sku.www.sessapplication.utils.MusicUtils;
import ir.ac.sku.www.sessapplication.utils.MyActivity;
import me.relex.circleindicator.CircleIndicator2;

public class NewbieActivity extends MyActivity implements
        DiscreteScrollView.OnItemChangedListener<DepartmentsAdapter.MyViewHolder>,
        DiscreteScrollView.ScrollStateChangeListener<DepartmentsAdapter.MyViewHolder> {

    //* Views
    @BindView(R.id.newbieActivity_RecyclerView) RecyclerView recyclerView;
    @BindView(R.id.newbieActivity_PagerIndicator) CircleIndicator2 pagerIndicator;
    @BindView(R.id.newbieActivity_DiscreteScrollView) DiscreteScrollView scrollView;
    @BindView(R.id.newbieActivity_IMageButtonPlay) ImageButton play;
    @BindView(R.id.newbieActivity_SongProgressbar) ProgressBar songProgressbar;

    private List<DetailsModel> detailsModels = new ArrayList<>();
    private List<AssociationModel> modelAnjoman = new ArrayList<>();
    private List<AssociationModel> modelKanon = new ArrayList<>();
    private List<AssociationModel> modelTashakol = new ArrayList<>();

    // Media Player
    private MediaPlayer mp;
    // Handler to update UI timer, progress bar etc,.
    private Handler mHandler = new Handler();

    //private SongsManager songManager;
    private MusicUtils utils;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_newbie);
        ButterKnife.bind(this);

        initToolbar();
        setUpData();

        // set Progress bar values
        songProgressbar.setProgress(0);
        songProgressbar.setMax(MusicUtils.MAX_PROGRESS);

        // Media Player
        mp = new MediaPlayer();
        mp.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                // Changing button image to play button
                play.setImageResource(R.drawable.ic_play_arrow);
            }
        });

        try {
            mp.setAudioStreamType(AudioManager.STREAM_MUSIC);
            AssetFileDescriptor afd = getAssets().openFd("music_one.mp3");
            mp.setDataSource(afd.getFileDescriptor(), afd.getStartOffset(), afd.getLength());
            afd.close();
            mp.prepare();
        } catch (Exception e) {
            Toast.makeText(this, "Cannot load audio file", Toast.LENGTH_SHORT).show();
        }

        utils = new MusicUtils();

        buttonPlayerAction();
    }

    private void initToolbar() {
        TextView title = findViewById(R.id.newbieActivity_ToolbarTitle);
        Toolbar toolbar = (Toolbar) findViewById(R.id.newbieActivity_ToolBar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        toolbar.setTitle("");
        toolbar.setSubtitle("");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        title.setTypeface(Typeface.createFromAsset(getAssets(), "fonts/Lalezar.ttf"));
    }

    private void setUpData() {
        modelAnjoman = Arrays.asList(
                new Gson().fromJson(readTextFile(getResources().openRawResource(R.raw.anjoman)), AssociationModel[].class));

        modelKanon = Arrays.asList(
                new Gson().fromJson(readTextFile(getResources().openRawResource(R.raw.kanon)), AssociationModel[].class));

        modelTashakol = Arrays.asList(
                new Gson().fromJson(readTextFile(getResources().openRawResource(R.raw.tashakol)), AssociationModel[].class));

        String[] detailsArray = getResources().getStringArray(R.array.detailsArray);
        for (int i = 0; i < detailsArray.length; i += 4) {
            DetailsModel model = new DetailsModel();
            model.setId(Integer.parseInt(detailsArray[i]));
            model.setDepartmentId(Integer.parseInt(detailsArray[i + 1]));
            model.setTitle(detailsArray[i + 2]);
            model.setUrl(detailsArray[i + 3]);

            detailsModels.add(model);
        }
        getDepartments();
    }

    private void getDepartments() {
        String[] topicsArray = getResources().getStringArray(R.array.topicsArray);
        List<DepartmentsModel> departmentsModel = new ArrayList<>();
        for (int i = 0; i < topicsArray.length; i += 3) {
            DepartmentsModel model = new DepartmentsModel();
            model.setId(Integer.parseInt(topicsArray[i]));
            model.setTitle(topicsArray[i + 1]);
            model.setImage(topicsArray[i + 2]);

            departmentsModel.add(model);
        }
        setUpDiscreteScrollView(departmentsModel);
    }

    private void setUpDiscreteScrollView(List<DepartmentsModel> departmentsModel) {
        scrollView.setOrientation(DSVOrientation.HORIZONTAL);
        scrollView.setSlideOnFling(true);
        scrollView.setAdapter(new DepartmentsAdapter(this, departmentsModel));
        scrollView.addOnItemChangedListener(this);
        scrollView.addScrollStateChangeListener(this);
        scrollView.setItemTransformer(new ScaleTransformer.Builder()
                .setMinScale(0.8f)
                .build());

        PagerSnapHelper pagerSnapHelper = new PagerSnapHelper();
        pagerSnapHelper.attachToRecyclerView(scrollView);

        pagerIndicator.attachToRecyclerView(scrollView, pagerSnapHelper);
    }

    /**
     * Play button click event plays a song and changes button to pause image
     * pauses a song and changes button to play image
     */
    private void buttonPlayerAction() {
        play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                // check for already playing
                if (mp.isPlaying()) {
                    mp.pause();
                    // Changing button image to play button
                    play.setImageResource(R.drawable.ic_play_arrow);
                } else {
                    // Resume song
                    mp.start();
                    // Changing button image to pause button
                    play.setImageResource(R.drawable.ic_pause);
                    mHandler.post(mUpdateTimeTask);
                }

            }
        });
    }

    public void controlClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.bt_expand: {
                Toast.makeText(this, "Expand", Toast.LENGTH_SHORT).show();
                break;
            }
        }
    }

    /**
     * Background Runnable thread
     */
    private Runnable mUpdateTimeTask = new Runnable() {
        public void run() {
            updateTimerAndSeekbar();
            // Running this thread after 10 milliseconds
            if (mp.isPlaying()) {
                mHandler.postDelayed(this, 100);
            }
        }
    };

    private void updateTimerAndSeekbar() {
        long totalDuration = mp.getDuration();
        long currentDuration = mp.getCurrentPosition();

        // Updating progress bar
        int progress = (int) (utils.getProgressSeekBar(currentDuration, totalDuration));
        songProgressbar.setProgress(progress);
    }

    // stop player when destroy
    @Override
    public void onDestroy() {
        super.onDestroy();
        mHandler.removeCallbacks(mUpdateTimeTask);
        mp.release();
    }

    @Override public void onCurrentItemChanged(@Nullable DepartmentsAdapter.MyViewHolder viewHolder, int adapterPosition) {
        if (viewHolder != null) {
            viewHolder.setBackground();

            recyclerView.setLayoutAnimation(AnimationUtils.loadLayoutAnimation(this, R.anim.layout_animation_from_right));
            recyclerView.setLayoutManager(new LinearLayoutManager(NewbieActivity.this, LinearLayoutManager.VERTICAL, false));
            recyclerView.setHasFixedSize(true);

            if (adapterPosition == 4) {

                for (int i = 0; i < modelAnjoman.size(); i++) {
                    modelAnjoman.get(i).setSwiped(false);
                    modelAnjoman.get(i).setExpanded(false);
                    modelAnjoman.get(i).setParent(false);
                }

                //set data and list adapter
                recyclerView.setAdapter(new AssociationAdapter(this, modelAnjoman));

            } else if (adapterPosition == 5) {
                for (int i = 0; i < modelKanon.size(); i++) {
                    modelKanon.get(i).setSwiped(false);
                    modelKanon.get(i).setExpanded(false);
                    modelKanon.get(i).setParent(false);
                }

                //set data and list adapter
                recyclerView.setAdapter(new AssociationAdapter(this, modelKanon));
            } else if (adapterPosition == 6) {
                for (int i = 0; i < modelTashakol.size(); i++) {
                    modelTashakol.get(i).setSwiped(false);
                    modelTashakol.get(i).setExpanded(false);
                    modelTashakol.get(i).setParent(false);
                }

                //set data and list adapter
                recyclerView.setAdapter(new AssociationAdapter(this, modelTashakol));
            } else {
                List<DetailsModel> data = new ArrayList<>();

                if (detailsModels != null) {
                    for (int j = 0; j < detailsModels.size(); j++) {
                        if (detailsModels.get(j).getDepartmentId() == (adapterPosition + 1)) {
                            data.add(detailsModels.get(j));
                        }
                    }

                    DetailsAdapter adapter = new DetailsAdapter(this, data);
                    recyclerView.setAdapter(adapter);
                }
            }
        }

    }

    @Override public void onScrollStart(@NonNull DepartmentsAdapter.MyViewHolder currentItemHolder, int adapterPosition) {
        currentItemHolder.resetBackground();
    }

    @Override public void onScrollEnd(@NonNull DepartmentsAdapter.MyViewHolder
                                              currentItemHolder, int adapterPosition) {

    }

    @Override public void onScroll(float scrollPosition, int currentPosition, int newPosition,
                                   @Nullable DepartmentsAdapter.MyViewHolder currentHolder,
                                   @Nullable DepartmentsAdapter.MyViewHolder newCurrent) {

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
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