package com.myoptimind.usashopper.features.orderdetail;

import android.app.Application;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

public class UploadOrderAdapter extends RecyclerView.Adapter {

    private static final String TAG = "UploadOrderAdapter";

    private int rvWidth;

    public void setRvWidth(int rvWidth) {
        this.rvWidth = rvWidth;
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

        public UploadHolder(@NonNull View itemView) {
            super(itemView);
            ivUploadedImage = itemView.findViewById(R.id.iv_uploaded_image);
        }

        void bind(OrderUpload orderUpload, int position){
            Glide.with(itemView.getContext())
                    .load(orderUpload.getImage())
                    .into(ivUploadedImage);
        }
    }

    /**
     *  PLUS ICON (ADD IMAGE)
     */
    public class UploadNewHolder extends RecyclerView.ViewHolder{

        private ImageButton ibAddNew;

        public UploadNewHolder(@NonNull View itemView) {

            super(itemView);

            ibAddNew = itemView.findViewById(R.id.ib_upload_new);

            ibAddNew.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });
        }
    }



    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        if(viewType == WITH_IMAGE){
            return new UploadHolder(inflater.inflate(R.layout.list_item_upload,parent,false));
        }
        return new UploadNewHolder(inflater.inflate(R.layout.list_item_upload_new,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return mUploads.size();
    }
}
