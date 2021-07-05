package com.example.car.ConstantsUtility;

public class Constants {
    public static final String PRIVATE_CODE_KEY = "privateCode";
    public static final String PHONE_KEY_WITH_PLUS = "phoneNumberPlus";
    public static final String PHONE_KEY_WITHOUT_PLUS = "phoneNumber";
    public static final String MODEL_KEY = "model";
    public static final String MATRICULE_KEY = "matricule";
    public static final String MARQUE_KEY = "marque";

    public static final String FILE_NAME = "Car_Data.txt";

    public static final String DEFAULT_TEXT = "You haven't yet set car data";

    public static final int PERMISSION_CODE = 1000;

    public static final int REQUEST_CHECK_SETTING = 1001;

    public static final String[] messages = {
            "Le code privé est trop court, saisissez au moins 8 caractères",
            "Le code privé doit contenir au moins 2 chiffres",
            "Le code privé doit contenir au moins 2 caractères spéciaux",
            "Le code privé doit contenir au moins 2 caractères majuscules"};

    public static final String DIGIT = "0123456789";

    public static final String SPECIAL_CHAR = "!\"#$%&'()*+,-./:;<=>?@[]^_`{|}~";

    public static final String ALPHA_UPPER_CASE = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";

    public static final String ALPHA_LOWER_CASE = "abcdefghijklmnopqrstuvxyz";
    public static final int LOCATION_SERVICE_ID = 175;

    public static final String ACTION_START_LOCATION_SERVICE = "startLocationService";

    public static final String ACTION_STOP_LOCATION_SERVICE = "stopLocationService";

}
