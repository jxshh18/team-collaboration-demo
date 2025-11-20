package com.example.dormmamu.utils;

import android.content.Context;
import android.content.SharedPreferences;

public class SessionManager {

    private static final String PREF_NAME = "UserSession";
    private static final String KEY_USERNAME = "username";
    private static final String KEY_EMAIL = "email";
    private static final String KEY_DESCRIPTION = "description";
    private static final String KEY_CONTACT = "contact";
    private static final String KEY_ADDRESS = "address"; // ✅ Fixed typo here
    private static final String KEY_IS_LOGGED_IN = "isLoggedIn";

    private SharedPreferences prefs;
    private SharedPreferences.Editor editor;

    public SessionManager(Context context) {
        prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        editor = prefs.edit();
    }

    public void saveUserSession(String username, String email) {
        editor.putString(KEY_USERNAME, username);
        editor.putString(KEY_EMAIL, email);
        editor.putBoolean(KEY_IS_LOGGED_IN, true);
        editor.apply();
    }

    public void saveProfileInfo(String description, String contact, String address) {
        editor.putString(KEY_DESCRIPTION, description);
        editor.putString(KEY_CONTACT, contact);
        editor.putString(KEY_ADDRESS, address);
        editor.apply();
    }

    public String getUsername() { return prefs.getString(KEY_USERNAME, ""); }
    public String getEmail() { return prefs.getString(KEY_EMAIL, ""); }
    public String getDescription() { return prefs.getString(KEY_DESCRIPTION, ""); }
    public String getContact() { return prefs.getString(KEY_CONTACT, ""); }
    public String getAddress() { return prefs.getString(KEY_ADDRESS, ""); }

    public boolean isLoggedIn() { return prefs.getBoolean(KEY_IS_LOGGED_IN, false); }

    public void logout() { clearSession(); }

    public void clearSession() {
        editor.clear();
        editor.apply();
    }
}
