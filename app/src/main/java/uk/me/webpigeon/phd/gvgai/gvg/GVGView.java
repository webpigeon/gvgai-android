package uk.me.webpigeon.phd.gvgai.gvg;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import java.sql.SQLOutput;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import core.SpriteGroup;
import core.VGDLSprite;
import core.game.Game;
import tools.Vector2d;

/**
 * Android version of the GVG viewer class
 */
public class GVGView extends View {
    private Paint background;
    private Paint test;

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

        test = new Paint();
        test.setColor(Color.BLUE);
        test.setStyle(Paint.Style.FILL);
    }

    public void setGame(Game game) {
        this.game = game;
        this.postInvalidate();
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
            System.out.println(cMap);

            //Iterate though each sprite
            Set<Map.Entry<Integer, VGDLSprite>> spriteEntrySet = cMap.entrySet();
            for (Map.Entry<Integer, VGDLSprite> spriteEntry : spriteEntrySet) {
                Integer key = spriteEntry.getKey();
                VGDLSprite sprite = spriteEntry.getValue();
                System.out.println("rendering: "+sprite);

                if (sprite != null) {
                    Vector2d pos = sprite.pos;
                    Vector2d size = sprite.size;

                    System.out.println("rendering: "+sprite+", pos: "+pos+", "+size);
                    c.drawRect((int)pos.x, (int)pos.y, (int)size.x, (int)size.y, test);

                    //sprite.draw(null, game);
                }
            }
        }

    }


}
