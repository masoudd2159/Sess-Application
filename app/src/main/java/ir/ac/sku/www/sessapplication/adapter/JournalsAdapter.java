package ir.ac.sku.www.sessapplication.adapter;

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
import ir.ac.sku.www.sessapplication.activity.utils.MusicActivity;
import ir.ac.sku.www.sessapplication.activity.utils.ShowPDFActivity;
import ir.ac.sku.www.sessapplication.activity.utils.VideoActivity;
import ir.ac.sku.www.sessapplication.model.JournalModel;

public class JournalsAdapter extends RecyclerView.Adapter<JournalsAdapter.MyViewHolder> {

    private JournalModel journals;
    private Activity activity;
    private String titleName;

    public JournalsAdapter(@NonNull Activity activity, JournalModel journalsModel, String title) {
        this.journals = (journalsModel == null) ? new JournalModel() : journalsModel;
        this.activity = activity;
        this.titleName = title;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.custom_journal_list, viewGroup, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, int position) {
        myViewHolder.bind(journals.getResult().get(position));
    }

    @Override
    public int getItemCount() {
        return journals.getResult().size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {

        private ImageView journalImage;
        private ImageView typeImage;
        private TextView title;
        private TextView version;
        private Dialog dialog;

        MyViewHolder(@NonNull View itemView) {
            super(itemView);

            journalImage = itemView.findViewById(R.id.journalList_JournalImageView);
            typeImage = itemView.findViewById(R.id.journalList_JournalImageViewType);
            title = itemView.findViewById(R.id.journalList_Title);
            version = itemView.findViewById(R.id.journalList_Version);

            itemView.setOnClickListener(this);
            itemView.setOnLongClickListener(this);
        }

        void bind(JournalModel.Result journalsModel) {
            title.setText(journalsModel.getDescription());
            version.setText("شمارگان : " + journalsModel.getVersion());

            switch (journalsModel.getType()) {
                case 1:
                    typeImage.setImageResource(R.drawable.ic_ebook);
                    break;
                case 2:
                    typeImage.setImageResource(R.drawable.ic_music);
                    break;
                case 3:
                    typeImage.setImageResource(R.drawable.ic_video);
                    break;
            }

            Glide.with(activity).load(journalsModel.getPicture()).diskCacheStrategy(DiskCacheStrategy.ALL).into(journalImage);
        }

        @Override
        public void onClick(View v) {

            switch (journals.getResult().get(getLayoutPosition()).getType()) {
                case 1:
                    final Intent intentPDF = new Intent(activity, ShowPDFActivity.class);
                    intentPDF.putExtra("pdf", journals.getResult().get(getLayoutPosition()).getFile());
                    intentPDF.putExtra("PDFName", titleName);
                    activity.startActivity(intentPDF);
                    break;
                case 2:
                    final Intent intentMP3 = new Intent(activity, MusicActivity.class);
                    intentMP3.putExtra("mp3", journals.getResult().get(getLayoutPosition()).getFile());
                    intentMP3.putExtra("mp3Name", journals.getResult().get(getLayoutPosition()).getDescription());
                    activity.startActivity(intentMP3);
                    break;
                case 3:
                    final Intent intentMP4 = new Intent(activity, VideoActivity.class);
                    intentMP4.putExtra("mp4", journals.getResult().get(getLayoutPosition()).getFile());
                    intentMP4.putExtra("mp4Name", journals.getResult().get(getLayoutPosition()).getDescription());
                    activity.startActivity(intentMP4);
                    break;
            }
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

            Glide.with(activity).load(journals.getResult().get(getLayoutPosition()).getPicture()).into(imageView);

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
