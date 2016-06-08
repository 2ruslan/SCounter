package rkupchinskii.scounter;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class Settings extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.setTheme(R.style.AppThemeWhite);

        setContentView(R.layout.activity_settings);
    }

    public void OnClicBlack(View view) {

    }
}
