package main.util;

import main.model.OnlineBankingUser;

public class AuthUtil {

    public static boolean isAdmin(OnlineBankingUser user) {
        return user != null && user.getEmployee() != null;
    }

    public static boolean isClient(OnlineBankingUser user) {
        return user != null && user.getClient() != null;
    }
}