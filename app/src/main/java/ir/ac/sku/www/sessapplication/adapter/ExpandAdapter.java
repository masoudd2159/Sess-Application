package ir.ac.sku.www.sessapplication.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import ir.ac.sku.www.sessapplication.R;
import ir.ac.sku.www.sessapplication.fragment.AssociationDialogFragment;
import ir.ac.sku.www.sessapplication.model.AssociationModel;

public class ExpandAdapter extends RecyclerView.Adapter<ExpandAdapter.MyViewHolder> {
    private Context context;
    private List<AssociationModel.Data> model;

    public ExpandAdapter(Context context, List<AssociationModel.Data> model) {
        this.model = model;
        this.context = context;
    }

    @NonNull @Override public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_expanded_list, parent, false));
    }

    @Override public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.title.setText(model.get(position).getFaName());
        holder.icMore.setOnClickListener(view -> {
            FragmentManager fragmentManager = ((FragmentActivity) context).getSupportFragmentManager();
            AssociationDialogFragment fragment = new AssociationDialogFragment(model.get(position));
            fragment.show(fragmentManager, "addUserPersonalInfo");
        });
    }

    @Override public int getItemCount() {
        return model.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        private TextView title;
        private ImageButton icMore;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.TextViewTitleItemExpand);
            icMore = itemView.findViewById(R.id.ImageButtonItemExpand);

        }
    }
}
