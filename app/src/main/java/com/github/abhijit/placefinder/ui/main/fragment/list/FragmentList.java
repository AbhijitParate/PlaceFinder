package com.github.abhijit.placefinder.ui.main.fragment.list;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;

import com.github.abhijit.placefinder.R;
import com.github.abhijit.placefinder.data.web.models.Places;
import com.github.abhijit.placefinder.ui.main.OnFragmentAttachListener;
import com.github.abhijit.placefinder.ui.main.ResultListener;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class FragmentList extends Fragment
        implements
        ResultListener,
        PlaceAdapter.OnPlaceClickListener,
        SwipeRefreshLayout.OnRefreshListener {

    public static final String TAG = FragmentList.class.getName();


    @BindView(R.id.swipeRefreshLayout)
    SwipeRefreshLayout swipeRefreshLayout;

    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;

    private PlaceAdapter placeAdapter;

    private boolean isScrolling = false;

    private RecyclerView.OnScrollListener onScrollListener = new RecyclerView.OnScrollListener() {
        @Override
        public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
            super.onScrollStateChanged(recyclerView, newState);
            if(newState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL) {
                isScrolling = true;
            }
        }

        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            super.onScrolled(recyclerView, dx, dy);
            LinearLayoutManager layoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
            int currentItems, scrollOutItems, totalItems;
            currentItems = layoutManager.getChildCount();
            totalItems = layoutManager.getItemCount();
            scrollOutItems = layoutManager.findFirstVisibleItemPosition();

            if(isScrolling && (currentItems + scrollOutItems == totalItems)) {
                isScrolling = false;
                ((OnFragmentAttachListener) getActivity()).loadMorePlaces();
            }
        }
    };

    public static FragmentList newInstance() {
        Bundle args = new Bundle();
        FragmentList fragment = new FragmentList();
        fragment.setArguments(args);
        return fragment;
    }

    public FragmentList() { /* empty public constructor required by Android */}

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_list, container, false);
        ButterKnife.bind(this, v);
        return v;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        swipeRefreshLayout.setOnRefreshListener(this);
        placeAdapter = new PlaceAdapter(this);
        recyclerView.setAdapter(placeAdapter);
    }

    @Override
    public void onStart() {
        super.onStart();
        ((OnFragmentAttachListener) getActivity()).onFragmentAttach(this);
    }

    @Override
    public void setPlaces(List<Places.Result> places) {
        placeAdapter.updatePlaceList(places);
        swipeRefreshLayout.setRefreshing(false);
        recyclerView.addOnScrollListener(onScrollListener);
    }

    @Override
    public void appendPlaces(List<Places.Result> places) {
        placeAdapter.appendPlaces(places);
    }

    @Override
    public void noMorePlaces() {
        recyclerView.removeOnScrollListener(onScrollListener);
        placeAdapter.noMorePlaces();
    }

    @Override
    public void onPlaceClicked(Places.Result result) {
        ((OnFragmentAttachListener) getActivity()).showPlaceDetails(result);
    }

    @Override
    public void onRefresh() {
        ((OnFragmentAttachListener) getActivity()).refreshPlaces();
    }
}
