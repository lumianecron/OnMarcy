package com.android.onmarcy;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.text.NumberFormat;
import java.util.Locale;

import model.Approach;
import model.Campaign;

public class ContentActivity extends AppCompatActivity {
    TextView tvTitle, tvDescription, tvPrice, tvBrand, tvInstagram, tvDate, tvTime, tvDuration, tvCategory, tvAge, tvGender, tvLocation;
    Button btnApproach, btnCancelApproach;
    private Campaign campaign;
    public static String EXTRA_CAMPAIGN = "campaign";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_content);
        getSupportActionBar().setTitle(getString(R.string.content));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        bindView();

        if(getIntent().hasExtra(EXTRA_CAMPAIGN)){
            campaign = getIntent().getParcelableExtra(EXTRA_CAMPAIGN);
            bindData();
            Approach.cekApproach(ContentActivity.this, campaign.getCode(), new Approach.Callback() {
                @Override
                public void success() {
                    btnApproach.setVisibility(View.GONE);
                }

                @Override
                public void error() {
                    btnCancelApproach.setVisibility(View.GONE);
                }
            });
        }

        btnApproach.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Approach.approach(ContentActivity.this, campaign.getCode(), campaign.getNotes(), new Approach.Callback() {
                    @Override
                    public void success() {
                        Toast.makeText(ContentActivity.this, "Approach success", Toast.LENGTH_SHORT).show();
                        btnApproach.setVisibility(View.GONE);
                        btnCancelApproach.setVisibility(View.VISIBLE);
                    }

                    @Override
                    public void error() {
                        Toast.makeText(ContentActivity.this, "This campaign already approached", Toast.LENGTH_SHORT).show();
                        btnApproach.setVisibility(View.GONE);
                        btnCancelApproach.setVisibility(View.VISIBLE);
                    }
                });
            }
        });

        btnCancelApproach.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Approach.cancelApproach(ContentActivity.this, campaign.getCode(), new Approach.Callback() {
                    @Override
                    public void success() {
                        Toast.makeText(ContentActivity.this, "Approach cancel success", Toast.LENGTH_SHORT).show();
                        btnApproach.setVisibility(View.VISIBLE);
                        btnCancelApproach.setVisibility(View.GONE);
                    }

                    @Override
                    public void error() {
                        Toast.makeText(ContentActivity.this, "Approach cancel fail", Toast.LENGTH_SHORT).show();
                        btnApproach.setVisibility(View.GONE);
                        btnCancelApproach.setVisibility(View.VISIBLE);
                    }
                });
            }
        });
    }

    private void bindData() {
        tvTitle.setText(campaign.getTitle());
        tvDescription.setText(campaign.getNotes());
        NumberFormat nf = NumberFormat.getInstance(new Locale("da", "DK"));
        tvPrice.setText(getResources().getString(R.string.price_format, nf.format(campaign.getPrice())));
        tvBrand.setText(campaign.getBrandName());
        tvDate.setText(campaign.getDate());
        tvTime.setText(campaign.getTime());
        tvDuration.setText(campaign.getDuration() + "");
        tvCategory.setText(campaign.getCategoryName());
        tvAge.setText(campaign.getAgeMin() + " - " + campaign.getAgeMax());
        int gender = campaign.getGender();
        if(gender == 1) tvGender.setText(getString(R.string.male));
        if(gender == 2) tvGender.setText(getString(R.string.female));
        if(gender == 3) tvGender.setText(getString(R.string.all));
        tvLocation.setText(campaign.getCityName());
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                finish();
        }
        return super.onOptionsItemSelected(item);
    }

    private void bindView() {
        tvTitle = findViewById(R.id.tv_title);
        tvDescription = findViewById(R.id.tv_description);
        tvPrice = findViewById(R.id.tv_price);
        tvBrand = findViewById(R.id.tv_brand);
        tvInstagram = findViewById(R.id.tv_instagram);
        tvDate = findViewById(R.id.tv_date);
        tvTime = findViewById(R.id.tv_time);
        tvDuration = findViewById(R.id.tv_duration);
        tvCategory = findViewById(R.id.tv_category);
        tvAge = findViewById(R.id.tv_age);
        tvGender = findViewById(R.id.tv_gender);
        tvLocation = findViewById(R.id.tv_location);
        btnApproach = findViewById(R.id.btn_approach);
        btnCancelApproach = findViewById(R.id.btn_cancelApproach);
    }
}