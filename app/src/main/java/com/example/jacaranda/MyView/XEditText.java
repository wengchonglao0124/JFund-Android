package com.example.jacaranda.MyView;

import android.content.Context;
import android.graphics.Canvas;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputConnection;

import androidx.appcompat.widget.AppCompatEditText;

public class XEditText extends AppCompatEditText {
    private static final String SPACE = " ";
    private static final int[] DEFAULT_PATTERN = new int[] { 4, 4, 4 ,4};

    private OnTextChangeListener mTextChangeListener;
    private TextWatcher mTextWatcher;

    private int preLength;
    private int currLength;

    private int[] pattern;
    private int[] intervals;
    private String separator;
    private int maxLength;
    private boolean hasNoSeparator;


    public XEditText(Context context) {
        this(context, null);
    }

    public XEditText(Context context, AttributeSet attrs) {
        this(context, attrs, android.R.attr.editTextStyle);
    }

    public XEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        if (separator == null)
            separator = SPACE;
        init();
    }

    private void init() {
        if (getInputType() == InputType.TYPE_CLASS_NUMBER)
            setInputType(InputType.TYPE_CLASS_PHONE);
        setPattern(DEFAULT_PATTERN);
        mTextWatcher = new MyTextWatcher();
        this.addTextChangedListener(mTextWatcher);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
    }

    public void setPattern(int[] pattern) {
        if (pattern == null) {
            throw new IllegalArgumentException("pattern can't be null !");
        }
        this.pattern = pattern;
        intervals = new int[pattern.length];
        int count = 0;
        int sum = 0;
        for (int i = 0; i < pattern.length; i++) {
            sum += pattern[i];
            intervals[i] = sum + count;
            if (i < pattern.length - 1)
                count++;
        }
        maxLength = intervals[intervals.length - 1];
    }

    private class MyTextWatcher implements TextWatcher {

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            preLength = s.length();
            if (mTextChangeListener != null)
                mTextChangeListener.beforeTextChanged(s, start, count, after);
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            currLength = s.length();
            if (hasNoSeparator)
                maxLength = currLength;
            if (currLength > maxLength) {
                getText().delete(currLength - 1, currLength);
                return;
            }
            for (int i = 0; i < pattern.length; i++) {
                if (currLength == intervals[i]) {
                    if (currLength > preLength) {
                        if (currLength < maxLength) {
                            removeTextChangedListener(mTextWatcher);
                            mTextWatcher = null;
                            getText().insert(currLength, separator);
                        }
                    } else if (preLength <= maxLength) {
                        removeTextChangedListener(mTextWatcher);
                        mTextWatcher = null;
                        getText().delete(currLength - 1, currLength);
                    }
                    if (mTextWatcher == null) {
                        mTextWatcher = new MyTextWatcher();
                        addTextChangedListener(mTextWatcher);
                    }
                    break;
                }
            }
            if (mTextChangeListener != null)
                mTextChangeListener.onTextChanged(s, start, before, count);
        }

        @Override
        public void afterTextChanged(Editable s) {
            if (mTextChangeListener != null)
                mTextChangeListener.afterTextChanged(s);
        }
    }

    public interface OnTextChangeListener {
        void beforeTextChanged(CharSequence s, int start, int count, int after);
        void onTextChanged(CharSequence s, int start, int before, int count);
        void afterTextChanged(Editable s);
    }

    @Override
    protected void onSelectionChanged(int selStart, int selEnd) {
        super.onSelectionChanged(selStart, selEnd);
        if(selStart==selEnd){
            setSelection(getText().length());
        }
    }
}
