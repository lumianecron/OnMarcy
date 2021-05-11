package com.android.onmarcy.profile;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.CheckBox;
import android.widget.TimePicker;
import android.widget.Toast;

import com.android.onmarcy.HomeActivity;
import com.android.onmarcy.R;
import com.google.android.material.textfield.TextInputEditText;

import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import model.SocialMedia;

public class UpdateInstagramActivity extends AppCompatActivity {
    private TextInputEditText edtFollowers, edtFollowing, edtTotalPost, edtTotalComment, edtTotalLike, edtMin, edtMax, edtMale, edtFemale, edtTime;
    private CheckBox cbBio, cbPost, cbStory;
    private String codeSocialMedia;
    private int bioService = 0, postService = 0, storyService = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_instagram);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(R.string.update_instagram);
        bindView();
        bindData();

        edtTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar currentTime = Calendar.getInstance();
                SimpleDateFormat mFormat = new SimpleDateFormat("HH:mm", Locale.getDefault());
                TimePickerDialog timePickerDialog = new TimePickerDialog(UpdateInstagramActivity.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int hour, int minute) {
                        currentTime.set(Calendar.HOUR_OF_DAY, hour);
                        currentTime.set(Calendar.MINUTE, minute);
                        edtTime.setText(mFormat.format(currentTime.getTime()));
                    }
                }, currentTime.get(Calendar.HOUR_OF_DAY), currentTime.get(Calendar.MINUTE), true);
                timePickerDialog.setTitle(getString(R.string.select_time));
                timePickerDialog.show();
            }
        });
    }

    private void bindView(){
        edtFollowers = findViewById(R.id.edt_followers);
        edtFollowing = findViewById(R.id.edt_following);
        edtTotalPost = findViewById(R.id.edt_total_post);
        edtTotalComment = findViewById(R.id.edt_total_comment);
        edtTotalLike = findViewById(R.id.edt_total_like);
        edtMin = findViewById(R.id.edt_min_age);
        edtMax = findViewById(R.id.edt_max_age);
        edtMale = findViewById(R.id.edt_male);
        edtFemale = findViewById(R.id.edt_female);
        edtTime = findViewById(R.id.edt_time);
        cbBio = findViewById(R.id.cb_bio);
        cbPost = findViewById(R.id.cb_post);
        cbStory = findViewById(R.id.cb_story);
    }

    private void bindData(){
        SocialMedia.select(this, new SocialMedia.CallbackSelect() {
            @Override
            public void success(JSONObject jsonObject) {
                try {
                    SocialMedia socialMedia = new SocialMedia(jsonObject);
                    edtFollowers.setText(socialMedia.getTotalFollower() + "");
                    edtFollowing.setText(socialMedia.getTotalFollowing() + "");
                    edtTotalPost.setText(socialMedia.getTotalPost() + "");
                    edtTotalComment.setText(socialMedia.getTotalComment() + "");
                    edtTotalLike.setText(socialMedia.getTotalLike() + "");
                    edtMin.setText(socialMedia.getMarketAgeMin() + "");
                    edtMax.setText(socialMedia.getMarketAgeMax() + "");
                    edtMale.setText(socialMedia.getMarketMale() + "");
                    edtFemale.setText(socialMedia.getMarketFemale() + "");
                    edtTime.setText(socialMedia.getTimePosting() + "");

                    codeSocialMedia = socialMedia.getCode() + "";
                    if(socialMedia.getServiceBio() == 1){
                        cbBio.setChecked(true);
                        bioService = 1;
                    }
                    if(socialMedia.getServicePost() == 1){
                        cbPost.setChecked(true);
                        postService = 1;
                    }
                    if(socialMedia.getServiceStory() == 1){
                        cbStory.setChecked(true);
                        storyService = 1;
                    }
                } catch (Exception ex) {
                    Toast.makeText(UpdateInstagramActivity.this, ex + "", Toast.LENGTH_SHORT).show();
                    Log.d("RUNNN", ex + "");
                }
            }

            @Override
            public void error() {

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.update_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        boolean isValid = true;
        switch (item.getItemId()) {
            case R.id.item_update:
                if (TextUtils.isEmpty(edtFollowers.getText().toString())) {
                    edtFollowers.setError(getResources().getString(R.string.please_fill_out_this_field));
                    isValid = false;
                }
                if (TextUtils.isEmpty(edtFollowing.getText().toString())) {
                    edtFollowing.setError(getResources().getString(R.string.please_fill_out_this_field));
                    isValid = false;
                }
                if (TextUtils.isEmpty(edtTotalPost.getText().toString())) {
                    edtTotalPost.setError(getResources().getString(R.string.please_fill_out_this_field));
                    isValid = false;
                }
                if (TextUtils.isEmpty(edtTotalComment.getText().toString())) {
                    edtTotalComment.setError(getResources().getString(R.string.please_fill_out_this_field));
                    isValid = false;
                }
                if (TextUtils.isEmpty(edtTotalLike.getText().toString())) {
                    edtTotalLike.setError(getResources().getString(R.string.please_fill_out_this_field));
                    isValid = false;
                }
                if (TextUtils.isEmpty(edtMin.getText().toString())) {
                    edtMin.setError(getResources().getString(R.string.please_fill_out_this_field));
                    isValid = false;
                }
                if (TextUtils.isEmpty(edtMax.getText().toString())) {
                    edtMax.setError(getResources().getString(R.string.please_fill_out_this_field));
                    isValid = false;
                }
                if (TextUtils.isEmpty(edtMale.getText().toString())) {
                    edtMale.setError(getResources().getString(R.string.please_fill_out_this_field));
                    isValid = false;
                }
                if (TextUtils.isEmpty(edtFemale.getText().toString())) {
                    edtFemale.setError(getResources().getString(R.string.please_fill_out_this_field));
                    isValid = false;
                }
                if (TextUtils.isEmpty(edtTime.getText().toString())) {
                    edtTime.setError(getResources().getString(R.string.please_fill_out_this_field));
                    isValid = false;
                }
                if(!cbBio.isChecked() && !cbPost.isChecked() && !cbStory.isChecked()){
                    Toast.makeText(this, getString(R.string.msg_choose_service), Toast.LENGTH_SHORT).show();
                    isValid = false;
                }

                if (isValid) {
                    bioService = (cbBio.isChecked()) ? 1 : 0;
                    postService = (cbPost.isChecked()) ? 1 : 0;
                    storyService = (cbStory.isChecked()) ? 1 : 0;
                    SocialMedia.update_info(UpdateInstagramActivity.this,
                        "",
                        1,
                        Integer.parseInt(edtTotalPost.getText().toString()),
                        Integer.parseInt(edtFollowers.getText().toString()),
                        Integer.parseInt(edtFollowing.getText().toString()),
                        Integer.parseInt(edtTotalComment.getText().toString()),
                        Integer.parseInt(edtTotalLike.getText().toString()),
                        Integer.parseInt(edtMin.getText().toString()),
                        Integer.parseInt(edtMax.getText().toString()),
                        Integer.parseInt(edtMale.getText().toString()),
                        Integer.parseInt(edtFemale.getText().toString()),
                        edtTime.getText().toString(),
                        postService,
                        storyService,
                        bioService,
                        codeSocialMedia,
                        false,
                        new SocialMedia.Callback(){
                            @Override
                            public void success() {
                                Toast.makeText(UpdateInstagramActivity.this, getString(R.string.success), Toast.LENGTH_SHORT).show();
//                                UpdateInstagramActivity.super.onBackPressed();
                                Intent intent = new Intent(UpdateInstagramActivity.this, HomeActivity.class);
                                intent.putExtra(HomeActivity.TAG, true);
                                startActivity(intent);
                                finishAffinity();
                            }

                            @Override
                            public void error() {
                                Toast.makeText(UpdateInstagramActivity.this, getString(R.string.fail), Toast.LENGTH_SHORT).show();
                            }
                        });
                }
                break;
            case android.R.id.home:
                super.onBackPressed();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}