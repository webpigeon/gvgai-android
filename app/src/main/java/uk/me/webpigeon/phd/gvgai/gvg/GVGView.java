package uk.me.webpigeon.phd.gvgai.gvg;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import java.util.List;

/**
 * Android version of the GVG viewer class
 */
public class GVGView extends View {
    private Paint background;
    private List<String> spriteGroups;

    public GVGView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public GVGView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public GVGView(Context context) {
        super(context);
    }

    private void init() {
        background = new Paint();
        background.setColor(Color.BLACK);
    }

    public void render(Canvas c) {
        //render a static background
        c.drawRect(0,0,getWidth(), getHeight(), background);

        //rendering time!
        if ( spriteGroups == null ) {
            return;
        }

        int[] gameSpriteOrder = null;
        for (Integer spriteTypeInt : gameSpriteOrder) {
        }

    }


}
