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

import ir.ac.sku.www.sessapplication.R;
import ir.ac.sku.www.sessapplication.models.StudentRequestDetails;

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

        @SuppressLint("SetTextI18n")
        public void bind(StudentRequestDetails.Result.Step myRequest) {
            switch (myRequest.getStatus()) {
                case "pass":
                    status.setImageResource(R.drawable.ic_check);
                    break;
                case "inProcess":
                    status.setImageResource(R.drawable.ic_pending);
                    break;
                case "notPass":
                    status.setImageResource(R.drawable.ic_close_red);
                    break;
            }
            step.setText(myRequest.getName());
            title.setText("عنوان  : " + myRequest.getTitle());
            user.setText(myRequest.getUser());
        }
    }
}
