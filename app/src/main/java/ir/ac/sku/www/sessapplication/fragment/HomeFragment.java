package ir.ac.sku.www.sessapplication.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import ir.ac.sku.www.sessapplication.R;
import ir.ac.sku.www.sessapplication.activity.about.AboutActivity;
import ir.ac.sku.www.sessapplication.activity.home.CulturalDeputyActivity;
import ir.ac.sku.www.sessapplication.activity.home.EducationalDeputyActivity;
import ir.ac.sku.www.sessapplication.activity.home.LeaderActivity;
import ir.ac.sku.www.sessapplication.activity.home.NewbieActivity;
import ir.ac.sku.www.sessapplication.activity.home.NewsActivity;
import ir.ac.sku.www.sessapplication.activity.home.OfficeDeputyActivity;
import ir.ac.sku.www.sessapplication.activity.home.OnlineShopActivity;
import ir.ac.sku.www.sessapplication.activity.home.PhoneBookActivity;
import ir.ac.sku.www.sessapplication.activity.home.ResearchDeputyActivity;
import ir.ac.sku.www.sessapplication.activity.home.StudentDeputyActivity;
import ir.ac.sku.www.sessapplication.activity.utils.ActivityWebView;
import ir.ac.sku.www.sessapplication.api.ApplicationAPI;
import ir.ac.sku.www.sessapplication.utils.helper.ManagerHelper;

public class HomeFragment extends Fragment {

    private CardView sess;
    private CardView nahad;
    private CardView educational;
    private CardView student;
    private CardView research;
    private CardView cultural;
    private CardView news;
    private CardView support;
    private CardView newbie;
   // private CardView onlineShop;
    private CardView office;
    private CardView phoneBook;

    private View rootView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        this.rootView = view;
        init();

        sess.setOnClickListener(v -> {
            if (ManagerHelper.isInternet(rootView.getContext())) {
                ManagerHelper.noInternetAccess(rootView.getContext());
            } else {
                Intent intent = new Intent(rootView.getContext(), ActivityWebView.class);
                intent.putExtra("key.EXTRA_OBJC", ApplicationAPI.SESS);
                rootView.getContext().startActivity(intent);
            }
        });

        cultural.setOnClickListener(v -> startActivity(new Intent(rootView.getContext(), CulturalDeputyActivity.class)));

        news.setOnClickListener(v -> startActivity(new Intent(rootView.getContext(), NewsActivity.class)));

        phoneBook.setOnClickListener(v -> startActivity(new Intent(rootView.getContext(), PhoneBookActivity.class)));

        educational.setOnClickListener(v -> startActivity(new Intent(rootView.getContext(), EducationalDeputyActivity.class)));

        student.setOnClickListener(v -> startActivity(new Intent(rootView.getContext(), StudentDeputyActivity.class)));

        research.setOnClickListener(v -> startActivity(new Intent(rootView.getContext(), ResearchDeputyActivity.class)));

        office.setOnClickListener(v -> startActivity(new Intent(rootView.getContext(), OfficeDeputyActivity.class)));

        support.setOnClickListener(v -> startActivity(new Intent(rootView.getContext(), AboutActivity.class)));

        newbie.setOnClickListener(v -> startActivity(new Intent(rootView.getContext(), NewbieActivity.class)));

        nahad.setOnClickListener(v -> startActivity(new Intent(rootView.getContext(), LeaderActivity.class)));

      //  onlineShop.setOnClickListener(v -> startActivity(new Intent(rootView.getContext(), OnlineShopActivity.class)));

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
        support = rootView.findViewById(R.id.homeFragment_Support);
        newbie = rootView.findViewById(R.id.homeFragment_Newbie);
        nahad = rootView.findViewById(R.id.homeFragment_Nahad);
       // onlineShop = rootView.findViewById(R.id.homeFragment_online_shop);
    }
}
