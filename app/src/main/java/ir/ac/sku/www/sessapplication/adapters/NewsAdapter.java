package ir.ac.sku.www.sessapplication.adapters;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import ir.ac.sku.www.sessapplication.R;
import ir.ac.sku.www.sessapplication.activities.utils.ActivityWebView;
import ir.ac.sku.www.sessapplication.models.NewsModel;

public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.MyViewHolder> {

    private NewsModel news;
    private Activity activity;

    public NewsAdapter(@NonNull Activity activity, NewsModel newsModel) {
        this.news = (newsModel == null) ? new NewsModel() : newsModel;
        this.activity = activity;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.custom_news_list, viewGroup, false);
        return new NewsAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, int position) {
        myViewHolder.bind(news.getResult().getNews().get(position));
    }

    @Override
    public int getItemCount() {
        return news.getResult().getNews().size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {

        private ImageView newsImage;
        private TextView title;
        private TextView summary;
        private Dialog dialog;

        MyViewHolder(@NonNull View itemView) {
            super(itemView);

            newsImage = itemView.findViewById(R.id.newsList_NewsImageView);
            title = itemView.findViewById(R.id.newsList_Title);
            summary = itemView.findViewById(R.id.newsList_Summary);

            itemView.setOnClickListener(this);
            itemView.setOnLongClickListener(this);
        }

        void bind(NewsModel.Result.News news) {
            title.setText(news.getTitle());
            summary.setText(news.getText());

            Glide.with(activity).load(news.getImage()).diskCacheStrategy(DiskCacheStrategy.ALL).into(newsImage);
        }

        @Override
        public void onClick(View v) {
            Intent intent = new Intent(activity, ActivityWebView.class);
            intent.putExtra("key.EXTRA_OBJC", news.getResult().getNews().get(getLayoutPosition()).getLink());
            activity.startActivity(intent);
        }

        @Override
        public boolean onLongClick(View v) {
            dialog = new Dialog(activity);
            dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            WindowManager.LayoutParams layoutParams = dialog.getWindow().getAttributes();
            layoutParams.dimAmount = 0.7f;
            dialog.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
            dialog.setContentView(R.layout.custom_show_image);

            Button close = dialog.findViewById(R.id.showImage_Close);
            ImageView imageView = dialog.findViewById(R.id.showImage_ImageView);

            Glide.with(activity).load(news.getResult().getNews().get(getLayoutPosition()).getImage()).into(imageView);

            close.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                }
            });

            dialog.show();
            return false;
        }
    }
}
