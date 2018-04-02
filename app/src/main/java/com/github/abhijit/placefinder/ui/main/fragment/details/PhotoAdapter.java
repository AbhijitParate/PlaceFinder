package com.github.abhijit.placefinder.ui.main.fragment.details;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.github.abhijit.placefinder.R;
import com.github.abhijit.placefinder.data.web.models.Photo;
import com.github.abhijit.placefinder.utils.GlideUtils;

import java.util.ArrayList;
import java.util.List;

class PhotoAdapter extends RecyclerView.Adapter<PhotoAdapter.PhotoHolder> {

    private List<Photo> photoList = new ArrayList<>();

    PhotoAdapter() { }

    @NonNull
    @Override
    public PhotoHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.layout_photo, parent, false);
        return new PhotoHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull PhotoHolder holder, int position) {
        GlideUtils.load(GlideUtils.PHOTO_BASE_URL + photoList.get(position).getPhotoReference(), (ImageView) holder.itemView);
    }

    @Override
    public int getItemCount() {
        return photoList.size();
    }

    public void updateDataset(List<Photo> photos){
        if (photos != null && photos.size() > 0) {
            this.photoList.clear();
            this.photoList.addAll(photos);
            notifyDataSetChanged();
        }
    }

    class PhotoHolder extends RecyclerView.ViewHolder {
        PhotoHolder(View itemView) {
            super(itemView);
        }
    }
}