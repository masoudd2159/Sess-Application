package ir.ac.sku.www.sessapplication.adapters;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import ir.ac.sku.www.sessapplication.R;
import ir.ac.sku.www.sessapplication.models.SFXWeeklyList;

public class FoodReservationAdapter {

    private View rootView;
    private SFXWeeklyList sfxWeeklyList;
    private int resource;

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

    public FoodReservationAdapter(@NonNull View rootView, int resource, @NonNull SFXWeeklyList sfxWeeklyList) {
        this.rootView = rootView;
        this.resource = resource;
        this.sfxWeeklyList = sfxWeeklyList;
        init();
        fileItems();
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
}
