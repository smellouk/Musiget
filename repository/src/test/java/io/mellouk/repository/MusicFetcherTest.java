package io.mellouk.repository;

import android.database.Cursor;

import androidx.annotation.NonNull;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import io.mellouk.repository.disk.MusicContentCursorProvider;
import io.mellouk.repository.disk.MusicFetcherImpl;

import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.atLeast;
import static org.mockito.Mockito.when;

public class MusicFetcherTest extends BaseTest {
    private static final int ID = 1;
    private static final String TITLE = "TITLE";
    private static final String ALBUM = "ALBUM";
    private static final String ARTIST = "ARTIST";
    private static final String DATA = "DATA";

    @Mock
    Cursor cursor;

    @Mock
    MusicContentCursorProvider cursorProvider;

    private int givenHashCode;
    private MusicFetcherImpl musicFetcher;

    @Before
    @Override
    public void setup() {
        super.setup();
        when(cursorProvider.getCursor()).thenReturn(cursor);
        musicFetcher = new MusicFetcherImpl(cursorProvider);
        createHash();
    }

    @Test
    public void getMusic_ShouldExtractMusicFromCursor() {
        givenCursor(cursorProvider);

        musicFetcher.getMusic()
                .test()
                .assertComplete()
                .assertValueCount(1)
                .assertValue(value -> value.size() == 1 && value.get(0).hashCode() == givenHashCode);

        then(cursor).should(atLeast(1)).moveToNext();
        then(cursor).should(once).getInt(MusicContentCursorProvider.ID);
        then(cursor).should(once).getString(MusicContentCursorProvider.TITLE);
        then(cursor).should(once).getString(MusicContentCursorProvider.ALBUM);
        then(cursor).should(once).getString(MusicContentCursorProvider.ARTIST);
        then(cursor).should(once).getString(MusicContentCursorProvider.DATA);
    }

    private void givenCursor(@NonNull final MusicContentCursorProvider cursorProvider) {
        when(cursor.moveToNext()).thenReturn(true).thenReturn(false);
        when(cursor.getInt(MusicContentCursorProvider.ID)).thenReturn(ID);
        when(cursor.getString(MusicContentCursorProvider.TITLE)).thenReturn(TITLE);
        when(cursor.getString(MusicContentCursorProvider.ALBUM)).thenReturn(ALBUM);
        when(cursor.getString(MusicContentCursorProvider.ARTIST)).thenReturn(ARTIST);
        when(cursor.getString(MusicContentCursorProvider.DATA)).thenReturn(DATA);
    }

    private void createHash() {
        givenHashCode = ID;
        givenHashCode = 31 * givenHashCode + TITLE.hashCode();
        givenHashCode = 31 * givenHashCode + ALBUM.hashCode();
        givenHashCode = 31 * givenHashCode + ARTIST.hashCode();
        givenHashCode = 31 * givenHashCode + DATA.hashCode();
    }
}
