package rkupchinskii.scounter;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class ModelStatistic {

    public static final String COLUMN_DATE = "coldate";
    public static final String COLUMN_VALUE = "colval";

    static public Cursor getReport(Context context, int id, int agr, int cwt) {
        String colDate = "DATE(" + DB.COLUMN_VALUES_ONDATE + ") as " + COLUMN_DATE;
        String colValue = null;
        String group = "DATE(" + DB.COLUMN_VALUES_ONDATE + ")";

        if (cwt == Helpers.PARM_CWT_CNT)
            colValue = DB.COLUMN_VALUES_CNT_REAL;
        else if (cwt == Helpers.PARM_CWT_TIM)
            colValue = DB.COLUMN_VALUES_TIM_REAL;
        else if (cwt == Helpers.PARM_CWT_WGT)
            colValue = DB.COLUMN_VALUES_WGT_REAL;
        else if (cwt == Helpers.PARM_CWT_CNTWGT)
            colValue = DB.COLUMN_VALUES_WGT_REAL +  " * " + DB.COLUMN_VALUES_CNT_REAL;


        String[] columns = null;

        if (agr == Helpers.PARM_AGR_ALL) {
            columns = new String[]{colDate, colValue + " as " + COLUMN_VALUE};
            group = null;
        } else if (agr == Helpers.PARM_AGR_MAX) {
            columns = new String[]{colDate, "MAX(" + colValue + ")" + " as " + COLUMN_VALUE};
        } else if (agr == Helpers.PARM_AGR_MIN) {
            columns = new String[]{colDate, "MIN(" + colValue + ")" + " as " + COLUMN_VALUE};
        } else if (agr == Helpers.PARM_AGR_SUM) {
            columns = new String[]{colDate, "SUM(" + colValue + ")" + " as " + COLUMN_VALUE};
        }
        SQLiteDatabase db = DB.getDBR(context);
        return db.query(DB.TABLE_VALUES, columns
                , DB.COLUMN_VALUES_EXID + " = ? "
                , new String[]{String.valueOf(id)}, group, null, DB.COLUMN_NAMES_ID);


    }
}
