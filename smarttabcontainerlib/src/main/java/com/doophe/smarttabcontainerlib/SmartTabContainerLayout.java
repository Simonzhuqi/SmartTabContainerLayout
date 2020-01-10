package com.doophe.smarttabcontainerlib;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

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
            measureChild(child,widthMeasureSpec,heightMeasureSpec);

            lineTotalWidth += child.getMeasuredWidth();
            if(lineTotalWidth > mWidth - leftPadding - rightPadding){
                totalLine++;
                lineTotalWidth = child.getMeasuredWidth();
            }
            lineTotalWidth += mHorizontalSpan;

            if(mHeight == 0){
                mHeight = (int) (child.getMeasuredHeight()+mVerticalSpan);
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

            lineTotalWidth += child.getMeasuredWidth();
            if(lineTotalWidth > mWidth- leftPadding - rightPadding){
                totalLine++;
                lineTotalWidth = child.getMeasuredWidth();
            }

            l = lineTotalWidth - child.getMeasuredWidth() + leftPadding;
            t = (int) (totalLine * (child.getMeasuredHeight() + mVerticalSpan)) + topPadding;
            r = l + child.getMeasuredWidth();
            b = t + child.getMeasuredHeight();

            child.layout(l, t, r, b);

            lineTotalWidth += mHorizontalSpan;
        }
    }

    private View createTabView(String t) {
        TextView textView = new TextView(getContext());
        textView.setPadding(mTabPaddingLeft,mTabPaddingTop,mTabPaddingRight,mTabPaddingBottom);
        textView.setText(t);
        textView.setGravity(Gravity.CENTER);
        textView.setBackgroundResource(mTabBackgroundRes);
        textView.setTextSize(mTabTextSize);
        textView.setTextColor(mTabTextColor);
        return textView;
    }

    /**
     * 添加标签
     * @param t 标签上显示的文字
     */
    public void addTab(String t){
        if (t != null && !t.trim().isEmpty()) {
            addView(createTabView(t), getChildCount());
        }
    }


    /**
     * 批量添加标签
     * @param tabs
     */
    public void addTabs(String[] tabs){
        if (tabs != null && tabs.length > 0) {
            for (String tab : tabs) {
                addTab(tab);
            }

        }
    }







}
