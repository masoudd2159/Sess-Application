package ir.ac.sku.www.sessapplication.activity.about;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.View;
import android.widget.TextView;

import androidx.coordinatorlayout.widget.CoordinatorLayout;

import com.mikhaellopez.circularimageview.CircularImageView;

import java.util.HashMap;

import butterknife.BindView;
import ir.ac.sku.www.sessapplication.R;
import ir.ac.sku.www.sessapplication.activity.SplashScreenActivity;
import ir.ac.sku.www.sessapplication.activity.messages.SendMessageActivity;
import ir.ac.sku.www.sessapplication.api.ApplicationAPI;
import ir.ac.sku.www.sessapplication.base.BaseActivity;
import ir.ac.sku.www.sessapplication.fragment.dialogfragment.DialogFragmentSignIn;
import ir.ac.sku.www.sessapplication.model.GetInfoForSend;
import ir.ac.sku.www.sessapplication.model.information.SendInformation;
import ir.ac.sku.www.sessapplication.utils.MyHandler;
import ir.ac.sku.www.sessapplication.utils.Tools;

public class AboutActivity
        extends BaseActivity
        implements DialogFragmentSignIn.UserInterface {

    @BindView(R.id.aboutActivity_ImageProfile) CircularImageView profile;
    @BindView(R.id.aboutActivity_Username) TextView username;
    @BindView(R.id.aboutActivity_Major) TextView majorUser;
    @BindView(R.id.layout_content) CoordinatorLayout content;

    @Override
    protected int getLayoutResource() {
        return R.layout.activity_about;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        changeStatusBarColor();
        setUpUserInformation();
    }

    @SuppressLint("SetTextI18n")
    private void setUpUserInformation() {
        if (preferencesUtils.getFirstName() == null && preferencesUtils.getLastName() == null) {
            username.setText(getResources().getString(R.string.shahrekord_university));
            Tools.displayImageOriginal(this, profile, R.drawable.ic_university);
        } else {
            majorUser.setText(preferencesUtils.getMajor());
            Tools.displayImageOriginal(this, profile, preferencesUtils.getImageBitmap());
            String fullName = preferencesUtils.getFirstName() + " " + preferencesUtils.getLastName();
            switch (preferencesUtils.getSex()) {
                case "مرد":
                    username.setText("آقای " + fullName);
                    break;
                case "زن":
                    username.setText("خانم " + fullName);
                    break;
                default:
                    username.setText(fullName);
                    break;
            }
        }
    }

    @SuppressLint("SetTextI18n")
    private void setUpUserInformation(SendInformation.Result.UserInformation userInformation, String cookie) {
        if (userInformation.getName() == null && userInformation.getFamily() == null) {
            username.setText(getResources().getString(R.string.shahrekord_university));
        } else {
            majorUser.setText(userInformation.getMajor());
            Tools.displayImageOriginal(this, profile, ApplicationAPI.IMAGE + cookie);
            String fullName = userInformation.getName() + " " + userInformation.getFamily();
            switch (userInformation.getSex()) {
                case "مرد":
                    username.setText("آقای " + fullName);
                    break;
                case "زن":
                    username.setText("خانم " + fullName);
                    break;
                default:
                    username.setText(fullName);
                    break;
            }
        }
    }

    public void onClickItemEmail(View view) {
        Intent emailIntent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts("mailto", getResources().getString(R.string.app_email), null));
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, getResources().getString(R.string.bug_report));
        startActivity(Intent.createChooser(emailIntent, getResources().getString(R.string.send_email_to_support)));
    }

    public void onClickItemLogout(View view) {
        preferencesUtils.clearSharedPreferences();
        startActivity(new Intent(AboutActivity.this, SplashScreenActivity.class));
        finish();
    }

    public void onClickItemBugReport(View view) {
        HashMap<String, String> params = new HashMap<>();
        params.put("id", "tapp");
        params.put("stNumber", "");

        GetInfoForSend.fetchFromWeb(AboutActivity.this, params, (MyHandler) (ok, obj) -> {
            if (ok) {
                Intent intent = new Intent(AboutActivity.this, SendMessageActivity.class);
                intent.putExtra("GetInfoForSend", (Parcelable) (GetInfoForSend) obj);
                intent.putExtra("id", "tapp");
                intent.putExtra("studentNumber", "");
                try {
                    Thread.sleep(1600);
                    startActivity(intent);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @Override public void addUserPersonalInfo(SendInformation.Result.UserInformation userInformation, String cookie) {
        setUpUserInformation(userInformation, cookie);
    }
}