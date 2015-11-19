package rkupchinskii.scounter;

import android.app.Activity;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;


public class ExerciseDetails  extends Activity /* implements SensorEventListener*/ {

    String idVal;
    String idExe;
    String idTrain;

    boolean isCnt;
    boolean isWgt;
    boolean isTim;


    EditText cntCtrl;
    EditText wgtCtrl;
    EditText timCtrl;

    TextView cntPCtrl;
    TextView wgtPCtrl;
    TextView timPCtrl;

    private SensorManager mSensorManager;
    private Sensor mAccelerometer;
    float mAccel;
    float mAccelCurrent;
    float mAccelLast;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exercise_details);

        mSensorManager = (SensorManager)getSystemService(SENSOR_SERVICE);
        mAccelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        mAccel = SensorManager.GRAVITY_EARTH;
        mAccelCurrent = SensorManager.GRAVITY_EARTH;
        mAccelLast = SensorManager.GRAVITY_EARTH;

        Bundle ex = getIntent().getExtras();

        idExe = ex.getString(Helpers.PARM_NAME_ID);
        idVal = ex.getString(Helpers.PARM_VAL_ID);
        idTrain = ex.getString(Helpers.PARM_TRAIN_ID);

        isCnt = ex.getBoolean(Helpers.PARM_NAME_ISCNT);
        isWgt = ex.getBoolean(Helpers.PARM_NAME_ISWGT);
        isTim = ex.getBoolean(Helpers.PARM_NAME_ISTIM);

        cntCtrl = (EditText) findViewById(R.id.mdCount);
        cntPCtrl = (TextView) findViewById(R.id.mdCountP);

        wgtCtrl = (EditText) findViewById(R.id.mdWeight);
        wgtPCtrl = (TextView) findViewById(R.id.mdWeightP);

        timCtrl = (EditText) findViewById(R.id.mdTime);
        timPCtrl = (TextView) findViewById(R.id.mdTimeP);

        boolean addMode = idVal.equals("");

        ((Button) findViewById(R.id.mdDelete)).setText(Helpers.getNoButtonTitle(addMode));

        if (!isCnt)
            (findViewById(R.id.llCnt)).setVisibility(LinearLayout.GONE);
        if (!isTim)
            (findViewById(R.id.llTim)).setVisibility(LinearLayout.GONE);
        if (!isWgt)
            (findViewById(R.id.llWgt)).setVisibility(LinearLayout.GONE);

        if (!addMode) {
            cntCtrl.setText(ex.getString(Helpers.PARM_CNT_REAL), TextView.BufferType.EDITABLE);
            cntPCtrl.setText(ex.getString(Helpers.PARM_CNT_PLAN));

            wgtCtrl.setText(ex.getString(Helpers.PARM_WGT_REAL), TextView.BufferType.EDITABLE);
            wgtPCtrl.setText(ex.getString(Helpers.PARM_WGT_PLAN));

            timCtrl.setText(ex.getString(Helpers.PARM_TIM_REAL), TextView.BufferType.EDITABLE);
            timPCtrl.setText(ex.getString(Helpers.PARM_TIM_PLAN));
        }

        if (isCnt)
            cntCtrl.requestFocus();
        else if (isWgt && !isCnt)
            wgtCtrl.requestFocus();
        else if(isTim && !isWgt && !isCnt)
            timCtrl.requestFocus();
        if (isTim)
            timCtrl.setOnKeyListener(onEnterFocusDown);
        else if (isWgt)
            wgtCtrl.setOnKeyListener(onEnterFocusDown);
        else if (isCnt)
            cntCtrl.setOnKeyListener(onEnterFocusDown);
    }

    protected TextView.OnKeyListener onEnterFocusDown = new TextView.OnKeyListener() {

        @Override
        public boolean onKey(View v, int keyCode, KeyEvent event) {
            if ((event.getAction() == KeyEvent.ACTION_DOWN) &&
                    (keyCode == KeyEvent.KEYCODE_ENTER)) {
                v.requestFocus(View.FOCUS_DOWN);
                Save();
            }
            return false;
        }
    };

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    protected void onResume() {
        super.onResume();
     //   mSensorManager.registerListener(this, mAccelerometer, SensorManager.SENSOR_DELAY_FASTEST);
    }

    protected void onPause() {
        super.onPause();
    //    mSensorManager.unregisterListener(this);
    }

    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    boolean top = false;
    boolean bootom = false;
    int cnt = 0;

    int cntx = 0;
    int cnty = 0;
    int cntz = 0;

    int px = 0;
    int py =0;
    int pz =0;

    float maxx = 0f;
    float maxy = 0f;
    float maxz = 0f;

    float speedAfter = 0f;
    float speedBefore = 0f;
    float distance =0f;
    long prevTms = 0;



    public void onSensorChanged(SensorEvent event) {
        float [] values = event.values;
        switch(event.sensor.getType())
        {

            case Sensor.TYPE_ACCELEROMETER:
            {
                float x = values[0];
                float y = values[1];
                float z = values[2];

                mAccelLast = mAccelCurrent;
                mAccelCurrent = (float) Math.sqrt((double) (x*x + y*y + z*z));
                float delta = mAccelCurrent - mAccelLast;

                mAccel = mAccel * 0.9f + delta; // perform low-cut filter

                long curTms = System.currentTimeMillis();
                if (prevTms == 0)
                    prevTms = curTms;
/*
                if (mAccel > 2 )
                    top = true;
                if (mAccel < 2 && top) {
                    cnt++;
                    top = false;
                }
*/
                float t = (curTms - prevTms) / 1000f;

                 speedAfter = speedAfter + (mAccel * t);

                 cntPCtrl.setText(String.valueOf(cnt) + " " + String.valueOf(speedAfter));

                prevTms = curTms;

            }
            break;
        }
    }

    private void Save()
    {
        ModelExercise.EditValue(this, idVal, idExe, idTrain
                , cntPCtrl.getText().toString()
                , cntCtrl.getText().toString()
                , wgtPCtrl.getText().toString()
                , wgtCtrl.getText().toString()
                , timPCtrl.getText().toString()
                , timCtrl.getText().toString()
        );

        theEnd();
    }

    public void OnClickSave(View view) {
        Save();
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
