package com.example.jacaranda.MyView;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.text.InputType;
import android.util.AttributeSet;

import androidx.appcompat.widget.AppCompatEditText;

import com.example.jacaranda.R;

import java.util.ArrayList;
import java.util.List;

public class PwdEditText extends AppCompatEditText {

    private Paint textPaint, emptyPaint;
    private Context mC;
    private int spzceX ,spzceY;
    private int Wide;
    private String mText;
    private int textLength;
    private List<RectF> rectFS;
    public PwdEditText(Context context) {
        super(context);
        mC = context;
        init();
    }

    public PwdEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        mC = context;
        init();
    }

    public PwdEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mC = context;
        init();
    }


    interface OnTextChangeListeven{
        void onTextChange(String pwd);
    }
    private OnTextChangeListeven onTextChangeListeven;
    public void setOnTextChangeListeven(OnTextChangeListeven onTextChangeListeven){
        this.onTextChangeListeven = onTextChangeListeven;
    }
    public void clearText(){
        setText("");
        setInputType(InputType.TYPE_CLASS_NUMBER|InputType.TYPE_NUMBER_VARIATION_NORMAL);
    }

    private void init() {
        setTextColor(0X00ffffff);
        setInputType(InputType.TYPE_CLASS_NUMBER|InputType.TYPE_NUMBER_VARIATION_NORMAL);
        spzceX = dp2px(0);
        spzceY = dp2px(0);
        Wide  = dp2px(25);

        emptyPaint = new Paint();
        emptyPaint.setAntiAlias(true);
        textPaint = new Paint();

        rectFS = new ArrayList<>();

        mText = "" ;
        textLength = 6;

        this.setBackgroundDrawable(null);
        setLongClickable(false);
        setTextIsSelectable(false);
        setCursorVisible(false);

    }

    @Override
    protected void onTextChanged(CharSequence text, int start, int lengthBefore, int lengthAfter) {
        super.onTextChanged(text, start, lengthBefore, lengthAfter);
        if (mText == null) {return;}
        if (text.toString().length() <= textLength){
            mText = text.toString();
        }else{
            setText(mText);
            setSelection(getText().toString().length());
            setInputType(InputType.TYPE_CLASS_NUMBER|InputType.TYPE_NUMBER_VARIATION_NORMAL);
        }
        if (onTextChangeListeven != null) onTextChangeListeven.onTextChange(mText);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);

        switch (heightMode){
            case MeasureSpec.EXACTLY:
                heightSize = MeasureSpec.getSize(heightMeasureSpec);
                break;
            case MeasureSpec.AT_MOST:
                heightSize = widthSize/textLength;
                break;
        }
        setMeasuredDimension(widthSize,heightSize);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        emptyPaint.setTextSize(18);
        emptyPaint.setStyle(Paint.Style.STROKE);
        emptyPaint.setColor(Color.parseColor("#979797"));

        textPaint.setTextSize(18);
        textPaint.setStyle(Paint.Style.FILL);
        textPaint.setColor(Color.parseColor("#522479"));

        for (int i = 0; i < textLength; i++) {
            RectF rect = new RectF(i * Wide + spzceX, spzceY,i * Wide + Wide - spzceX,Wide - spzceY);
            rectFS.add(rect);

            canvas.drawCircle(rectFS.get(i).centerX(),rectFS.get(i).centerY(),dp2px(5),emptyPaint);
        }

        for (int j = 0; j < mText.length(); j++) {
            canvas.drawCircle(rectFS.get(j).centerX(),rectFS.get(j).centerY(),dp2px(5),textPaint);
        }
    }

    private int dp2px(float dpValue){
        float scale=mC.getResources().getDisplayMetrics().density;
        return (int)(dpValue*scale+0.5f);
    }

}