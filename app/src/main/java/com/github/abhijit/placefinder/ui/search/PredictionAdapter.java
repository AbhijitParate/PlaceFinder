package com.github.abhijit.placefinder.ui.search;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.github.abhijit.placefinder.R;
import com.github.abhijit.placefinder.data.web.models.SearchPredictions;

import java.util.ArrayList;
import java.util.List;

class PredictionAdapter extends RecyclerView.Adapter<PredictionAdapter.PredictionHolder> {

    private List<SearchPredictions.Prediction> predictions = new ArrayList<>(0);
    private OnItemClickListener itemClickListener;

    PredictionAdapter(OnItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    @NonNull
    @Override
    public PredictionHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.layout_prediction, parent, false);
        return new PredictionHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PredictionHolder holder, int position) {
        holder.tvPredictionText.setText(predictions.get(position).getDescription());
    }

    @Override
    public int getItemCount() {
        return predictions.size();
    }

    public void updateDateSet(List<SearchPredictions.Prediction> predictions) {
        if (predictions != null && predictions.size() > 0) {
            this.predictions.clear();
            this.predictions.addAll(predictions);
            notifyDataSetChanged();
        }
    }

    class PredictionHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView tvPredictionText;

        PredictionHolder(View itemView) {
            super(itemView);
            tvPredictionText = itemView.findViewById(R.id.tvPredictionText);
            tvPredictionText.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            itemClickListener.onItemClickListener(predictions.get(getAdapterPosition()));
        }
    }

    interface OnItemClickListener {
        void onItemClickListener(SearchPredictions.Prediction prediction);
    }

}
