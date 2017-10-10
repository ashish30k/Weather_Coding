package com.example.ashishkumar.weather;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.ashishkumar.weather.model.City;
import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;
import java.util.List;

import static com.example.ashishkumar.weather.Constants.BASE_WEATHER_ICON_URL;

/**
 * Created by ashishkumar on 10/9/17.
 */

public class SearchResultsAdapter extends BaseExpandableListAdapter {
    private Context context;
    private List<City> citiesList;

    public SearchResultsAdapter(Context context, List<City> cityList) {
        this.context = context;
        this.citiesList = cityList;
    }

    @Override
    public City getChild(int groupPosition, int childPosition) {
        return citiesList.get(groupPosition);
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild,
                             View view, ViewGroup parent) {
        City city = getChild(groupPosition, childPosition);
        String iconUrl = BASE_WEATHER_ICON_URL + city.getWeather().get(0).getIcon() + ".png";

        if (view == null) {
            LayoutInflater infalInflater = (LayoutInflater)
                    context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = infalInflater.inflate(R.layout.search_result_child, parent, false);
        }
        TextView cityTv = (TextView) view.findViewById(R.id.city);
        TextView tempTv = (TextView) view.findViewById(R.id.temp);
        TextView conditionTv = (TextView) view.findViewById(R.id.weather_condition_tv);
        ImageView conditionIv = (ImageView) view.findViewById(R.id.weather_condition_iv);

        cityTv.setText(city.getName());
        tempTv.setText(String.format("%s%s", new DecimalFormat("#").format(city.getMain().getTemp()), context.getString(R.string.fahrenheit)));
        conditionTv.setText(city.getWeather().get(0).getDescription());
        Picasso.with(context).load(iconUrl).into(conditionIv);
        return view;
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return 1;
    }

    @Override
    public City getGroup(int groupPosition) {
        return citiesList.get(groupPosition);
    }

    @Override
    public int getGroupCount() {
        return citiesList.size();
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isLastChild, View view,
                             ViewGroup parent) {

        City city = getGroup(groupPosition);
        if (view == null) {
            LayoutInflater inf = (LayoutInflater)
                    context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inf.inflate(R.layout.search_results_group, parent, false);
        }
        TextView cityTv = (TextView) view.findViewById(R.id.search_result_group_city_text_view);
        cityTv.setText(String.format(context.getString(R.string.city_description), city.getName(), String.valueOf(city.getCoord().getLat()), String.valueOf(city.getCoord().getLon())));
        return view;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

    public void clearData() {
        citiesList.clear();
        notifyDataSetChanged();
    }

    public void setData(List<City> citiesList) {
        this.citiesList = citiesList;
        notifyDataSetChanged();
    }

}
