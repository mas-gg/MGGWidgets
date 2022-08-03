package com.wx.demo.view.widgets.contentscrollable;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.BlurMaskFilter;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.text.TextPaint;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Interpolator;

import com.wx.demo.R;

@SuppressWarnings("unused")
public class ContentScrollableTextView extends View {

    public enum ScrollDirection {
        ANY, UP, DOWN
    }

    private static final int DEFAULT_TEXT_SIZE = 12;
    private static final int DEFAULT_TEXT_COLOR = Color.BLACK;
    private static final int DEFAULT_ANIMATION_DURATION = 350;
    private static final Interpolator DEFAULT_ANIMATION_INTERPOLATOR =
            new AccelerateDecelerateInterpolator();
    private static final int DEFAULT_GRAVITY = Gravity.START;

    protected final Paint textPaint = new TextPaint(Paint.ANTI_ALIAS_FLAG);

    private final ContentDrawMetrics metrics = new ContentDrawMetrics(textPaint);
    private final ContentColumnManager columnManager = new ContentColumnManager(metrics);
    private final ValueAnimator animator = ValueAnimator.ofFloat(1f);

    private final Rect viewBounds = new Rect();

    private String text;

    private int lastMeasuredDesiredWidth, lastMeasuredDesiredHeight;

    private int gravity;
    private int textColor;
    private float textSize;
    private long animationDelayInMillis;
    private long animationDurationInMillis;
    private Interpolator animationInterpolator;
    private boolean animateMeasurementChange;
    private String pendingTextToSet;

    public ContentScrollableTextView(Context context) {
        this(context, null);
    }

    public ContentScrollableTextView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ContentScrollableTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs, defStyleAttr);
    }

    protected void init(Context context, AttributeSet attrs, int defStyleAttr) {
        final Resources res = context.getResources();
        final StyledAttributes styledAttributes = new StyledAttributes(res);

        final TypedArray arr = context.obtainStyledAttributes(attrs, R.styleable.ContentScrollableTextView,
                defStyleAttr, 0);

        final int textAppearanceResId = arr.getResourceId(
                R.styleable.ContentScrollableTextView_android_textAppearance, -1);

        if (textAppearanceResId != -1) {
            final TypedArray textAppearanceArr = context.obtainStyledAttributes(
                    textAppearanceResId, R.styleable.ContentScrollableTextView);
            styledAttributes.applyTypedArray(textAppearanceArr);
            textAppearanceArr.recycle();
        }

        styledAttributes.applyTypedArray(arr);

        animationInterpolator = DEFAULT_ANIMATION_INTERPOLATOR;
        this.animationDurationInMillis = arr.getInt(
                R.styleable.ContentScrollableTextView_scroll_duration, DEFAULT_ANIMATION_DURATION);
        this.animateMeasurementChange = arr.getBoolean(
                R.styleable.ContentScrollableTextView_scrolling_measurement_change, false);
        this.gravity = styledAttributes.gravity;

        if (styledAttributes.shadowColor != 0) {
            textPaint.setShadowLayer(styledAttributes.shadowRadius, styledAttributes.shadowDx,
                    styledAttributes.shadowDy, styledAttributes.shadowColor);
        }

        Typeface typeface = getTypefaceByTextStyle(styledAttributes.textStyle, textPaint.getTypeface());

        setTypeface(typeface);

        setTextColor(styledAttributes.textColor);
        setTextSize(styledAttributes.textSize);

        final int contentType = arr.getInt(R.styleable.ContentScrollableTextView_content_type, 1);
        switch (contentType) {
            case 1:
                setContentList(ContentUtils.provideNumberList());
                break;
            case 2:
                setContentList(ContentUtils.provideAlphabeticalList());
                break;
            default:
                if (isInEditMode()) {
                    setContentList(ContentUtils.provideNumberList());
                }
        }

        final int scrollDirection = arr.getInt(R.styleable.ContentScrollableTextView_scroll_direction, 0);
        switch (scrollDirection) {
            case 0:
                metrics.setScrollDirection(ScrollDirection.ANY);
                break;
            case 1:
                metrics.setScrollDirection(ScrollDirection.UP);
                break;
            case 2:
                metrics.setScrollDirection(ScrollDirection.DOWN);
                break;
            default:
//                CDException.handleException("Unsupported scrollDirection: " + scrollDirection);
        }

        if (isCharacterListsSet()) {
            setText(styledAttributes.text, false);
        } else {
            this.pendingTextToSet = styledAttributes.text;
        }

        arr.recycle();

        animator.addUpdateListener(animation -> {
            columnManager.setAnimationProgress(
                    animation.getAnimatedFraction());
            checkForRelayout();
            invalidate();
        });
        animator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                columnManager.onAnimationEnd();
                checkForRelayout();
                invalidate();
            }
        });
    }

    private static class StyledAttributes {
        int gravity;
        int shadowColor;
        float shadowDx;
        float shadowDy;
        float shadowRadius;
        String text;
        int textColor;
        float textSize;
        int textStyle;

        StyledAttributes(Resources res) {
            textColor = DEFAULT_TEXT_COLOR;
            textSize = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP,
                    DEFAULT_TEXT_SIZE, res.getDisplayMetrics());
            gravity = DEFAULT_GRAVITY;
        }

        void applyTypedArray(TypedArray arr) {
            gravity = arr.getInt(R.styleable.ContentScrollableTextView_android_gravity, gravity);
            shadowColor = arr.getColor(R.styleable.ContentScrollableTextView_android_shadowColor,
                    shadowColor);
            shadowDx = arr.getFloat(R.styleable.ContentScrollableTextView_android_shadowDx, shadowDx);
            shadowDy = arr.getFloat(R.styleable.ContentScrollableTextView_android_shadowDy, shadowDy);
            shadowRadius = arr.getFloat(R.styleable.ContentScrollableTextView_android_shadowRadius,
                    shadowRadius);
            text = arr.getString(R.styleable.ContentScrollableTextView_android_text);
            textColor = arr.getColor(R.styleable.ContentScrollableTextView_android_textColor, textColor);
            textSize = arr.getDimension(R.styleable.ContentScrollableTextView_android_textSize, textSize);
            textStyle = arr.getInt(R.styleable.ContentScrollableTextView_android_textStyle, textStyle);
        }
    }

    public void setContentList(String... contentList) {
        columnManager.setContentList(contentList);
        if (pendingTextToSet != null) {
            setText(pendingTextToSet, false);
            pendingTextToSet = null;
        }
    }

    public boolean isCharacterListsSet() {
        return columnManager.getCharacterLists() != null;
    }

    public void setText(String text) {
        setText(text, !TextUtils.isEmpty(this.text));
    }

    public void setText(String text, boolean animate) {
        if (TextUtils.equals(text, this.text)) {
            return;
        }

        if (animator.isRunning()) {
            animator.cancel();
        }

        this.text = text;
        final char[] targetText = text == null ? new char[0] : text.toCharArray();

        columnManager.setText(targetText);
        setContentDescription(text);

        if (animate) {
            animator.setStartDelay(animationDelayInMillis);
            animator.setDuration(animationDurationInMillis);
            animator.setInterpolator(animationInterpolator);
            animator.start();
        } else {
            columnManager.setAnimationProgress(1f);
            columnManager.onAnimationEnd();
            checkForRelayout();
            invalidate();
        }
    }

    public String getText() {
        return text;
    }

    public int getTextColor() {
        return textColor;
    }

    public void setTextColor(int color) {
        if (this.textColor != color) {
            textColor = color;
            textPaint.setColor(textColor);
            invalidate();
        }
    }

    public float getTextSize() {
        return textSize;
    }

    public void setTextSize(float textSize) {
        if (this.textSize != textSize) {
            this.textSize = textSize;
            textPaint.setTextSize(textSize);
            onTextPaintMeasurementChanged();
        }
    }

    public Typeface getTypeface() {
        return textPaint.getTypeface();
    }

    public void setTypeface(Typeface typeface) {
        if (typeface == null) {
            return;
        }
        textPaint.setTypeface(typeface);
        onTextPaintMeasurementChanged();
    }

    private Typeface getTypefaceByTextStyle(int textStyle, Typeface typeface) {
        switch (textStyle) {
            case Typeface.BOLD_ITALIC:
                return Typeface.create(typeface, Typeface.BOLD_ITALIC);
            case Typeface.BOLD:
                return Typeface.create(typeface, Typeface.BOLD);
            case Typeface.ITALIC:
                return Typeface.create(typeface, Typeface.ITALIC);
            default:
                return typeface;
        }
    }

    public long getAnimationDelay() {
        return animationDelayInMillis;
    }

    public void setAnimationDelay(long animationDelayInMillis) {
        this.animationDelayInMillis = animationDelayInMillis;
    }

    public long getAnimationDuration() {
        return animationDurationInMillis;
    }

    public void setAnimationDuration(long animationDurationInMillis) {
        this.animationDurationInMillis = animationDurationInMillis;
    }

    public Interpolator getAnimationInterpolator() {
        return animationInterpolator;
    }

    public void setAnimationInterpolator(Interpolator animationInterpolator) {
        this.animationInterpolator = animationInterpolator;
    }

    public void setPreferredScrollingDirection(ScrollDirection direction) {
        this.metrics.setScrollDirection(direction);
    }

    public int getGravity() {
        return gravity;
    }

    public void setGravity(int gravity) {
        if (this.gravity != gravity) {
            this.gravity = gravity;
            invalidate();
        }
    }

    public void setAnimateMeasurementChange(boolean animateMeasurementChange) {
        this.animateMeasurementChange = animateMeasurementChange;
    }

    public boolean getAnimateMeasurementChange() {
        return animateMeasurementChange;
    }

    public void addAnimatorListener(Animator.AnimatorListener animatorListener) {
        animator.addListener(animatorListener);
    }

    public void removeAnimatorListener(Animator.AnimatorListener animatorListener) {
        animator.removeListener(animatorListener);
    }

    public void setPaintFlags(int flags) {
        textPaint.setFlags(flags);
        onTextPaintMeasurementChanged();
    }

    public void setBlurMaskFilter(BlurMaskFilter.Blur style, float radius) {
        if (style != null && radius > 0f) {
            BlurMaskFilter filter = new BlurMaskFilter(radius, style);
            textPaint.setMaskFilter(filter);
        } else {
            setLayerType(View.LAYER_TYPE_SOFTWARE, null);
            textPaint.setMaskFilter(null);
        }
    }

    private void checkForRelayout() {
        final boolean widthChanged = lastMeasuredDesiredWidth != computeDesiredWidth();
        final boolean heightChanged = lastMeasuredDesiredHeight != computeDesiredHeight();

        if (widthChanged || heightChanged) {
            requestLayout();
        }
    }

    private int computeDesiredWidth() {
        final int contentWidth = (int) (animateMeasurementChange ?
                columnManager.getCurrentWidth() : columnManager.getMinimumRequiredWidth());
        return contentWidth + getPaddingLeft() + getPaddingRight();
    }

    private int computeDesiredHeight() {
        return (int) metrics.getCharHeight() + getPaddingTop() + getPaddingBottom();
    }

    private void onTextPaintMeasurementChanged() {
        metrics.invalidate();
        checkForRelayout();
        invalidate();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        lastMeasuredDesiredWidth = computeDesiredWidth();
        lastMeasuredDesiredHeight = computeDesiredHeight();

        int desiredWidth = resolveSize(lastMeasuredDesiredWidth, widthMeasureSpec);
        int desiredHeight = resolveSize(lastMeasuredDesiredHeight, heightMeasureSpec);

        setMeasuredDimension(desiredWidth, desiredHeight);
    }

    @Override
    protected void onSizeChanged(int width, int height, int oldw, int oldh) {
        super.onSizeChanged(width, height, oldw, oldh);
        viewBounds.set(getPaddingLeft(), getPaddingTop(), width - getPaddingRight(),
                height - getPaddingBottom());
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        canvas.save();

        realignAndClipCanvasForGravity(canvas);

        canvas.translate(0f, metrics.getCharBaseline());

        columnManager.draw(canvas, textPaint);

        canvas.restore();
    }

    private void realignAndClipCanvasForGravity(Canvas canvas) {
        final float currentWidth = columnManager.getCurrentWidth();
        final float currentHeight = metrics.getCharHeight();
        realignAndClipCanvasForGravity(canvas, gravity, viewBounds, currentWidth, currentHeight);
    }

    private static void realignAndClipCanvasForGravity(Canvas canvas, int gravity, Rect viewBounds,
                                                       float currentWidth, float currentHeight) {
        final int availableWidth = viewBounds.width();
        final int availableHeight = viewBounds.height();

        float translationX = 0;
        float translationY = 0;
        if ((gravity & Gravity.CENTER_VERTICAL) == Gravity.CENTER_VERTICAL) {
            translationY = viewBounds.top + (availableHeight - currentHeight) / 2f;
        }
        if ((gravity & Gravity.CENTER_HORIZONTAL) == Gravity.CENTER_HORIZONTAL) {
            translationX = viewBounds.left + (availableWidth - currentWidth) / 2f;
        }
        if ((gravity & Gravity.TOP) == Gravity.TOP) {
            translationY = 0;
        }
        if ((gravity & Gravity.BOTTOM) == Gravity.BOTTOM) {
            translationY = viewBounds.top + (availableHeight - currentHeight);
        }
        if ((gravity & Gravity.START) == Gravity.START) {
            translationX = 0;
        }
        if ((gravity & Gravity.END) == Gravity.END) {
            translationX = viewBounds.left + (availableWidth - currentWidth);
        }

        canvas.translate(translationX, translationY);
        canvas.clipRect(0f, 0f, currentWidth, currentHeight);
    }
}
