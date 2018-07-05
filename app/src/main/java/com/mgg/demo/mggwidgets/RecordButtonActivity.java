package com.mgg.demo.mggwidgets;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.mgg.demo.mggwidgets.widgets.RecordButton;

public class RecordButtonActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record_button);
    }

    public void onRecordButtonClick(View view) {
        RecordButton recordButton= (RecordButton) view;
        recordButton.setRecording(!recordButton.getIsRecording());
    }
}
