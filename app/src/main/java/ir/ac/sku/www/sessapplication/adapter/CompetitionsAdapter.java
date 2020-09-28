package ir.ac.sku.www.sessapplication.adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.uncopt.android.widget.text.justify.JustifiedTextView;

import ir.ac.sku.www.sessapplication.api.PreferenceName;
import ir.ac.sku.www.sessapplication.R;
import ir.ac.sku.www.sessapplication.model.CompetitionsModel;

public class CompetitionsAdapter extends RecyclerView.Adapter<CompetitionsAdapter.MyViewHolder> {
    private SharedPreferences preferencesUserInformation;
    private CompetitionsModel competitions;
    private Activity activity;

    public CompetitionsAdapter(@NonNull Activity activity, CompetitionsModel competitionsModel) {
        this.competitions = (competitionsModel == null) ? new CompetitionsModel() : competitionsModel;
        this.activity = activity;
        preferencesUserInformation = activity.getSharedPreferences(PreferenceName.PREFERENCE_USER_INFORMATION, Context.MODE_PRIVATE);
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.custom_competitions_list, viewGroup, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, int position) {
        myViewHolder.bind(competitions.getResult().get(position));
    }

    @Override
    public int getItemCount() {
        return competitions.getResult().size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private TextView title;
        private JustifiedTextView description;
        private ImageView picture;
        private RecyclerView recyclerView;

        MyViewHolder(@NonNull View itemView) {
            super(itemView);

            title = itemView.findViewById(R.id.customCompetitionsList_TextViewTitle);
            description = itemView.findViewById(R.id.customCompetitionsList_TextViewDescription);
            picture = itemView.findViewById(R.id.customCompetitionsList_ImageViewPicture);
            recyclerView = itemView.findViewById(R.id.customCompetitionsList_RecyclerView);

            itemView.setOnClickListener(this);
        }

        @SuppressLint("WrongConstant")
        void bind(CompetitionsModel.Result competitions) {
            title.setText(competitions.getTitle());
            description.setText(competitions.getDescription());
            Glide.with(activity).load(competitions.getPicture()).diskCacheStrategy(DiskCacheStrategy.ALL).into(picture);

            AttachmentAdapter adapter = new AttachmentAdapter(activity, competitions);
            int resId = R.anim.layout_animation_from_right;
            LayoutAnimationController animation = AnimationUtils.loadLayoutAnimation(activity, resId);
            recyclerView.setLayoutAnimation(animation);
            recyclerView.setLayoutManager(new LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false));
            recyclerView.setAdapter(adapter);
        }

        @Override
        public void onClick(View v) {
            Intent intent = new Intent(Intent.ACTION_VIEW);
            String myURL = competitions.getResult().get(getLayoutPosition()).getUrl() + "?cookie=" + preferencesUserInformation.getString(PreferenceName.PREFERENCE_COOKIE, "NULL");
            intent.setData(Uri.parse(myURL));
            activity.startActivity(intent);
        }

        public class AttachmentAdapter extends RecyclerView.Adapter<AttachmentAdapter.MyAttachmentAdapterViewHolder> {

            private CompetitionsModel.Result result;
            private Activity activity;

            public AttachmentAdapter(@NonNull Activity activity, CompetitionsModel.Result result) {
                this.result = result;
                this.activity = activity;
            }

            @NonNull
            @Override
            public MyAttachmentAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
                View itemView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.custom_simple_attachment_list, viewGroup, false);
                return new MyAttachmentAdapterViewHolder(itemView);
            }

            @Override
            public void onBindViewHolder(@NonNull MyAttachmentAdapterViewHolder holder, int position) {
                holder.bind(result.getFiles().get(position));
            }

            @Override
            public int getItemCount() {
                return result.getFiles().size();
            }

            class MyAttachmentAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

                private TextView description;

                public MyAttachmentAdapterViewHolder(@NonNull View itemView) {
                    super(itemView);

                    description = itemView.findViewById(R.id.textViewDescription);

                    itemView.setOnClickListener(this);
                }

                @SuppressLint("WrongConstant")
                void bind(CompetitionsModel.Result.File file) {
                    description.setText(file.getDescription());
                }

                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    intent.setData(Uri.parse(result.getFiles().get(getLayoutPosition()).getLink()));
                    activity.startActivity(intent);
                }
            }

        }
    }
}
