package rkupchinskii.scounter;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;


public class EndPrg extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_end_prg);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }

    public void OnClickEstim(View view) {
        String appPackageName = getPackageName();
        Intent marketIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName));
        marketIntent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY | Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
        startActivity(marketIntent);
        PreferencesHelper.SetNoShowPropO(true);

        finish();
    }
    public void OnClickNoShowEstim(View view) {
        PreferencesHelper.SetNoShowPropO(true);
        finish();
    }


    public void OnClickClose(View view) {
        finish();
    }

}
