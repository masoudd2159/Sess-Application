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
import ir.ac.sku.www.sessapplication.activities.CompetitionsActivity;
import ir.ac.sku.www.sessapplication.activities.JournalsActivity;
import ir.ac.sku.www.sessapplication.activities.NewsActivity;

public class HomeFragment extends Fragment {

    private CardView journals;
    private CardView news;
    private CardView competitions;
    private View rootView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        this.rootView = view;
        init();

        journals.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(rootView.getContext(), JournalsActivity.class));
            }
        });

        news.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(rootView.getContext(), NewsActivity.class));
            }
        });

        competitions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(rootView.getContext(), CompetitionsActivity.class));
            }
        });

        return view;
    }

    private void init() {
        journals = rootView.findViewById(R.id.homeFragment_Journals);
        news = rootView.findViewById(R.id.homeFragment_News);
        competitions = rootView.findViewById(R.id.homeFragment_Competitions);
    }
}
