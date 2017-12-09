package org.zaregoto.apl.lasttimecounter.db;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import org.zaregoto.apl.lasttimecounter.LastTimeItem;

import java.util.ArrayList;
import java.util.Date;

public class ItemStore {

    static final String QUERY_TABLE = "select _id, name, detail, type, lasttime, createtime from items order by lasttime;";

    public static boolean loadInitialData(Context context, ArrayList<LastTimeItem> items) {

        ItemDBHelper dbhelper = new ItemDBHelper(context.getApplicationContext());
        SQLiteDatabase db = dbhelper.getReadableDatabase();
        Cursor cursor;
        int _id;
        String name;
        String detail;
        int type;
        Date lasttime;
        Date createtime;
        LastTimeItem item;


        if (null != db) {
            cursor = db.rawQuery(QUERY_TABLE, null);

            if (null != cursor) {
                cursor.moveToFirst();
                while (cursor.moveToNext()) {
                    _id = cursor.getInt(cursor.getColumnIndex("_id"));
                    name = cursor.getString(cursor.getColumnIndex("name"));
                    detail = cursor.getString(cursor.getColumnIndex("detail"));
                    type = cursor.getInt(cursor.getColumnIndex("type"));
                    lasttime = null;
                    createtime = null;
                    item = new LastTimeItem(_id, name, detail, null, createtime, lasttime);
                    if (null != items) {
                        items.add(item);
                    }
                }
            }
        }

        return true;
    }

}
