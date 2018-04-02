package com.github.abhijit.placefinder.ui.search;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.widget.EditText;

import com.github.abhijit.placefinder.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SearchActivity extends AppCompatActivity {

    public static final String SEARCH_RESULT = "_search_result";

    @BindView(R.id.etSearchBar)
    EditText etSearchBar;

    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;


    public static Intent getLauncherIntent(Context context) {
        return new Intent(context, SearchActivity.class);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        ButterKnife.bind(this);
    }
}
