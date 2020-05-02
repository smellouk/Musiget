package io.mellouk.common.base;


import io.reactivex.Observable;

public interface BaseUseCase<DataState extends BaseDataState> {
    Observable<DataState> buildObservable();
}