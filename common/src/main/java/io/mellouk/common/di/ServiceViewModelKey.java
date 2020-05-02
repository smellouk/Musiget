package io.mellouk.common.di;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import dagger.MapKey;
import io.mellouk.common.base.BaseServiceViewModel;

@MapKey
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface ServiceViewModelKey {
    Class<? extends BaseServiceViewModel> value();
}
