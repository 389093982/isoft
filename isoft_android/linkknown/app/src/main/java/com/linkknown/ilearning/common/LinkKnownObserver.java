package com.linkknown.ilearning.common;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

public abstract class LinkKnownObserver<T> implements Observer<T> {

    @Override
    public void onSubscribe(Disposable d) {

    }

    @Override
    public abstract void onNext(T o);

    @Override
    public abstract void onError(Throwable e);

    @Override
    public void onComplete() {

    }
}
