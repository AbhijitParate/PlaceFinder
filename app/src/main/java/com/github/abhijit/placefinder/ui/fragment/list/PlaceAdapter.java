package com.github.abhijit.placefinder.ui.fragment.list;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.github.abhijit.placefinder.utils.GlideUtils;
import com.github.abhijit.placefinder.R;
import com.github.abhijit.placefinder.retrofit.models.Result;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class PlaceAdapter extends RecyclerView.Adapter<PlaceAdapter.PlaceViewHolder> {

    private List<Result> placeList;
    private Context context;
    private OnPlaceClickListener placeClickListener;

    public PlaceAdapter(Context context, OnPlaceClickListener listener, List<Result> places) {
        this.context = context;
        this.placeClickListener = listener;
        this.placeList = places;
    }

    @Override
    public PlaceViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.place_card, parent, false);
        return new PlaceViewHolder(view);
    }

    @Override
    public void onBindViewHolder(PlaceViewHolder holder, int position) {
        Result r = placeList.get(position);
        holder.tvName.setText(r.getName());
        if (r.getRating() != null) {
            holder.ratingBar.setVisibility(View.VISIBLE);
            holder.ratingBar.setRating(Float.valueOf(String.valueOf(r.getRating())));
        } else {
            holder.ratingBar.setVisibility(View.GONE);
        }
        if (r.getPhotos() != null && r.getPhotos().size() > 0) {
            GlideUtils.load("https://maps.googleapis.com/maps/api/place/photo?maxwidth=400&key=AIzaSyB-bpw0ollWA5AKpT11Y2CL2qPFs4kC_dk&photoreference=" + r.getPhotos().get(0).getPhotoReference(), holder.placeImageView);
        }
        holder.bindClickListener();
    }

    @Override
    public int getItemCount() {
        return placeList.size();
    }

    class PlaceViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.tvName)
        TextView tvName;

        @BindView(R.id.ratingBar)
        RatingBar ratingBar;

        @BindView(R.id.placeImageView)
        ImageView placeImageView;

        PlaceViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        void bindClickListener(){
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    placeClickListener.onPlaceClicked(placeList.get(getAdapterPosition()));
                }
            });
        }
    }

    interface OnPlaceClickListener {
        void onPlaceClicked(Result result);
    }
}