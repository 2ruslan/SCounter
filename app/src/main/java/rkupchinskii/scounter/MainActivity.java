package rkupchinskii.scounter;

import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import java.io.File;
import java.util.Random;


public class MainActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        Helpers.Settings = getSharedPreferences(Helpers.SETTING_FILE, this.MODE_PRIVATE);
        PreferencesHelper.init(getSharedPreferences(PreferencesHelper.APP_PREFERENCES, Context.MODE_PRIVATE));
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        DB.getDB(this).close();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
       // getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    public void OnClickTrain(View view) {
        Intent intent = new Intent(MainActivity.this, TrainingSelect.class);
        startActivity(intent);
    }

    public void OnClickExerc(View view) {
        Intent intent = new Intent(MainActivity.this, ExerciseSelect.class);
        intent.putExtra(Helpers.PARM_MODE,  Helpers.PARM_MODE_VIEW);
        startActivityForResult(intent, 0);
    }

    public void OnClickMeas(View view) {
        Intent intent = new Intent(MainActivity.this, MeasurementSelect.class);
        startActivity(intent);
    }

    public void OnClickStatistic(View view) {
        Intent intent = new Intent(MainActivity.this, StatisticSelect.class);
        startActivity(intent);
    }
    public void OnClickPlan(View view) {
        Intent intent = new Intent(MainActivity.this, PlanSelect.class);
        startActivity(intent);
    }
    public void OnClickExit(View view) {

        if (!PreferencesHelper.GetNoShowPropO()) {
            Random rnd = new Random();
            if (rnd.nextInt(31) == 7) {
                Intent intent = new Intent(MainActivity.this, EndPrg.class);
                startActivity(intent);
            }
        }
        finish();
    }
}
