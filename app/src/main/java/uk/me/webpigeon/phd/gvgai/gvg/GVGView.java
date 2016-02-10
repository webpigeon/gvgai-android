package uk.me.webpigeon.phd.gvgai.gvg;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import core.SpriteGroup;
import core.VGDLSprite;
import core.game.Game;

/**
 * Android version of the GVG viewer class
 */
public class GVGView extends View {
    private Paint background;

    private Game game;
    private SpriteGroup[] spriteGroups;

    public GVGView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public GVGView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public GVGView(Context context) {
        super(context);
        init();
    }

    private void init() {
        background = new Paint();
        background.setColor(Color.BLACK);
        background.setStyle(Paint.Style.FILL);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        render(canvas);
    }

    public void render(Canvas c) {
        //render a static background
        c.drawRect(0,0,getWidth(), getHeight(), background);

        //rendering time!
        if ( spriteGroups == null ) {
            return;
        }

        int[] gameSpriteOrder = game.getSpriteOrder();
        for (Integer spriteTypeInt : gameSpriteOrder) {
            ConcurrentHashMap<Integer, VGDLSprite> cMap = spriteGroups[spriteTypeInt].getSprites();

            //Iterate though each sprite
            Set<Map.Entry<Integer, VGDLSprite>> spriteEntrySet = cMap.entrySet();
            for (Map.Entry<Integer, VGDLSprite> spriteEntry : spriteEntrySet) {
                Integer key = spriteEntry.getKey();
                VGDLSprite sprite = spriteEntry.getValue();
                if (sprite != null) {
                    //sprite.draw(null, game);
                }
            }
        }

    }


}
