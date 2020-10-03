package ir.ac.sku.www.sessapplication.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.util.Base64;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;

import java.io.ByteArrayOutputStream;

import ir.ac.sku.www.sessapplication.R;
import ir.ac.sku.www.sessapplication.api.ApplicationAPI;
import ir.ac.sku.www.sessapplication.model.information.SendInformation;

public class SharedPreferencesUtils {

    private static final String PREFERENCE_USER_INFORMATION = "UserInformation";

    private static final String PREFERENCE_COOKIE = "Preferences_Cookie";
    private static final String PREFERENCE_FIRST_NAME = "Preferences_FirstName";
    private static final String PREFERENCE_IMAGE = "Preferences_Image";
    private static final String PREFERENCE_LAST_NAME = "Preferences_LastName";
    private static final String PREFERENCE_MAJOR = "Preferences_Major";
    private static final String PREFERENCE_MOBILE = "Preferences_Mobile";
    private static final String PREFERENCE_PASSWORD = "Preferences_Password";
    private static final String PREFERENCE_SEX = "Preferences_Sex";
    private static final String PREFERENCE_TYPE = "Preferences_Type";
    private static final String PREFERENCE_USERNAME = "Preferences_Username";


    private SharedPreferences preferencesUserInformation;
    private SharedPreferences.Editor editSharedPreferences;

    private Context context;

    @SuppressLint("CommitPrefEdits")
    public SharedPreferencesUtils(Context context) {
        preferencesUserInformation = context.getSharedPreferences(PREFERENCE_USER_INFORMATION, Context.MODE_PRIVATE);
        editSharedPreferences = preferencesUserInformation.edit();
        this.context = context;
    }

    public String getCookie() {
        return preferencesUserInformation.getString(PREFERENCE_COOKIE, null);
    }

    public void setCookie(String cookie) {
        editSharedPreferences.putString(PREFERENCE_COOKIE, cookie.trim()).apply();
    }

    public String getUsername() {
        return preferencesUserInformation.getString(PREFERENCE_USERNAME, null);
    }

    public void setUsername(String username) {
        editSharedPreferences.putString(PREFERENCE_USERNAME, username.trim()).apply();
    }

    public String getPassword() {
        return preferencesUserInformation.getString(PREFERENCE_PASSWORD, null);
    }

    public void setPassword(String password) {
        editSharedPreferences.putString(PREFERENCE_PASSWORD, password.trim()).apply();
    }

    public String getFirstName() {
        return preferencesUserInformation.getString(PREFERENCE_FIRST_NAME, null);
    }

    public void setFirstName(String firstName) {
        editSharedPreferences.putString(PREFERENCE_FIRST_NAME, firstName.trim()).apply();
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

    public String getSex() {
        return preferencesUserInformation.getString(PREFERENCE_SEX, null);
    }

    public void setSex(String sex) {
        editSharedPreferences.putString(PREFERENCE_SEX, sex.trim()).apply();
    }

    public String getType() {
        return preferencesUserInformation.getString(PREFERENCE_TYPE, null);
    }

    public void setType(String type) {
        editSharedPreferences.putString(PREFERENCE_TYPE, type.trim()).apply();
    }

    public String getMobile() {
        return preferencesUserInformation.getString(PREFERENCE_MOBILE, null);
    }

    public void setMobile(String mobile) {
        editSharedPreferences.putString(PREFERENCE_MOBILE, mobile.trim()).apply();
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

    public void setImageBitmap(String cookie) {
        Glide.with(context)
                .asBitmap()
                .load(ApplicationAPI.IMAGE + cookie)
                .error(R.drawable.ic_university)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(new CustomTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                        resource.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
                        setImage(Base64.encodeToString(byteArrayOutputStream.toByteArray(), Base64.DEFAULT));
                    }

                    @Override public void onLoadCleared(@Nullable Drawable placeholder) {
                    }
                });
    }

    public void clearSharedPreferences() {
        editSharedPreferences.clear().apply();
    }

    public void savePreferences(String cookie, String username, String password, SendInformation.Result.UserInformation userInformation) {
        setCookie(cookie);
        setUsername(username);
        setPassword(password);
        setFirstName(userInformation.getName());
        setLastName(userInformation.getFamily());
        setMajor(userInformation.getMajor());
        setSex(userInformation.getSex());
        setType(userInformation.getType());
        setMobile(userInformation.getMobile());
        setImageBitmap(cookie);
    }
}
