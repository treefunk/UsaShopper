package com.myoptimind.usashopper.features.searchorder;

import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.myoptimind.usashopper.api.OrderService;
import com.myoptimind.usashopper.models.Order;
import com.myoptimind.usashopper.repositories.OrderRepository;

import org.reactivestreams.Publisher;

import java.util.Iterator;
import java.util.List;
import java.util.concurrent.Callable;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

public class SearchViewModel extends ViewModel {

    private static final String TAG = "SearchViewModel";

    OrderRepository mOrderRepository;
    private MutableLiveData<List<Order>> mOrders = new MutableLiveData<>();
    private MutableLiveData<Boolean> isFetchingOrders = new MutableLiveData<>();

    private CompositeDisposable mCompositeDisposable = new CompositeDisposable();


    public SearchViewModel() {
        mOrderRepository = new OrderRepository();
        isFetchingOrders.setValue(false);
    }

    public void getOrders(String keyword){
//        orders = mOrderRepository.getOrders();
        isFetchingOrders.setValue(true);
        mCompositeDisposable.add(mOrderRepository.getOrderList()
                .map(new Function<OrderService.OrderResponse, List<Order>>() {
                    @Override
                    public List<Order> apply(OrderService.OrderResponse orderResponse) throws Exception {
                        return orderResponse.getResults();
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<List<Order>>() {
                    @Override
                    public void accept(List<Order> orders) throws Exception {
                        mOrders.postValue(orders);
                        isFetchingOrders.postValue(false);
                    }

                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        Log.e(TAG,"error: " + throwable.getMessage());
                        isFetchingOrders.postValue(false);
                    }
                }));
    }

    public LiveData<List<Order>> getOrders() {
        return mOrders;
    }

    public LiveData<Boolean> getIsFetchingOrders() {
        return isFetchingOrders;
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        mCompositeDisposable.clear();
    }
}
