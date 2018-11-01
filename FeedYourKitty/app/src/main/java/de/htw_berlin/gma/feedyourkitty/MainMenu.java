package de.htw_berlin.gma.feedyourkitty;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class MainMenu extends Activity
{
    private Intent intentGame;
    private Intent intentScores;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);

        intentGame = new Intent(this, Game.class);
        intentScores = new Intent(this, Scores.class);

        DemoSQLiteOpenHelper demoSqlHelper = new DemoSQLiteOpenHelper(this);

        //SQL-debug-commands
        //demoSqlHelper.insert("Penny", 27);
        //demoSqlHelper.insert("Carl", 13);
        //demoSqlHelper.clearTable();
    }

    public void LoadGame(View view)
    {
        startActivity(intentGame);
    }

    public void LoadScores(View view)
    {
        startActivity(intentScores);
    }
}