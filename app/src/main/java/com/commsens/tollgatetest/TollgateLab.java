package com.commsens.tollgatetest;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;

import static com.commsens.tollgatetest.TollgateDBSchema.*;

public class TollgateLab {

    private static TollgateLab tollgateLab;
    private Context context;
    private SQLiteDatabase sqLiteDatabase;


    public TollgateLab(Context context) {
        this.context = context.getApplicationContext();
        sqLiteDatabase = new TollgateBaseHelper(this.context).getWritableDatabase();
    }

    public static TollgateLab getInstance(Context context) {
        if (tollgateLab == null) {
            tollgateLab = new TollgateLab(context);
        }
        return tollgateLab;
    }

    public ArrayList<Tollgate> getTollgates() {
        ArrayList<Tollgate> tollgates = new ArrayList<>();
        TollgateCursorWrapper cursorWrapper = queryTollgates(null, null);
        cursorWrapper.moveToFirst();

        try {
            while (!cursorWrapper.isAfterLast()){
                tollgates.add(cursorWrapper.getTollgate());
                cursorWrapper.moveToNext();
            }
        } finally {
            cursorWrapper.close();
        }

        return tollgates;
    }

    public Tollgate getTollgate(String unitCode){
        TollgateCursorWrapper wrapper = queryTollgates(
                TollgateTable.Cols.UNIT_CODE + " = ?",
                new String[] {unitCode}
        );

        try {
            if(wrapper.getCount() == 0) {
                return null;
            }
            wrapper.moveToFirst();
            return wrapper.getTollgate();
        } finally {
            wrapper.close();
        }
    }

    private static ContentValues getContentValues(Tollgate tollgate) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(TollgateTable.Cols.UNIT_CODE, tollgate.getUnitCode());
        contentValues.put(TollgateTable.Cols.UNIT_NAME, tollgate.getUnitName());
        contentValues.put(TollgateTable.Cols.UNIT_LATITUDE, tollgate.getUnitLatitude());
        contentValues.put(TollgateTable.Cols.UNIT_LONGITUDE, tollgate.getUnitLongitude());
        contentValues.put(TollgateTable.Cols.ROUTE_CODE, tollgate.getRouteCode());
        contentValues.put(TollgateTable.Cols.ROUTE_NAME, tollgate.getRouteName());
        return contentValues;
    }

    public void addTollgate(Tollgate tollgate) {
        ContentValues values = getContentValues(tollgate);
        sqLiteDatabase.insert(TollgateTable.NAME, null, values);
    }

    public void updateTollgate(Tollgate tollgate) {
        String unitCode = tollgate.getUnitCode();
        ContentValues values = getContentValues(tollgate);
        sqLiteDatabase.update(TollgateTable.NAME, values,
                TollgateTable.Cols.UNIT_CODE + " = ?",
                            new String[]{unitCode});
    }

    public void deleteTollgate(Tollgate tollgate){
        String unitCode = tollgate.getUnitCode();
        sqLiteDatabase.delete(TollgateTable.NAME,
                TollgateTable.Cols.UNIT_CODE + " = ?",
                new String[]{unitCode});
    }

    private TollgateCursorWrapper queryTollgates(String whereClause, String[] whereArgs){
        Cursor cursor = sqLiteDatabase.query(
                TollgateTable.NAME,
                null,
                whereClause,
                whereArgs,
                null,
                null,
                null
        );

        return new TollgateCursorWrapper(cursor);
    }

}
