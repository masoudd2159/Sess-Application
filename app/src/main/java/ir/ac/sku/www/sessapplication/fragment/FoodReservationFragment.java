package ir.ac.sku.www.sessapplication.fragment;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.view.GestureDetector;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
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
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

import ir.ac.sku.www.sessapplication.API.MyConfig;
import ir.ac.sku.www.sessapplication.API.MyLog;
import ir.ac.sku.www.sessapplication.API.PreferenceName;
import ir.ac.sku.www.sessapplication.R;
import ir.ac.sku.www.sessapplication.activities.BottomBarActivity;
import ir.ac.sku.www.sessapplication.activities.LoginActivity;
import ir.ac.sku.www.sessapplication.activities.SplashScreenActivity;
import ir.ac.sku.www.sessapplication.adapters.FoodReservationAdapter;
import ir.ac.sku.www.sessapplication.models.SFXIncreaseCreditDetail;
import ir.ac.sku.www.sessapplication.models.SFXWeeklyList;
import ir.ac.sku.www.sessapplication.utils.Handler;
import ir.ac.sku.www.sessapplication.utils.HttpManager;
import ir.ac.sku.www.sessapplication.utils.SignIn;
import ir.ac.sku.www.sessapplication.utils.WebService;
import pl.droidsonroids.gif.GifImageView;

public class FoodReservationFragment extends Fragment {

    //My Library
    private SwipeRefreshLayout swipeRefreshLayout;
    private Gson gson;
    private SharedPreferences preferencesCookie;
    private View rootView;

    //My Views
    private Button btn_IncreaseCredit;
    private Button nextWeek;
    private Button previousWeek;
    private ProgressBar progressBar;
    private GifImageView gifImageViewPeriod;
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

    @SuppressLint("LongLogTag")
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
        preferencesCookie = this.getActivity().getSharedPreferences(PreferenceName.COOKIE_PREFERENCE_NAME, Context.MODE_PRIVATE);

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

        rootView.setOnTouchListener(new OnSwipeTouchListener(rootView.getContext()) {

            @Override
            public void onSwipeRight() {
                textViewPeriod.setText("");
                gifImageViewPeriod.setVisibility(View.VISIBLE);
                textViewPeriod.setVisibility(View.INVISIBLE);
                Log.i(MyLog.FOOD_RESERVATION_FRAGMENT, "Previous Week List");
                if (HttpManager.isNOTOnline(rootView.getContext())) {
                    Log.i(MyLog.FOOD_RESERVATION_FRAGMENT, "OFFLine");
                    HttpManager.noInternetAccess(rootView.getContext());
                } else {
                    Log.i(MyLog.FOOD_RESERVATION_FRAGMENT, "OnLine");
                    getMealList("prev");
                }
            }

            @Override
            public void onSwipeLeft() {
                textViewPeriod.setText("");
                gifImageViewPeriod.setVisibility(View.VISIBLE);
                textViewPeriod.setVisibility(View.INVISIBLE);

                Log.i(MyLog.FOOD_RESERVATION_FRAGMENT, "Next Week List");
                if (HttpManager.isNOTOnline(rootView.getContext())) {
                    Log.i(MyLog.FOOD_RESERVATION_FRAGMENT, "OFFLine");
                    HttpManager.noInternetAccess(rootView.getContext());
                } else {
                    Log.i(MyLog.FOOD_RESERVATION_FRAGMENT, "OnLine");
                    getMealList("next");
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
        progressBar = rootView.findViewById(R.id.foodReservation_ProgressBarWaiting);
        gifImageViewPeriod = rootView.findViewById(R.id.foodReservation_GifImageViewPeriod);
        textViewPeriod = rootView.findViewById(R.id.foodReservation_PeriodTextView);
        statusBar = rootView.findViewById(R.id.foodReservation_StatusBar_Top);
    }

    @SuppressLint("LongLogTag")
    private void weeklyList() {
        //get Meal List
        textViewPeriod.setText("");
        gifImageViewPeriod.setVisibility(View.VISIBLE);
        textViewPeriod.setVisibility(View.INVISIBLE);

        Log.i(MyLog.FOOD_RESERVATION_FRAGMENT, "First List");
        getMealList("");


        nextWeek.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                textViewPeriod.setText("");
                gifImageViewPeriod.setVisibility(View.VISIBLE);
                textViewPeriod.setVisibility(View.INVISIBLE);

                Log.i(MyLog.FOOD_RESERVATION_FRAGMENT, "Next Week List");
                if (HttpManager.isNOTOnline(rootView.getContext())) {
                    Log.i(MyLog.FOOD_RESERVATION_FRAGMENT, "OFFLine");
                    HttpManager.noInternetAccess(rootView.getContext());
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
                textViewPeriod.setVisibility(View.INVISIBLE);

                Log.i(MyLog.FOOD_RESERVATION_FRAGMENT, "Previous Week List");
                if (HttpManager.isNOTOnline(rootView.getContext())) {
                    Log.i(MyLog.FOOD_RESERVATION_FRAGMENT, "OFFLine");
                    HttpManager.noInternetAccess(rootView.getContext());
                } else {
                    Log.i(MyLog.FOOD_RESERVATION_FRAGMENT, "OnLine");
                    getMealList("prev");
                }
            }
        });
    }

    @SuppressLint("LongLogTag")
    public void getMealList(String week) {
        Map<String, String> params = new HashMap<>();
        params.put("week", week);
        String URI = MyConfig.SFX_WEEKLY_LIST + "?" + HttpManager.enCodeParameters(params);

        webService.request(URI, Request.Method.GET, new Handler() {
            @Override
            public void onResponse(boolean ok, Object obj) {
                if (ok) {
                    try {
                        sfxWeeklyList = gson.fromJson(new String(obj.toString().getBytes("ISO-8859-1"), "UTF-8"), SFXWeeklyList.class);

                        if (sfxWeeklyList.isOk()) {
                            Log.i(MyLog.FOOD_RESERVATION_FRAGMENT, "All Params True");
                            progressBar.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    Log.i(MyLog.FOOD_RESERVATION_FRAGMENT, "InVisible Gif Image View And Get Captcha");
                                    progressBar.setVisibility(View.INVISIBLE);
                                    adapter = new FoodReservationAdapter(rootView, R.layout.fragment_food_reservation, sfxWeeklyList);
                                    gifImageViewPeriod.setVisibility(View.INVISIBLE);
                                    textViewPeriod.setVisibility(View.VISIBLE);
                                    btn_IncreaseCredit.setEnabled(true);
                                }
                            }, 600);
                            Log.i(MyLog.FOOD_RESERVATION_FRAGMENT, "sfxWeeklyList Send To Adapter");
                        }

                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
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
        String URI = MyConfig.SFX_INCREASE_CREDIT + "?" + HttpManager.enCodeParameters(params);
        Log.i(MyLog.FOOD_RESERVATION_ADAPTER, "cookie Picker : " + params.get("cookie"));


        webService.request(URI, Request.Method.GET, new Handler() {
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
        params.put("cookie", preferencesCookie.getString(PreferenceName.COOKIE_PREFERENCE_COOKIE, null));
        params.put("subject", subject);
        params.put("plan", plan);
        String URIPayment = MyConfig.SFX_INCREASE_CREDIT_AMOUNT + "?" + HttpManager.enCodeParameters(params);
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse(URIPayment));
        getActivity().startActivity(intent);
    }

    private void swipeRefresh() {
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (HttpManager.isNOTOnline(rootView.getContext())) {
                    HttpManager.noInternetAccess(rootView.getContext());
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
