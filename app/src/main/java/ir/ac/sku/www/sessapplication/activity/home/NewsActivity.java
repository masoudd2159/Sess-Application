package ir.ac.sku.www.sessapplication.activity.home;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.text.Html;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.bumptech.glide.Glide;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.appbar.CollapsingToolbarLayout;

import ir.ac.sku.www.sessapplication.R;
import ir.ac.sku.www.sessapplication.adapters.NewsAdapter;
import ir.ac.sku.www.sessapplication.model.NewsModel;
import ir.ac.sku.www.sessapplication.utils.MyHandler;
import ir.ac.sku.www.sessapplication.utils.MyActivity;

public class NewsActivity extends MyActivity {

    private SliderPagerAdapter pagerAdapter;
    private ViewPager viewPager;
    private LinearLayout layoutDot;
    private RelativeLayout relativeLayout;

    private RecyclerView recyclerView;
    private NewsAdapter adapter;
    private ProgressDialog progressDialog;
    private AppBarLayout appBarLayout;
    private CollapsingToolbarLayout collapsingToolbar;

    private android.os.Handler handler;
    private int delay = 5000; //milliseconds
    private int page = 0;

    Runnable runnable = new Runnable() {
        public void run() {
            if (pagerAdapter != null) {
                if (pagerAdapter.getCount() == page) {
                    page = 0;
                } else {
                    page++;
                }
                viewPager.setCurrentItem(page, true);
                handler.postDelayed(this, delay);
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news);

        handler = new android.os.Handler();

        final Toolbar toolbar = (Toolbar) findViewById(R.id.newsActivity_ToolBar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        appBarLayout = (AppBarLayout) findViewById(R.id.newsActivity_AppBar);

        collapsingToolbar = (CollapsingToolbarLayout) findViewById(R.id.newsActivity_CollapsingToolbar);
        collapsingToolbar.setTitle(" ");

        toolbar.setTitle(" ");
        toolbar.setSubtitle(" ");
        final TextView title = findViewById(R.id.newsActivity_ToolbarTitle);
        title.setTypeface(Typeface.createFromAsset(getAssets(), "fonts/Lalezar.ttf"));

        appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            boolean isShow = true;
            int scrollRange = -1;

            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                if (scrollRange == -1) {
                    scrollRange = appBarLayout.getTotalScrollRange();
                }
                if (scrollRange + verticalOffset == 0) {
                    title.setVisibility(View.VISIBLE);
                    isShow = true;
                } else if (isShow) {
                    title.setVisibility(View.INVISIBLE);
                    isShow = false;
                }
            }
        });

        init();

        progressDialog = new ProgressDialog(NewsActivity.this);
        progressDialog.setMessage("لطفا منتظر بمانید!");
        progressDialog.setCancelable(false);
        progressDialog.show();

        getDataFromServer();
    }

    @Override
    protected void onResume() {
        super.onResume();
        handler.postDelayed(runnable, delay);
    }

    @Override
    protected void onPause() {
        super.onPause();
        handler.removeCallbacks(runnable);
    }

    private void init() {
        viewPager = findViewById(R.id.newsActivity_ViewPager);
        layoutDot = findViewById(R.id.newsActivity_DotLayout);
        recyclerView = findViewById(R.id.newsActivity_RecyclerView);
    }

    private void getDataFromServer() {
        NewsModel.fetchFromWeb(NewsActivity.this, null, new MyHandler() {
            @Override
            public void onResponse(boolean ok, Object obj) {
                progressDialog.dismiss();

                if (ok) {
                    NewsModel newsModel = new NewsModel();
                    newsModel = (NewsModel) obj;
                    showData(newsModel);

                    pagerAdapter = new SliderPagerAdapter(newsModel);
                    viewPager.setAdapter(pagerAdapter);
                    showDots(viewPager.getCurrentItem());
                    viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                        @Override
                        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

                        }

                        @Override
                        public void onPageSelected(int position) {
                            showDots(position);
                        }

                        @Override
                        public void onPageScrollStateChanged(int state) {

                        }
                    });
                }
            }
        });
    }

    @SuppressLint("WrongConstant")
    private void showData(NewsModel newsModel) {
        adapter = new NewsAdapter(NewsActivity.this, newsModel);
        int resId = R.anim.layout_animation_from_right;
        LayoutAnimationController animation = AnimationUtils.loadLayoutAnimation(NewsActivity.this, resId);
        recyclerView.setLayoutAnimation(animation);
        recyclerView.setLayoutManager(new LinearLayoutManager(NewsActivity.this, LinearLayoutManager.VERTICAL, false));
        recyclerView.setAdapter(adapter);
    }

    private void showDots(int pageNumber) {
        TextView[] dots = new TextView[viewPager.getAdapter().getCount()];
        layoutDot.removeAllViews();
        for (int i = 0; i < dots.length; i++) {
            dots[i] = new TextView(this);
            dots[i].setText(Html.fromHtml("&#8226"));
            dots[i].setTextSize(TypedValue.COMPLEX_UNIT_SP, 30);
            dots[i].setTextColor(ContextCompat.getColor(this, (i == pageNumber ? R.color.dotActive : R.color.dotInActive)));
            layoutDot.addView(dots[i]);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public class SliderPagerAdapter extends PagerAdapter {
        private NewsModel newsModel;

        SliderPagerAdapter(NewsModel newsModel) {
            this.newsModel = newsModel;
        }

        @NonNull
        @Override
        public Object instantiateItem(@NonNull ViewGroup container, final int position) {
            View view = LayoutInflater.from(NewsActivity.this).inflate(R.layout.gallery_slider, container, false);
            ImageView imageView = view.findViewById(R.id.slideImage);
            TextView textView = view.findViewById(R.id.title);
            Glide.with(NewsActivity.this).load(newsModel.getResult().getPins().get(position).getImage()).into(imageView);
            textView.setText(newsModel.getResult().getPins().get(position).getTitle().trim());

            view.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    intent.setData(Uri.parse(newsModel.getResult().getPins().get(position).getLink()));
                    startActivity(intent);
                }
            });

            container.addView(view);
            return view;
        }

        @Override
        public int getCount() {
            return newsModel.getResult().getPins().size();
        }

        @Override
        public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
            return view == object;
        }

        @Override
        public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
            View view = (View) object;
            container.removeView(view);
        }

    }
}
