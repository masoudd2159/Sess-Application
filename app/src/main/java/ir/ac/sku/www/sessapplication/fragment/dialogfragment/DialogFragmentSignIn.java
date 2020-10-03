package ir.ac.sku.www.sessapplication.fragment.dialogfragment;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.airbnb.lottie.LottieAnimationView;
import com.airbnb.lottie.LottieDrawable;

import java.util.HashMap;

import butterknife.BindView;
import ir.ac.sku.www.sessapplication.R;
import ir.ac.sku.www.sessapplication.api.MyLog;
import ir.ac.sku.www.sessapplication.base.BaseDialogFragment;
import ir.ac.sku.www.sessapplication.fragment.WelcomeDialogFragment;
import ir.ac.sku.www.sessapplication.model.information.LoginInformation;
import ir.ac.sku.www.sessapplication.model.information.SendInformation;
import ir.ac.sku.www.sessapplication.utils.MyHandler;
import ir.ac.sku.www.sessapplication.utils.helper.ManagerHelper;

public class DialogFragmentSignIn extends BaseDialogFragment {
    @BindView(R.id.dialogUsernamePassword_GifImageViewEnter) LottieAnimationView gifImageViewEnter;
    @BindView(R.id.dialogUsernamePassword_EditTextUsername) EditText editTextUsername;
    @BindView(R.id.dialogUsernamePassword_EditTextPassword) EditText editTextPassword;
    @BindView(R.id.dialogUsernamePassword_Close) ImageView close;
    @BindView(R.id.dialogUsernamePassword_Enter) Button enter;

    //Required libraries
    private MyHandler myHandler;
    private UserInterface userInterface;

    public DialogFragmentSignIn(MyHandler myHandler) {
        this.myHandler = myHandler;
    }

    @Override
    public int getLayoutResource() {
        return R.layout.dialog_username_password;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        enter.setOnClickListener(v -> {
            if (ManagerHelper.checkInternetServices(baseActivity)) {
                enter.setVisibility(View.GONE);
                gifImageViewEnter.setVisibility(View.VISIBLE);
                gifImageViewEnter.setAnimation("loading_1.json");
                gifImageViewEnter.playAnimation();
                gifImageViewEnter.setRepeatCount(LottieDrawable.INFINITE);
                getLoginInformation(editTextUsername.getText().toString().trim(), editTextPassword.getText().toString().trim(), myHandler);
            }
        });

        close.setOnClickListener(v -> {
            dismiss();
            enter.setClickable(true);
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        changeDialogSize((int) (getDisplayMetrics().widthPixels * 0.65), ViewGroup.LayoutParams.WRAP_CONTENT);
    }

    private void getLoginInformation(String username, String password, MyHandler handler) {
        LoginInformation.fetchFromWeb(baseActivity, null, (ok, obj) -> {
            if (ok) {
                LoginInformation loginInformation = (LoginInformation) obj;
                Log.i(MyLog.SESS + TAG, "Cookie : " + loginInformation.getCookie());
                sendParamsPost(loginInformation.getCookie(), username, password, handler);
            }
        });
    }

    private void sendParamsPost(String cookie, String username, String password, MyHandler handler) {
        HashMap<String, String> params = new HashMap<>();
        params.put("cookie", cookie);
        params.put("username", username);
        params.put("password", password);

        SendInformation.fetchFromWeb(baseActivity, params, (ok, obj) -> {
            if (ok) {
                SendInformation sendInformation = (SendInformation) obj;
                Log.i(MyLog.SESS + TAG, "Login with User <" + sendInformation.getResult().getUserInformation().getName() + " " + sendInformation.getResult().getUserInformation().getFamily() + ">");
                dismiss();
                preferencesUtils.savePreferences(cookie, username, password, sendInformation.getResult().getUserInformation());
                userInterface.addUserPersonalInfo(sendInformation.getResult().getUserInformation(), cookie);
                handler.onResponse(true, sendInformation);
                new WelcomeDialogFragment(sendInformation.getResult().getUserInformation()).show(baseActivity.getSupportFragmentManager(), "WelcomeDialogFragment");
                if (sendInformation.getResult().getInstantMessage().size() > 0) {
                    new DialogFragmentInstantMessage(sendInformation.getResult()).show(baseActivity.getSupportFragmentManager(), "DialogFragmentInstantMessage");
                }
            } else {
                enter.setVisibility(View.VISIBLE);
                editTextPassword.setText("");
                gifImageViewEnter.setVisibility(View.GONE);
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