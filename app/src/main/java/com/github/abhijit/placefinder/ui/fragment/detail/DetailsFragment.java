package com.github.abhijit.placefinder.ui.fragment.detail;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.github.abhijit.placefinder.utils.GlideUtils;
import com.github.abhijit.placefinder.R;
import com.github.abhijit.placefinder.retrofit.models.Result;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DetailsFragment extends DialogFragment {

    public static final String KEY_RESULT = "KEY_RESULT";

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

    public DetailsFragment() {

    }

    public static DetailsFragment newInstance(Result result) {
        Bundle args = new Bundle();
        args.putParcelable(KEY_RESULT, result);
        DetailsFragment fragment = new DetailsFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        result = getArguments().getParcelable(KEY_RESULT);
        geocoder = new Geocoder(getActivity(), Locale.getDefault());
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
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
                tvContact.setText("Not available");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (result.getPhotos() != null && result.getPhotos().size() > 0){
            GlideUtils.load("https://maps.googleapis.com/maps/api/place/photo?maxwidth=400&key=AIzaSyB-bpw0ollWA5AKpT11Y2CL2qPFs4kC_dk&" + "photoreference=" + result.getPhotos().get(0).getPhotoReference(), imageContainer);
        }
    }
}
