package com.haoxue.haotianqi.view;

import com.haoxue.haotianqi.R;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.widget.FrameLayout;

/**
 * 仿window的圆点加载进度（布局文件）
 */
public class SpotsDialogLayout extends FrameLayout {

    private static final int DEFAULT_COUNT = 5;
    private int spotsCount;

    public SpotsDialogLayout(Context context) {
        this(context, null);
    }

    public SpotsDialogLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SpotsDialogLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs, defStyleAttr, 0);
    }

    private void init(AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        TypedArray a = getContext().getTheme().obtainStyledAttributes(
                attrs,
                R.styleable.Dialog,
                defStyleAttr, defStyleRes);

        spotsCount = a.getInt(R.styleable.Dialog_DialogSpotCount, DEFAULT_COUNT);
        a.recycle();
    }

    public int getSpotsCount() {
        return spotsCount;
    }
}
