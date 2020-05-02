package io.mellouk.common.utils;

import io.reactivex.Maybe;

public class RxUtils {
    public static <T> Maybe<T> ofNullable(final T value) {
        return value == null ? Maybe.empty() : Maybe.just(value);
    }
}
