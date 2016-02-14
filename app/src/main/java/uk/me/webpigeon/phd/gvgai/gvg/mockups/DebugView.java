package uk.me.webpigeon.phd.gvgai.gvg.mockups;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;


import ontology.Types;
import uk.me.webpigeon.phd.gvgai.R;
import uk.me.webpigeon.phd.gvgai.gvg.GameState;

/**
 * Android version of the GVG viewer class
 */
public class DebugView extends View  implements View.OnTouchListener {
    private Paint background;
    private Paint foreground;

    private DebugActivity activity;
    private int fontSize;

    private HotzoneControlScheme controls;

    public DebugView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.activity = (DebugActivity)context;
        init();
    }

    public DebugView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.activity = (DebugActivity)context;
        init();
    }

    public DebugView(Context context) {
        super(context);
        this.activity = (DebugActivity)context;
        init();
    }

    private void init() {
        background = new Paint();
        background.setColor(Color.BLACK);
        background.setStyle(Paint.Style.FILL);

        fontSize = getResources().getDimensionPixelSize(R.dimen.myFontSize);

        foreground = new Paint();
        foreground.setTextSize(fontSize);
        foreground.setAntiAlias(true);
        foreground.setColor(Color.WHITE);
        foreground.setStyle(Paint.Style.FILL);

        this.setOnTouchListener(this);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        //check the controls are initialised
        if (controls == null) {
            this.controls = new HotzoneControlScheme();
            controls.init(getWidth(), getHeight());
        }

        render(canvas);
    }

    public void render(Canvas c) {
        //render a static background
        c.drawRect(0,0,getWidth(), getHeight(), background);

        GameState state = activity.getState();
        if (state == null) {
            return;
        }

        //render the debug controls
        controls.render(c, true);

        //render some game stats
        String lastMove = activity.getString(R.string.debug_last_move);
        String totalMoves = activity.getString(R.string.debug_total_moves);
        String instructions = activity.getString(R.string.debug_instructions);

        c.drawText(String.format(lastMove, state.getLastAction()), 50, fontSize, foreground);
        c.drawText(String.format(totalMoves, state.getMoveCount()), 50, fontSize * 2, foreground);
        c.drawText(instructions, 50, fontSize * 3, foreground);
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        if (event.getAction() != MotionEvent.ACTION_DOWN) {
            return false;
        }

        Types.ACTIONS action = controls.getAction(event.getX(), event.getY());
        if (action != null) {
            GameState state = activity.getState();
            state.process(action);
            postInvalidate();
            return true;
        }

        return false;
    }
}
