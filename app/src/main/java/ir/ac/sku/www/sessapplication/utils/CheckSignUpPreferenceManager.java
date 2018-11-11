package ir.ac.sku.www.sessapplication.utils;

import android.content.Context;
import android.content.SharedPreferences;

import ir.ac.sku.www.sessapplication.API.PreferenceName;

public class CheckSignUpPreferenceManager {

    private Context context;
    private SharedPreferences preferences;

    public CheckSignUpPreferenceManager(Context context) {
        this.context = context;
        preferences = context.getSharedPreferences(PreferenceName.SIGN_UP_PREFERENCE_NAME, Context.MODE_PRIVATE);
    }

    public boolean startSignUpPreference() {
        return preferences.getBoolean(PreferenceName.SIGN_UP_PREFERENCE_START_KEY, true);
    }

    public void setStartSignUpPreference(boolean starter) {
        preferences.edit().putBoolean(PreferenceName.SIGN_UP_PREFERENCE_START_KEY, starter).apply();
    }
}
