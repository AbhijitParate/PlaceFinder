package com.github.abhijit.placefinder.ui.main.fragment.list;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.github.abhijit.placefinder.R;
import com.github.abhijit.placefinder.data.web.models.Places;
import com.github.abhijit.placefinder.utils.GlideUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class PlaceAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int ITEM_TYPE_FOOTER = 100;
    private static final int ITEM_TYPE_PLACE = 101;

    private List<Places.Result> placeList = new ArrayList<>();
    private AdapterCallbackListener callbackListener;
    private String nextPageToken;

    PlaceAdapter(AdapterCallbackListener listener) {
        this.callbackListener = listener;
    }

    @Override
    public int getItemViewType(int position) {
        if (placeList.get(position) == null) {
            if (nextPageToken != null) {
                callbackListener.getMorePlaces(nextPageToken);
                nextPageToken = null;
            }
            return ITEM_TYPE_FOOTER;
        }
        return ITEM_TYPE_PLACE;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == ITEM_TYPE_FOOTER) {
            return new FooterViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_footer, parent, false));
        } else {
            return new PlaceViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_place_card, parent, false));
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        int itemViewType = getItemViewType(position);
        if (itemViewType != ITEM_TYPE_FOOTER) {
            Places.Result r = placeList.get(position);
            ((PlaceViewHolder) holder).tvName.setText(String.format("%s %s", String.valueOf(position + 1), r.getName()));
            if (r.getRating() != null) {
                ((PlaceViewHolder) holder).ratingBar.setVisibility(View.VISIBLE);
                ((PlaceViewHolder) holder).ratingBar.setRating(Float.valueOf(String.valueOf(r.getRating())));
            } else {
                ((PlaceViewHolder) holder).ratingBar.setVisibility(View.GONE);
            }
            if (r.getPhotos() != null && r.getPhotos().size() > 0) {
                GlideUtils.load(GlideUtils.PHOTO_BASE_URL + r.getPhotos().get(0).getPhotoReference(), ((PlaceViewHolder) holder).placeImageView);
            }
            ((PlaceViewHolder) holder).bindClickListener();
            if (position == placeList.size() - 1) {
                Log.d("onBindViewHolder: ", "No more places");
            }
        }
    }

    @Override
    public int getItemCount() {
        return placeList.size();
    }

    public void updateDataSet(Places places) {
        placeList.clear();
        placeList.addAll(places.getResults());
        placeList.add(null);
        updateNextPageToken(places);
        notifyDataSetChanged();
    }

    public void appendPlaces(Places places) {
        int oldSize = placeList.size() - 1;
        placeList.addAll(oldSize, places.getResults());
        updateNextPageToken(places);
        notifyItemRangeChanged(oldSize, placeList.size());
    }

    private void updateNextPageToken(Places places) {
        nextPageToken = places.getNextPageToken();
        if (nextPageToken == null) {
            placeList.remove(placeList.size() - 1);
        }
    }

    public void noMorePlaces() {
        placeList.remove(placeList.size() - 1);
        notifyItemRemoved(placeList.size() - 1);
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

        void bindClickListener() {
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    callbackListener.onPlaceClicked(placeList.get(getAdapterPosition()));
                }
            });
        }
    }

    class FooterViewHolder extends RecyclerView.ViewHolder {
        FooterViewHolder(View itemView) {
            super(itemView);
        }
    }

    interface AdapterCallbackListener {
        void onPlaceClicked(Places.Result result);
        void getMorePlaces(String nextPageToken);
    }
}