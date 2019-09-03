package com.example.recognizemlkit.model;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.databinding.Observable;

import java.util.LinkedHashMap;


public class ViewProfileModel  implements Parcelable {

    LinkedHashMap<String, String>  extractedLines;

    Observable textView;


    public ViewProfileModel(){}
    protected ViewProfileModel(Parcel in) {

    }

    public static final Creator<ViewProfileModel> CREATOR = new Creator<ViewProfileModel>() {
        @Override
        public ViewProfileModel createFromParcel(Parcel in) {
            return new ViewProfileModel(in);
        }

        @Override
        public ViewProfileModel[] newArray(int size) {
            return new ViewProfileModel[size];
        }
    };

    public LinkedHashMap<String, String> getExtractedLines() {
        return extractedLines;
    }

    public void setExtractedLines(LinkedHashMap<String, String> extractedLines) {
        this.extractedLines = extractedLines;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
    }
}
