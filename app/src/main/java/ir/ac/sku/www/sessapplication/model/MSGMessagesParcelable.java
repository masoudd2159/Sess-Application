package ir.ac.sku.www.sessapplication.model;

import android.content.Context;
import android.os.Parcel;
import android.os.Parcelable;

import com.android.volley.Request;
import com.google.gson.Gson;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import ir.ac.sku.www.sessapplication.api.MyConfig;
import ir.ac.sku.www.sessapplication.utils.MyHandler;
import ir.ac.sku.www.sessapplication.utils.HttpManager;
import ir.ac.sku.www.sessapplication.utils.WebService;

public class MSGMessagesParcelable implements Parcelable {
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

    public static class Result implements Parcelable {
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

        public static class Message implements Parcelable {
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

            public String getPriority() {
                return priority;
            }

            public void setPriority(String priority) {
                this.priority = priority;
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

            public String getReceiver() {
                return receiver;
            }

            public void setReceiver(String receiver) {
                this.receiver = receiver;
            }

            public static Creator<Message> getCREATOR() {
                return CREATOR;
            }

            public Message() {
            }

            @Override
            public int describeContents() {
                return 0;
            }

            @Override
            public void writeToParcel(Parcel dest, int flags) {
                dest.writeString(this.sender);
                dest.writeString(this.receiver);
                dest.writeString(this.subject);
                dest.writeString(this.ident);
                dest.writeValue(this.attachment);
                dest.writeString(this.priority);
                dest.writeString(this.date);
                dest.writeValue(this.read);
            }

            protected Message(Parcel in) {
                this.sender = in.readString();
                this.receiver = in.readString();
                this.subject = in.readString();
                this.ident = in.readString();
                this.attachment = (Boolean) in.readValue(Boolean.class.getClassLoader());
                this.priority = in.readString();
                this.date = in.readString();
                this.read = (Boolean) in.readValue(Boolean.class.getClassLoader());
            }

            public static final Creator<Message> CREATOR = new Creator<Message>() {
                @Override
                public Message createFromParcel(Parcel source) {
                    return new Message(source);
                }

                @Override
                public Message[] newArray(int size) {
                    return new Message[size];
                }
            };
        }


        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeList(this.messages);
            dest.writeString(this.type);
        }

        public Result() {
        }

        protected Result(Parcel in) {
            this.messages = new ArrayList<Message>();
            in.readList(this.messages, Message.class.getClassLoader());
            this.type = in.readString();
        }

        public static final Creator<Result> CREATOR = new Creator<Result>() {
            @Override
            public Result createFromParcel(Parcel source) {
                return new Result(source);
            }

            @Override
            public Result[] newArray(int size) {
                return new Result[size];
            }
        };
    }

    public static void fetchFromWeb(Context context, HashMap<String, String> params, final MyHandler handler) {
        final Gson gson = new Gson();

        WebService webService = new WebService(context);
        String myURL = MyConfig.MSG_MESSAGES + "?" + HttpManager.enCodeParameters(params);
        webService.request(myURL, Request.Method.GET, new MyHandler() {
            @Override
            public void onResponse(boolean ok, Object obj) {
                if (ok) {
                    try {
                        MSGMessagesParcelable messages = gson.fromJson(new String(obj.toString().getBytes("ISO-8859-1"), "UTF-8"), MSGMessagesParcelable.class);
                        if (messages.getOk()) {
                            handler.onResponse(true, messages);
                        }
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                        handler.onResponse(false, null);
                    }
                } else {
                    handler.onResponse(false, obj);
                }
            }
        });
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(this.ok);
        dest.writeParcelable(this.result, flags);
    }

    public MSGMessagesParcelable() {
    }

    protected MSGMessagesParcelable(Parcel in) {
        this.ok = (Boolean) in.readValue(Boolean.class.getClassLoader());
        this.result = in.readParcelable(Result.class.getClassLoader());
    }

    public static final Parcelable.Creator<MSGMessagesParcelable> CREATOR = new Parcelable.Creator<MSGMessagesParcelable>() {
        @Override
        public MSGMessagesParcelable createFromParcel(Parcel source) {
            return new MSGMessagesParcelable(source);
        }

        @Override
        public MSGMessagesParcelable[] newArray(int size) {
            return new MSGMessagesParcelable[size];
        }
    };
}
