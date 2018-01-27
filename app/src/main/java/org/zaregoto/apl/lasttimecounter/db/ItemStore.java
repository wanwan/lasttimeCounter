package org.zaregoto.apl.lasttimecounter.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Environment;
import org.zaregoto.apl.lasttimecounter.ItemHistory;
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

    private static final String QUERY_TABLE_NEW_TO_OLD
            = "select items._id as _id, name, detail, type_id, lasttime, createtime, alarm_type, day_after_lastdate " +
            "from items left join alarms on items._id = alarms._id order by lasttime desc;";
    private static final String QUERY_TABLE_OLD_TO_NEW
            = "select items._id as _id, name, detail, type_id, lasttime, createtime, alarm_type, day_after_lastdate " +
            "from items left join alarms on items._id = alarms._id order by lasttime;";
    private static final String QUERY_TABLE_ALARM_LIMIT
            = "select items._id as _id, name, detail, type_id, lasttime, createtime, alarm_type, day_after_lastdate, " +
            "date(lasttime, '+'||day_after_lastdate||' days') as alarm_limit_date " +
            "from items left join alarms on items._id = alarms._id where day_after_lastdate is not null order by alarm_limit_date;";
    private static final String QUERY_TABLE_NEW_TO_OLD_TYPEID
            = "select items._id as _id, name, detail, type_id, lasttime, createtime, alarm_type, day_after_lastdate " +
            "from items left join alarms on items._id = alarms._id where type_id = ? order by lasttime desc;";
    private static final String QUERY_TABLE_OLD_TO_NEW_TYPEID
            = "select items._id as _id, name, detail, type_id, lasttime, createtime, alarm_type, day_after_lastdate " +
            "from items left join alarms on items._id = alarms._id where type_id = ? order by lasttime;";
    private static final String QUERY_TABLE_ALARM_LIMIT_TYPEID
            = "select items._id as _id, name, detail, type_id, lasttime, createtime, alarm_type, day_after_lastdate, " +
            "datetime(lasttime, '+'||day_after_lastdate||' days') as alarm_limit_date " +
            "from items left join alarms on items._id = alarms._id where day_after_lastdate is not null and type_id = ? order by alarm_limit_date;";
    private static final String QUERY_ITEMS
            = "select items._id as _id, name, detail, type_id, lasttime, createtime, alarm_type, day_after_lastdate " +
            "from items left join alarms on items._id = alarms._id where items._id = ?;";

    private static final String QUERY_ITEM_ALARM_LIMIT
            = "select items._id as _id, name, detail, type_id, lasttime, createtime, alarm_type, day_after_lastdate, " +
            "datetime(lasttime, '+'||day_after_lastdate||' days') as alarm_limit_date " +
            "from items left join alarms on items._id = alarms._id where day_after_lastdate is not null and alarm_limit_date >= ?;";

    private static final String INSERT_TABLE = "insert into items (name, detail, type_id, lasttime, createtime) values (?, ?, ?, ?, ?) ;";
    private static final String UPDATE_TABLE = "update items set name=?, detail=?, type_id=?, lasttime=?, createtime=? where _id=? ;";
    private static final String DELETE_TABLE = "delete from items where _id = ?;";


    private static final String QUERY_ITEMTYPES = "select type_id, section, filename, label from itemtypes; ";
    private static final String QUERY_ITEMTYPE_BY_TYPEID = "select section, filename from itemtypes where type_id = ?; ";

    private static final String QUERY_ALARMS  = "select _id, alarm_type, day_after_lastdate from alarms where _id = ?;";
    private static final String INSERT_ALARMS = "insert into alarms (_id, alarm_type, day_after_lastdate) values (?, ?, ?) ;";
    private static final String UPDATE_ALARMS = "update alarms set alarm_type=?, day_after_lastdate=? where _id=? ;";
    private static final String DELETE_ALARMS = "delete from alarms where _id = ?;";

    private static final String QUERY_HISTORIES  = "select do_date from histories where _id = ?";
    private static final String DELETE_HISTORIES = "delete from histories where _id = ?";

    private static final String READ_SEQ_NO = "select seq from sqlite_sequence where name = ?";



    public static boolean loadInitialData(Context context, ArrayList<ListableUnit> items) {
        return loadData(context, items, ListableUnit.SORT_TYPE.SORT_TYPE_NEWER_TO_OLD);
    }


    public static boolean loadData(Context context, ArrayList<ListableUnit> items, ListableUnit.SORT_TYPE orderType) {
        return loadData(context, items, orderType, null);
    }



    public static boolean loadData(Context context, ArrayList<ListableUnit> items, ListableUnit.SORT_TYPE orderType, ItemType itemType) {

        ItemDBHelper dbhelper = null;
        SQLiteDatabase db = null;
        Cursor cursor = null;

        Item item;

        try {
            dbhelper = new ItemDBHelper(context.getApplicationContext());
            db = dbhelper.getReadableDatabase();

            if (null != itemType) {
                String[] args = new String[]{String.valueOf(itemType.getTypeId())};

                if (ListableUnit.SORT_TYPE.SORT_TYPE_NEWER_TO_OLD == orderType) {
                    cursor = db.rawQuery(QUERY_TABLE_NEW_TO_OLD_TYPEID, args);
                    items.add(new ItemHeader("current"));
                } else if (ListableUnit.SORT_TYPE.SORT_TYPE_OLDER_TO_NEW == orderType) {
                    cursor = db.rawQuery(QUERY_TABLE_OLD_TO_NEW_TYPEID, args);
                    items.add(new ItemHeader("oldest"));
                } else {
                    cursor = db.rawQuery(QUERY_TABLE_ALARM_LIMIT_TYPEID, args);
                    items.add(new ItemHeader("alarm limit"));
                }
            } else {
                if (ListableUnit.SORT_TYPE.SORT_TYPE_NEWER_TO_OLD == orderType) {
                    cursor = db.rawQuery(QUERY_TABLE_NEW_TO_OLD, null);
                    items.add(new ItemHeader("current"));
                } else if (ListableUnit.SORT_TYPE.SORT_TYPE_OLDER_TO_NEW == orderType) {
                    cursor = db.rawQuery(QUERY_TABLE_OLD_TO_NEW, null);
                    items.add(new ItemHeader("oldest"));
                } else {
                    cursor = db.rawQuery(QUERY_TABLE_ALARM_LIMIT, null);
                    items.add(new ItemHeader("alarm limit"));
                }
            }

            if (null != cursor) {
                cursor.moveToFirst();
                for (int i = 0; i < cursor.getCount(); i++) {

                    item = getItemFromCursor(context, cursor);

                    if (null != items) {
                        items.add(item);
                    }
                    cursor.moveToNext();
                }
            }
        } finally {
            if (null != cursor) {
                cursor.close();
            }
            if (null != db) {
                db.close();
            }
            if (null != dbhelper) {
                dbhelper.close();
            }
        }


        return true;
    }



    public static ArrayList<Item> checkAlarmList(Context context, Date now) {

        SimpleDateFormat spf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String str = spf.format(now);

        ItemDBHelper dbhelper = null;
        SQLiteDatabase db = null;
        Cursor cursor = null;
        String[] args;
        Item item;
        ArrayList<Item> ret = new ArrayList<>();

        try {
            dbhelper = new ItemDBHelper(context.getApplicationContext());
            db = dbhelper.getReadableDatabase();

            args = new String[1];
            args[0] = str;
            cursor = db.rawQuery(QUERY_ITEM_ALARM_LIMIT, args);

            if (null != cursor && cursor.getCount() > 0) {
                cursor.moveToFirst();
                for (int i = 0; i < cursor.getCount(); i++) {

                    item = getItemFromCursor(context, cursor);
                    if (null != item) {
                        ret.add(item);
                    }

                    cursor.moveToNext();
                }
            }
        }
        finally {
            if (null != cursor) {
                cursor.close();
            }
            if (null != db) {
                db.close();
            }
            if (null != dbhelper) {
                dbhelper.close();
            }
        }


        return ret;
    }



    public static boolean insertData(Context context, Item item) {

        ItemDBHelper dbhelper = null;
        SQLiteDatabase db = null;
        Object[] args = new Object[5];
        Object[] alarmargs = new Object[3];
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        try {
            dbhelper = new ItemDBHelper(context.getApplicationContext());
            db = dbhelper.getWritableDatabase();

            db.beginTransaction();

            args[0] = item.getName();
            args[1] = item.getDetail();
            args[2] = item.getType().getTypeId();
            args[3] = sdf.format(item.getLastTime());
            args[4] = sdf.format(item.getCreatetime());
            db.execSQL(INSERT_TABLE, args);

            Integer itemId = readSeqNo(db, "items");

            Alarm alarm = item.getAlarm();
            if (null != alarm && null != itemId) {
                alarmargs[0] = itemId;
                alarmargs[1] = alarm.getType().getTypeId();
                alarmargs[2] = alarm.getDays();

                db.execSQL(INSERT_ALARMS, alarmargs);
            }
            db.setTransactionSuccessful();
        } finally {
            if (null != db) {
                db.endTransaction();
                db.close();
            }
            if (null != dbhelper) {
                dbhelper.close();
            }
        }

        return true;
    }


    public static boolean updateData(MainActivity context, Item item) {

        // "update items set name=?, detail=?, type_id=?, lasttime=?, createtime=? where _id=? ;";
        ItemDBHelper dbhelper = null;
        SQLiteDatabase db = null;
        Object[] args = new Object[6];
        Object[] alarmargs = new Object[3];
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        try {
            dbhelper = new ItemDBHelper(context.getApplicationContext());
            db = dbhelper.getWritableDatabase();

            db.beginTransaction();

            args[5] = item.getId();

            args[0] = item.getName();
            args[1] = item.getDetail();
            args[2] = item.getType().getTypeId();
            args[3] = sdf.format(item.getLastTime());
            args[4] = sdf.format(item.getCreatetime());
            db.execSQL(UPDATE_TABLE, args);

            Alarm alarm = item.getAlarm();
            if (null != alarm) {
                String[] _o = new String[]{String.valueOf(item.getId())};
                Cursor c = db.rawQuery(QUERY_ALARMS, _o);
                if (0 == c.getCount()) {

                    alarmargs[0] = new Integer(item.getId());
                    alarmargs[1] = alarm.getType().getTypeId();
                    alarmargs[2] = alarm.getDays();

                    db.execSQL(INSERT_ALARMS, alarmargs);
                }
                else {
                    alarmargs[2] = item.getId();

                    alarmargs[0] = alarm.getType().getTypeId();
                    alarmargs[1] = alarm.getDays();
                    db.execSQL(UPDATE_ALARMS, alarmargs);
                }
            }

            db.setTransactionSuccessful();
        }
        finally {
            if (null != db) {
                db.endTransaction();
                db.close();
            }
            if (null != dbhelper) {
                dbhelper.close();
            }
        }

        return true;
    }


    public static boolean deleteData(Context context, ListableUnit item) {

        ItemDBHelper dbhelper = null;
        SQLiteDatabase db = null;
        Object[] args = new Object[1];

        if (!(item instanceof Item)) {
            return false;
        }

        try {
            dbhelper = new ItemDBHelper(context.getApplicationContext());
            db = dbhelper.getWritableDatabase();
            db.beginTransaction();

            args[0] = ((Item) item).getId();
            db.execSQL(DELETE_HISTORIES, args);
            db.execSQL(DELETE_TABLE, args);

            Alarm alarm = ((Item) item).getAlarm();
            if (null != alarm) {
                db.execSQL(DELETE_ALARMS, args);
            }

            db.setTransactionSuccessful();
        }
        finally {
            if (null != db) {
                db.endTransaction();
                db.close();
            }
            if (null != dbhelper) {
                dbhelper.close();
            }
        }

        return true;
    }


    public static void redoData(Context context, Item item) throws ItemDBException {

        ItemDBHelper dbhelper = null;
        SQLiteDatabase db = null;
        int itemId;
        String prevLasttime;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        if (null != context) {
            try {
                dbhelper = new ItemDBHelper(context.getApplicationContext());
                db = dbhelper.getReadableDatabase();

                db.beginTransaction();

                String[] _o = new String[]{String.valueOf(item.getId())};
                Cursor cursor = db.rawQuery(QUERY_ITEMS, _o);
                if (cursor.getCount() > 0) {
                    cursor.moveToFirst();

                    itemId = cursor.getInt(cursor.getColumnIndex("_id"));
                    prevLasttime = cursor.getString(cursor.getColumnIndex("lasttime"));

                    cursor.close();

                    // add histories
                    ContentValues cv = new ContentValues();
                    cv.put("_id", itemId);
                    cv.put("do_date", prevLasttime);
                    db.insert("histories", null, cv);

                    // modify lasttime date in items table
                    cv = new ContentValues();
                    cv.put("lasttime", sdf.format(item.getLastTime()));
                    String selection = "_id = ?";
                    String[] args = new String[]{String.valueOf(itemId)};
                    db.update("items", cv, selection, args);

                }
                else {
                    throw new ItemDBException();
                }

                db.setTransactionSuccessful();
            }
            finally {
                if (null != db) {
                    db.endTransaction();
                    db.close();
                }
                if (null != dbhelper) {
                    dbhelper.close();
                }
            }
        }
    }




    public static ArrayList<ItemType> getAllItemTypes(Context context) {

        ItemDBHelper dbhelper = null;
        SQLiteDatabase db = null;
        Cursor cursor;
        int typeId;
        String filename;
        String section;
        String label;
        ArrayList<ItemType> ret = new ArrayList<>();
        ItemType _itemType;

        try {
            dbhelper = new ItemDBHelper(context.getApplicationContext());
            db = dbhelper.getReadableDatabase();

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
        } finally {
            if (null != db) {
                db.close();
            }
            if (null != dbhelper) {
                dbhelper.close();
            }
        }

        return ret;
    }


    // TODO:
    public static ItemType getItemType(Context context, int typeId) {

        ItemDBHelper dbhelper = null;
        SQLiteDatabase db = null;
        String[] args = new String[1];
        Cursor cursor;
        String filename = null;
        String section = null;
        String label = null;

        try {
            dbhelper = new ItemDBHelper(context.getApplicationContext());
            db = dbhelper.getReadableDatabase();

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
        finally {
            if (null != db) {
                db.close();
            }
            if (null != dbhelper) {
                dbhelper.close();
            }
        }

        return new ItemType(typeId, section, filename, label);
    }


    public static ArrayList<ItemHistory> getHistoriesFromItem(Context context, Item item) {

        ItemDBHelper dbhelper = null;
        SQLiteDatabase db = null;
        ArrayList<ItemHistory> ret = new ArrayList<>();
        ItemHistory history;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        if (null == item) {
            return ret;
        }

        try {
            dbhelper = new ItemDBHelper(context.getApplicationContext());
            db = dbhelper.getReadableDatabase();

            String[] args = new String[]{String.valueOf(item.getId())};
            Cursor c = db.rawQuery(QUERY_HISTORIES, args);
            if (null != c && c.getCount() >0) {
                c.moveToFirst();
                for (int i = 0; i < c.getCount(); i++) {

                    try {
                        Date d = sdf.parse(c.getString(c.getColumnIndex("do_date")));
                        history = new ItemHistory(d);
                        ret.add(history);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    c.moveToNext();

                }
            }

        } finally {
            if (null != db) {
                db.close();
            }
            if (null != dbhelper) {
                dbhelper.close();
            }
        }

        return ret;
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


    private static Integer readSeqNo(SQLiteDatabase db, String tableName) {

        Integer ret = null;
        String[] args = new String[]{tableName};
        Cursor cursor = db.rawQuery(READ_SEQ_NO, args);

        if (null != cursor && cursor.getCount() > 0) {
            cursor.moveToFirst();
            ret = cursor.getInt((cursor.getColumnIndex("seq")));
        }

        return ret;
    }


    private static Item getItemFromCursor(Context context, Cursor cursor) {

        int _id;
        String name;
        String detail;
        int type_id;
        int alarm_type;
        int day_after_lastdate;
        Alarm alarm;
        ItemType type;
        Date lasttime;
        Date createtime;
        Item item;

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        _id = cursor.getInt(cursor.getColumnIndex("_id"));
        name = cursor.getString(cursor.getColumnIndex("name"));
        detail = cursor.getString(cursor.getColumnIndex("detail"));
        type_id = cursor.getInt(cursor.getColumnIndex("type_id"));

        if (!cursor.isNull(cursor.getColumnIndex("alarm_type"))) {
            alarm_type = cursor.getInt(cursor.getColumnIndex("alarm_type"));
            day_after_lastdate = cursor.getInt(cursor.getColumnIndex("day_after_lastdate"));
            alarm = new Alarm(alarm_type, day_after_lastdate);
        } else {
            alarm = new Alarm(Alarm.ALARM_TYPE.ALARM_TYPE_NONE);
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
        item = new Item(_id, name, detail, type, lasttime, createtime, alarm);

        return item;
    }

}
