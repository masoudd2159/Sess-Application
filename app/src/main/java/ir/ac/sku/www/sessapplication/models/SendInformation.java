package ir.ac.sku.www.sessapplication.models;

import java.util.List;

public class SendInformation {
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

    public class Result {
        private List<InstantMessage> instantMessages;
        private UserInfomation userInfomation;


        public List<InstantMessage> getInstantMessages() {
            return instantMessages;
        }

        public void setInstantMessages(List<InstantMessage> instantMessages) {
            this.instantMessages = instantMessages;
        }

        public UserInfomation getUserInfomation() {
            return userInfomation;
        }

        public void setUserInfomation(UserInfomation userInfomation) {
            this.userInfomation = userInfomation;
        }

        public class InstantMessage {
            private String a;
            private String b;
            private String c;
            private String d;

            public String getA() {
                return a;
            }

            public void setA(String a) {
                this.a = a;
            }

            public String getB() {
                return b;
            }

            public void setB(String b) {
                this.b = b;
            }

            public String getC() {
                return c;
            }

            public void setC(String c) {
                this.c = c;
            }

            public String getD() {
                return d;
            }

            public void setD(String d) {
                this.d = d;
            }
        }

        public class UserInfomation{
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
}
