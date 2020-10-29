package com.mgg.demo.mggwidgets.view.activity;

import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.mgg.demo.mggwidgets.R;
import com.mgg.demo.mggwidgets.view.widgets.RecordButton;

public class RecordButtonActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record_button);
    }

    public void onRecordButtonClick(View view) {
        RecordButton recordButton= (RecordButton) view;
        recordButton.setRecording(!recordButton.isRecording());
    }
}
