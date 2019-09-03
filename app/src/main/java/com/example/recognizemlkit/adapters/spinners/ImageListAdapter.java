package com.example.recognizemlkit.adapters.spinners;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.DataSetObserver;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SpinnerAdapter;
import android.widget.TextView;


import java.util.ArrayList;

public class ImageListAdapter implements SpinnerAdapter {
    Context  context;
    ArrayList imageList;
    public ImageListAdapter(Context context, ArrayList imaeList)
    {
                        this.context=context;
                        this.imageList=imaeList;


    }
    @Override
    public View getDropDownView(int i, View view, ViewGroup viewGroup) {
        view = new TextView(context);
        ((TextView) view).setText(imageList.get(i).toString());

        return view;
    }

    @Override
    public void registerDataSetObserver(DataSetObserver dataSetObserver) {

    }

    @Override
    public void unregisterDataSetObserver(DataSetObserver dataSetObserver) {

    }

    @Override
    public int getCount() {
        return imageList.size();
    }

    @Override
    public Object getItem(int i) {
        return imageList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @SuppressLint("ViewHolder")
    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        view = LayoutInflater.from(context).inflate(android.R.layout.simple_list_item_1,viewGroup,false);
        TextView imageName=view.findViewById(android.R.id.text1);
        imageName.setText(imageList.get(i).toString());

        return view;
    }

    @Override
    public int getItemViewType(int i) {
        return android.R.id.text1;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public boolean isEmpty() {
        return false;
    }
}
