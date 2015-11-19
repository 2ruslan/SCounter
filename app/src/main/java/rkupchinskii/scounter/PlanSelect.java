package rkupchinskii.scounter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;


public class PlanSelect extends ActionBarActivity {

    private final String[] columns = new String[]{
            DB.COLUMN_NAMES_NAME
            ,DB.COLUMN_NAMES_ONDATE
    };
    private final int[] to = new int[]{
            R.id.slp_name
            ,R.id.slp_ondate
    };
    ListView listView;
    Cursor cursor;
    private SimpleCursorAdapter dataAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_plan_select);

        listView = (ListView) findViewById(R.id.selPlan);

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


    private void refreshListView() {
        cursor = ModelPlan.GetName(this);
        dataAdapter.changeCursor(cursor);
    }

    private void initListView() {
        cursor = ModelPlan.GetName(this);

        dataAdapter = new SimpleCursorAdapter(
                this, R.layout.list_plan,
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

                Intent intent = new Intent(PlanSelect.this, Plan.class);
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
        View frg = View.inflate(this, R.layout.fragment_plan_edit, null);

       /* final CheckBox cbIsCnt = (CheckBox) frg.findViewById(R.id.cbIsCnt);
        final CheckBox cbIsWgt = (CheckBox) frg.findViewById(R.id.cbIsWgt);
        final CheckBox cbIsTim = (CheckBox) frg.findViewById(R.id.cbIsTim);
        final EditText etName = (EditText) frg.findViewById(R.id.etName);

        etName.setText(pname);
        cbIsCnt.setChecked(piscnt);
        cbIsWgt.setChecked(piswgt);
        cbIsTim.setChecked(pistim);
*/
        AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.CustomDialogTheme);
        builder.setTitle(Helpers.getEditDlgTitle(addmode));


        builder
                .setView(frg)
                .setCancelable(false)
                .setPositiveButton(R.string.title_save, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
/*
                        ModelExercise.EditName(context, pid, etName.getText().toString()
                                , cbIsCnt.isChecked(), cbIsWgt.isChecked(), cbIsTim.isChecked());
                        refreshListView();
                        */
                    }
                })
                .setNegativeButton(addmode ? R.string.title_cancel : R.string.title_delete, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        /*
                        if (!addmode) {
                            ModelExercise.DeleteName(context, pid);
                            refreshListView();
                        }
                        */
                    }
                }).show();

       /*
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
                ModelPlan.EditName(context, pid, value);
                refreshListView();
            }
        });

        alert.setNegativeButton(Helpers.getNoButtonTitle(addmode), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                if (!addmode) {
                    ModelPlan.DeleteName(context, pid);
                    refreshListView();
                }
            }
        });

        alert.show();
        */
    }

    public void OnClickAdd(View view) {
        Edit("", "");
    }

}
