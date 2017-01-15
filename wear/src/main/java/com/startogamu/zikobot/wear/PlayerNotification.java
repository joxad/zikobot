package com.startogamu.zikobot.wear;

import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.wearable.activity.WearableActivity;
import android.widget.TextView;

public class PlayerNotification extends WearableActivity {

    private TextView mTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player);
        mTextView = (TextView) findViewById(R.id.text);
        setAmbientEnabled();

    }


    @Override
    public void onEnterAmbient(Bundle ambientDetails) {
        super.onEnterAmbient(ambientDetails);
        updateDisplay();
    }

    @Override
    public void onUpdateAmbient() {
        super.onUpdateAmbient();
        updateDisplay();
    }

    @Override
    public void onExitAmbient() {
        updateDisplay();
        super.onExitAmbient();
    }

    private void updateDisplay() {
        if (isAmbient()) {
            mTextView.setTextColor(ContextCompat.getColor(this, android.R.color.white));

        } else {
            mTextView.setTextColor(ContextCompat.getColor(this, android.R.color.black));
        }
    }
}
