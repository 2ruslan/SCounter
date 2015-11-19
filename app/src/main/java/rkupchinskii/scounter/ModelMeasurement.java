package rkupchinskii.scounter;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.Calendar;

public class ModelMeasurement {
    private static final String[] SelectNameColumns = new String[]{DB.COLUMN_NAMES_ID
            , DB.COLUMN_NAMES_NAME
            , DB.COLUMN_NAMES_ISWEIGHT
            , DB.COLUMN_NAMES_ISTIME
            , DB.COLUMN_NAMES_ISCOUNT
    };
    private static final String[] SelectValueColumns = new String[]{DB.COLUMN_VALUES_ID
            , DB.COLUMN_VALUES_EXID
            , DB.COLUMN_VALUES_CNT_REAL
            , DB.COLUMN_VALUES_ONDATE
    };

    static public Cursor GetName(Context context) {
        SQLiteDatabase db = DB.getDBR(context);
        return db.query(DB.TABLE_NAMES, SelectNameColumns
                , DB.COLUMN_NAMES_TYPE + " = ? "
                , new String[]{String.valueOf(DB.COLUMN_NAMES_TYPE_MEAS)}, null, null, DB.COLUMN_NAMES_NAME);
    }

    static public long EditName(Context context, String id, String name) {
        SQLiteDatabase db = DB.getDBW(context);

        boolean isNew = id.equals("");

        ContentValues values = new ContentValues();
        values.put(DB.COLUMN_NAMES_NAME, name);
        if (isNew)
            values.put(DB.COLUMN_NAMES_TYPE, DB.COLUMN_NAMES_TYPE_MEAS);

        long row = isNew ?
                db.insert(DB.TABLE_NAMES, null, values) :
                db.update(DB.TABLE_NAMES, values, DB.COLUMN_NAMES_ID + " = ? ", new String[]{id});
        db.close();

        return row;
    }

    static public void DeleteName(Context context, String id) {
        SQLiteDatabase db = DB.getDBW(context);
        db.delete(DB.TABLE_VALUES, DB.COLUMN_VALUES_EXID + " = ? ", new String[]{id});
        db.delete(DB.TABLE_NAMES, DB.COLUMN_NAMES_ID + " = ? ", new String[]{id});
        db.close();
    }

    static public Cursor GetValues(Context context, String nameID) {
        SQLiteDatabase db = DB.getDBR(context);

        return db.query(DB.TABLE_VALUES, SelectValueColumns
                , DB.COLUMN_VALUES_EXID + " = ?", new String[]{nameID}
                , null, null, DB.COLUMN_VALUES_ONDATE + " DESC");
    }

    static public long EditValue(Context context, String id, String nameId, String val) {
        SQLiteDatabase db = DB.getDBW(context);

        boolean isNew = id.equals("");

        ContentValues values = new ContentValues();
        values.put(DB.COLUMN_VALUES_CNT_REAL, val);
        if (isNew) {
            values.put(DB.COLUMN_VALUES_EXID, nameId);
            values.put(DB.COLUMN_VALUES_ONDATE, DB.getDataStr(Calendar.getInstance().getTime()));
        }

        long row = isNew ?
                db.insert(DB.TABLE_VALUES, null, values) :
                db.update(DB.TABLE_VALUES, values, DB.COLUMN_VALUES_ID + " = ? ", new String[]{id});
        db.close();

        return row;
    }

    static public void DeleteValue(Context context, String id) {
        SQLiteDatabase db = DB.getDBW(context);
        db.delete(DB.TABLE_VALUES, DB.COLUMN_VALUES_ID + " = ? ", new String[]{id});
        db.close();
    }
}
