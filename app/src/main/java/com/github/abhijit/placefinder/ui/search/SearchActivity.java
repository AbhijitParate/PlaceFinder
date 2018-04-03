package com.github.abhijit.placefinder.ui.search;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.Toast;

import com.github.abhijit.placefinder.R;
import com.github.abhijit.placefinder.data.scheduler.SchedulerInjector;
import com.github.abhijit.placefinder.data.web.WebServiceInjector;
import com.github.abhijit.placefinder.data.web.models.SearchPredictions;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SearchActivity extends AppCompatActivity
        implements
        SearchContact.View,
        TextWatcher,
        PredictionAdapter.OnItemClickListener {

    public static final String SEARCH_RESULT = "_search_result";
    private static final int SEARCH_DELAY = 250; // .25 Seconds

    @BindView(R.id.etSearchBar)
    EditText etSearchBar;

    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;

    private PredictionAdapter predictionAdapter;

    private SearchContact.Presenter presenter;

    Handler handler = new Handler();
    Runnable searchRunnable;

    public static Intent getLauncherIntent(Context context) {
        return new Intent(context, SearchActivity.class);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        ButterKnife.bind(this);

        presenter = new SearchPresenter(this, WebServiceInjector.getWebService(), SchedulerInjector.getScheduler());

        etSearchBar.addTextChangedListener(this);
        predictionAdapter = new PredictionAdapter(this);
        recyclerView.setAdapter(predictionAdapter);
    }

    @Override
    protected void onStart() {
        super.onStart();
        presenter.subscribe();
    }

    @Override
    protected void onStop() {
        super.onStop();
        presenter.unsubscribe();
    }

    @Override
    public void showMessage(int stringId) {
        Snackbar.make(findViewById(android.R.id.content), stringId, Snackbar.LENGTH_SHORT).show();
    }

    @Override
    public void setPredictions(List<SearchPredictions.Prediction> predictions) {
        predictionAdapter.updateDateSet(predictions);
    }

    @Override
    public void showNearbyPlaces(SearchPredictions.Prediction prediction) {
        Intent intent = new Intent();
        intent.putExtra(SEARCH_RESULT, prediction);
        setResult(RESULT_OK, intent);
        finish();
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        // nothing here
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        // nothing here
    }

    @Override
    public void afterTextChanged(final Editable s) {
        handler.removeCallbacks(searchRunnable);
        searchRunnable = new Runnable() {
            @Override
            public void run() {
                if (!TextUtils.isEmpty(s)) {
                    presenter.getPredictions(s.toString().trim());
                }
            }
        };
        handler.postDelayed(searchRunnable, SEARCH_DELAY);

    }

    @Override
    public void onItemClickListener(SearchPredictions.Prediction prediction) {
        Toast.makeText(this, prediction.getDescription() + "clicked", Toast.LENGTH_SHORT).show();
        presenter.onPredictionClicked(prediction);
    }
}
