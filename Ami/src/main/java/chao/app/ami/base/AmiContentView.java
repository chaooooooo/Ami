package chao.app.ami.base;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.FrameLayout;
import chao.app.ami.plugin.MovementLayout;

/**
 * @author qinchao
 * @since 2018/9/10
 */
public class AmiContentView extends FrameLayout {

    private MovementLayout movementLayout;

    public AmiContentView(@NonNull Context context) {
        super(context);
        init();
    }

    public AmiContentView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public AmiContentView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        movementLayout = new MovementLayout(this);
    }

    public MovementLayout getMovementLayout() {
        return movementLayout;
    }
}
