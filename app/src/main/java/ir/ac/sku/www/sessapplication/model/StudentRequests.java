package ir.ac.sku.www.sessapplication.model;

import android.content.Context;

import com.android.volley.Request;
import com.google.gson.Gson;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;

import ir.ac.sku.www.sessapplication.api.ApplicationAPI;
import ir.ac.sku.www.sessapplication.utils.MyHandler;
import ir.ac.sku.www.sessapplication.utils.WebService;
import ir.ac.sku.www.sessapplication.utils.helper.ManagerHelper;

public class StudentRequests {

    @SerializedName("ok")
    @Expose
    private Boolean ok;

    @SerializedName("result")
    @Expose
    private Result result;

    public static void fetchFromWeb(Context context, HashMap<String, String> params, final MyHandler handler) {
        final Gson gson = new Gson();

        WebService webService = new WebService(context);
        String myURL = ApplicationAPI.STUDENT_REQUEST + "?" + ManagerHelper.enCodeParameters(params);
        webService.request(myURL, Request.Method.GET, new MyHandler() {
            @Override
            public void onResponse(boolean ok, Object obj) {
                if (ok) {
                    StudentRequests studentRequests = gson.fromJson(new String(obj.toString().getBytes(StandardCharsets.ISO_8859_1), StandardCharsets.UTF_8), StudentRequests.class);
                    if (studentRequests.getOk()) {
                        handler.onResponse(true, studentRequests);
                    }
                }
            }
        });
    }

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

    public class Result {

        @SerializedName("requests")
        @Expose
        private List<Request> requests = null;


        public List<Request> getRequests() {
            return requests;
        }

        public void setRequests(List<Request> requests) {
            this.requests = requests;
        }


        public class Request {

            @SerializedName("ident")
            @Expose
            private String ident;

            @SerializedName("student")
            @Expose
            private String student;

            @SerializedName("request")
            @Expose
            private String request;

            @SerializedName("status")
            @Expose
            private String status;

            @SerializedName("step")
            @Expose
            private String step;

            @SerializedName("employee")
            @Expose
            private String employee;


            public String getIdent() {
                return ident;
            }

            public void setIdent(String ident) {
                this.ident = ident;
            }

            public String getStudent() {
                return student;
            }

            public void setStudent(String student) {
                this.student = student;
            }

            public String getRequest() {
                return request;
            }

            public void setRequest(String request) {
                this.request = request;
            }

            public String getStatus() {
                return status;
            }

            public void setStatus(String status) {
                this.status = status;
            }

            public String getStep() {
                return step;
            }

            public void setStep(String step) {
                this.step = step;
            }

            public String getEmployee() {
                return employee;
            }

            public void setEmployee(String employee) {
                this.employee = employee;
            }

        }
    }
}


