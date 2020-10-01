package ir.ac.sku.www.sessapplication.fragment;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
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

import butterknife.BindView;
import ir.ac.sku.www.sessapplication.R;
import ir.ac.sku.www.sessapplication.api.ApplicationAPI;
import ir.ac.sku.www.sessapplication.base.BaseDialogFragment;
import ir.ac.sku.www.sessapplication.fragment.dialogfragment.DialogFragmentInstantMessage;
import ir.ac.sku.www.sessapplication.model.LoginInformation;
import ir.ac.sku.www.sessapplication.model.SendInformation;
import ir.ac.sku.www.sessapplication.utils.MyHandler;
import ir.ac.sku.www.sessapplication.utils.SignIn;
import ir.ac.sku.www.sessapplication.utils.WebService;
import ir.ac.sku.www.sessapplication.utils.helper.ManagerHelper;

public class SignInDialogFragment extends BaseDialogFragment {

    @BindView(R.id.dialogUsernamePassword_GifImageViewEnter) LottieAnimationView gifImageViewEnter;
    @BindView(R.id.dialogUsernamePassword_EditTextUsername) EditText btn_Username;
    @BindView(R.id.dialogUsernamePassword_EditTextPassword) EditText btn_Password;
    @BindView(R.id.dialogUsernamePassword_Close) ImageView close;
    @BindView(R.id.dialogUsernamePassword_Enter) Button enter;

    //Required libraries
    private Gson gson;
    private RequestQueue queue;
    private MyHandler myHandler;
    private UserInterface userInterface;

    //my Class Model
    private LoginInformation loginInformation;
    private SendInformation sendInformation;

    public SignInDialogFragment(MyHandler myHandler) {
        this.myHandler = myHandler;
    }

    @Override public int getLayoutResource() {
        return R.layout.dialog_username_password;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        loginInformation = new LoginInformation();
        sendInformation = new SendInformation();

        gson = new Gson();
        queue = Volley.newRequestQueue(getActivity());

        enter.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("LongLogTag")
            @Override
            public void onClick(View v) {
                enter.setClickable(false);
                if (ManagerHelper.isInternet(getActivity())) {
                    ManagerHelper.noInternetAccess(getActivity());
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
    }

    @Override
    public void onResume() {
        super.onResume();
        changeDialogSize((int) (getDisplayMetrics().widthPixels * 0.65), ViewGroup.LayoutParams.WRAP_CONTENT);
    }

    private void getLoginInformationUsernamePassword(final MyHandler handler, final String username, final String password) {
        StringRequest request = new StringRequest(ApplicationAPI.LOGIN_INFORMATION,
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
                        new AlertDialog.Builder(getActivity())
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

            WebService webService = new WebService(getActivity());
            webService.requestPost(ApplicationAPI.SEND_INFORMATION, Request.Method.POST, params, new MyHandler() {
                @SuppressLint({"NewApi", "LongLogTag"})
                @Override
                public void onResponse(boolean ok, Object obj) {
                    sendInformation = gson.fromJson(new String(obj.toString().getBytes(StandardCharsets.ISO_8859_1), StandardCharsets.UTF_8), SendInformation.class);
                    dismiss();
                    if (sendInformation.isOk()) {
                        preferencesUtils.setUsername(username);
                        preferencesUtils.setPassword(password);
                        preferencesUtils.setCookie(loginInformation.getCookie());
                        preferencesUtils.setFirstName(sendInformation.getResult().getUserInformation().getName());
                        preferencesUtils.setLastName(sendInformation.getResult().getUserInformation().getFamily());
                        preferencesUtils.setMajor(sendInformation.getResult().getUserInformation().getMajor());

                        userInterface.addUserPersonalInfo(sendInformation.getResult().getUserInformation(), loginInformation.getCookie());

                        if (sendInformation.getResult().getInstantMessage().size() > 0) {
                            FragmentManager fragmentManager = ((FragmentActivity) getActivity()).getSupportFragmentManager();
                            new DialogFragmentInstantMessage(sendInformation.getResult()).show(fragmentManager, "DialogFragmentInstantMessage");
                        }
                        getUserImage();

                        preferencesUtils.setStartKey(false);
                        new SignIn(getActivity()).SignInDialog(handler);

                        if (getFragmentManager() != null) {
                            new WelcomeDialogFragment(
                                    " خوش آمدید " + sendInformation.getResult().getUserInformation().getName() + " " + sendInformation.getResult().getUserInformation().getFamily()
                            ).show(getFragmentManager(), "WelcomeDialogFragment");
                        }

                    } else if (!sendInformation.isOk()) {
                        Toast.makeText(getActivity(), sendInformation.getDescription().getErrorText(), Toast.LENGTH_SHORT).show();
                        if (getFragmentManager() != null) {
                            new SignInDialogFragment(handler).show(getFragmentManager(), "addUserPersonalInfo");
                        }
                    }
                }
            });
        }
    }

    @SuppressLint("LongLogTag")
    private void getUserImage() {
        Glide.with(getActivity())
                .asBitmap()
                .load(sendInformation.getResult().getUserInformation().getImage() + "?cookie=" + loginInformation.getCookie())
                .into(new SimpleTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                        resource.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
                        byte[] b = byteArrayOutputStream.toByteArray();
                        preferencesUtils.setImage(Base64.encodeToString(b, Base64.DEFAULT));
                    }
                });
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.userInterface = (UserInterface) context;
    }

    public interface UserInterface {
        void addUserPersonalInfo(SendInformation.Result.UserInformation userInformation, String cookie);
    }
}
