package org.zaregoto.apl.lasttimecounter.db;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import org.zaregoto.apl.lasttimecounter.Item;
import org.zaregoto.apl.lasttimecounter.ItemType;

import java.util.ArrayList;
import java.util.Date;


public class ItemStore {

    static final String QUERY_TABLE = "select _id, name, detail, type_id, lasttime, createtime from items order by lasttime;";
    static final String INSERT_TABLE = "insert into items (name, detail, type_id, lasttime, createtime) values (?, ?, ?, ?, ?) ;";

    static final String QUERY_ITEMTYPES = "select section, filename from itemtypes where type_id = ?; ";


    public static boolean loadInitialData(Context context, ArrayList<Item> items) {

        ItemDBHelper dbhelper = new ItemDBHelper(context.getApplicationContext());
        SQLiteDatabase db = dbhelper.getReadableDatabase();
        Cursor cursor;
        int _id;
        String name;
        String detail;
        int type_id;
        ItemType type;
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
                    type_id = cursor.getInt(cursor.getColumnIndex("type_id"));
                    type = ItemType.createItemType(context, type_id);
                    lasttime = null;
                    createtime = null;
                    item = new Item(_id, name, detail, type, createtime, lasttime);
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
            args[2] = item.getType().getTypeId();
            args[3] = item.getCreatetime();
            args[4] = item.getLastTime();
            db.execSQL(INSERT_TABLE, args);
        }

        return true;
    }


    // TODO:
    public static ItemType getItemType(Context context, int typeId) {

        ItemDBHelper dbhelper = new ItemDBHelper(context.getApplicationContext());
        SQLiteDatabase db = dbhelper.getReadableDatabase();
        String[] args = new String[1];
        Cursor cursor;
        String filename = null;
        String section = null;

        if (null != db) {
            args[0] = Integer.toString(typeId);
            cursor = db.rawQuery(QUERY_ITEMTYPES, args);
            if (null != cursor) {
                cursor.moveToFirst();
                if (cursor.getCount() > 0) {
                    section = cursor.getString(cursor.getColumnIndex("section"));
                    filename = cursor.getString(cursor.getColumnIndex("filename"));
                }
            }
        }

        return new ItemType(typeId, section, filename);
    }
}
