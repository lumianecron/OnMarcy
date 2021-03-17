package com.android.onmarcy;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;

import androidx.annotation.NonNull;

import java.util.Calendar;
import java.util.Date;

public class FilterDialog extends Dialog {
    RadioButton rbActive, rbProgress, rbCompleted;
    EditText edtLowest, edtHighest, edtYear;
    LinearLayout containerStatus;
    private int type;
    private int status;
    private int lowest, highest;
    private int year;
    OnMyDialogResult onMyDialogResult;

    public FilterDialog(@NonNull Context context, int type) {
        super(context);
        this.type = type;
    }

    public interface OnMyDialogResult{
        void finish(int status, int lowest, int highest, int year);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.filter_dialog);

        rbActive = findViewById(R.id.rb_active);
        rbProgress = findViewById(R.id.rb_progress);
        rbCompleted = findViewById(R.id.rb_completed);
        edtLowest = findViewById(R.id.edt_lowest_price);
        edtHighest = findViewById(R.id.edt_highest_price);
        containerStatus = findViewById(R.id.container_status);
        edtYear = findViewById(R.id.edt_year);

        if(this.type == 2){ //Marketer
            containerStatus.setVisibility(View.GONE);
        }

        findViewById(R.id.btn_filter).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(rbActive.isChecked()) status = 1;
                if(rbProgress.isChecked()) status = 2;
                if(rbCompleted.isChecked()) status = 3;
                lowest = (!TextUtils.isEmpty(edtLowest.getText().toString())) ? Integer.parseInt(edtLowest.getText().toString()) : Integer.MIN_VALUE;
                highest = (!TextUtils.isEmpty(edtHighest.getText().toString())) ? Integer.parseInt(edtHighest.getText().toString()) : Integer.MAX_VALUE;
                year  = (!TextUtils.isEmpty(edtYear.getText().toString())) ? Integer.parseInt(edtYear.getText().toString()) : 0;
                onMyDialogResult.finish(status, lowest, highest, year);
                dismiss();
            }
        });
    }
}
