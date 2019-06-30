package ir.ac.sku.www.sessapplication.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import ir.ac.sku.www.sessapplication.R;
import ir.ac.sku.www.sessapplication.activities.ShowStudentRequestActivity;
import ir.ac.sku.www.sessapplication.models.StudentRequests;

public class ProcessesFragmentAdapter extends RecyclerView.Adapter<ProcessesFragmentAdapter.MyViewHolder> {

    private List<StudentRequests.Result.Request> requests;

    public ProcessesFragmentAdapter(List<StudentRequests.Result.Request> requests) {
        this.requests = (requests == null) ? new ArrayList<StudentRequests.Result.Request>() : requests;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.custom_process, viewGroup, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, int position) {
        myViewHolder.bind(requests.get(position));
    }

    @Override
    public int getItemCount() {
        return requests.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private final Context context;
        private TextView request;
        private TextView status;
        private TextView step;
        private TextView employee;


        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            context = itemView.getContext();

            request = itemView.findViewById(R.id.customProcess_TextViewRequest);
            status = itemView.findViewById(R.id.customProcess_TextViewStatus);
            step = itemView.findViewById(R.id.customProcess_TextViewStep);
            employee = itemView.findViewById(R.id.customProcess_TextViewEmployee);

            itemView.setOnClickListener(this);
        }

        @SuppressLint("SetTextI18n")
        public void bind(StudentRequests.Result.Request myRequest) {
            request.setText("درخواست " + myRequest.getRequest());
            status.setText("وضعیت :‌ " + myRequest.getStatus());
            step.setText("گام :‌ " + myRequest.getStep());
            employee.setText("کارفرما : " + myRequest.getEmployee());
        }

        @Override
        public void onClick(View v) {
            final Intent intent = new Intent(context, ShowStudentRequestActivity.class);
            intent.putExtra("ident", requests.get(getLayoutPosition()).getIdent());
            context.startActivity(intent);
        }
    }
}
