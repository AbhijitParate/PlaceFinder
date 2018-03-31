package com.github.abhijit.placefinder.ui.main.fragment.detail;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.github.abhijit.placefinder.data.web.models.Places;

import java.util.List;

public class PhotoAdapter extends FragmentPagerAdapter {

    private List<Places.Result.Photo> photoList;

    public PhotoAdapter(FragmentManager fm, List<Places.Result.Photo> photos) {
        super(fm);
        this.photoList = photos;
    }

    @Override
    public Fragment getItem(int position) {
        return PhotoFragment.getInstance(photoList.get(position));
    }

    @Override
    public int getCount() {
        return photoList.size();
    }
}