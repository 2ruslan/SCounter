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


public class Measurement extends ActionBarActivity {

    private final String[] columns = new String[]{
            DB.COLUMN_VALUES_CNT_REAL,
            DB.COLUMN_VALUES_ONDATE
    };

    private final int[] to = new int[]{
            R.id.vlm_cnt,
            R.id.vlm_ondate
    };

    String idMes;
    String nameMes;
    ListView listView;
    Cursor cursor;
    private SimpleCursorAdapter dataAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_measurement);

        Bundle b = getIntent().getExtras();
        idMes = b.getString(Helpers.PARM_NAME_ID);
        nameMes = b.getString(Helpers.PARM_NAME_NAME);

        setTitle(nameMes);

        listView = (ListView) findViewById(R.id.vmList);

        initListView();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == android.R.id.home) {
            close();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void refreshListView() {
        cursor = ModelMeasurement.GetValues(this, idMes);
        dataAdapter.changeCursor(cursor);
    }

    private void initListView() {

        cursor = ModelMeasurement.GetValues(this, idMes);

        dataAdapter = new SimpleCursorAdapter(
                this, R.layout.list_measurement_val,
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
                        , cursor.getString(cursor.getColumnIndexOrThrow(DB.COLUMN_VALUES_CNT_REAL))
                );
            }
        });
    }

    public void OpenEditor(String idM, String idV, String c) {
        Intent intent = new Intent(Measurement.this, MeasurementDetails.class);
        intent.putExtra(Helpers.PARM_NAME_ID, idM);
        intent.putExtra(Helpers.PARM_VAL_ID, idV);
        intent.putExtra(Helpers.PARM_CNT_REAL, c);

        startActivityForResult(intent, 0);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK)
            refreshListView();
    }

    public void OnClickAdd(View view) {
        OpenEditor(idMes, "", "");
    }

    public void close() {
        setResult(RESULT_OK);
        finish();
    }

    public void OnClickStatistic(View view) {
        Intent intent = new Intent(Measurement.this, StatisticSelect.class);
        intent.putExtra(Helpers.PARAM_EXERC_ID, idMes);
        intent.putExtra(Helpers.PARM_MODE, Helpers.PARAM_MEAS );
        startActivityForResult(intent, 1);
    }
}
