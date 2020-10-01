package ir.ac.sku.www.sessapplication.fragment;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.gesture.GestureLibraries;
import android.gesture.GestureLibrary;
import android.gesture.GestureOverlayView;
import android.gesture.GestureStroke;
import android.gesture.Prediction;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.view.GestureDetector;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.airbnb.lottie.LottieAnimationView;
import com.android.volley.Request;
import com.google.gson.Gson;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import ir.ac.sku.www.sessapplication.api.ApplicationAPI;
import ir.ac.sku.www.sessapplication.api.MyLog;
import ir.ac.sku.www.sessapplication.api.PreferenceName;
import ir.ac.sku.www.sessapplication.R;
import ir.ac.sku.www.sessapplication.adapter.FoodReservationAdapter;
import ir.ac.sku.www.sessapplication.model.SFXIncreaseCreditDetail;
import ir.ac.sku.www.sessapplication.model.SFXWeeklyList;
import ir.ac.sku.www.sessapplication.utils.MyHandler;
import ir.ac.sku.www.sessapplication.utils.helper.ManagerHelper;
import ir.ac.sku.www.sessapplication.utils.WebService;

public class FoodReservationFragment extends Fragment {

    //My Library
    private SwipeRefreshLayout swipeRefreshLayout;
    private Gson gson;
    private View rootView;
    private SharedPreferences preferencesUserInformation;

    //My Views
    private Button btn_IncreaseCredit;
    private Button nextWeek;
    private Button previousWeek;
    //private ProgressBar progressBar;
    private LottieAnimationView gifImageViewPeriod;
    private LottieAnimationView progressBarAnimation;
    private TextView textViewPeriod;
    private LinearLayout statusBar;

    private NumberPicker subject;
    private NumberPicker credit;
    private TextView summary;
    private Button submit;

    //My Java Model Class
    private SFXWeeklyList sfxWeeklyList;
    private FoodReservationAdapter adapter;
    private SFXIncreaseCreditDetail creditDetail;
    private Dialog dialog;
    private WebService webService;

    GestureOverlayView gesture;
    GestureLibrary lib;
    ArrayList<Prediction> prediction;

    @SuppressLint({"LongLogTag", "ResourceType"})
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_food_reservation, container, false);
        Log.i(MyLog.FOOD_RESERVATION_FRAGMENT, "Food Reservation Fragment Created");

        webService = new WebService(rootView.getContext());

        //Initial Views
        init();

        btn_IncreaseCredit.setEnabled(false);

        //allocate Classes
        sfxWeeklyList = new SFXWeeklyList();
        creditDetail = new SFXIncreaseCreditDetail();

        //use Lib
        gson = new Gson();
        preferencesUserInformation = rootView.getContext().getSharedPreferences(PreferenceName.PREFERENCE_USER_INFORMATION, Context.MODE_PRIVATE);

        //create Dialog
        dialog = new Dialog(rootView.getContext(), R.style.Theme_Dialog);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        Window window = dialog.getWindow();
        if (window != null) {
            window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
            window.setGravity(Gravity.BOTTOM);
        }

        //get Weekly List
        weeklyList();
        increaseCredit();
        swipeRefresh();

        lib = GestureLibraries.fromRawResource(rootView.getContext(), R.id.foodReservation_CoordinatorLayout);
        gesture = rootView.findViewById(R.id.foodReservation_CoordinatorLayout);

        gesture.addOnGesturePerformedListener(new GestureOverlayView.OnGesturePerformedListener() {
            @Override
            public void onGesturePerformed(GestureOverlayView overlay, android.gesture.Gesture gesture) {
                ArrayList<GestureStroke> strokeList = gesture.getStrokes();
                // prediction = lib.recognize(gesture);
                float f[] = strokeList.get(0).points;

                if (f[0] < f[f.length - 2]) {
                    textViewPeriod.setText("");
                    gifImageViewPeriod.setVisibility(View.VISIBLE);
                    gifImageViewPeriod.setAnimation("loading_3.json");
                    gifImageViewPeriod.playAnimation();
                    gifImageViewPeriod.loop(true);
                    textViewPeriod.setVisibility(View.INVISIBLE);
                    Log.i(MyLog.FOOD_RESERVATION_FRAGMENT, "Previous Week List");
                    if (ManagerHelper.isInternet(rootView.getContext())) {
                        Log.i(MyLog.FOOD_RESERVATION_FRAGMENT, "OFFLine");
                        ManagerHelper.noInternetAccess(rootView.getContext());
                    } else {
                        Log.i(MyLog.FOOD_RESERVATION_FRAGMENT, "OnLine");
                        getMealList("prev");
                    }
                } else if (f[0] > f[f.length - 2]) {
                    textViewPeriod.setText("");
                    gifImageViewPeriod.setVisibility(View.VISIBLE);
                    gifImageViewPeriod.setAnimation("loading_3.json");
                    gifImageViewPeriod.playAnimation();
                    gifImageViewPeriod.loop(true);
                    textViewPeriod.setVisibility(View.INVISIBLE);

                    Log.i(MyLog.FOOD_RESERVATION_FRAGMENT, "Next Week List");
                    if (ManagerHelper.isInternet(rootView.getContext())) {
                        Log.i(MyLog.FOOD_RESERVATION_FRAGMENT, "OFFLine");
                        ManagerHelper.noInternetAccess(rootView.getContext());
                    } else {
                        Log.i(MyLog.FOOD_RESERVATION_FRAGMENT, "OnLine");
                        getMealList("next");
                    }
                }
            }
        });

        return rootView;
    }

    private void init() {
        swipeRefreshLayout = rootView.findViewById(R.id.foodReservation_SwipeRefresh);
        btn_IncreaseCredit = rootView.findViewById(R.id.foodReservation_IncreaseCreditButton);
        nextWeek = rootView.findViewById(R.id.foodReservation_NextWeek);
        previousWeek = rootView.findViewById(R.id.foodReservation_PreviousWeek);
        progressBarAnimation = rootView.findViewById(R.id.foodReservation_ProgressBarWaiting);
        gifImageViewPeriod = rootView.findViewById(R.id.foodReservation_GifImageViewPeriod);
        textViewPeriod = rootView.findViewById(R.id.foodReservation_PeriodTextView);
        statusBar = rootView.findViewById(R.id.foodReservation_StatusBar_Top);
    }

    @SuppressLint("LongLogTag")
    private void weeklyList() {
        //get Meal List
        textViewPeriod.setText("");
        gifImageViewPeriod.setVisibility(View.VISIBLE);
        gifImageViewPeriod.setAnimation("loading_3.json");
        gifImageViewPeriod.playAnimation();
        gifImageViewPeriod.loop(true);
        textViewPeriod.setVisibility(View.INVISIBLE);

        Log.i(MyLog.FOOD_RESERVATION_FRAGMENT, "First List");
        getMealList("");


        nextWeek.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                textViewPeriod.setText("");
                gifImageViewPeriod.setVisibility(View.VISIBLE);
                gifImageViewPeriod.setAnimation("loading_3.json");
                gifImageViewPeriod.playAnimation();
                gifImageViewPeriod.loop(true);
                textViewPeriod.setVisibility(View.INVISIBLE);

                Log.i(MyLog.FOOD_RESERVATION_FRAGMENT, "Next Week List");
                if (ManagerHelper.isInternet(rootView.getContext())) {
                    Log.i(MyLog.FOOD_RESERVATION_FRAGMENT, "OFFLine");
                    ManagerHelper.noInternetAccess(rootView.getContext());
                } else {
                    Log.i(MyLog.FOOD_RESERVATION_FRAGMENT, "OnLine");
                    getMealList("next");
                }
            }
        });

        previousWeek.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                textViewPeriod.setText("");
                gifImageViewPeriod.setVisibility(View.VISIBLE);
                gifImageViewPeriod.setAnimation("loading_3.json");
                gifImageViewPeriod.playAnimation();
                gifImageViewPeriod.loop(true);
                textViewPeriod.setVisibility(View.INVISIBLE);

                Log.i(MyLog.FOOD_RESERVATION_FRAGMENT, "Previous Week List");
                if (ManagerHelper.isInternet(rootView.getContext())) {
                    Log.i(MyLog.FOOD_RESERVATION_FRAGMENT, "OFFLine");
                    ManagerHelper.noInternetAccess(rootView.getContext());
                } else {
                    Log.i(MyLog.FOOD_RESERVATION_FRAGMENT, "OnLine");
                    getMealList("prev");
                }
            }
        });
    }

    @SuppressLint("LongLogTag")
    private void getMealList(String week) {
        Map<String, String> params = new HashMap<>();
        params.put("week", week);
        String URI = ApplicationAPI.SFX_WEEKLY_LIST + "?" + ManagerHelper.enCodeParameters(params);

        webService.request(URI, Request.Method.GET, new MyHandler() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onResponse(boolean ok, Object obj) {
                if (ok) {
                    try {
                        sfxWeeklyList = gson.fromJson(new String(obj.toString().getBytes("ISO-8859-1"), "UTF-8"), SFXWeeklyList.class);
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }

                    if (sfxWeeklyList.isOk()) {
                        Log.i(MyLog.FOOD_RESERVATION_FRAGMENT, "All Params True");
                        progressBarAnimation.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                Log.i(MyLog.FOOD_RESERVATION_FRAGMENT, "InVisible Gif Image View");
                                progressBarAnimation.setVisibility(View.INVISIBLE);
                                adapter = new FoodReservationAdapter(rootView, R.layout.fragment_food_reservation, sfxWeeklyList);
                                gifImageViewPeriod.setVisibility(View.INVISIBLE);
                                textViewPeriod.setVisibility(View.VISIBLE);
                                btn_IncreaseCredit.setEnabled(true);
                            }
                        }, 600);
                        Log.i(MyLog.FOOD_RESERVATION_FRAGMENT, "sfxWeeklyList Send To Adapter");
                    }
                }
            }
        });
    }

    @SuppressLint("LongLogTag")
    private void increaseCredit() {
        btn_IncreaseCredit.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View v) {
                Log.i(MyLog.FOOD_RESERVATION_ADAPTER, "Increase Credit Click Listener Run");
                ((ViewGroup) dialog.getWindow().getDecorView()).getChildAt(0).startAnimation(AnimationUtils.loadAnimation(rootView.getContext(), R.anim.slide_up));
                dialog.getWindow().getAttributes().windowAnimations = R.style.PauseDialogAnimation;
                WindowManager.LayoutParams layoutParams = dialog.getWindow().getAttributes();
                layoutParams.dimAmount = 0.7f;
                layoutParams.gravity = Gravity.BOTTOM;
                dialog.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
                dialog.setContentView(R.layout.sfx_increase_credit_detail);
                dialog.setCanceledOnTouchOutside(true);
                subject = dialog.findViewById(R.id.increaseCredit_PickerSubject);
                credit = dialog.findViewById(R.id.increaseCredit_PickerCredit);
                summary = dialog.findViewById(R.id.increaseCredit_PickerSummary);
                submit = dialog.findViewById(R.id.increaseCredit_Submit);
                setPicker();
                dialog.show();
            }
        });
    }

    @SuppressLint("LongLogTag")
    private void setPicker() {
        Log.i(MyLog.FOOD_RESERVATION_ADAPTER, "setPicker Start");
        Map<String, String> params = new HashMap<>();
        String URI = ApplicationAPI.SFX_INCREASE_CREDIT + "?" + ManagerHelper.enCodeParameters(params);
        Log.i(MyLog.FOOD_RESERVATION_ADAPTER, "cookie Picker : " + params.get("cookie"));


        webService.request(URI, Request.Method.GET, new MyHandler() {
            @Override
            public void onResponse(boolean ok, Object obj) {
                if (ok) {
                    try {
                        creditDetail = gson.fromJson(new String(obj.toString().getBytes("ISO-8859-1"), "UTF-8"), SFXIncreaseCreditDetail.class);
                        if (creditDetail.getOk()) {
                            showPicker();
                        }
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    @SuppressLint({"LongLogTag", "SetTextI18n"})
    private void showPicker() {
        Log.i(MyLog.FOOD_RESERVATION_ADAPTER, "Show Picker Started");

        String[] labelCredits = new String[creditDetail.getResult().getPlans().getCredits().size()];
        for (int i = 0; i < creditDetail.getResult().getPlans().getCredits().size(); i++) {
            labelCredits[i] = creditDetail.getResult().getPlans().getCredits().get(i).getLabel();
        }

        credit.setMinValue(0);
        credit.setMaxValue(creditDetail.getResult().getPlans().getCredits().size() - 1);
        credit.setDisplayedValues(labelCredits);


        String[] labelSubjects = new String[creditDetail.getResult().getSubjects().size()];
        for (int i = 0; i < creditDetail.getResult().getSubjects().size(); i++) {
            labelSubjects[i] = creditDetail.getResult().getSubjects().get(i).getLabel();
        }

        subject.setMinValue(0);
        subject.setMaxValue(creditDetail.getResult().getSubjects().size() - 1);
        subject.setDisplayedValues(labelSubjects);

        final String str1 = " مبلغ ";
        final String[] str2 = {creditDetail.getResult().getPlans().getCredits().get(0).getLabel()};
        final String str3 = " ";
        final String str4 = creditDetail.getResult().getPlans().getCurrency();
        final String str5 = " بابت ";
        final String[] str6 = {creditDetail.getResult().getSubjects().get(0).getLabel()};

        summary.setText(str1 + str2[0] + str3 + str4 + str5 + str6[0]);

        credit.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                str2[0] = creditDetail.getResult().getPlans().getCredits().get(newVal).getLabel();
                summary.setText(str1 + str2[0] + str3 + str4 + str5 + str6[0]);
            }
        });

        subject.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                str6[0] = creditDetail.getResult().getSubjects().get(newVal).getLabel();
                summary.setText(str1 + str2[0] + str3 + str4 + str5 + str6[0]);
            }
        });

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                increaseAmount(creditDetail.getResult().getSubjects().get(subject.getValue()).getValue(), creditDetail.getResult().getPlans().getCredits().get(credit.getValue()).getValue());
                dialog.dismiss();

            }
        });
    }

    @SuppressLint("LongLogTag")
    private void increaseAmount(String subject, String plan) {
        Log.i(MyLog.FOOD_RESERVATION_ADAPTER, "increaseAmount Start");
        Map<String, String> params = new HashMap<>();
        params.put("cookie", preferencesUserInformation.getString(PreferenceName.PREFERENCE_COOKIE, null));
        params.put("subject", subject);
        params.put("plan", plan);
        String URIPayment = ApplicationAPI.SFX_INCREASE_CREDIT_AMOUNT + "?" + ManagerHelper.enCodeParameters(params);
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse(URIPayment));
        getActivity().startActivity(intent);
    }

    private void swipeRefresh() {
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (ManagerHelper.isInternet(rootView.getContext())) {
                    ManagerHelper.noInternetAccess(rootView.getContext());
                } else {
                    getMealList("");
                    swipeRefreshLayout.setRefreshing(false);
                }
            }
        });
    }

    public class OnSwipeTouchListener implements OnTouchListener {

        private final GestureDetector gestureDetector;

        public OnSwipeTouchListener(Context ctx) {
            gestureDetector = new GestureDetector(ctx, new GestureListener());
        }

        @Override
        public boolean onTouch(View v, MotionEvent event) {
            return gestureDetector.onTouchEvent(event);
        }

        private final class GestureListener extends SimpleOnGestureListener {

            private static final int SWIPE_THRESHOLD = 100;
            private static final int SWIPE_VELOCITY_THRESHOLD = 100;

            @Override
            public boolean onDown(MotionEvent e) {
                return true;
            }

            @Override
            public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
                boolean result = false;
                try {
                    float diffY = e2.getY() - e1.getY();
                    float diffX = e2.getX() - e1.getX();
                    if (Math.abs(diffX) > Math.abs(diffY)) {
                        if (Math.abs(diffX) > SWIPE_THRESHOLD && Math.abs(velocityX) > SWIPE_VELOCITY_THRESHOLD) {
                            if (diffX > 0) {
                                onSwipeRight();
                            } else {
                                onSwipeLeft();
                            }
                            result = true;
                        }
                    }
                } catch (Exception exception) {
                    exception.printStackTrace();
                }
                return result;
            }
        }

        public void onSwipeRight() {
        }

        public void onSwipeLeft() {
        }
    }
}
