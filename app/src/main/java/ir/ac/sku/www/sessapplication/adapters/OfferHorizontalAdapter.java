package ir.ac.sku.www.sessapplication.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import ir.ac.sku.www.sessapplication.R;
import ir.ac.sku.www.sessapplication.model.OfferModel;
import ir.ac.sku.www.sessapplication.utils.Tools;

class OfferHorizontalAdapter extends RecyclerView.Adapter<OfferHorizontalAdapter.MyViewHolder> {

    private final Context context;
    private final List<OfferModel.Result> items;

    OfferHorizontalAdapter(Context context, List<OfferModel.Result> items) {
        this.context = context;
        this.items = (items != null) ? items : new ArrayList<>();
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        return new MyViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_offer_horizontal_item, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.bind(items.get(position));
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    @SuppressLint("NonConstantResourceId")
    class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        @BindView(R.id.image) ImageView image;
        @BindView(R.id.title) TextView title;
        @BindView(R.id.price) TextView price;
        @BindView(R.id.description) TextView description;

        MyViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(this);
        }

        @SuppressLint("SetTextI18n")
        void bind(OfferModel.Result result) {
            title.setText(result.getTitle());
            price.setText(result.getPrice() + " تومان ");
            description.setText(result.getDescription());
            Tools.displayImageOriginal(context, image, getImage(result.getImage()));
        }

        public int getImage(String imageName) {
            return context.getResources().getIdentifier(imageName, "drawable", context.getPackageName());
        }

        @Override
        public void onClick(View v) {
        }
    }
}
