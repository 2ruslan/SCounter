package rkupchinskii.scounter;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Environment;
import android.support.v4.content.ContextCompat;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DB extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 5;

    public static final int COLUMN_NAMES_TYPE_EXER = 0;
    public static final int COLUMN_NAMES_TYPE_MEAS = 1;
    public static final int COLUMN_NAMES_TYPE_TRAIN = 2;
    public static final int COLUMN_NAMES_TYPE_PLAN = 3;

    // oth
    public static final String H_VALUES_GRP = "grpval";
    // names
    public static final String TABLE_NAMES = "name";
    public static final String COLUMN_NAMES_ID = "_id";
    public static final String COLUMN_NAMES_PID = "pid";
    public static final String COLUMN_NAMES_NAME = "name";
    public static final String COLUMN_NAMES_TYPE = "type";
    public static final String COLUMN_NAMES_ISCOUNT = "iscount";
    public static final String COLUMN_NAMES_ISTIME = "istime";
    public static final String COLUMN_NAMES_ISWEIGHT = "isweight";
    public static final String COLUMN_NAMES_ISSET = "isset";
    public static final String COLUMN_NAMES_ONDATE = "ondt";
    public static final String COLUMN_NAMES_DAYS = "days";

    private static final String SQL_CREATE_NAMES = "CREATE TABLE " + TABLE_NAMES + " ("
            + COLUMN_NAMES_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + COLUMN_NAMES_PID + " INTEGER, "
            + COLUMN_NAMES_NAME + " VARCHAR, "
            + COLUMN_NAMES_TYPE + " INTEGER, "
            + COLUMN_NAMES_ISCOUNT + " INTEGER, "
            + COLUMN_NAMES_ISTIME + " INTEGER, "
            + COLUMN_NAMES_ISWEIGHT + " INTEGER, "
            + COLUMN_NAMES_ONDATE + " DATETIME, "
            + COLUMN_NAMES_DAYS + " VARCHAR, "
            + COLUMN_NAMES_ISSET + " INTEGER "
            + ");";
    // values
    public static final String TABLE_VALUES = "value";
    public static final String COLUMN_VALUES_ID = "_id";
    public static final String COLUMN_VALUES_ONDATE = "ondt";
    public static final String COLUMN_VALUES_ORDER = "ord";
    public static final String COLUMN_VALUES_EXID = "ex_id";
    public static final String COLUMN_VALUES_TRID = "tr_id";
    public static final String COLUMN_VALUES_CNT_PLAN = "cnt_p";
    public static final String COLUMN_VALUES_WGT_PLAN = "wgt_p";
    public static final String COLUMN_VALUES_TIM_PLAN = "tim_p";
    public static final String COLUMN_VALUES_CNT_REAL = "cnt_r";
    public static final String COLUMN_VALUES_WGT_REAL = "wgt_r";
    public static final String COLUMN_VALUES_TIM_REAL = "tim_r";
    public static final String COLUMN_VALUES_RESULT = "result";
    public static final String COLUMN_VALUES_SET_REAL = "set_r";
    public static final String COLUMN_VALUES_SET_PLAN = "set_r";

    private static final String SQL_CREATE_VALUES = "CREATE TABLE " + TABLE_VALUES + " ("
            + COLUMN_VALUES_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + COLUMN_VALUES_ONDATE + " DATETIME, "
            + COLUMN_VALUES_ORDER + " INTEGER, "
            + COLUMN_VALUES_EXID + " INTEGER REFERENCES " + TABLE_NAMES + " ( " + COLUMN_NAMES_ID + " ), "
            + COLUMN_VALUES_TRID + " INTEGER REFERENCES " + TABLE_NAMES + " ( " + COLUMN_NAMES_ID + " ), "
            + COLUMN_VALUES_CNT_PLAN + " NUMERIC, "
            + COLUMN_VALUES_WGT_PLAN + " NUMERIC, "
            + COLUMN_VALUES_TIM_PLAN + " NUMERIC, "
            + COLUMN_VALUES_CNT_REAL + " NUMERIC, "
            + COLUMN_VALUES_WGT_REAL + " NUMERIC, "
            + COLUMN_VALUES_TIM_REAL + " NUMERIC, "
            + COLUMN_VALUES_RESULT + " NUMERIC, "
            + COLUMN_VALUES_SET_REAL + " NUMERIC, "
            + COLUMN_VALUES_SET_PLAN + " NUMERIC "
            + ");";
    private static final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private static final String DATABASE_NAME = "training.db";

    private static DB _db = null;

    public static void copyFile(File src, File dst) throws IOException
    {
        FileChannel inChannel = null;
        FileChannel outChannel = null;
        try
        {
            inChannel = new FileInputStream(src).getChannel();
            outChannel = new FileOutputStream(dst).getChannel();
            inChannel.transferTo(0, inChannel.size(), outChannel);
        }
        finally
        {
            if (inChannel != null)
                inChannel.close();
            if (outChannel != null)
                outChannel.close();
        }
    }

    private static String prepareBDPath(Context context)
    {
        String result = DATABASE_NAME;
        File[] paths = ContextCompat.getExternalCacheDirs(context);
        try {
            if (paths.length > 0) {
                File exfile = new File(paths[0].getAbsolutePath(), DATABASE_NAME);
                File local = context.getDatabasePath(DATABASE_NAME);
                if (local.exists()) {
                    if (!exfile.exists() || (exfile.lastModified() < local.lastModified()))
                        try {
                            copyFile(local, exfile);
                        } catch (IOException ex) {
                        }
                }
                if (exfile.exists())
                    result = exfile.getAbsolutePath();
            }
        }
        catch (Exception ex) {}

        return result;
    }

    public DB(Context context) {
        super(context, prepareBDPath(context), null, DATABASE_VERSION);
    }

    static DB getDB(Context context) {
        if (_db == null)
            _db = new DB(context);

        return _db;
    }

    public static SQLiteDatabase getDBR(Context context) {
        return getDB(context).getReadableDatabase();
    }

    public static SQLiteDatabase getDBW(Context context) {
        return getDB(context).getWritableDatabase();
    }

    public static String getDataStr(Date dt) {
        return sdf.format(dt);
    }

    public static Date getData(String dt) {
        try {
            return sdf.parse(dt);
        } catch (Exception ex) {
            return null;
        }
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_NAMES);
        db.execSQL(SQL_CREATE_VALUES);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (newVersion > oldVersion)
        {
            if (oldVersion < 2)
            {
                db.execSQL("ALTER TABLE " + TABLE_NAMES + " ADD COLUMN " + COLUMN_NAMES_ONDATE + " DATETIME");
            }
            if (oldVersion < 3)
            {
                db.execSQL("UPDATE " + TABLE_VALUES + " SET " +
                            COLUMN_VALUES_CNT_PLAN + " = (CASE WHEN " + COLUMN_VALUES_CNT_PLAN + " = '' THEN NULL ELSE " + COLUMN_VALUES_CNT_PLAN + " END), " +
                            COLUMN_VALUES_WGT_PLAN + " = (CASE WHEN " + COLUMN_VALUES_WGT_PLAN + " = '' THEN NULL ELSE " + COLUMN_VALUES_WGT_PLAN + " END), " +
                            COLUMN_VALUES_TIM_PLAN + " = (CASE WHEN " + COLUMN_VALUES_TIM_PLAN + " = '' THEN NULL ELSE " + COLUMN_VALUES_TIM_PLAN + " END), " +
                            COLUMN_VALUES_CNT_REAL + " = (CASE WHEN " + COLUMN_VALUES_CNT_REAL + " = '' THEN NULL ELSE " + COLUMN_VALUES_CNT_REAL + " END), " +
                            COLUMN_VALUES_WGT_REAL + " = (CASE WHEN " + COLUMN_VALUES_WGT_REAL + " = '' THEN NULL ELSE " + COLUMN_VALUES_WGT_REAL + " END), " +
                            COLUMN_VALUES_TIM_REAL + " = (CASE WHEN " + COLUMN_VALUES_TIM_REAL + " = '' THEN NULL ELSE " + COLUMN_VALUES_TIM_REAL + " END)"
                );
            }
            if (oldVersion < 4)
            {
                db.execSQL("ALTER TABLE " + TABLE_NAMES + " ADD COLUMN " + COLUMN_NAMES_DAYS + " VARCHAR");
            }
            if (oldVersion < 5)
            {
                db.execSQL("ALTER TABLE " + TABLE_NAMES + " ADD COLUMN " + COLUMN_NAMES_ISSET + " INTEGER");
                db.execSQL("ALTER TABLE " + TABLE_VALUES + " ADD COLUMN " + COLUMN_VALUES_SET_REAL + " NUMERIC");
                db.execSQL("ALTER TABLE " + TABLE_VALUES + " ADD COLUMN " + COLUMN_VALUES_SET_PLAN + " NUMERIC");
            }
        }
    }
}
