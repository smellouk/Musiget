package io.mellouk.repository.entity;

import androidx.annotation.Nullable;

public class MusicEntity {
    private final int id;

    @Nullable
    private final String title;
    @Nullable
    private final String album;
    @Nullable
    private final String artist;
    @Nullable
    private final String path;

    public MusicEntity(final int id,
                       @Nullable final String title,
                       @Nullable final String album,
                       @Nullable final String artist,
                       @Nullable final String path
    ) {
        this.id = id;
        this.title = title;
        this.album = album;
        this.artist = artist;
        this.path = path;
    }

    public int getId() {
        return id;
    }

    @Nullable
    public String getTitle() {
        return title;
    }

    @Nullable
    public String getAlbum() {
        return album;
    }

    @Nullable
    public String getArtist() {
        return artist;
    }

    @Nullable
    public String getPath() {
        return path;
    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + safeHashCode(title);
        result = 31 * result + safeHashCode(album);
        result = 31 * result + safeHashCode(artist);
        result = 31 * result + safeHashCode(path);
        return result;
    }

    private int safeHashCode(@Nullable final String value) {
        return value == null ? 1 : value.hashCode();
    }
}
