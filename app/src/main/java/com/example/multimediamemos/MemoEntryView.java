package com.example.multimediamemos;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.Toast;

import java.io.InputStream;


public class MemoEntryView extends FrameLayout {
    public EditText captionField;
    public ImageButton videoBtn;
    public Button playBtn;
    public Button recordBtn;
    public Button saveBtn;
    public Button deleteBtn;
    public MemoEntry memo = MemoEntry.builder().build();
    private MainActivity viewController;

    public MemoEntryView(Context context, MainActivity activity) {
        super(context);
        inflate(getContext(), R.layout.recycler_view_item, this);
        captionField = findViewById(R.id.captionEdt);
        videoBtn = findViewById(R.id.videoBox);
        playBtn = findViewById(R.id.playBtn);
        recordBtn = findViewById(R.id.recordBtn);
        saveBtn = findViewById(R.id.saveBtn);
        deleteBtn = findViewById(R.id.deleteBtn);
        viewController = activity;

        videoBtn.setOnClickListener(listener);
        playBtn.setOnClickListener(listener);
        recordBtn.setOnClickListener(listener);
        saveBtn.setOnClickListener(listener);
        deleteBtn.setOnClickListener(listener);
    }

    private final OnClickListener listener = new OnClickListener() {
        public void onClick(View v) {
            switch(v.getId()) {
                case R.id.videoBox:
                    videoClick(v);
                    break;
                case R.id.playBtn:
                    playClick(v);
                    break;
                case R.id.recordBtn:
                    recordClick(v);
                    break;
                case R.id.saveBtn:
                    saveClick(v);
                    break;
                case R.id.deleteBtn:
                    deleteClick(v);
                    break;
            }
        }
    };

    private void playClick(View view) {
        if (memo.getVoiceRecordPath() != null) {
//            InputStream filestream = viewController.getContentResolver().openInputStream(audioUri);
            MediaPlayer mp = new MediaPlayer();
            try {
                mp.setDataSource(memo.getVoiceRecordPath());
                mp.prepare();
                mp.start();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    private void recordClick(View view) {
        viewController.recordVoice(memo, captionField);
    }
    private void saveClick(View view) {
        Toast.makeText(view.getContext(), "save?", Toast.LENGTH_SHORT).show();
    }
    private void deleteClick(View view) {
        Toast.makeText(view.getContext(), "delete?", Toast.LENGTH_SHORT).show();
    }
    private void videoClick(View view) {
        viewController.pickMedia(memo, videoBtn);
    }
}
