package ir.ac.sku.www.sessapplication.fragment.dialogfragment;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import butterknife.BindView;
import ir.ac.sku.www.sessapplication.R;
import ir.ac.sku.www.sessapplication.api.MyLog;
import ir.ac.sku.www.sessapplication.base.BaseDialogFragment;
import ir.ac.sku.www.sessapplication.model.appInfo.AppInfo;

public class DialogFragmentUpdate extends BaseDialogFragment {

    @BindView(R.id.customUpdate_UpdateButton) Button update;
    @BindView(R.id.customUpdate_ShowMessage) TextView showMessage;

    private AppInfo appInfo;
    private OnCancelListener onCancelListener;

    public DialogFragmentUpdate(AppInfo appInfo) {
        this.appInfo = appInfo;
    }

    @Override public int getLayoutResource() {
        return R.layout.custom_update;
    }

    @Override
    public void onViewCreated(@NonNull View rootView, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(rootView, savedInstanceState);
        try {
            PackageInfo packageInfo = baseActivity.getPackageManager().getPackageInfo(baseActivity.getPackageName(), 0);

            Log.i(MyLog.SESS + TAG, "Version Code : " + packageInfo.versionCode);
            Log.i(MyLog.SESS + TAG, "Version Name : " + packageInfo.versionName);

            if (packageInfo.versionCode < appInfo.getResult().getAndroidMinimumVersion()) {
                setCancelable(false);
                showMessage.setText(appInfo.getResult().getForceUpdateMessage());
            } else {
                setCancelable(true);
                showMessage.setText(appInfo.getResult().getUpdateMessage());
            }
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        update.setOnClickListener(v -> {
            Log.i(MyLog.SESS + TAG, "On Update Listener");
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(Uri.parse(appInfo.getResult().getDownloadAndroidUrl()));
            startActivity(intent);
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        changeDialogSize((int) (getDisplayMetrics().widthPixels * 0.8), ViewGroup.LayoutParams.WRAP_CONTENT);
    }

    @Override public void onDestroy() {
        super.onDestroy();
        Log.i(MyLog.SESS + TAG, "Dialog Dismissed");
        onCancelListener.onCancelListener();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.onCancelListener = (OnCancelListener) context;
    }

    public interface OnCancelListener {
        void onCancelListener();
    }
}
