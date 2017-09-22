package com.example.nkorolkov.andmanyaiswelldone;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.Toast;

public class ActivityWithMenu extends AppCompatActivity {
    @Override
    public boolean onCreateOptionsMenu(android.view.Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.sign_in:
                // Red item was selected
                Toast.makeText(this, "Google Sign in.", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(this, AuthorizationActivity.class));
                return true;
            case R.id.help:
                Toast.makeText(this, "help", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(this, HelpActivity.class));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
