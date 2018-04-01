package com.github.abhijit.placefinder.ui.main.fragment.details;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.github.abhijit.placefinder.R;
import com.github.abhijit.placefinder.data.web.models.PlaceDetails;
import com.github.abhijit.placefinder.utils.GlideUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

class ReviewAdapter extends RecyclerView.Adapter<ReviewAdapter.ReviewHolder> {

    private List<PlaceDetails.Result.Review> reviewList = new ArrayList<>();

    ReviewAdapter(List<PlaceDetails.Result.Review> reviewList) {
        this.reviewList.addAll(reviewList);
    }

    @NonNull
    @Override
    public ReviewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.layout_review, parent, false);
        return new ReviewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ReviewHolder holder, int position) {
        PlaceDetails.Result.Review review = reviewList.get(position);
        GlideUtils.load(review.getProfilePhotoUrl(), holder.reviewerImage);
        holder.tvReviewerName.setText(review.getAuthorName());
        holder.reviewRating.setRating(Float.valueOf(review.getRating()));
        holder.tvTimeStamp.setText(review.getTimestamp());
        holder.tvReview.setText(review.getText());
    }

    @Override
    public int getItemCount() {
        return reviewList.size();
    }

    class ReviewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        @BindView(R.id.tvReviewerName)
        TextView tvReviewerName;

        @BindView(R.id.tvTimeStamp)
        TextView tvTimeStamp;

        @BindView(R.id.tvReview)
        TextView tvReview;

        @BindView(R.id.reviewRating)
        RatingBar reviewRating;

        @BindView(R.id.reviewerImage)
        ImageView reviewerImage;

        ReviewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            tvReview.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            tvReview.setMaxLines(10);
        }
    }
}
