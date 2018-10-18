package ir.ac.sku.www.sessapplication.parser;

import android.util.Log;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import ir.ac.sku.www.sessapplication.activities.SplashScreenActivity;
import ir.ac.sku.www.sessapplication.models.LoginInformation;

public class LoginInformationJSONParser {

    private static final String OK = "ok";
    private static final String COOKIE = "cookie";

    /*public LoginInformation parserJSON(String jsonString) {
        LoginInformation loginInformation = new LoginInformation();
        try {
            JSONObject jsonObject = new JSONObject(jsonString);

            loginInformation.setOk(jsonObject.getBoolean(OK));
            loginInformation.setCookie(jsonObject.getString(COOKIE));

            return loginInformation;
        } catch (JSONException e1) {
            e1.printStackTrace();
        }
        return null;
    }*/
}
