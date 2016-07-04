package com.esp.testvideo.Utils;

import android.os.Environment;

public class Config {
    public static String TAG = "MyMaps";
    public static String DB_NAME = "root.db";
    // Create a directory in SD CARD
    public static String APP_HOME = Environment.getExternalStorageDirectory().getPath() + "/" + TAG;
    // A directory to store logs
    public static String DIR_LOG = APP_HOME + "/log";
    public static final String PREF_FILE = TAG + "_PREF";
    public static String API_KEY = "AAASSA";
    public static String DIR_USERDATA = APP_HOME + "/userdata";


    public static int API_SUCCESS = 0;
    public static int API_FAIL = 1;

    // SOCKET TIMEOUT IS SET TO 30 SECONDS
    public static int TIMEOUT_SOCKET = 30000;

    //    public static final String YOUTUBE_API_KEY = "AIzaSyCHDQ3Qom0D3UdQTn6aCuZ2BHlJAoZnLpA"; // final 1
    public static final String YOUTUBE_API_KEY = "AIzaSyDqc4on_Q3XKR2TDuJSQN0SxDd3YDjC9Ko"; // final 2


}