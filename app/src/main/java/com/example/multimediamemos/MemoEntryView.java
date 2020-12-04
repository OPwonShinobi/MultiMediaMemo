package com.example.multimediamemos;

import android.annotation.SuppressLint;
import android.content.Context;
import android.media.MediaPlayer;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.Toast;


@SuppressLint("ViewConstructor")
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
        videoBtn.setOnLongClickListener(longListener);
        playBtn.setOnClickListener(listener);
        recordBtn.setOnClickListener(listener);
        saveBtn.setOnClickListener(listener);
        deleteBtn.setOnClickListener(listener);
    }
    private final OnLongClickListener longListener = new OnLongClickListener() {
        @Override
        public boolean onLongClick(View v) {
            videoLongClick();
            return false;
        }
    };
    private final OnClickListener listener = new OnClickListener() {
        public void onClick(View v) {
            switch(v.getId()) {
                case R.id.videoBox:
                    videoClick();
                    break;
                case R.id.playBtn:
                    playClick();
                    break;
                case R.id.recordBtn:
                    recordClick();
                    break;
                case R.id.saveBtn:
                    saveClick(v);
                    break;
                case R.id.deleteBtn:
                    deleteClick();
                    break;
            }
        }
    };

    private void playClick() {
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
    private void videoLongClick() {
        viewController.pickMedia(memo, videoBtn);
    }
    private void recordClick() {
        viewController.recordVoice(memo, captionField);
    }
    private void saveClick(View view) {
        Toast.makeText(view.getContext(), "save?", Toast.LENGTH_SHORT).show();
    }
    private void deleteClick() {
        viewController.deleteItem(null);
    }
    private void videoClick() {
        if (memo.getMediaPath() != null) {
            viewController.showMedia(memo);
        }
    }
}
