package com.android.onmarcy.campaign;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.android.onmarcy.R;
import com.android.onmarcy.profile.PortfolioActivity;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DecodeFormat;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;

import org.json.JSONObject;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;
import model.Campaign;
import model.SocialMedia;

public class DetailMarketerActivity extends AppCompatActivity {
    private TextView tvName, tvUser, tvUsername, tvCategory, tvFollowers, tvFollowing, tvTotalPost, tvTotalComment, tvTotalLike, tvMinAge, tvMaxAge, tvMale, tvFemale, tvTimePosting, tvServiceType;
    private CircleImageView imageView;
    public static final String EXTRA_USERNAME = "username";
    public static final String EXTRA_NAME = "name";
    public static final String EXTRA_PHOTO = "photo";
    private String username, name, photo;
    private SocialMedia socialMedia;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_marketer);
        bindView();
        getSupportActionBar().setTitle(getString(R.string.profile));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        if (getIntent().hasExtra(EXTRA_USERNAME)) {
            username = getIntent().getStringExtra(EXTRA_USERNAME);
            name = getIntent().getStringExtra(EXTRA_NAME);
            photo = getIntent().getStringExtra(EXTRA_PHOTO);
        }

        tvUser.setText(username);
        tvName.setText(name);
        setImage(photo);

        SocialMedia.detail(this, username, new SocialMedia.CallbackSelect() {
            @Override
            public void success(JSONObject jsonObject) {
                socialMedia = new SocialMedia(jsonObject);
                tvUsername.setText(socialMedia.getId());
                tvCategory.setText(socialMedia.getCategoryName());
                tvFollowers.setText(String.valueOf(socialMedia.getTotalFollower()));
                tvFollowing.setText(String.valueOf(socialMedia.getTotalFollowing()));
                tvTotalPost.setText(String.valueOf(socialMedia.getTotalPost()));
                tvTotalComment.setText(String.valueOf(socialMedia.getTotalComment()));
                tvTotalLike.setText(String.valueOf(socialMedia.getTotalLike()));
                tvMinAge.setText(String.valueOf(socialMedia.getMarketAgeMin()));
                tvMaxAge.setText(String.valueOf(socialMedia.getMarketAgeMax()));
                tvMale.setText(String.valueOf(socialMedia.getMarketMale()));
                tvFemale.setText(String.valueOf(socialMedia.getMarketFemale()));
                tvTimePosting.setText(String.valueOf(socialMedia.getTimePosting()));

                String txtService = "";
                ArrayList<String> services = new ArrayList<>();

                if (socialMedia.getServiceBio() == 1) {
                    services.add(getString(R.string.bio));
                }

                if (socialMedia.getServicePost() == 1) {
                    services.add(getString(R.string.post));
                }

                if (socialMedia.getServiceStory() == 1) {
                    services.add(getString(R.string.story));
                }

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    txtService = String.join(", ", services);
                }

                tvServiceType.setText(txtService);
            }

            @Override
            public void error() {

            }
        });
    }

    private void setImage(String img) {
        if (img.equals("")) {
            Glide.with(this)
                    .load(R.drawable.person)
                    .apply(new RequestOptions().fitCenter().format(DecodeFormat.PREFER_ARGB_8888).override(Target.SIZE_ORIGINAL))
                    .into(imageView);
        } else {
            Glide.with(this)
                    .load(img)
                    .apply(new RequestOptions().fitCenter().format(DecodeFormat.PREFER_ARGB_8888).override(Target.SIZE_ORIGINAL))
                    .into(imageView);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_portfolio, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
            case R.id.item_portfolio:
                Intent intent = new Intent(this, PortfolioActivity.class);
                intent.putExtra(PortfolioActivity.TAG, socialMedia.getCode());
                intent.putExtra(PortfolioActivity.TAG2, true);
                startActivity(intent);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void bindView() {
        tvName = findViewById(R.id.tv_name);
        tvUser = findViewById(R.id.tv_user);
        tvFollowers = findViewById(R.id.tv_followers);
        tvFollowing = findViewById(R.id.tv_following);
        tvUsername = findViewById(R.id.tv_username);
        tvTotalPost = findViewById(R.id.tv_total_post);
        tvTotalComment = findViewById(R.id.tv_total_comment);
        tvTotalLike = findViewById(R.id.tv_total_like);
        tvMinAge = findViewById(R.id.tv_min_age);
        tvMaxAge = findViewById(R.id.tv_max_age);
        tvMale = findViewById(R.id.tv_male);
        tvFemale = findViewById(R.id.tv_female);
        tvTimePosting = findViewById(R.id.tv_time_posting);
        tvServiceType = findViewById(R.id.tv_service_type);
        tvCategory = findViewById(R.id.tv_category);
        imageView = findViewById(R.id.image_view);
    }
}