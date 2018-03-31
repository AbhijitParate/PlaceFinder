package com.github.abhijit.placefinder.ui.fragment.list;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.github.abhijit.placefinder.R;
import com.github.abhijit.placefinder.retrofit.models.Result;
import com.github.abhijit.placefinder.utils.GlideUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class PlaceAdapter extends RecyclerView.Adapter<PlaceAdapter.PlaceViewHolder> {

    private List<Result> placeList = new ArrayList<>();
    private OnPlaceClickListener placeClickListener;

    PlaceAdapter(OnPlaceClickListener listener) {
        this.placeClickListener = listener;
    }

    @Override
    public int getItemViewType(int position) {
        return super.getItemViewType(position);
    }

    @NonNull
    @Override
    public PlaceViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.place_card, parent, false);
        return new PlaceViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PlaceViewHolder holder, int position) {
        Result r = placeList.get(position);
        holder.tvName.setText(r.getName());
        if (r.getRating() != null) {
            holder.ratingBar.setVisibility(View.VISIBLE);
            holder.ratingBar.setRating(Float.valueOf(String.valueOf(r.getRating())));
        } else {
            holder.ratingBar.setVisibility(View.GONE);
        }
        if (r.getPhotos() != null && r.getPhotos().size() > 0) {
            GlideUtils.load(r.getPhotos().get(0).getPhotoReference(), holder.placeImageView);
        }
        holder.bindClickListener();
    }

    @Override
    public int getItemCount() {
        return placeList.size();
    }

    public void updateDataSet(List<Result> results) {
        placeList.clear();
        placeList.addAll(results);
        notifyDataSetChanged();
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