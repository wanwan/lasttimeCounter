package org.zaregoto.apl.lasttimecounter.db;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import org.zaregoto.apl.lasttimecounter.Item;

import java.util.ArrayList;
import java.util.Date;


public class ItemStore {

    static final String QUERY_TABLE = "select _id, name, detail, type_id, lasttime, createtime from items order by lasttime;";
    static final String INSERT_TABLE = "insert into items (name, detail, type_id, lasttime, createtime) values (?, ?, ?, ?, ?) ;";

    public static boolean loadInitialData(Context context, ArrayList<Item> items) {

        ItemDBHelper dbhelper = new ItemDBHelper(context.getApplicationContext());
        SQLiteDatabase db = dbhelper.getReadableDatabase();
        Cursor cursor;
        int _id;
        String name;
        String detail;
        int type;
        Date lasttime;
        Date createtime;
        Item item;


        if (null != db) {
            cursor = db.rawQuery(QUERY_TABLE, null);

            if (null != cursor) {
                cursor.moveToFirst();
                for (int i = 0; i < cursor.getCount(); i++) {
                    _id = cursor.getInt(cursor.getColumnIndex("_id"));
                    name = cursor.getString(cursor.getColumnIndex("name"));
                    detail = cursor.getString(cursor.getColumnIndex("detail"));
                    type = cursor.getInt(cursor.getColumnIndex("type_id"));
                    lasttime = null;
                    createtime = null;
                    item = new Item(_id, name, detail, null, createtime, lasttime);
                    if (null != items) {
                        items.add(item);
                    }
                }
            }
        }

        return true;
    }


    public static boolean insertData(Context context, Item item) {

        ItemDBHelper dbhelper = new ItemDBHelper(context.getApplicationContext());
        SQLiteDatabase db = dbhelper.getWritableDatabase();
        Object[] args = new Object[5];

        if (null != db) {
            args[0] = item.getName();
            args[1] = item.getDetail();
            args[2] = null;
            args[3] = item.getCreatetime();
            args[4] = item.getLastTime();
            db.execSQL(INSERT_TABLE, args);
        }

        return true;
    }


}
