package rkupchinskii.scounter;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;


public class PlanExerciseDetails extends ActionBarActivity {

    String idVal;
    String idExe;
    String idPlan;

    boolean isCnt;
    boolean isWgt;
    boolean isTim;


    EditText cntPCtrl;
    EditText wgtPCtrl;
    EditText timPCtrl;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_plan_exercise_details);

        Bundle ex = getIntent().getExtras();

        idExe = ex.getString(Helpers.PARM_NAME_ID);
        idVal = ex.getString(Helpers.PARM_VAL_ID);
        idPlan = ex.getString(Helpers.PARM_PLAN_ID);

        isCnt = ex.getBoolean(Helpers.PARM_NAME_ISCNT);
        isWgt = ex.getBoolean(Helpers.PARM_NAME_ISWGT);
        isTim = ex.getBoolean(Helpers.PARM_NAME_ISTIM);

        cntPCtrl = (EditText) findViewById(R.id.mdCountP);
        wgtPCtrl = (EditText) findViewById(R.id.mdWeightP);
        timPCtrl = (EditText) findViewById(R.id.mdTimeP);

        boolean addMode = idVal.equals("");

        ((Button) findViewById(R.id.mdDelete)).setText(Helpers.getNoButtonTitle(addMode));

        if (!isCnt)
            (findViewById(R.id.llCnt)).setVisibility(LinearLayout.GONE);
        if (!isTim)
            (findViewById(R.id.llTim)).setVisibility(LinearLayout.GONE);
        if (!isWgt)
            (findViewById(R.id.llWgt)).setVisibility(LinearLayout.GONE);

        if (!addMode) {
            cntPCtrl.setText(ex.getString(Helpers.PARM_CNT_PLAN), TextView.BufferType.EDITABLE);
            wgtPCtrl.setText(ex.getString(Helpers.PARM_WGT_PLAN), TextView.BufferType.EDITABLE);
            timPCtrl.setText(ex.getString(Helpers.PARM_TIM_PLAN), TextView.BufferType.EDITABLE);
        }
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

    public void OnClickSave(View view) {
        ModelPlan.EditValue(this, idVal, idExe, idPlan
                , cntPCtrl.getText().toString()
                , wgtPCtrl.getText().toString()
                , timPCtrl.getText().toString()
        );

        theEnd();
    }

    public void OnClickDelete(View view) {
        ModelExercise.DeleteValue(this, idVal);
        theEnd();
    }

    private void theEnd() {
        Intent intent = new Intent();
        setResult(RESULT_OK, intent);
        finish();
    }
}
