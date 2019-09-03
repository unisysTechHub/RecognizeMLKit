package com.example.recognizemlkit.fragments;


import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.graphics.Rect;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.databinding.BindingAdapter;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.InverseBindingListener;
import androidx.databinding.adapters.ListenerUtil;
import androidx.databinding.adapters.TextViewBindingAdapter;
import androidx.fragment.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import com.example.recognizemlkit.R;
import com.example.recognizemlkit.adapters.spinners.ImageListAdapter;
import com.example.recognizemlkit.databinding.FragmentRecognizeTextInIimageBinding;
import com.example.recognizemlkit.model.ViewProfileModel;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.ml.vision.FirebaseVision;
import com.google.firebase.ml.vision.common.FirebaseVisionImage;
import com.google.firebase.ml.vision.text.FirebaseVisionText;
import com.google.firebase.ml.vision.text.FirebaseVisionTextRecognizer;
import com.google.firebase.ml.vision.text.RecognizedLanguage;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.ListResult;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link RecognizeTextInIImage#newInstance} factory method to
 * create an instance of this fragment.
 */
public class RecognizeTextInIImage extends Fragment {
    public static String TAG ="@Ramesh";;
    private StorageReference mStorageRef;
    Bitmap bitmap;
    ImageView imageView;
    TextView textView;
    ArrayList<String> imageList= new ArrayList<>();

    LinkedHashMap<String,String> linesInImage;
    Button viewProfile;
    ProgressBarFragment progressBarFragment;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;


    public RecognizeTextInIImage() {
        // Required empty public constructor
    }


    public static RecognizeTextInIImage newInstance(String param1, String param2) {
        RecognizeTextInIImage fragment = new RecognizeTextInIImage();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
         mStorageRef = FirebaseStorage.getInstance().getReference();
        FragmentRecognizeTextInIimageBinding fragmentRecognizeTextInIimageBinding=
                DataBindingUtil.inflate(inflater,R.layout.fragment_recognize_text_in_iimage,container,false);
        imageList.add("Green_card");
        fragmentRecognizeTextInIimageBinding.setImageListAdapter(new ImageListAdapter(this.getActivity(),imageList));
        //View view= inflater.inflate(R.layout.fragment_recognize_text_in_iimage, container, false);
        View view= fragmentRecognizeTextInIimageBinding.getRoot();
//        Spinner spinner=view.findViewById(R.id.image_list);
//
//
//
//        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
//
//                Log.d(TAG, "omItemSelected");
//            }
//
//            @Override
//            public void onNothingSelected(AdapterView<?> adapterView) {
//
//            }
//        });

        imageView=view.findViewById(R.id.image);
        viewProfile=view.findViewById(R.id.view_profile);

        viewProfile.setOnClickListener( buttonView ->{

            ViewProfileModel viewProfileModel = new ViewProfileModel();
            viewProfileModel.setExtractedLines(linesInImage);
            getActivity().getSupportFragmentManager().beginTransaction().
                    replace(R.id.frame_layout,ViewProfile.newInstance(viewProfileModel)).addToBackStack(null).
                    commit();
            //Log.d(TAG, "View profile ");

        });

        mStorageRef.child("users").child("uid").listAll().addOnSuccessListener(
                new OnSuccessListener<ListResult>() {
                    @Override
                    public void onSuccess(ListResult listResult) {
                        for(StorageReference storageReference: listResult.getItems())
                        {

                            Log.d(TAG, storageReference.getName());
                            imageList.add(storageReference.getName());
//                            spinner.setSelection(1);
                        }
                    }
                }
        );

        progressBarFragment= new ProgressBarFragment();


        // Inflate the layout for this fragment
        return view;
    }


    void downLoadImageFromCloud(String fileName)
    {
        progressBarFragment.show(getFragmentManager(),"MLKit");

        StorageReference mlImageStorgeReference=mStorageRef.child("users").child("uid").child(fileName);
        mlImageStorgeReference.getBytes(1024*1024).
                addOnSuccessListener( bytes -> {
                     bitmap =BitmapFactory.decodeByteArray(bytes,0,bytes.length);
                     imageView.setImageBitmap(bitmap);
                    progressBarFragment.dismiss();

                    viewProfile.setVisibility(View.VISIBLE);

                    FirebaseVisionImage firebaseVisionImage = FirebaseVisionImage.fromBitmap(bitmap);
                    processVisionImage(firebaseVisionImage);


                });



    }
    void processVisionImage(FirebaseVisionImage firebaseVisionImage)
    {
        FirebaseVisionTextRecognizer detector = FirebaseVision.getInstance()
                .getCloudTextRecognizer();
        Task<FirebaseVisionText> result =
                detector.processImage(firebaseVisionImage)
                        .addOnSuccessListener( firebaseVisionText ->{
                                Log.d("@Ramesh", "text in Image " +firebaseVisionText.getText());
                                List<FirebaseVisionText.TextBlock> textbloksInImage=getTextBlocksInImage(firebaseVisionText);
                                getTextLinesInEachBlock(textbloksInImage);

                            }
                        )
                        .addOnFailureListener(
                                new OnFailureListener() {
                                     @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Log.d("@Ramesh", e.getMessage());
                                    }
                                });

    }

    List<FirebaseVisionText.TextBlock> getTextBlocksInImage(FirebaseVisionText firebaseVisionText)
    {

        return firebaseVisionText.getTextBlocks();

    }
    void getTextLinesInEachBlock(List<FirebaseVisionText.TextBlock> textBlocksInImage)
    {

            int lineNo=1;
            String key=null;
        linesInImage= new LinkedHashMap<>();
        String categoryValue=null;
        String sex=null;
        for(FirebaseVisionText.TextBlock block : textBlocksInImage)
        {
            String blockText = block.getText();

                            for ( FirebaseVisionText.Line line: block.getLines())
                                {
                                    if(lineNo ==1 )
                                    linesInImage.put("CardType",line.getText());
                                    if(lineNo == 3)
                                        key=line.getText();
                                    if(lineNo==5)
                                    {
                                        linesInImage.put(key,line.getText());
                                        key="default";
                                    }
                                    if( lineNo >= 6)
                                    {
                                        if(lineNo % 2 == 0)
                                            key=line.getText();
                                        else
                                            linesInImage.put(key,line.getText());
                                    }

                                  Log.d("@Ramesh", "Line No : " +lineNo );
                                Log.d("@Ramesh", line.getText());
                                    lineNo++;

                            }

//
            Float blockConfidence = block.getConfidence();
            List<RecognizedLanguage> blockLanguages = block.getRecognizedLanguages();
//            for (RecognizedLanguage rLanguage: blockLanguages
//                 ) {
//                Log.d("@Ramesh", "block languages codes" +rLanguage.getLanguageCode());
//
//            }

            Point[] blockCornerPoints = block.getCornerPoints();
            Rect blockFrame = block.getBoundingBox();
            for (FirebaseVisionText.Line line: block.getLines()) {
                String lineText = line.getText();
                Float lineConfidence = line.getConfidence();
//                List<RecognizedLanguage> lineLanguages = line.getRecognizedLanguages();
//                Log.d("@Ramesh", "line languages" +lineLanguages.toString());
                Point[] lineCornerPoints = line.getCornerPoints();
                Rect lineFrame = line.getBoundingBox();
                for (FirebaseVisionText.Element element: line.getElements()) {
                    String elementText = element.getText();
                    Float elementConfidence = element.getConfidence();
//                    List<RecognizedLanguage> elementLanguages = element.getRecognizedLanguages();
//                    Log.d("@Ramesh", "languages" +elementLanguages.toString());
                    Point[] elementCornerPoints = element.getCornerPoints();
                    Rect elementFrame = element.getBoundingBox();

                }
            }

            progressBarFragment.dismiss();


        }

        Log.d("@Ramesh" , "Keyset");
        linesInImage.keySet().stream().forEach( x -> { Log.d("@Ramesh" , x);});
        Log.d("@Ramesh" , "valuesset");
        linesInImage.values().stream().forEach( x -> { Log.d("@Ramesh" , x);});
    }

}

