package com.android.onmarcy;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.net.Uri;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageSwitcher;
import android.widget.ImageView;
import android.widget.Toast;
import android.widget.ViewSwitcher;

import java.io.File;
import java.util.ArrayList;

import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;

public class PreviewActivity extends AppCompatActivity {
    public static final String TAG = "preview";
    public static final String TAG2 = "single";
    private ImageSwitcher imageSwitcher;
    private ArrayList<String> picturePathList = new ArrayList<>();
    private int position = 0;
    private int mode = 0;
    Uri imageUri;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preview);
        imageSwitcher = findViewById(R.id.image_switcher);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(getString(R.string.preview));

        if (getIntent().hasExtra(TAG)) {
            picturePathList = getIntent().getStringArrayListExtra(TAG);
            mode = 1;
        }

        if (getIntent().hasExtra(TAG2)) {
            imageUri = Uri.parse(getIntent().getStringExtra(TAG2));
            mode = 2;
        }

        imageSwitcher.setFactory(new ViewSwitcher.ViewFactory() {
            @Override
            public View makeView() {
                ImageView myView = new ImageView(getApplicationContext());
                myView.setScaleType(ImageView.ScaleType.FIT_CENTER);
                myView.setLayoutParams(new ImageSwitcher.LayoutParams(WRAP_CONTENT, WRAP_CONTENT, Gravity.CENTER));
                return myView;
            }
        });

        if (mode == 1) imageUri = Uri.fromFile(new File(picturePathList.get(position)));
        imageSwitcher.setImageURI(imageUri);

        imageSwitcher.setOnTouchListener(new OnSwipeTouchListener(this) {
            public void onSwipeRight() {
                if (mode == 1) {
                    if (position < picturePathList.size() - 1) {
                        position++;
                    } else {
                        position = 0;
                    }
                    imageUri = Uri.fromFile(new File(picturePathList.get(position)));
                }

                imageSwitcher.setImageURI(imageUri);
            }

            public void onSwipeLeft() {
                if (mode == 1) {
                    if (position > 0) {
                        position--;
                    } else {
                        position = picturePathList.size() - 1;
                    }
                    imageUri = Uri.fromFile(new File(picturePathList.get(position)));
                }

                imageSwitcher.setImageURI(imageUri);
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
        }
        return super.onOptionsItemSelected(item);
    }
}