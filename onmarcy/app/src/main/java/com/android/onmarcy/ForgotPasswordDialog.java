package com.android.onmarcy;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.material.textfield.TextInputEditText;

public class ForgotPasswordDialog extends Dialog {
    private TextInputEditText edtEmail;
    private Button btnSend;
    OnMyDialogResult onMyDialogResult;

    public ForgotPasswordDialog(@NonNull Context context) {
        super(context);
    }

    public interface OnMyDialogResult {
        void finish(String email);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.forgot_password_dialog);
        edtEmail = findViewById(R.id.edt_email);
        btnSend = findViewById(R.id.btn_send);

        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (TextUtils.isEmpty(edtEmail.getText().toString())) {
                    edtEmail.setError(getContext().getResources().getString(R.string.please_fill_out_this_field));
                } else {
                    onMyDialogResult.finish(edtEmail.getText().toString());
                    dismiss();
                }
            }
        });
    }
}
