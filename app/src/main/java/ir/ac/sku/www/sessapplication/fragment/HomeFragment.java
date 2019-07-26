package ir.ac.sku.www.sessapplication.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import ir.ac.sku.www.sessapplication.API.MyLog;
import ir.ac.sku.www.sessapplication.R;
import ir.ac.sku.www.sessapplication.activities.CulturalDeputyActivity;
import ir.ac.sku.www.sessapplication.activities.LoginActivity;
import ir.ac.sku.www.sessapplication.activities.NewsActivity;
import ir.ac.sku.www.sessapplication.activities.PhoneBookActivity;
import ir.ac.sku.www.sessapplication.activities.fragmentHome.EducationalDeputyActivity;
import ir.ac.sku.www.sessapplication.activities.fragmentHome.OfficeDeputyActivity;
import ir.ac.sku.www.sessapplication.activities.fragmentHome.ResearchDeputyActivity;
import ir.ac.sku.www.sessapplication.activities.fragmentHome.SESSActivity;
import ir.ac.sku.www.sessapplication.activities.fragmentHome.StudentDeputyActivity;
import ir.ac.sku.www.sessapplication.activities.fragmentHome.TelevisionActivity;
import ir.ac.sku.www.sessapplication.utils.HttpManager;

public class HomeFragment extends Fragment {

    private CardView sess;
    private CardView educational;
    private CardView student;
    private CardView research;
    private CardView cultural;
    private CardView news;
    private CardView television;
    private CardView office;
    private CardView phoneBook;
    private CardView lost;
    private CardView market;

    private View rootView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        this.rootView = view;
        init();

        sess.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (HttpManager.isNOTOnline(rootView.getContext())) {
                    HttpManager.noInternetAccess(rootView.getContext());
                } else {
                    startActivity(new Intent(rootView.getContext(), SESSActivity.class));
                }
            }
        });

        cultural.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(rootView.getContext(), CulturalDeputyActivity.class));
            }
        });

        news.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(rootView.getContext(), NewsActivity.class));
            }
        });

        phoneBook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(rootView.getContext(), PhoneBookActivity.class));
            }
        });

        educational.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(rootView.getContext(), EducationalDeputyActivity.class));
            }
        });

        student.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(rootView.getContext(), StudentDeputyActivity.class));
            }
        });

        research.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(rootView.getContext(), ResearchDeputyActivity.class));
            }
        });

        office.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(rootView.getContext(), OfficeDeputyActivity.class));
            }
        });
        television.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(rootView.getContext(), TelevisionActivity.class));
            }
        });

        return view;
    }

    private void init() {
        sess = rootView.findViewById(R.id.homeFragment_SESS);
        cultural = rootView.findViewById(R.id.homeFragment_CulturalDeputy);
        news = rootView.findViewById(R.id.homeFragment_News);
        phoneBook = rootView.findViewById(R.id.homeFragment_PhoneBook);
        educational = rootView.findViewById(R.id.homeFragment_EducationalDeputy);
        student = rootView.findViewById(R.id.homeFragment_StudentDeputy);
        research = rootView.findViewById(R.id.homeFragment_ResearchDeputy);
        office = rootView.findViewById(R.id.homeFragment_OfficeDeputy);
        television = rootView.findViewById(R.id.homeFragment_Television);
    }
}
