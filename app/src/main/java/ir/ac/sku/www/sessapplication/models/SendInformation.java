package ir.ac.sku.www.sessapplication.models;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

public class SendInformation {
    private boolean ok;
    private Result result;
    private Description description;

    public Description getDescription() {
        return description;
    }

    public void setDescription(Description description) {
        this.description = description;
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

    public static class Result implements Parcelable {
        private List<InstantMessage> instantMessage;
        private UserInformation userInformation;

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


        public static class InstantMessage implements Parcelable {

            private String subject;
            private String sender;
            private String date;
            private String time;
            private String text;
            private String target;
            private String type;
            private String priority;
            private Boolean attachment;

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


            @Override
            public int describeContents() {
                return 0;
            }

            @Override
            public void writeToParcel(Parcel dest, int flags) {
                dest.writeString(this.subject);
                dest.writeString(this.sender);
                dest.writeString(this.date);
                dest.writeString(this.time);
                dest.writeString(this.text);
                dest.writeString(this.target);
                dest.writeString(this.type);
                dest.writeString(this.priority);
                dest.writeValue(this.attachment);
            }

            public InstantMessage() {
            }

            protected InstantMessage(Parcel in) {
                this.subject = in.readString();
                this.sender = in.readString();
                this.date = in.readString();
                this.time = in.readString();
                this.text = in.readString();
                this.target = in.readString();
                this.type = in.readString();
                this.priority = in.readString();
                this.attachment = (Boolean) in.readValue(Boolean.class.getClassLoader());
            }

            public static final Parcelable.Creator<InstantMessage> CREATOR = new Parcelable.Creator<InstantMessage>() {
                @Override
                public InstantMessage createFromParcel(Parcel source) {
                    return new InstantMessage(source);
                }

                @Override
                public InstantMessage[] newArray(int size) {
                    return new InstantMessage[size];
                }
            };
        }

        public static class UserInformation implements Parcelable {
            private String name;
            private String major;
            private String intrant;

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public String getMajor() {
                return major;
            }

            public void setMajor(String major) {
                this.major = major;
            }

            public String getIntrant() {
                return intrant;
            }

            public void setIntrant(String intrant) {
                this.intrant = intrant;
            }


            @Override
            public int describeContents() {
                return 0;
            }

            @Override
            public void writeToParcel(Parcel dest, int flags) {
                dest.writeString(this.name);
                dest.writeString(this.major);
                dest.writeString(this.intrant);
            }

            public UserInformation() {
            }

            protected UserInformation(Parcel in) {
                this.name = in.readString();
                this.major = in.readString();
                this.intrant = in.readString();
            }

            public static final Parcelable.Creator<UserInformation> CREATOR = new Parcelable.Creator<UserInformation>() {
                @Override
                public UserInformation createFromParcel(Parcel source) {
                    return new UserInformation(source);
                }

                @Override
                public UserInformation[] newArray(int size) {
                    return new UserInformation[size];
                }
            };
        }


        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeTypedList(this.instantMessage);
            dest.writeParcelable(this.userInformation, flags);
        }

        public Result() {
        }

        protected Result(Parcel in) {
            this.instantMessage = in.createTypedArrayList(InstantMessage.CREATOR);
            this.userInformation = in.readParcelable(UserInformation.class.getClassLoader());
        }

        public static final Parcelable.Creator<Result> CREATOR = new Parcelable.Creator<Result>() {
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

}
