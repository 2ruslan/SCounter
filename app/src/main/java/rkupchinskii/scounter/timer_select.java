package rkupchinskii.scounter;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;

public class timer_select extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timer_select);
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
                      //  refreshListView();
                    }
                })
                .setNegativeButton(addmode ? R.string.title_cancel : R.string.title_delete, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        if (!addmode) {
                            ModelExercise.DeleteName(context, pid);
                        //    refreshListView();
                        }
                    }
                }).show();
    }

    public void OnClickTimer_1(View view) {
        StartTimer(1);
    }
    public void OnClickTimer_2(View view) {
        StartTimer(2);
    }
    public void OnClickTimer_3(View view) {
        StartTimer(3);
    }
    public void OnClickTimer_4(View view) {
        StartTimer(4);
    }
    private void StartTimer(int time){
        Intent intent = new Intent(timer_select.this, timer.class);
        intent.putExtra(timer.PRM_SECOND, time);

        startActivityForResult(intent, 0);

        setResult(RESULT_OK);
        finish();
    }
}
