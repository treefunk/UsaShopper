package com.myoptimind.usashopper.features.orderdetail;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.myoptimind.usashopper.api.ErrorResponse;
import com.myoptimind.usashopper.api.OrderService;
import com.myoptimind.usashopper.api.RequestListener;
import com.myoptimind.usashopper.api.UsaShopperApi;
import com.myoptimind.usashopper.models.Order;
import com.myoptimind.usashopper.models.OrderStatus;
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

    private MutableLiveData<Order> mOrder                    = new MutableLiveData<>();
    private MutableLiveData<List<OrderUpload>> mOrderUploads = new MutableLiveData<>();
    private MutableLiveData<OrderStatus> mOrderStatus        = new MutableLiveData<>();
    private MutableLiveData<List<OrderStatus>> mStatusList   = new MutableLiveData<>();

    private MutableLiveData<File> mUploaded                  = new MutableLiveData<>();


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

                        // Order fields
                        mOrder.setValue(order);

                        // Order Uploads
                        List<OrderUpload> uploads = order.getUploads();
                        uploads.add(null);
                        mOrderUploads.setValue(order.getUploads());

                        // Order status
                        OrderStatus orderStatus = new OrderStatus();
                        orderStatus.setLabel(order.getFormattedStatus());
                        orderStatus.setId(order.getStatus());
                        mOrderStatus.setValue(orderStatus);

                        // Order - status list selection available for this item
                        mStatusList.setValue(order.getStatusList());

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
//                        getUploaded().getValue().delete();
//                        setUploaded(null);
                        uploads.remove(uploads.size() - 1);
                        uploads.add(orderUploadResponse.getData());
                        uploads.add(null);
                        mOrderUploads.setValue(uploads);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        Log.d(TAG,throwable.getMessage());
//                        getUploaded().getValue().delete();
//                        setUploaded(null);
                        HttpException e = (HttpException) throwable;
                        ErrorResponse errorResponse = UsaShopperApi.getConverter().convert(e.response().errorBody());
                        Log.d(TAG,errorResponse.getMeta().getMessage());
                    }
                })
        );
    }

    public void updateItemStatus(String statusId, RequestListener requestListener){
        requestListener.onRequestStart();
        mDisposable.add(
                mOrderRepository.updateItemStatus(
                        mOrder.getValue().getId(),
                        statusId
                ).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<OrderService.OrderResponse>() {
                    @Override
                    public void accept(OrderService.OrderResponse orderResponse) throws Exception {
                        if(orderResponse.getMeta().getStatus().equals("200")){
                            OrderStatus orderStatus = new OrderStatus();
                            orderStatus.setLabel(orderResponse.getData().getFormattedStatus());
                            orderStatus.setId(orderResponse.getData().getStatus());
                            mOrderStatus.setValue(orderStatus);
                            requestListener.onFinishRequest(true);
                        }else{
                            requestListener.onFinishRequest(false);
                        }
                    }
                })
        );
    }

    public void removeUploadedImage(int pos, String imageId, RequestListener requestListener){
        requestListener.onRequestStart();

        if(mOrderUploads != null && mOrderUploads.getValue().size() == 2){
            if(!getOrderStatus().getValue().getId().equals("0")){
                requestListener.onFinishRequest(false);
                return;
            }
        }

        mDisposable.add(
                mOrderRepository.removeUploadedImage(imageId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<OrderService.OrderRemoveUploadResponse>() {
                    @Override
                    public void accept(OrderService.OrderRemoveUploadResponse orderRemoveUploadResponse) throws Exception {
                        if(orderRemoveUploadResponse.getMeta().getStatus().equals("200")){
                            List<OrderUpload> orderUploads = mOrderUploads.getValue();
                            orderUploads.remove(pos);
                            if(orderUploads.size() == 1){

                            }
                            mOrderUploads.setValue(orderUploads);
                            requestListener.onFinishRequest(true);
                        }else{
                            requestListener.onFinishRequest(false);
                        }
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

    public LiveData<List<OrderStatus>> getStatusList() {
        return mStatusList;
    }

    public LiveData<OrderStatus> getOrderStatus() {
        return mOrderStatus;
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        mDisposable.clear();
    }
}
