package com.example.multimediamemos;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.provider.MediaStore;
import android.speech.RecognizerIntent;

import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import lombok.SneakyThrows;

public class MainActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private RecyclerView.Adapter<MemoAdapter.MemoEntryHolder> mAdapter;
    private RecyclerView.LayoutManager layoutManager;
    List<String> myDataset = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //add menu_main toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        //add recycler view
        recyclerView = (RecyclerView) findViewById(R.id.memo_list);

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        recyclerView.setHasFixedSize(true);

        // use a linear layout manager
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        // specify an adapter (see also next example)

        mAdapter = new MemoAdapter(myDataset, this);
        recyclerView.setAdapter(mAdapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    private static final int SEARCH_CAPTIONS = 3;
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_add) {
            myDataset.add("@TODO");
            findViewById(R.id.empty_list_msg).setVisibility(View.GONE);
            mAdapter.notifyDataSetChanged();
            return true;
        }
        if (id == R.id.action_search) {
            Intent intent = new Intent(this, SearchActivity.class);
            startActivityForResult(intent, SEARCH_CAPTIONS);
        }
        return super.onOptionsItemSelected(item);
    }
    private ImageButton tempImageBtn = null;
    private MemoEntry tempMemo = null;
    private static final int SELECT_MEDIA = 1;
    public void pickMedia(MemoEntry selectedMemo, ImageButton selectedBtn) {
        tempImageBtn = selectedBtn;
        tempMemo = selectedMemo;
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), SELECT_MEDIA);
    }

    @SneakyThrows
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
            if (resultCode == Activity.RESULT_OK && data != null) {
                if (requestCode == SELECT_MEDIA) {
                    try {
                        Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), data.getData());
//                        tempMemo.setVideoPath(data.getData().toString());
                        setOptimizedBitmap(bitmap, tempImageBtn);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                if (requestCode == RECORD_VOICE) {
                    //null
//                    Uri audioUri = data.getData();
//                    tempMemo.setVoiceRecordPath(audioUri.toString());
                    ArrayList<String> result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    tempCaptionField.setText(result.get(0));
                }
            } else if (resultCode == Activity.RESULT_CANCELED)  {
                Toast.makeText(this, "Canceled", Toast.LENGTH_SHORT).show();
            }
        tempImageBtn = null;
        tempMemo = null;
        tempCaptionField = null;
    }

    private void setOptimizedBitmap(Bitmap orig, ImageButton image) {
        float scaleWidth = ((float) image.getWidth()) / orig.getWidth();
        float scaleHeight = ((float) image.getHeight()) / orig.getHeight();
        Matrix matrix = new Matrix();
        matrix.postScale(scaleWidth, scaleHeight);

        Bitmap resized = Bitmap.createBitmap(orig, 0, 0, orig.getWidth(), orig.getHeight(), matrix, false);
        orig.recycle();
        image.setImageBitmap(resized);
    }
    private static final int RECORD_VOICE = 2;
    private EditText tempCaptionField = null;

    public void recordVoice(MemoEntry selectedMemo, EditText selectedCaptionField) {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        if (intent.resolveActivity(getPackageManager()) == null) {
            Toast.makeText(this, getResources().getString(R.string.speech_permission_err), Toast.LENGTH_LONG);
            return;
        }

        tempMemo = selectedMemo;
        tempCaptionField = selectedCaptionField;
        intent.putExtra(RecognizerIntent.ACTION_RECOGNIZE_SPEECH, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
        //no longer works, always cancelled
//        intent.putExtra("android.speech.extra.GET_AUDIO_FORMAT", "audio/AMR");
//        intent.putExtra("android.speech.extra.GET_AUDIO", true);
        startActivityForResult(intent,RECORD_VOICE);
    }
}