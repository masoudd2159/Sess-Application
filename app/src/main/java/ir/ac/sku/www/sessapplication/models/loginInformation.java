package ir.ac.sku.www.sessapplication.models;

public class loginInformation {
    private boolean ok;
    private String cookie;

    public boolean isOk() {
        return ok;
    }

    public void setOk(boolean ok) {
        this.ok = ok;
    }

    public String getCookie() {
        return cookie;
    }

    public void setCookie(String cookie) {
        this.cookie = cookie;
    }
}
