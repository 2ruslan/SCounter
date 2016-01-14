package rkupchinskii.scounter;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

public class timer_select extends Activity {

    Integer time1;
    Integer time2;

    Button bt_timer_1;
    Button bt_timer_2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timer_select);

        bt_timer_1 = (Button)findViewById(R.id.timer_1);
        bt_timer_2 = (Button)findViewById(R.id.timer_2);

        initTimers();
    }

    private  void initTimers(){
        time1 = PreferencesHelper.GetTimer_1();
        time2 = PreferencesHelper.GetTimer_2();

        if(time1 == null && time2 == null) {
            Edit();
        }

        SyncTimer();
    }

    private void Edit() {

        View frg = View.inflate(this, R.layout.timer_edit, null);
        final EditText ed_timer_1 = (EditText) frg.findViewById(R.id.ed_timer_1);
        final EditText ed_timer_2 = (EditText) frg.findViewById(R.id.ed_timer_2);

        if(time1 != null)
            ed_timer_1.setText(String.valueOf(time1));
        if(time2 != null)
            ed_timer_2.setText(String.valueOf(time2));

        AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.CustomDialogTheme);

        builder.setTitle(R.string.title_edit);

        builder
                .setView(frg)
                .setCancelable(false)
                .setPositiveButton(R.string.title_save, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        String t1 = ed_timer_1.getText().toString();
                        String t2 = ed_timer_2.getText().toString();

                        if (t1 != null && !t1.equals(""))
                            time1 = Integer.valueOf(t1);
                        else
                            time1 = null;

                        if (t2 != null && !t2.equals(""))
                            time2 = Integer.valueOf(t2);
                        else
                            time2 = null;

                        PreferencesHelper.SetTimer_1(time1);
                        PreferencesHelper.SetTimer_2(time2);

                        SyncTimer();
                    }
                })
                .setNegativeButton(R.string.title_cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                    }
                }).show();


    }

    private void SyncTimer(){
        if(time1 != null)
            bt_timer_1.setText( String.valueOf(time1));
        else
            bt_timer_1.setText( "");

        if(time2 != null)
            bt_timer_2.setText( String.valueOf(time2));
        else
            bt_timer_2.setText( "");
    }

    public void OnClickTimer_1(View view) {
        if(time1 != null)
            StartTimer(time1);
        else
            Edit();
    }
    public void OnClickTimer_2(View view) {
        if(time2 != null)
            StartTimer(time2);
        else
            Edit();
    }

    public void OnCancel(View view) {
        setResult(RESULT_CANCELED);
        finish();
    }

    public void OnEdit(View view) {
        Edit();
    }

    private void StartTimer(int time){
        Intent intent = new Intent(timer_select.this, timer.class);
        intent.putExtra(timer.PRM_SECOND, time);

        startActivityForResult(intent, 0);

        setResult(RESULT_OK);
        finish();
    }
}
