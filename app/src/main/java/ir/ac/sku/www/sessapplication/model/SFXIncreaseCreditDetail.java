package ir.ac.sku.www.sessapplication.model;

import java.util.List;

public class SFXIncreaseCreditDetail {
    private Boolean ok;
    private Result result;
    private Description description;

    public Description getDescription() {
        return description;
    }

    public void setDescription(Description description) {
        this.description = description;
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
        private Plans plans;
        private List<Subject> subjects = null;

        public Plans getPlans() {
            return plans;
        }

        public void setPlans(Plans plans) {
            this.plans = plans;
        }

        public List<Subject> getSubjects() {
            return subjects;
        }

        public void setSubjects(List<Subject> subjects) {
            this.subjects = subjects;
        }

        public class Plans {
            private String currency;
            private List<Credit> credits = null;

            public String getCurrency() {
                return currency;
            }

            public void setCurrency(String currency) {
                this.currency = currency;
            }

            public List<Credit> getCredits() {
                return credits;
            }

            public void setCredits(List<Credit> credits) {
                this.credits = credits;
            }

            public class Credit {
                private String label;
                private String value;

                public String getLabel() {
                    return label;
                }

                public void setLabel(String label) {
                    this.label = label;
                }

                public String getValue() {
                    return value;
                }

                public void setValue(String value) {
                    this.value = value;
                }
            }
        }

        public class Subject {
            private String label;
            private String value;

            public String getLabel() {
                return label;
            }

            public void setLabel(String label) {
                this.label = label;
            }

            public String getValue() {
                return value;
            }

            public void setValue(String value) {
                this.value = value;
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
