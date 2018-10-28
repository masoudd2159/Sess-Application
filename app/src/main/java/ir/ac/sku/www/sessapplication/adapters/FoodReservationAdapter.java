package ir.ac.sku.www.sessapplication.adapters;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ir.ac.sku.www.sessapplication.API.MyConfig;
import ir.ac.sku.www.sessapplication.API.MyLog;
import ir.ac.sku.www.sessapplication.R;
import ir.ac.sku.www.sessapplication.activities.BottomBarActivity;
import ir.ac.sku.www.sessapplication.activities.LoginActivity;
import ir.ac.sku.www.sessapplication.fragment.FoodReservationFragment;
import ir.ac.sku.www.sessapplication.models.SFXMealDetail;
import ir.ac.sku.www.sessapplication.models.SFXWeeklyList;
import ir.ac.sku.www.sessapplication.models.SendInformation;
import ir.ac.sku.www.sessapplication.utils.CustomToast;
import ir.ac.sku.www.sessapplication.utils.HttpManager;

@SuppressLint("Registered")
public class FoodReservationAdapter {

    private View rootView;
    private SFXWeeklyList sfxWeeklyList;
    private int resource;
    private String cookie;

    private Button increaseCredit;
    private TextView credit;


    private Button saturdayBreakfast;
    private Button saturdayLunch;
    private Button saturdayDinner;

    private Button sundayBreakfast;
    private Button sundayLunch;
    private Button sundayDinner;

    private Button monday_Dinner;
    private Button monday_Lunch;
    private Button monday_Breakfast;

    private Button tuesdayBreakfast;
    private Button tuesdayLunch;
    private Button tuesdayDinner;

    private Button wednesdayBreakfast;
    private Button wednesdayLunch;
    private Button wednesdayDinner;

    private Button thursdayBreakfast;
    private Button thursdayLunch;
    private Button thursdayDinner;

    private Button fridayBreakfast;
    private Button fridayLunch;
    private Button fridayDinner;


    private Button nextWeek;
    private TextView state;
    private Button previousWeek;

    private Button restaurant;
    private Button food;
    private Button buy;

    private String restaurantCode = "";
    private String foodCode = "";

    private RequestQueue queue;
    private Gson gson;
    private SFXMealDetail sfxMealDetail;

    public FoodReservationAdapter(@NonNull View rootView, int resource, @NonNull SFXWeeklyList sfxWeeklyList, String cookie) {
        this.rootView = rootView;
        this.resource = resource;
        this.sfxWeeklyList = sfxWeeklyList;
        this.cookie = cookie;
        sfxMealDetail = new SFXMealDetail();
        gson = new Gson();
        queue = Volley.newRequestQueue(rootView.getContext());
        init();
        fileItems();
        sfmMeals();
    }

    private void init() {
        increaseCredit = rootView.findViewById(R.id.btn_increaseCredit);
        credit = rootView.findViewById(R.id.tv_Credit);


        saturdayBreakfast = rootView.findViewById(R.id.saturday_Breakfast);
        saturdayLunch = rootView.findViewById(R.id.saturday_Lunch);
        saturdayDinner = rootView.findViewById(R.id.saturday_Dinner);

        sundayBreakfast = rootView.findViewById(R.id.sunday_Breakfast);
        sundayLunch = rootView.findViewById(R.id.sunday_Lunch);
        sundayDinner = rootView.findViewById(R.id.sunday_Dinner);

        monday_Breakfast = rootView.findViewById(R.id.monday_Breakfast);
        monday_Lunch = rootView.findViewById(R.id.monday_Lunch);
        monday_Dinner = rootView.findViewById(R.id.monday_Dinner);

        tuesdayBreakfast = rootView.findViewById(R.id.tuesday_Breakfast);
        tuesdayLunch = rootView.findViewById(R.id.tuesday_Lunch);
        tuesdayDinner = rootView.findViewById(R.id.tuesday_Dinner);

        wednesdayBreakfast = rootView.findViewById(R.id.wednesday_Breakfast);
        wednesdayLunch = rootView.findViewById(R.id.wednesday_Lunch);
        wednesdayDinner = rootView.findViewById(R.id.wednesday_Dinner);

        thursdayBreakfast = rootView.findViewById(R.id.thursday_Breakfast);
        thursdayLunch = rootView.findViewById(R.id.thursday_Lunch);
        thursdayDinner = rootView.findViewById(R.id.thursday_Dinner);

        fridayBreakfast = rootView.findViewById(R.id.friday_Breakfast);
        fridayLunch = rootView.findViewById(R.id.friday_Lunch);
        fridayDinner = rootView.findViewById(R.id.friday_Dinner);


        nextWeek = rootView.findViewById(R.id.next_Week);
        state = rootView.findViewById(R.id.tv_State);
        previousWeek = rootView.findViewById(R.id.previous_Week);
    }

    @SuppressLint("SetTextI18n")
    private void fileItems() {

        if (sfxWeeklyList.isOk()) {
            credit.setText(" اعتبار : " + sfxWeeklyList.getResult().getCredit() + " ریال ");

//__________________________________________________________________________________________________
            //Saturday
            Button buttons_Saturday[] = {saturdayBreakfast, saturdayLunch, saturdayDinner};
            for (int i = 0; i < 3; i++) {
                if (sfxWeeklyList.getResult().getDay0().get(i).getStatus().equals("invalid")) {
                    invalid(buttons_Saturday[i]);
                    if (sfxWeeklyList.getResult().getDay0().get(i).getName().equals("empty")) {
                        buttons_Saturday[i].setText("");
                    } else {
                        buttons_Saturday[i].setText(sfxWeeklyList.getResult().getDay0().get(i).getName());
                    }
                } else if (sfxWeeklyList.getResult().getDay0().get(i).getStatus().equals("valid")) {
                    if (sfxWeeklyList.getResult().getDay0().get(i).getName().equals("empty")) {
                        validLiteGreen(buttons_Saturday[i]);
                        buttons_Saturday[i].setText("رزرو");
                    } else {
                        validGreen(buttons_Saturday[i]);
                        buttons_Saturday[i].setText(sfxWeeklyList.getResult().getDay0().get(i).getName());
                    }
                }
            }
//__________________________________________________________________________________________________
            //Sunday
            Button buttons_Sunday[] = {sundayBreakfast, sundayLunch, sundayDinner};
            for (int i = 0; i < 3; i++) {
                if (sfxWeeklyList.getResult().getDay1().get(i).getStatus().equals("invalid")) {
                    invalid(buttons_Sunday[i]);
                    if (sfxWeeklyList.getResult().getDay1().get(i).getName().equals("empty")) {
                        buttons_Sunday[i].setText("");
                    } else {
                        buttons_Sunday[i].setText(sfxWeeklyList.getResult().getDay1().get(i).getName());
                    }
                } else if (sfxWeeklyList.getResult().getDay1().get(i).getStatus().equals("valid")) {
                    if (sfxWeeklyList.getResult().getDay1().get(i).getName().equals("empty")) {
                        validLiteGreen(buttons_Sunday[i]);
                        buttons_Sunday[i].setText("رزرو");
                    } else {
                        validGreen(buttons_Sunday[i]);
                        buttons_Sunday[i].setText(sfxWeeklyList.getResult().getDay1().get(i).getName());
                    }
                }
            }
//__________________________________________________________________________________________________
            //Monday
            Button buttons_Monday[] = {monday_Breakfast, monday_Lunch, monday_Dinner};
            for (int i = 0; i < 3; i++) {
                if (sfxWeeklyList.getResult().getDay2().get(i).getStatus().equals("invalid")) {
                    invalid(buttons_Monday[i]);
                    if (sfxWeeklyList.getResult().getDay2().get(i).getName().equals("empty")) {
                        buttons_Monday[i].setText("");
                    } else {
                        buttons_Monday[i].setText(sfxWeeklyList.getResult().getDay2().get(i).getName());
                    }
                } else if (sfxWeeklyList.getResult().getDay2().get(i).getStatus().equals("valid")) {
                    if (sfxWeeklyList.getResult().getDay2().get(i).getName().equals("empty")) {
                        validLiteGreen(buttons_Monday[i]);
                        buttons_Monday[i].setText("رزرو");
                    } else {
                        validGreen(buttons_Monday[i]);
                        buttons_Monday[i].setText(sfxWeeklyList.getResult().getDay2().get(i).getName());
                    }
                }
            }
//__________________________________________________________________________________________________
            //Tuesday
            Button buttons_Tuesday[] = {tuesdayBreakfast, tuesdayLunch, tuesdayDinner};
            for (int i = 0; i < 3; i++) {
                if (sfxWeeklyList.getResult().getDay3().get(i).getStatus().equals("invalid")) {
                    invalid(buttons_Tuesday[i]);
                    if (sfxWeeklyList.getResult().getDay3().get(i).getName().equals("empty")) {
                        buttons_Tuesday[i].setText("");
                    } else {
                        buttons_Tuesday[i].setText(sfxWeeklyList.getResult().getDay3().get(i).getName());
                    }
                } else if (sfxWeeklyList.getResult().getDay3().get(i).getStatus().equals("valid")) {
                    if (sfxWeeklyList.getResult().getDay3().get(i).getName().equals("empty")) {
                        validLiteGreen(buttons_Tuesday[i]);
                        buttons_Tuesday[i].setText("رزرو");
                    } else {
                        validGreen(buttons_Tuesday[i]);
                        buttons_Tuesday[i].setText(sfxWeeklyList.getResult().getDay3().get(i).getName());
                    }
                }
            }
//__________________________________________________________________________________________________
            //Wednesday
            Button buttons_Wednesday[] = {wednesdayBreakfast, wednesdayLunch, wednesdayDinner};
            for (int i = 0; i < 3; i++) {
                if (sfxWeeklyList.getResult().getDay4().get(i).getStatus().equals("invalid")) {
                    invalid(buttons_Wednesday[i]);
                    if (sfxWeeklyList.getResult().getDay4().get(i).getName().equals("empty")) {
                        buttons_Wednesday[i].setText("");
                    } else {
                        buttons_Wednesday[i].setText(sfxWeeklyList.getResult().getDay4().get(i).getName());
                    }
                } else if (sfxWeeklyList.getResult().getDay4().get(i).getStatus().equals("valid")) {
                    if (sfxWeeklyList.getResult().getDay4().get(i).getName().equals("empty")) {
                        validLiteGreen(buttons_Wednesday[i]);
                        buttons_Wednesday[i].setText("رزرو");
                    } else {
                        validGreen(buttons_Wednesday[i]);
                        buttons_Wednesday[i].setText(sfxWeeklyList.getResult().getDay4().get(i).getName());
                    }
                }
            }
//__________________________________________________________________________________________________
            //Thursday
            Button buttons_Thursday[] = {thursdayBreakfast, thursdayLunch, thursdayDinner};
            for (int i = 0; i < 3; i++) {
                if (sfxWeeklyList.getResult().getDay5().get(i).getStatus().equals("invalid")) {
                    invalid(buttons_Thursday[i]);
                    if (sfxWeeklyList.getResult().getDay5().get(i).getName().equals("empty")) {
                        buttons_Thursday[i].setText("");
                    } else {
                        buttons_Thursday[i].setText(sfxWeeklyList.getResult().getDay5().get(i).getName());
                    }
                } else if (sfxWeeklyList.getResult().getDay5().get(i).getStatus().equals("valid")) {
                    if (sfxWeeklyList.getResult().getDay5().get(i).getName().equals("empty")) {
                        validLiteGreen(buttons_Thursday[i]);
                        buttons_Thursday[i].setText("رزرو");
                    } else {
                        validGreen(buttons_Thursday[i]);
                        buttons_Thursday[i].setText(sfxWeeklyList.getResult().getDay5().get(i).getName());
                    }
                }
            }
//__________________________________________________________________________________________________
            //Friday
            Button buttons_Friday[] = {fridayBreakfast, fridayLunch, fridayDinner};
            for (int i = 0; i < 3; i++) {
                if (sfxWeeklyList.getResult().getDay6().get(i).getStatus().equals("invalid")) {
                    invalid(buttons_Friday[i]);
                    if (sfxWeeklyList.getResult().getDay6().get(i).getName().equals("empty")) {
                        buttons_Friday[i].setText("");
                    } else {
                        buttons_Friday[i].setText(sfxWeeklyList.getResult().getDay6().get(i).getName());
                    }
                } else if (sfxWeeklyList.getResult().getDay6().get(i).getStatus().equals("valid")) {
                    if (sfxWeeklyList.getResult().getDay6().get(i).getName().equals("empty")) {
                        validLiteGreen(buttons_Friday[i]);
                        buttons_Friday[i].setText("رزرو");
                    } else {
                        validGreen(buttons_Friday[i]);
                        buttons_Friday[i].setText(sfxWeeklyList.getResult().getDay6().get(i).getName());
                    }
                }
            }
//__________________________________________________________________________________________________
            state.setText(sfxWeeklyList.getResult().getDay0().get(3).getDateText() + " - " + sfxWeeklyList.getResult().getDay6().get(3).getDateText());
        }
    }

    private void invalid(Button button) {
        button.setBackgroundResource(R.drawable.reservation_background_black);
        button.setTextColor(Color.parseColor("#ffffff"));
    }

    private void validLiteGreen(Button button) {
        button.setBackgroundResource(R.drawable.reservation_background_lite_green);
        button.setTextColor(Color.parseColor("#4CAF50"));
    }

    private void validGreen(Button button) {
        button.setBackgroundResource(R.drawable.reservation_background_green);
        button.setTextColor(Color.parseColor("#ffffff"));
    }

    private void sfmMeals() {
        saturdayBreakfast.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String state = "شنبه > صبحانه";
                if (sfxWeeklyList.getResult().getDay0().get(0).getStatus().equals("invalid")) {
                    if (sfxWeeklyList.getResult().getDay0().get(0).getName().equals("empty")) {
                        Toast.makeText(rootView.getContext(), "شما برای این وعده غذایی انتخاب نکرده اید!", Toast.LENGTH_SHORT).show();
                    } else {
                        showMealItem(sfxWeeklyList.getResult().getDay0().get(0).getName(),
                                sfxWeeklyList.getResult().getDay0().get(0).getRestaurant(),
                                sfxWeeklyList.getResult().getDay0().get(0).getPrice(),
                                state);
                    }
                } else if (sfxWeeklyList.getResult().getDay0().get(0).getStatus().equals("valid")) {
                    if (sfxWeeklyList.getResult().getDay0().get(0).getName().equals("empty")) {
                        //chooseMealItem(sfxWeeklyList.getResult().getRestaurants());
                    } else {
                        // deleteMealItem();
                    }
                }
            }
        });

        monday_Lunch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String state = "دوشنبه > ناهار";
                if (sfxWeeklyList.getResult().getDay2().get(1).getStatus().equals("invalid")) {
                    if (sfxWeeklyList.getResult().getDay2().get(1).getName().equals("empty")) {
                        Toast.makeText(rootView.getContext(), "این وعده غذایی انتخاب نشده!", Toast.LENGTH_SHORT).show();
                    } else {
                        showMealItem(sfxWeeklyList.getResult().getDay2().get(1).getName(),
                                sfxWeeklyList.getResult().getDay2().get(1).getRestaurant(),
                                sfxWeeklyList.getResult().getDay2().get(1).getPrice(),
                                state);
                    }
                } else if (sfxWeeklyList.getResult().getDay2().get(1).getStatus().equals("valid")) {
                    if (sfxWeeklyList.getResult().getDay2().get(1).getName().equals("empty")) {
                        chooseMealItem(sfxWeeklyList.getResult().getDay2().get(1).getMealCode(), sfxWeeklyList.getResult().getDay2().get(3).getDateCode(), state);
                    } else {
                    }
                }
            }
        });
    }

    @SuppressLint("SetTextI18n")
    private void showMealItem(String name, String restaurant, String price, String state) {
        final Dialog dialog = new Dialog(rootView.getContext());
        dialog.setContentView(R.layout.sfx_show_meal_item);

        TextView textViewName = dialog.findViewById(R.id.sfxShowMeal_name);
        TextView textViewRestaurant = dialog.findViewById(R.id.sfxShowMeal_restaurant);
        TextView textViewPrice = dialog.findViewById(R.id.sfxShowMeal_price);
        TextView textViewState = dialog.findViewById(R.id.sfxShowMeal_State);

        textViewName.setText(name);
        textViewRestaurant.setText(restaurant);
        textViewPrice.setText(price + " ریال ");
        textViewState.setText(state);

        Button cancel = dialog.findViewById(R.id.sfxShowMeal_close);

        dialog.setCancelable(false);

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.show();
    }

    private void chooseMealItem(final String mealCode, final String dateCode, String status) {
        final Dialog dialog = new Dialog(rootView.getContext());
        dialog.setContentView(R.layout.sfx_choose_meal_item);
        dialog.setCancelable(false);

        restaurant = dialog.findViewById(R.id.sfxChoose_Restaurant);
        food = dialog.findViewById(R.id.sfxChoose_food);
        buy = dialog.findViewById(R.id.sfxChoose_Buy);
        Button close = dialog.findViewById(R.id.sfxChoose_Close);
        TextView state = dialog.findViewById(R.id.sfxChoose_State);

        restaurant.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getRestaurantCode();
            }
        });

        food.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getFood(cookie, mealCode, dateCode, restaurantCode);
            }
        });

        buy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buyFood(cookie, mealCode, dateCode, restaurantCode, foodCode);
                dialog.dismiss();
            }
        });

        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        state.setText(status);

        dialog.show();
    }

    private void getRestaurantCode() {
        final Dialog dialog = new Dialog(rootView.getContext());
        dialog.setContentView(R.layout.sfx_list);

        ListView listView = dialog.findViewById(R.id.sfxList_ListView);

        List<String> items = new ArrayList<>();
        for (int i = 0; i < sfxWeeklyList.getResult().getRestaurants().size(); i++) {
            items.add(sfxWeeklyList.getResult().getRestaurants().get(i).getName());
        }

        final ArrayAdapter<String> adapter = new ArrayAdapter<String>(rootView.getContext(), android.R.layout.simple_list_item_1, items);
        listView.setAdapter(adapter);


        ImageView close = dialog.findViewById(R.id.sfxList_close);
        dialog.setCancelable(false);
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        TextView state = dialog.findViewById(R.id.sfxList_State);
        state.setText("انتخاب رستوران");

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                for (int i = 0; i < sfxWeeklyList.getResult().getRestaurants().size(); i++) {
                    if (adapter.getItem(position).equals(sfxWeeklyList.getResult().getRestaurants().get(i).getName())) {
                        restaurantCode = sfxWeeklyList.getResult().getRestaurants().get(i).getCode();
                        dialog.dismiss();
                        restaurant.setText(adapter.getItem(position));
                        restaurant.setBackgroundResource(R.drawable.sfx_select_background);
                    }
                }
            }
        });
        dialog.show();
    }

    private void getFood(String cookie, String mealCode, String dateCode, String restaurantCode) {
        Log.i(MyLog.SESS, cookie);
        Log.i(MyLog.SESS, mealCode);
        Log.i(MyLog.SESS, dateCode);
        Log.i(MyLog.SESS, restaurantCode);

        Map<String, String> params = new HashMap<>();
        params.put("cookie", cookie);
        params.put("meal", mealCode);
        params.put("date", dateCode);
        params.put("restaurant", restaurantCode);
        String URI = MyConfig.SFX_MEAL_DETAIL + "?" + HttpManager.enCodeParameters(params);

        StringRequest stringRequest = new StringRequest(Request.Method.GET,
                URI,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            sfxMealDetail = gson.fromJson(new String(response.getBytes("ISO-8859-1"), "UTF-8"), SFXMealDetail.class);
                            showFood();
                            Log.i(MyLog.SESS, sfxMealDetail.getOk().toString());
                            Log.i(MyLog.SESS, sfxMealDetail.getResult().get(0).getName());
                        } catch (UnsupportedEncodingException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                });
        queue.add(stringRequest);
    }

    private void showFood() {
        final Dialog dialog = new Dialog(rootView.getContext());
        dialog.setContentView(R.layout.sfx_list);

        ListView listView = dialog.findViewById(R.id.sfxList_ListView);

        List<String> items = new ArrayList<>();
        for (int i = 0; i < 1; i++) {
            items.add(sfxMealDetail.getResult().get(i).getName());
        }

        final ArrayAdapter<String> adapter = new ArrayAdapter<String>(rootView.getContext(), android.R.layout.simple_list_item_1, items);
        listView.setAdapter(adapter);


        ImageView close = dialog.findViewById(R.id.sfxList_close);
        dialog.setCancelable(false);
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        TextView state = dialog.findViewById(R.id.sfxList_State);
        state.setText("انتخاب غذا");

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                for (int i = 0; i < sfxMealDetail.getResult().size(); i++) {
                    if (adapter.getItem(position).equals(sfxMealDetail.getResult().get(i).getName())) {
                        foodCode = sfxMealDetail.getResult().get(i).getCode();
                        dialog.dismiss();
                        food.setText(adapter.getItem(position));
                        food.setBackgroundResource(R.drawable.sfx_select_background);
                    }
                }
            }
        });
        dialog.show();
    }

    private void buyFood(final String cookie, final String mealCode, final String dateCode, final String restaurantCode, final String foodCode) {

        StringRequest stringRequest = new StringRequest(Request.Method.POST,
                MyConfig.SFX_BUY_MEAL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        SFXWeeklyList newSFXWeeklyList = new SFXWeeklyList();
                        try {
                            sfxWeeklyList = gson.fromJson(new String(response.getBytes("ISO-8859-1"), "UTF-8"),
                                    SFXWeeklyList.class);
                            new FoodReservationAdapter(rootView, R.layout.fragment_food_reservation, sfxWeeklyList, cookie);
                        } catch (UnsupportedEncodingException e) {
                            e.printStackTrace();
                        }
                        CustomToast.success(rootView.getContext(), "وعده غذایی خریداری شد", Toast.LENGTH_LONG).show();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("cookie", cookie);
                params.put("meal", mealCode);
                params.put("date", dateCode);
                params.put("restaurant", restaurantCode);
                params.put("food", foodCode);
                return params;
            }
        };
        queue.add(stringRequest);
    }
}
