package com.github.abhijit.placefinder.ui.fragment.list;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.abhijit.placefinder.R;
import com.github.abhijit.placefinder.retrofit.models.Places;
import com.github.abhijit.placefinder.retrofit.models.Result;
import com.github.abhijit.placefinder.ui.OnFragmentAttachListener;
import com.github.abhijit.placefinder.ui.OnResultListener;
import com.github.abhijit.placefinder.ui.fragment.detail.FragmentPlaceDetail;

import butterknife.BindView;
import butterknife.ButterKnife;

public class FragmentList extends Fragment implements OnResultListener, PlaceAdapter.OnPlaceClickListener, SwipeRefreshLayout.OnRefreshListener {

    public static final String TAG = FragmentList.class.getName();


    @BindView(R.id.swipeRefreshLayout)
    SwipeRefreshLayout swipeRefreshLayout;

    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;

    private PlaceAdapter placeAdapter;

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
        View v = inflater.inflate(com.github.abhijit.placefinder.R.layout.fragment_list, container, false);
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
    public void onResultReady(Places places) {
        placeAdapter.updateDataSet(places.getResults());
        swipeRefreshLayout.setRefreshing(false);
    }

    @Override
    public void onPlaceClicked(Result result) {
        FragmentPlaceDetail.newInstance(result).show(getChildFragmentManager());
    }

    @Override
    public void onRefresh() {
        ((OnFragmentAttachListener) getActivity()).refreshPlaces();
    }
}
