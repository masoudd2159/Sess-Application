package ir.ac.sku.www.sessapplication.models;

public class UsernamePassword {
    private static String username;
    private static String Password;

    public static String getUsername() {
        return username;
    }

    public static void setUsername(String username) {
        UsernamePassword.username = username;
    }

    public static String getPassword() {
        return Password;
    }

    public static void setPassword(String password) {
        Password = password;
    }
}
