package uk.me.webpigeon.phd.gvgai.gvg.mockups;

import java.util.List;

import core.VGDLSprite;
import ontology.Types;
import uk.me.webpigeon.phd.gvgai.gvg.GameState;

/**
 * A Mock GVGAI state.
 *
 * Track the number of clicks the user has made on the screen.
 *
 * Ideally, this would be the GVGAI game state but:
 * 1) GVGAI is heavily dependent on AWT (unpicking in progress)
 * 2) GVGAI games are not serializable (TODO)
 * 3) The use of refection/magic makes it difficult to swap in android versions of the sprites
 */
public class DummyGameState implements GameState {
    public int leftClicks;
    public int rightClicks;
    public int upClicks;
    public int downClicks;
    public int useClicks;

    public int moveCount;
    public Types.ACTIONS lastAction;

    public DummyGameState() {
       reset();
    }

    @Override
    public void reset() {
        this.leftClicks = 0;
        this.rightClicks = 0;
        this.upClicks = 0;
        this.downClicks = 0;
        this.useClicks = 0;

        this.moveCount = 0;
        this.lastAction = Types.ACTIONS.ACTION_NIL;
    }

    @Override
    public void process(Types.ACTIONS action){

        switch(action) {
            case ACTION_DOWN: {
                downClicks++;
                break;
            }
            case ACTION_LEFT: {
                leftClicks++;
                break;
            }
            case ACTION_RIGHT: {
                rightClicks++;
                break;
            }
            case ACTION_UP: {
                upClicks++;
                break;
            }
            case ACTION_USE: {
                useClicks++;
                break;
            }
        }

        moveCount++;
        lastAction = action;
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
        return null;
    }

    @Override
    public double getHeight() {
        return 0;
    }

    @Override
    public double getWidth() {
        return 0;
    }

}
