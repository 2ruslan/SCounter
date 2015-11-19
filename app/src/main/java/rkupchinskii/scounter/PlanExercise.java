package rkupchinskii.scounter;

import android.content.Intent;
import android.database.Cursor;
import android.media.Image;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;


public class PlanExercise extends ActionBarActivity {

    private final String[] columns = new String[]{
            DB.H_VALUES_GRP
    };

    private final int[] to = new int[]{
            R.id.vlpe_exerc
    };


    String idExe;
    String nameExe;
    String idPlan;
    boolean isCnt;
    boolean isTim;
    boolean isWgt;
    ListView listView;
    Cursor cursor;
    private SimpleCursorAdapter dataAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_plan_exercise);

        Bundle b = getIntent().getExtras();

        idExe = b.getString(Helpers.PARM_NAME_ID);
        nameExe = b.getString(Helpers.PARM_NAME_NAME);
        idPlan = b.getString(Helpers.PARM_PLAN_ID);

        isCnt = b.getBoolean(Helpers.PARM_NAME_ISCNT);
        isTim = b.getBoolean(Helpers.PARM_NAME_ISTIM);
        isWgt = b.getBoolean(Helpers.PARM_NAME_ISWGT);
        setTitle(nameExe);

        listView = (ListView) findViewById(R.id.vpeList);

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
            theEnd();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void refreshListView() {
        cursor = ModelPlan.GetPlanValues(this, idExe, idPlan);
        dataAdapter.changeCursor(cursor);
    }

    private void initListView() {

        cursor = ModelPlan.GetPlanValues(this, idExe, idPlan);

        dataAdapter = new SimpleCursorAdapter(
                this, R.layout.list_plan_exercise_val,
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
                        , cursor.getString(cursor.getColumnIndexOrThrow(DB.COLUMN_VALUES_WGT_PLAN))
                        , cursor.getString(cursor.getColumnIndexOrThrow(DB.COLUMN_VALUES_TIM_PLAN))
                );
            }
        });
    }

    public void OpenEditor(String idE, String idV,
                           String cp, String wp, String tp
    ) {
        Intent intent = new Intent(PlanExercise.this, PlanExerciseDetails.class);

        intent.putExtra(Helpers.PARM_NAME_ID, idE);
        intent.putExtra(Helpers.PARM_VAL_ID, idV);
        intent.putExtra(Helpers.PARM_PLAN_ID, idPlan);
        intent.putExtra(Helpers.PARM_CNT_PLAN, cp);
        intent.putExtra(Helpers.PARM_WGT_PLAN, wp);
        intent.putExtra(Helpers.PARM_TIM_PLAN, tp);

        intent.putExtra(Helpers.PARM_NAME_ISCNT, isCnt);
        intent.putExtra(Helpers.PARM_NAME_ISTIM, isTim);
        intent.putExtra(Helpers.PARM_NAME_ISWGT, isWgt);

        startActivityForResult(intent, 0);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK)
            refreshListView();
    }

    public void OnClickAdd(View view) {
        OpenEditor(idExe, "", "", "", "");
    }

    private void theEnd() {
        Intent intent = new Intent();
        setResult(RESULT_OK, intent);
        finish();
    }

}
