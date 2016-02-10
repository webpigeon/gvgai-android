package uk.me.webpigeon.phd.gvgai;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private ArrayList<String> games;
    private ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ListView list = (ListView)findViewById(R.id.GameList);
        games = new ArrayList<String>();

        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, games);
        list.setAdapter(adapter);

        populateGameList();
    }

    protected void populateGameList() {
        adapter.add("Test game");
        adapter.add("Test game");
        adapter.add("Test game");
        adapter.add("Test game");
        adapter.add("Test game");
        adapter.add("Test game");
        adapter.add("Test game");
        adapter.notifyDataSetChanged();
    }
}
