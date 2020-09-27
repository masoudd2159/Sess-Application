package ir.ac.sku.www.sessapplication.adapters;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import ir.ac.sku.www.sessapplication.R;
import ir.ac.sku.www.sessapplication.models.AssociationModel;

public class AdminAdapter extends RecyclerView.Adapter<AdminAdapter.MyViewHolder> {

    private Context context;
    private List<AssociationModel.Data.Admin> admin;

    public AdminAdapter(Context context, List<AssociationModel.Data.Admin> admin) {
        this.context = context;
        this.admin = admin;
    }

    @NonNull @Override public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_admin, parent, false));

    }

    @Override public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.adminName.setText(admin.get(position).getName());
    }

    @Override public int getItemCount() {
        return admin.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        private TextView adminName;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            adminName = itemView.findViewById(R.id.listItem_TextViewAdmins);

            itemView.setOnClickListener(View -> {
                if (!admin.get(getLayoutPosition()).getTelegramId().equals("")) {
                    Intent intentTelegram = new Intent(Intent.ACTION_VIEW);
                    intentTelegram.setData(Uri.parse(admin.get(getLayoutPosition()).getTelegramId()));
                    context.startActivity(intentTelegram);
                }
            });
        }
    }
}

