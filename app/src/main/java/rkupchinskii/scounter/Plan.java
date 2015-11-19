package rkupchinskii.scounter;

import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;


public class Plan extends ActionBarActivity {
    private final String[] columns = new String[]{
            DB.COLUMN_NAMES_NAME,
            DB.H_VALUES_GRP

    };

    private final int[] to = new int[]{
            R.id.vlpe_name,
            R.id.vlpe_exrc
    };

    String idPlan;
    String nameExe;

    ListView listView;
    Cursor cursor;
    private SimpleCursorAdapter dataAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_plan);

        Bundle b = getIntent().getExtras();

        idPlan = b.getString(Helpers.PARM_NAME_ID);
        nameExe = b.getString(Helpers.PARM_NAME_NAME);

        setTitle(nameExe);

        listView = (ListView) findViewById(R.id.vpeList);

        initListView();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }


    private void refreshListView() {
        cursor = ModelPlan.GetExercisesByPlan(this, idPlan);
        dataAdapter.changeCursor(cursor);
    }

    private void initListView() {

        cursor = ModelPlan.GetExercisesByPlan(this, idPlan);

        dataAdapter = new SimpleCursorAdapter(
                this, R.layout.list_plan_exerc,
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
                        cursor.getString(cursor.getColumnIndexOrThrow(DB.COLUMN_NAMES_ID))
                        , cursor.getString(cursor.getColumnIndexOrThrow(DB.COLUMN_NAMES_NAME))
                        , false
                        , cursor.getInt(cursor.getColumnIndexOrThrow(DB.COLUMN_NAMES_ISCOUNT)) == 1
                        , cursor.getInt(cursor.getColumnIndexOrThrow(DB.COLUMN_NAMES_ISTIME)) == 1
                        , cursor.getInt(cursor.getColumnIndexOrThrow(DB.COLUMN_NAMES_ISWEIGHT)) == 1
                );
            }
        });
    }

    public void OpenEditor(String id, String name, boolean isStartAdd,
                           boolean isCnt, boolean isTim, boolean isWgt
    ) {

        Intent intent = new Intent(Plan.this, PlanExercise.class);

        intent.putExtra(Helpers.PARM_NAME_ID, id);
        intent.putExtra(Helpers.PARM_NAME_NAME, name);
        intent.putExtra(Helpers.PARM_PLAN_ID, idPlan);
        intent.putExtra(Helpers.PARM_MODE, isStartAdd ? Helpers.PARM_MODE_ADD : Helpers.PARM_MODE_NORMAL );

        intent.putExtra(Helpers.PARM_NAME_ISCNT, isCnt);
        intent.putExtra(Helpers.PARM_NAME_ISTIM, isTim);
        intent.putExtra(Helpers.PARM_NAME_ISWGT, isWgt);

        startActivityForResult(intent, Helpers.PARAM_VALUES);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == Helpers.PARAM_EXERC) {
            if (resultCode == RESULT_OK) {
                Bundle b = data.getExtras();

                OpenEditor(b.getString(Helpers.PARM_NAME_ID), b.getString(Helpers.PARM_NAME_NAME), true,
                        b.getBoolean(Helpers.PARM_NAME_ISCNT), b.getBoolean(Helpers.PARM_NAME_ISTIM), b.getBoolean(Helpers.PARM_NAME_ISWGT));
            }
        }
        else
            refreshListView();
    }

    public void OnClickAdd(View view) {
        Intent intent = new Intent(Plan.this, ExerciseSelect.class);
        intent.putExtra(Helpers.PARM_MODE,  Helpers.PARM_MODE_GET);
        startActivityForResult(intent, Helpers.PARAM_EXERC);
    }

    public void close() {
        setResult(RESULT_OK);
        finish();
    }
}
