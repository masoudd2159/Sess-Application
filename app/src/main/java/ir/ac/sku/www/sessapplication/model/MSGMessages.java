package ir.ac.sku.www.sessapplication.model;

import android.content.Context;

import com.android.volley.Request;
import com.google.gson.Gson;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.List;

import ir.ac.sku.www.sessapplication.api.ApplicationAPI;
import ir.ac.sku.www.sessapplication.utils.MyHandler;
import ir.ac.sku.www.sessapplication.utils.helper.ManagerHelper;
import ir.ac.sku.www.sessapplication.utils.WebService;

public class MSGMessages {
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

    public static class Result {
        private List<Message> messages = null;
        private String type;

        public List<Message> getMessages() {
            return messages;
        }

        public void setMessages(List<Message> messages) {
            this.messages = messages;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public static class Message {
            private String sender;
            private String receiver;
            private String subject;
            private String ident;
            private Boolean attachment;
            private String priority;
            private String date;
            private Boolean read;


            public String getSender() {
                return sender;
            }

            public void setSender(String sender) {
                this.sender = sender;
            }

            public String getSubject() {
                return subject;
            }

            public void setSubject(String subject) {
                this.subject = subject;
            }

            public String getIdent() {
                return ident;
            }

            public void setIdent(String ident) {
                this.ident = ident;
            }

            public Boolean getAttachment() {
                return attachment;
            }

            public void setAttachment(Boolean attachment) {
                this.attachment = attachment;
            }

            public String getDate() {
                return date;
            }

            public void setDate(String date) {
                this.date = date;
            }

            public Boolean getRead() {
                return read;
            }

            public void setRead(Boolean read) {
                this.read = read;
            }

            public String getPriority() {
                return priority;
            }

            public void setPriority(String priority) {
                this.priority = priority;
            }

            public String getReceiver() {
                return receiver;
            }

            public void setReceiver(String receiver) {
                this.receiver = receiver;
            }
        }
    }

    public static void fetchFromWeb(Context context, HashMap<String, String> params, final MyHandler handler) {
        final Gson gson = new Gson();

        WebService webService = new WebService(context);
        String myURL = ApplicationAPI.MSG_MESSAGES + "?" + ManagerHelper.enCodeParameters(params);
        webService.request(myURL, Request.Method.GET, new MyHandler() {
            @Override
            public void onResponse(boolean ok, Object obj) {
                if (ok) {
                    try {
                        MSGMessages messages = gson.fromJson(new String(obj.toString().getBytes("ISO-8859-1"), "UTF-8"), MSGMessages.class);
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
