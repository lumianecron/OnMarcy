package com.android.onmarcy.profile;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.onmarcy.HomeActivity;
import com.android.onmarcy.PortfolioAdapter;
import com.android.onmarcy.PreviewActivity2;
import com.android.onmarcy.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.theartofdev.edmodo.cropper.CropImage;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import model.Portfolio;

public class PortfolioActivity extends AppCompatActivity {
    public static final String TAG = "code";
    public static final String TAG2 = "view_only";
    private RecyclerView rvPortfolio;
    private ProgressBar progressBar;
    private FloatingActionButton floatingActionButton;
    private TextView tvNotFound;
    private ArrayList<Portfolio> portfolios = new ArrayList<>();
    private int code = 0;
    private PortfolioAdapter portfolioAdapter;
    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };
    private String base64String = "";
    private int permission = 0;
    private Uri imageUri;
    private boolean isViewOnly = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_portfolio);
        getSupportActionBar().setTitle(R.string.portfolio);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        floatingActionButton = findViewById(R.id.floating_action_button);
        progressBar = findViewById(R.id.progressBar);
        rvPortfolio = findViewById(R.id.rv_portfolio);
        tvNotFound = findViewById(R.id.tv_not_found);
        rvPortfolio.setHasFixedSize(true);
        rvPortfolio.setLayoutManager(new GridLayoutManager(this, 3));
        portfolioAdapter = new PortfolioAdapter(portfolios);
        rvPortfolio.setAdapter(portfolioAdapter);

        if (getIntent().hasExtra(TAG2)) {
            floatingActionButton.setVisibility(View.GONE);
            isViewOnly = true;
        }

        portfolioAdapter.setOnItemCallback(new PortfolioAdapter.OnItemCallback() {
            @Override
            public void OnItemClicked(Portfolio portfolio) {
                Intent intent = new Intent(PortfolioActivity.this, PreviewActivity2.class);
                intent.putExtra(PreviewActivity2.TAG, portfolio.getImageUrl());
                intent.putExtra(PreviewActivity2.TAG2, portfolio.getCode());
                intent.putExtra(PreviewActivity2.EXTRA_SM_CODE, code);
                intent.putExtra(PreviewActivity2.EXTRA_VIEW_ONLY, isViewOnly);
                startActivity(intent);
            }
        });

        if (getIntent().hasExtra(TAG)) {
            code = getIntent().getIntExtra(TAG, 0);
            getPortfolio();
        }

        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                verifyStoragePermissions(PortfolioActivity.this);
            }
        });

        OnBackPressedCallback callback = new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                Intent intent = new Intent(PortfolioActivity.this, HomeActivity.class);
                intent.putExtra(HomeActivity.TAG, true);
                startActivity(intent);
                finishAffinity();
            }
        };

        getOnBackPressedDispatcher().addCallback(this, callback);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                Intent intent = new Intent(PortfolioActivity.this, HomeActivity.class);
                intent.putExtra(HomeActivity.TAG, true);
                startActivity(intent);
                finishAffinity();
        }
        return super.onOptionsItemSelected(item);
    }

    private void getPortfolio() {
        Portfolio.select(this, code, new Portfolio.CallbackSelect() {
            @Override
            public void success(JSONArray data) {
                progressBar.setVisibility(View.GONE);
                portfolios.clear();
                for (int i = 0; i < data.length(); i++) {
                    try {
                        portfolios.add(new Portfolio(data.getJSONObject(i)));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                portfolioAdapter.notifyDataSetChanged();

                setVisibility();
            }

            @Override
            public void error() {
                progressBar.setVisibility(View.GONE);
                setVisibility();
            }
        });
    }

    private void setVisibility() {
        if (portfolios.size() == 0) {
            tvNotFound.setVisibility(View.VISIBLE);
        } else {
            tvNotFound.setVisibility(View.GONE);
        }
    }

    public void verifyStoragePermissions(Activity activity) {
        permission = ActivityCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE);

        if (permission != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(activity, PERMISSIONS_STORAGE, REQUEST_EXTERNAL_STORAGE);
        } else {
            selectImage();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            selectImage();
        }
    }

    public void selectImage() {
        final CharSequence[] options = {getString(R.string.choose_from_gallery), getString(R.string.cancel)};
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.insert_picture);
        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (options[item].equals(getString(R.string.choose_from_gallery))) {
                    pickFromGallery();
                } else if (options[item].equals(getString(R.string.cancel))) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }

    private void pickFromGallery() {
        CropImage.activity().start(this);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
                CropImage.ActivityResult result = CropImage.getActivityResult(data);
                imageUri = result.getUri();

                Bitmap thumbnail;

                try {
                    thumbnail = MediaStore.Images.Media.getBitmap(getContentResolver(), imageUri);
                    thumbnail = getResizedBitmap(thumbnail, 600);
                    base64String = BitMapToString(thumbnail);

                    Portfolio.insert(this, code, base64String, new Portfolio.Callback() {
                        @Override
                        public void success() {
                            Toast.makeText(PortfolioActivity.this, getString(R.string.msg_add_portfolio), Toast.LENGTH_SHORT).show();
                            getPortfolio();
                        }

                        @Override
                        public void error() {

                        }
                    });
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private String BitMapToString(Bitmap userImage1) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        userImage1.compress(Bitmap.CompressFormat.PNG, 60, byteArrayOutputStream);
        byte[] b = byteArrayOutputStream.toByteArray();
        base64String = Base64.encodeToString(b, Base64.DEFAULT);
        return base64String;
    }

    private Bitmap getResizedBitmap(Bitmap image, int maxSize) {
        int width = image.getWidth();
        int height = image.getHeight();
        float bitmapRatio = (float) width / (float) height;

        if (bitmapRatio > 1) {
            width = maxSize;
            height = (int) (width / bitmapRatio);
        } else {
            height = maxSize;
            width = (int) (height * bitmapRatio);
        }

        return Bitmap.createScaledBitmap(image, width, height, true);
    }
}