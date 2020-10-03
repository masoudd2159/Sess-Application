package ir.ac.sku.www.sessapplication.fragment;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import java.util.Objects;

import ir.ac.sku.www.sessapplication.R;
import ir.ac.sku.www.sessapplication.api.MyLog;
import ir.ac.sku.www.sessapplication.model.information.SendInformation;

public class WelcomeDialogFragment extends DialogFragment {

    private View rootView;
    private String message;

    public WelcomeDialogFragment(SendInformation.Result.UserInformation userInformation) {
        String fullName = userInformation.getName() + " " + userInformation.getFamily();
        switch (userInformation.getSex()) {
            case "مرد":
                this.message = "خوش آمدید آقای " + fullName;
                break;
            case "زن":
                this.message = "خوش آمدید خانم " + fullName;
                break;
            default:
                this.message = "خوش آمدید " + fullName;
                break;
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.custom_welcome_dialog, container, false);
        if (getDialog() != null && getDialog().isShowing())
            dismiss();
        changeStatusBarColor();

        getDialog().getWindow().setWindowAnimations(R.style.FragmentDialogAnimation);

        WindowManager.LayoutParams layoutParams = getDialog().getWindow().getAttributes();
        layoutParams.dimAmount = 0.05f;
        layoutParams.gravity = Gravity.TOP;
        getDialog().getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        getDialog().getWindow().setAttributes(layoutParams);
        getDialog().setCanceledOnTouchOutside(true);
        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        getDialog().getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        ((TextView) rootView.findViewById(R.id.welcomeDialog_Message)).setText(message);

        final Handler handler = new Handler();
        final Runnable runnable = () -> {
            if (Objects.requireNonNull(getDialog()).isShowing()) {
                getDialog().dismiss();
            }
        };

        getDialog().setOnDismissListener(dialog -> handler.removeCallbacks(runnable));

        handler.postDelayed(runnable, 1500);
        return rootView;
    }

    private void changeStatusBarColor() {
        Log.i(MyLog.SESS + WelcomeDialogFragment.class.getSimpleName(), "Change Status Bar");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = Objects.requireNonNull(getDialog()).getWindow();
            if (window != null) {
                window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
                window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                window.setStatusBarColor(Color.TRANSPARENT);
            }
        }
    }
}
