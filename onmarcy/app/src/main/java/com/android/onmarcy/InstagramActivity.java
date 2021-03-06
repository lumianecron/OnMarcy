package com.android.onmarcy;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class InstagramActivity extends AppCompatActivity {
    TextView tvFollower, tvFollowing, tvUsername, tvTotalPost, tvTotalComment, tvTotalLike, tvMinAge, tvMaxAge, tvMale, tvFemale, tvTimePosting, tvServiceType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_instagram);

        bindView();

        if(getIntent().getExtras() != null){
            int totalFollower = getIntent().getIntExtra("total_follower", 0);
            int totalFollowing = getIntent().getIntExtra("total_following", 0);
            String username = getIntent().getStringExtra("username");
            int totalPost = getIntent().getIntExtra("total_post", 0);
            int totalComment = getIntent().getIntExtra("total_comment", 0);
            int totalLike = getIntent().getIntExtra("total_like", 0);
            int minAge = getIntent().getIntExtra("min_age", 0);
            int maxAge = getIntent().getIntExtra("max_age", 0);
            int male = getIntent().getIntExtra("male", 0);
            int female = getIntent().getIntExtra("female", 0);
            int timePosting = getIntent().getIntExtra("time_posting", 0);
            int serviceType = getIntent().getIntExtra("service_type", 0);

            tvFollower.setText(totalFollower + "");
            tvFollowing.setText(totalFollowing + "");
            tvUsername.setText(username);
            tvTotalPost.setText(totalPost + "");
            tvTotalComment.setText(totalComment + "");
            tvTotalLike.setText(totalLike + "");
            tvMinAge.setText(minAge + "");
            tvMaxAge.setText(maxAge + "");
            tvMale.setText(male + "");
            tvFemale.setText(female + "");
            tvTimePosting.setText(timePosting + "");
            tvServiceType.setText(serviceType + "");
        }
    }

    private void bindView() {
        tvFollower = findViewById(R.id.tvFollowers);
        tvFollowing = findViewById(R.id.tvFollowing);
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
    }
}