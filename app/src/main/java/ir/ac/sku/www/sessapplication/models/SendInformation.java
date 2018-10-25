package ir.ac.sku.www.sessapplication.models;

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

    public class Result {
        private List<InstantMessage> instantMessages;
        private UserInformation userInformation;


        public List<InstantMessage> getInstantMessages() {
            return instantMessages;
        }

        public void setInstantMessages(List<InstantMessage> instantMessages) {
            this.instantMessages = instantMessages;
        }

        public UserInformation getUserInformation() {
            return userInformation;
        }

        public void setUserInformation(UserInformation userInformation) {
            this.userInformation = userInformation;
        }

        public class InstantMessage {
            private String sender;
            private String date;
            private String text;
            private boolean attachment;


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

            public String getText() {
                return text;
            }

            public void setText(String text) {
                this.text = text;
            }

            public boolean isAttachment() {
                return attachment;
            }

            public void setAttachment(boolean attachment) {
                this.attachment = attachment;
            }
        }

        public class UserInformation {
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
}
