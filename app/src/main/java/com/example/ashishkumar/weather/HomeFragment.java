package com.example.ashishkumar.weather;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.Toast;

import com.example.ashishkumar.weather.model.City;
import com.example.ashishkumar.weather.model.SearchResultResponse;
import com.example.ashishkumar.weather.network.NetworkUtil;
import com.example.ashishkumar.weather.network.ParsingUtil;

import java.util.ArrayList;


/**
 * Created by ashishkumar on 10/9/17.
 */

public class HomeFragment extends Fragment implements Constants {
    private static final String LOG_TAG = HomeFragment.class.getSimpleName();
    private TextInputEditText mSearchEditText;
    private ProgressDialog mProgressDialog;
    SearchResultsAdapter mSearchResultsAdapter;

    public static Fragment newInstance() {
        return new HomeFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        final TextInputLayout searchTextInputLayout = view.findViewById(R.id.search_text_input_layout);
        mSearchEditText = (TextInputEditText) view.findViewById(R.id.search_edit_text);
        ExpandableListView searchResultsListView = (ExpandableListView) view.findViewById(R.id.search_results_list_view);
        Button searchButton = (Button) view.findViewById(R.id.search_button);
        mSearchResultsAdapter = new SearchResultsAdapter(getContext(), new ArrayList<City>());
        searchResultsListView.setAdapter(mSearchResultsAdapter);

        String savedCity = getSavedCity();
        //get the last successfully queried city, set to input fields and and call the service to fetch the current data(only if its non empty)
        if (!TextUtils.isEmpty(savedCity)) {
            mSearchEditText.setText(savedCity);
            mSearchEditText.setSelection(savedCity.length());
            callWeatherSearchService(savedCity);
        }
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (getSearchInput().length() >= 1) {
                    callWeatherSearchService(getSearchInput());
                } else {
                    // Display error message
                    searchTextInputLayout.setError(getString(R.string.search_query_empty_error_message));
                }
            }
        });
        return view;
    }

    private AsyncTask<String, String, SearchResultResponse> callWeatherSearchService(String city) {
        return new WeatherSearchAsyncTask().execute(city);
    }

    private String getSearchInput() {
        return mSearchEditText.getText().toString();
    }

    //save the searched city inside shared preferences
    private void saveSearchedCity(String city) {
        SharedPreferences sharedPreferences = getContext().getSharedPreferences(SHARED_PREF_FILE_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(SHARED_PREF_CITY_KEY, city);
        editor.apply();
    }

    private String getSavedCity() {
        SharedPreferences sharedPreferences = getContext().getSharedPreferences(SHARED_PREF_FILE_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getString(SHARED_PREF_CITY_KEY, "");
    }

    private void displayToast(String message) {
        Toast.makeText(getContext(), message, Toast.LENGTH_LONG).show();
    }

    private class WeatherSearchAsyncTask extends AsyncTask<String, String, SearchResultResponse> {
        private String errorMessage;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mProgressDialog = ProgressDialog.show(getContext(), "",
                    getString(R.string.loading), true);
            mProgressDialog.setCancelable(false);
        }

        @Override
        protected SearchResultResponse doInBackground(String... params) {
            SearchResultResponse searchResultResponse = null;
            try {
                String response = new NetworkUtil().fetchCitiesWeather(BASE_WEATHER_URL, params[0]);
                if (response != null) {
                    searchResultResponse = ParsingUtil.parseJSON(response);
                }
            } catch (Exception e) {
                errorMessage = e.getMessage();
                Log.e(LOG_TAG, e.getMessage(), e);
            }
            return searchResultResponse;
        }

        @Override
        protected void onPostExecute(SearchResultResponse searchResultResponse) {
            super.onPostExecute(searchResultResponse);
            mProgressDialog.dismiss();
            if (!TextUtils.isEmpty(errorMessage)) {
                displayToast(SERVER_ERROR);
            } else {
                if (searchResultResponse != null && searchResultResponse.getList() != null) {
                    if (searchResultResponse.getList().size() >= 1) {
                        // Save the query to shared preferences
                        saveSearchedCity(getSearchInput());
                        mSearchResultsAdapter.setData(searchResultResponse.getList());
                    } else {
                        mSearchResultsAdapter.clearData();
                        displayToast(CITIES_SEARCH_EMPTY_MESSAGE);
                    }
                } else {
                    mSearchResultsAdapter.clearData();
                    displayToast(SERVER_ERROR);
                }
            }
        }
    }
}
