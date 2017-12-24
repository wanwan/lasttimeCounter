package org.zaregoto.apl.lasttimecounter.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import org.zaregoto.apl.lasttimecounter.Item;

import java.io.*;
import java.util.*;

public class RestoreDBHelper extends SQLiteOpenHelper {

    private static final String DB = "restore.db";
    private static final int DB_VERSION = 1;
    private String dumpFile;



    private HashMap<String, ArrayList<String>> types;

    public RestoreDBHelper(Context applicationContext, String dumpFile) {
        super(applicationContext, DB, null, DB_VERSION);
        this.dumpFile = dumpFile;
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        try {
            FileReader fr = new FileReader(new FileInputStream(new File(dumpFile));
            BufferedReader br = new BufferedReader(fr);

            String buf;
            if (null != br)) {
                while (buf = br.readLine()) {
                    
                }
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
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
