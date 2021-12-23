package com.krishiv.myislam.utils;

import android.content.Context;
import android.widget.TextView;

public class Resizable  extends android.support.v7.widget.AppCompatTextView {
    public Resizable(Context context) {
        super(context);

    }
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int width = MeasureSpec.getSize(widthMeasureSpec);
        setMeasuredDimension(width, width);
    }
}
