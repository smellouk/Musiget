package io.mellouk.repository.disk;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.BaseColumns;
import android.provider.MediaStore;

import androidx.annotation.NonNull;

public class MusicContentCursorProvider {
    public static final int ID = 0;
    public static final int TITLE = 1;
    public static final int ALBUM = 2;
    public static final int ARTIST = 3;
    public static final int DATA = 4;

    private static final Uri EXTERNAL_CONTENT_URI = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
    private static final String[] PROJECTION = {
            BaseColumns._ID,
            MediaStore.Audio.AudioColumns.TITLE,
            MediaStore.Audio.AudioColumns.ALBUM,
            MediaStore.Audio.AudioColumns.ARTIST,
            MediaStore.Audio.AudioColumns.DATA
    };
    private static final String MUSIC_QUERY = MediaStore.Audio.Media.IS_MUSIC + "!= 0";
    private static final String SORT_ORDER = MediaStore.Audio.Media.DEFAULT_SORT_ORDER;

    private final Context context;

    public MusicContentCursorProvider(@NonNull final Context context) {
        this.context = context;
    }

    public Cursor getCursor() {
        return context.getContentResolver().query(
                EXTERNAL_CONTENT_URI,
                PROJECTION,
                MUSIC_QUERY,
                null,
                SORT_ORDER
        );
    }
}
