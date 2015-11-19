package rkupchinskii.scounter;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;


public class Exercise extends ActionBarActivity {

    private final String[] columns = new String[]{
            DB.H_VALUES_GRP,
            DB.COLUMN_VALUES_ONDATE
    };

    private final int[] to = new int[]{
            R.id.vle_exerc,
            R.id.vle_ondt
    };


    String idExe;
    String nameExe;
    String idTrain;
    boolean isCnt;
    boolean isTim;
    boolean isWgt;
    ListView listView;
    Cursor cursor;
    private SimpleCursorAdapter dataAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exercise);

        Bundle b = getIntent().getExtras();

        idExe = b.getString(Helpers.PARM_NAME_ID);
        nameExe = b.getString(Helpers.PARM_NAME_NAME);
        idTrain = b.getString(Helpers.PARM_TRAIN_ID, "");

        isCnt = b.getBoolean(Helpers.PARM_NAME_ISCNT);
        isTim = b.getBoolean(Helpers.PARM_NAME_ISTIM);
        isWgt = b.getBoolean(Helpers.PARM_NAME_ISWGT);

        setTitle(nameExe);

        listView = (ListView) findViewById(R.id.veList);

        initListView();

        if (b.getInt(Helpers.PARM_MODE) == Helpers.PARM_MODE_ADD)
            OnClickAdd(null);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == android.R.id.home)
        {
            close();
            return true;
        }
        else
            return super.onOptionsItemSelected(item);
    }

    private void refreshListView() {
        cursor = ModelExercise.GetValues(this, idExe, idTrain);
        dataAdapter.changeCursor(cursor);
    }

    private void initListView() {

        cursor = ModelExercise.GetValues(this, idExe, idTrain);

        dataAdapter = new SimpleCursorAdapter(
                this, R.layout.list_exercise_val,
                cursor,
                columns,
                to,
                0);

        listView.setAdapter(dataAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> listView, View view,
                                    int position, long id) {
                Cursor cursor = (Cursor) listView.getItemAtPosition(position);
                OpenEditor(
                        cursor.getString(cursor.getColumnIndexOrThrow(DB.COLUMN_VALUES_EXID))
                        , cursor.getString(cursor.getColumnIndexOrThrow(DB.COLUMN_VALUES_ID))
                        , cursor.getString(cursor.getColumnIndexOrThrow(DB.COLUMN_VALUES_CNT_PLAN))
                        , cursor.getString(cursor.getColumnIndexOrThrow(DB.COLUMN_VALUES_CNT_REAL))
                        , cursor.getString(cursor.getColumnIndexOrThrow(DB.COLUMN_VALUES_WGT_PLAN))
                        , cursor.getString(cursor.getColumnIndexOrThrow(DB.COLUMN_VALUES_WGT_REAL))
                        , cursor.getString(cursor.getColumnIndexOrThrow(DB.COLUMN_VALUES_TIM_PLAN))
                        , cursor.getString(cursor.getColumnIndexOrThrow(DB.COLUMN_VALUES_TIM_REAL))
                );
            }
        });
    }

    public void OpenEditor(String idE, String idV,
                           String cp, String cr,
                           String wp, String wr,
                           String tp, String tr
    ) {
        Intent intent = new Intent(Exercise.this, ExerciseDetails.class);

        intent.putExtra(Helpers.PARM_NAME_ID, idE);
        intent.putExtra(Helpers.PARM_VAL_ID, idV);
        intent.putExtra(Helpers.PARM_TRAIN_ID, idTrain);
        intent.putExtra(Helpers.PARM_CNT_REAL, cr);
        intent.putExtra(Helpers.PARM_CNT_PLAN, cp);
        intent.putExtra(Helpers.PARM_WGT_REAL, wr);
        intent.putExtra(Helpers.PARM_WGT_PLAN, wp);
        intent.putExtra(Helpers.PARM_TIM_REAL, tr);
        intent.putExtra(Helpers.PARM_TIM_PLAN, tp);

        intent.putExtra(Helpers.PARM_NAME_ISCNT, isCnt);
        intent.putExtra(Helpers.PARM_NAME_ISTIM, isTim);
        intent.putExtra(Helpers.PARM_NAME_ISWGT, isWgt);

        startActivityForResult(intent, 0);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK && requestCode == 0)
            refreshListView();
    }

    public void OnClickAdd(View view) {
        OpenEditor(idExe, "", "", "", "", "", "", "");
    }

    public void close() {
        setResult(RESULT_OK);
        finish();
    }

    public void OnClickStatistic(View view) {
        Intent intent = new Intent(Exercise.this, StatisticSelect.class);
        intent.putExtra(Helpers.PARAM_EXERC_ID, idExe);
        intent.putExtra(Helpers.PARM_MODE, Helpers.PARAM_EXERC );
        startActivityForResult(intent, 1);
    }
}
