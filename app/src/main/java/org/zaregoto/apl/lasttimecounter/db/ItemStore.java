package org.zaregoto.apl.lasttimecounter.db;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Environment;
import org.zaregoto.apl.lasttimecounter.model.*;
import org.zaregoto.apl.lasttimecounter.ui.MainActivity;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;


public class ItemStore {

    static final String QUERY_TABLE_NEW_TO_OLD = "select items._id as _id, name, detail, type_id, lasttime, createtime, alarm_id, day_after_lastdate from items left join alarms on items._id = alarms._id order by lasttime desc;";
    static final String QUERY_TABLE_OLD_TO_NEW = "select items._id as _id, name, detail, type_id, lasttime, createtime, alarm_id, day_after_lastdate from items left join alarms on items._id = alarms._id order by lasttime;";
    static final String INSERT_TABLE = "insert into items (name, detail, type_id, lasttime, createtime) values (?, ?, ?, ?, ?) ;";
    static final String UPDATE_TABLE = "update items set name=?, detail=?, type_id=?, lasttime=?, createtime=? where _id=? ;";
    static final String DELETE_TABLE = "delete from items where _id = ?;";

    static final String QUERY_ITEMTYPES = "select type_id, section, filename, label from itemtypes; ";
    static final String QUERY_ITEMTYPE_BY_TYPEID = "select section, filename from itemtypes where type_id = ?; ";


    public static boolean loadInitialData(Context context, ArrayList<Item> items) {
        return loadData(context, items, OrderType.ORDER_TYPE_CURRENT_TO_OLD);
    }



    public static boolean loadData(Context context, ArrayList<Item> items, OrderType orderType) {

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

        int alarm_id;
        int day_after_lastdate;
        Alarm alarm;

        ItemUnit item;

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        if (null != db) {
            if (OrderType.ORDER_TYPE_CURRENT_TO_OLD == orderType) {
                cursor = db.rawQuery(QUERY_TABLE_NEW_TO_OLD, null);
                items.add(new ItemHeader("current"));
            }
            else {
                cursor = db.rawQuery(QUERY_TABLE_OLD_TO_NEW, null);
                items.add(new ItemHeader("oldest"));
            }

            if (null != cursor) {
                cursor.moveToFirst();
                for (int i = 0; i < cursor.getCount(); i++) {
                    _id = cursor.getInt(cursor.getColumnIndex("_id"));
                    name = cursor.getString(cursor.getColumnIndex("name"));
                    detail = cursor.getString(cursor.getColumnIndex("detail"));
                    type_id = cursor.getInt(cursor.getColumnIndex("type_id"));

                    if (! cursor.isNull(cursor.getColumnIndex("alarm_id"))) {
                        alarm_id = cursor.getInt(cursor.getColumnIndex("alarm_id"));
                        day_after_lastdate = cursor.getInt(cursor.getColumnIndex("day_after_lastdate"));
                        alarm = new Alarm(alarm_id, day_after_lastdate);
                    }
                    else {
                        alarm = null;
                    }

                    type = ItemType.createItemType(context, type_id);
                    try {
                        lasttime = sdf.parse(cursor.getString(cursor.getColumnIndex("lasttime")));
                    } catch (ParseException e) {
                        lasttime = null;
                        e.printStackTrace();
                    }
                    try {
                        createtime = sdf.parse(cursor.getString(cursor.getColumnIndex("createtime")));
                    } catch (ParseException e) {
                        createtime = null;
                        e.printStackTrace();
                    }
                    item = new ItemUnit(_id, name, detail, type, lasttime, createtime, alarm);
                    if (null != items) {
                        items.add(item);
                    }
                    cursor.moveToNext();
                }
            }
        }

        return true;
    }



    public static boolean insertData(Context context, ItemUnit item) {

        ItemDBHelper dbhelper = new ItemDBHelper(context.getApplicationContext());
        SQLiteDatabase db = dbhelper.getWritableDatabase();
        Object[] args = new Object[5];
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        if (null != db) {
            args[0] = item.getName();
            args[1] = item.getDetail();
            args[2] = item.getType().getTypeId();
            args[3] = sdf.format(item.getLastTime());
            args[4] = sdf.format(item.getCreatetime());
            db.execSQL(INSERT_TABLE, args);
        }

        return true;
    }


    public static boolean updateData(MainActivity context, ItemUnit item) {

        // "update items set name=?, detail=?, type_id=?, lasttime=?, createtime=? where _id=? ;";
        ItemDBHelper dbhelper = new ItemDBHelper(context.getApplicationContext());
        SQLiteDatabase db = dbhelper.getWritableDatabase();
        Object[] args = new Object[6];
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        if (null != db) {
            args[5] = item.getId();

            args[0] = item.getName();
            args[1] = item.getDetail();
            args[2] = item.getType().getTypeId();
            args[3] = sdf.format(item.getLastTime());
            args[4] = sdf.format(item.getCreatetime());
            db.execSQL(UPDATE_TABLE, args);
        }

        return true;
    }


    public static boolean deleteData(Context context, Item item) {

        ItemDBHelper dbhelper = new ItemDBHelper(context.getApplicationContext());
        SQLiteDatabase db = dbhelper.getWritableDatabase();
        Object[] args = new Object[1];

        if (null != db && item instanceof ItemUnit) {
            args[0] = ((ItemUnit)item).getId();
            db.execSQL(DELETE_TABLE, args);
        }

        return true;
    }


    public static ArrayList<ItemType> getAllItemTyps(Context context) {

        ItemDBHelper dbhelper = new ItemDBHelper(context.getApplicationContext());
        SQLiteDatabase db = dbhelper.getReadableDatabase();
        Cursor cursor;
        int typeId = -1;
        String filename = null;
        String section = null;
        String label = null;
        ArrayList<ItemType> ret = new ArrayList<>();
        ItemType _itemType;

        if (null != db) {
            cursor = db.rawQuery(QUERY_ITEMTYPES, null);
            if (null != cursor) {
                cursor.moveToFirst();
                for (int i = 0; i < cursor.getCount(); i++) {
                    typeId = cursor.getInt(cursor.getColumnIndex("type_id"));
                    section = cursor.getString(cursor.getColumnIndex("section"));
                    filename = cursor.getString(cursor.getColumnIndex("filename"));
                    label = null;
                    _itemType = new ItemType(typeId, section, filename, label);
                    ret.add(_itemType);
                    cursor.moveToNext();
                }
            }
        }

        return ret;
    }


    // TODO:
    public static ItemType getItemType(Context context, int typeId) {

        ItemDBHelper dbhelper = new ItemDBHelper(context.getApplicationContext());
        SQLiteDatabase db = dbhelper.getReadableDatabase();
        String[] args = new String[1];
        Cursor cursor;
        String filename = null;
        String section = null;
        String label = null;

        if (null != db) {
            args[0] = Integer.toString(typeId);
            cursor = db.rawQuery(QUERY_ITEMTYPE_BY_TYPEID, args);
            if (null != cursor) {
                cursor.moveToFirst();
                if (cursor.getCount() > 0) {
                    section = cursor.getString(cursor.getColumnIndex("section"));
                    filename = cursor.getString(cursor.getColumnIndex("filename"));
                    label = null;
                }
            }
        }

        return new ItemType(typeId, section, filename, label);
    }


    public static boolean makeBackup(Context context, File dir, String filename) throws IOException {

        File sd = Environment.getExternalStorageDirectory();
        boolean ret = false;

        if (sd.canWrite()) {
            String currentDBPath = "/data/data/" + context.getPackageName() + "/databases/" + ItemDBHelper.DB;
            File currentDB = new File(currentDBPath);
            File backupDB = new File(sd, filename);

            if (currentDB.exists()) {
                FileChannel src = new FileInputStream(currentDB).getChannel();
                FileChannel dst = new FileOutputStream(backupDB).getChannel();
                dst.transferFrom(src, 0, src.size());
                src.close();
                dst.close();
                ret = true;
            }
        }

        return ret;
    }


    public static boolean restoreBackup(File dir, String filename) {


        return true;
    }


    public enum OrderType {

        ORDER_TYPE_CURRENT_TO_OLD,
        ORDER_TYPE_OLD_TO_CURRENT
    }
}
