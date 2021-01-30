package com.commsens.tollgatetest;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import com.commsens.tollgatetest.TollgateDBSchema.TollgateTable;

public class TollgateBaseHelper  extends SQLiteOpenHelper {
    private static final int VERSION = 1;
    private static final String DATABASE_NAME = "tollgate.db";

    public TollgateBaseHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table " + TollgateTable.NAME + "(" +
                        TollgateTable.Cols.UNIT_CODE + ", " +
                        TollgateTable.Cols.UNIT_NAME + ", " +
                        TollgateTable.Cols.UNIT_LATITUDE + ", " +
                        TollgateTable.Cols.UNIT_LONGITUDE + ", " +
                        TollgateTable.Cols.ROUTE_CODE + ", " +
                        TollgateTable.Cols.ROUTE_NAME + ")"
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
