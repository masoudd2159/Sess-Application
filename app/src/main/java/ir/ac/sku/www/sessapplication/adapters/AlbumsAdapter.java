package ir.ac.sku.www.sessapplication.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import ir.ac.sku.www.sessapplication.R;
import ir.ac.sku.www.sessapplication.api.MyLog;
import ir.ac.sku.www.sessapplication.model.AlbumModel;
import ir.ac.sku.www.sessapplication.utils.Tools;

public class AlbumsAdapter extends RecyclerView.Adapter<AlbumsAdapter.MyViewHolder> {

    private final List<AlbumModel> items;
    private ItemClickListener mClickListener;
    private Context context;

    public AlbumsAdapter(Context context, List<AlbumModel> items) {
        this.context = context;
        this.items = items;
    }

    @NonNull @Override public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_album, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Log.i(MyLog.SESS, String.valueOf(items.get(position).getSection()));
        holder.title.setText(items.get(position).getSection());
        Tools.displayImageOriginal(
                context,
                holder.imageAlbum,
                "https://app.sku.ac.ir/SkuPic/" + items.get(position).getPictures().get(0).getImage());
    }

    @Override
    public int getItemCount() {
        Log.i(MyLog.SESS, String.valueOf(items.size()));
        return items.size();
    }

    public void setClickListener(ItemClickListener itemClickListener) {
        this.mClickListener = itemClickListener;
    }

    public interface ItemClickListener {
        void onItemClick(View view, int position);
    }

    @SuppressLint("NonConstantResourceId")
    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        @BindView(R.id.image) ImageView imageAlbum;
        @BindView(R.id.title) TextView title;

        MyViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (mClickListener != null) mClickListener.onItemClick(view, getAdapterPosition());
        }
    }
}
