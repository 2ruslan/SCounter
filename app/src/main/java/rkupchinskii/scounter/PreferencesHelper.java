package rkupchinskii.scounter;

import android.content.SharedPreferences;

public class PreferencesHelper {
    public static final String APP_PREFERENCES = "pref";
    public static final String APP_PREFERENCES_WORK_DAYS_OFFSET = "counter123";
    public static final String APP_PREFERENCES_NO_SHOW_PROP_O = "counter223";
    public static final String APP_PREFERENCES_NO_SHOW_PROP_P = "counter4";
    public static final String APP_PREFERENCES_FIRST_RUN = "counter3";
    public static final String APP_PREFERENCES_TIMER_1 = "time1";
    public static final String APP_PREFERENCES_TIMER_2 = "time2";


    private static SharedPreferences mSettings;

    public static void init(SharedPreferences aSettings) {
        mSettings = aSettings;
    }

    //region сдвиг дней тренировок
    public static void SetTimer_1(Integer val)    {
        SharedPreferences.Editor editor = mSettings.edit();
        if(val != null)
            editor.putInt(APP_PREFERENCES_TIMER_1, val);
        else
            editor.putInt(APP_PREFERENCES_TIMER_1, 0);
        editor.apply();
    }

    public static void SetTimer_2(Integer val)    {
        SharedPreferences.Editor editor = mSettings.edit();
        if(val != null)
            editor.putInt(APP_PREFERENCES_TIMER_2, val);
        else
            editor.putInt(APP_PREFERENCES_TIMER_2, 0);

        editor.apply();
    }

    public static Integer GetTimer_1()    {
        Integer res;
        res = mSettings.getInt(APP_PREFERENCES_TIMER_1, 0);
        if(res <=0)
            res = null;
        return res;
    }
    public static Integer GetTimer_2()    {
        Integer res;
        res = mSettings.getInt(APP_PREFERENCES_TIMER_2, 0);
        if(res <=0)
            res = null;
        return res;
    }
    //endregion сдвиг дней тренировок

    //region не показывать напоминая о оценке программы
    public static void SetNoShowPropO(boolean val) {
        SharedPreferences.Editor editor = mSettings.edit();
        editor.putBoolean(APP_PREFERENCES_NO_SHOW_PROP_O, val);
        editor.apply();
    }

    public static boolean GetNoShowPropO() {
        return mSettings.getBoolean(APP_PREFERENCES_NO_SHOW_PROP_O, false);
    }
    //endregion не показывать напоминая о оценке программы

    //region не показывать напоминая о покупке программы
    public static void SetNoShowPropP(boolean val) {
        SharedPreferences.Editor editor = mSettings.edit();
        editor.putBoolean(APP_PREFERENCES_NO_SHOW_PROP_P, val);
        editor.apply();
    }

    public static boolean GetNoShowPropP() {
        return mSettings.getBoolean(APP_PREFERENCES_NO_SHOW_PROP_P, false);
    }
    //endregion не показывать напоминая о покупке программы

    //region первый запуск
    public static boolean GetFirstRun() {
        boolean res = mSettings.getBoolean(APP_PREFERENCES_FIRST_RUN, true);

        if (res) {
            SharedPreferences.Editor editor = mSettings.edit();
            editor.putBoolean(APP_PREFERENCES_FIRST_RUN, false);
            editor.apply();
        }
        return res;
    }
    //endregion первый запуск


}
