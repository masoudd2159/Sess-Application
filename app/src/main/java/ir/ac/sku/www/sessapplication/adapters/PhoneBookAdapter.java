package ir.ac.sku.www.sessapplication.adapters;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import ir.ac.sku.www.sessapplication.R;
import ir.ac.sku.www.sessapplication.models.PhoneBookModel;

public class PhoneBookAdapter extends RecyclerView.Adapter<PhoneBookAdapter.MyViewHolder> {

    private PhoneBookModel phoneBook;
    private Activity activity;

    public PhoneBookAdapter(@NonNull Activity activity, PhoneBookModel phoneBook) {
        this.phoneBook = (phoneBook == null) ? new PhoneBookModel() : phoneBook;
        this.activity = activity;
    }


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.custom_phone_book, viewGroup, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, int position) {
        myViewHolder.bind(phoneBook.getResult().get(position));
    }

    @Override
    public int getItemCount() {
        return phoneBook.getResult().size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        private TextView name;
        private TextView collage;
        private TextView unit;
        private TextView telephone;
        private RelativeLayout phone;
        private RelativeLayout link;
        private View view;


        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            name = itemView.findViewById(R.id.customPhoneBook_Name);
            collage = itemView.findViewById(R.id.customPhoneBook_Collage);
            unit = itemView.findViewById(R.id.customPhoneBook_Unit);
            phone = itemView.findViewById(R.id.customPhoneBook_Phone);
            telephone = itemView.findViewById(R.id.customPhoneBook_TextViewPhone);
            view = itemView.findViewById(R.id.customPhoneBook_Line);
            link = itemView.findViewById(R.id.customPhoneBook_Link);
        }

        @SuppressLint("SetTextI18n")
        void bind(final PhoneBookModel.Result result) {

            name.setText(result.getName());

            if (!result.getCollege().equals("")) {
                collage.setVisibility(View.VISIBLE);
                collage.setText(" هیئت علمی " + result.getCollege());
            } else {
                collage.setVisibility(View.GONE);
            }

            if (!result.getUnit().equals("")) {
                unit.setVisibility(View.VISIBLE);
                unit.setText(" کارکنان " + result.getUnit());
            } else {
                unit.setVisibility(View.GONE);
            }

            if (!result.getPhone().equals("")) {
                phone.setVisibility(View.VISIBLE);
                view.setVisibility(View.VISIBLE);
                telephone.setText(result.getPhone());
            } else {
                phone.setVisibility(View.GONE);
                view.setVisibility(View.GONE);
            }

            phone.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!result.getPhone().equals("") && !result.getPhone().equals(" ")) {
                        Intent intent = new Intent(Intent.ACTION_VIEW);
                        intent.setData(Uri.parse("tel:" + result.getPhone()));
                        activity.startActivity(intent);
                    } else {
                        Toast.makeText(activity, "شماره ی مورد نظر در سیستم وجود ندارد!", Toast.LENGTH_SHORT).show();
                    }
                }
            });

            link.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if (!result.getLink().equals("") && !result.getLink().equals(" ")) {
                        Intent intent = new Intent(Intent.ACTION_VIEW);
                        intent.setData(Uri.parse(result.getLink()));
                        activity.startActivity(intent);
                    } else {
                        Toast.makeText(activity, "لینک مورد نظر در سیستم وجود ندارد!", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }
}
