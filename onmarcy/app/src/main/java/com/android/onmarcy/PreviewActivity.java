package com.android.onmarcy;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageSwitcher;
import android.widget.ImageView;
import android.widget.Toast;
import android.widget.ViewSwitcher;

import java.io.File;
import java.util.ArrayList;

import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;

public class PreviewActivity extends AppCompatActivity {
    public static final String TAG = "preview";
    private ImageSwitcher imageSwitcher;
    private ArrayList<String> picturePathList = new ArrayList<>();
    private int position = 0;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preview);
        imageSwitcher = findViewById(R.id.image_switcher);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(getString(R.string.preview));
        picturePathList = getIntent().getStringArrayListExtra(TAG);

        imageSwitcher.setFactory(new ViewSwitcher.ViewFactory() {
            @Override
            public View makeView() {
                ImageView myView = new ImageView(getApplicationContext());
                myView.setScaleType(ImageView.ScaleType.FIT_CENTER);
                myView.setLayoutParams(new ImageSwitcher.LayoutParams(WRAP_CONTENT, WRAP_CONTENT));
                return myView;
            }
        });
        imageSwitcher.setImageURI(Uri.fromFile(new File(picturePathList.get(position))));

        imageSwitcher.setOnTouchListener(new OnSwipeTouchListener(this){
            public void onSwipeRight() {
                if(position < picturePathList.size() - 1){
                    position++;
                }else{
                    position = 0;
                }
                imageSwitcher.setImageURI(Uri.fromFile(new File(picturePathList.get(position))));
            }
            public void onSwipeLeft() {
                if(position > 0){
                    position--;
                }else{
                    position = picturePathList.size() - 1;
                }
                imageSwitcher.setImageURI(Uri.fromFile(new File(picturePathList.get(position))));
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                finish();
        }
        return super.onOptionsItemSelected(item);
    }
}