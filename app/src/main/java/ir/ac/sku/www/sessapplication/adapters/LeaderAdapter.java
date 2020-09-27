package ir.ac.sku.www.sessapplication.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.util.List;

import ir.ac.sku.www.sessapplication.R;
import ir.ac.sku.www.sessapplication.models.LeaderModel;

public class LeaderAdapter extends RecyclerView.Adapter<LeaderAdapter.MyViewHolder> {

    private Context context;
    private List<LeaderModel> models;
    private OnItemClickListener mOnItemClickListener;

    public LeaderAdapter(Context context, List<LeaderModel> models) {
        this.context = context;
        this.models = models;
    }

    public void setOnItemClickListener(final OnItemClickListener mItemClickListener) {
        this.mOnItemClickListener = mItemClickListener;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_leader, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        if (!models.get(position).getLogo().equals("")) {
            Glide
                    .with(context)
                    .load("https://app.sku.ac.ir/Logo/" + models.get(position).getLogo())
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(holder.image);
        }

        if (!(models.get(position).getFaName().equals(""))) {
            holder.title.setText(models.get(position).getFaName());
        }

        if (!(models.get(position).getDescription().equals(""))) {
            holder.description.setText(models.get(position).getDescription());
        }

        holder.lyt_parent.setOnClickListener(view -> {
            if (mOnItemClickListener == null) return;
            mOnItemClickListener.onItemClick(view, models.get(position), position);
        });
    }

    @Override
    public int getItemCount() {
        return models.size();
    }

    public interface OnItemClickListener {
        void onItemClick(View view, LeaderModel obj, int position);
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        public ImageView image;
        public TextView title;
        public TextView description;
        public View lyt_parent;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            image = itemView.findViewById(R.id.itemLeader_ImageView);
            title = itemView.findViewById(R.id.itemLeader_Title);
            description = itemView.findViewById(R.id.itemLeader_Description);
            lyt_parent = itemView.findViewById(R.id.itemLeader_LayoutParent);
        }
    }
}
