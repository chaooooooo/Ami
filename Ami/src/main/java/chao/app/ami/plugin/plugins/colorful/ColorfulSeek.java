package chao.app.ami.plugin.plugins.colorful;

import android.content.Context;
import android.content.res.TypedArray;
import androidx.annotation.Nullable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import chao.app.debug.R;
import java.math.RoundingMode;
import java.text.NumberFormat;

/**
 * @author qinchao
 * @since 2018/9/22
 */
public class ColorfulSeek extends LinearLayout implements SeekBar.OnSeekBarChangeListener {


    private float value;

    private float max;

    private float min;

    /**
     * 将seekbar分成amount等分
     */
    private int amount;

    /**
     * 精度
     * 0 表示整数
     * 1 表示保留1位有效数字
     * 2 表示保留2位有效数字
     * ...
     */
    private int precision;

    private float unitGap;

    private SeekBar mSeekBar;

    private TextView mTitleView;

    private TextView mProgressView;

    private OnSeekChangeListener listener;

    public ColorfulSeek(Context context, int min, int max) {
        super(context);
        this.max = max;
        this.min = min;
        init(context, null);
    }

    public ColorfulSeek(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public ColorfulSeek(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {

        setOrientation(HORIZONTAL);
        setGravity(Gravity.CENTER_VERTICAL);

        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.ColorfulSeek, 0, 0);
        String valueText = typedArray.getString(R.styleable.ColorfulSeek_value);
        if (!TextUtils.isEmpty(valueText)) {
            try {
                value = Float.valueOf(valueText);
            } catch (Throwable e) {
                value = 0;
            }
        }

        max = typedArray.getInteger(R.styleable.ColorfulSeek_max, 255);
        min = typedArray.getInteger(R.styleable.ColorfulSeek_min, 0);
        precision = typedArray.getInteger(R.styleable.ColorfulSeek_precision, 0);//默认使用整数
        amount = typedArray.getInteger(R.styleable.ColorfulSeek_amount, 100);
        unitGap = (max - min) / amount;
        String title = typedArray.getString(R.styleable.ColorfulSeek_title);
        typedArray.recycle();

        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.ami_plugin_colorful_seek_item, this, true);
        mSeekBar = (SeekBar) view.findViewById(R.id.ami_plugin_colorful_item_progressbar);
        mTitleView = (TextView) view.findViewById(R.id.ami_plugin_colorful_item_title);
        mProgressView = (TextView) view.findViewById(R.id.ami_plugin_colorful_item_progress);
        mSeekBar.setOnSeekBarChangeListener(this);

        setTitle(title);
        if (max < min) {
            throw new IllegalArgumentException("max必须大于等于min");
        }
        mSeekBar.setMax(amount);

        mProgressView.setText(formatDouble(value));
        mSeekBar.setProgress((int) (value * amount));
    }

    public void setTitle(String title) {
        mTitleView.setText(title);
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        if (listener != null) {
            listener.onValueChanged(this, progress * unitGap);
        }
        mProgressView.setText(formatDouble(progress * unitGap));
    }

    public void setOnSeekChangeListener(OnSeekChangeListener listener) {
        this.listener = listener;
    }

    public interface OnSeekChangeListener {
        void onValueChanged(ColorfulSeek seekBar, float value);
    }

    public String formatDouble(double d) {


        NumberFormat mNumberFormat = NumberFormat.getNumberInstance();

        // 保留两位小数
        mNumberFormat.setMaximumFractionDigits(precision);
        // 如果不需要四舍五入，可以使用RoundingMode.DOWN
        mNumberFormat.setRoundingMode(RoundingMode.UP);

        return mNumberFormat.format(d);
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

    }

    public float getValue() {
        return value;
    }
}
