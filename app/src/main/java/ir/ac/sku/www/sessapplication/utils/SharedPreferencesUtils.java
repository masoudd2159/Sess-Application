package ir.ac.sku.www.sessapplication.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;

public class SharedPreferencesUtils {

    private static final String PREFERENCE_USER_INFORMATION = "UserInformation";

    private static final String PREFERENCE_USERNAME = "Preferences_Username";
    private static final String PREFERENCE_PASSWORD = "Preferences_Password";
    private static final String PREFERENCE_COOKIE = "Preferences_Cookie";
    private static final String PREFERENCE_FIRST_NAME = "Preferences_FirstName";
    private static final String PREFERENCE_LAST_NAME = "Preferences_LastName";
    private static final String PREFERENCE_IMAGE = "Preferences_Image";
    private static final String PREFERENCE_MAJOR = "Preferences_Major";
    private static final String PREFERENCE_START_KEY = "Preferences_StartKey";

    private SharedPreferences preferencesUserInformation;
    private SharedPreferences.Editor editSharedPreferences;


    @SuppressLint("CommitPrefEdits")
    public SharedPreferencesUtils(Context context) {
        preferencesUserInformation = context.getSharedPreferences(PREFERENCE_USER_INFORMATION, Context.MODE_PRIVATE);
        editSharedPreferences = preferencesUserInformation.edit();
    }

    public String getCookie() {
        return preferencesUserInformation.getString(PREFERENCE_COOKIE, null);
    }

    public void setCookie(String cookie) {
        editSharedPreferences.putString(PREFERENCE_COOKIE, cookie.trim()).apply();
    }

    public String getFirstName() {
        return preferencesUserInformation.getString(PREFERENCE_FIRST_NAME, null);
    }

    public void setFirstName(String firstName) {
        editSharedPreferences.putString(PREFERENCE_FIRST_NAME, firstName.trim()).apply();
    }

    public String getImage() {
        return preferencesUserInformation.getString(PREFERENCE_IMAGE, null);
    }

    public void setImage(String image) {
        editSharedPreferences.putString(PREFERENCE_IMAGE, image.trim()).apply();
    }

    public Bitmap getImageBitmap() {
        if (getImage() != null) {
            byte[] buffer = Base64.decode(getImage(), Base64.DEFAULT);
            return BitmapFactory.decodeByteArray(buffer, 0, buffer.length);
        }
        return null;
    }

    public String getLastName() {
        return preferencesUserInformation.getString(PREFERENCE_LAST_NAME, null);
    }

    public void setLastName(String lastName) {
        editSharedPreferences.putString(PREFERENCE_LAST_NAME, lastName.trim()).apply();
    }

    public String getMajor() {
        return preferencesUserInformation.getString(PREFERENCE_MAJOR, null);
    }

    public void setMajor(String major) {
        editSharedPreferences.putString(PREFERENCE_MAJOR, major.trim()).apply();
    }

    public String getPassword() {
        return preferencesUserInformation.getString(PREFERENCE_PASSWORD, null);
    }

    public void setPassword(String password) {
        editSharedPreferences.putString(PREFERENCE_PASSWORD, password.trim()).apply();
    }

    public String getUsername() {
        return preferencesUserInformation.getString(PREFERENCE_USERNAME, null);
    }

    public void setUsername(String username) {
        editSharedPreferences.putString(PREFERENCE_USERNAME, username.trim()).apply();
    }

    public boolean isStartKey() {
        return preferencesUserInformation.getBoolean(PREFERENCE_START_KEY, true);
    }

    public void setStartKey(boolean startKey) {
        editSharedPreferences.putBoolean(PREFERENCE_START_KEY, startKey).apply();
    }

    public void clearSharedPreferences() {
        editSharedPreferences.clear().apply();
        setStartKey(true);
    }
}
