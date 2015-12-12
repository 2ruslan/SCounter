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
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;


public class ExerciseSelect extends ActionBarActivity {

    private final String[] columns = new String[]{
            DB.COLUMN_NAMES_NAME
    };
    private final int[] to = new int[]{
            R.id.sle_name
    };
    //String idTrain;
    ListView listView;
    Cursor cursor;
    private SimpleCursorAdapter dataAdapter;
    private int mode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exercise_select);

        Bundle b = getIntent().getExtras();

        mode = b.getInt(Helpers.PARM_MODE);
        listView = (ListView) findViewById(R.id.seList);

        initListView();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.home)
        {
            close();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void refreshListView() {
        cursor = ModelExercise.GetName(this);
        dataAdapter.changeCursor(cursor);
    }

    private void initListView() {
        cursor = ModelExercise.GetName(this);

        dataAdapter = new SimpleCursorAdapter(
                this, R.layout.list_exercise,
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

                if (mode == Helpers.PARM_MODE_GET) {
                    Intent intent = new Intent();
                    intent.putExtra(Helpers.PARM_NAME_ID, cursor.getString(cursor.getColumnIndexOrThrow(DB.COLUMN_NAMES_ID)));
                    intent.putExtra(Helpers.PARM_NAME_NAME, cursor.getString(cursor.getColumnIndexOrThrow(DB.COLUMN_NAMES_NAME)));
                    intent.putExtra(Helpers.PARM_NAME_ISCNT, cursor.getInt(cursor.getColumnIndexOrThrow(DB.COLUMN_NAMES_ISCOUNT)) == 1);
                    intent.putExtra(Helpers.PARM_NAME_ISTIM, cursor.getInt(cursor.getColumnIndexOrThrow(DB.COLUMN_NAMES_ISTIME)) == 1);
                    intent.putExtra(Helpers.PARM_NAME_ISWGT, cursor.getInt(cursor.getColumnIndexOrThrow(DB.COLUMN_NAMES_ISWEIGHT)) == 1);
                    intent.putExtra(Helpers.PARM_NAME_ISSET, cursor.getInt(cursor.getColumnIndexOrThrow(DB.COLUMN_NAMES_ISSET)) == 1);
                    setResult(RESULT_OK, intent);
                    finish();
                }
                else if (mode == Helpers.PARM_MODE_VIEW)
                {
                    Intent intent = new Intent(ExerciseSelect.this, Exercise.class);
                    intent.putExtra(Helpers.PARM_NAME_ID, cursor.getString(cursor.getColumnIndexOrThrow(DB.COLUMN_NAMES_ID)));
                    intent.putExtra(Helpers.PARM_NAME_NAME, cursor.getString(cursor.getColumnIndexOrThrow(DB.COLUMN_NAMES_NAME)));
                    intent.putExtra(Helpers.PARM_NAME_ISCNT, cursor.getInt(cursor.getColumnIndexOrThrow(DB.COLUMN_NAMES_ISCOUNT)) == 1);
                    intent.putExtra(Helpers.PARM_NAME_ISTIM, cursor.getInt(cursor.getColumnIndexOrThrow(DB.COLUMN_NAMES_ISTIME)) == 1);
                    intent.putExtra(Helpers.PARM_NAME_ISWGT, cursor.getInt(cursor.getColumnIndexOrThrow(DB.COLUMN_NAMES_ISWEIGHT)) == 1);
                    intent.putExtra(Helpers.PARM_NAME_ISSET, cursor.getInt(cursor.getColumnIndexOrThrow(DB.COLUMN_NAMES_ISSET)) == 1);
                 //   intent.putExtra(Helpers.PARM_TRAIN_ID, idTrain);
                    startActivityForResult(intent, 0);
                }
            }
        });

        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> arg0, View v,
                                           int index, long arg3) {

                Cursor cursor = (Cursor) listView.getItemAtPosition(index);
                Edit(
                        cursor.getString(cursor.getColumnIndexOrThrow(DB.COLUMN_NAMES_ID)),
                        cursor.getString(cursor.getColumnIndexOrThrow(DB.COLUMN_NAMES_NAME)),
                        cursor.getInt(cursor.getColumnIndexOrThrow(DB.COLUMN_NAMES_ISCOUNT)) == 1,
                        cursor.getInt(cursor.getColumnIndexOrThrow(DB.COLUMN_NAMES_ISWEIGHT)) == 1,
                        cursor.getInt(cursor.getColumnIndexOrThrow(DB.COLUMN_NAMES_ISTIME)) == 1,
                        cursor.getInt(cursor.getColumnIndexOrThrow(DB.COLUMN_NAMES_ISSET)) == 1
                );

                return true;
            }
        });
    }

    private void Edit(final String pid, String pname
            , boolean piscnt, boolean piswgt, boolean pistim, boolean pisset) {
        final boolean addmode = pid.equals("");
        final Context context = this;
        View frg = View.inflate(this, R.layout.fragment_exerc_edit, null);
        final CheckBox cbIsCnt = (CheckBox) frg.findViewById(R.id.cbIsCnt);
        final CheckBox cbIsWgt = (CheckBox) frg.findViewById(R.id.cbIsWgt);
        final CheckBox cbIsTim = (CheckBox) frg.findViewById(R.id.cbIsTim);
        final CheckBox cbIsSet = (CheckBox) frg.findViewById(R.id.cbIsSet);
        final EditText etName = (EditText) frg.findViewById(R.id.etName);

        etName.setText(pname);
        cbIsCnt.setChecked(piscnt);
        cbIsWgt.setChecked(piswgt);
        cbIsTim.setChecked(pistim);
        cbIsSet.setChecked(pisset);

        AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.CustomDialogTheme);
        builder.setTitle(Helpers.getEditDlgTitle(addmode));


        builder
                .setView(frg)
                .setCancelable(false)
                .setPositiveButton(R.string.title_save, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                        ModelExercise.EditName(context, pid, etName.getText().toString()
                                , cbIsCnt.isChecked(), cbIsWgt.isChecked(), cbIsTim.isChecked(), cbIsSet.isChecked());
                        refreshListView();
                    }
                })
                .setNegativeButton(addmode ? R.string.title_cancel : R.string.title_delete, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        if (!addmode) {
                            ModelExercise.DeleteName(context, pid);
                            refreshListView();
                        }
                    }
                }).show();
    }

    public void OnClickAdd(View view) {
        Edit("", "", true, false, false, false);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK)
            refreshListView();
    }

    public void close() {
        setResult(RESULT_CANCELED);
        finish();
    }
}
