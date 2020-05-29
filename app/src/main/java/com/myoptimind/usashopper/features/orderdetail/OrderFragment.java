package com.myoptimind.usashopper.features.orderdetail;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.transition.TransitionManager;

import com.google.android.material.button.MaterialButtonToggleGroup;
import com.myoptimind.usashopper.R;
import com.myoptimind.usashopper.models.OrderUpload;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import io.reactivex.Flowable;

import static android.app.Activity.RESULT_OK;

public class OrderFragment extends Fragment{

    private static final String TAG = "OrderFragment";

    private static final String ARGS_ORDER_ID = "args_order_id";
    private static final int REQUEST_IMAGE_CAPTURE = 300;

    private String orderId;

    private ConstraintLayout mConstraintLayout;

    private OrderViewModel orderViewModel;

    private File uploaded = null;

    private String fileUpload;

    private int uploadCount = 0;

    private UploadOrderAdapter uploadOrderAdapter;


    public static OrderFragment newInstance(String orderId) {

        Bundle args = new Bundle();

        OrderFragment fragment = new OrderFragment();
        args.putString(ARGS_ORDER_ID,orderId);
        fragment.setArguments(args);
        return fragment;

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        orderId = getArguments().getString(ARGS_ORDER_ID);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_order,container,false);

        orderViewModel = new ViewModelProvider(this).get(OrderViewModel.class);

        orderViewModel.initOrder(orderId);

        final RecyclerView rvOrderUploads                = view.findViewById(R.id.rv_upload_orders);
        final Button btnMark                             = view.findViewById(R.id.btn_mark_as_arrived);
        final MaterialButtonToggleGroup btnToggleStatus  = view.findViewById(R.id.toggleButton);

        initOrderUploads(
                rvOrderUploads,
                btnMark,
                btnToggleStatus
        );

        mConstraintLayout = (ConstraintLayout) view;


        return view;
    }

    private void initOrderUploads(
            final RecyclerView rvOrderUploads,
            final Button btnMark,
            final MaterialButtonToggleGroup btnToggleStatus) {

        // set grid layout
        rvOrderUploads.setLayoutManager(new GridLayoutManager(getActivity(),2));

        uploadOrderAdapter = new UploadOrderAdapter(new ArrayList<>());
        rvOrderUploads.setAdapter(uploadOrderAdapter);

        // observe order uploads
        orderViewModel.getOrderUploads().observe(getViewLifecycleOwner(), new Observer<List<OrderUpload>>() {
            @Override
            public void onChanged(List<OrderUpload> orderUploads) {

                if(orderUploads != null){

                    String markMessage;

                    if(orderUploads.size() == 1){
                        markMessage = "Please upload an image first before proceeding.";
                    }else{
                        markMessage = "Order successfully marked as arrived.";
                    }

                    btnMark.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Log.d(TAG,"Marked as arrived");

                            FragmentManager fm = getActivity().getSupportFragmentManager();
                            MarkDialogFragment markDialogFragment = new MarkDialogFragment();
                            markDialogFragment.show(fm,"t");


                        }
                    });


                    uploadOrderAdapter.setUploads(orderUploads);

                    uploadOrderAdapter.setUploadOrderListener(new UploadOrderListener() {
                        @Override
                        public void onClickUpload(int pos) {
                            Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

                            if(takePictureIntent.resolveActivity(getActivity().getPackageManager()) != null){

                                uploaded = null;
                                try {
                                    uploaded = createImageFile();
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }

                                if(uploaded != null){

                                    Uri uploadedUri = FileProvider.getUriForFile(
                                            getActivity(),
                                            "com.myoptimind.usashopper.fileprovider",
                                            uploaded
                                    );

                                    takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, uploadedUri);
                                    startActivityForResult(takePictureIntent,REQUEST_IMAGE_CAPTURE);

                                }

                            }
                        }

                        @Override
                        public void onClickImage(int pos) {
                            Intent intent = SlideShowActivity.createIntent(
                                    getActivity(),
                                    (ArrayList<OrderUpload>)orderUploads,
                                    pos
                            );
                            startActivity(intent);
                        }

                        @Override
                        public void onClickRemove(int pos) {
                            new AlertDialog.Builder(getActivity())
                                    .setMessage("Are you sure you want to remove this image?")
                                    .setPositiveButton("Remove", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            orderUploads.remove(pos);
                                            uploadOrderAdapter.notifyItemRemoved(pos);
                                        }
                                    }).setNegativeButton("Cancel",null)
                                    .show();

                        }
                    });



                    if((orderUploads.size() - 1) - uploadCount == 1){
                        uploadOrderAdapter.notifyItemChanged(orderUploads.size() - 1);
                        uploadOrderAdapter.notifyItemChanged(orderUploads.size());
                    }else{
                        uploadOrderAdapter.notifyDataSetChanged();
                    }

                    uploadCount = orderUploads.size() - 1;

                }


            }
        });

        orderViewModel.getUploaded().observe(getViewLifecycleOwner(), new Observer<File>() {
            @Override
            public void onChanged(File file) {
                if(file != null){
                    orderViewModel.doUploadRequest(file);
                }
            }
        });

    }

    private File createImageFile() throws IOException {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName =  timeStamp + ".jpg";

        File storageDir = new File(getActivity().getFilesDir(),"uploaded");
        if(!storageDir.exists()){
            storageDir.mkdirs();
        }
        File image = new File(
                storageDir,
                imageFileName
        );
        fileUpload = image.getAbsolutePath();
        return image;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK){
            if(uploaded != null){
                orderViewModel.setUploaded(uploaded);
            }
        }
    }


}
