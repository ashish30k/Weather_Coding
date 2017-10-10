package com.example.ashishkumar.weather.network;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import com.example.ashishkumar.weather.Constants;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;


/**
 * Created by ashishkumar on 10/9/17.
 */

public class NetworkUtil implements Constants {
    private final String LOG_TAG = NetworkUtil.class.getSimpleName();


    public String fetchCitiesWeather(final String url, String city) throws IOException {
        String modifiedUrl = url + "?q=" + city + ",us" + "&" + TEMP_UNIT_PARAM + "=" + TEMP_UNIT_FAHRENHEIT_VALUE + "&" + APP_ID_PARAM + "=" + APP_ID_PARAM_VALUE;
        Log.d(LOG_TAG, "modifiedUrl :: " + modifiedUrl);

        InputStream stream = null;
        HttpURLConnection connection = null;
        String result = null;
        try {
            connection = (HttpURLConnection) new URL(modifiedUrl).openConnection();
            connection.setRequestMethod("GET");
            connection.setDoInput(true);
            connection.connect();
            int responseCode = connection.getResponseCode();

            if (responseCode != HttpsURLConnection.HTTP_OK) {
                if (responseCode == HttpsURLConnection.HTTP_NOT_FOUND) {
                    throw new IOException(CITIES_SEARCH_EMPTY_MESSAGE);
                } else {
                    throw new IOException(SERVER_ERROR);
                }
            }
            stream = connection.getInputStream();
            if (stream != null) {
                result = readStream(stream);
            }
        } finally {
            if (stream != null) {
                stream.close();
            }
            if (connection != null) {
                connection.disconnect();
            }
        }
        Log.d(LOG_TAG, "result :: " + result);
        return result;
    }

    public Bitmap downloadWeatherConditionsIcon(String url, String iconID) throws IOException {
        String modifiedUrl = url + iconID + ".png";
        Log.d(LOG_TAG, "modifiedUrl :: " + modifiedUrl);

        InputStream stream = null;
        HttpURLConnection connection = null;
        Bitmap bitmap = null;
        try {
            connection = (HttpURLConnection) new URL(modifiedUrl).openConnection();
            connection.setRequestMethod("GET");
            connection.setDoInput(true);
            connection.connect();
            int responseCode = connection.getResponseCode();

            if (responseCode != HttpsURLConnection.HTTP_OK) {
                throw new IOException(WEATHER_ICON_FETCH_ERROR);
            }
            stream = connection.getInputStream();
            bitmap = BitmapFactory.decodeStream(stream);

        } finally {
            if (stream != null) {
                stream.close();
            }
            if (connection != null) {
                connection.disconnect();
            }
        }
        return bitmap;
    }

    private String readStream(InputStream stream) throws IOException {
        if (stream != null) {
            BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
            StringBuilder builder = new StringBuilder();

            try {
                String line = reader.readLine();
                while (line != null) {
                    builder.append(line);
                    line = reader.readLine();
                }

            } finally {
                try {
                    reader.close();
                } catch (IOException e) {

                }
            }
            return new String(builder);
        } else {
            return null;
        }
    }
}
