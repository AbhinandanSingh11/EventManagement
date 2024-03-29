package com.abhinandan.nimus.Adapters;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import java.util.ArrayList;

public class MenuAdapter extends FragmentPagerAdapter {

    private ArrayList<Fragment> fragments ;

    public MenuAdapter(@NonNull FragmentManager fm, int behavior,ArrayList<Fragment> fragments) {
        super(fm, behavior);
        this.fragments = fragments;
    }


    @NonNull
    @Override
    public Fragment getItem(int position) {
        return fragments.get(position);
    }

    @Override
    public int getCount() {
        return fragments.size();
    }



}
