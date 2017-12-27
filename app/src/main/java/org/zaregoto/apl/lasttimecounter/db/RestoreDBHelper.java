package org.zaregoto.apl.lasttimecounter.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

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

        String sql;

        if (null != dumpFile) {
            File f = new File(dumpFile);
            if (!f.exists() || !f.isFile() ) {
                return;
            }
        }

        sql = ".read " + dumpFile;
        db.execSQL(sql);

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

    public boolean recoveryDatabase(Context context) {
        // read backuped DB version
        // read destination DB version

        return true;
    }

}
