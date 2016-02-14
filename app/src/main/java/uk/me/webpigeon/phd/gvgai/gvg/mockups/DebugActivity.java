package uk.me.webpigeon.phd.gvgai.gvg.mockups;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;

import uk.me.webpigeon.phd.gvgai.R;
import uk.me.webpigeon.phd.gvgai.gvg.GameState;

public class DebugActivity extends AppCompatActivity {
    public static final String GAME_STATE = "gameState";

    private GameState state;
    private DebugView view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        view = new DebugView(this);
        setContentView(view);

        if (savedInstanceState != null) {
            state = (GameState)savedInstanceState.getSerializable(GAME_STATE);
            view.postInvalidate();
        } else {
            Intent intent = getIntent();
            state = (GameState)intent.getSerializableExtra(GAME_STATE);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        menu.add("reset");

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        String name = (String)item.getTitle();

        if ("reset".equals(name)) {
            askForReset();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putSerializable(GAME_STATE, state);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        state = (GameState)savedInstanceState.getSerializable(GAME_STATE);
    }

    public GameState getState() {
        return state;
    }

    protected void askForReset() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(R.string.confirm_reset).setTitle(R.string.confirm_reset_title);

        builder.setPositiveButton("sure", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                state.reset();
                view.postInvalidate();
            }
        });

        builder.setNegativeButton("nope", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }
}
