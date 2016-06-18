package com.example.paranoid.geoloc_gid;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.google.android.gms.maps.model.LatLng;

public class GeoObject {
    public  String name;
    public  String dscr;
    public  double lat;
    public  double lng;

    public LatLng getLatLng(){
        return new LatLng(lat, lng);
    }

    public static GeoObject[] getAll(Context context){

        DataBaseContext ctx = new DataBaseContext(context);
        SQLiteDatabase db = ctx.openOrCreateDatabase("geoDB.sqlite", 0, null);

        String[]    columns = {"lat", "lng", "name", "dscr"};
        Cursor cur = db.query("objects", columns, null, null, null, null, null, null);


        GeoObject[] result = new GeoObject[cur.getCount()];
        int i = 0;
        while (cur.moveToNext()){
            GeoObject obj = new GeoObject();

            obj.name = cur.getString(cur.getColumnIndex("name"));
            obj.dscr = cur.getString(cur.getColumnIndex("dscr"));
            obj.lat = cur.getDouble(cur.getColumnIndex("lat"));
            obj.lng = cur.getDouble(cur.getColumnIndex("lng"));
            result[i++] = obj;
        }
        cur.close();

        return result;
    }
}