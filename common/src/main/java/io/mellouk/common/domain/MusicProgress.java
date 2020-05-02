package io.mellouk.common.domain;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

public class MusicProgress implements Parcelable {
    public static final Parcelable.Creator<MusicProgress> CREATOR = new Parcelable.Creator<MusicProgress>() {
        @Override
        public MusicProgress createFromParcel(final Parcel source) {
            return new MusicProgress(source);
        }

        @Override
        public MusicProgress[] newArray(final int size) {
            return new MusicProgress[size];
        }
    };
    @NonNull
    private final String currentPosition;
    @NonNull
    private final String duration;
    private final int progress;

    public MusicProgress(@NonNull final String currentPosition, @NonNull final String duration, final int progress) {
        this.currentPosition = currentPosition;
        this.duration = duration;
        this.progress = progress;
    }

    protected MusicProgress(final Parcel in) {
        this.currentPosition = in.readString();
        this.duration = in.readString();
        this.progress = in.readInt();
    }

    @NonNull
    public String getCurrentPosition() {
        return currentPosition;
    }

    @NonNull
    public String getDuration() {
        return duration;
    }

    public int getProgress() {
        return progress;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(final Parcel dest, final int flags) {
        dest.writeString(this.currentPosition);
        dest.writeString(this.duration);
        dest.writeInt(this.progress);
    }
}
