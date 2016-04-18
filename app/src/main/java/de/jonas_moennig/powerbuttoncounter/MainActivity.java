package de.jonas_moennig.powerbuttoncounter;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        startService(new Intent(this, UpdateService.class));
        update();

    }

    public void update(){
        ((TextView) findViewById(R.id.count)).setText(Integer.toString(Storage.getInstance(this).getCount()));
    }

    public void reset(View view){
        Storage.getInstance(this).reset();
        update();
    }

    public void increment(View view){
        Storage.getInstance(this).increment();
        update();
    }

    public void stop(View view){
        stopService(new Intent(this, UpdateService.class));
        finish();
    }

    @Override
    public void onResume(){
        super.onResume();
        update();
    }

    @Override
    public void onRestart(){
        super.onRestart();
        update();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
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
