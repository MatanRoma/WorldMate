package com.example.androidsecondproject.model;

public class TranslateString {
    public static boolean checkMale(String xmlString){
        final String MALE_ENGLISH = "male";
        final String MALE_HEBREW ="זכר";
        if((xmlString.equals(MALE_ENGLISH)|| xmlString.equals(MALE_HEBREW)))
        {
            return true;
        }
        return false;
    }

    public static boolean checkFemale(String xmlString){
        final String FEMALE_ENGLISH = "female";
        final String FEMALE_HEBREW ="נקבה";
        if((xmlString.equals(FEMALE_ENGLISH)|| xmlString.equals(FEMALE_HEBREW)))
        {
            return true;
        }
        return false;
    }

    public static boolean checkSport(String xmlString){
        final String SPORT_ENGLISH = "sport";
        final String SPORT_HEBREW ="ספורט";
        if((xmlString.equals(SPORT_ENGLISH)|| xmlString.equals(SPORT_HEBREW)))
        {
            return true;
        }
        return false;
    }

    public static boolean checkfood(String xmlString){
        final String FOOD_ENGLISH = "food";
        final String FOOD_HEBREW ="אוכל";
        if((xmlString.equals(FOOD_ENGLISH)|| xmlString.equals(FOOD_HEBREW)))
        {
            return true;
        }
        return false;
    }

    public static boolean checkCulture(String xmlString){
        final String CULTURE_ENGLISH = "culture";
        final String CULTURE_HEBREW ="תרבות";
        if((xmlString.equals(CULTURE_ENGLISH)|| xmlString.equals(CULTURE_HEBREW)))
        {
            return true;
        }
        return false;
    }

    public static boolean checkMusic(String xmlString){
        final String MUSIC_ENGLISH = "music";
        final String MUSIC_HEBREW ="מוזיקה";
        if((xmlString.equals(MUSIC_ENGLISH)|| xmlString.equals(MUSIC_HEBREW)))
        {
            return true;
        }
        return false;
    }

    public static String HebToEnglish(String str)
    {
        switch (str){
            case "ספורט":
                return "sport";
            case "אוכל":
                return "food";
            case "תרבות":
                return "culture";
            case "מוזיקה":
                return "music";
        }
        return str;
    }
}
