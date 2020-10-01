package ir.ac.sku.www.sessapplication.api;

import android.annotation.SuppressLint;
import android.util.Log;

import rx.Observable;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

public class RxApiCallHelper {

    public static <T> Subscription call(Observable<T> observable, final RxApiCallback<T> rxApiCallback) {
        if (observable == null) {
            throw new IllegalArgumentException("Observable must not be null.");
        }

        if (rxApiCallback == null) {
            throw new IllegalArgumentException("Callback must not be null.");
        }

        return observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
//                .unsubscribeOn(Schedulers.io())
                .subscribe(new Action1<T>() {
                    @Override
                    public void call(T t) {
                        rxApiCallback.onSuccess(t);
                    }
                }, new Action1<Throwable>() {
                    @SuppressLint("LongLogTag") @Override
                    public void call(Throwable throwable) {
                        rxApiCallback.onFailed(throwable.getMessage());
                        Log.i(MyLog.SESS, throwable.getMessage());
                    }
                });
    }
}
