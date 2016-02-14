package uk.me.webpigeon.phd.gvgai.gvg.wrapper;

import core.game.StateObservation;
import core.player.AbstractPlayer;
import ontology.Types;
import tools.ElapsedCpuTimer;

/**
 * Created by webpigeon on 14/02/16.
 */
public class AndroidPlayer extends AbstractPlayer {
    private Types.ACTIONS nextAction;

    public AndroidPlayer() {
        nextAction = Types.ACTIONS.ACTION_NIL;
    }

    public void setNextAction(Types.ACTIONS nextAction) {
        this.nextAction = nextAction;
    }

    @Override
    public Types.ACTIONS act(StateObservation stateObs, ElapsedCpuTimer elapsedTimer) {
        Types.ACTIONS action = nextAction;
        nextAction = Types.ACTIONS.ACTION_NIL;
        return action;
    }
}
