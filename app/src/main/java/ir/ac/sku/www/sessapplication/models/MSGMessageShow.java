package ir.ac.sku.www.sessapplication.models;

import android.content.Context;

import com.android.volley.Request;
import com.google.gson.Gson;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;

import ir.ac.sku.www.sessapplication.API.MyConfig;
import ir.ac.sku.www.sessapplication.utils.Handler;
import ir.ac.sku.www.sessapplication.utils.HttpManager;
import ir.ac.sku.www.sessapplication.utils.WebService;

public class MSGMessageShow {

    private Boolean ok;
    private Result result;

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
        private String subject;
        private String sender;
        private String date;
        private String time;
        private String text;
        private String target;
        private String receiver;
        private String type;
        private String priority;
        private Boolean attachment;
        private Boolean read;

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

        public Boolean getAttachment() {
            return attachment;
        }

        public void setAttachment(Boolean attachment) {
            this.attachment = attachment;
        }

        public Boolean getRead() {
            return read;
        }

        public void setRead(Boolean read) {
            this.read = read;
        }

        public String getReceiver() {
            return receiver;
        }

        public void setReceiver(String receiver) {
            this.receiver = receiver;
        }
    }

    public static void fetchFromWeb(Context context, HashMap<String, String> params, final Handler handler) {
        final Gson gson = new Gson();

        WebService webService = new WebService(context);
        String myURL = MyConfig.MSG_MESSAGES + "?" + HttpManager.enCodeParameters(params);
        webService.request(myURL, Request.Method.GET, new Handler() {
            @Override
            public void onResponse(boolean ok, Object obj) {
                if (ok) {
                    try {
                        MSGMessageShow messages = gson.fromJson(new String(obj.toString().getBytes("ISO-8859-1"), "UTF-8"), MSGMessageShow.class);
                        if (messages.getOk()) {
                            handler.onResponse(true, messages);
                        }
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                        handler.onResponse(false, null);
                    }
                }
            }
        });
    }
}
