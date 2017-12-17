package org.zaregoto.apl.lasttimecounter.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import org.zaregoto.apl.lasttimecounter.Item;

import java.io.IOException;
import java.util.*;

public class ItemDBHelper extends SQLiteOpenHelper {

    static final String DB = "items.db";
    static final int DB_VERSION = 1;

    static final String FOREIGN_KEY_ENABLE = "pragma foreign_keys = on;";

    static final String CREATE_ITEMS_TABLE = "create table items ( " +
            "_id integer primary key autoincrement, " +
            "name string not null, " +
            "detail string," +
            "type_id integer not null," +
            "lasttime datetime not null," +
            "createtime datetime not null, " +
            "foreign key(type_id) references itemtypes(type_id));";
    static final String DROP_ITEMS_TABLE = "drop table items;";
    static final String ITEMS_TABLE_NAME = "items";

    static final String CREATE_ITEMTYPES_TABLE = "create table itemtypes ( " +
            "type_id integer primary key autoincrement, " +
            "section string not null, " +
            "filename string not null );";
    static final String DROP_ITEMTYPES_TABLE = "drop table itemtypes;";
    static final String ITEMTYPES_TABLE_NAME = "itemtypes";

    static final String CREATE_HISTORIES_TABLE = "create table histories ( " +
            "_id integer, " +
            "do_date string not null );";
    static final String DROP_HISTORIES_TABLE = "drop table histories;";
    static final String HISTORIES_TABLE_NAME = "histories";


    static final String CREATE_METAINFO_TABLE = "create table metainfo (" +
            "dbversion integer," +
            "default_type_id integer);";
    static final String INSERT_METAINFO_TABLE = "insert into metainfo (dbversion, default_type_id) values (?, ?);";


    private HashMap<String, ArrayList<String>> types;

    public ItemDBHelper(Context applicationContext) {
        super(applicationContext, DB, null, DB_VERSION);
        try {
            types = getInitialItemTypes(applicationContext);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL(FOREIGN_KEY_ENABLE);

        db.execSQL(CREATE_ITEMTYPES_TABLE);
        db.execSQL(CREATE_HISTORIES_TABLE);
        db.execSQL(CREATE_ITEMS_TABLE);

        db.execSQL(CREATE_METAINFO_TABLE);

        insertInitialTypes(db, types);
        insertMetaInfo(db, DB_VERSION, Item.DEFAULT_TYPE_ID);

    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL(DROP_ITEMS_TABLE);
        db.execSQL(DROP_HISTORIES_TABLE);
        db.execSQL(DROP_ITEMTYPES_TABLE);
        onCreate(db);
    }


    @Override
    public void onOpen(SQLiteDatabase db) {
        super.onOpen(db);
        if (!db.isReadOnly()) {
            // Enable foreign key constraints
            db.execSQL("PRAGMA foreign_keys=ON;");
        }
    }


    @Override
    public void onConfigure(SQLiteDatabase db){
        super.onConfigure(db);
        db.setForeignKeyConstraintsEnabled(true);
    }


    // TODO:
    private HashMap<String, ArrayList<String>> getInitialItemTypes(Context context) throws IOException {

        HashMap<String, ArrayList<String>> ret = new HashMap<>();
        String section;
        String[] _list;
        ArrayList<String> array;

        section = "toggle";
        _list = context.getAssets().list("typeicons" + "/" + section);
        if (null != _list) {
            array = new ArrayList(Arrays.asList(_list));
            ret.put(section, array);
        }

        section = "alert";
        _list = context.getAssets().list("typeicons" + "/" + section);
        if (null != _list) {
            array = new ArrayList(Arrays.asList(_list));
            ret.put(section, array);
        }

        return ret;
    }


    private void insertInitialTypes(SQLiteDatabase sqLiteDatabase, HashMap<String, ArrayList<String>> types) {

        Iterator<String> it;
        Iterator<String> it2;
        String type;
        ContentValues values;
        ArrayList<String> filenames;
        String section;

        try {
            sqLiteDatabase.beginTransaction();

            values = new ContentValues();

            Set<String> keys = types.keySet();

            it = keys.iterator();
            while (it.hasNext()) {
                section = it.next();
                filenames = types.get(section);
                it2 = filenames.iterator();
                while (it2.hasNext()) {
                    type = it2.next();
                    values.put("filename", type);
                    values.put("section", section);
                    sqLiteDatabase.insert(ITEMTYPES_TABLE_NAME, null, values);
                }
            }

            sqLiteDatabase.setTransactionSuccessful();
        }
        finally {
            sqLiteDatabase.endTransaction();
        }

    }


    private void insertMetaInfo(SQLiteDatabase db, int dbVersion, int defaultTypeId) {

        Object[] args = new Object[2];

        args[0] = dbVersion;
        args[1] = defaultTypeId;

        db.execSQL(INSERT_METAINFO_TABLE, args);
    }



}
