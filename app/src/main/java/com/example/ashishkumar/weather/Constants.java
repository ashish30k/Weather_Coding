package com.example.ashishkumar.weather;

/**
 * Created by ashishkumar on 10/9/17.
 */

public interface Constants {

    String BASE_WEATHER_URL = "http://api.openweathermap.org/data/2.5/find";
    String BASE_WEATHER_ICON_URL = "http://openweathermap.org/img/w/";
    String APP_ID_PARAM = "APPID";
    String TEMP_UNIT_PARAM = "units";
    String TEMP_UNIT_FAHRENHEIT_VALUE = "imperial";
    String APP_ID_PARAM_VALUE = "7d50b9391e5ebf0ade8b4c99f9e7fb71";

    String CITIES_SEARCH_EMPTY_MESSAGE = "No Data Found";
    String SERVER_ERROR = "Network Issue";
    String WEATHER_ICON_FETCH_ERROR = "Could not retrieve weather condition image";

    String SHARED_PREF_FILE_NAME = "com.example.ashishkumar.weather.PREFERENCE_FILE_KEY";
    String SHARED_PREF_CITY_KEY = "city";
}
