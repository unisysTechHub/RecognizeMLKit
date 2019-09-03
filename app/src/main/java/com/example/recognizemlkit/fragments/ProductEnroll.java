package com.example.recognizemlkit.fragments;


import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.recognizemlkit.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class ProductEnroll extends Fragment {


    public ProductEnroll() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_product_enroll, container, false);
    }

}
