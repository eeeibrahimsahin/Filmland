package com.sogeti.filmland.constants;

public final class Constant {
    public static final String LOGIN_SUCCESSFUL = "Login Successful";
    public static final String LOGIN_FAILED = "Login Failed";
    public static final String REGISTRATION_SUCCESSFUL = "Registration Successful";
    public static final String AUTHORISATION_KEY_IS = "Authorisation key is ";
    public static final String USER_NOT_FOUND = "User with email %s not found";
    public static final String EMAIL_ALREADY_TAKEN = "Email already taken";
    public static final String CATEGORY_NOT_FOUND = "Category with name %s not found";
    public static final String USERNAME_OR_PASSWORD_IS_WRONG = "Username or password is wrong";
    public static final String EMAIL_NOT_VALID = "Email is not valid";
    public static final String KEY_NOT_FOUND = " %s Aut. Key not found";
    public static final String SUCCESSFUL = "Successful!";
    public static final String FAILED = "Failed!";
    public static final String SUBSCRIPTION_FAILED = "Failed to create subscription";
    public static final String SUBSCRIPTION_ALREADY_EXISTS = "The subscription already exists!";
    public static final String SUBSCRIPTION_SUCCESSFUL = "User is subscribed to ";
    public static final String SUBSCRIPTION_NOT_FOUND = "Subscription with name %s not found";
    public static final String SUBSCRIPTION_SHARED_WITH = "Subscription is shared with ";
    public static final String KEY_NOT_VALID = " %s the key is not valid for %s";
    public static final String MONTHLY_LIMIT_EXCESS = "Monthly limit is full for %s ";
    public static final int MONTHLY_LIMIT = 50;
    public static final int ONE_MONTH = 30;
    public static final String SUBSCRIPTION_ALREADY_SHARED = "The subscription is already shared!";


    public static final class Path {
        public static final String PATH_USER = "/api/user/";
        public static final String PATH_CATEGORY = "/api/category/";
        public static final String PATH_LOGIN = "/api/login";
        public static final String PATH_REGISTER = "api/registration";
        public static final String PATH_SUBSCRIPTION = "/api/subscription";
        public static final String PATH_SHARE = "/share";
    }
}
