package com.commsens.tollgatetest;

import android.database.Cursor;
import android.database.CursorWrapper;

public class TollgateCursorWrapper extends CursorWrapper {
    /**
     * Creates a cursor wrapper.
     *
     * @param cursor The underlying cursor to wrap.
     */
    public TollgateCursorWrapper(Cursor cursor) {
        super(cursor);
    }

    public Tollgate getTollgate(){
        String unitCode = getString(getColumnIndex(TollgateDBSchema.TollgateTable.Cols.UNIT_CODE));
        String unitName = getString(getColumnIndex(TollgateDBSchema.TollgateTable.Cols.UNIT_NAME));
        String unitLatitude = getString(getColumnIndex(TollgateDBSchema.TollgateTable.Cols.UNIT_LATITUDE));
        String unitLongitude = getString(getColumnIndex(TollgateDBSchema.TollgateTable.Cols.UNIT_LONGITUDE));
        String routeCode = getString(getColumnIndex(TollgateDBSchema.TollgateTable.Cols.ROUTE_CODE));
        String routeName = getString(getColumnIndex(TollgateDBSchema.TollgateTable.Cols.ROUTE_NAME));

        Tollgate tollgate = new Tollgate(unitCode, unitName,
                unitLatitude, unitLongitude, routeCode,routeName);

        return tollgate;
    }
}
