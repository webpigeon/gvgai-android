package uk.me.webpigeon.phd.gvgai;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import uk.me.webpigeon.phd.gvgai.gvg.GameActivity;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {
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
        list.setOnItemClickListener(this);

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

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        String item = adapter.getItem(position);
        System.out.println(item);

        Intent gameIntent = new Intent(getBaseContext(), GameActivity.class);
        startActivity(gameIntent);
    }
}
