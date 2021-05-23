package com.android.onmarcy.campaign;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.MenuItem;

import com.android.onmarcy.R;

public class ViewCommentsActivity extends AppCompatActivity {
    public static final String TAG = "code";
    private RecyclerView rvComments;
    private int code;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_comments);
        getSupportActionBar().setTitle(R.string.comments);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        rvComments = findViewById(R.id.rv_comments);
        rvComments.setHasFixedSize(true);
        rvComments.setLayoutManager(new LinearLayoutManager(this));

        if (getIntent().hasExtra(TAG)) {
            code = getIntent().getIntExtra(TAG, 0);
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
}