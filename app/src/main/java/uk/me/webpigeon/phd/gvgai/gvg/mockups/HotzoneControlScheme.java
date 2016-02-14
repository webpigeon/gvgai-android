package uk.me.webpigeon.phd.gvgai.gvg.mockups;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;

import ontology.Types;
import uk.me.webpigeon.phd.gvgai.gvg.ControlScheme;

/**
 * Large control scheme.
 *
 * This control scheme allows the user to touch the edges of the screen for movement and the center
 * for firing. I was planning to use on screen controls originally but they will cover too much
 * of the screen. This control scheme does not require any screen real estate and should be
 * relatively easy to use.
 *
 * Created by webpigeon on 14/02/16.
 */
public class HotzoneControlScheme implements ControlScheme {

    private RectF topZone;
    private RectF botZone;
    private RectF leftZone;
    private RectF rightZone;
    private RectF useZone;

    private Paint touchZonesUD;
    private Paint touchZonesLR;
    private Paint touchZoneUse;

    public void init(float width, float height) {
        //touch zones
        float zoneX = width * 0.3f;
        float zoneY = height * 0.3f;

        //render touch zones
        topZone = new RectF(zoneX, 0, width-zoneX, zoneY);
        botZone = new RectF(zoneX, height-zoneY, width-zoneX, height);
        leftZone = new RectF(0, zoneY, zoneX, height-zoneY);
        rightZone = new RectF(width - zoneX, zoneY, width, height-zoneY);
        useZone = new RectF(zoneX, zoneY, width-zoneX, height-zoneY);
    }

    private void setupPaint() {
        touchZonesUD = new Paint();
        touchZonesUD.setAlpha(200);
        touchZonesUD.setColor(Color.RED);
        touchZonesUD.setStyle(Paint.Style.FILL);

        touchZonesLR = new Paint();
        touchZonesLR.setAlpha(200);
        touchZonesLR.setColor(Color.CYAN);
        touchZonesLR.setStyle(Paint.Style.FILL);

        touchZoneUse = new Paint();
        touchZoneUse.setAlpha(200);
        touchZoneUse.setColor(Color.YELLOW);
        touchZoneUse.setStyle(Paint.Style.FILL);
    }
    
    public void render(Canvas canvas, boolean debug) {
        if (topZone == null) {
            init(canvas.getWidth(), canvas.getHeight());
            return;
        }

        if (debug) {
            if (touchZonesLR == null) {
                setupPaint();
            }

            //render touch zones
            canvas.drawRect(leftZone, touchZonesLR);
            canvas.drawRect(rightZone, touchZonesLR);
            canvas.drawRect(topZone, touchZonesUD);
            canvas.drawRect(botZone, touchZonesUD);
            canvas.drawRect(useZone, touchZoneUse);
        }
    }
    
    
    public Types.ACTIONS getAction(float x, float y) {
        if (topZone == null) {
            return null;
        }

        Types.ACTIONS action = null;

        if (topZone.contains(x,y)) {
            action = Types.ACTIONS.ACTION_UP;
        } else if (botZone.contains(x,y)) {
            action = Types.ACTIONS.ACTION_DOWN;
        } else if (leftZone.contains(x,y)) {
            action = Types.ACTIONS.ACTION_LEFT;
        } else if (rightZone.contains(x,y)) {
            action = Types.ACTIONS.ACTION_RIGHT;
        } else if (useZone.contains(x,y)) {
            action = Types.ACTIONS.ACTION_USE;
        }

        return action;
    }
}
