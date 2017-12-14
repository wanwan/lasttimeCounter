package org.zaregoto.apl.lasttimecounter.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;

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

    static final String CREATE_HISTORIES_TABLE = "create table histories ( " +
            "_id integer, " +
            "do_date string not null );";
    static final String DROP_HISTORIES_TABLE = "drop table histories;";
    static final String HISTORIES_TABLE_NAME = "histories";


    private ArrayList<String> types;

    public ItemDBHelper(Context applicationContext) {
        super(applicationContext, DB, null, DB_VERSION);
        try {
            types = getInitialItemTypes(applicationContext);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(CREATE_ITEMTYPES_TABLE);
        sqLiteDatabase.execSQL(CREATE_HISTORIES_TABLE);
        sqLiteDatabase.execSQL(CREATE_ITEMS_TABLE);

        insertInitialTypes(sqLiteDatabase, types);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL(DROP_ITEMS_TABLE);
        sqLiteDatabase.execSQL(DROP_HISTORIES_TABLE);
        sqLiteDatabase.execSQL(DROP_ITEMTYPES_TABLE);
        onCreate(sqLiteDatabase);
    }


    private ArrayList<String> getInitialItemTypes(Context context) throws IOException {
        String[] list = context.getAssets().list("types");
        if (null != list) {
            return new ArrayList(Arrays.asList(list));
        }
        else {
            return new ArrayList<>();
        }
    }


    private void insertInitialTypes(SQLiteDatabase sqLiteDatabase, ArrayList<String> types) {

        Iterator<String> it;
        String type;
        ContentValues values;

        try {
            sqLiteDatabase.beginTransaction();

            values = new ContentValues();

            it = types.iterator();
            while (it.hasNext()) {
                type = it.next();
                values.put("filename", type);
                sqLiteDatabase.insert(ITEMTYPES_TABLE_NAME, null, values);
            }

            sqLiteDatabase.setTransactionSuccessful();
        }
        finally {
            sqLiteDatabase.endTransaction();
        }




    }

}
