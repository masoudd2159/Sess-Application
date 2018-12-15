package ir.ac.sku.www.sessapplication.fragment;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ir.ac.sku.www.sessapplication.R;
import ir.ac.sku.www.sessapplication.activities.SendMessageActivity;
import ir.ac.sku.www.sessapplication.models.MSGMessagesParcelable;
import ir.ac.sku.www.sessapplication.utils.Handler;

public class MessagesFragment extends Fragment {

    private ViewPager viewPager;
    private TabLayout tabLayout;
    private SlidePagerAdapter slidePagerAdapter;
    private View rootView;
    private FragmentActivity fragmentActivity;
    private ProgressDialog progressDialog;

    private FrameLayout fab;
    private ImageButton fabBtn;
    private View fabShadow;

    String[] typeMessage;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.fragment_messages, container, false);
        this.rootView = rootView;

        progressDialog = new ProgressDialog(rootView.getContext());
        progressDialog.setMessage("لطفا منتظر بمانید!");
        progressDialog.setCancelable(false);
        progressDialog.show();

        typeMessage = getResources().getStringArray(R.array.typeMessages);

        init();
        setUpViewPager();

        prepareData(typeMessage[0], "", 0);


        tabLayout.setupWithViewPager(viewPager);
        tabLayout.setTabMode(TabLayout.MODE_FIXED);

        for (int i = 0; i < tabLayout.getTabCount(); i++) {
            TextView textView = (TextView) LayoutInflater.from(rootView.getContext()).inflate(R.layout.custom_tab, null);
            //textView.setTypeface(Typeface);
            tabLayout.getTabAt(i).setCustomView(textView);
        }

        Animation animation = AnimationUtils.loadAnimation(rootView.getContext(), R.anim.simple_grow);
        fab = rootView.findViewById(R.id.addButton_Main);
        fabBtn = rootView.findViewById(R.id.addButton_ImageButtonAdd);
        fabShadow = rootView.findViewById(R.id.addButton_Shadow);

        /*if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            fabShadow.setVisibility(View.GONE);
            fabBtn.setBackground(getDrawable(R.drawable.ripple_accent));
        }*/

        fab.startAnimation(animation);

        fabBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("CLICK", "FAB CLICK");
                rootView.getContext().startActivity(new Intent(rootView.getContext(), SendMessageActivity.class));
            }
        });


        return rootView;
    }

    private void init() {
        viewPager = rootView.findViewById(R.id.fragmentMessage_ViewPager);
        tabLayout = rootView.findViewById(R.id.fragmentMessage_TabLayout);
    }

    private void setUpViewPager() {
        slidePagerAdapter = new SlidePagerAdapter(fragmentActivity.getSupportFragmentManager());
        viewPager.setAdapter(slidePagerAdapter);
    }


    private void prepareData(String type, String ident, final int position) {
        Map<String, String> params = new HashMap<>();
        params.put("type", type);
        params.put("ident", ident);

        MSGMessagesParcelable.fetchFromWeb(rootView.getContext(), (HashMap<String, String>) params, new Handler() {
            @Override
            public void onResponse(boolean ok, Object obj) {
                if (ok) {
                    MSGMessagesParcelable messages = (MSGMessagesParcelable) obj;
                    prepareSlides(messages, position);

                    if (position < 3) {
                        prepareData(typeMessage[position + 1], "", position + 1);
                    } else if (position == 3) {
                        progressDialog.dismiss();
                    }

                }
            }
        });
    }

    private void prepareSlides(MSGMessagesParcelable message, int position) {
        String[] title = getResources().getStringArray(R.array.messagePagerTitle);

        if (message != null) {
            slidePagerAdapter.addFragment(SlideFragmentMessage.newInstance(message), title[position]);
        }

        for (int i = 0; i < tabLayout.getTabCount(); i++) {
            TextView textView = (TextView) LayoutInflater.from(rootView.getContext()).inflate(R.layout.custom_tab, null);
            tabLayout.getTabAt(i).setCustomView(textView);
        }
    }


    public class SlidePagerAdapter extends FragmentPagerAdapter {
        List<Fragment> fragments;
        List<String> tabTitles;


        public SlidePagerAdapter(FragmentManager fm) {
            super(fm);
            fragments = new ArrayList<>();
            tabTitles = new ArrayList<>();
        }

        @Override
        public Fragment getItem(int i) {
            return fragments.get(i);
        }

        @Override
        public int getCount() {
            return fragments.size();
        }

        public void addFragment(Fragment fragment, String tabTitle) {
            fragments.add(fragment);
            tabTitles.add(tabTitle);
            notifyDataSetChanged();
        }

        @Nullable
        @Override
        public CharSequence getPageTitle(int position) {
            return tabTitles.get(position);
        }
    }

    @Override
    public void onAttach(Activity activity) {
        fragmentActivity = (FragmentActivity) activity;
        super.onAttach(activity);
    }
}
