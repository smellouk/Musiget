package io.mellouk.common.base;

import androidx.annotation.NonNull;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subjects.BehaviorSubject;

public abstract class BaseServiceViewModel<State extends BaseViewState> {
    protected final BehaviorSubject<State> stateObserver = BehaviorSubject.create();

    private final CompositeDisposable compositeDisposable = new CompositeDisposable();

    public BaseServiceViewModel() {
        stateObserver.onNext(getInitialState());
    }

    public <T> void addObservable(@NonNull final Observable<T> source,
                                  @NonNull final Consumer<T> onSuccess,
                                  @NonNull final Consumer<Throwable> onError
    ) {
        compositeDisposable.add(source.observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(onSuccess, onError)
        );
    }

    public void onCleared() {
        compositeDisposable.clear();
    }

    public abstract State getInitialState();
}
