package chao.app.debugtools.aicai.widgets;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import androidx.annotation.Nullable;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.View;

/**
 * @author qinchao
 * @since 2018/8/31
 */
public class TestView extends View {

    private float mTextHeight = 0;
    private float mFontHeight = 0;

    private TextPaint textPaint = new TextPaint();

    public TestView(Context context) {
        this(context, null);
    }

    public TestView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TestView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        textPaint = new TextPaint();
        textPaint.setFlags(Paint.ANTI_ALIAS_FLAG);
        textPaint.setTextAlign(Paint.Align.CENTER);
        textPaint.setTextSize(30);
        textPaint.setTypeface(Typeface.create(Typeface.SANS_SERIF, Typeface.BOLD));
        textPaint.setColor(Color.RED);

        TextPaint.FontMetrics fontMetrics = textPaint.getFontMetrics();
        mTextHeight = fontMetrics.bottom;
        mFontHeight = mTextHeight - fontMetrics.top; //字体高度

        setWillNotDraw(false);
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawText("Hello World, luqin", 0, mFontHeight, textPaint);

//        canvas.drawText("luqin", 0, 30, textPaint);
    }
}
