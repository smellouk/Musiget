package io.mellouk.repository.disk;

import android.database.Cursor;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;

import io.mellouk.repository.entity.MusicEntity;
import io.reactivex.Single;

public class MusicFetcherImpl implements MusicFetcher {
    @NonNull
    private final Cursor cursor;

    public MusicFetcherImpl(@NonNull final MusicContentCursorProvider contentCursorProvider) {
        this.cursor = contentCursorProvider.getCursor();
    }

    @NonNull
    public Single<List<MusicEntity>> getMusic() {
        return Single.create(
                emitter -> {
                    final ArrayList<MusicEntity> musicList = new ArrayList<>();
                    try {
                        while (cursor.moveToNext()) {
                            musicList.add(map(cursor));
                        }
                    } catch (final Exception e) {
                        emitter.onError(e);
                    }
                    emitter.onSuccess(musicList);
                }
        );
    }

    private MusicEntity map(@NonNull final Cursor cursor) {
        final int id = cursor.getInt(MusicContentCursorProvider.ID);
        final String title = cursor.getString(MusicContentCursorProvider.TITLE);
        final String album = cursor.getString(MusicContentCursorProvider.ALBUM);
        final String artist = cursor.getString(MusicContentCursorProvider.ARTIST);
        final String path = cursor.getString(MusicContentCursorProvider.DATA);

        return new MusicEntity(id, title, album, artist, path);
    }
}
