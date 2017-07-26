package com.github.abhijit.placefinder.ui.fragment.list;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.abhijit.placefinder.R;
import com.github.abhijit.placefinder.retrofit.models.Places;
import com.github.abhijit.placefinder.retrofit.models.Result;
import com.github.abhijit.placefinder.ui.OnFragmentAttachListener;
import com.github.abhijit.placefinder.ui.OnResultListener;
import com.github.abhijit.placefinder.ui.fragment.detail.DetailsFragment;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ListFragment extends Fragment implements OnResultListener, PlaceAdapter.OnPlaceClickListener {

    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;

    OnFragmentAttachListener listener;

    public static ListFragment newInstance() {

        Bundle args = new Bundle();

        ListFragment fragment = new ListFragment();
        fragment.setArguments(args);
        return fragment;
    }

    public ListFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(com.github.abhijit.placefinder.R.layout.fragment_list, container, false);
        ButterKnife.bind(this, v);
        return v;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setRetainInstance(true);
        listener = (OnFragmentAttachListener) getActivity();
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
    }

    @Override
    public void onStart() {
        super.onStart();
        listener.onFragmentAttach(this);
    }

    @Override
    public void onResultReady(Places places) {
        if (places != null) {
            recyclerView.setAdapter(new PlaceAdapter(getActivity(), this, places.getResults()));
        }
    }

    @Override
    public void onPlaceClicked(Result result) {
        DetailsFragment.newInstance(result).show(getFragmentManager(), "Details");
    }
}
