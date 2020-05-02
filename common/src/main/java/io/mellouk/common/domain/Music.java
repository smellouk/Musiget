package io.mellouk.common.domain;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

public class Music implements Parcelable {
    public static final Parcelable.Creator<Music> CREATOR = new Parcelable.Creator<Music>() {
        @Override
        public Music createFromParcel(final Parcel source) {
            return new Music(source);
        }

        @Override
        public Music[] newArray(final int size) {
            return new Music[size];
        }
    };
    private final int id;
    @NonNull
    private final String title;
    @NonNull
    private final String album;
    @NonNull
    private final String artist;
    @NonNull
    private final String path;

    public Music(final int id,
                 @NonNull final String title,
                 @NonNull final String album,
                 @NonNull final String artist,
                 @NonNull final String path
    ) {
        this.id = id;
        this.title = title;
        this.album = album;
        this.artist = artist;
        this.path = path;
    }

    protected Music(final Parcel in) {
        this.id = in.readInt();
        this.title = in.readString();
        this.album = in.readString();
        this.artist = in.readString();
        this.path = in.readString();
    }

    public int getId() {
        return id;
    }

    @NonNull
    public String getTitle() {
        return title;
    }

    @NonNull
    public String getAlbum() {
        return album;
    }

    @NonNull
    public String getArtist() {
        return artist;
    }

    @NonNull
    public String getPath() {
        return path;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(final Parcel dest, final int flags) {
        dest.writeInt(this.id);
        dest.writeString(this.title);
        dest.writeString(this.album);
        dest.writeString(this.artist);
        dest.writeString(this.path);
    }
}
