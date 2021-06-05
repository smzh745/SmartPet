package com.smartpet.online.utilities;

public class Constants {
    public static final String TAG = "SmartPet";
    public static final String IS_LOGGIN = "isLoggedIn";
    public static final String USER_DATA = "user_data";
    public static final String USER_ID = "user_id";
    public static final String USER_NAME = "user_name";
    public static final String USER_NUMBER = "user_number";
    public static final String USER_EMAIL = "user_email";
    public static final String USER_TYPE = "user_type";
    public final static int PICK_IMAGE = 101;
    public final static int PICK_CAM = 102;

    //urls
    public static final String BASE_URL = "http://192.168.0.115/pets/";
    public static final String REGISTER_USER = BASE_URL + "registerUser.php";
    public static final String LOGIN_USER = BASE_URL + "loginUser.php";
    public static final String FIND_PET = BASE_URL + "findPet.php";
    public static final String FIND_DOCTOR = BASE_URL + "findDoctor.php";
    public static final String POST_PET = BASE_URL + "postPet.php?apicall=" + "uploadpic";
    public static final String UPLOAD_FOLDER = BASE_URL+"uploads/";

}
