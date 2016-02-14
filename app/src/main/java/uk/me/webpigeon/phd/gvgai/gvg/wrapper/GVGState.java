package uk.me.webpigeon.phd.gvgai.gvg.wrapper;

import android.app.ProgressDialog;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import core.SpriteGroup;
import core.VGDLSprite;
import core.game.Game;
import ontology.Types;
import uk.me.webpigeon.phd.gvgai.gvg.GameState;

/**
 * Created by webpigeon on 14/02/16.
 */
public class GVGState implements GameState {
    private Game game;
    private AndroidPlayer player;

    private Types.ACTIONS lastAction;
    private int moveCount;

    public GVGState(Game game) {
        this.game = game;
        this.player = new AndroidPlayer();
        game.playGame(player, 0);
    }

    @Override
    public void reset() {
        game.reset();
        moveCount = 0;
    }

    @Override
    public void process(Types.ACTIONS action) {
        player.setNextAction(action);
    }

    @Override
    public Types.ACTIONS getLastAction() {
        return lastAction;
    }

    @Override
    public int getMoveCount() {
        return moveCount;
    }

    @Override
    public List<VGDLSprite> getSprites() {
        List<VGDLSprite> sprites = new ArrayList<>();

        int[] gameSpriteOrder = game.getSpriteOrder();
        for (Integer spriteTypeInt : gameSpriteOrder) {
            Iterator<VGDLSprite> spriteItr = game.getSpriteGroup(spriteTypeInt);

            if (spriteItr == null) {
                continue;
            }

            //Iterate though each sprite
            while (spriteItr.hasNext()) {
                VGDLSprite sprite = spriteItr.next();
                System.out.println("adding: "+sprite);

                if (sprite != null) {
                    sprites.add(sprite);
                }
            }
        }

        return sprites;
    }

    @Override
    public double getHeight() {
        return game.getHeight();
    }

    @Override
    public double getWidth() {
        return game.getWidth();
    }
}
