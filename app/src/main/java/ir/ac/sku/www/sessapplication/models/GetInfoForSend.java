package ir.ac.sku.www.sessapplication.models;

import android.content.Context;
import android.os.Parcel;
import android.os.Parcelable;

import com.android.volley.Request;
import com.google.gson.Gson;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import ir.ac.sku.www.sessapplication.API.MyConfig;
import ir.ac.sku.www.sessapplication.utils.Handler;
import ir.ac.sku.www.sessapplication.utils.HttpManager;
import ir.ac.sku.www.sessapplication.utils.WebService;

public class GetInfoForSend implements Parcelable {

    private boolean ok;
    private Result result;

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

    public static class Result implements Parcelable {

        private String date;
        private List<Target> target;
        private List<Types> types;
        private List<Priorities> priorities;

        public String getDate() {
            return date;
        }

        public void setDate(String date) {
            this.date = date;
        }

        public List<Target> getTarget() {
            return target;
        }

        public void setTarget(List<Target> target) {
            this.target = target;
        }

        public List<Types> getTypes() {
            return types;
        }

        public void setTypes(List<Types> types) {
            this.types = types;
        }

        public List<Priorities> getPriorities() {
            return priorities;
        }

        public void setPriorities(List<Priorities> priorities) {
            this.priorities = priorities;
        }

        public static class Target implements Parcelable {

            private String name;
            private String position;

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public String getPosition() {
                return position;
            }

            public void setPosition(String position) {
                this.position = position;
            }


            @Override
            public int describeContents() {
                return 0;
            }

            @Override
            public void writeToParcel(Parcel dest, int flags) {
                dest.writeString(this.name);
                dest.writeString(this.position);
            }

            public Target() {
            }

            protected Target(Parcel in) {
                this.name = in.readString();
                this.position = in.readString();
            }

            public static final Creator<Target> CREATOR = new Creator<Target>() {
                @Override
                public Target createFromParcel(Parcel source) {
                    return new Target(source);
                }

                @Override
                public Target[] newArray(int size) {
                    return new Target[size];
                }
            };
        }

        public static class Types implements Parcelable {

            private String name;
            private String value;

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public String getValue() {
                return value;
            }

            public void setValue(String value) {
                this.value = value;
            }


            @Override
            public int describeContents() {
                return 0;
            }

            @Override
            public void writeToParcel(Parcel dest, int flags) {
                dest.writeString(this.name);
                dest.writeString(this.value);
            }

            public Types() {
            }

            protected Types(Parcel in) {
                this.name = in.readString();
                this.value = in.readString();
            }

            public static final Creator<Types> CREATOR = new Creator<Types>() {
                @Override
                public Types createFromParcel(Parcel source) {
                    return new Types(source);
                }

                @Override
                public Types[] newArray(int size) {
                    return new Types[size];
                }
            };
        }

        public static class Priorities implements Parcelable {

            private String name;
            private String value;

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public String getValue() {
                return value;
            }

            public void setValue(String value) {
                this.value = value;
            }


            @Override
            public int describeContents() {
                return 0;
            }

            @Override
            public void writeToParcel(Parcel dest, int flags) {
                dest.writeString(this.name);
                dest.writeString(this.value);
            }

            public Priorities() {
            }

            protected Priorities(Parcel in) {
                this.name = in.readString();
                this.value = in.readString();
            }

            public static final Creator<Priorities> CREATOR = new Creator<Priorities>() {
                @Override
                public Priorities createFromParcel(Parcel source) {
                    return new Priorities(source);
                }

                @Override
                public Priorities[] newArray(int size) {
                    return new Priorities[size];
                }
            };
        }


        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(this.date);
            dest.writeList(this.target);
            dest.writeList(this.types);
            dest.writeList(this.priorities);
        }

        public Result() {
        }

        protected Result(Parcel in) {
            this.date = in.readString();
            this.target = new ArrayList<Target>();
            in.readList(this.target, Target.class.getClassLoader());
            this.types = new ArrayList<Types>();
            in.readList(this.types, Types.class.getClassLoader());
            this.priorities = new ArrayList<Priorities>();
            in.readList(this.priorities, Priorities.class.getClassLoader());
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

    public static void fetchFromWeb(Context context, HashMap<String, String> params, final Handler handler) {
        final Gson gson = new Gson();

        WebService webService = new WebService(context);
        String myURL = MyConfig.MSG_GET_INFO_FOR_SEND + "?" + HttpManager.enCodeParameters(params);
        webService.request(myURL, Request.Method.GET, new Handler() {
            @Override
            public void onResponse(boolean ok, Object obj) {
                if (ok) {
                    GetInfoForSend getInfoForSend = gson.fromJson(new String(obj.toString().getBytes(StandardCharsets.ISO_8859_1), StandardCharsets.UTF_8), GetInfoForSend.class);
                    if (getInfoForSend.isOk()) {
                        handler.onResponse(true, getInfoForSend);
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
        dest.writeByte(this.ok ? (byte) 1 : (byte) 0);
        dest.writeParcelable((Parcelable) this.result, flags);
    }

    public GetInfoForSend() {
    }

    protected GetInfoForSend(Parcel in) {
        this.ok = in.readByte() != 0;
        this.result = in.readParcelable(Result.class.getClassLoader());
    }

    public static final Parcelable.Creator<GetInfoForSend> CREATOR = new Parcelable.Creator<GetInfoForSend>() {
        @Override
        public GetInfoForSend createFromParcel(Parcel source) {
            return new GetInfoForSend(source);
        }

        @Override
        public GetInfoForSend[] newArray(int size) {
            return new GetInfoForSend[size];
        }
    };
}
