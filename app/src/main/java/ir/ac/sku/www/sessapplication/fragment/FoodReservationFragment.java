package ir.ac.sku.www.sessapplication.fragment;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
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
import ir.ac.sku.www.sessapplication.R;
import ir.ac.sku.www.sessapplication.adapters.FoodReservationAdapter;
import ir.ac.sku.www.sessapplication.models.SFXIncreaseCreditDetail;
import ir.ac.sku.www.sessapplication.models.SFXWeeklyList;
import ir.ac.sku.www.sessapplication.utils.HttpManager;

public class FoodReservationFragment extends Fragment {

    //My Library
    private SwipeRefreshLayout swipeRefreshLayout;
    private RequestQueue queue;
    private Gson gson;
    private View rootView;

    //My Views
    private Button btn_IncreaseCredit;
    private Button nextWeek;
    private Button previousWeek;
    private ProgressBar progressBar;
    private NumberPicker type;
    private NumberPicker amount;
    private TextView summery;
    private Button submit;

    //My Java Model Class
    private SFXWeeklyList sfxWeeklyList;
    private FoodReservationAdapter adapter;
    private SFXIncreaseCreditDetail creditDetail;
    private Dialog dialog;

    @SuppressLint("LongLogTag")
    public static FoodReservationFragment newInstance(String cookie) {
        Log.i(MyLog.FOOD_RESERVATION_FRAGMENT, "New Instance -> Cookie : " + cookie);
        FoodReservationFragment fragment = new FoodReservationFragment();
        Bundle args = new Bundle();
        args.putString("cookie", cookie);
        fragment.setArguments(args);
        return fragment;
    }

    @SuppressLint("LongLogTag")
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_food_reservation, container, false);
        Log.i(MyLog.FOOD_RESERVATION_FRAGMENT, "Food Reservation Fragment Created");

        //Initial Views
        init();

        //allocate Classes
        sfxWeeklyList = new SFXWeeklyList();
        creditDetail = new SFXIncreaseCreditDetail();

        //use Lib
        queue = Volley.newRequestQueue(rootView.getContext());
        gson = new Gson();
        dialog = new Dialog(rootView.getContext());

        //get Weekly List
        weeklyList();
        increaseCredit();
        swipeRefresh();
        return rootView;
    }

    private void init() {
        swipeRefreshLayout = rootView.findViewById(R.id.mySwipe);
        btn_IncreaseCredit = rootView.findViewById(R.id.btn_increaseCredit);
        nextWeek = rootView.findViewById(R.id.next_Week);
        previousWeek = rootView.findViewById(R.id.previous_Week);
        progressBar = rootView.findViewById(R.id.food_ProgressBar);
    }

    @SuppressLint("LongLogTag")
    private void weeklyList() {
        //get Meal List
        if (getArguments() != null) {
            Log.i(MyLog.FOOD_RESERVATION_FRAGMENT, "First List");
            getMealList(getArguments().getString("cookie"), "");
        }


        nextWeek.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i(MyLog.FOOD_RESERVATION_FRAGMENT, "Next Week List");
                if (HttpManager.isNOTOnline(rootView.getContext())) {
                    Log.i(MyLog.FOOD_RESERVATION_FRAGMENT, "OFFLine");
                    HttpManager.noInternetAccess(rootView.getContext());
                } else {
                    Log.i(MyLog.FOOD_RESERVATION_FRAGMENT, "OnLine");
                    getMealList(getArguments().getString("cookie"), "next");
                }
            }
        });
        previousWeek.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i(MyLog.FOOD_RESERVATION_FRAGMENT, "Previous Week List");
                if (HttpManager.isNOTOnline(rootView.getContext())) {
                    Log.i(MyLog.FOOD_RESERVATION_FRAGMENT, "OFFLine");
                    HttpManager.noInternetAccess(rootView.getContext());
                } else {
                    Log.i(MyLog.FOOD_RESERVATION_FRAGMENT, "OnLine");
                    getMealList(getArguments().getString("cookie"), "prev");
                }
            }
        });
    }

    @SuppressLint("LongLogTag")
    public void getMealList(String cookie, String week) {
        Map<String, String> params = new HashMap<>();
        params.put("cookie", cookie);
        params.put("week", week);
        String URI = MyConfig.SFX_WEEKLY_LIST + "?" + HttpManager.enCodeParameters(params);

        Log.i(MyLog.FOOD_RESERVATION_FRAGMENT, "Meal List -> Cookie : " + params.get("cookie"));
        Log.i(MyLog.FOOD_RESERVATION_FRAGMENT, "Meal List -> Week : " + params.get("week"));

        StringRequest stringRequest = new StringRequest(Request.Method.GET,
                URI,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            sfxWeeklyList = gson.fromJson(new String(response.getBytes("ISO-8859-1"), "UTF-8"), SFXWeeklyList.class);
                            if (sfxWeeklyList.isOk()) {
                                Log.i(MyLog.FOOD_RESERVATION_FRAGMENT, "All Params True");
                                if (getArguments() != null) {
                                    progressBar.postDelayed(new Runnable() {
                                        @Override
                                        public void run() {
                                            Log.i(MyLog.FOOD_RESERVATION_FRAGMENT, "InVisible Gif Image View And Get Captcha");
                                            progressBar.setVisibility(View.INVISIBLE);
                                            adapter = new FoodReservationAdapter(rootView, R.layout.fragment_food_reservation, sfxWeeklyList, getArguments().getString("cookie"));
                                        }
                                    }, 600);
                                    Log.i(MyLog.FOOD_RESERVATION_FRAGMENT, "sfxWeeklyList Send To Adapter");
                                }
                            } else if (!sfxWeeklyList.isOk()) {
                                Log.i(MyLog.FOOD_RESERVATION_FRAGMENT, "Some Parameter is False : " + "ERROR CODE : " + sfxWeeklyList.getDescription().getErrorCode() + sfxWeeklyList.getDescription().getErrorText());
                                Log.i(MyLog.FOOD_RESERVATION_FRAGMENT, "ERROR CODE : " + sfxWeeklyList.getDescription().getErrorCode());
                                Log.i(MyLog.FOOD_RESERVATION_FRAGMENT, "ERROR TEXT : " + sfxWeeklyList.getDescription().getErrorText());
                                Toast.makeText(rootView.getContext(), sfxWeeklyList.getDescription().getErrorText(), Toast.LENGTH_SHORT).show();
                                if (Integer.parseInt(sfxWeeklyList.getDescription().getErrorCode()) < 0) {
                                    Log.i(MyLog.FOOD_RESERVATION_FRAGMENT, "Lost Cookie");
                                    System.exit(0);
                                }
                            }
                        } catch (UnsupportedEncodingException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.i(MyLog.FOOD_RESERVATION_FRAGMENT, error.getMessage());
                        new AlertDialog.Builder(rootView.getContext())
                                .setTitle("ERROR")
                                .setMessage(error.getMessage())
                                .setPositiveButton("Ok", null)
                                .setIcon(android.R.drawable.ic_dialog_alert)
                                .show();
                    }
                });
        queue.add(stringRequest);
    }

    @SuppressLint("LongLogTag")
    private void increaseCredit() {
        btn_IncreaseCredit.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View v) {
                Log.i(MyLog.FOOD_RESERVATION_ADAPTER, "Increase Credit Click Listener Run");
                dialog.setContentView(R.layout.sfx_increase_credit_detail);
                Window window = dialog.getWindow();
                if (window != null) {
                    window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
                    window.setGravity(Gravity.BOTTOM);
                }
                type = dialog.findViewById(R.id.pickerCredit_Type);
                amount = dialog.findViewById(R.id.pickerCredit_Amount);
                summery = dialog.findViewById(R.id.pickerCredit_State);
                submit = dialog.findViewById(R.id.pickerCredit_Submit);
                setPicker();
                dialog.show();
            }
        });
    }

    @SuppressLint("LongLogTag")
    private void setPicker() {
        Log.i(MyLog.FOOD_RESERVATION_ADAPTER, "setPicker Start");

        Map<String, String> params = new HashMap<>();
        params.put("cookie", getArguments().getString("cookie"));
        String URI = MyConfig.SFX_INCREASE_CREDIT + "?" + HttpManager.enCodeParameters(params);
        Log.i(MyLog.FOOD_RESERVATION_ADAPTER, "cookie Picker : " + params.get("cookie"));

        StringRequest stringRequest = new StringRequest(Request.Method.GET,
                URI,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            creditDetail = gson.fromJson(new String(response.getBytes("ISO-8859-1"), "UTF-8"), SFXIncreaseCreditDetail.class);
                            if (creditDetail.getOk()) {
                                showPicker();
                            } else if (!creditDetail.getOk()) {
                                Toast.makeText(rootView.getContext(), creditDetail.getDescription().getErrorText(), Toast.LENGTH_SHORT).show();
                                if (Integer.parseInt(creditDetail.getDescription().getErrorCode()) < 0) {
                                    System.exit(0);
                                }
                            }
                        } catch (UnsupportedEncodingException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.i(MyLog.FOOD_RESERVATION_ADAPTER, error.getMessage());
                        new AlertDialog.Builder(rootView.getContext())
                                .setTitle("ERROR")
                                .setMessage(error.getMessage())
                                .setPositiveButton("Ok", null)
                                .setIcon(android.R.drawable.ic_dialog_alert)
                                .show();
                    }
                });
        queue.add(stringRequest);
    }

    @SuppressLint({"LongLogTag", "SetTextI18n"})
    private void showPicker() {
        Log.i(MyLog.FOOD_RESERVATION_ADAPTER, "Show Picker Started");

        String[] labelCredits = new String[creditDetail.getResult().getPlans().getCredits().size()];
        for (int i = 0; i < creditDetail.getResult().getPlans().getCredits().size(); i++) {
            labelCredits[i] = creditDetail.getResult().getPlans().getCredits().get(i).getLabel();
        }

        amount.setMinValue(0);
        amount.setMaxValue(creditDetail.getResult().getPlans().getCredits().size() - 1);
        amount.setDisplayedValues(labelCredits);


        String[] labelSubjects = new String[creditDetail.getResult().getSubjects().size()];
        for (int i = 0; i < creditDetail.getResult().getSubjects().size(); i++) {
            labelSubjects[i] = creditDetail.getResult().getSubjects().get(i).getLabel();
        }

        type.setMinValue(0);
        type.setMaxValue(creditDetail.getResult().getSubjects().size() - 1);
        type.setDisplayedValues(labelSubjects);

        final String str1 = " مبلغ ";
        final String[] str2 = {creditDetail.getResult().getPlans().getCredits().get(0).getLabel()};
        final String str3 = " ";
        final String str4 = creditDetail.getResult().getPlans().getCurrency();
        final String str5 = " بابت ";
        final String[] str6 = {creditDetail.getResult().getSubjects().get(0).getLabel()};

        summery.setText(str1 + str2[0] + str3 + str4 + str5 + str6[0]);

        amount.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                str2[0] = creditDetail.getResult().getPlans().getCredits().get(newVal).getLabel();
                summery.setText(str1 + str2[0] + str3 + str4 + str5 + str6[0]);
            }
        });

        type.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                str6[0] = creditDetail.getResult().getSubjects().get(newVal).getLabel();
                summery.setText(str1 + str2[0] + str3 + str4 + str5 + str6[0]);
            }
        });

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (getArguments() != null) {
                    increaseAmount(getArguments().getString("cookie"), creditDetail.getResult().getSubjects().get(type.getValue()).getValue(), creditDetail.getResult().getPlans().getCredits().get(amount.getValue()).getValue());
                    dialog.dismiss();
                }
            }
        });
    }

    @SuppressLint("LongLogTag")
    private void increaseAmount(String cookie, String subject, String plan) {
        Log.i(MyLog.FOOD_RESERVATION_ADAPTER, "increaseAmount Start");
        Map<String, String> params = new HashMap<>();
        params.put("cookie", cookie);
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
                    if (getArguments() != null) {
                        getMealList(getArguments().getString("cookie"), "");
                        swipeRefreshLayout.setRefreshing(false);
                    }
                }
            }
        });
    }
}
