package com.myoptimind.usashopper.features.uploadedorderview;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.transition.TransitionManager;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.constraintlayout.widget.Group;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.bumptech.glide.Glide;
import com.github.chrisbanes.photoview.PhotoView;
import com.myoptimind.usashopper.R;
import com.myoptimind.usashopper.features.orderdetail.OrderActivity;
import com.myoptimind.usashopper.models.OrderUpload;

import java.util.ArrayList;

public class SlideShowFragment extends Fragment {

    public static final String KEY_DESCRIPTION = "key_description";
    public static final String KEY_POSITION    = "key_position";

    ArrayList<OrderUpload> mOrderUploads;
    int position;
    private EditText etDescription;
    private View backgroundOverlay;
    private Group grpEditWidgets;
    private SlideShowViewModel mSlideShowViewModel;
    private ConstraintLayout mConstraintLayout;

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

        mSlideShowViewModel = new ViewModelProvider(getActivity()).get(SlideShowViewModel.class);

        if(bundle != null){
            mOrderUploads = bundle.getParcelableArrayList(ARGS_IMAGES);
            position      = bundle.getInt(ARGS_POSITION);
        }
        setHasOptionsMenu(true);

    }

/*    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.photo_menu,menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getTitle() != null){
            if(item.getItemId() == R.id.add_description){
                etDescription.requestFocus();
            }
        }
        return super.onOptionsItemSelected(item);
    }*/

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_slide_show,container,false);

        mConstraintLayout = (ConstraintLayout) view;

        setupPhotoView(
                view.findViewById(R.id.photo_view)
        );
        etDescription = view.findViewById(R.id.et_caption);
        grpEditWidgets = view.findViewById(R.id.group_edit_widgets);
        backgroundOverlay = view.findViewById(R.id.loading_overlay);
        setupDescription();

        ImageButton ibConfirm = view.findViewById(R.id.ib_confirm_description);
        ibConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mSlideShowViewModel.updateDescription(
                        mOrderUploads.get(position).getId(),
                        etDescription.getText().toString()
                );
            }
        });
        ImageButton ibCancel  = view.findViewById(R.id.ib_close_description);
        ibCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideKeyboard();
                mSlideShowViewModel.refreshDescription();
            }
        });

        mSlideShowViewModel.getEditSuccess().observe(getViewLifecycleOwner(), message -> {
            Toast.makeText(getActivity(),message,Toast.LENGTH_SHORT).show();
            hideKeyboard();
        });

        return  view;
    }

    private void hideKeyboard() {
        etDescription.clearFocus();
        // hide virtual keyboard
        InputMethodManager imm = (InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(etDescription.getWindowToken(), 0);
    }

    private void setupDescription() {

        ConstraintSet cs1 = new ConstraintSet();
        cs1.clone(mConstraintLayout);

        String description = mOrderUploads.get(position).getCaption();

        mSlideShowViewModel.setDescription(description);

        mSlideShowViewModel.getDescription().observe(getViewLifecycleOwner(), desc -> {
            if(desc != null){
                etDescription.setText(desc);
                Intent intent = new Intent();
                intent.putExtra(KEY_DESCRIPTION,desc);
                intent.putExtra(KEY_POSITION,position);
                getActivity().setResult(OrderActivity.RESULT_OK,intent);
            }
        });

        mSlideShowViewModel.getIsUpdatingDescription().observe(getViewLifecycleOwner(), isUpdating -> {
            if(isUpdating){
                backgroundOverlay.setVisibility(View.VISIBLE);
            }else{
                backgroundOverlay.setVisibility(View.GONE);
            }
        });


        etDescription.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus){
                    ConstraintSet cs2 = new ConstraintSet();
                    cs2.clone(mConstraintLayout);
                    cs2.clear(
                            R.id.et_caption,
                            ConstraintSet.TOP
                    );
                    cs2.clear(
                            R.id.photo_view,
                            ConstraintSet.BOTTOM
                    );
                    cs2.connect(
                            R.id.photo_view,
                            ConstraintSet.BOTTOM,
                            ConstraintSet.PARENT_ID,
                            ConstraintSet.BOTTOM
                    );
                    TransitionManager.beginDelayedTransition(mConstraintLayout);
                    cs2.applyTo(mConstraintLayout);
                    grpEditWidgets.setVisibility(View.VISIBLE);
                }else{
                    cs1.applyTo(mConstraintLayout);
                    grpEditWidgets.setVisibility(View.INVISIBLE);
                    mSlideShowViewModel.refreshDescription();
                }
            }
        });
    }


    private void setupPhotoView(PhotoView photoView) {
        Glide.with(getActivity())
                .load(mOrderUploads.get(position).getImage())
                .into(photoView);
    }

}
