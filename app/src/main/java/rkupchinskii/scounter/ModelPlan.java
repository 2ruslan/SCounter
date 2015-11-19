package rkupchinskii.scounter;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class ModelPlan
{
    private static final String[] SelectNameColumns = new String[]{DB.COLUMN_NAMES_ID
            , DB.COLUMN_NAMES_NAME
            , DB.COLUMN_NAMES_ONDATE
    };
    private static final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

    static public Cursor GetName(Context context) {
        SQLiteDatabase db = DB.getDBR(context);
        return db.query(DB.TABLE_NAMES, SelectNameColumns
                , DB.COLUMN_NAMES_TYPE + " = ? "
                , new String[]{String.valueOf(DB.COLUMN_NAMES_TYPE_PLAN)}, null, null, DB.COLUMN_NAMES_ID + " DESC");
    }

    static public long EditName(Context context, String id, String name) {
        SQLiteDatabase db = DB.getDBW(context);

        boolean isNew = id.equals("");

        ContentValues values = new ContentValues();
        values.put(DB.COLUMN_NAMES_NAME, name);

        if (isNew)
            values.put(DB.COLUMN_NAMES_TYPE, DB.COLUMN_NAMES_TYPE_PLAN);

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
// group_concat(X,Y)
    private static final String sqlExercByTrain =
            " select " +
                    DB.TABLE_NAMES + "." + DB.COLUMN_NAMES_NAME + ", " +
                    DB.TABLE_NAMES + "." + DB.COLUMN_NAMES_ID + ", " +
                    DB.TABLE_NAMES + "." + DB.COLUMN_NAMES_ISCOUNT + ", " +
                    DB.TABLE_NAMES + "." + DB.COLUMN_NAMES_ISTIME + ", " +
                    DB.TABLE_NAMES + "." + DB.COLUMN_NAMES_ISWEIGHT + ", " +
                    " group_concat( " +
                    Helpers.ExDetailsP +
                    "    , '; ') as " + DB.H_VALUES_GRP +
            " from " +
                    DB.TABLE_NAMES + " inner join " + DB.TABLE_VALUES
                    + " on " +  DB.TABLE_NAMES + "." +  DB.COLUMN_NAMES_ID + " = " +  DB.TABLE_VALUES + "." +  DB.COLUMN_VALUES_EXID +
            " where " +
                    DB.TABLE_VALUES + "." + DB.COLUMN_VALUES_TRID  + " = ? " +
            " group by " +
                    DB.TABLE_NAMES + "." + DB.COLUMN_NAMES_NAME + ", " +
                    DB.TABLE_NAMES + "." + DB.COLUMN_NAMES_ID + ", " +
                    DB.TABLE_NAMES + "." + DB.COLUMN_NAMES_ISCOUNT + ", " +
                    DB.TABLE_NAMES + "." + DB.COLUMN_NAMES_ISTIME + ", " +
                    DB.TABLE_NAMES + "." + DB.COLUMN_NAMES_ISWEIGHT +
            " order by " + DB.TABLE_NAMES + "." + DB.COLUMN_NAMES_ID + " DESC "
            ;

    static public Cursor GetExercisesByPlan(Context context, String trainId) {
        SQLiteDatabase db = DB.getDBR(context);
        return db.rawQuery(sqlExercByTrain, new String[]{trainId});
    }


    private static final String[] SelectPlanValues = new String[]{DB.COLUMN_VALUES_ID
            , DB.COLUMN_VALUES_EXID
            , DB.COLUMN_VALUES_CNT_PLAN
            , DB.COLUMN_VALUES_TIM_PLAN
            , DB.COLUMN_VALUES_WGT_PLAN
            , DB.COLUMN_VALUES_ORDER
            , Helpers.ExDetailsP + " as " + DB.H_VALUES_GRP
    };
    static public Cursor GetPlanValues(Context context, String nameID, String planId) {
        SQLiteDatabase db = DB.getDBR(context);


        return db.query(DB.TABLE_VALUES, SelectPlanValues
                ,  DB.COLUMN_VALUES_EXID + " = ? AND " + DB.COLUMN_VALUES_TRID + " = ? "
                , new String[]{nameID, planId}
                , null, null, DB.COLUMN_VALUES_ORDER );
    }

    static public long EditValue(Context context, String valId, String nameId, String planId
            , String cp, String wp, String tp
    ) {
        boolean isNew = valId.equals("");

        ContentValues values = new ContentValues();
        values.put(DB.COLUMN_VALUES_CNT_PLAN, cp.equals("") ? null : cp );
        values.put(DB.COLUMN_VALUES_WGT_PLAN, wp.equals("") ? null : wp);
        values.put(DB.COLUMN_VALUES_TIM_PLAN, tp.equals("") ? null : tp);

        if (isNew) {
            values.put(DB.COLUMN_VALUES_EXID, nameId);
            values.put(DB.COLUMN_VALUES_ONDATE, DB.getDataStr(Calendar.getInstance().getTime()));
            values.put(DB.COLUMN_VALUES_TRID, planId);
            //
            Cursor cursor = null;
            long res = -1;
            SQLiteDatabase db = DB.getDBR(context);
            cursor = db.rawQuery( "select ifnull( max(" + DB.COLUMN_VALUES_ORDER + "), 0) + 1 from "
                    + DB.TABLE_VALUES +" where " + DB.COLUMN_VALUES_TRID + " = ? AND " + DB.COLUMN_VALUES_EXID + " = ? "
                    ,new String[]{planId, nameId} );

            if (cursor.getCount() > 0) {
                cursor.moveToFirst();
                res = cursor.getLong(0);
            }

            values.put(DB.COLUMN_VALUES_ORDER, res);
        }

        SQLiteDatabase db = DB.getDBW(context);
        long row = isNew ?
                db.insert(DB.TABLE_VALUES, null, values) :
                db.update(DB.TABLE_VALUES, values, DB.COLUMN_VALUES_ID + " = ? ", new String[]{valId});
        db.close();

        return row;
    }

}
