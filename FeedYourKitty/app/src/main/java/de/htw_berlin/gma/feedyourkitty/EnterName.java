package de.htw_berlin.gma.feedyourkitty;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

public class EnterName extends Activity
{
    private Intent                  intentScores;
    private int                     points;
    private EditText                editNameText;
    private DemoSQLiteOpenHelper    demoSqlHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enter_name);

        //get points from game_activity
        Bundle bundle = getIntent().getExtras();
        points = bundle.getInt("points", 0);

        //display congratulatory text
        TextView endTextDisplay = (TextView) findViewById(R.id.end_text);
        endTextDisplay.setText("You Rock!\nYou got " + points + " Points!");

        editNameText = (EditText) findViewById(R.id.edit_name_text);

        intentScores = new Intent(this, Scores.class);

        demoSqlHelper = new DemoSQLiteOpenHelper(this);
    }

    public void LoadScores(View view)
    {
        String playerName = editNameText.getText().toString();
        if(playerName == "" || playerName == null)
            playerName = "Penny";

        demoSqlHelper.insert(playerName, points);

        startActivity(intentScores);
    }
}