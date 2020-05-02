package io.mellouk.common.base;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public abstract class BaseActivityViewModel<State extends BaseViewState> extends ViewModel {
    final MutableLiveData<State> stateObserver = new MutableLiveData<>();

    private final CompositeDisposable compositeDisposable = new CompositeDisposable();

    public BaseActivityViewModel() {
        stateObserver.setValue(getInitialState());
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

    @Override
    public void onCleared() {
        compositeDisposable.clear();
        super.onCleared();
    }

    public abstract State getInitialState();
}
