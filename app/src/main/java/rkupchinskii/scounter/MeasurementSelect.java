package rkupchinskii.scounter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;


public class MeasurementSelect extends ActionBarActivity {

    private final String[] columns = new String[]{
            DB.COLUMN_NAMES_NAME
    };

    private final int[] to = new int[]{
            R.id.slm_name
    };
    ListView listView;
    Cursor cursor;
    private SimpleCursorAdapter dataAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_measurement_select);

        listView = (ListView) findViewById(R.id.smList);

        initListView();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        //  if (resultCode == RESULT_OK)
        //     refreshListView();
    }

    private void refreshListView() {
        cursor = ModelMeasurement.GetName(this);
        dataAdapter.changeCursor(cursor);
    }

    private void initListView() {
        cursor = ModelMeasurement.GetName(this);

        dataAdapter = new SimpleCursorAdapter(
                this, R.layout.list_measurement,
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

                Intent intent = new Intent(MeasurementSelect.this, Measurement.class);
                intent.putExtra(Helpers.PARM_NAME_ID, cursor.getString(cursor.getColumnIndexOrThrow(DB.COLUMN_NAMES_ID)));
                intent.putExtra(Helpers.PARM_NAME_NAME, cursor.getString(cursor.getColumnIndexOrThrow(DB.COLUMN_NAMES_NAME)));
                startActivityForResult(intent, 0);
            }
        });

        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> arg0, View v,
                                           int index, long arg3) {

                Cursor cursor = (Cursor) listView.getItemAtPosition(index);
                Edit(
                        cursor.getString(cursor.getColumnIndexOrThrow(DB.COLUMN_NAMES_ID)),
                        cursor.getString(cursor.getColumnIndexOrThrow(DB.COLUMN_NAMES_NAME))
                );

                return true;
            }
        });
    }

    private void Edit(final String pid, String pname) {
        final boolean addmode = pid.equals("");
        final Context context = this;

        AlertDialog.Builder alert = new AlertDialog.Builder(this, R.style.CustomDialogTheme);

        alert.setTitle(Helpers.getEditDlgTitle(addmode));
        final EditText input = new EditText(this);
        input.setText(pname);
        alert.setView(input);

        alert.setPositiveButton(R.string.title_save, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                String value = input.getText().toString();
                ModelMeasurement.EditName(context, pid, value);
                refreshListView();
            }
        });

        alert.setNegativeButton(Helpers.getNoButtonTitle(addmode), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                if (!addmode) {
                    ModelMeasurement.DeleteName(context, pid);
                    refreshListView();
                }
            }
        });

        alert.show();


    }

    public void OnClickAdd(View view) {
        Edit("", "");
    }
}
