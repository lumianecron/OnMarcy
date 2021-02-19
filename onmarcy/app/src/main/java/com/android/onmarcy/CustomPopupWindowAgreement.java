package com.android.onmarcy;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.Gravity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.core.content.ContextCompat;

public class CustomPopupWindowAgreement extends AppCompatTextView {
    private Drawable background;
    private int textColor;
    public static String errMessage;

    public CustomPopupWindowAgreement(@NonNull Context context) {
        super(context);
        init();
    }

    public CustomPopupWindowAgreement(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public CustomPopupWindowAgreement(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        setBackground(background);
        setTextColor(textColor);
        setTextSize(14.f);
        setGravity(Gravity.CENTER);
        setText(errMessage);
        setPadding(30, 30, 30, 30);
    }

    private void init() {
        textColor = ContextCompat.getColor(getContext(), R.color.white);
        background = getResources().getDrawable(R.drawable.custom_popup_window_agreement);
    }
}
