package com.mgg.demo.mggwidgets;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.mgg.demo.mggwidgets.widgets.RecordButton;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void onRecordButtonClick(View view) {
        RecordButton recordButton= (RecordButton) view;
        recordButton.setRecording(!recordButton.getIsRecording());
    }
}
