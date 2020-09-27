package ir.ac.sku.www.sessapplication.adapters;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import ir.ac.sku.www.sessapplication.R;
import ir.ac.sku.www.sessapplication.models.DepartmentsModel;

public class DepartmentsAdapter extends RecyclerView.Adapter<DepartmentsAdapter.MyViewHolder> {

    private Context context;
    private List<DepartmentsModel> departmentsModel;
    private int index = -1;

    public DepartmentsAdapter(Context context, List<DepartmentsModel> departmentsModel) {
        this.context = context;
        this.departmentsModel = departmentsModel;
    }

    @NonNull @Override public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        return new MyViewHolder(LayoutInflater.from(context).inflate(R.layout.custom_department_view, viewGroup, false));
    }

    @Override public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, int position) {
        myViewHolder.bind(departmentsModel.get(position));
    }

    @Override public int getItemCount() {
        return departmentsModel.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.customDepartmentView_TextView) TextView departmentTitle;
        @BindView(R.id.customDepartmentView_ImageView) ImageView departmentImage;
        @BindView(R.id.customDepartmentView_LinearLayout) LinearLayout linearLayout;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void bind(DepartmentsModel departmentsModel) {
            departmentTitle.setText(departmentsModel.getTitle());
            Glide.with(context).load(getImage(departmentsModel.getImage())).diskCacheStrategy(DiskCacheStrategy.ALL).into(departmentImage);
        }

        public int getImage(String imageName) {
            return context.getResources().getIdentifier(imageName, "drawable", context.getPackageName());
        }

        public void setBackground() {
            linearLayout.setBackgroundResource(R.drawable.bg_department_selected);
        }

        public void resetBackground() {
            linearLayout.setBackgroundColor(Color.TRANSPARENT);
        }
    }
}
