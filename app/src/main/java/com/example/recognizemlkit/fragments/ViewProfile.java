package com.example.recognizemlkit.fragments;


import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.databinding.BindingAdapter;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.recognizemlkit.R;
import com.example.recognizemlkit.databinding.FragmentViewProfileBinding;
import com.example.recognizemlkit.model.ViewProfileModel;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Set;

/**
 * A simple {@link Fragment} subclass.
 */
public class ViewProfile extends Fragment {
        public static String TAG="@Ramesh";
ViewProfileModel viewProfileModel;
    public ViewProfile() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        viewProfileModel=getArguments().getParcelable("viewProfile");

    }

    public static ViewProfile newInstance(ViewProfileModel viewProfileModel) {

        Bundle args = new Bundle();
        args.putParcelable("viewProfile",viewProfileModel);
        ViewProfile fragment = new ViewProfile();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        FragmentViewProfileBinding fragmentViewProfileBinding=DataBindingUtil.inflate(inflater,R.layout.fragment_view_profile,container,false);
        fragmentViewProfileBinding.setProfile(viewProfileModel);
        viewProfileModel.getExtractedLines().keySet().stream().forEach( x ->
                Log.d(TAG," " + x));
        viewProfileModel.getExtractedLines().entrySet().stream().forEach( x ->
                Log.d(TAG," " + x));

        return fragmentViewProfileBinding.getRoot();


    }

  @BindingAdapter("android:text")
  public static   void setText(TextView view, String newValue)
    {
        view.setText(newValue);
        Log.d(TAG, newValue);

    }


}
