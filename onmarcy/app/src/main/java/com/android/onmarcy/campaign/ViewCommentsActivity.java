package com.android.onmarcy.campaign;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;

import com.android.onmarcy.R;

import org.json.JSONArray;
import org.json.JSONException;

import java.lang.reflect.Array;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import model.Campaign;
import model.Message;

public class ViewCommentsActivity extends AppCompatActivity {
    public static final String TAG = "code";
    private ArrayList<Message> messages = new ArrayList<>();
    private RecyclerView rvComments;
    private int code;
    private MessageAdapter messageAdapter;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_comments);
        getSupportActionBar().setTitle(R.string.comments);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        progressBar = findViewById(R.id.progressBar);
        rvComments = findViewById(R.id.rv_comments);
        rvComments.setHasFixedSize(true);
        rvComments.setLayoutManager(new LinearLayoutManager(this));
        messageAdapter = new MessageAdapter(messages);
        rvComments.setAdapter(messageAdapter);

        if (getIntent().hasExtra(TAG)) {
            code = getIntent().getIntExtra(TAG, 0);
        }

        getMessages();
    }

    private void getMessages() {
        Message.select(this, code, new Message.CallbackSelect() {
            @Override
            public void success(JSONArray data) {
                progressBar.setVisibility(View.GONE);
                messages.clear();
                for (int i = 0; i < data.length(); i++) {
                    try {
                        messages.add(new Message(data.getJSONObject(i)));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                messageAdapter.notifyDataSetChanged();
            }

            @Override
            public void error() {
                progressBar.setVisibility(View.GONE);
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