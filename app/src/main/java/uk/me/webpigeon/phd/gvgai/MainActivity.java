package uk.me.webpigeon.phd.gvgai;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.gson.Gson;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;

import uk.me.webpigeon.phd.gvgai.gvg.GameConfig;
import uk.me.webpigeon.phd.gvgai.gvg.GameActivity;
import uk.me.webpigeon.phd.gvgai.gvg.mockups.DebugActivity;
import uk.me.webpigeon.phd.gvgai.gvg.mockups.DummyGameState;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {
    private ArrayList<GameConfig> games;
    private ArrayAdapter<GameConfig> adapter;
    private static final String GVGAI_WEBSITE = "http://gvgai.net";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ListView list = (ListView)findViewById(R.id.GameList);
        games = new ArrayList<GameConfig>();

        adapter = new ArrayAdapter<GameConfig>(this, android.R.layout.simple_list_item_1, games);
        list.setAdapter(adapter);
        list.setOnItemClickListener(this);

        if (savedInstanceState == null) {
            populateGameList();
        } else {
            games = (ArrayList<GameConfig>)savedInstanceState.getSerializable("TEST");
            adapter.addAll(games);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.homepage_menu, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.visit_gvgai) {
            Intent i = new Intent(Intent.ACTION_VIEW);
            i.setData(Uri.parse(GVGAI_WEBSITE));
            startActivity(i);
            return true;
        }

        if (id == R.id.debug_launch) {
            Intent gameIntent = new Intent(getBaseContext(), DebugActivity.class);
            gameIntent.putExtra(GameActivity.GAME_STATE, new DummyGameState());
            startActivity(gameIntent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    protected void populateGameList() {
        try {
            Gson gson = new Gson();
            Reader reader = new InputStreamReader(getAssets().open("GameManifest.json"), "UTF-8");
            GameConfig[] gameConfigs = gson.fromJson(reader, GameConfig[].class);

            for (GameConfig config : gameConfigs) {
                adapter.add(config);
            }

            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        adapter.notifyDataSetChanged();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putSerializable("TEST", games);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        GameConfig item = adapter.getItem(position);
        System.out.println(item);

        Intent gameIntent = new Intent(getBaseContext(), GameActivity.class);
        gameIntent.putExtra(GameActivity.GAME_CONFIG, item);
        startActivity(gameIntent);
    }
}
