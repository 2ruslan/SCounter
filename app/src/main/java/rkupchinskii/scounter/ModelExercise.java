package rkupchinskii.scounter;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.Calendar;

public class ModelExercise {
    private static final String[] SelectNameColumns = new String[]{DB.COLUMN_NAMES_ID
            , DB.COLUMN_NAMES_NAME
            , DB.COLUMN_NAMES_ISWEIGHT
            , DB.COLUMN_NAMES_ISTIME
            , DB.COLUMN_NAMES_ISCOUNT
            , DB.COLUMN_NAMES_ISSET
    };

    private static final String[] SelectValues = new String[]{DB.COLUMN_VALUES_ID
            , DB.COLUMN_VALUES_EXID
            , DB.COLUMN_VALUES_ONDATE
            , DB.COLUMN_VALUES_CNT_PLAN
            , DB.COLUMN_VALUES_CNT_REAL
            , DB.COLUMN_VALUES_TIM_PLAN
            , DB.COLUMN_VALUES_TIM_REAL
            , DB.COLUMN_VALUES_WGT_PLAN
            , DB.COLUMN_VALUES_WGT_REAL
            , DB.COLUMN_VALUES_SET_PLAN
            , DB.COLUMN_VALUES_SET_REAL
            , Helpers.ExDetailsR + " as " + DB.H_VALUES_GRP
    };



    static public Cursor GetName(Context context) {
        SQLiteDatabase db = DB.getDBR(context);
        return db.query(DB.TABLE_NAMES, SelectNameColumns
                , DB.COLUMN_NAMES_TYPE + " = ? "
                , new String[]{String.valueOf(DB.COLUMN_NAMES_TYPE_EXER)}, null, null, DB.COLUMN_NAMES_NAME);
    }

    static public long EditName(Context context, String id, String name
            , boolean isCnt, boolean isWgt, boolean isTim, boolean isSet) {
        SQLiteDatabase db = DB.getDBW(context);

        boolean isNew = id.equals("");

        ContentValues values = new ContentValues();
        values.put(DB.COLUMN_NAMES_NAME, name);
        values.put(DB.COLUMN_NAMES_ISCOUNT, isCnt);
        values.put(DB.COLUMN_NAMES_ISTIME, isTim);
        values.put(DB.COLUMN_NAMES_ISWEIGHT, isWgt);
        values.put(DB.COLUMN_NAMES_ISSET, isSet);

        if (isNew)
            values.put(DB.COLUMN_NAMES_TYPE, DB.COLUMN_NAMES_TYPE_EXER);

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

    static public Cursor GetValues(Context context, String nameID, String trainId) {
        SQLiteDatabase db = DB.getDBR(context);

        boolean noTrain = trainId.equals("");
        String where = noTrain ? DB.COLUMN_VALUES_EXID + " = ? AND " + DB.COLUMN_VALUES_TRID
                       + " not in ( select "+ DB.COLUMN_NAMES_ID +" from "+ DB.TABLE_NAMES
                       + " where " + DB.COLUMN_NAMES_TYPE + " = " + DB.COLUMN_NAMES_TYPE_PLAN +" ) "
                : DB.COLUMN_VALUES_EXID + " = ? AND " + DB.COLUMN_VALUES_TRID + " = ? ";
        String[] wparm = noTrain ? new String[]{nameID} : new String[]{nameID, trainId};

        return db.query(DB.TABLE_VALUES, SelectValues
                , where
                , wparm
                , null, null, DB.COLUMN_VALUES_ONDATE + (noTrain ? " DESC" : ""));
    }

    static public long EditValue(Context context, String valId, String nameId, String trainId
            , String cp, String cr
            , String wp, String wr
            , String tp, String tr
            , String sp, String sr
    ) {
        boolean isNew = valId.equals("");

        ContentValues values = new ContentValues();
        values.put(DB.COLUMN_VALUES_CNT_REAL, cr.equals("") ? null : cr );
        values.put(DB.COLUMN_VALUES_CNT_PLAN, cp.equals("") ? null : cp );
        values.put(DB.COLUMN_VALUES_WGT_REAL, wr.equals("") ? null : wr);
        values.put(DB.COLUMN_VALUES_WGT_PLAN, wp.equals("") ? null : wp);
        values.put(DB.COLUMN_VALUES_TIM_REAL, tr.equals("") ? null : tr);
        values.put(DB.COLUMN_VALUES_TIM_PLAN, tp.equals("") ? null : tr);
        values.put(DB.COLUMN_VALUES_SET_PLAN, sp.equals("") ? null : sp);
        values.put(DB.COLUMN_VALUES_SET_REAL, sr.equals("") ? null : sr);

        if (isNew) {
            values.put(DB.COLUMN_VALUES_EXID, nameId);
            values.put(DB.COLUMN_VALUES_ONDATE, DB.getDataStr(Calendar.getInstance().getTime()));
            values.put(DB.COLUMN_VALUES_TRID, (trainId.equals("") ?  ModelTraining.getDefaulttrainingId(context): trainId) );
        }

        SQLiteDatabase db = DB.getDBW(context);
        long row = isNew ?
                db.insert(DB.TABLE_VALUES, null, values) :
                db.update(DB.TABLE_VALUES, values, DB.COLUMN_VALUES_ID + " = ? ", new String[]{valId});
        db.close();

        return row;
    }

    static public void DeleteValue(Context context, String id) {
        SQLiteDatabase db = DB.getDBW(context);
        db.delete(DB.TABLE_VALUES, DB.COLUMN_VALUES_ID + " = ? ", new String[]{id});
        db.close();
    }
}
