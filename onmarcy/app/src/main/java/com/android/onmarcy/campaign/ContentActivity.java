package com.android.onmarcy.campaign;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.onmarcy.Global;
import com.android.onmarcy.PreviewActivity3;
import com.android.onmarcy.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import model.Approach;
import model.Campaign;
import model.Message;
import model.User;

public class ContentActivity extends AppCompatActivity {
    private TextView tvTitle, tvDescription, tvPrice, tvBrand, tvInstagram, tvDate, tvTime, tvDuration, tvCategory, tvAge, tvGender, tvLocation, tvViewComments, tvCaption, tvBio;
    private Button btnApproach, btnCancelApproach, btnPreviewPost, btnPreviewStory;
    private LinearLayout linearPost, linearStory;
    private EditText edtMsg;
    private ImageView btnSend;
    private Campaign campaign;
    public static String EXTRA_CAMPAIGN = "campaign";
    public static final String EXTRA_APPROVAL = "approval";
    private User user;
    private int totalComments = 0;
    private ArrayList<String> picturesPost = new ArrayList<>();
    private ArrayList<String> picturesStory = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_content);
        getSupportActionBar().setTitle(getString(R.string.Detail_Campaign));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        bindView();

        try {
            user = new User(new JSONObject(Global.getShared(Global.SHARED_INDEX.USER, "{}")));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        if (getIntent().hasExtra(EXTRA_CAMPAIGN)) {
            campaign = getIntent().getParcelableExtra(EXTRA_CAMPAIGN);
            bindData();
            setVisibility();
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

        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String msg = edtMsg.getText().toString();

                if (!msg.equals("")) {
                    Message.insert(ContentActivity.this, campaign.getCode(), msg, new Message.Callback() {
                        @Override
                        public void success() {
                            tvViewComments.setVisibility(View.VISIBLE);
                            edtMsg.setText("");
                        }

                        @Override
                        public void error() {

                        }
                    });
                }
            }
        });

        tvViewComments.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ContentActivity.this, ViewCommentsActivity.class);
                intent.putExtra(ViewCommentsActivity.TAG, campaign.getCode());
                startActivity(intent);
            }
        });

        btnPreviewPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ContentActivity.this, PreviewActivity3.class);
                intent.putExtra(PreviewActivity3.TAG, picturesPost);
                startActivity(intent);
            }
        });

        btnPreviewStory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ContentActivity.this, PreviewActivity3.class);
                intent.putExtra(PreviewActivity3.TAG, picturesStory);
                startActivity(intent);
            }
        });
    }

    private void bindData() {
        Campaign.detail(this, campaign.getCode(), campaign.getCodeString(), new Campaign.CallbackSelect() {
            @Override
            public void success(JSONArray data) {
                try {
                    Campaign campaign = new Campaign(data.getJSONObject(0));
                    tvTitle.setText(campaign.getTitle());

                    if (campaign.getNotes().equals("")) {
                        tvDescription.setText("-");
                    } else {
                        tvDescription.setText(campaign.getNotes());
                    }

                    tvInstagram.setText(campaign.getBrandCode());
                    NumberFormat nf = NumberFormat.getInstance(new Locale("da", "DK"));
                    tvPrice.setText(getResources().getString(R.string.price_format, nf.format(campaign.getPrice())));
                    tvBrand.setText(campaign.getBrandName());
                    String[] date = campaign.getDate().split("-");
                    tvDate.setText(getString(R.string.date, date[2], date[1], date[0]));
                    tvTime.setText(campaign.getTime());
                    tvDuration.setText(campaign.getDuration() + "");
                    tvCategory.setText(campaign.getCategoryName());
                    tvAge.setText(campaign.getAgeMin() + " - " + campaign.getAgeMax());

                    int gender = campaign.getGender();
                    if (gender == 1) tvGender.setText(getString(R.string.male));
                    if (gender == 2) tvGender.setText(getString(R.string.female));
                    if (gender == 3) tvGender.setText(getString(R.string.all));

                    tvLocation.setText(campaign.getCityName());

                    if (campaign.caption.equals("")) {
                        tvCaption.setText("-");
                    } else {
                        tvCaption.setText(campaign.caption);
                    }

                    if (campaign.bio.equals("")) {
                        tvBio.setText("-");
                    } else {
                        tvBio.setText(campaign.bio);
                    }

                    if (!campaign.posts[0].equals("")) {
                        linearPost.setVisibility(View.VISIBLE);
                    }
                    if (!campaign.stories[0].equals("")) {
                        linearStory.setVisibility(View.VISIBLE);
                    }

                    for (int i = 0; i < campaign.posts.length; i++) {
                        if (!campaign.posts[i].equals("")) {
                            picturesPost.add(campaign.posts[i]);
                        }
                    }

                    for (int i = 0; i < campaign.stories.length; i++) {
                        if (!campaign.stories[i].equals("")) {
                            picturesStory.add(campaign.stories[i]);
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void error() {

            }
        });

        Message.select(this, campaign.getCode(), new Message.CallbackSelect() {
            @Override
            public void success(JSONArray data) {
                totalComments = data.length();

                if (totalComments == 0) {
                    tvViewComments.setVisibility(View.GONE);
                }
            }

            @Override
            public void error() {
                if (totalComments == 0) {
                    tvViewComments.setVisibility(View.GONE);
                }
            }
        });
    }

    private void setVisibility() {
        if (user.getUserType() == 1 || getIntent().hasExtra(EXTRA_APPROVAL)) {
            btnApproach.setVisibility(View.GONE);
            btnCancelApproach.setVisibility(View.GONE);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        menu.clear();
        getMenuInflater().inflate(R.menu.view_marketer_menu, menu);
        MenuItem menuItem = menu.findItem(R.id.item_view_marketer);
        if(user.getUserType() == 2){
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
            case R.id.item_view_marketer:
                Intent intent = new Intent(ContentActivity.this, ViewMarketerActivity.class);
                intent.putExtra(ViewMarketerActivity.EXTRA_CODE, campaign.getCode());
                startActivity(intent);
                break;
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
        edtMsg = findViewById(R.id.edt_msg);
        btnSend = findViewById(R.id.btn_send);
        tvViewComments = findViewById(R.id.tv_view_comments);
        tvCaption = findViewById(R.id.tv_caption);
        tvBio = findViewById(R.id.tv_bio);
        btnPreviewPost = findViewById(R.id.btn_preview_post);
        btnPreviewStory = findViewById(R.id.btn_preview_story);
        linearPost = findViewById(R.id.linear_layout_post);
        linearStory = findViewById(R.id.linear_layout_story);
    }
}