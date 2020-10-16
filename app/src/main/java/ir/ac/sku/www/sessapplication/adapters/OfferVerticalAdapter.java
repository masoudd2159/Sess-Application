package ir.ac.sku.www.sessapplication.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import ir.ac.sku.www.sessapplication.R;
import ir.ac.sku.www.sessapplication.model.OfferModel;


public class OfferVerticalAdapter extends RecyclerView.Adapter<OfferVerticalAdapter.MyViewHolder> {

    private final Context context;
    private final List<OfferModel> items;

    public OfferVerticalAdapter(Context context, List<OfferModel> offerModel) {
        this.context = context;
        this.items = offerModel;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        return new MyViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_offer_vertical_list, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.categoryTitle.setText(items.get(position).getSection());
        setUpRecyclerView(items.get(position).getResult(), holder);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    private void setUpRecyclerView(List<OfferModel.Result> results, MyViewHolder holder) {
        holder.recyclerViewHorizontal.setLayoutAnimation(AnimationUtils.loadLayoutAnimation(context, R.anim.layout_animation_from_right));
        holder.recyclerViewHorizontal.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false));
        holder.recyclerViewHorizontal.setAdapter(new OfferHorizontalAdapter(context, results));
    }

    @SuppressLint("NonConstantResourceId")
    static class MyViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.category_title) TextView categoryTitle;
        @BindView(R.id.recyclerView) RecyclerView recyclerViewHorizontal;

        MyViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
