package com.android.onmarcy;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.android.onmarcy.profile.UpdateInstagramActivity;
import com.steelkiwi.cropiwa.AspectRatio;
import com.steelkiwi.cropiwa.CropIwaView;
import com.steelkiwi.cropiwa.config.CropIwaSaveConfig;
import com.steelkiwi.cropiwa.config.InitialPosition;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

public class CropActivity extends AppCompatActivity {
    public static final String TAG = "img_uri";
    private CropIwaView cropView;
    private Uri savedImageURI;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crop);
        cropView = findViewById(R.id.crop_view);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        savedImageURI = Uri.parse(getIntent().getStringExtra(TAG));
        cropView.setImageUri(savedImageURI);

        cropView.configureOverlay()
                .setAspectRatio(new AspectRatio(1, 1))
                .apply();

        cropView.configureImage()
                .setImageInitialPosition(InitialPosition.CENTER_INSIDE)
                .apply();

        cropView.configureImage()
                .setScale(0.5f)
                .apply();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_crop, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setCancelable(false);
                builder.setTitle(R.string.confirmation);
                builder.setMessage(R.string.msg_save_changes);
                builder.setPositiveButton(R.string.save, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        save();
                    }
                });
                builder.setNegativeButton(R.string.discard, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                        Intent backIntent = new Intent(CropActivity.this, HomeActivity.class);
                        backIntent.putExtra(HomeActivity.TAG, true);
                        startActivity(backIntent);
                    }
                });

                AlertDialog dialog = builder.create();
                dialog.show();
                break;
            case R.id.item_done:
                save();
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void save(){
        cropView.crop(new CropIwaSaveConfig.Builder(destinationUri())
                .setCompressFormat(Bitmap.CompressFormat.PNG)
                .setQuality(100)
                .build());
        cropView.setCropSaveCompleteListener(bitmapUri -> {
            Intent intent = new Intent(CropActivity.this, HomeActivity.class);
            intent.putExtra(HomeActivity.TAG, true);
            intent.putExtra(TAG, bitmapUri.toString());
            startActivity(intent);
        });
    }

    private Uri destinationUri(){
        String folderPath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES) + "/OnMarcy/";
        File folder = new File(folderPath);
        if (!folder.exists()) {
            File wallpaperDirectory = new File(folderPath);
            wallpaperDirectory.mkdirs();
        }
        File newFile = new File(folderPath, new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date()) + ".jpg");
        Uri relativePath = Uri.fromFile(newFile);
        return relativePath;
    }
}