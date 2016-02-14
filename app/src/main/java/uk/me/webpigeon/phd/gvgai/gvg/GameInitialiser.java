package uk.me.webpigeon.phd.gvgai.gvg;

import android.content.res.AssetManager;
import android.os.AsyncTask;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import core.VGDLFactory;
import core.VGDLParser;
import core.VGDLRegistry;
import core.game.Game;

/**
 * Created by webpigeon on 14/02/16.
 */
public class GameInitialiser extends AsyncTask<GameConfig, Void, Game> {
    private AssetManager am;
    private GameActivity activity;

    public GameInitialiser(AssetManager am, GameActivity activity) {
        this.am = am;
        this.activity = activity;
    }

    @Override
    protected Game doInBackground(GameConfig... params) {
        GameConfig config = params[0];

        //init GVGAI
        VGDLFactory.GetInstance().init();
        VGDLRegistry.GetInstance().init();

        // First, we create the game to be played..
        try {
            String[] gameDef = readVGDL(config.gameDef);
            String[] levelDef = readVGDL(config.levelDef[0]);

            Game game = new VGDLParser().parseGame(gameDef);
            game.buildStringLevel(levelDef);

            return game;
        } catch (IOException ex) {
            ex.printStackTrace();
        }

        return null;
    }

    protected void onPostExecute(Game result) {
        activity.setGame(result);
    }


    private String[] readVGDL(String filename) throws IOException {
        Scanner s = new Scanner(am.open(filename));
        List<String> lines = new ArrayList<String>();
        while (s.hasNextLine()) {
            lines.add(s.nextLine());
        }

        return lines.toArray(new String[lines.size()]);
    }

}
