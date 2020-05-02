package io.mellouk.view.domain;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import javax.inject.Inject;

import io.mellouk.common.domain.Music;
import io.mellouk.repository.entity.MusicEntity;
import io.mellouk.view.di.MainScope;

@MainScope
public class MusicMapper {

    private static final String DEFAULT_STRING = "";

    @Inject
    MusicMapper() {
    }

    Music map(@NonNull final MusicEntity entity) {
        return new Music(
                entity.getId(),
                nonNullString(entity.getTitle()),
                nonNullString(entity.getAlbum()),
                nonNullString(entity.getArtist()),
                nonNullString(entity.getPath())
        );
    }

    @NonNull
    private String nonNullString(@Nullable final String value) {
        return value == null ? DEFAULT_STRING : value;
    }
}
