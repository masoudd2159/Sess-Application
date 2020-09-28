package ir.ac.sku.www.sessapplication.adapter;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import ir.ac.sku.www.sessapplication.api.MyLog;
import ir.ac.sku.www.sessapplication.R;
import ir.ac.sku.www.sessapplication.activity.home.JournalsActivity;
import ir.ac.sku.www.sessapplication.model.TotalJournalsModel;

public class JournalTotalAdapter extends RecyclerView.Adapter<JournalTotalAdapter.MyViewHolder> {

    private TotalJournalsModel journals;
    private Activity activity;

    public JournalTotalAdapter(@NonNull Activity activity, TotalJournalsModel totalJournalsModel) {
        this.journals = (totalJournalsModel == null) ? new TotalJournalsModel() : totalJournalsModel;
        this.activity = activity;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.custom_journal_total_list, viewGroup, false);
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
        private NumberPicker years;
        private Dialog dialog;

        MyViewHolder(@NonNull View itemView) {
            super(itemView);

            journalImage = itemView.findViewById(R.id.journalTotalList_JournalImageView);
            title = itemView.findViewById(R.id.journalTotalList_Title);
            years = itemView.findViewById(R.id.journalTotalList_PickerYears);

            itemView.setOnClickListener(this);
            itemView.setOnLongClickListener(this);
        }

        void bind(TotalJournalsModel.Result journalsTotalModel) {

            if (!journalsTotalModel.getYear().isEmpty()) {
                String[] valueType = new String[journalsTotalModel.getYear().size()];
                for (int i = 0; i < journalsTotalModel.getYear().size(); i++) {
                    valueType[i] = journalsTotalModel.getYear().get(i);
                }
                years.setMinValue(0);
                years.setMaxValue(0);
                try {
                    years.setDisplayedValues(valueType);
                } catch (Exception e) {
                    Log.e(MyLog.SESS, "years occurs Error");
                }
                years.setMaxValue(valueType.length - 1);

            } else {
                String[] valueType = {"0"};
                years.setMaxValue(valueType.length - 1);
                years.setMinValue(0);
                years.setDisplayedValues(valueType);
            }

            title.setText(journalsTotalModel.getTitle());

            Glide.with(activity).load(journalsTotalModel.getPicture()).diskCacheStrategy(DiskCacheStrategy.ALL).into(journalImage);
        }

        @Override
        public void onClick(View v) {
            if (!journals.getResult().get(getLayoutPosition()).getYear().isEmpty()) {
                final Intent intent = new Intent(activity, JournalsActivity.class);
                intent.putExtra("id", journals.getResult().get(getLayoutPosition()).getId());
                intent.putExtra("year", journals.getResult().get(getLayoutPosition()).getYear().get(years.getValue()));
                intent.putExtra("title", journals.getResult().get(getLayoutPosition()).getTitle());
                activity.startActivity(intent);
            } else {
                Toast.makeText(activity, "محتوای مورد نظر در دسترس نیست!", Toast.LENGTH_SHORT).show();
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
