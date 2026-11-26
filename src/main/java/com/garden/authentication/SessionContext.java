package com.garden.authentication;

import com.garden.model.User;

public class SessionContext {

    private static User currentUser;

    public static void login(User user) {
        currentUser = user;
    }

    public static User getUser() {
        return currentUser;
    }

    public static void logout() {
        currentUser = null;
    }

    public static boolean isAuthenticated() {
        return currentUser != null;
    }
}
