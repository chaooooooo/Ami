package chao.app.debugtools.aicai.widgets;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.support.v7.widget.AppCompatImageButton;
import android.text.TextPaint;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.TypedValue;
import chao.app.debugtools.R;


/**
 * @author Created by diaowanjun on 2015/06/19.
 */
public class NoticeImageView extends AppCompatImageButton {

    private String text;
    private float textSize = 0;
    private int textColor = Color.WHITE;
    private float circleSize = 0;
    private int circleColor = Color.RED;
    private Paint paint, paintdot;
    private TextPaint textPaint;
    private float mTextWidth;
    private float mTextHeight;
    private float mFontHeight;
    private static boolean circle = false;

    private int maxTextLength = 1;//最大数字长度， 默认2及数值99，如果超过99则显示99+

    private Paint.FontMetrics fontMetrics;

    public NoticeImageView(Context context) {
        super(context);
    }

    public NoticeImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        final TypedArray ta = getContext().obtainStyledAttributes(
                attrs, R.styleable.NoticeImageView);
        text = ta.getString(R.styleable.NoticeImageView_noticeText);
        textSize = ta.getDimensionPixelOffset(R.styleable.NoticeImageView_noticeTextSize, (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 9.0f, context.getResources().getDisplayMetrics()));
        textColor = ta.getColor(R.styleable.NoticeImageView_noticeTextColor, textColor);
        circleSize = ta.getDimension(R.styleable.NoticeImageView_noticeCircleSize, circleSize);
        circleColor = ta.getColor(R.styleable.NoticeImageView_noticeCircleColor, circleColor);
        ta.recycle();

        textPaint = new TextPaint();
        textPaint.setFlags(Paint.ANTI_ALIAS_FLAG);
        textPaint.setTextAlign(Paint.Align.CENTER);
        textPaint.setTypeface(Typeface.create(Typeface.SANS_SERIF, Typeface.BOLD));

        paint = new Paint();
        paint.setAntiAlias(true); //消除锯齿
        paint.setStyle(Paint.Style.FILL);

        paintdot = new Paint();
        paintdot.setAntiAlias(true);
        paintdot.setStyle(Paint.Style.FILL);

        invalidateTextPaintAndMeasurements();
    }

    private void invalidateTextPaintAndMeasurements() {
        textPaint.setTextSize(textSize);
        textPaint.setColor(textColor);
        mTextWidth = textPaint.measureText(text);
        fontMetrics = textPaint.getFontMetrics();
        mTextHeight = fontMetrics.bottom;
        mFontHeight = mTextHeight - fontMetrics.top; //字体高度

        paint.setColor(circleColor);
        paintdot.setColor(Color.WHITE);
        postInvalidate();
    }

    StringBuilder maxText = new StringBuilder();
    RectF rectF = new RectF();
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if (!text.equals("0") && !TextUtils.isEmpty(text)) {
            if (text.length() > maxTextLength) {
                if (maxText.length() > 0) {
                    maxText.delete(0, maxText.length());
                }
                for (int i=0;i<maxTextLength;i++) {
                    maxText.append("9");
                }
                maxText.append("+");
                text = maxText.toString();
            }

            float single = textPaint.measureText("9");
            float fontWidth = textPaint.measureText(text);
            float width = fontWidth + circleSize * 2 - single;

            rectF.right = getWidth();
            rectF.left = rectF.right - width;
            rectF.top = 6;
            rectF.bottom = rectF.top + 2 * circleSize;

            float x = (rectF.left + rectF.right) / 2;
            float y = (rectF.top + rectF.bottom) / 2 - (fontMetrics.bottom + fontMetrics.top)/2;

            canvas.drawRoundRect(rectF, circleSize, circleSize, paint);
            canvas.drawText(text, x, y, textPaint);

        }

        if (circle){
//            canvas.drawCircle(circleX, circleY, (float) (circleSize*0.7), paint);
        }
        postInvalidate();//重新绘制
    }

    public void setText(String text) {
        this.text = text;
        invalidateTextPaintAndMeasurements();
    }

    public void setCircle(boolean circle) {
        NoticeImageView.circle = circle;
        postInvalidate();//重新绘制
    }

    public float getTextSize() {
        return textSize;
    }

    public void setTextSize(float textSize) {
        this.textSize = textSize;
        invalidateTextPaintAndMeasurements();
    }

    public int getTextColor() {
        return textColor;
    }

    public void setTextColor(int textColor) {
        this.textColor = textColor;
        invalidateTextPaintAndMeasurements();
    }

    public float getCircleSize() {
        return circleSize;
    }

    public void setCircleSize(float circleSize) {
        this.circleSize = circleSize;
        invalidateTextPaintAndMeasurements();
    }

    public int getCircleColor() {
        return circleColor;
    }

    public void setCircleColor(int circleColor) {
        this.circleColor = circleColor;
        invalidateTextPaintAndMeasurements();
    }

    public void setMaxTextLength(int maxTextLength) {
        this.maxTextLength = maxTextLength;
    }
}
