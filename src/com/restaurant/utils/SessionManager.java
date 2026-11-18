package com.restaurant.utils;

import com.restaurant.models.Employee;

public class SessionManager {
    private static Employee loggedInUser;

    public static void setLoggedInUser(Employee emp) {
        loggedInUser = emp;
    }

    public static Employee getLoggedInUser() {
        return loggedInUser;
    }

    public static boolean isLoggedIn() {
        return loggedInUser != null;
    }

    public static void logout() {
        loggedInUser = null;
    }
}
 