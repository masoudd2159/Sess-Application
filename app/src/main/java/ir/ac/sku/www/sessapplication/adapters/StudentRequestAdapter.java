package ir.ac.sku.www.sessapplication.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

import ir.ac.sku.www.sessapplication.R;
import ir.ac.sku.www.sessapplication.activities.ShowStudentRequestActivity;
import ir.ac.sku.www.sessapplication.models.StudentRequestDetails;
import ir.ac.sku.www.sessapplication.models.StudentRequests;

public class StudentRequestAdapter extends RecyclerView.Adapter<StudentRequestAdapter.MyViewHolder> {

    private StudentRequestDetails requests;

    public StudentRequestAdapter(StudentRequestDetails requests) {
        this.requests = (requests == null) ? new StudentRequestDetails() : requests;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.custom_show_student_request, viewGroup, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, int position) {
        myViewHolder.bind(requests.getResult().getSteps().get(position));
    }

    @Override
    public int getItemCount() {
        return requests.getResult().getSteps().size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        private final Context context;
        private ImageView status;
        private TextView step;
        private TextView title;
        private TextView user;


        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            context = itemView.getContext();

            status = itemView.findViewById(R.id.customShowStudentRequest_ImageViewStatus);
            step = itemView.findViewById(R.id.customShowStudentRequest_TextViewStep);
            title = itemView.findViewById(R.id.customShowStudentRequest_TextViewTitle);
            user = itemView.findViewById(R.id.customShowStudentRequest_TextViewUser);
        }

        public void bind(StudentRequestDetails.Result.Step myRequest) {
            if (myRequest.getStatus().equals("pass")) {
                status.setImageResource(R.drawable.ic_check);
            } else if (myRequest.getStatus().equals("inProcess")) {
                status.setImageResource(R.drawable.ic_pending);
            } else if (myRequest.getStatus().equals("notPass")) {
                status.setImageResource(R.drawable.ic_close_red);
            }
            step.setText(myRequest.getName());
            title.setText("عنوان  : " + myRequest.getTitle());
            user.setText(myRequest.getUser());
        }
    }
}
