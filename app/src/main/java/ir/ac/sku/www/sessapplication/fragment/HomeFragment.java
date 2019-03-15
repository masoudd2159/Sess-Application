package ir.ac.sku.www.sessapplication.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import ir.ac.sku.www.sessapplication.R;
import ir.ac.sku.www.sessapplication.activities.CulturalDeputyActivity;
import ir.ac.sku.www.sessapplication.activities.NewsActivity;
import ir.ac.sku.www.sessapplication.activities.PhoneBookActivity;
import ir.ac.sku.www.sessapplication.activities.fragmentHome.EducationalDeputyActivity;
import ir.ac.sku.www.sessapplication.activities.fragmentHome.OfficeDeputyActivity;
import ir.ac.sku.www.sessapplication.activities.fragmentHome.ResearchDeputyActivity;
import ir.ac.sku.www.sessapplication.activities.fragmentHome.StudentDeputyActivity;

public class HomeFragment extends Fragment {

    private CardView educational;
    private CardView student;
    private CardView cultural;
    private CardView research;
    private CardView office;
    private CardView news;
    private CardView phoneBook;

    private View rootView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        this.rootView = view;
        init();

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

        return view;
    }

    private void init() {
        cultural = rootView.findViewById(R.id.homeFragment_CulturalDeputy);
        news = rootView.findViewById(R.id.homeFragment_News);
        phoneBook = rootView.findViewById(R.id.homeFragment_PhoneBook);
        educational = rootView.findViewById(R.id.homeFragment_EducationalDeputy);
        student = rootView.findViewById(R.id.homeFragment_StudentDeputy);
        research = rootView.findViewById(R.id.homeFragment_ResearchDeputy);
        office = rootView.findViewById(R.id.homeFragment_OfficeDeputy);
    }
}
