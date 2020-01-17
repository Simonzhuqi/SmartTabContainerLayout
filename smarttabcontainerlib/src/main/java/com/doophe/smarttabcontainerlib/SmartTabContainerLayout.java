package com.doophe.smarttabcontainerlib;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * @author simon
 * @time 2020-01-09
 */
public class SmartTabContainerLayout extends ViewGroup {

    private int mWidth;
    private int mHeight;
    private int mTabBackgroundRes;
    private float mTabTextSize;
    private int mTabTextColor;
    /**
     * Tab的内边距
     */
    private int mTabPaddingLeft, mTabPaddingTop, mTabPaddingRight, mTabPaddingBottom;
    /**
     * Tab左右间距
     */
    private float mHorizontalSpan;
    /**
     * 行间距
     */
    private float mVerticalSpan;

    private Callback mCallback;

    public SmartTabContainerLayout(Context context) {
        super(context);
    }

    public SmartTabContainerLayout(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public SmartTabContainerLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initAttr(context, attrs);

    }

    private void initAttr(Context context, AttributeSet attrs) {
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.TabContainerLayout);
        mTabBackgroundRes = typedArray.getResourceId(R.styleable.TabContainerLayout_tab_background,R.drawable.default_bg_tab);
        mTabTextSize = typedArray.getDimension(R.styleable.TabContainerLayout_tab_text_size, 20);
        mTabTextColor = typedArray.getColor(R.styleable.TabContainerLayout_tab_text_color, Color.GRAY);
        mHorizontalSpan = typedArray.getDimension(R.styleable.TabContainerLayout_tab_horizontal_span,20);
        mVerticalSpan = typedArray.getDimension(R.styleable.TabContainerLayout_tab_vertical_span,20);
        mTabPaddingLeft = (int) typedArray.getDimension(R.styleable.TabContainerLayout_tab_padding_left,20);
        mTabPaddingTop = (int) typedArray.getDimension(R.styleable.TabContainerLayout_tab_padding_top,5);
        mTabPaddingRight = (int) typedArray.getDimension(R.styleable.TabContainerLayout_tab_padding_right, 20);
        mTabPaddingBottom = (int) typedArray.getDimension(R.styleable.TabContainerLayout_tab_padding_bottom, 5);
        typedArray.recycle();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        mWidth = View.MeasureSpec.getSize(widthMeasureSpec);
        mHeight = 0;
        int leftPadding = getPaddingLeft();
        int rightPadding = getPaddingRight();
        int topPadding = getPaddingTop();
        int bottomPadding = getPaddingBottom();
        int lineTotalWidth = 0;
        int totalLine = 1;
        int childCount = getChildCount();
        for (int i = 0; i < childCount; i++) {
            View child = getChildAt(i);
            //在此测量子View
            measureChild(child,widthMeasureSpec,heightMeasureSpec);
            //测量后子View的宽
            int childWidth = child.getMeasuredWidth();
            //测量后子View的高
            int childHeight = child.getMeasuredHeight();

            lineTotalWidth += childWidth;
            if(lineTotalWidth > mWidth - leftPadding - rightPadding){
                totalLine++;
                lineTotalWidth = childWidth;
            }
            lineTotalWidth += mHorizontalSpan;

            if(mHeight == 0){
                mHeight = (int) (childHeight + mVerticalSpan);
            }
        }
        if (mHeight != 0) {
            mHeight = (int) (totalLine*mHeight - mVerticalSpan) + topPadding + bottomPadding;
        }
        setMeasuredDimension(mWidth,mHeight);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {

        int lineTotalWidth = 0;
        int totalLine = 0;
        int childCount = getChildCount();

        int leftPadding = getPaddingLeft();
        int rightPadding = getPaddingRight();
        int topPadding = getPaddingTop();

        for (int i = 0; i < childCount; i++) {
            View child = getChildAt(i);
            if(child.getVisibility() == GONE){
                continue;
            }
            int childWidth = child.getMeasuredWidth();
            int childHeight = child.getMeasuredHeight();

            lineTotalWidth += childWidth;
            if (lineTotalWidth > mWidth - leftPadding - rightPadding) {
                totalLine++;
                lineTotalWidth = childWidth;
            }

            l = lineTotalWidth - childWidth + leftPadding;
            t = (int) (totalLine * (childHeight + mVerticalSpan)) + topPadding;
            r = l + childWidth;
            b = t + childHeight;

            child.layout(l, t, r, b);

            lineTotalWidth += mHorizontalSpan;
        }
    }

    private View createTabView(final String t) {
        TextView textView = new TextView(getContext());
        textView.setPadding(mTabPaddingLeft, mTabPaddingTop, mTabPaddingRight, mTabPaddingBottom);
        textView.setText(t);
        textView.setGravity(Gravity.CENTER);
        textView.setBackgroundResource(mTabBackgroundRes);
        textView.setTextSize(mTabTextSize);
        textView.setTextColor(mTabTextColor);
        textView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mCallback != null) {
                    mCallback.onTabClickListener(v, t);
                }
            }
        });
        textView.setOnLongClickListener(new OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if (mCallback != null) {
                    mCallback.onTabLongClickListener(v, t);
                }
                return false;
            }
        });
        return textView;
    }

    /**
     * 添加标签
     *
     * @param t 标签上显示的文字
     */
    public void addTab(String t) {
        if (t != null && !t.trim().isEmpty()) {
            addView(createTabView(t), getChildCount());
        }
    }

    /**
     * 批量添加标签
     *
     * @param tabs
     */
    public void addTabs(String[] tabs) {
        if (tabs != null && tabs.length > 0) {
            for (String tab : tabs) {
                addTab(tab);
            }

        }
    }


    /**
     * 监听回调（单击和长按的回调）
     * @param callback
     */
    public void addCallback(Callback callback) {
        this.mCallback = callback;
    }

    public interface Callback {
        void onTabClickListener(View view, String value);

        void onTabLongClickListener(View view, String value);
    }


}
