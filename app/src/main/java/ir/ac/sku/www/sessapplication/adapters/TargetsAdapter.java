package ir.ac.sku.www.sessapplication.adapters;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import ir.ac.sku.www.sessapplication.R;
import ir.ac.sku.www.sessapplication.models.GetInfoForSend;

public class TargetsAdapter extends RecyclerView.Adapter<TargetsAdapter.MyViewHolder> {
    private GetInfoForSend getInfoForSend;
    private Activity activity;

    public TargetsAdapter(@NonNull Activity activity, GetInfoForSend getInfoForSend) {
        this.getInfoForSend = (getInfoForSend == null) ? new GetInfoForSend() : getInfoForSend;
        this.activity = activity;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.custom_targets, viewGroup, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, int position) {
        myViewHolder.bind(getInfoForSend.getResult().getTarget().get(position));
    }

    @Override
    public int getItemCount() {
        return getInfoForSend.getResult().getTarget().size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        private TextView target;

        MyViewHolder(@NonNull View itemView) {
            super(itemView);

            target = itemView.findViewById(R.id.customTargets_TextView);
        }

        @SuppressLint("SetTextI18n")
        void bind(GetInfoForSend.Result.Target targets) {
            target.setText(targets.getName() + " (" + targets.getPosition() + ")");
        }
    }
}
