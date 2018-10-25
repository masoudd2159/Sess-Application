package ir.ac.sku.www.sessapplication.utils;


import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import ir.ac.sku.www.sessapplication.R;

public class CustomToast {

    private Context context;
    private View view;
    private Toast toast;

    public CustomToast(Context context) {
        this.context = context;
        this.toast = new Toast(context);
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.view = inflater.inflate(R.layout.custom_toast, null);
        toast.setView(view);
    }

    private void setText(String text) {
        if (view == null)
            return;
        ((TextView) view.findViewById(R.id.toast_Message)).setText(text);
    }

    private Toast getToast() {
        return toast;
    }

    private View getView() {
        return view;
    }

    private static View getToastView(Toast toast) {
        return (toast == null) ? null : toast.getView();
    }

    private static Toast colorToast(Toast toast, int colorID) {
        View layout = getToastView(toast);
        if (layout != null) {
            layout.setBackgroundColor(colorID);
        }
        return toast;
    }

    public static Toast success(Context context, String text, int duration) {
        CustomToast customToast = new CustomToast(context);
        customToast.setText(text);
        customToast.getToast().setDuration(duration);
        customToast.getToast().setGravity(Gravity.TOP, 0, 60);
        return customToast.getToast();
    }
}
