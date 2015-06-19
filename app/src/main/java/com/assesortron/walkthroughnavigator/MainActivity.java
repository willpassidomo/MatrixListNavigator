package com.assesortron.walkthroughnavigator;

import android.app.FragmentTransaction;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.i("MainActivity", "startOnCreate");
        Navigator nav = new Navigator();

        FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.add(R.id.main_display, nav);
        ft.commit();

        List<Navigator.DisplayObject> displayObjects = new ArrayList<>();
        for (int i = 1; i <= 5; i++) {
            displayObjects.add(new WalkThrough(i+"", "Living Room",  "Electric"));
            displayObjects.add(new WalkThrough(i +"", "Living Room", "Framing"));
            displayObjects.add(new WalkThrough(i+"", "Living Room", "Plumbing"));
            displayObjects.add(new WalkThrough(i+"", "Bedroom",  "Electric"));
            displayObjects.add(new WalkThrough(i +"", "Bedroom", "Framing"));
            displayObjects.add(new WalkThrough(i+"", "Bedroom", "Plumbing"));
            displayObjects.add(new WalkThrough(i+"", "Dining Room",  "Electric"));
            displayObjects.add(new WalkThrough(i +"", "Dining Room", "Framing"));
            displayObjects.add(new WalkThrough(i+"", "Dining Room", "Plumbing"));

        }

        nav.setUp(displayObjects, new PreviewFragment(), new PreviewFragment());

        Log.i("MainActivity", "endOnCreate");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        Log.i("MainActivity", "OnCreateOptionsMenu");
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
