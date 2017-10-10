package com.example.ashishkumar.weather.network;

import com.example.ashishkumar.weather.model.SearchResultResponse;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

/**
 * Created by ashishkumar on 10/9/17.
 */

public class ParsingUtil {
    public static SearchResultResponse parseJSON(String response) {
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.setLenient();
        Gson gson = gsonBuilder.create();
        return gson.fromJson(response, SearchResultResponse.class);
    }

}
