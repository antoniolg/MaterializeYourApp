package com.antonioleiva.materializeyourapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

public class SettingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(R.anim.slide_in_up, R.anim.still);
        setContentView(R.layout.activity_settings);
    }

    @Override
    protected void onPause() {
        super.onPause();
        overridePendingTransition(R.anim.still, R.anim.slide_out_down);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.still, R.anim.slide_out_down);
    }

    @Override
    public void onEnterAnimationComplete() {
        super.onEnterAnimationComplete();
        Toast.makeText(this, "Animation Completed", Toast.LENGTH_SHORT).show();
    }
}
