package ir.ac.sku.www.sessapplication.fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;

import ir.ac.sku.www.sessapplication.API.MyLog;
import ir.ac.sku.www.sessapplication.R;
import ir.ac.sku.www.sessapplication.adapters.MessageSliderAdapter;
import ir.ac.sku.www.sessapplication.models.MSGMessagesParcelable;

public class SlideFragmentMessage extends Fragment {

    private RecyclerView recyclerView;
    private MessageSliderAdapter adapter;
    private View rootView;


    @SuppressLint("LongLogTag")
    public static SlideFragmentMessage newInstance(MSGMessagesParcelable msgMessagesParcelable) {
        SlideFragmentMessage fragment = new SlideFragmentMessage();
        Bundle args = new Bundle();

        args.putParcelable("MSGMessagesParcelable", (Parcelable) msgMessagesParcelable);
        Log.i(MyLog.MESSAGE, "SlideFragmentMessage:newInstance = arguments putted");

        fragment.setArguments(args);
        return fragment;
    }

    @SuppressLint("LongLogTag")
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @SuppressLint("LongLogTag")
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.slide_fragment_message, container, false);
        this.rootView = rootView;

        Bundle arguments = getArguments();
        if (arguments != null) {
            MSGMessagesParcelable messages = arguments.getParcelable("MSGMessagesParcelable");
            Log.i(MyLog.MESSAGE, "2- onCreate : " + String.valueOf(messages.getResult().getMessages().size()));

            recyclerView = rootView.findViewById(R.id.slideFragmentMessage_RecyclerView);
            showData(messages);
        }
        return rootView;
    }

    private void showData(MSGMessagesParcelable messages) {
        adapter = new MessageSliderAdapter(messages.getResult().getMessages());
        int resId = R.anim.layout_animation_from_right;
        LayoutAnimationController animation = AnimationUtils.loadLayoutAnimation(rootView.getContext(), resId);
        recyclerView.setLayoutAnimation(animation);
        recyclerView.setLayoutManager(new LinearLayoutManager(rootView.getContext(), LinearLayoutManager.VERTICAL, false));
        recyclerView.setAdapter(adapter);

        //messages = null;
    }
}
