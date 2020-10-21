package com.example.facear.Utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import java.util.Map;

public class Preferences {

    private static final String APP_PREFERENCES = "veriFace";

    public static final String BASE_URL = "BASE_URL";
    public static final String API_Key = "API_KEY";
    public static final String API_SecretKey = "API_SECRET_KEY";
    public static final String UserName = "UserName";
    public static final String isSaved = "isSaved";
    public static final String api_token = "api_token";
    public static final String user_session = "user_session";
    public static final String isLogin = "isLogin";

    // profile Setting
    public static final String isUserProfile = "isUserProfile";
    public static final String PROFILE_FIRSTNAME = "PROFILE_FIRSTNAME";
    public static final String PROFILE_LASTNAME = "PROFILE_LASTNAME";
    public static final String PROFILE_USEREMAIL= "PROFILE_USEREMAIL";
    public static final String PROFILE_COUNTRYNAME = "PROFILE_COUNTRYNAME";
    public static final String PROFILE_CITY = "PROFILE_CITY";
    public static final String PROFILE_STREET1 = "PROFILE_STREET1";
    public static final String PROFILE_STREET2 = "PROFILE_STREET2";
    public static final String PROFILE_REGION = "PROFILE_REGION";
    public static final String PROFILE_POSTCODE = "PROFILE_POSTCODE";
    public static final String PROFILE_PHONENUMBER = "PROFILE_PHONENUMBER";
    public static final String PROFILE_BIRTHDAY = "PROFILE_BIRTHDAY";
    public static final String PROFILE_PASSWORD = "PROFILE_PASSWORD";
    public static final String PROFILE_ADDRESS_DOCPATH = "PROFILE_ADDRESS_DOCPATH";
    public static final String PROFILE_PASSPORTPATH = "PROFILE_PASSPORTPATH";
    public static final String PROFILE_IDPHOTOPATH = "PROFILE_IDPHOTOPATH";
    public static final String PROFILE_DRIVINGPATH = "PROFILE_DRIVINGPATH";
    public static final String PROFILE_RESIDENT_DOCPATH = "PROFILE_RESIDENT_DOCPATH";
    public static final String PROFILE_COUNTRYFLAG = "PROFILE_COUNTRYFLAG";
    public static final String PROFILE_EMAILVERIFY = "PROFILE_EMAILVERIFY";
    public static final String PROFILE_PHONEVERIFY = "PROFILE_PHONEVERIFY";
    // FaceMatch to ID card
    public static final String FACEMATCHIDPATH = "FACEMATCHIDPATH";
    // IDCardVerification path
    public static final String IDCARD_VERIFY_PATH = "IDCARD_VERIFY_PATH";
    public static final String DIVING_VERIFY_PATH = "DIVING_VERIFY_PATH";
    public static final String RESIDENT_VERIFY_PATH = "RESIDENT_VERIFY_PATH";

    // TODO: GET & SET STRING
    public static String getValue_String(Context context, String Key) {
        SharedPreferences settings = context.getSharedPreferences(
                APP_PREFERENCES, 0);
        return settings.getString(Key, "");
    }

    public static void setValue(Context context, String Key, String Value) {
        SharedPreferences settings = context.getSharedPreferences(APP_PREFERENCES, 0);
        SharedPreferences.Editor editor = settings.edit();
        editor.putString(Key, Value);
        editor.apply();
    }

    // TODO: GET & SET INTEGER
    public static int getValue_Integer(Context context, String Key) {
        SharedPreferences settings = context.getSharedPreferences(
                APP_PREFERENCES, 0);
        return settings.getInt(Key, 1);
    }

    public static void setValue(Context context, String Key, int Value) {
        SharedPreferences settings = context.getSharedPreferences(
                APP_PREFERENCES, 0);
        SharedPreferences.Editor editor = settings.edit();
        editor.putInt(Key, Value);
        editor.apply();
    }

    // TODO: GET & SET FLOAT
    public static float getValue_Float(Context context, String Key) {
        SharedPreferences settings = context.getSharedPreferences(
                APP_PREFERENCES, 0);
        return settings.getFloat(Key, 0.0f);
    }

    public static void setValue(Context context, String Key, float Value) {
        SharedPreferences settings = context.getSharedPreferences(
                APP_PREFERENCES, 0);
        SharedPreferences.Editor editor = settings.edit();
        editor.putFloat(Key, Value);
        editor.apply();
    }

    // TODO: GET & SET LONG
    public static long getValue_Long(Context context, String Key) {
        SharedPreferences settings = context.getSharedPreferences(
                APP_PREFERENCES, 0);
        return settings.getLong(Key, 0);
    }

    public static void setValue(Context context, String Key, long Value) {
        SharedPreferences settings = context.getSharedPreferences(
                APP_PREFERENCES, 0);
        SharedPreferences.Editor editor = settings.edit();
        editor.putLong(Key, Value);
        editor.apply();
    }

    // TODO: GET & SET BOOLEAN
    public static boolean getValue_Boolean(Context context, String Key,
                                           boolean Default) {
        SharedPreferences settings = context.getSharedPreferences(
                APP_PREFERENCES, 0);
        return settings.getBoolean(Key, Default);
    }

    public static void setValue(Context context, String Key, boolean Value) {
        SharedPreferences settings = context.getSharedPreferences(
                APP_PREFERENCES, 0);
        SharedPreferences.Editor editor = settings.edit();
        editor.putBoolean(Key, Value);
        editor.apply();
    }

    // TODO: GET & SET String Array
    public static void saveArray(Context context, String[] array,
                                 String arrayName) {
        SharedPreferences settings = context.getSharedPreferences(
                APP_PREFERENCES, 0);
        SharedPreferences.Editor editor = settings.edit();
        Map<String, ?> keys = settings.getAll();

        for (Map.Entry<String, ?> entry : keys.entrySet()) {
            Log.d("map values", entry.getKey() + ": "
                    + entry.getValue().toString());
            if (entry.getKey().contains(arrayName + "_"))
                editor.remove(entry.getKey());
        }
        editor.putInt(arrayName + "_size", array.length);
        for (int i = 0; i < array.length; i++)
            editor.putString(arrayName + "_" + i, array[i]);
        Log.e("Save Succeed?", editor.commit() + "");
    }

    public static String[] loadArray(Context context, String arrayName) {
        SharedPreferences settings = context.getSharedPreferences(
                APP_PREFERENCES, 0);
        int size = settings.getInt(arrayName + "_size", 0);
        String array[] = new String[size];
        for (int i = 0; i < size; i++)
            array[i] = settings.getString(arrayName + "_" + i, null);

        Log.e("Loaded Array Size", array.length + "");
        return array;
    }

    public static int getArraySize(Context context, String arrayName) {
        SharedPreferences settings = context.getSharedPreferences(
                APP_PREFERENCES, 0);
        return settings.getInt(arrayName + "_size", 0);
    }


}
