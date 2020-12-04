package com.example.multimediamemos;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Environment;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.speech.RecognizerIntent;

import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import lombok.SneakyThrows;

public class MainActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private RecyclerView.Adapter<MemoAdapter.MemoEntryHolder> mAdapter;
    private RecyclerView.LayoutManager layoutManager;
    List<Integer> myDataset = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());
        builder.detectFileUriExposure();

        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        recyclerView = findViewById(R.id.memo_list);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        mAdapter = new MemoAdapter(myDataset, this);
        recyclerView.setAdapter(mAdapter);

        if(Build.VERSION.SDK_INT > 22){
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
            }
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        for (int i = 0; i < grantResults.length; i++) {
            if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Permission denied for:" + permissions[i], Toast.LENGTH_SHORT).show();
            }
        }
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
            myDataset.add(myDataset.size()+1);
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
    public void deleteItem(MemoEntryView memoView) {
        if (myDataset.size() > 0) {
            myDataset.remove(0);
            mAdapter.notifyDataSetChanged();
            if (myDataset.isEmpty()) {
                findViewById(R.id.empty_list_msg).setVisibility(View.VISIBLE);
            }
        }
    }

    private ImageButton tempImageBtn = null;
    private MemoEntry tempMemo = null;
    private static final int SELECT_MEDIA = 1;
    public void pickMedia(MemoEntry selectedMemo, ImageButton selectedBtn) {
        tempImageBtn = selectedBtn;
        tempMemo = selectedMemo;
        Intent intent = new Intent();
        intent.setType("video/*");
        intent.setAction(Intent.ACTION_PICK);
        startActivityForResult(Intent.createChooser(intent, "Select Video"), SELECT_MEDIA);
    }
    private File createVideoFile(String fileName) {
        String fileNameWithOutExt = fileName.substring(0, fileName.lastIndexOf("."));
        File storageDir = this.getExternalFilesDir(Environment.DIRECTORY_DCIM);
        File image = null;
        try {
            image = File.createTempFile(fileNameWithOutExt,".mp4", storageDir);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return image;
    }

    public void showMedia(MemoEntry selectedMemo) {
        Intent intent = new Intent(Intent.ACTION_VIEW);

        intent.setDataAndType(
                FileProvider.getUriForFile(this,getPackageName() + ".provider", new File(selectedMemo.getMediaPath())),
                "video/*"
        );
//        intent.setData(Uri.fromFile(new File(selectedMemo.getMediaPath())));
//        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
//        intent.putExtra(Intent.EXTRA_MIME_TYPES, new String[] {"image/*", "video/*"});
        startActivity(intent);
    }
    @SneakyThrows
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
            if (resultCode == Activity.RESULT_OK && data != null) {
                if (requestCode == SELECT_MEDIA) {
                    Uri mediaUri = data.getData();
                    Glide
                        .with(this)
                        .asBitmap()
                        .centerInside()
                        .load(mediaUri)
                        .into(tempImageBtn);
                    //copy file into tempfile
                    File origFile = new File(getFilePath(this, mediaUri));
                    File newFile = createVideoFile(origFile.getName());
                    copyFile(origFile, newFile);
                    tempMemo.setMediaPath(newFile.getPath());
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
    private void copyFile(File sourceFile, File destFile) throws IOException {
        if (!sourceFile.exists()) {
            return;
        }

        FileChannel source = null;
        FileChannel destination = null;
        source = new FileInputStream(sourceFile).getChannel();
        destination = new FileOutputStream(destFile).getChannel();
        if (destination != null && source != null) {
            destination.transferFrom(source, 0, source.size());
        }
        if (source != null) {
            source.close();
        }
        if (destination != null) {
            destination.close();
        }
    }

    private static String getFilePath(Context context, Uri uri ) {
        String result = null;
        String[] proj = { MediaStore.Images.Media.DATA };
        Cursor cursor = context.getContentResolver( ).query( uri, proj, null, null, null );
        if(cursor != null){
            if ( cursor.moveToFirst( ) ) {
                int column_index = cursor.getColumnIndexOrThrow( proj[0] );
                result = cursor.getString( column_index );
            }
            cursor.close( );
        }
        return result;
    }
    private static final int RECORD_VOICE = 2;
    private EditText tempCaptionField = null;

    public void recordVoice(MemoEntry selectedMemo, EditText selectedCaptionField) {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        if (intent.resolveActivity(getPackageManager()) == null) {
            Toast.makeText(this, getResources().getString(R.string.speech_permission_err), Toast.LENGTH_LONG).show();
            return;
        } else {
            Toast.makeText(this, getResources().getString(R.string.speech_help_msg), Toast.LENGTH_LONG).show();
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