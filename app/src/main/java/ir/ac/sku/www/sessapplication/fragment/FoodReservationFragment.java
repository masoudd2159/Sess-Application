package ir.ac.sku.www.sessapplication.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

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
import ir.ac.sku.www.sessapplication.R;
import ir.ac.sku.www.sessapplication.adapters.FoodReservationAdapter;
import ir.ac.sku.www.sessapplication.models.LoginInformation;
import ir.ac.sku.www.sessapplication.models.SFXWeeklyList;
import ir.ac.sku.www.sessapplication.utils.HttpManager;

public class FoodReservationFragment extends Fragment {

    RequestQueue queue;
    Gson gson;
    View rootView;
    Button nextWeek;
    Button previousWeek;

    LoginInformation loginInformation;
    SFXWeeklyList sfxWeeklyList;
    FoodReservationAdapter adapter;

    public static FoodReservationFragment newInstance(String cookie) {
        FoodReservationFragment fragment = new FoodReservationFragment();

        Bundle args = new Bundle();
        args.putString("cookie", cookie);
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_food_reservation, container, false);
        loginInformation = new LoginInformation();
        sfxWeeklyList = new SFXWeeklyList();
        queue = Volley.newRequestQueue(rootView.getContext());
        gson = new Gson();
        getMealList(getArguments().getString("cookie"), "");

        nextWeek = rootView.findViewById(R.id.next_Week);
        previousWeek = rootView.findViewById(R.id.previous_Week);

        nextWeek.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getMealList(getArguments().getString("cookie"), "next");
            }
        });
        previousWeek.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getMealList(getArguments().getString("cookie"), "prev");
            }
        });
        return rootView;
    }

    public void getMealList(String cookie, String week) {
        Map<String, String> params = new HashMap<>();
        params.put("cookie", cookie);
        params.put("week", week);
        String URI = MyConfig.SFX_WEEKLY_LIST + "?" + HttpManager.enCodeParameters(params);

        StringRequest stringRequest = new StringRequest(Request.Method.GET,
                URI,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            sfxWeeklyList = gson.fromJson(new String(response.getBytes("ISO-8859-1"), "UTF-8"),
                                    SFXWeeklyList.class);
                            adapter = new FoodReservationAdapter(rootView, R.layout.fragment_food_reservation, sfxWeeklyList, getArguments().getString("cookie"));
                        } catch (UnsupportedEncodingException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
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
