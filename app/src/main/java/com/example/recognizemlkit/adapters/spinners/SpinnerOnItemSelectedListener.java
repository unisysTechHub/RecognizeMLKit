package com.example.recognizemlkit.adapters.spinners;

import android.util.Log;
import android.view.View;
import android.widget.AdapterView;

public class SpinnerOnItemSelectedListener implements AdapterView.OnItemSelectedListener {
    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

        Log.d("@Ramesh", "onItemSelected" );


    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

}
