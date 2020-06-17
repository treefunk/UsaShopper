package com.myoptimind.usashopper.features.orderdetail;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
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

import com.bumptech.glide.Glide;
import com.bumptech.glide.GlideBuilder;
import com.bumptech.glide.RequestBuilder;
import com.google.android.material.button.MaterialButtonToggleGroup;
import com.myoptimind.usashopper.R;
import com.myoptimind.usashopper.Utils;
import com.myoptimind.usashopper.api.RequestListener;
import com.myoptimind.usashopper.features.searchorder.SearchFragment;
import com.myoptimind.usashopper.models.Order;
import com.myoptimind.usashopper.models.OrderUpload;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import okhttp3.internal.Util;

import static android.app.Activity.RESULT_OK;

public class OrderFragment extends Fragment{

    private static final String TAG = "OrderFragment";

    private static final String ARGS_ORDER_ID = "args_order_id";

    private static final int REQUEST_IMAGE_CAPTURE = 300;
    public static final int REQUEST_ORDER_STATUS   = 400;

    private String orderId;

    private ConstraintLayout mConstraintLayout;

    private OrderViewModel orderViewModel;

    private File uploaded = null;

    private String fileUpload;

    private int uploadCount = 0;

    private View view;

    private UploadOrderAdapter uploadOrderAdapter;

    private ImageView ivLoading;


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
        view = inflater.inflate(R.layout.fragment_order,container,false);
        mConstraintLayout = view.findViewById(R.id.cl_order);


        orderViewModel = new ViewModelProvider(this).get(OrderViewModel.class);

        initLoadingIcon();

        orderViewModel.initOrder(orderId);

        orderViewModel.getOrder().observe(getViewLifecycleOwner(), new Observer<Order>() {
            @Override
            public void onChanged(Order order) {
                if(order != null){
                    initOrderFields(order);
                    view.findViewById(R.id.group_loading).setVisibility(View.GONE);
                }else{

                }
            }
        });

        orderViewModel.getAlertMessage().observe(getViewLifecycleOwner(), alertMessage -> {
            Toast.makeText(getActivity(),alertMessage,Toast.LENGTH_SHORT).show();
        });

        initOrderUploads();



        return view;
    }

    private void initLoadingIcon() {
        ivLoading = view.findViewById(R.id.iv_loading);
//        ImageView ivMainLoading = view.findViewById(R.id.loading_image);

        RequestBuilder builder = Glide.with(getActivity())
                .load(R.raw.dualball);

        builder.into(ivLoading);
//        builder.into(ivMainLoading);

    }

/*    private void initFab() {
        FloatingActionButton fabMain       = view.findViewById(R.id.fab_main);
        FloatingActionButton fabAddImage   = view.findViewById(R.id.fab_add_image);
        FloatingActionButton fabEditStatus = view.findViewById(R.id.fab_edit_status);
        Group fabMenu                      = view.findViewById(R.id.group_fab);;

        fabMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(fabMenu.getVisibility() != View.VISIBLE){
                    fabMain.animate()
                            .rotation(90 + 45)
                            .setDuration(300);
                    fabAddImage.animate().alpha(1);
                    fabEditStatus.animate().alpha(1);
                    fabMenu.setVisibility(View.VISIBLE);
                }else{
                    fabMain.animate()
                            .rotation(0)
                            .setDuration(300);
                    fabAddImage.animate().alpha(0);
                    fabEditStatus.animate().alpha(0);
                    fabMenu.setVisibility(View.GONE);
                }


            }
        });
    }*/

    private void initOrderFields(Order order) {

        TextView tvLabel       = view.findViewById(R.id.tv_order_label);
        TextView tvNum         = view.findViewById(R.id.tv_order_num);
        TextView tvShopperName = view.findViewById(R.id.tv_order_shopper_name);
        TextView tvRequestDate = view.findViewById(R.id.tv_order_requested_date);
        TextView tvOtherInfo   = view.findViewById(R.id.tv_order_other_info);
        TextView tvQuantity    = view.findViewById(R.id.tv_order_quantity);
        TextView tvTotalPrice  = view.findViewById(R.id.tv_order_price);
        TextView tvOrderStatus = view.findViewById(R.id.tv_order_status);

        tvLabel.setText(order.getLabel());
        tvNum.setText(order.getOrderId());
        tvShopperName.setText(order.getShopperName());
        tvRequestDate.setText(order.getRequestedDate());
        tvOtherInfo.setText(order.getOtherInfo());
        tvQuantity.setText(
                getActivity().getResources().getQuantityString(
                        R.plurals.item_plurals,
                        Integer.parseInt(order.getQuantity()),
                        Integer.parseInt(order.getQuantity())
                )
        );

        tvTotalPrice.setText("₱" + Double.parseDouble(order.getPrice()));

        initAnimOrder();

        orderViewModel.getOrderStatus().observe(getViewLifecycleOwner(), orderStatus -> {
            tvOrderStatus.setText(orderStatus.getLabel());
        });
    }

    private void initAnimOrder() {
        ConstraintSet constraintSet = new ConstraintSet();
        constraintSet.clone(mConstraintLayout);

        constraintSet.connect(
                R.id.card_main_detail,
                ConstraintSet.TOP,
                ConstraintSet.PARENT_ID,
                ConstraintSet.TOP
        );

        constraintSet.clear(
                R.id.card_main_detail,
                ConstraintSet.BOTTOM
        );


        constraintSet.connect(
                R.id.card_status_panel,
                ConstraintSet.BOTTOM,
                ConstraintSet.PARENT_ID,
                ConstraintSet.BOTTOM
        );


        TransitionManager.beginDelayedTransition(mConstraintLayout);

        constraintSet.applyTo(mConstraintLayout);
        view.findViewById(R.id.btn_mark_as_arrived).setVisibility(View.VISIBLE);
    }

    private void initOrderUploads() {

        final RecyclerView rvOrderUploads                = view.findViewById(R.id.rv_upload_orders);
        final Button btnMark                             = view.findViewById(R.id.btn_mark_as_arrived);
        final MaterialButtonToggleGroup btnToggleStatus  = view.findViewById(R.id.toggleButton);

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

                    initAnimUploads();

                    if(orderUploads.size() == 1){
                        btnMark.setEnabled(false);
                        markMessage = "Please upload an image first before proceeding.";
                        btnMark.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Toast.makeText(getActivity(),markMessage,Toast.LENGTH_SHORT);
                            }
                        });
                    }else{
                        btnMark.setEnabled(true);
                        markMessage = "Order successfully marked as arrived.";
                        btnMark.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Log.d(TAG,"Marked as arrived");

                                FragmentManager fm = getActivity().getSupportFragmentManager();
                                DialogStatusFragment dialogStatusFragment = DialogStatusFragment.newInstance();
                                dialogStatusFragment.setTargetFragment(OrderFragment.this,REQUEST_ORDER_STATUS);
                                dialogStatusFragment.show(fm,"DialogStatusFragmnet");

                            }
                        });
                    }




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
                                            OrderUpload orderUpload = orderUploads.get(pos);
                                            orderViewModel.removeUploadedImage(pos,orderUpload.getId(), new RequestListener() {
                                                @Override
                                                public void onRequestStart() {

                                                }
                                                @Override
                                                public void onFinishRequest(Boolean isSuccess) {
                                                    if(isSuccess){
//                                                        orderUploads.remove(pos);
                                                        uploadOrderAdapter.notifyItemRemoved(pos);
                                                        Toast.makeText(getActivity(),"Image Successfully Removed.",Toast.LENGTH_SHORT ).show();
                                                    }
                                                }

                                                @Override
                                                public void onRequestError(String message, int errorCode) {
                                                    Toast.makeText(getActivity(), message,Toast.LENGTH_LONG).show();
                                                }
                                            });

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

    private void initAnimUploads() {
        ConstraintSet constraintSet = new ConstraintSet();

        constraintSet.clone(mConstraintLayout);

        constraintSet.connect(
                R.id.card_images_panel,
                ConstraintSet.TOP,
                R.id.card_main_detail,
                ConstraintSet.BOTTOM
        );

        TransitionManager.beginDelayedTransition(mConstraintLayout);

        constraintSet.applyTo(mConstraintLayout);
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
        }else if(requestCode == REQUEST_ORDER_STATUS && resultCode == RESULT_OK){


            Bundle bundle = data.getExtras();
            String selectedStatusId = bundle.getString(DialogStatusFragment.KEY_STATUS_ID);
            String label            = bundle.getString(DialogStatusFragment.KEY_STATUS_LABEL);



            orderViewModel.updateItemStatus(selectedStatusId, new RequestListener() {
                @Override
                public void onRequestStart() {
                    view.findViewById(R.id.tv_order_status).setVisibility(View.INVISIBLE);
                    view.findViewById(R.id.btn_mark_as_arrived).setEnabled(false);
                    view.findViewById(R.id.btn_mark_as_arrived).setEnabled(false);
                    ivLoading.setVisibility(View.VISIBLE);
                }

                @Override
                public void onFinishRequest(Boolean isSuccess) {
                    view.findViewById(R.id.tv_order_status).setVisibility(View.VISIBLE);
                    view.findViewById(R.id.btn_mark_as_arrived).setEnabled(true);
                    ivLoading.setVisibility(View.GONE);
                    if(isSuccess){
                        Toast.makeText(getActivity(),"Status Successfully Updated.",Toast.LENGTH_SHORT ).show();
                        Intent intent = new Intent();
                        intent.putExtra(SearchFragment.REQUEST_STATUS_LABEL_KEY,label);
                        intent.putExtra(SearchFragment.REQUEST_STATUS_KEY,selectedStatusId);
                        getActivity().setResult(SearchFragment.REQUEST_SINGLE_ORDER,intent);
                    }else{
                        Toast.makeText(getActivity(),"Something went wrong, Please try again.",Toast.LENGTH_SHORT ).show();
                    }
                }

                @Override
                public void onRequestError(String message, int errorCode) {
                    Toast.makeText(getActivity(),message,Toast.LENGTH_LONG).show();
                }
            });

        }
    }


}
