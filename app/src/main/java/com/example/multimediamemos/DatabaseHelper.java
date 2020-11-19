package com.example.multimediamemos;

import android.content.ContentValues;
import android.content.Context;
import android.content.res.Resources;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {
    private final String MEMO_TABLE;
    private final String ID_COL;
    private final String MEDIA_PATH_COL;
    private final String VOICE_PATH_COL;
    private final String CAPTION_COL;

    public DatabaseHelper(Context context) {
        super(context, context.getResources().getString(R.string.DB_NAME), null, context.getResources().getInteger(R.integer.DB_VERSION));
        Resources res = context.getResources();
        MEMO_TABLE = res.getString(R.string.MEMO_TABLE);
        ID_COL = res.getString(R.string.id);
        MEDIA_PATH_COL = res.getString(R.string.media_path);
        VOICE_PATH_COL = res.getString(R.string.voice_path);
        CAPTION_COL = res.getString(R.string.caption);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_MEMO_TABLE =
        "CREATE TABLE " + MEMO_TABLE
        + "("
            + ID_COL + " INTEGER PRIMARY KEY,"
            + MEDIA_PATH_COL + " TEXT,"
            + VOICE_PATH_COL + " TEXT,"
            + CAPTION_COL + " TEXT,"
        + ")";
        db.execSQL(CREATE_MEMO_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + MEMO_TABLE);
        onCreate(db);
    }

    public void insertMemo(MemoEntry memo) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(MEDIA_PATH_COL, memo.getMediaPath()); // Contact Name
        values.put(VOICE_PATH_COL, memo.getVoiceRecordPath()); // Contact Phone
        values.put(CAPTION_COL, memo.getCaption()); // Contact Phone
        db.insert(MEMO_TABLE, null, values);
        db.close();
    }

}
