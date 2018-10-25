package ir.ac.sku.www.sessapplication.utils;

import android.content.Context;
import android.content.SharedPreferences;

public class FirstPreferenceManager {

    private Context context;
    private SharedPreferences preferences;

    private static final String PREFERENCE_NAME = "FirstPreference";
    private static final String START_KEY = "StartPreference";


    public FirstPreferenceManager(Context context) {
        this.context = context;
        preferences = context.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE);
    }

    public boolean start() {
        return preferences.getBoolean(START_KEY, true);
    }

    public void setStart(boolean starter) {
        preferences.edit().putBoolean(START_KEY, starter).apply();
    }
}
