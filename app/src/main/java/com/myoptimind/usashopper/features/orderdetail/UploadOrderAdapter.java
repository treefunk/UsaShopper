package com.myoptimind.usashopper.features.orderdetail;

import android.app.Application;
import android.content.Intent;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.myoptimind.usashopper.R;
import com.myoptimind.usashopper.models.OrderUpload;

import java.util.List;

import timber.log.Timber;

public class UploadOrderAdapter extends RecyclerView.Adapter {

    private static final String TAG = "UploadOrderAdapter";
    private UploadOrderListener mUploadOrderListener;

    private int rvWidth;

    public void setRvWidth(int rvWidth) {
        this.rvWidth = rvWidth;
    }

    public void setUploadOrderListener(UploadOrderListener uploadOrderListener) {
        mUploadOrderListener = uploadOrderListener;
    }

    private static final int WITH_IMAGE = 100;
    private static final int ADD_IMAGE  = 200;

    private List<OrderUpload> mUploads;


    public UploadOrderAdapter(List<OrderUpload> uploads) {
        mUploads = uploads;
    }


    @Override
    public int getItemViewType(int position) {
        if(mUploads.get(position) != null){
            return WITH_IMAGE;
        }
        return ADD_IMAGE;
    }

    /**
     *  WITH IMAGE
     */
    public class UploadHolder extends RecyclerView.ViewHolder{

        private ImageView ivUploadedImage;
        private ImageButton ibRemove;
        private UploadOrderListener mUploadOrderListener;


        public UploadHolder(@NonNull View itemView, UploadOrderListener uploadOrderListener) {
            super(itemView);
            mUploadOrderListener = uploadOrderListener;
            ivUploadedImage = itemView.findViewById(R.id.iv_uploaded_image);
            ibRemove        = itemView.findViewById(R.id.ib_remove);

            ivUploadedImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    uploadOrderListener.onClickImage(getAdapterPosition());
                }
            });
            ibRemove.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    uploadOrderListener.onClickRemove(getAdapterPosition());
                }
            });
        }

        void bind(OrderUpload orderUpload, int position){
            Glide.with(itemView.getContext())
                    .load(
                            orderUpload.getImage().isEmpty() ? orderUpload.getBitmap() : orderUpload.getImage()
                    )
                    .into(ivUploadedImage);
        }
    }

    /**
     *  PLUS ICON (ADD IMAGE)
     */
    public class UploadNewHolder extends RecyclerView.ViewHolder{

        private Button btnAddNew;
        private UploadOrderListener mUploadOrderListener;

        public UploadNewHolder(@NonNull View itemView, UploadOrderListener uploadOrderListener) {
            super(itemView);
            mUploadOrderListener = uploadOrderListener;

            btnAddNew = itemView.findViewById(R.id.btn_upload_new);

            btnAddNew.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    uploadOrderListener.onClickUpload(getAdapterPosition());
                }
            });
        }
    }



    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        if(viewType == WITH_IMAGE){
            return new UploadHolder(inflater.inflate(R.layout.list_item_upload,parent,false),mUploadOrderListener);
        }
        return new UploadNewHolder(inflater.inflate(R.layout.list_item_upload_new,parent,false),mUploadOrderListener);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        OrderUpload orderUpload = mUploads.get(position);

        if(orderUpload != null){
            ((UploadHolder) holder).bind(orderUpload,position);
        }

        if(orderUpload == null && position == 0){
            ((UploadNewHolder) holder).btnAddNew.setText("Add Image");
        }

    }

    @Override
    public int getItemCount() {
        return mUploads.size();
    }
}
