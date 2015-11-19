package rkupchinskii.scounter;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;


public class MeasurementDetails extends Activity {

    String idVal;
    String idMes;

    EditText cntCtrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_measurement_details);

        Bundle b = getIntent().getExtras();
        idMes = b.getString(Helpers.PARM_NAME_ID);
        idVal = b.getString(Helpers.PARM_VAL_ID);

        boolean addMode = idVal.equals("");

        cntCtrl = (EditText) findViewById(R.id.mdCount);
        ((Button) findViewById(R.id.mdDelete)).setText(Helpers.getNoButtonTitle(addMode));

        if (!addMode)
            cntCtrl.setText(b.getString(Helpers.PARM_CNT_REAL), TextView.BufferType.EDITABLE);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        return super.onOptionsItemSelected(item);
    }

    public void OnClickSave(View view) {
        ModelMeasurement.EditValue(this, idVal, idMes, cntCtrl.getText().toString());

        theEnd();
    }

    public void OnClickDelete(View view) {
        ModelMeasurement.DeleteValue(this, idVal);

        theEnd();
    }

    private void theEnd() {
        Intent intent = new Intent();
        setResult(RESULT_OK, intent);
        finish();
    }
}
