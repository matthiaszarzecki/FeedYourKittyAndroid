package de.htw_berlin.gma.feedyourkitty;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class Scores extends Activity
{
    private Intent intentMenu;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scores);

        DemoSQLiteOpenHelper demoSqlHelper = new DemoSQLiteOpenHelper(this);

        ArrayAdapter adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, demoSqlHelper.queryList());

        ListView listView = (ListView) findViewById(R.id.score_view);
        listView.setAdapter(adapter);

        intentMenu = new Intent(this, MainMenu.class);
    }

    public void LoadMenu(View view)
    {
        startActivity(intentMenu);
    }
}