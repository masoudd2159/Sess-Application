package ir.ac.sku.www.sessapplication.models;

import android.content.Context;
import android.os.Build;

import androidx.annotation.RequiresApi;

import com.android.volley.Request;
import com.google.gson.Gson;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;

import ir.ac.sku.www.sessapplication.API.MyConfig;
import ir.ac.sku.www.sessapplication.utils.MyHandler;
import ir.ac.sku.www.sessapplication.utils.HttpManager;
import ir.ac.sku.www.sessapplication.utils.WebService;

public class StudentRequestDetails {

    private Boolean ok;
    private Result result;
    private Description description;

    public Boolean getOk() {
        return ok;
    }

    public void setOk(Boolean ok) {
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

        private String title;
        private List<Step> steps = null;
        private Integer totalSteps;
        private Integer currentStepNumber;

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public List<Step> getSteps() {
            return steps;
        }

        public void setSteps(List<Step> steps) {
            this.steps = steps;
        }

        public Integer getTotalSteps() {
            return totalSteps;
        }

        public void setTotalSteps(Integer totalSteps) {
            this.totalSteps = totalSteps;
        }

        public Integer getCurrentStepNumber() {
            return currentStepNumber;
        }

        public void setCurrentStepNumber(Integer currentStepNumber) {
            this.currentStepNumber = currentStepNumber;
        }

        public class Step {
            private String name;
            private String title;
            private String user;
            private Boolean currentUser;
            private String status;

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public String getTitle() {
                return title;
            }

            public void setTitle(String title) {
                this.title = title;
            }

            public String getUser() {
                return user;
            }

            public void setUser(String user) {
                this.user = user;
            }

            public Boolean getCurrentUser() {
                return currentUser;
            }

            public void setCurrentUser(Boolean currentUser) {
                this.currentUser = currentUser;
            }

            public String getStatus() {
                return status;
            }

            public void setStatus(String status) {
                this.status = status;
            }
        }
    }

    public class Description {
        private String errorText;
        private String errorCode;

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

    public static void fetchFromWeb(Context context, HashMap<String, String> params, final MyHandler handler) {
        final Gson gson = new Gson();

        WebService webService = new WebService(context);
        String myURL = MyConfig.STUDENT_REQUEST + "?" + HttpManager.enCodeParameters(params);
        webService.request(myURL, Request.Method.GET, new MyHandler() {
            @Override
            public void onResponse(boolean ok, Object obj) {
                if (ok) {
                    StudentRequestDetails studentRequestDetails = gson.fromJson(new String(obj.toString().getBytes(StandardCharsets.ISO_8859_1), StandardCharsets.UTF_8), StudentRequestDetails.class);
                    if (studentRequestDetails.getOk()) {
                        handler.onResponse(true, studentRequestDetails);
                    }
                }
            }
        });
    }
}
