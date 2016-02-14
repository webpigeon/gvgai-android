package uk.me.webpigeon.phd.gvgai.gvg.mockups;

import android.content.Context;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.net.Uri;
import android.util.AttributeSet;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;


import ontology.Types;
import uk.me.webpigeon.phd.gvgai.R;
import uk.me.webpigeon.phd.gvgai.gvg.GameActivity;
import uk.me.webpigeon.phd.gvgai.gvg.GameState;

/**
 * Android version of the GVG viewer class
 */
public class DebugView extends View  implements View.OnTouchListener {
    private Paint background;
    private Paint foreground;

    private Paint touchZonesUD;
    private Paint touchZonesLR;

    private RectF topZone;
    private RectF botZone;
    private RectF leftZone;
    private RectF rightZone;

    private DebugActivity activity;
    private int fontSize;

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
        foreground.setColor(Color.BLUE);
        foreground.setStyle(Paint.Style.FILL);

        touchZonesUD = new Paint();
        touchZonesUD.setAlpha(200);
        touchZonesUD.setColor(Color.RED);
        touchZonesUD.setStyle(Paint.Style.FILL);

        touchZonesLR = new Paint();
        touchZonesLR.setAlpha(200);
        touchZonesLR.setColor(Color.CYAN);
        touchZonesLR.setStyle(Paint.Style.FILL);


        this.setOnTouchListener(this);
    }

    private void initZones() {
        //touch zones
        float zoneX = getWidth() * 0.3f;
        float zoneY = getHeight() * 0.3f;

        //render touch zones
        topZone = new RectF(0, 0, getWidth(), zoneY);
        botZone = new RectF(0, getHeight()-zoneY, getWidth(), getHeight());
        leftZone = new RectF(0, 0, zoneX, getHeight());
        rightZone = new RectF(getWidth()-zoneX, 0, getWidth(), getHeight());
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        render(canvas);
    }

    public void render(Canvas c) {
        //render a static background
        c.drawRect(0,0,getWidth(), getHeight(), background);

        GameState state = activity.getState();
        if (state == null) {
            return;
        }

        if (topZone == null) {
            initZones();
        }

        //render touch zones
        c.drawRect(leftZone, touchZonesLR);
        c.drawRect(rightZone, touchZonesLR);
        c.drawRect(topZone, touchZonesUD);
        c.drawRect(botZone, touchZonesUD);

        //render some game stats
        c.drawText("last: " + state.getLastAction(), 100, fontSize, foreground);
        c.drawText("# moves: " + state.getMoveCount(), 100, fontSize * 2, foreground);
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        if (event.getAction() != MotionEvent.ACTION_DOWN) {
            return false;
        }

        Types.ACTIONS action = null;
        float x = event.getX();
        float y = event.getY();

        if (topZone.contains(x,y)) {
            action = Types.ACTIONS.ACTION_UP;
        } else if (botZone.contains(x,y)) {
            action = Types.ACTIONS.ACTION_DOWN;
        } else if (leftZone.contains(x,y)) {
            action = Types.ACTIONS.ACTION_LEFT;
        } else if (rightZone.contains(x,y)) {
            action = Types.ACTIONS.ACTION_RIGHT;
        }

        if (action != null) {
            GameState state = activity.getState();
            state.process(action);
            postInvalidate();
            return true;
        }

        return false;
    }
}
