package com.github.abhijit.placefinder.ui.fragment.detail;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.github.abhijit.placefinder.R;
import com.github.abhijit.placefinder.retrofit.models.Photo;

import butterknife.BindView;
import butterknife.ButterKnife;

public class PhotoFragment extends Fragment {

    public static final String KEY_PHOTO_REFERENCE = "KEY_PHOTO_REFERENCE";

    @BindView(R.id.photo)
    ImageView photo;

    String photoReference;

    public static PhotoFragment getInstance(Photo photo) {
        PhotoFragment f = new PhotoFragment();
        Bundle args = new Bundle();
        args.putString(KEY_PHOTO_REFERENCE, photo.getPhotoReference());
        f.setArguments(args);
        return f;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        photoReference = getArguments().getString(KEY_PHOTO_REFERENCE);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_image, container, false);
        ButterKnife.bind(this, v);
        return v;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Glide.with(getActivity())
                .load("https://maps.googleapis.com/maps/api/place/photo?maxwidth=400&key=AIzaSyB-bpw0ollWA5AKpT11Y2CL2qPFs4kC_dk&" +
                        "photoreference=" + photoReference)
                .asBitmap()
                .placeholder(R.drawable.placeholder)
                .into(photo);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}