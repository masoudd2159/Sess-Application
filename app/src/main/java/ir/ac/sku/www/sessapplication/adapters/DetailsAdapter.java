package ir.ac.sku.www.sessapplication.adapters;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import ir.ac.sku.www.sessapplication.R;
import ir.ac.sku.www.sessapplication.activity.home.ChannelActivity;
import ir.ac.sku.www.sessapplication.activity.home.gallery.AlbumsActivity;
import ir.ac.sku.www.sessapplication.activity.utils.ActivityWebView;
import ir.ac.sku.www.sessapplication.activity.utils.ParsMapActivity;
import ir.ac.sku.www.sessapplication.activity.utils.ShowPDFActivity;
import ir.ac.sku.www.sessapplication.model.DetailsModel;

public class DetailsAdapter extends RecyclerView.Adapter<DetailsAdapter.MyViewHolder> {

    private Context context;
    private List<DetailsModel> detailsModels;

    public DetailsAdapter(Context context, List<DetailsModel> detailsModels) {
        this.detailsModels = detailsModels;
        this.context = context;
    }

    @NonNull @Override public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        return new MyViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.custom_areas_list, viewGroup, false));
    }

    @Override public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, int position) {
        myViewHolder.persianTitle.setText(detailsModels.get(position).getTitle());
        Log.i("Masoud", "i : " + position);
        Log.i("Masoud", "size : " + detailsModels.size());
    }

    @Override public int getItemCount() {
        return detailsModels.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        @BindView(R.id.customAreasListView_TextViewPersianTitle) TextView persianTitle;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

            itemView.setOnClickListener(this);
        }

        @Override public void onClick(View view) {
            if (detailsModels.get(getLayoutPosition()).getId() == 3) {
                Intent intentMap = new Intent(context, ParsMapActivity.class);
                context.startActivity(intentMap);
            } else if (detailsModels.get(getLayoutPosition()).getId() == 4) {
                Intent intentVideo = new Intent(context, ChannelActivity.class);
                intentVideo.putExtra("URL", detailsModels.get(getLayoutPosition()).getUrl());
                context.startActivity(intentVideo);
            } else if (detailsModels.get(getLayoutPosition()).getId() == 5) {
                Intent intentAlbum = new Intent(context, AlbumsActivity.class);
                context.startActivity(intentAlbum);
            } else if (detailsModels.get(getLayoutPosition()).getId() == 49) {
                Intent intentVideoBus = new Intent(context, ChannelActivity.class);
                intentVideoBus.putExtra("URL", detailsModels.get(getLayoutPosition()).getUrl());
                context.startActivity(intentVideoBus);
            } else if (detailsModels.get(getLayoutPosition()).getId() == 47) {
                Intent intentPDF = new Intent(context, ShowPDFActivity.class);
                intentPDF.putExtra("pdf", detailsModels.get(getLayoutPosition()).getUrl());
                intentPDF.putExtra("PDFName", "میثاق نامه دانشجویان");
                context.startActivity(intentPDF);
            } else {
                Intent intent = new Intent(context, ActivityWebView.class);
                intent.putExtra("key.EXTRA_OBJC", detailsModels.get(getLayoutPosition()).getUrl());
                context.startActivity(intent);
            }
        }
    }
}
