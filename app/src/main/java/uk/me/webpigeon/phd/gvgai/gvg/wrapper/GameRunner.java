package uk.me.webpigeon.phd.gvgai.gvg.wrapper;

import android.view.View;

import core.game.Game;
import core.player.AbstractPlayer;
import uk.me.webpigeon.phd.gvgai.gvg.GameState;

/**
 * Created by webpigeon on 14/02/16.
 */
public class GameRunner implements Runnable {
    private GameState state;
    private View view;

    public GameRunner(GameState state, GVGView view) {
        this.state = state;
        this.view = view;
    }

    @Override
    public void run() {
        try {
            state.init();
            while(!state.isGameOver()){
                System.out.println("tick");
                state.tick();
                view.postInvalidate();
                Thread.sleep(1000);
            }
        } catch (Exception ex) {
            System.err.println("GVGAI Crashed again...");
            ex.printStackTrace();
        }
    }
}
