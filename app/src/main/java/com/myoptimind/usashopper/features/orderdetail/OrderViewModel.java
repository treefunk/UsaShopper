package com.myoptimind.usashopper.features.orderdetail;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;

import com.myoptimind.usashopper.api.ErrorResponse;
import com.myoptimind.usashopper.api.OrderService;
import com.myoptimind.usashopper.api.UsaShopperApi;
import com.myoptimind.usashopper.models.Order;
import com.myoptimind.usashopper.models.OrderUpload;
import com.myoptimind.usashopper.repositories.OrderRepository;

import java.io.File;
import java.util.List;

import io.reactivex.Scheduler;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.HttpException;

public class OrderViewModel extends ViewModel {

    private static final String TAG = "OrderViewModel";

    private MutableLiveData<List<OrderUpload>> mOrderUploads = new MutableLiveData<>();
    private MutableLiveData<Order> mOrder = new MutableLiveData<>();
    private MutableLiveData<File> mUploaded = new MutableLiveData<>();
    private OrderRepository mOrderRepository;
    private CompositeDisposable mDisposable = new CompositeDisposable();

    public OrderViewModel() {
        mOrderRepository = new OrderRepository();
    }

    public void initOrder(String orderId) {
        if(orderId == null){
            return;
        }
        mDisposable.add(
                mOrderRepository.getOrder(orderId)
                .map(new Function<OrderService.OrderResponse, Order>() {
                    @Override
                    public Order apply(OrderService.OrderResponse orderResponse) throws Exception {
                        return orderResponse.getData();
                    }
                }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Order>() {
                    @Override
                    public void accept(Order order) throws Exception {
                        mOrder.setValue(order);

                        List<OrderUpload> uploads = order.getUploads();
                        uploads.add(null);

                        mOrderUploads.setValue(order.getUploads());
                    }
                })
        );

    }

    public void setUploaded(File uploaded) {
        mUploaded.setValue(uploaded);
    }

    public void doUploadRequest(File file){

        RequestBody requestBody = RequestBody.create(MediaType.parse("image/*"), file);
        MultipartBody.Part multipart = MultipartBody.Part.createFormData(
                "image_url",
                file.getName(),
                requestBody
        );

        mDisposable.add(mOrderRepository.uploadOrderImage(
                mOrder.getValue().getId(),
                multipart
        ).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<OrderService.OrderUploadResponse>() {
                    @Override
                    public void accept(OrderService.OrderUploadResponse orderUploadResponse) throws Exception {
                        List<OrderUpload> uploads = mOrderUploads.getValue();
                        uploads.remove(uploads.size() - 1);
                        uploads.add(orderUploadResponse.getData());
                        uploads.add(null);
                        mOrderUploads.setValue(uploads);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        Log.d(TAG,throwable.getMessage());
                        HttpException e = (HttpException) throwable;
                        ErrorResponse errorResponse = UsaShopperApi.getConverter().convert(e.response().errorBody());
                        Log.d(TAG,errorResponse.getMeta().getMessage());
                    }
                })
        );




    }

    public LiveData<File> getUploaded() {
        return mUploaded;
    }

    public LiveData<List<OrderUpload>> getOrderUploads() {
        return mOrderUploads;
    }

    public LiveData<Order> getOrder() {
        return mOrder;
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        mDisposable.clear();
    }
}
