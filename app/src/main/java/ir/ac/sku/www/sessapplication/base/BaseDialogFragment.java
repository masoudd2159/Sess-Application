package ir.ac.sku.www.sessapplication.base;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;
import androidx.fragment.app.DialogFragment;

import java.util.Objects;

import butterknife.ButterKnife;
import ir.ac.sku.www.sessapplication.R;

public abstract class BaseDialogFragment extends AppCompatDialogFragment {

    protected OnDialogItemClick mOnDialogItemClick;
    private BaseActivity baseActivity;

    @Override public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.baseActivity = (BaseActivity) context;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(getLayoutResource(), container, false);
        ButterKnife.bind(this, view);

        if (getDialog() != null && getDialog().isShowing()) {
            dismiss();
        } else {
            baseActivity.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
            baseActivity.requestWindowFeature(Window.FEATURE_NO_TITLE);
            baseActivity.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            WindowManager.LayoutParams layoutParams = baseActivity.getWindow().getAttributes();
            layoutParams.dimAmount = 0.7f;
            baseActivity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
            getDialog().setCancelable(false);
        }

        return view;
    }

    public void changeDialogSize(int width, int height) {
        if (getDialog() != null && getDialog().getWindow() != null)
            getDialog().getWindow().setLayout(width, height);
    }

    public DisplayMetrics getDisplayMetrics() {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        baseActivity.getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        return displayMetrics;
    }

    public void setOnDialogItemClick(OnDialogItemClick onDialogItemClick) {
        mOnDialogItemClick = onDialogItemClick;
    }

    public abstract int getLayoutResource();

    public interface OnDialogItemClick {
        void onDialogItemClick(DialogFragment dialogFragment, View view, Bundle data);
    }
}
