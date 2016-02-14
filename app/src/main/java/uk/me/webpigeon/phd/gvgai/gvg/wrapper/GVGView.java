package uk.me.webpigeon.phd.gvgai.gvg.wrapper;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridLayout;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import core.SpriteGroup;
import core.VGDLSprite;
import core.game.Game;
import ontology.Types;
import tools.Vector2d;
import uk.me.webpigeon.phd.gvgai.R;
import uk.me.webpigeon.phd.gvgai.gvg.GameActivity;
import uk.me.webpigeon.phd.gvgai.gvg.GameState;
import uk.me.webpigeon.phd.gvgai.gvg.mockups.DummyGameState;

/**
 * Android version of the GVG viewer class
 */
public class GVGView extends View  implements View.OnClickListener, View.OnTouchListener {
    private Paint background;
    private Paint test;

    private SpriteGroup[] spriteGroups;
    private GameActivity activity;

    public GVGView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.activity = (GameActivity)context;
        init();
    }

    public GVGView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.activity = (GameActivity)context;
        init();
    }

    public GVGView(Context context) {
        super(context);
        this.activity = (GameActivity)context;
        init();
    }

    private void init() {
        background = new Paint();
        background.setColor(Color.BLACK);
        background.setStyle(Paint.Style.FILL);

        test = new Paint();
        test.setColor(Color.WHITE);
        test.setStyle(Paint.Style.FILL);

        this.setOnClickListener(this);
        this.setOnTouchListener(this);
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
            System.out.println("state skip!");
            return;
        }

        float gridSize = getResources().getDimensionPixelSize(R.dimen.gridSize);
        System.out.println(gridSize);

        double width = state.getWidth();
        double height = state.getHeight();

        double scaleX = getWidth() / width;
        double scaleY = getHeight() / height;

        for (VGDLSprite sprite : state.getSprites()) {
            Vector2d pos = sprite.pos;
            Vector2d size = sprite.size;

            float x = (float)(pos.x * scaleX);
            float y = (float)(pos.y * scaleY);
            float bx = (float)(x + (size.x * scaleX));
            float by = (float)(y + (size.y * scaleY));

            System.out.println("rendering "+sprite+" at "+x+","+y);

            c.drawRect(x, y, bx, by, test);
        }
    }


    @Override
    public void onClick(View v) {
        GameState state = activity.getState();
        if (state != null) {
            state.process(Types.ACTIONS.ACTION_DOWN);
            postInvalidate();
        }
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            int pointerCount = event.getPointerCount();
            System.out.println("fingers: " + pointerCount + " " + event.getX() + " " + event.getY());

            return true;
        }
        return false;
    }
}
