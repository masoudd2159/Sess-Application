package ir.ac.sku.www.sessapplication.utils;

import android.content.Context;
import android.util.Log;

import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;

import java.util.HashMap;

import ir.ac.sku.www.sessapplication.api.MyLog;
import ir.ac.sku.www.sessapplication.fragment.dialogfragment.DialogFragmentSignIn;
import ir.ac.sku.www.sessapplication.fragment.dialogfragment.DialogFragmentInstantMessage;
import ir.ac.sku.www.sessapplication.model.information.LoginInformation;
import ir.ac.sku.www.sessapplication.model.information.SendInformation;

public class SignIn extends SharedPreferencesUtils {

    private Context context;

    public SignIn(Context context) {
        super(context);
        this.context = context;
    }

    public void SignInDialog(MyHandler handler) {
        Log.i(MyLog.SESS + SignIn.class.getSimpleName(), "Open Sign In Class");
        if (getUsername() == null && getPassword() == null) {
            Log.i(MyLog.SESS + SignIn.class.getSimpleName(), "UserName And Password is Null");
            FragmentManager fragmentManager = ((FragmentActivity) context).getSupportFragmentManager();
            new DialogFragmentSignIn(handler).show(fragmentManager, "DialogFragmentSignIn");
        } else {
            Log.i(MyLog.SESS + SignIn.class.getSimpleName(), "User <" + getUsername() + "> : " + getFirstName() + " " + getLastName());
            getLoginInformation(handler);
        }
    }

    private void getLoginInformation(MyHandler handler) {
        LoginInformation.fetchFromWeb(context, null, (ok, obj) -> {
            if (ok) {
                LoginInformation loginInformation = (LoginInformation) obj;
                Log.i(MyLog.SESS + SignIn.class.getSimpleName(), "Cookie : " + loginInformation.getCookie());
                sendParamsPost(loginInformation.getCookie(), handler);
            }
        });
    }

    private void sendParamsPost(String cookie, MyHandler handler) {
        HashMap<String, String> params = new HashMap<>();
        params.put("cookie", cookie);
        params.put("username", getUsername());
        params.put("password", getPassword());

        SendInformation.fetchFromWeb(context, params, (ok, obj) -> {
            if (ok) {
                SendInformation sendInformation = (SendInformation) obj;
                Log.i(MyLog.SESS + SignIn.class.getSimpleName(), "Login with User <" + sendInformation.getResult().getUserInformation().getName() + " " + sendInformation.getResult().getUserInformation().getFamily() + ">");
                setCookie(cookie);
                setImageBitmap(cookie);
                handler.onResponse(true, sendInformation);
                if (sendInformation.getResult().getInstantMessage().size() > 0) {
                    FragmentManager fragmentManager = ((FragmentActivity) context).getSupportFragmentManager();
                    new DialogFragmentInstantMessage(sendInformation.getResult()).show(fragmentManager, "DialogFragmentInstantMessage");
                }
            }
        });
    }
}
