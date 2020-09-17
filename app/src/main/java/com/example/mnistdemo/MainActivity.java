package com.example.mnistdemo;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.res.AssetFileDescriptor;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.tensorflow.lite.Interpreter;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.MappedByteBuffer;

import butterknife.BindView;
import butterknife.ButterKnife;


public class MainActivity extends AppCompatActivity {
    private static final int PIXEL_WIDTH = 28;
    private static final String TAG ="Jacky" ;
    private DigitsDetector mnistClassifier;
    private PaintView paintView;
    private View detectButton,clearButton;
    LinearLayout inferencePreview;
    ImageView previewImage;
    TextView mResultText;
//    @BindView(R.id.button_detect)
//    View detectButton;

//    @BindView(R.id.button_clear)
//    View clearButton;

//    @BindView(R.id.text_result)
//    TextView mResultText;

//    @BindView(R.id.paintView)
//    PaintView paintView;

//    @BindView(R.id.preview_image)
//    ImageView previewImage;

//    @BindView(R.id.inference_preview)
//    LinearLayout inferencePreview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitle(R.string.app_name);
//        ButterKnife.bind(this);
        paintView = findViewById(R.id.paintView);
        detectButton = findViewById(R.id.button_detect);
        inferencePreview = findViewById(R.id.inference_preview);
        mnistClassifier = new DigitsDetector(this);
        previewImage = findViewById(R.id.preview_image);
        mResultText = findViewById(R.id.text_result);
        clearButton = findViewById(R.id.button_clear);

        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        if (paintView == null)
            Log.d(TAG,"paintView null");
        paintView.init(metrics);

        detectButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onDetectClicked();
            }
        });

        clearButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClearClicked();
            }
        });
    }

    private void onDetectClicked() {
        inferencePreview.setVisibility(View.VISIBLE);
        Bitmap scaledBitmap = Bitmap.createScaledBitmap(paintView.getBitmap(), PIXEL_WIDTH, PIXEL_WIDTH, false);
        int digit = mnistClassifier.classify(scaledBitmap);
        previewImage.setImageBitmap(scaledBitmap);
        if (digit >= 0) {
            Log.d(TAG, "Found Digit = " + digit);
            mResultText.setText(String.valueOf(digit));
        } else {
            mResultText.setText(getString(R.string.not_detected));
        }
    }

    private void onClearClicked() {
        mResultText.setText("");
        paintView.clear();
    }

}