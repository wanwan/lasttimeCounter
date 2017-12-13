package org.zaregoto.apl.lasttimecounter.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class ItemDBHelper extends SQLiteOpenHelper {

    static final String DB = "items.db";
    static final int DB_VERSION = 1;
    static final String CREATE_ITEMS_TABLE = "create table items ( " +
            "_id integer primary key autoincrement, " +
            "name string not null, " +
            "detail string," +
            "type_id integer," +
            "lasttime datetime," +
            "createtime datetime);";
    static final String DROP_ITEMS_TABLE = "drop table items;";
    static final String ITEMS_TABLE_NAME = "items";

    static final String CREATE_ITEMTYPES_TABLE = "create table itemtypes ( " +
            "type_id integer primary key autoincrement, " +
            "filename string not null );";
    static final String DROP_ITEMTYPES_TABLE = "drop table itemtypes;";
    static final String ITEMTYPES_TABLE_NAME = "itemtypes";


    public ItemDBHelper(Context applicationContext) {
        super(applicationContext, DB, null, DB_VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(CREATE_ITEMTYPES_TABLE);
        sqLiteDatabase.execSQL(CREATE_ITEMS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL(DROP_ITEMS_TABLE);
        sqLiteDatabase.execSQL(DROP_ITEMTYPES_TABLE);
        onCreate(sqLiteDatabase);
    }



}
