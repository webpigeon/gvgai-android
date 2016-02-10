package uk.me.webpigeon.phd.gvgai.gvg;

import android.content.res.AssetManager;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import core.VGDLFactory;
import core.VGDLParser;
import core.VGDLRegistry;
import core.competition.CompetitionParameters;
import core.game.Game;
import core.player.AbstractPlayer;
import uk.me.webpigeon.phd.gvgai.R;

public class GameActivity extends AppCompatActivity {

    private Game game;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(new GVGView(this));

        if (savedInstanceState != null) {
            String gameFile = savedInstanceState.getString("gameFile", "aliens.txt");
            String levelFile = savedInstanceState.getString("levelFile", "aliens_level0.txt");
            game = buildGame(gameFile, levelFile);
        } else {
            game = buildGame("aliens.txt", "aliens_lvl0.txt");
        }
    }

    private Game buildGame(String gameFile, String levelFile){
        VGDLFactory.GetInstance().init(); //This always first thing to do.
        VGDLRegistry.GetInstance().init();

        // First, we create the game to be played..
        try {
            String[] gameDef = readVGDL("aliens.txt");
            String[] levelDef = readVGDL("aliens_lvl0.txt");

            Game game = new VGDLParser().parseGame(gameDef);
            game.buildStringLevel(levelDef);

            return game;
        } catch (IOException ex) {
            ex.printStackTrace();
        }

        return game;
    }

    private String[] readVGDL(String filename) throws IOException {
        AssetManager am = getAssets();
        Scanner s = new Scanner(am.open(filename));
        List<String> lines = new ArrayList<String>();
        while (s.hasNextLine()) {
            lines.add(s.nextLine());
        }

        return lines.toArray(new String[lines.size()]);
    }

}
