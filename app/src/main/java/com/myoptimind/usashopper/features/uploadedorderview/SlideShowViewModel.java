package com.myoptimind.usashopper.features.uploadedorderview;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.myoptimind.usashopper.Utils;
import com.myoptimind.usashopper.features.shared.SingleLiveEvent;
import com.myoptimind.usashopper.models.OrderUpload;
import com.myoptimind.usashopper.repositories.OrderRepository;

import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

public class SlideShowViewModel extends ViewModel {

    private CompositeDisposable mDisposable = new CompositeDisposable();
    private MutableLiveData<String> mDescription = new MutableLiveData<>();
    private SingleLiveEvent<String> mEditSuccess = new SingleLiveEvent<>();
    private MutableLiveData<Boolean> isUpdatingDescription = new MutableLiveData<>();
    private OrderRepository mOrderRepository;

    public SlideShowViewModel() {
        mOrderRepository = new OrderRepository();
        isUpdatingDescription.setValue(false);
    }

    public LiveData<String> getEditSuccess() {
        return mEditSuccess;
    }

    public void updateDescription(String id, String description){
        if(isUpdatingDescription.getValue() != true){
            isUpdatingDescription.setValue(true);
            mDisposable.add(
                    mOrderRepository.updateDescription(
                            id,description
                    ).subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(orderUploadResponse -> {
                                if(orderUploadResponse.getMeta().getStatus().equals("200")){
                                    String caption = orderUploadResponse.getData().getCaption();
                                    mEditSuccess.setValue(orderUploadResponse.getMeta().getMessage());
                                    mDescription.setValue(caption);
                                }
                                isUpdatingDescription.setValue(false);
                            }, e -> {
                                mEditSuccess.setValue(Utils.handleRequestExceptions(e));
                                refreshDescription();
                                isUpdatingDescription.setValue(false);
                            })
            );
        }
    }

    public LiveData<String> getDescription() {
        return mDescription;
    }

    public void setDescription(String description) {
        mDescription.setValue(description);
    }

    public LiveData<Boolean> getIsUpdatingDescription() {
        return isUpdatingDescription;
    }

    public void refreshDescription(){
        mDescription.setValue(mDescription.getValue());
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        mDisposable.clear();
    }
}
