package com.github.abhijit.placefinder.ui.fragment.detail;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetDialogFragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.github.abhijit.placefinder.R;
import com.github.abhijit.placefinder.retrofit.models.Result;
import com.github.abhijit.placefinder.utils.GlideUtils;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;

public class FragmentPlaceDetail extends BottomSheetDialogFragment {

    private static final String TAG = FragmentPlaceDetail.class.getName();
    private static final String KEY_RESULT = "KEY_RESULT";

    @BindView(R.id.tvTitle)
    TextView tvTitle;

    @BindView(R.id.ratingBar)
    RatingBar ratingBar;

    @BindView(R.id.tvAddress)
    TextView tvAddress;

    @BindView(R.id.tvContact)
    TextView tvContact;

    @BindView(R.id.imageContainer)
    ImageView imageContainer;

    @BindView(R.id.reviewList)
    ListView reviewList;

    Geocoder geocoder;
    Result result;

    public FragmentPlaceDetail() { }

    public static FragmentPlaceDetail newInstance(Result result) {
        Bundle args = new Bundle();
        args.putParcelable(KEY_RESULT, result);
        FragmentPlaceDetail fragment = new FragmentPlaceDetail();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        result = getArguments().getParcelable(KEY_RESULT);
        geocoder = new Geocoder(getActivity(), Locale.getDefault());
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        // Set title for this dialog
        getDialog().setTitle(result.getName());

        View v = inflater.inflate(R.layout.fragment_details, container, false);
        ButterKnife.bind(this, v);
        return v;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        tvTitle.setText(result.getName());

        if (result.getRating() != null){
            ratingBar.setRating(Float.valueOf(String.valueOf(result.getRating())));
        }

        try {
            List<Address> addresses = geocoder.getFromLocation(result.getGeometry().getLocation().getLat(), result.getGeometry().getLocation().getLng(), 1);
            Address address = addresses.get(0);
            tvAddress.setText(address.getAddressLine(0));
            if (address.getPhone() != null && !address.getPhone().isEmpty()){
                tvContact.setText(address.getPhone());
            } else {
                tvContact.setText(R.string.label_not_available);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (result.getPhotos() != null && result.getPhotos().size() > 0){
            GlideUtils.load(result.getPhotos().get(0).getPhotoReference(), imageContainer);
        }
    }

    public void show(FragmentManager manager) {
        show(manager, TAG);
    }
}
