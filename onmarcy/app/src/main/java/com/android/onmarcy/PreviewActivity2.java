package com.android.onmarcy;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.onmarcy.campaign.ViewResultActivity;
import com.android.onmarcy.profile.PortfolioActivity;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DecodeFormat;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;

import model.Campaign;
import model.Portfolio;

public class PreviewActivity2 extends AppCompatActivity {
    public static final String TAG = "url";
    public static final String TAG2 = "portfolio";
    public static final String EXTRA_SM_CODE = "code";
    public static final String EXTRA_VIEW_ONLY = "view_only";
    private ImageView img;
    private String url = "";
    private boolean isPortfolio = false;
    private int code = 0;
    private int socialmediaCode = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preview2);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(getString(R.string.preview));
        img = findViewById(R.id.imageView);

        if (getIntent().hasExtra(TAG)) {
            url = getIntent().getStringExtra(TAG);
            Glide.with(this)
                    .load(url)
                    .apply(new RequestOptions().fitCenter().format(DecodeFormat.PREFER_ARGB_8888).override(Target.SIZE_ORIGINAL))
                    .into(img);
        }

        if(getIntent().hasExtra(TAG2)){
            isPortfolio = true;
            code = getIntent().getIntExtra(TAG2, 0);
            socialmediaCode = getIntent().getIntExtra(EXTRA_SM_CODE, 0);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        menu.clear();
        getMenuInflater().inflate(R.menu.menu_delete, menu);
        MenuItem menuItem = menu.findItem(R.id.item_delete);

        if(!isPortfolio){
            menuItem.setVisible(false);
        }

        if(getIntent().hasExtra(EXTRA_VIEW_ONLY) && getIntent().getBooleanExtra(EXTRA_VIEW_ONLY, false)){
            menuItem.setVisible(false);
        }

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
            case R.id.item_delete:
                AlertDialog.Builder builder = new AlertDialog.Builder(PreviewActivity2.this);
                builder.setCancelable(false);
                builder.setTitle(R.string.confirmation);
                builder.setMessage(R.string.msg_delete_portfolio);

                builder.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Portfolio.delete(PreviewActivity2.this, code, new Portfolio.Callback() {
                            @Override
                            public void success() {
                                Intent intent = new Intent(PreviewActivity2.this, PortfolioActivity.class);
                                intent.putExtra(PortfolioActivity.TAG, socialmediaCode);
                                startActivity(intent);
                                finish();
                            }

                            @Override
                            public void error() {

                            }
                        });
                    }
                });

                builder.setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

                AlertDialog dialog = builder.create();
                dialog.show();
        }
        return super.onOptionsItemSelected(item);
    }
}