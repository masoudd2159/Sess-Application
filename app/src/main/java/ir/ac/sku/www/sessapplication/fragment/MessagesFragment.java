package ir.ac.sku.www.sessapplication.fragment;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.airbnb.lottie.LottieAnimationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ir.ac.sku.www.sessapplication.api.MyLog;
import ir.ac.sku.www.sessapplication.R;
import ir.ac.sku.www.sessapplication.activity.messages.SendMessageActivity;
import ir.ac.sku.www.sessapplication.model.GetInfoForSend;
import ir.ac.sku.www.sessapplication.model.MSGMessagesParcelable;
import ir.ac.sku.www.sessapplication.utils.MyHandler;
import ir.ac.sku.www.sessapplication.utils.HttpManager;

public class MessagesFragment extends Fragment {


    private SlidePagerAdapter slidePagerAdapter;
    private FragmentActivity fragmentActivity;
    private ProgressDialog progressDialog;
    private View rootView;

    private ViewPager viewPager;
    private TabLayout tabLayout;

    private FloatingActionButton floatingActionButton;

    private Dialog dialog;

    private LottieAnimationView gifImageViewEnter;
    private EditText studentNumber;
    private ImageView close;
    private Button enter;
    private EditText id;

    private GetInfoForSend getInfoForSend;

    String[] typeMessage;

    @SuppressLint("LongLogTag")
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.i(MyLog.MESSAGE, "Fragment Message Created");
        final View rootView = inflater.inflate(R.layout.fragment_messages, container, false);
        this.rootView = rootView;
        getInfoForSend = new GetInfoForSend();

        progressDialog = new ProgressDialog(rootView.getContext());
        progressDialog.setMessage("لطفا منتظر بمانید!");
        progressDialog.setCancelable(false);
        progressDialog.setButton(DialogInterface.BUTTON_NEGATIVE, "انصراف", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        progressDialog.show();

        typeMessage = getResources().getStringArray(R.array.typeMessages);

        init();
        setUpViewPager();

        tabLayout.setupWithViewPager(viewPager);
        tabLayout.setTabMode(TabLayout.MODE_SCROLLABLE);

        prepareData(typeMessage[0], 0);

        Animation animation = AnimationUtils.loadAnimation(rootView.getContext(), R.anim.simple_grow);

        floatingActionButton = rootView.findViewById(R.id.fragmentMessage_FloatingActionButton);
        floatingActionButton.startAnimation(animation);
        if (floatingActionButton != null) {
            floatingActionButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.i(MyLog.MESSAGE, "on fab Button Clicked");
                    showSendMessageDialog();
                }
            });
        }

        Drawable background = viewPager.getBackground();
        background.setAlpha(70);


        return rootView;
    }

    private void init() {
        viewPager = rootView.findViewById(R.id.fragmentMessage_ViewPager);
        tabLayout = rootView.findViewById(R.id.fragmentMessage_TabLayout);
    }

    @SuppressLint("LongLogTag")
    private void setUpViewPager() {
        Log.i(MyLog.MESSAGE, "setUpViewPager");
        slidePagerAdapter = new SlidePagerAdapter(getChildFragmentManager());
        viewPager.setAdapter(slidePagerAdapter);
    }

    @SuppressLint("LongLogTag")
    private void prepareData(String type, final int position) {
        Map<String, String> params = new HashMap<>();
        params.put("type", type);

        Log.i(MyLog.MESSAGE, type);

        MSGMessagesParcelable.fetchFromWeb(rootView.getContext(), (HashMap<String, String>) params, new MyHandler() {
            @Override
            public void onResponse(boolean ok, Object obj) {
                if (ok) {
                    MSGMessagesParcelable messages = (MSGMessagesParcelable) obj;
                    Log.i(MyLog.MESSAGE, "1- prepareData : " + String.valueOf(messages.getResult().getMessages().size()));

                    prepareSlides(messages, position);
                } else {
                    progressDialog.dismiss();
                }
            }
        });
    }

    @SuppressLint("LongLogTag")
    private void prepareSlides(MSGMessagesParcelable message, int position) {
        Log.i(MyLog.MESSAGE, "prepareSlides Run");
        String[] title = getResources().getStringArray(R.array.messagePagerTitle);

        if (message != null) {
            Log.i(MyLog.MESSAGE, "2- prepareSlides : " + String.valueOf(message.getResult().getMessages().size()));
            slidePagerAdapter.addFragment(SlideFragmentMessage.newInstance(message), title[position]);
        }

        for (int i = 0; i < tabLayout.getTabCount(); i++) {
            TextView textView = (TextView) LayoutInflater.from(rootView.getContext()).inflate(R.layout.custom_tab, null);
            tabLayout.getTabAt(i).setCustomView(textView);
        }

        if (position < 4) {
            prepareData(typeMessage[position + 1], position + 1);
        } else if (position == 4) {
            progressDialog.dismiss();
        }
    }

    private void showSendMessageDialog() {
        dialog = new Dialog(rootView.getContext());
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        WindowManager.LayoutParams layoutParams = dialog.getWindow().getAttributes();
        layoutParams.dimAmount = 0.7f;
        dialog.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.dialog_send_message);

        gifImageViewEnter = dialog.findViewById(R.id.dialogSendMessage_GifImageViewEnter);
        studentNumber = dialog.findViewById(R.id.dialogSendMessage_EditTextStudentNumber);
        id = dialog.findViewById(R.id.dialogSendMessage_EditTextID);
        close = dialog.findViewById(R.id.dialogSendMessage_Close);
        enter = dialog.findViewById(R.id.dialogSendMessage_Enter);

        enter.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("LongLogTag")
            @Override
            public void onClick(View v) {
                enter.setClickable(false);
                Log.i(MyLog.MESSAGE, "Click on Enter Button");
                if (HttpManager.isNOTOnline(rootView.getContext())) {
                    Log.i(MyLog.MESSAGE, "OFFLine");
                    HttpManager.noInternetAccess(rootView.getContext());
                } else {
                    Log.i(MyLog.MESSAGE, "OnLine");
                    enter.setVisibility(View.INVISIBLE);
                    gifImageViewEnter.setVisibility(View.VISIBLE);
                    gifImageViewEnter.setAnimation("loading_1.json");
                    gifImageViewEnter.playAnimation();
                    gifImageViewEnter.loop(true);

                    infoForSend(id.getText().toString().trim(), studentNumber.getText().toString().trim());
                }
            }
        });

        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                enter.setClickable(true);
            }
        });

        dialog.show();
    }

    private void infoForSend(final String ID, final String StudentNumber) {
        HashMap<String, String> params = new HashMap<>();
        params.put("id", ID);
        params.put("stNumber", StudentNumber);

        GetInfoForSend.fetchFromWeb(rootView.getContext(), params, new MyHandler() {
            @Override
            public void onResponse(boolean ok, Object obj) {

                enter.setClickable(true);

                if (ok) {
                    getInfoForSend = (GetInfoForSend) obj;

                    Intent intent = new Intent(rootView.getContext(), SendMessageActivity.class);
                    intent.putExtra("GetInfoForSend", (Parcelable) getInfoForSend);
                    intent.putExtra("id", ID);
                    intent.putExtra("studentNumber", StudentNumber);
                    rootView.getContext().startActivity(intent);

                    dialog.dismiss();
                } else {
                    id.setText("");
                    studentNumber.setText("");
                    enter.setVisibility(View.VISIBLE);
                    gifImageViewEnter.setVisibility(View.INVISIBLE);
                }
            }
        });
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
