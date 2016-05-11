package com.erd.nextdev2016.helper;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import com.erd.nextdev2016.LoginActivity;

import java.util.HashMap;

/**
 * Created by E.R.D on 4/2/2016.
 */
public class SessionManager {
    SharedPreferences pref;             // Shared Preferences
    SharedPreferences.Editor editor;
    Context _context;
    int PRIVATE_MODE = 0;               // Shared pref mode

    // Shared preferences file name
    private static final String PREF_NAME = "NextDev2016Login";
    private static final String KEY_IS_LOGGEDIN = "isLoggedIn";

    //ID (make variable public to access from outside)
    public static final String KEY_ID = "cid";

    // Password (make variable public to access from outside)
    public static final String KEY_PASSWORD = "pass";

    // history (make variable public to access from outside)
    public static final String KEY_CID = "cid";
    public static final String KEY_FNAME = "fname";
    public static final String KEY_LNAME = "lname";

    public SessionManager(Context context) {
        this._context = context;
        pref = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();
    }

    public void createLoginSession(String email, String pass){
        // Storing login value as TRUE
        editor.putBoolean(KEY_IS_LOGGEDIN, true);

        // Storing name in pref
        editor.putString(KEY_CID, email);
        // Storing email in pref
        editor.putString(KEY_PASSWORD, pass);



        // commit changes
        editor.commit();
    }

    public void checkLogin(){
        // Check login status
        if(!this.isLoggedIn()){
            // user is not logged in redirect him to Login Activity
            Intent i = new Intent(_context, LoginActivity.class);
            // Closing all the Activities
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            // Add new Flag to start new Activity
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            // Staring Login Activity
            _context.startActivity(i);
        }

    }

    public void setLogin(boolean isLoggedIn) {
        editor.putBoolean(KEY_IS_LOGGEDIN, isLoggedIn);
        // commit changes
        editor.commit();
    }

    //Get stored session data just name
    public HashMap<String, String> getUserDetails(){
        HashMap<String, String> user = new HashMap<String, String>();
        // user name
        user.put(KEY_CID, pref.getString(KEY_CID, null));
        user.put(KEY_FNAME, pref.getString(KEY_FNAME, null));
        user.put(KEY_LNAME, pref.getString(KEY_LNAME, null));
        // return user
        return user;
    }

    //FOR LOGOUT USER AND REMOVE SESSION

    public void logoutUser(){
        // Clearing all data from Shared Preferences
        editor.clear();
        editor.commit();
        // After logout redirect user to Loing Activity
        Intent i;
        i = new Intent(_context, LoginActivity.class);
        // Closing all the Activities
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        // Add new Flag to start new Activity
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        // Staring Login Activity
        _context.startActivity(i);
    }

    public void logoutUser_L(){
        // Clearing all data from Shared Preferences
        editor.clear();
        editor.commit();
        // After logout redirect user to Loing Activity
    }

    public boolean isLoggedIn(){
        return pref.getBoolean(KEY_IS_LOGGEDIN, false);
    }

}
