package uk.me.webpigeon.phd.gvgai.gvg;

import android.graphics.Canvas;

import ontology.Types;

/**
 * Created by webpigeon on 14/02/16.
 */
public interface ControlScheme {

    public void init(float width, float height);
    public void render(Canvas canvas, boolean debug);
    public Types.ACTIONS getAction(float x, float y);

}
