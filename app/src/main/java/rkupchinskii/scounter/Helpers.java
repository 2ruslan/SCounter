package rkupchinskii.scounter;

import android.content.Context;
import android.content.SharedPreferences;

public class Helpers {
    public static final String PARM_VAL_ID = "1";
    public static final String PARM_NAME_ID = "2";
    public static final String PARM_NAME_NAME = "3";

    public static final String PARM_NAME_ISCNT = "4";
    public static final String PARM_NAME_ISWGT = "5";
    public static final String PARM_NAME_ISTIM = "6";

    public static final String PARM_TRAIN_ID = "7";
    public static final String PARM_CNT_REAL = "8";
    public static final String PARM_CNT_PLAN = "9";
    public static final String PARM_WGT_PLAN = "10";
    public static final String PARM_WGT_REAL = "11";
    public static final String PARM_TIM_PLAN = "12";
    public static final String PARM_TIM_REAL = "13";

    public static final String PARM_AGR = "14";
    public static final String PARM_CWT = "15";

    public static final int PARM_AGR_ALL = 16;
    public static final int PARM_AGR_SUM = 17;
    public static final int PARM_AGR_MAX = 18;
    public static final int PARM_AGR_MIN = 19;

    public static final int PARM_CWT_CNT = 20;
    public static final int PARM_CWT_WGT = 21;
    public static final int PARM_CWT_TIM = 22;
    public static final int PARM_CWT_CNTWGT = 23;

    public static final String PARM_PLAN_ID = "24";

    public static final String PARM_MODE = "25";
    public static final int PARM_MODE_GET = 26;
    public static final int PARM_MODE_VIEW = 27;
    public static final int PARM_MODE_ADD = 28;
    public static final int PARM_MODE_NORMAL = 29;
    public static  final int PARAM_EXERC = 30;
    public static  final int PARAM_VALUES = 31;
    public static  final int PARAM_MEAS = 33;

    public static final String PARAM_EXERC_ID = "32";

    public static final  String ExDetailsP = DB.TABLE_VALUES + "." + DB.COLUMN_VALUES_ORDER + " || ') ' || " +
            " (case when " + DB.TABLE_VALUES + "." + DB.COLUMN_VALUES_CNT_PLAN + " is not null and " + DB.TABLE_VALUES + "." +  DB.COLUMN_VALUES_WGT_PLAN + " is not null then " +
            DB.TABLE_VALUES + "." + DB.COLUMN_VALUES_CNT_PLAN + " || ' x ' || " + DB.TABLE_VALUES + "." +  DB.COLUMN_VALUES_WGT_PLAN +
            " when " + DB.TABLE_VALUES + "." + DB.COLUMN_VALUES_CNT_PLAN + " is not null then " + DB.TABLE_VALUES + "." +  DB.COLUMN_VALUES_CNT_PLAN +
            " when " + DB.TABLE_VALUES + "." + DB.COLUMN_VALUES_WGT_PLAN + " is not null then " + DB.TABLE_VALUES + "." + DB.COLUMN_VALUES_WGT_PLAN +
            " else '' " +
            " end ) " +
            "    || (case when " + DB.TABLE_VALUES + "." + DB.COLUMN_VALUES_TIM_PLAN + " is not null then " +
            " case when " + DB.TABLE_VALUES + "." + DB.COLUMN_VALUES_CNT_PLAN + " is not null or " + DB.TABLE_VALUES + "." + DB.COLUMN_VALUES_WGT_PLAN + " is not null then ' - ' else '' end " +
            " || " + DB.TABLE_VALUES + "." + DB.COLUMN_VALUES_TIM_PLAN + " else '' end) ";

    public static final  String ExDetailsR =
            " (case when " + DB.TABLE_VALUES + "." + DB.COLUMN_VALUES_CNT_REAL + " is not null and " + DB.TABLE_VALUES + "." +  DB.COLUMN_VALUES_WGT_REAL + " is not null then " +
            DB.TABLE_VALUES + "." + DB.COLUMN_VALUES_CNT_REAL + " || ' x ' || " + DB.TABLE_VALUES + "." +  DB.COLUMN_VALUES_WGT_REAL +
            " when " + DB.TABLE_VALUES + "." + DB.COLUMN_VALUES_CNT_REAL + " is not null then " + DB.TABLE_VALUES + "." + DB.COLUMN_VALUES_CNT_REAL +
            " when " + DB.TABLE_VALUES + "." + DB.COLUMN_VALUES_WGT_REAL + " is not null then " + DB.TABLE_VALUES + "." + DB.COLUMN_VALUES_WGT_REAL +
            " else ''" +
            " end ) " +
            "    || (case when " + DB.TABLE_VALUES + "." + DB.COLUMN_VALUES_TIM_REAL + " is not null then " +
            " case when " + DB.TABLE_VALUES + "." + DB.COLUMN_VALUES_CNT_REAL + " is not null or " + DB.TABLE_VALUES + "." + DB.COLUMN_VALUES_WGT_REAL + " is not null then ' - ' else '' end " +
            " || " + DB.TABLE_VALUES + "." + DB.COLUMN_VALUES_TIM_REAL + " else '' end) ";


    public static int getNoButtonTitle(boolean addmode) {
        return addmode ? R.string.title_cancel : R.string.title_delete;
    }

    public static int getEditDlgTitle(boolean addmode) {
        return addmode ? R.string.title_new : R.string.title_edit;
    }


    public static final String SETTING_FILE = "appsettings";
    public static final String SETTING_HIDE_STTISTIC = "hide_stat";

    public static SharedPreferences Settings;



    public static void SettingsSet(String key, boolean val)
    {
        SharedPreferences.Editor editor = Settings.edit();
        editor.putBoolean(key, val);
        editor.apply();
    }

    public static void SettingsSet(String key, String val)
    {
        SharedPreferences.Editor editor = Settings.edit();
        editor.putString(key, val);
        editor.apply();
    }

    public static void SettingsSet(String key, int val)
    {
        SharedPreferences.Editor editor = Settings.edit();
        editor.putInt(key, val);
        editor.apply();
    }

    /*
    public static void ExeEdit(Context context,final String pid, String pname
            , boolean piscnt, boolean piswgt, boolean pistim) {
        final DB mDbHelper = new DB(context);
        View frg = View.inflate(context, R.layout.fragment_exerc_edit, null);
        final CheckBox cbIsCnt = (CheckBox) frg.findViewById(R.id.cbIsCnt);
        final CheckBox cbIsWgt = (CheckBox) frg.findViewById(R.id.cbIsWgt);
        final CheckBox cbIsTim = (CheckBox) frg.findViewById(R.id.cbIsTim);
        final EditText etName = (EditText) frg.findViewById(R.id.etName);

        etName.setText(pname);
        cbIsCnt.setChecked(piscnt);
        cbIsWgt.setChecked(piswgt);
        cbIsTim.setChecked(pistim);

        final boolean addmode = pid.equals("");

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        //builder.setTitle(R.string.title_edit);
        builder
                .setView(frg)
                .setCancelable(false)
                .setPositiveButton(R.string.title_save, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        if (addmode) {
                            mDbHelper.ExercAdd(etName.getText().toString(), cbIsCnt.isChecked()
                                    , cbIsWgt.isChecked(), cbIsTim.isChecked());
                        } else {
                            mDbHelper.ExercEdit(pid, etName.getText().toString(), cbIsCnt.isChecked()
                                    , cbIsWgt.isChecked(), cbIsTim.isChecked());
                        }
                    }
                })
                .setNegativeButton(addmode ? R.string.title_cancel :  R.string.title_delete, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        if (!addmode)
                            mDbHelper.ExercDelete(pid);
                    }
                }).show();
    }
*/

}
