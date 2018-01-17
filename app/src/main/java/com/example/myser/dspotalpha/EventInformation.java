package com.example.myser.dspotalpha;

/**
 * Created by myser on 08-Dec-17.
 */

public class EventInformation {

    public String title, date, website, time, description, cover, location;
    public double latitude, longitude;

    public EventInformation () {}

    public EventInformation (String _title, String _date, String _website, String _time, String _description, String _photo, String _location, double _latitude, double _longitude) {
        title = _title;
        date = _date;
        website = _website;
        time = _time;
        description = _description;
        cover =  _photo;
        location = _location;
        latitude = _latitude;
        longitude = _longitude;
    }

}
