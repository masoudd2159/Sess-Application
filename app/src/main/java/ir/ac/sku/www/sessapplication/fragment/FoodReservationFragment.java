package ir.ac.sku.www.sessapplication.fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
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
import ir.ac.sku.www.sessapplication.models.SFXWeeklyList;
import ir.ac.sku.www.sessapplication.utils.HttpManager;

public class FoodReservationFragment extends Fragment {

    //My Library
    private RequestQueue queue;
    private Gson gson;
    private View rootView;

    //My Views
    private Button nextWeek;
    private Button previousWeek;
    private ProgressBar progressBar;

    //My Java Model Class
    private SFXWeeklyList sfxWeeklyList;
    private FoodReservationAdapter adapter;

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

        //use Lib
        queue = Volley.newRequestQueue(rootView.getContext());
        gson = new Gson();

        //get Weekly List
        weeklyList();


        return rootView;
    }

    private void init() {
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
                getMealList(getArguments().getString("cookie"), "next");
            }
        });
        previousWeek.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i(MyLog.FOOD_RESERVATION_FRAGMENT, "Previous Week List");
                getMealList(getArguments().getString("cookie"), "prev");
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
}
