package com.myoptimind.usashopper.features.orderdetail;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.github.chrisbanes.photoview.PhotoView;
import com.myoptimind.usashopper.R;
import com.myoptimind.usashopper.models.OrderUpload;

import java.util.ArrayList;

public class SlideShowFragment extends Fragment {

    ArrayList<OrderUpload> mOrderUploads;
    int position;

    public static final String ARGS_IMAGES = "ARGS_IMAGES";
    public static final String ARGS_POSITION = "ARGS_POSITION";

    public static SlideShowFragment newInstance(ArrayList<OrderUpload> orderUploads, int position) {

        Bundle args = new Bundle();

        SlideShowFragment fragment = new SlideShowFragment();

        args.putParcelableArrayList(ARGS_IMAGES,orderUploads);
        args.putInt(ARGS_POSITION,position);

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getArguments();

        if(bundle != null){
            mOrderUploads = bundle.getParcelableArrayList(ARGS_IMAGES);
            position      = bundle.getInt(ARGS_POSITION);
        }

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_slide_show,container,false);

        setupPhotoView(
                view.findViewById(R.id.photo_view)
        );

        return  view;
    }

    private void setupPhotoView(PhotoView photoView) {
        Glide.with(getActivity())
                .load(mOrderUploads.get(position).getImage())
                .into(photoView);
    }

}
