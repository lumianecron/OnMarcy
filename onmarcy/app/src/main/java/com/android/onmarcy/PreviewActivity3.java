package com.android.onmarcy;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageSwitcher;
import android.widget.ImageView;
import android.widget.Toast;
import android.widget.ViewSwitcher;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DecodeFormat;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;

public class PreviewActivity3 extends AppCompatActivity {
    public static final String TAG = "url";
    private ImageView imageView;
    private ArrayList<String> pictures = new ArrayList<>();
    private int position = 0;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preview3);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(getString(R.string.preview));
        imageView = findViewById(R.id.imageView);

        if (getIntent().hasExtra(TAG)) {
            pictures = getIntent().getStringArrayListExtra(TAG);
        }

        Glide.with(this)
                .load(pictures.get(position))
                .apply(new RequestOptions().fitCenter().format(DecodeFormat.PREFER_ARGB_8888).override(Target.SIZE_ORIGINAL))
                .into(imageView);

        imageView.setOnTouchListener(new OnSwipeTouchListener(this) {
            public void onSwipeRight() {
                if (position < pictures.size() - 1) {
                    position++;
                } else {
                    position = 0;
                }

                Glide.with(PreviewActivity3.this)
                        .load(pictures.get(position))
                        .apply(new RequestOptions().fitCenter().format(DecodeFormat.PREFER_ARGB_8888).override(Target.SIZE_ORIGINAL))
                        .into(imageView);

                System.out.println(position);
            }

            public void onSwipeLeft() {
                if (position > 0) {
                    position--;
                } else {
                    position = pictures.size() - 1;
                }

                Glide.with(PreviewActivity3.this)
                        .load(pictures.get(position))
                        .apply(new RequestOptions().fitCenter().format(DecodeFormat.PREFER_ARGB_8888).override(Target.SIZE_ORIGINAL))
                        .into(imageView);

                System.out.println(position);
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