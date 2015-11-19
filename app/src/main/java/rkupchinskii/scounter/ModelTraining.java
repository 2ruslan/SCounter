package rkupchinskii.scounter;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class ModelTraining {


    private static final String[] SelectNameColumns = new String[]{DB.COLUMN_NAMES_ID
            , DB.COLUMN_NAMES_NAME
            , DB.COLUMN_NAMES_ONDATE
    };
    private static final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

    static public Cursor GetName(Context context) {
        SQLiteDatabase db = DB.getDBR(context);
        return db.query(DB.TABLE_NAMES, SelectNameColumns
                , DB.COLUMN_NAMES_TYPE + " = ? "
                , new String[]{String.valueOf(DB.COLUMN_NAMES_TYPE_TRAIN)}, null, null, DB.COLUMN_NAMES_ID + " DESC");
    }

    static public long EditName(Context context, String id, String name) {
        SQLiteDatabase db = DB.getDBW(context);

        boolean isNew = id.equals("");

        ContentValues values = new ContentValues();
        values.put(DB.COLUMN_NAMES_NAME, name);

        if (isNew)
            values.put(DB.COLUMN_NAMES_TYPE, DB.COLUMN_NAMES_TYPE_TRAIN);

        long row = isNew ?
                db.insert(DB.TABLE_NAMES, null, values) :
                db.update(DB.TABLE_NAMES, values, DB.COLUMN_NAMES_ID + " = ? ", new String[]{id});
        db.close();

        return row;
    }

    static public void DeleteName(Context context, String id) {
        SQLiteDatabase db = DB.getDBW(context);
      //  db.delete(DB.TABLE_VALUES, DB.COLUMN_VALUES_EXID + " = ? ", new String[]{id});
        db.delete(DB.TABLE_NAMES, DB.COLUMN_NAMES_ID + " = ? ", new String[]{id});
        db.close();
    }

    public static String getDefaulttrainingId(Context context) {
        String defName = sdf.format(Calendar.getInstance().getTime());
        Cursor cursor = null;
        long res = -1;
        SQLiteDatabase db = DB.getDBR(context);
        try {
            cursor = db.query(DB.TABLE_NAMES, SelectNameColumns
                    , DB.COLUMN_NAMES_NAME + " = ? "
                    , new String[]{defName}, null, null, null);

            if (cursor.getCount() > 0) {
                cursor.moveToFirst();
                res = cursor.getLong(cursor.getColumnIndex(DB.COLUMN_NAMES_ID));
            }

        } finally {
            cursor.close();
            db.close();
        }

        if (res == -1)
            res = EditName(context, "", defName);

        return String.valueOf(res);
    }


    private static final String sqlExercByTrain =
            " select distinct " +
                DB.TABLE_NAMES + "." + DB.COLUMN_NAMES_NAME + ", " +
                DB.TABLE_NAMES + "." + DB.COLUMN_NAMES_ID + ", " +
                DB.TABLE_NAMES + "." + DB.COLUMN_NAMES_ISCOUNT + ", " +
                DB.TABLE_NAMES + "." + DB.COLUMN_NAMES_ISTIME + ", " +
                DB.TABLE_NAMES + "." + DB.COLUMN_NAMES_ISWEIGHT +
            " from " +
                DB.TABLE_NAMES + " inner join " + DB.TABLE_VALUES
                    + " on " +  DB.TABLE_NAMES + "." +  DB.COLUMN_NAMES_ID + " = " +  DB.TABLE_VALUES + "." +  DB.COLUMN_VALUES_EXID +
            " where " +
                DB.TABLE_VALUES + "." + DB.COLUMN_VALUES_TRID  + " = ? " +
            " order by " + DB.TABLE_NAMES + "." + DB.COLUMN_NAMES_ID + " DESC "
            ;

    static public Cursor GetExercisesByTrain(Context context, String trainId) {
        SQLiteDatabase db = DB.getDBR(context);
        return db.rawQuery(sqlExercByTrain, new String[]{trainId});
    }

}
