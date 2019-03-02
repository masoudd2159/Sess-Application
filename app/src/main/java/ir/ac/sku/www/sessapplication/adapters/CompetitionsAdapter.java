package ir.ac.sku.www.sessapplication.adapters;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import ir.ac.sku.www.sessapplication.R;
import ir.ac.sku.www.sessapplication.models.CompetitionsModel;

public class CompetitionsAdapter extends RecyclerView.Adapter<CompetitionsAdapter.MyViewHolder> {
    private CompetitionsModel competitions;
    private Activity activity;

    public CompetitionsAdapter(@NonNull Activity activity, CompetitionsModel competitionsModel) {
        this.competitions = (competitionsModel == null) ? new CompetitionsModel() : competitionsModel;
        this.activity = activity;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.costum_simple_list, viewGroup, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, int position) {
        myViewHolder.bind(competitions.getResult().get(position));
    }

    @Override
    public int getItemCount() {
        return competitions.getResult().size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private TextView title;

        MyViewHolder(@NonNull View itemView) {
            super(itemView);

            title = itemView.findViewById(R.id.text_id);
            itemView.setOnClickListener(this);
        }

        void bind(CompetitionsModel.Result competitions) {
            title.setText(competitions.getTitle());
        }

        @Override
        public void onClick(View v) {
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(Uri.parse(competitions.getResult().get(getLayoutPosition()).getUrl()));
            activity.startActivity(intent);
        }
    }
}
