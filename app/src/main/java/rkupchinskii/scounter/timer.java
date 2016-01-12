package rkupchinskii.scounter;


import android.app.Activity;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.widget.TextView;


public class timer extends Activity {

    public static final String PRM_SECOND = "prmSecond";
    static int soundIdEnd;
    TextView tvTmr;
    CountDownTimer tmr;
    SoundPool sp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timer);

        Bundle b = getIntent().getExtras();
        int seconds = b.getInt(PRM_SECOND, 0);

        tvTmr = (TextView) findViewById(R.id.tvTimerVal);

        sp = new SoundPool(5, AudioManager.STREAM_MUSIC, 0);
        soundIdEnd = sp.load(this, R.raw.tmr_end, 1);

        showTimer(seconds);
    }

    private void showTimer(int seconds) {
        if(tmr != null) { tmr.cancel(); }
        tmr = new CountDownTimer(seconds * 1000,  1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                tvTmr.setText(String.valueOf(millisUntilFinished / 1000));
            }
            @Override
            public void onFinish() {
                tmr.cancel();
                playEnd();
                close();
            }
        }.start();
    }

    private void playEnd()
    {
        sp.play(soundIdEnd, 1, 1, 0, 0, 1);
    }

    public void close() {
        setResult(RESULT_OK);
        finish();
    }

}