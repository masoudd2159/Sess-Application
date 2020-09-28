package ir.ac.sku.www.sessapplication.fragment;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Base64;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;

import com.airbnb.lottie.LottieAnimationView;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.google.gson.Gson;

import java.io.ByteArrayOutputStream;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Objects;

import ir.ac.sku.www.sessapplication.api.MyConfig;
import ir.ac.sku.www.sessapplication.api.PreferenceName;
import ir.ac.sku.www.sessapplication.R;
import ir.ac.sku.www.sessapplication.model.LoginInformation;
import ir.ac.sku.www.sessapplication.model.SendInformation;
import ir.ac.sku.www.sessapplication.utils.CheckSignUpPreferenceManager;
import ir.ac.sku.www.sessapplication.fragment.dialogfragment.DialogFragmentInstantMessage;
import ir.ac.sku.www.sessapplication.utils.HttpManager;
import ir.ac.sku.www.sessapplication.utils.MyHandler;
import ir.ac.sku.www.sessapplication.utils.SignIn;
import ir.ac.sku.www.sessapplication.utils.WebService;

import static android.content.Context.MODE_PRIVATE;

public class SignInDialogFragment extends DialogFragment {

    private View rootView;

    private LottieAnimationView gifImageViewEnter;
    private EditText btn_Username;
    private EditText btn_Password;
    private ImageView close;
    private Button enter;

    //Required libraries
    private Gson gson;
    private RequestQueue queue;
    private MyHandler myHandler;
    private UserInterface userInterface;

    //Preferences
    private CheckSignUpPreferenceManager manager;
    private SharedPreferences.Editor editSharedPreferences;

    //my Class Model
    private LoginInformation loginInformation;
    private SendInformation sendInformation;

    @SuppressLint("CommitPrefEdits")
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.dialog_username_password, container, false);

        if (getDialog() != null && getDialog().isShowing())
            dismiss();

        init();

        loginInformation = new LoginInformation();
        sendInformation = new SendInformation();

        gson = new Gson();
        queue = Volley.newRequestQueue(rootView.getContext());

        manager = new CheckSignUpPreferenceManager(rootView.getContext());
        SharedPreferences preferencesUserInformation = rootView.getContext().getSharedPreferences(PreferenceName.PREFERENCE_USER_INFORMATION, MODE_PRIVATE);
        editSharedPreferences = preferencesUserInformation.edit();

        getDialog().getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        WindowManager.LayoutParams layoutParams = getDialog().getWindow().getAttributes();
        layoutParams.dimAmount = 0.7f;
        getDialog().getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        getDialog().setCancelable(false);

        enter.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("LongLogTag")
            @Override
            public void onClick(View v) {
                enter.setClickable(false);
                if (HttpManager.isNOTOnline(rootView.getContext())) {
                    HttpManager.noInternetAccess(rootView.getContext());
                } else {
                    enter.setVisibility(View.INVISIBLE);
                    gifImageViewEnter.setVisibility(View.VISIBLE);
                    gifImageViewEnter.setAnimation("loading_1.json");
                    gifImageViewEnter.playAnimation();
                    gifImageViewEnter.loop(true);
                    getLoginInformationUsernamePassword(myHandler, btn_Username.getText().toString().trim(), btn_Password.getText().toString().trim());
                }
            }
        });

        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
                enter.setClickable(true);
            }
        });

        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        changeDialogSize();
    }

    private void init() {
        gifImageViewEnter = rootView.findViewById(R.id.dialogUsernamePassword_GifImageViewEnter);
        btn_Username = rootView.findViewById(R.id.dialogUsernamePassword_EditTextUsername);
        btn_Password = rootView.findViewById(R.id.dialogUsernamePassword_EditTextPassword);
        close = rootView.findViewById(R.id.dialogUsernamePassword_Close);
        enter = rootView.findViewById(R.id.dialogUsernamePassword_Enter);
    }

    public SignInDialogFragment(MyHandler myHandler) {
        this.myHandler = myHandler;
    }

    private void getLoginInformationUsernamePassword(final MyHandler handler, final String username, final String password) {
        StringRequest request = new StringRequest(MyConfig.LOGIN_INFORMATION,
                new Response.Listener<String>() {
                    @SuppressLint("NewApi")
                    @Override
                    public void onResponse(String response) {
                        loginInformation = gson.fromJson(new String(response.getBytes(StandardCharsets.ISO_8859_1), StandardCharsets.UTF_8), LoginInformation.class);

                        if (loginInformation.isOk()) {
                            sendParamsPostUsernamePassword(handler, username, password);
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
        queue.add(request);
    }

    private void sendParamsPostUsernamePassword(final MyHandler handler, final String username, final String password) {
        if (loginInformation.getCookie() == null) {
            getLoginInformationUsernamePassword(handler, username, password);
        } else if (loginInformation.getCookie() != null) {

            HashMap<String, String> params = new HashMap<String, String>();
            params.put("cookie", loginInformation.getCookie());
            params.put("username", username);
            params.put("password", password);

            WebService webService = new WebService(rootView.getContext());
            webService.requestPost(MyConfig.SEND_INFORMATION, Request.Method.POST, params, new MyHandler() {
                @SuppressLint({"NewApi", "LongLogTag"})
                @Override
                public void onResponse(boolean ok, Object obj) {
                    sendInformation = gson.fromJson(new String(obj.toString().getBytes(StandardCharsets.ISO_8859_1), StandardCharsets.UTF_8), SendInformation.class);
                    dismiss();
                    if (sendInformation.isOk()) {
                        editSharedPreferences.putString(PreferenceName.PREFERENCE_USERNAME, username);
                        editSharedPreferences.putString(PreferenceName.PREFERENCE_PASSWORD, password);
                        editSharedPreferences.putString(PreferenceName.PREFERENCE_COOKIE, loginInformation.getCookie());
                        editSharedPreferences.putString(PreferenceName.PREFERENCE_FIRST_NAME, sendInformation.getResult().getUserInformation().getName());
                        editSharedPreferences.putString(PreferenceName.PREFERENCE_LAST_NAME, sendInformation.getResult().getUserInformation().getFamily());
                        editSharedPreferences.putString(PreferenceName.PREFERENCE_MAJOR, sendInformation.getResult().getUserInformation().getMajor());
                        editSharedPreferences.apply();

                        userInterface.addUserPersonalInfo(
                                sendInformation.getResult().getUserInformation().getName(),
                                sendInformation.getResult().getUserInformation().getFamily(),
                                loginInformation.getCookie()
                        );

                        FragmentManager fragmentManager = ((FragmentActivity) rootView.getContext()).getSupportFragmentManager();
                        new DialogFragmentInstantMessage(sendInformation.getResult()).show(fragmentManager, "DialogFragmentInstantMessage");

                        getUserImage();

                        manager.setStartSignUpPreference(false);
                        new SignIn(rootView.getContext()).SignInDialog(handler);

                        if (getFragmentManager() != null) {
                            new WelcomeDialogFragment(
                                    " خوش آمدید " + sendInformation.getResult().getUserInformation().getName() + " " + sendInformation.getResult().getUserInformation().getFamily()
                            ).show(getFragmentManager(), "WelcomeDialogFragment");
                        }

                    } else if (!sendInformation.isOk()) {
                        Toast.makeText(rootView.getContext(), sendInformation.getDescription().getErrorText(), Toast.LENGTH_SHORT).show();
                        if (getFragmentManager() != null) {
                            new SignInDialogFragment(handler).show(getFragmentManager(),"addUserPersonalInfo");
                        }
                    }
                }
            });
        }
    }

    @SuppressLint("LongLogTag")
    private void getUserImage() {
        Glide.with(rootView.getContext())
                .asBitmap()
                .load(sendInformation.getResult().getUserInformation().getImage() + "?cookie=" + loginInformation.getCookie())
                .into(new SimpleTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                        resource.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
                        byte[] b = byteArrayOutputStream.toByteArray();
                        String encodedImage = Base64.encodeToString(b, Base64.DEFAULT);
                        editSharedPreferences.putString(PreferenceName.PREFERENCE_IMAGE, encodedImage);
                        editSharedPreferences.apply();
                    }
                });
    }

    public static interface UserInterface {
        void addUserPersonalInfo(String name, String family, String cookie);
    }

    @Override
    public void onAttach(@NonNull Activity activity) {
        super.onAttach(activity);
        this.userInterface = (UserInterface) activity;
    }

    private void changeDialogSize() {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        Objects.requireNonNull(getActivity()).getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        Objects.requireNonNull(Objects.requireNonNull(getDialog()).getWindow()).setLayout(
                (int) (displayMetrics.widthPixels * 0.7),
                ViewGroup.LayoutParams.WRAP_CONTENT);
    }
}
