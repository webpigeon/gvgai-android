package uk.me.webpigeon.phd.gvgai.gvg;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import core.game.Game;
import uk.me.webpigeon.phd.gvgai.gvg.wrapper.GVGState;
import uk.me.webpigeon.phd.gvgai.gvg.wrapper.GVGView;
import uk.me.webpigeon.phd.gvgai.gvg.wrapper.GameRunner;

public class GameActivity extends AppCompatActivity {
    public static final String GAME_CONFIG = "gameFile";
    public static final String GAME_STATE = "gameState";
    public static final Boolean MOCK_FOR_ASSIGNMENT = true;

    private GameState state;
    private GVGView view;
    private ProgressDialog dialog;
    private GameConfig config;
    private Thread gameThread;


    protected void startGame() {
        if (gameThread != null) {
            gameThread.interrupt();
        }

        GameRunner runner = new GameRunner(state, view);
        gameThread = new Thread(runner);
        gameThread.start();
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        view = new GVGView(this);
        setContentView(view);

        dialog = new ProgressDialog(this);
        dialog.setIndeterminate(true);


        if (savedInstanceState != null) {
            config = (GameConfig)savedInstanceState.getSerializable(GAME_CONFIG);
            state = (GameState)savedInstanceState.getSerializable(GAME_STATE);
            startGame();
            view.postInvalidate();
        } else {
            Intent intent = getIntent();
            config = (GameConfig)intent.getSerializableExtra(GAME_CONFIG);
            buildGame(config);
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putSerializable(GAME_CONFIG, config);
        outState.putSerializable(GAME_STATE, state);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        config = (GameConfig)savedInstanceState.getSerializable(GAME_CONFIG);
        state = (GameState)savedInstanceState.getSerializable(GAME_STATE);
    }

    private void buildGame(GameConfig config){
        dialog.show();

        GameInitialiser init = new GameInitialiser(getAssets(), this);
        init.execute(config);
    }

    public void setGame(Game game) {
        dialog.dismiss();

        state = new GVGState(game);
        startGame();
        view.postInvalidate();
    }

    public GameState getState() {
        return state;
    }
}
