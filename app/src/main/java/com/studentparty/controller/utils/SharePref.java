package com.studentparty.controller.utils;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Sathyaventhan
 */
public class SharePref {
    private static final String TAG = SharePref.class.getSimpleName();

    public static final String IS_ALREADY_LAUNCH = "is_already_launch";
    public static final String IS_LOGIN = "is_login";
    public static final String LOGIN_MODE = "login_mode";
    public static final String SIGNIN_ID = "SIGNIN_ID";
    public static final String LOGIN_TYPE = "login_type";
    public static final String IS_PERMISSION = "IS_PERMISSION";
    public static final String LOGIN_INFO = "login_info";
    public static final String RECENT_NEWS_LIST = "recent_news_list";
    public static final String MY_LOCATIONDATA = "MY_LOCATIONDATA";
    public static final String MYLATTDUE = "MYLATTDUE";
    public static final String MYLONG= "MYLONG";
    public static final String ISPROFILE= "ISPROFILE";
    public static final String MYRANGE= "RANGE";
    public static final String CURRENT_LOCATION_DATA = "CURRENT_LOCATIONDATA";
    public static final String CURRENTLATIDUE = "CURRENTLATTDUE";
    public static final String CURRENTLLONGITUDE= "CURRENTLONG";
    public static final String IS_APP_LIVE = "is_app_live";
    private static SharePref mThis = null;
    private Context mContext = null;
    private SharedPreferences mPreference = null;

    private SharePref() {

    }

    public static void init(Context context) {
        if (mThis == null) {
            mThis = new SharePref();
            mThis.setData(context);
        }
        else
        {
            mThis.setData(context);
        }
    }

    private void setData(Context context) {
        mThis.mContext = context;
        mPreference = mContext.getSharedPreferences(TAG, Context.MODE_PRIVATE);
    }

    public static SharePref getInstance() {
        return mThis;
    }

    public void writeString(String tag, String data) {
        SharedPreferences.Editor editor = mPreference.edit();
        editor.putString(tag, data).commit();
    }

    public String readString(String tag) {
        return mPreference.getString(tag, null);
    }

    public void writeBoolean(String tag, boolean data) {
        SharedPreferences.Editor editor = mPreference.edit();
        editor.putBoolean(tag, data).commit();


    }

    public boolean readBoolean(String tag) {
        return mPreference.getBoolean(tag, false);
    }

    public void writeInt(String tag, int data) {
        SharedPreferences.Editor editor = mPreference.edit();
        editor.putInt(tag, data).commit();
    }

    public int readInt(String tag) {
        return mPreference.getInt(tag, 0);
    }

    public void clear(String tag)
    {
        mPreference.edit().remove(tag).commit();
    }


}
