package ir.ac.sku.www.sessapplication.adapters;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import ir.ac.sku.www.sessapplication.API.MyConfig;
import ir.ac.sku.www.sessapplication.API.MyLog;
import ir.ac.sku.www.sessapplication.API.PreferenceName;
import ir.ac.sku.www.sessapplication.R;
import ir.ac.sku.www.sessapplication.models.SFXMealDetail;
import ir.ac.sku.www.sessapplication.models.SFXWeeklyList;
import ir.ac.sku.www.sessapplication.utils.CustomToastSuccess;
import ir.ac.sku.www.sessapplication.utils.MyHandler;
import ir.ac.sku.www.sessapplication.utils.HttpManager;
import ir.ac.sku.www.sessapplication.utils.SignIn;
import ir.ac.sku.www.sessapplication.utils.WebService;

@SuppressLint("Registered")
public class FoodReservationAdapter {

    public static final String RESERVATION = "رزرو";

    //contractor
    private View rootView;
    private SFXWeeklyList sfxWeeklyList;
    private int resource;

    //My Views at Fragment
    private TextView tv_Credit;


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


    private TextView period;

    //My View at Layout
    private Button btn_Restaurant;
    private Button btn_Food;
    private Button btn_Buy;
    private Button btn_Delete;


    //Codes
    private String restaurantCode = null;
    private String foodCode = null;

    //My Library
    private RequestQueue queue;
    private Gson gson;

    //Preferences
    private SharedPreferences preferencesUsernameAndPassword;
    private SharedPreferences preferencesCookie;

    //My Java Model Class
    private SFXWeeklyList weeklyList;
    private SFXMealDetail sfxMealDetail;

    @SuppressLint("LongLogTag")
    public FoodReservationAdapter(@NonNull View rootView, int resource, @NonNull SFXWeeklyList sfxWeeklyList) {
        Log.i(MyLog.FOOD_RESERVATION_ADAPTER, "Food Reservation Adapter Run");
        this.rootView = rootView;
        this.resource = resource;
        this.sfxWeeklyList = sfxWeeklyList;

        //allocate Classes
        sfxMealDetail = new SFXMealDetail();

        //use Lib
        gson = new Gson();
        queue = Volley.newRequestQueue(rootView.getContext());
        preferencesUsernameAndPassword = rootView.getContext().getSharedPreferences(PreferenceName.USERNAME_AND_PASSWORD_PREFERENCE_NAME, Context.MODE_PRIVATE);
        preferencesCookie = rootView.getContext().getSharedPreferences(PreferenceName.COOKIE_PREFERENCE_NAME, Context.MODE_PRIVATE);

        //my Functions
        init();
        fillItems();
        sfxMeal();
    }

    private void init() {
        tv_Credit = rootView.findViewById(R.id.foodReservation_IncreaseCreditTextView);


        saturdayBreakfast = rootView.findViewById(R.id.foodReservation_SaturdayBreakfast);
        saturdayLunch = rootView.findViewById(R.id.foodReservation_SaturdayLunch);
        saturdayDinner = rootView.findViewById(R.id.foodReservation_SaturdayDinner);

        sundayBreakfast = rootView.findViewById(R.id.foodReservation_SundayBreakfast);
        sundayLunch = rootView.findViewById(R.id.foodReservation_SundayLunch);
        sundayDinner = rootView.findViewById(R.id.foodReservation_SundayDinner);

        monday_Breakfast = rootView.findViewById(R.id.foodReservation_MondayBreakfast);
        monday_Lunch = rootView.findViewById(R.id.foodReservation_MondayLunch);
        monday_Dinner = rootView.findViewById(R.id.foodReservation_MondayDinner);

        tuesdayBreakfast = rootView.findViewById(R.id.foodReservation_TuesdayBreakfast);
        tuesdayLunch = rootView.findViewById(R.id.foodReservation_TuesdayLunch);
        tuesdayDinner = rootView.findViewById(R.id.foodReservation_TuesdayDinner);

        wednesdayBreakfast = rootView.findViewById(R.id.foodReservation_WednesdayBreakfast);
        wednesdayLunch = rootView.findViewById(R.id.foodReservation_WednesdayLunch);
        wednesdayDinner = rootView.findViewById(R.id.foodReservation_WednesdayDinner);

        thursdayBreakfast = rootView.findViewById(R.id.foodReservation_ThursdayBreakfast);
        thursdayLunch = rootView.findViewById(R.id.foodReservation_ThursdayLunch);
        thursdayDinner = rootView.findViewById(R.id.foodReservation_ThursdayDinner);

        fridayBreakfast = rootView.findViewById(R.id.foodReservation_FridayBreakfast);
        fridayLunch = rootView.findViewById(R.id.foodReservation_FridayLunch);
        fridayDinner = rootView.findViewById(R.id.foodReservation_FridayDinner);

        period = rootView.findViewById(R.id.foodReservation_PeriodTextView);
    }

    @SuppressLint({"SetTextI18n", "LongLogTag"})
    private void fillItems() {
        if (sfxWeeklyList.isOk()) {
            tv_Credit.setText(" اعتبار : " + sfxWeeklyList.getResult().getCredit() + " ریال ");

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
                        buttons_Saturday[i].setText(RESERVATION);
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
                        buttons_Sunday[i].setText(RESERVATION);
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
                        buttons_Monday[i].setText(RESERVATION);
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
                        buttons_Tuesday[i].setText(RESERVATION);
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
                        buttons_Wednesday[i].setText(RESERVATION);
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
                        buttons_Thursday[i].setText(RESERVATION);
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
                        buttons_Friday[i].setText(RESERVATION);
                    } else {
                        validGreen(buttons_Friday[i]);
                        buttons_Friday[i].setText(sfxWeeklyList.getResult().getDay6().get(i).getName());
                    }
                }
            }
//__________________________________________________________________________________________________
            period.setText(sfxWeeklyList.getResult().getDay0().get(3).getDateText() + " - " + sfxWeeklyList.getResult().getDay6().get(3).getDateText());
        }
    }

    private void invalid(Button button) {
        button.setBackgroundResource(R.drawable.bg_reservation_black);
        button.setTextColor(Color.parseColor("#ffffff"));
    }

    private void validLiteGreen(Button button) {
        button.setBackgroundResource(R.drawable.bg_reservation_lite_green);
        button.setTextColor(Color.parseColor("#4CAF50"));
    }

    private void validGreen(Button button) {
        button.setBackgroundResource(R.drawable.bg_reservation_green);
        button.setTextColor(Color.parseColor("#ffffff"));
    }

    @SuppressLint("LongLogTag")
    private void sfxMeal() {
        Log.i(MyLog.FOOD_RESERVATION_ADAPTER, "OnLine");
        final String[] meals = {"صبحانه", "ناهار", "شام"};
//__________________________________________________________________________________________________
        //Saturday
        Button buttons_Saturday[] = {saturdayBreakfast, saturdayLunch, saturdayDinner};
        for (int i = 0; i < 3; i++) {
            final int finalI = i;
            buttons_Saturday[i].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String state = "شنبه" + " > " + meals[finalI];
                    if (sfxWeeklyList.getResult().getDay0().get(finalI).getStatus().equals("invalid")) {
                        if (sfxWeeklyList.getResult().getDay0().get(finalI).getName().equals("empty")) {
                            Toast.makeText(rootView.getContext(), "برنامه ریزی نشده", Toast.LENGTH_SHORT).show();
                        } else {
                            showMealItem(sfxWeeklyList.getResult().getDay0().get(finalI).getName(),
                                    sfxWeeklyList.getResult().getDay0().get(finalI).getRestaurant(),
                                    sfxWeeklyList.getResult().getDay0().get(finalI).getPrice(),
                                    state);
                        }
                    } else if (sfxWeeklyList.getResult().getDay0().get(finalI).getStatus().equals("valid")) {
                        if (sfxWeeklyList.getResult().getDay0().get(finalI).getName().equals("empty")) {
                            chooseMealItem(sfxWeeklyList.getResult().getDay0().get(finalI).getMealCode(),
                                    sfxWeeklyList.getResult().getDay0().get(3).getDateCode(),
                                    state);
                        } else {
                            deleteMealItem(sfxWeeklyList.getResult().getDay0().get(finalI).getData(),
                                    sfxWeeklyList.getResult().getDay0().get(finalI).getName(),
                                    sfxWeeklyList.getResult().getDay0().get(finalI).getRestaurant(),
                                    sfxWeeklyList.getResult().getDay0().get(finalI).getPrice(),
                                    state);
                        }
                    }
                }
            });
        }
//__________________________________________________________________________________________________
        //Sunday
        Button buttons_Sunday[] = {sundayBreakfast, sundayLunch, sundayDinner};
        for (int i = 0; i < 3; i++) {
            final int finalI = i;
            buttons_Sunday[i].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String state = "یک شنبه" + " > " + meals[finalI];
                    if (sfxWeeklyList.getResult().getDay1().get(finalI).getStatus().equals("invalid")) {
                        if (sfxWeeklyList.getResult().getDay1().get(finalI).getName().equals("empty")) {
                            Toast.makeText(rootView.getContext(), "برنامه ریزی نشده", Toast.LENGTH_SHORT).show();
                        } else {
                            showMealItem(sfxWeeklyList.getResult().getDay1().get(finalI).getName(),
                                    sfxWeeklyList.getResult().getDay1().get(finalI).getRestaurant(),
                                    sfxWeeklyList.getResult().getDay1().get(finalI).getPrice(),
                                    state);
                        }
                    } else if (sfxWeeklyList.getResult().getDay1().get(finalI).getStatus().equals("valid")) {
                        if (sfxWeeklyList.getResult().getDay1().get(finalI).getName().equals("empty")) {
                            chooseMealItem(sfxWeeklyList.getResult().getDay1().get(finalI).getMealCode(),
                                    sfxWeeklyList.getResult().getDay1().get(3).getDateCode(),
                                    state);
                        } else {
                            deleteMealItem(sfxWeeklyList.getResult().getDay1().get(finalI).getData(),
                                    sfxWeeklyList.getResult().getDay1().get(finalI).getName(),
                                    sfxWeeklyList.getResult().getDay1().get(finalI).getRestaurant(),
                                    sfxWeeklyList.getResult().getDay1().get(finalI).getPrice(),
                                    state);
                        }
                    }
                }
            });
        }
//__________________________________________________________________________________________________
        //Monday
        Button buttons_Monday[] = {monday_Breakfast, monday_Lunch, monday_Dinner};
        for (int i = 0; i < 3; i++) {
            final int finalI = i;
            buttons_Monday[i].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String state = "دوشنبه" + " > " + meals[finalI];
                    if (sfxWeeklyList.getResult().getDay2().get(finalI).getStatus().equals("invalid")) {
                        if (sfxWeeklyList.getResult().getDay2().get(finalI).getName().equals("empty")) {
                            Toast.makeText(rootView.getContext(), "برنامه ریزی نشده", Toast.LENGTH_SHORT).show();
                        } else {
                            showMealItem(sfxWeeklyList.getResult().getDay2().get(finalI).getName(),
                                    sfxWeeklyList.getResult().getDay2().get(finalI).getRestaurant(),
                                    sfxWeeklyList.getResult().getDay2().get(finalI).getPrice(),
                                    state);
                        }
                    } else if (sfxWeeklyList.getResult().getDay2().get(finalI).getStatus().equals("valid")) {
                        if (sfxWeeklyList.getResult().getDay2().get(finalI).getName().equals("empty")) {
                            chooseMealItem(sfxWeeklyList.getResult().getDay2().get(finalI).getMealCode(),
                                    sfxWeeklyList.getResult().getDay2().get(3).getDateCode(),
                                    state);
                        } else {
                            deleteMealItem(sfxWeeklyList.getResult().getDay2().get(finalI).getData(),
                                    sfxWeeklyList.getResult().getDay2().get(finalI).getName(),
                                    sfxWeeklyList.getResult().getDay2().get(finalI).getRestaurant(),
                                    sfxWeeklyList.getResult().getDay2().get(finalI).getPrice(),
                                    state);
                        }
                    }
                }
            });
        }
//__________________________________________________________________________________________________
        //Tuesday
        Button buttons_Tuesday[] = {tuesdayBreakfast, tuesdayLunch, tuesdayDinner};
        for (int i = 0; i < 3; i++) {
            final int finalI = i;
            buttons_Tuesday[i].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String state = "سه شنبه" + " > " + meals[finalI];
                    if (sfxWeeklyList.getResult().getDay3().get(finalI).getStatus().equals("invalid")) {
                        if (sfxWeeklyList.getResult().getDay3().get(finalI).getName().equals("empty")) {
                            Toast.makeText(rootView.getContext(), "برنامه ریزی نشده", Toast.LENGTH_SHORT).show();
                        } else {
                            showMealItem(sfxWeeklyList.getResult().getDay3().get(finalI).getName(),
                                    sfxWeeklyList.getResult().getDay3().get(finalI).getRestaurant(),
                                    sfxWeeklyList.getResult().getDay3().get(finalI).getPrice(),
                                    state);
                        }
                    } else if (sfxWeeklyList.getResult().getDay3().get(finalI).getStatus().equals("valid")) {
                        if (sfxWeeklyList.getResult().getDay3().get(finalI).getName().equals("empty")) {
                            chooseMealItem(sfxWeeklyList.getResult().getDay3().get(finalI).getMealCode(),
                                    sfxWeeklyList.getResult().getDay3().get(3).getDateCode(),
                                    state);
                        } else {
                            deleteMealItem(sfxWeeklyList.getResult().getDay3().get(finalI).getData(),
                                    sfxWeeklyList.getResult().getDay3().get(finalI).getName(),
                                    sfxWeeklyList.getResult().getDay3().get(finalI).getRestaurant(),
                                    sfxWeeklyList.getResult().getDay3().get(finalI).getPrice(),
                                    state);
                        }
                    }
                }
            });
        }
//__________________________________________________________________________________________________
        //Wednesday
        Button buttons_Wednesday[] = {wednesdayBreakfast, wednesdayLunch, wednesdayDinner};
        for (int i = 0; i < 3; i++) {
            final int finalI = i;
            buttons_Wednesday[i].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String state = "چهارشنبه" + " > " + meals[finalI];
                    if (sfxWeeklyList.getResult().getDay4().get(finalI).getStatus().equals("invalid")) {
                        if (sfxWeeklyList.getResult().getDay4().get(finalI).getName().equals("empty")) {
                            Toast.makeText(rootView.getContext(), "برنامه ریزی نشده", Toast.LENGTH_SHORT).show();
                        } else {
                            showMealItem(sfxWeeklyList.getResult().getDay4().get(finalI).getName(),
                                    sfxWeeklyList.getResult().getDay4().get(finalI).getRestaurant(),
                                    sfxWeeklyList.getResult().getDay4().get(finalI).getPrice(),
                                    state);
                        }
                    } else if (sfxWeeklyList.getResult().getDay4().get(finalI).getStatus().equals("valid")) {
                        if (sfxWeeklyList.getResult().getDay4().get(finalI).getName().equals("empty")) {
                            chooseMealItem(sfxWeeklyList.getResult().getDay4().get(finalI).getMealCode(),
                                    sfxWeeklyList.getResult().getDay4().get(3).getDateCode(),
                                    state);
                        } else {
                            deleteMealItem(sfxWeeklyList.getResult().getDay4().get(finalI).getData(),
                                    sfxWeeklyList.getResult().getDay4().get(finalI).getName(),
                                    sfxWeeklyList.getResult().getDay4().get(finalI).getRestaurant(),
                                    sfxWeeklyList.getResult().getDay4().get(finalI).getPrice(),
                                    state);
                        }
                    }
                }
            });
        }
//__________________________________________________________________________________________________
        //Thursday
        Button buttons_Thursday[] = {thursdayBreakfast, thursdayLunch, thursdayDinner};
        for (int i = 0; i < 3; i++) {
            final int finalI = i;
            buttons_Thursday[i].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String state = "پنج شنبه" + " > " + meals[finalI];
                    if (sfxWeeklyList.getResult().getDay5().get(finalI).getStatus().equals("invalid")) {
                        if (sfxWeeklyList.getResult().getDay5().get(finalI).getName().equals("empty")) {
                            Toast.makeText(rootView.getContext(), "برنامه ریزی نشده", Toast.LENGTH_SHORT).show();
                        } else {
                            showMealItem(sfxWeeklyList.getResult().getDay5().get(finalI).getName(),
                                    sfxWeeklyList.getResult().getDay5().get(finalI).getRestaurant(),
                                    sfxWeeklyList.getResult().getDay5().get(finalI).getPrice(),
                                    state);
                        }
                    } else if (sfxWeeklyList.getResult().getDay5().get(finalI).getStatus().equals("valid")) {
                        if (sfxWeeklyList.getResult().getDay5().get(finalI).getName().equals("empty")) {
                            chooseMealItem(sfxWeeklyList.getResult().getDay5().get(finalI).getMealCode(),
                                    sfxWeeklyList.getResult().getDay5().get(3).getDateCode(),
                                    state);
                        } else {
                            deleteMealItem(sfxWeeklyList.getResult().getDay5().get(finalI).getData(),
                                    sfxWeeklyList.getResult().getDay5().get(finalI).getName(),
                                    sfxWeeklyList.getResult().getDay5().get(finalI).getRestaurant(),
                                    sfxWeeklyList.getResult().getDay5().get(finalI).getPrice(),
                                    state);
                        }
                    }
                }
            });
        }
//__________________________________________________________________________________________________
        //Friday
        Button buttons_Friday[] = {fridayBreakfast, fridayLunch, fridayDinner};
        for (int i = 0; i < 3; i++) {
            final int finalI = i;
            buttons_Friday[i].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String state = "جمعه" + " > " + meals[finalI];
                    if (sfxWeeklyList.getResult().getDay6().get(finalI).getStatus().equals("invalid")) {
                        if (sfxWeeklyList.getResult().getDay6().get(finalI).getName().equals("empty")) {
                            Toast.makeText(rootView.getContext(), "برنامه ریزی نشده", Toast.LENGTH_SHORT).show();
                        } else {
                            showMealItem(sfxWeeklyList.getResult().getDay6().get(finalI).getName(),
                                    sfxWeeklyList.getResult().getDay6().get(finalI).getRestaurant(),
                                    sfxWeeklyList.getResult().getDay6().get(finalI).getPrice(),
                                    state);
                        }
                    } else if (sfxWeeklyList.getResult().getDay6().get(finalI).getStatus().equals("valid")) {
                        if (sfxWeeklyList.getResult().getDay6().get(finalI).getName().equals("empty")) {
                            chooseMealItem(sfxWeeklyList.getResult().getDay6().get(finalI).getMealCode(),
                                    sfxWeeklyList.getResult().getDay6().get(3).getDateCode(),
                                    state);
                        } else {
                            deleteMealItem(sfxWeeklyList.getResult().getDay6().get(finalI).getData(),
                                    sfxWeeklyList.getResult().getDay6().get(finalI).getName(),
                                    sfxWeeklyList.getResult().getDay6().get(finalI).getRestaurant(),
                                    sfxWeeklyList.getResult().getDay6().get(finalI).getPrice(),
                                    state);
                        }
                    }
                }
            });
        }
    }

    @SuppressLint({"SetTextI18n", "LongLogTag"})
    private void showMealItem(String name, String restaurant, String price, String status) {
        Log.i(MyLog.FOOD_RESERVATION_ADAPTER, "Show Meal Item : " + status);
        final Dialog dialog = new Dialog(rootView.getContext());
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        WindowManager.LayoutParams layoutParams = dialog.getWindow().getAttributes();
        layoutParams.dimAmount = 0.7f;
        dialog.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        dialog.setContentView(R.layout.sfx_show_meal_item);
        dialog.setCancelable(false);

        TextView textViewName = dialog.findViewById(R.id.sfxShowMeal_name);
        TextView textViewRestaurant = dialog.findViewById(R.id.sfxShowMeal_restaurant);
        TextView textViewPrice = dialog.findViewById(R.id.sfxShowMeal_price);
        TextView textViewState = dialog.findViewById(R.id.sfxShowMeal_State);

        textViewName.setText(name);
        textViewRestaurant.setText(restaurant);
        textViewPrice.setText(price + " ریال ");
        textViewState.setText(status);

        Button cancel = dialog.findViewById(R.id.sfxShowMeal_Close);

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i(MyLog.FOOD_RESERVATION_ADAPTER, "on Close Clicked");
                dialog.dismiss();
            }
        });

        dialog.show();
    }

    @SuppressLint("LongLogTag")
    private void chooseMealItem(final String mealCode, final String dateCode, String status) {
        Log.i(MyLog.FOOD_RESERVATION_ADAPTER, "Choose Meal Item : " + status);
        restaurantCode = null;
        foodCode = null;
        if (HttpManager.isNOTOnline(rootView.getContext())) {
            Log.i(MyLog.FOOD_RESERVATION_ADAPTER, "OFFLine");
            HttpManager.noInternetAccess(rootView.getContext());
        } else {
            Log.i(MyLog.FOOD_RESERVATION_ADAPTER, "OnLine");

            final Dialog dialog = new Dialog(rootView.getContext());
            dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            WindowManager.LayoutParams layoutParams = dialog.getWindow().getAttributes();
            layoutParams.dimAmount = 0.7f;
            dialog.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
            dialog.setContentView(R.layout.sfx_choose_meal_item);
            dialog.setCancelable(false);

            btn_Restaurant = dialog.findViewById(R.id.sfxChoose_Restaurant);
            btn_Food = dialog.findViewById(R.id.sfxChoose_food);
            btn_Buy = dialog.findViewById(R.id.sfxChoose_Buy);
            Button close = dialog.findViewById(R.id.sfxChoose_Close);
            TextView state = dialog.findViewById(R.id.sfxChoose_State);
            btn_Food.setEnabled(false);
            btn_Food.setClickable(false);

            state.setText(status);

            btn_Restaurant.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    btn_Food.setEnabled(true);
                    btn_Food.setClickable(true);
                    Log.i(MyLog.FOOD_RESERVATION_ADAPTER, "on Restaurant Click Listener");
                    foodCode = null;
                    restaurantCode = null;
                    btn_Food.setText("انتخاب کنید");
                    btn_Food.setBackgroundResource(R.drawable.bg_grey_solid);

                    getRestaurantCode();
                    Log.i(MyLog.FOOD_RESERVATION_ADAPTER, "Restaurant Text : " + btn_Restaurant.getText());
                    Log.i(MyLog.FOOD_RESERVATION_ADAPTER, "Restaurant Code : " + restaurantCode);
                }
            });

            btn_Food.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    btn_Food.setEnabled(false);
                    btn_Food.setClickable(false);
                    Log.i(MyLog.FOOD_RESERVATION_ADAPTER, "on Food Click Listener");
                    if (HttpManager.isNOTOnline(rootView.getContext())) {
                        Log.i(MyLog.FOOD_RESERVATION_ADAPTER, "OFFLine");
                        HttpManager.noInternetAccess(rootView.getContext());
                        dialog.dismiss();
                    } else {
                        Log.i(MyLog.FOOD_RESERVATION_ADAPTER, "OnLine");
                        if (restaurantCode != null) {
                            getFood(mealCode, dateCode, restaurantCode);
                            Log.i(MyLog.FOOD_RESERVATION_ADAPTER, "Food Text : " + btn_Food.getText());
                            Log.i(MyLog.FOOD_RESERVATION_ADAPTER, "Food Code : " + foodCode);
                        }
                    }
                }
            });

            btn_Buy.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.i(MyLog.FOOD_RESERVATION_ADAPTER, "on Buy Click Listener");
                    if (HttpManager.isNOTOnline(rootView.getContext())) {
                        Log.i(MyLog.FOOD_RESERVATION_ADAPTER, "OFFLine");
                        HttpManager.noInternetAccess(rootView.getContext());
                        dialog.dismiss();
                    } else {
                        Log.i(MyLog.FOOD_RESERVATION_ADAPTER, "OnLine");
                        if (restaurantCode != null && foodCode != null) {
                            buyFood(mealCode, dateCode, restaurantCode, foodCode);
                            dialog.dismiss();
                        }
                    }
                }
            });

            close.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.i(MyLog.FOOD_RESERVATION_ADAPTER, "on Close Clicked");
                    dialog.dismiss();
                }
            });

            dialog.show();
        }
    }

    @SuppressLint("LongLogTag")
    private void getRestaurantCode() {
        Log.i(MyLog.FOOD_RESERVATION_ADAPTER, "get Restaurant Code Start");
        final Dialog dialog = new Dialog(rootView.getContext());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setContentView(R.layout.sfx_list);
        dialog.setCancelable(false);

        ListView listView = dialog.findViewById(R.id.sfxList_ListView);

        List<String> items = new ArrayList<>();
        for (int i = 0; i < sfxWeeklyList.getResult().getRestaurants().size(); i++) {
            items.add(sfxWeeklyList.getResult().getRestaurants().get(i).getName());
        }

        final ArrayAdapter<String> adapter = new ArrayAdapter<String>(rootView.getContext(), android.R.layout.simple_list_item_1, items);
        listView.setAdapter(adapter);


        ImageView close = dialog.findViewById(R.id.sfxList_close);
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i(MyLog.FOOD_RESERVATION_ADAPTER, "on Image Close Clicked");
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
                        Log.i(MyLog.FOOD_RESERVATION_ADAPTER, "Restaurant Text : " + sfxWeeklyList.getResult().getRestaurants().get(i).getName());
                        Log.i(MyLog.FOOD_RESERVATION_ADAPTER, "Restaurant Code : " + restaurantCode);
                        dialog.dismiss();
                        btn_Restaurant.setText(adapter.getItem(position));
                        btn_Restaurant.setBackgroundResource(R.drawable.bg_sky_solid);
                    }
                }
            }
        });
        dialog.show();
    }

    @SuppressLint("LongLogTag")
    private void getFood(String meal_code, String date_code, String restaurant_code) {
        Log.i(MyLog.FOOD_RESERVATION_ADAPTER, "Get Food Start");

        Map<String, String> params = new HashMap<>();
        params.put("meal", meal_code);
        params.put("date", date_code);
        params.put("restaurant", restaurant_code);
        String URI = MyConfig.SFX_MEAL_DETAIL + "?" + HttpManager.enCodeParameters(params);

        Log.i(MyLog.FOOD_RESERVATION_ADAPTER, "mealCode       : " + params.get("meal"));
        Log.i(MyLog.FOOD_RESERVATION_ADAPTER, "dateCode       : " + params.get("date"));
        Log.i(MyLog.FOOD_RESERVATION_ADAPTER, "restaurantCode : " + params.get("restaurant"));

        WebService webService = new WebService(rootView.getContext());
        webService.request(URI, Request.Method.GET, new MyHandler() {
            @SuppressLint("NewApi")
            @Override
            public void onResponse(boolean ok, Object obj) {
                if (ok) {
                    try {
                        sfxMealDetail = gson.fromJson(new String(obj.toString().getBytes("ISO-8859-1"), "UTF-8"), SFXMealDetail.class);
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }
                    if (sfxMealDetail.getOk()) {
                        if (sfxMealDetail.getResult().size() <= 0) {
                            Log.i(MyLog.FOOD_RESERVATION_ADAPTER, "No food found");
                            Toast.makeText(rootView.getContext(), "هیچ غذایی یافت نشد", Toast.LENGTH_SHORT).show();
                            btn_Restaurant.setText("انتخاب کنید");
                            btn_Food.setText("انتخاب کنید");
                            btn_Restaurant.setBackgroundResource(R.drawable.bg_grey_solid);
                            btn_Food.setBackgroundResource(R.drawable.bg_grey_solid);
                            restaurantCode = null;
                            foodCode = null;
                        } else if (sfxMealDetail.getResult().size() > 0) {
                            Log.i(MyLog.FOOD_RESERVATION_ADAPTER, "Food : " + sfxMealDetail.getResult().get(0).getName());
                            showFood();
                        }
                    } else if (!sfxMealDetail.getOk()) {
                        restaurantCode = null;
                        foodCode = null;
                    }
                }
            }
        });
    }

    @SuppressLint("LongLogTag")
    private void showFood() {
        Log.i(MyLog.FOOD_RESERVATION_ADAPTER, "Show Food Started");
        final Dialog dialog = new Dialog(rootView.getContext());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

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
                Log.i(MyLog.FOOD_RESERVATION_ADAPTER, "on Close Clicked");
                dialog.dismiss();
                btn_Food.setEnabled(true);
                btn_Food.setClickable(true);
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
                        Log.i(MyLog.FOOD_RESERVATION_ADAPTER, "Food Text : " + sfxMealDetail.getResult().get(i).getName());
                        Log.i(MyLog.FOOD_RESERVATION_ADAPTER, "Food Code : " + foodCode);
                        btn_Food.setText(adapter.getItem(position));
                        btn_Food.setBackgroundResource(R.drawable.bg_sky_solid);
                        dialog.dismiss();
                    }
                }
            }
        });
        dialog.show();
    }

    @SuppressLint("LongLogTag")
    private void buyFood(final String meal_code, final String date_code, final String restaurant_code, final String food_code) {
        Log.i(MyLog.FOOD_RESERVATION_ADAPTER, "Buy Food Run");
        weeklyList = new SFXWeeklyList();

        StringRequest stringRequest = new StringRequest(Request.Method.POST,
                MyConfig.SFX_BUY_MEAL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            weeklyList = gson.fromJson(new String(response.getBytes("ISO-8859-1"), "UTF-8"), SFXWeeklyList.class);
                            if (weeklyList.isOk()) {
                                sfxWeeklyList = weeklyList;
                                Log.i(MyLog.FOOD_RESERVATION_ADAPTER, sfxWeeklyList.getResult().getMessage());
                                new FoodReservationAdapter(rootView, R.layout.fragment_food_reservation, sfxWeeklyList);
                                CustomToastSuccess.success(rootView.getContext(), "وعده غذایی خریداری شد", Toast.LENGTH_SHORT).show();
                            } else if (!weeklyList.isOk()) {
                                Log.i(MyLog.FOOD_RESERVATION_ADAPTER, weeklyList.getDescription().getErrorText());
                                restaurantCode = null;
                                foodCode = null;
                                if (Integer.parseInt(weeklyList.getDescription().getErrorCode()) > 0) {
                                    HttpManager.unsuccessfulOperation(rootView.getContext(), weeklyList.getDescription().getErrorText());
                                } else if (Integer.parseInt(weeklyList.getDescription().getErrorCode()) < 0) {
                                    SignIn signIn = new SignIn(rootView.getContext());
                                   // signIn.SignInDialog();
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
                }
        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("cookie", preferencesCookie.getString(PreferenceName.COOKIE_PREFERENCE_COOKIE, null));
                params.put("meal", meal_code);
                params.put("date", date_code);
                params.put("restaurant", restaurant_code);
                params.put("food", food_code);
                return params;
            }
        };
        queue.add(stringRequest);
    }

    @SuppressLint({"SetTextI18n", "LongLogTag"})
    private void deleteMealItem(final String data, String name, String restaurant, String price, String status) {
        Log.i(MyLog.FOOD_RESERVATION_ADAPTER, "Delete Meal Item : " + status);
        final Dialog dialog = new Dialog(rootView.getContext());
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        WindowManager.LayoutParams layoutParams = dialog.getWindow().getAttributes();
        layoutParams.dimAmount = 0.7f;
        dialog.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        dialog.setContentView(R.layout.sfx_delete_meal_item);
        dialog.setCancelable(false);

        btn_Delete = dialog.findViewById(R.id.sfxDeleteMeal_Delete);
        Button close = dialog.findViewById(R.id.sfxDeleteMeal_Close);
        TextView textViewName = dialog.findViewById(R.id.sfxDeleteMeal_name);
        TextView textViewRestaurant = dialog.findViewById(R.id.sfxDeleteMeal_restaurant);
        TextView textViewPrice = dialog.findViewById(R.id.sfxDeleteMeal_price);
        TextView textViewState = dialog.findViewById(R.id.sfxDeleteMeal_State);

        textViewName.setText(name);
        textViewRestaurant.setText(restaurant);
        textViewPrice.setText(price + " ریال ");
        textViewState.setText(status);

        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i(MyLog.FOOD_RESERVATION_ADAPTER, "on Close Clicked");
                dialog.dismiss();
            }
        });

        btn_Delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i(MyLog.FOOD_RESERVATION_ADAPTER, "on Delete Click Listener");
                if (HttpManager.isNOTOnline(rootView.getContext())) {
                    Log.i(MyLog.FOOD_RESERVATION_ADAPTER, "OFFLine");
                    HttpManager.noInternetAccess(rootView.getContext());
                    dialog.dismiss();
                } else {
                    Log.i(MyLog.FOOD_RESERVATION_ADAPTER, "OnLine");
                    deleteFood(data);
                    dialog.dismiss();
                }
            }
        });
        dialog.show();
    }

    @SuppressLint("LongLogTag")
    private void deleteFood(final String data) {
        Log.i(MyLog.FOOD_RESERVATION_ADAPTER, "Delete Food Run");

        Map<String, String> params = new HashMap<String, String>();
        params.put("username", preferencesUsernameAndPassword.getString(PreferenceName.USERNAME_AND_PASSWORD_PREFERENCE_USERNAME, null));
        params.put("data", data);
        String URI = MyConfig.SFX_DELETE_MEAL + "?" + HttpManager.enCodeParameters(params);
        weeklyList = new SFXWeeklyList();



        WebService webService = new WebService(rootView.getContext());
        webService.request(URI, Request.Method.DELETE, new MyHandler() {
            @SuppressLint("NewApi")
            @Override
            public void onResponse(boolean ok, Object obj) {
                if (ok) {
                    try {
                        weeklyList = gson.fromJson(new String(obj.toString().getBytes("ISO-8859-1"), "UTF-8"), SFXWeeklyList.class);
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }
                    if (weeklyList.isOk()) {
                        sfxWeeklyList = weeklyList;
                        Log.i(MyLog.FOOD_RESERVATION_ADAPTER, sfxWeeklyList.getResult().getMessage());
                        new FoodReservationAdapter(rootView, R.layout.fragment_food_reservation, sfxWeeklyList);
                        CustomToastSuccess.success(rootView.getContext(), "وعده غذایی حذف شد", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }
}
