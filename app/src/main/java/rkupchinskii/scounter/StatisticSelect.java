package rkupchinskii.scounter;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.SimpleCursorAdapter;
import android.widget.Spinner;

import java.util.ArrayList;


public class StatisticSelect extends ActionBarActivity {

    final private String SET_TYPE = "lasttype";
    final private int TYPE_EXERCISE = 0;
    final private int TYPE_MEASURMENT = 1;
    Spinner spType;
    Spinner spName;
    Spinner spAgr;
    Spinner spCTW;
    Cursor cursorNames;
    SimpleCursorAdapter dataAdapterNames;
    int nameId;
    int cwt;
    int aggr;
    private String titleCnt;
    private String titleWgt;
    private String titleCntWgt;
    private String titleTim;
    private ArrayAdapter<String> dataAdapterCWT;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistic_select);

        spType = (Spinner) findViewById(R.id.ssType);
        spName = (Spinner) findViewById(R.id.ssName);
        spAgr = (Spinner) findViewById(R.id.ssAgr);
        spCTW = (Spinner) findViewById(R.id.ssCTW);


        titleCnt = getResources().getString(R.string.title_iscnt);
        titleWgt = getResources().getString(R.string.title_iswgt);
        titleTim = getResources().getString(R.string.title_istim);
        titleCntWgt = titleCnt + " x " + titleWgt;

        initSpiiners();

        Bundle b = getIntent().getExtras();
        if ( b != null && b.containsKey(Helpers.PARAM_EXERC_ID)) {

            if( b.getInt(Helpers.PARM_MODE) == Helpers.PARAM_MEAS)
                spType.setSelection(1);

            String id = b.getString(Helpers.PARAM_EXERC_ID, "");
            if (!id.equals("")) {
                for (int i = 0; i < spName.getAdapter().getCount(); i++) {
                    Cursor cursor = (Cursor) spName.getItemAtPosition(i);
                    String idi = cursor.getString(cursor.getColumnIndexOrThrow(DB.COLUMN_NAMES_ID));
                    if (id.equals(idi))
                        spName.setSelection(i);
                }
                spAgr.setSelection(1); //sum
            }
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
/*
        if (id == R.id.home)
        {
            */
            close();
            return true;
  //      }

    //    return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

    }

    private void restore(Bundle outState)
    {
        spType.setSelection(outState.getInt(SET_TYPE,0));
    }

    private void initSpiiners() {
        // Тип
         if (spType.getAdapter() !=null)
             return;

        ArrayAdapter<?> adapter =
                ArrayAdapter.createFromResource(this, R.array.statistic_type, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spType.setAdapter(adapter);
        spType.setSelection(0);

        spType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent,
                                       View itemSelected, int selectedItemPosition, long selectedId) {
                refreshNames(selectedItemPosition);
            }

            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        // Названия
        cursorNames = ModelExercise.GetName(this);

        String[] columns = new String[]{DB.COLUMN_NAMES_NAME};
        int[] to = new int[]{android.R.id.text1};

        dataAdapterNames = new SimpleCursorAdapter(
                this, android.R.layout.simple_spinner_item,
                cursorNames,
                columns,
                to,
                0);
        dataAdapterNames.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spName.setAdapter(dataAdapterNames);
        spName.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent,
                                       View itemSelected, int selectedItemPosition, long selectedId) {
                Cursor cursor = (Cursor) spName.getItemAtPosition(selectedItemPosition);

                nameId = cursor.getInt(cursor.getColumnIndexOrThrow(DB.COLUMN_NAMES_ID));

                if (spType.getSelectedItemPosition() == TYPE_EXERCISE)
                    refreshCWT(cursor.getInt(cursor.getColumnIndexOrThrow(DB.COLUMN_NAMES_ISCOUNT)) == 1,
                            cursor.getInt(cursor.getColumnIndexOrThrow(DB.COLUMN_NAMES_ISWEIGHT)) == 1,
                            cursor.getInt(cursor.getColumnIndexOrThrow(DB.COLUMN_NAMES_ISTIME)) == 1
                    );
            }

            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        // cwt
        dataAdapterCWT = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, new ArrayList<String>());
        dataAdapterCWT.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spCTW.setAdapter(dataAdapterCWT);
        spCTW.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent,
                                       View itemSelected, int selectedItemPosition, long selectedId) {
                if (selectedItemPosition > -1) {
                    if (dataAdapterCWT.getItem(selectedItemPosition) == titleCnt)
                        cwt = Helpers.PARM_CWT_CNT;
                    else if (dataAdapterCWT.getItem(selectedItemPosition) == titleWgt)
                        cwt = Helpers.PARM_CWT_WGT;
                    else if (dataAdapterCWT.getItem(selectedItemPosition) == titleTim)
                        cwt = Helpers.PARM_CWT_TIM;
                    else if (dataAdapterCWT.getItem(selectedItemPosition) == titleCntWgt)
                        cwt = Helpers.PARM_CWT_CNTWGT;
                }
            }

            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        // aggr
        spAgr.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent,
                                       View itemSelected, int selectedItemPosition, long selectedId) {

                if (selectedItemPosition == 0 /*All*/)
                    aggr = Helpers.PARM_AGR_ALL;
                else if (selectedItemPosition == 1/*Sum*/)
                    aggr = Helpers.PARM_AGR_SUM;
                else if (selectedItemPosition == 2/*Max*/)
                    aggr = Helpers.PARM_AGR_MAX;
                else if (selectedItemPosition == 3/*Min*/)
                    aggr = Helpers.PARM_AGR_MIN;
            }

            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

    }

    private void refreshNames(int nameType) {
        if (nameType == TYPE_EXERCISE) {
            cursorNames = ModelExercise.GetName(this);
            spCTW.setVisibility(View.VISIBLE);
            spAgr.setVisibility(View.VISIBLE);
        } else {
            cursorNames = ModelMeasurement.GetName(this);
            spCTW.setVisibility(View.GONE);
            spAgr.setVisibility(View.GONE);
            cwt = Helpers.PARM_CWT_CNT;
            aggr = Helpers.PARM_AGR_ALL;
        }

        dataAdapterNames.changeCursor(cursorNames);

    }

    private void refreshCWT(boolean isCnt, boolean isWgt, boolean isTim) {
        dataAdapterCWT.clear();
        if (isCnt)
            dataAdapterCWT.add(titleCnt);
        if (isWgt)
            dataAdapterCWT.add(titleWgt);
        if (isCnt && isWgt)
            dataAdapterCWT.add(titleCntWgt);
        if (isTim)
            dataAdapterCWT.add(titleTim);
        dataAdapterCWT.notifyDataSetChanged();
    }

    public void OnClickGo(View view) {

        Intent intent = new Intent(StatisticSelect.this, Statistic.class);
        intent.putExtra(Helpers.PARM_NAME_ID, nameId);
        intent.putExtra(Helpers.PARM_AGR, aggr);
        intent.putExtra(Helpers.PARM_CWT, cwt);
        intent.putExtra(Helpers.PARM_MODE,
                spType.getSelectedItemPosition() == TYPE_EXERCISE ? Helpers.PARAM_EXERC : Helpers.PARAM_MEAS );

        startActivityForResult(intent, 0);
    }

    public void close() {
        setResult(RESULT_CANCELED);
        finish();
    }
}
