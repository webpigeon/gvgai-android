package uk.me.webpigeon.phd.gvgai.gvg;

import java.io.Serializable;
import java.util.List;

import core.VGDLSprite;
import ontology.Types;

/**
 * Created by webpigeon on 14/02/16.
 */
public interface GameState extends Serializable {

    public void reset();
    public void process(Types.ACTIONS action);

    Types.ACTIONS getLastAction();

    int getMoveCount();

    public List<VGDLSprite> getSprites();

    double getHeight();

    double getWidth();

    void tick();

    boolean isGameOver();

    void init();
}
