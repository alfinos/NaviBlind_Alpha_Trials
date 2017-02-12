package com.example.salfino.naviblind_110217;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class TestActivity extends AppCompatActivity {

    private TextView mTest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);

        mTest = (TextView) findViewById(R.id.textView);
        mTest.setTextSize(30);
        mTest.setTextColor(0xFFFF4046);
    }
}
