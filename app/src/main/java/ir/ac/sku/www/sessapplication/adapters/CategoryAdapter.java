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

import butterknife.BindView;
import butterknife.ButterKnife;
import ir.ac.sku.www.sessapplication.R;
import ir.ac.sku.www.sessapplication.model.CategoryModel;
import ir.ac.sku.www.sessapplication.utils.Tools;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.MyViewHolder> {
    private Context context;
    private List<CategoryModel> items;

    public CategoryAdapter(Context context, List<CategoryModel> categoryModels) {
        this.context = context;
        this.items = categoryModels;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(context).inflate(R.layout.item_category, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        if (items.get(position).getIcon() != null) {
            holder.icon.setVisibility(View.VISIBLE);
            Tools.displayImageOriginal(context, holder.icon, getImage(items.get(position).getIcon()));
        }

        if (items.get(position).getTitle() != null) {
            holder.categoryName.setText(items.get(position).getTitle());
        }

        if (items.get(position).isChild()) {
            holder.subCategory.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public int getImage(String imageName) {
        return context.getResources().getIdentifier(imageName, "drawable", context.getPackageName());
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.imageView_icon) ImageView icon;
        @BindView(R.id.imageView_subCategoty) ImageView subCategory;
        @BindView(R.id.textView_category_name) TextView categoryName;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
