package ir.ac.sku.www.sessapplication.model.information;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.HashMap;
import java.util.List;

import ir.ac.sku.www.sessapplication.R;
import ir.ac.sku.www.sessapplication.api.ApiFactory;
import ir.ac.sku.www.sessapplication.api.MyLog;
import ir.ac.sku.www.sessapplication.api.RxApiCallHelper;
import ir.ac.sku.www.sessapplication.api.RxApiCallback;
import ir.ac.sku.www.sessapplication.model.appInfo.AppInfo;
import ir.ac.sku.www.sessapplication.utils.MyHandler;
import ir.ac.sku.www.sessapplication.utils.SignIn;
import ir.ac.sku.www.sessapplication.utils.helper.ManagerHelper;
import rx.Observable;

public class SendInformation {
    @SerializedName("ok")
    @Expose private boolean ok;

    @SerializedName("result")
    @Expose private Result result;

    @SerializedName("description")
    @Expose private Description description;

    public static void fetchFromWeb(@NonNull Context context, HashMap<String, String> params, final MyHandler handler) {
        if (ManagerHelper.checkInternetServices(context)) {
            Observable<JsonObject> observable = ApiFactory.createProvideApiService(informationService.class).postSendInformation(params);
            RxApiCallHelper.call(observable, new RxApiCallback<JsonObject>() {
                @Override
                public void onSuccess(JsonObject jsonObject) {
                    Log.i(MyLog.SESS + SendInformation.class.getSimpleName(), "Object Successfully Receive");
                    SendInformation response = new Gson().fromJson(jsonObject, SendInformation.class);
                    if (response.isOk()) {
                        handler.onResponse(true, response);
                    } else {
                        handler.onResponse(false, null);
                        Log.i(MyLog.SESS + SendInformation.class.getSimpleName(), "Response False : <" + response.getDescription().getErrorCode() + "> " + response.getDescription().getErrorText());
                        if (Integer.parseInt(response.getDescription().getErrorCode()) > 0) {
                            ManagerHelper.unsuccessfulOperation(context, response.getDescription().getErrorText());
                        } else if (Integer.parseInt(response.getDescription().getErrorCode()) < 0) {
                            new SignIn(context).SignInDialog((ok, obj) -> {
                                if (ok) handler.onResponse(true, obj);
                            });
                        }
                    }
                }

                @Override
                public void onFailed(String errorMsg) {
                    Log.i(MyLog.SESS + AppInfo.class.getSimpleName(), errorMsg);
                    Toast.makeText(context, R.string.unable_to_connect_to_the_server, Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    public boolean isOk() {
        return ok;
    }

    public void setOk(boolean ok) {
        this.ok = ok;
    }

    public Result getResult() {
        return result;
    }

    public void setResult(Result result) {
        this.result = result;
    }

    public Description getDescription() {
        return description;
    }

    public void setDescription(Description description) {
        this.description = description;
    }

    public class Result {
        @SerializedName("instantMessage")
        @Expose private List<InstantMessage> instantMessage;

        @SerializedName("userInformation")
        @Expose private UserInformation userInformation;

        public List<InstantMessage> getInstantMessage() {
            return instantMessage;
        }

        public void setInstantMessage(List<InstantMessage> instantMessage) {
            this.instantMessage = instantMessage;
        }

        public UserInformation getUserInformation() {
            return userInformation;
        }

        public void setUserInformation(UserInformation userInformation) {
            this.userInformation = userInformation;
        }

        public class InstantMessage {
            @SerializedName("subject")
            @Expose private String subject;

            @SerializedName("sender")
            @Expose private String sender;

            @SerializedName("date")
            @Expose private String date;

            @SerializedName("time")
            @Expose private String time;

            @SerializedName("text")
            @Expose private String text;

            @SerializedName("target")
            @Expose private String target;

            @SerializedName("type")
            @Expose private String type;

            @SerializedName("priority")
            @Expose private String priority;

            @SerializedName("attachment")
            @Expose private boolean attachment;

            public String getSubject() {
                return subject;
            }

            public void setSubject(String subject) {
                this.subject = subject;
            }

            public String getSender() {
                return sender;
            }

            public void setSender(String sender) {
                this.sender = sender;
            }

            public String getDate() {
                return date;
            }

            public void setDate(String date) {
                this.date = date;
            }

            public String getTime() {
                return time;
            }

            public void setTime(String time) {
                this.time = time;
            }

            public String getText() {
                return text;
            }

            public void setText(String text) {
                this.text = text;
            }

            public String getTarget() {
                return target;
            }

            public void setTarget(String target) {
                this.target = target;
            }

            public String getType() {
                return type;
            }

            public void setType(String type) {
                this.type = type;
            }

            public String getPriority() {
                return priority;
            }

            public void setPriority(String priority) {
                this.priority = priority;
            }

            public boolean isAttachment() {
                return attachment;
            }

            public void setAttachment(boolean attachment) {
                this.attachment = attachment;
            }
        }

        public class UserInformation {
            @SerializedName("name")
            @Expose private String name;

            @SerializedName("family")
            @Expose private String family;

            @SerializedName("major")
            @Expose private String major;

            @SerializedName("sex")
            @Expose private String sex;

            @SerializedName("type")
            @Expose private String type;

            @SerializedName("mobile")
            @Expose private String mobile;

            @SerializedName("image")
            @Expose private String image;

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public String getFamily() {
                return family;
            }

            public void setFamily(String family) {
                this.family = family;
            }

            public String getMajor() {
                return major;
            }

            public void setMajor(String major) {
                this.major = major;
            }

            public String getSex() {
                return sex;
            }

            public void setSex(String sex) {
                this.sex = sex;
            }

            public String getType() {
                return type;
            }

            public void setType(String type) {
                this.type = type;
            }

            public String getMobile() {
                return mobile;
            }

            public void setMobile(String mobile) {
                this.mobile = mobile;
            }

            public String getImage() {
                return image;
            }

            public void setImage(String image) {
                this.image = image;
            }
        }
    }

    public class Description {
        @SerializedName("errorText")
        @Expose private String errorText;

        @SerializedName("errorCode")
        @Expose private String errorCode;

        public String getErrorText() {
            return errorText;
        }

        public void setErrorText(String errorText) {
            this.errorText = errorText;
        }

        public String getErrorCode() {
            return errorCode;
        }

        public void setErrorCode(String errorCode) {
            this.errorCode = errorCode;
        }
    }
}
