package com.abhinandan.nimus.FragmentsProfile;


import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.abhinandan.nimus.R;

public class FragmentOthers extends Fragment {


    public FragmentOthers() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view =  inflater.inflate(R.layout.fragment_others, container, false);
        return view;
    }

}
