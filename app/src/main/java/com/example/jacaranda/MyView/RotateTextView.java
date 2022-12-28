package com.example.jacaranda.MyView;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Shader;
import android.util.AttributeSet;


public class RotateTextView extends androidx.appcompat.widget.AppCompatTextView {
    public RotateTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    private LinearGradient mLinearGradient;
    @Override
    protected void onDraw(Canvas canvas) {
        Paint p = new Paint();
        mLinearGradient = new LinearGradient(-20, -5, getWidth()+10, getHeight()/2,
                new int[]{Color.parseColor("#f05f57"), Color.parseColor("#ae0a0a")}, null,
                Shader.TileMode.REPEAT);
        p.setShader(mLinearGradient);
        p.setStyle(Paint.Style.FILL);
        canvas.rotate(30, getMeasuredWidth() / 2, getMeasuredHeight() / 2);
        canvas.translate(11,2);
        canvas.drawRect(-20, -5, getWidth()+10, getHeight()/2, p);
        super.onDraw(canvas);
    }
}
