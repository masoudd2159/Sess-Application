package ir.ac.sku.www.sessapplication.adapters;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.util.Objects;

import ir.ac.sku.www.sessapplication.R;
import ir.ac.sku.www.sessapplication.activities.ShowPDFActivity;
import ir.ac.sku.www.sessapplication.models.JournalModel;
import ir.ac.sku.www.sessapplication.models.TotalJournalsModel;
import pl.droidsonroids.gif.GifImageView;

import android.content.Intent;

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
        private TextView title;
        private TextView version;
        private GifImageView gifImageView;
        private Dialog dialog;

        MyViewHolder(@NonNull View itemView) {
            super(itemView);

            journalImage = itemView.findViewById(R.id.journalList_JournalImageView);
            title = itemView.findViewById(R.id.journalList_Title);
            version = itemView.findViewById(R.id.journalList_Version);
            gifImageView = itemView.findViewById(R.id.journalList_GifImageView);

            itemView.setOnClickListener(this);
            itemView.setOnLongClickListener(this);
        }

        void bind(JournalModel.Result journalsModel) {
            title.setText(journalsModel.getDescription());
            version.setText("شمارگان : " + journalsModel.getVersion());

            Glide.with(activity).load(journalsModel.getPicture()).diskCacheStrategy(DiskCacheStrategy.ALL).into(journalImage);

            gifImageView.setVisibility(View.INVISIBLE);
            journalImage.setVisibility(View.VISIBLE);
        }

        @Override
        public void onClick(View v) {
            final Intent intent = new Intent(activity, ShowPDFActivity.class);
            intent.putExtra("pdf", journals.getResult().get(getLayoutPosition()).getFile());
            intent.putExtra("PDFName", titleName);
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
