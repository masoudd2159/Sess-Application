package ir.ac.sku.www.sessapplication.api;

public interface RxApiCallback<T> {

    void onSuccess(T t);

    void onFailed(String errorMsg);
}