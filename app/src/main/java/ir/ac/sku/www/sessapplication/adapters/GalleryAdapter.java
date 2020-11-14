package ir.ac.sku.www.sessapplication.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import butterknife.BindView;
import butterknife.ButterKnife;
import ir.ac.sku.www.sessapplication.R;
import ir.ac.sku.www.sessapplication.model.AlbumModel;
import ir.ac.sku.www.sessapplication.utils.Tools;

public class GalleryAdapter extends RecyclerView.Adapter<GalleryAdapter.MyViewHolder> {
    private final Context context;
    private final AlbumModel items;
    private ItemClickListener mClickListener;

    public GalleryAdapter(Context context, AlbumModel albumModel) {
        this.context = context;
        this.items = albumModel;
    }

    @NonNull @Override public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_gallery, parent, false));
    }

    @Override public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Tools.displayImageOriginal(
                context,
                holder.imageAlbum,
                "https://app.sku.ac.ir/SkuPic/" + items.getPictures().get(position).getImage());
    }

    @Override public int getItemCount() {
        return items.getPictures().size();
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
