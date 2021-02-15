package com.android.onmarcy;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.Window;

import androidx.annotation.NonNull;

public class TermsOfServiceDialog extends Dialog implements View.OnClickListener {

    OnMyDialogResult dialogResult;

    public TermsOfServiceDialog(@NonNull Context context) {
        super(context);
    }

    public interface OnMyDialogResult{
        void finish(boolean isClose);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_terms_of_service);

        findViewById(R.id.btn_close).setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_close:
                dialogResult.finish(true);
                break;
            default:
                break;
        }
        dismiss();
    }
}
