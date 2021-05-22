package com.android.onmarcy.campaign;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.onmarcy.PreviewActivity;
import com.android.onmarcy.PreviewActivity2;
import com.android.onmarcy.R;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DecodeFormat;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;

import org.json.JSONArray;
import org.json.JSONException;

import java.text.NumberFormat;
import java.util.Locale;

import model.Campaign;
import model.Result;

public class ViewResultActivity extends AppCompatActivity {
    public static final String EXTRA_CAMPAIGN = "campaign";
    private Button btnView, btnPreviewPost, btnPreviewStory, btnPreviewBio;
    private TextView tvTitle, tvDesc, tvBrand, tvPrice, tvLike, tvComment, tvReach, tvEngagement, tvImpression, tvSave, tvNotes;
    private LinearLayout linearLayoutPost, linearLayoutStory, linearLayoutBio, linearLayoutUploadScreenshot;
    private ImageView imgPost, imgStory, imgBio;
    private Campaign campaign;
    private String post, story, bio;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_result);
        bindView();
        getSupportActionBar().setTitle(getString(R.string.detail_result));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        if (getIntent().hasExtra(EXTRA_CAMPAIGN)) {
            campaign = getIntent().getParcelableExtra(EXTRA_CAMPAIGN);
        }

        tvTitle.setText(campaign.getTitle());
        tvBrand.setText(campaign.getBrandName());
        if (campaign.getNotes().equals("")) {
            tvDesc.setText("-");
        } else {
            tvDesc.setText(campaign.getNotes());
        }
        NumberFormat nf = NumberFormat.getInstance(new Locale("da", "DK"));
        tvPrice.setText(getString(R.string.price_format, nf.format(campaign.getPrice())));

        Result.select(ViewResultActivity.this, campaign.getCode(), new Campaign.CallbackSelect() {
            @Override
            public void success(JSONArray data) {
                try {
                    Result result = new Result(data.getJSONObject(0));
                    tvLike.setText(String.valueOf(result.getLike()));
                    tvComment.setText(String.valueOf(result.getComment()));
                    tvSave.setText(String.valueOf(result.getSave()));
                    tvEngagement.setText(String.valueOf(result.getEngagement()));
                    tvReach.setText(String.valueOf(result.getReach()));
                    tvImpression.setText(String.valueOf(result.getImpression()));
                    tvNotes.setText(result.getNotes());
                    post = result.getLinkPost();
                    story = result.getLinkStory();
                    bio = result.getLinkBio();

                    setVisibility(post, story, bio);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void error() {
                Toast.makeText(ViewResultActivity.this, getString(R.string.fail), Toast.LENGTH_SHORT).show();
            }
        });

        btnView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ViewResultActivity.this, ContentActivity.class);
                intent.putExtra(ContentActivity.EXTRA_CAMPAIGN, campaign);
                intent.putExtra(ContentActivity.EXTRA_APPROVAL, true);
                startActivity(intent);
            }
        });

        btnPreviewPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ViewResultActivity.this, PreviewActivity2.class);
                intent.putExtra(PreviewActivity2.TAG, post);
                startActivity(intent);
            }
        });

        btnPreviewStory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ViewResultActivity.this, PreviewActivity2.class);
                intent.putExtra(PreviewActivity2.TAG, story);
                startActivity(intent);
            }
        });

        btnPreviewBio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ViewResultActivity.this, PreviewActivity2.class);
                intent.putExtra(PreviewActivity2.TAG, bio);
                startActivity(intent);
            }
        });
    }

    private void setVisibility(String post, String story, String bio) {
        if(!post.equals("null") || !story.equals("null") || !bio.equals("null")){
            linearLayoutUploadScreenshot.setVisibility(View.VISIBLE);
        }

        if (!post.equals("null")) {
            linearLayoutPost.setVisibility(View.VISIBLE);
            Glide.with(ViewResultActivity.this)
                    .load(post)
                    .apply(new RequestOptions().fitCenter().format(DecodeFormat.PREFER_ARGB_8888).override(Target.SIZE_ORIGINAL))
                    .into(imgPost);
        }

        if (!story.equals("null")) {
            linearLayoutStory.setVisibility(View.VISIBLE);
            Glide.with(ViewResultActivity.this)
                    .load(story)
                    .apply(new RequestOptions().fitCenter().format(DecodeFormat.PREFER_ARGB_8888).override(Target.SIZE_ORIGINAL))
                    .into(imgStory);
        }

        if (!bio.equals("null")) {
            linearLayoutBio.setVisibility(View.VISIBLE);
            Glide.with(ViewResultActivity.this)
                    .load(bio)
                    .apply(new RequestOptions().fitCenter().format(DecodeFormat.PREFER_ARGB_8888).override(Target.SIZE_ORIGINAL))
                    .into(imgBio);
        }
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
        tvDesc = findViewById(R.id.tv_description);
        tvPrice = findViewById(R.id.tv_price);
        tvBrand = findViewById(R.id.tv_brand);
        btnView = findViewById(R.id.btn_view);
        imgPost = findViewById(R.id.img_post);
        imgStory = findViewById(R.id.img_story);
        imgBio = findViewById(R.id.img_bio);
        btnPreviewPost = findViewById(R.id.btn_preview_post);
        btnPreviewStory = findViewById(R.id.btn_preview_story);
        btnPreviewBio = findViewById(R.id.btn_preview_bio);
        tvLike = findViewById(R.id.tv_like);
        tvComment = findViewById(R.id.tv_comment);
        tvSave = findViewById(R.id.tv_save);
        tvEngagement = findViewById(R.id.tv_engagement);
        tvReach = findViewById(R.id.tv_reach);
        tvNotes = findViewById(R.id.tv_notes);
        tvImpression = findViewById(R.id.tv_impression);
        linearLayoutPost = findViewById(R.id.linear_layout_post);
        linearLayoutStory = findViewById(R.id.linear_layout_story);
        linearLayoutBio = findViewById(R.id.linear_layout_bio);
        linearLayoutUploadScreenshot = findViewById(R.id.linear_layout_upload_screenshot);
    }
}