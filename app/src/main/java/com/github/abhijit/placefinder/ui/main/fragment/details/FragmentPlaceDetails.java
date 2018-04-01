package com.github.abhijit.placefinder.ui.main.fragment.details;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.github.abhijit.placefinder.R;
import com.github.abhijit.placefinder.data.scheduler.SchedulerInjector;
import com.github.abhijit.placefinder.data.web.WebServiceInjector;
import com.github.abhijit.placefinder.data.web.models.Photo;
import com.github.abhijit.placefinder.data.web.models.PlaceDetails;
import com.github.abhijit.placefinder.data.web.models.Places;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class FragmentPlaceDetails extends DialogFragment
        implements
        PhotoDetailsContract.View {

    private static final String TAG = FragmentPlaceDetails.class.getName();
    private static final String KEY_RESULT = "KEY_RESULT";

    @BindView(R.id.tvTitle)
    TextView tvTitle;

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.ratingBar)
    RatingBar ratingBar;

    @BindView(R.id.tvReviewCount)
    TextView tvReviewCount;

    @BindView(R.id.tvTags)
    TextView tvTags;

    @BindView(R.id.tvAddress)
    TextView tvAddress;

    @BindView(R.id.tvContact)
    TextView tvContact;

    @BindView(R.id.photoList)
    RecyclerView photoList;

    @BindView(R.id.reviewList)
    RecyclerView reviewList;

    private Places.Result result;

    private PhotoDetailsContract.Presenter presenter;

    public FragmentPlaceDetails() { /* Empty constructor */ }

    public static FragmentPlaceDetails newInstance(Places.Result result) {
        Bundle args = new Bundle();
        args.putParcelable(KEY_RESULT, result);
        FragmentPlaceDetails fragment = new FragmentPlaceDetails();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        setStyle(DialogFragment.STYLE_NO_TITLE, R.style.FullScreenDialogTheme);
        result = getArguments().getParcelable(KEY_RESULT);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_place_details, container, false);
        ButterKnife.bind(this, v);
        return v;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        tvTitle.setText(result.getName());
        if (result.getRating() != null){
            // TODO: 3/31/18 grey out starts if rating is not available
            ratingBar.setRating(Float.valueOf(String.valueOf(result.getRating())));
        }
        if (presenter == null) {
            presenter = new PhotoDetailsPresenter(result.getPlaceId(), this, WebServiceInjector.getWebService(), SchedulerInjector.getScheduler());
        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);
        dialog.getWindow().getAttributes().windowAnimations
                = R.style.DialogAnimation;
        return dialog;
    }

    @Override
    public void onStart() {
        super.onStart();
        presenter.subscribe();
    }

    @Override
    public void onStop() {
        super.onStop();
        presenter.unsubscribe();
    }

    public void show(FragmentManager manager) {
        show(manager, TAG);
    }

    @Override
    public void showMessage(int stringId) {
        Toast.makeText(getContext(), getString(stringId), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void setPlaceTitle(String name) {
        toolbar.setTitle(name);
        tvTitle.setText(name);
    }

    @Override
    public void setRating(Double rating) {
        ratingBar.setRating(Float.valueOf(String.valueOf(rating)));
    }

    @Override
    public void setAddress(String address) {
        tvAddress.setText(address);
    }

    @Override
    public void setContact(String contact) {
        tvContact.setText(contact);
    }

    @Override
    public void setPhotos(List<Photo> photos) {
        photoList.setAdapter(new PhotoAdapter(photos));
        photoList.setHasFixedSize(true);
    }

    @Override
    public void setReviews(List<PlaceDetails.Result.Review> reviews) {
        reviewList.setAdapter(new ReviewAdapter(reviews));
        reviewList.setHasFixedSize(true);
    }
}
