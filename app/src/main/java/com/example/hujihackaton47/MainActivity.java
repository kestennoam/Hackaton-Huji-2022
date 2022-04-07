package com.example.hujihackaton47;

import android.content.Intent;
import android.os.Bundle;

import com.example.hujihackaton47.db.Database;
import com.example.hujihackaton47.models.Item;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;

import android.util.Log;
import android.view.View;

import androidx.lifecycle.LiveData;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.hujihackaton47.databinding.ActivityMainBinding;

import android.view.Menu;
import android.view.MenuItem;
import android.widget.SearchView;

public class MainActivity extends AppCompatActivity {

    Database db;
    LiveData<Item> itemLiveData;

    private AppBarConfiguration appBarConfiguration;
    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // our code
        setContentView(R.layout.activity_main);
        db = Database.getInstance();

        // set ui components
        FloatingActionButton addItemFab = findViewById(R.id.fab_add_item);
        FloatingActionButton orderItemFab = findViewById(R.id.fab_order_item);
        SearchView simpleSearchView = (SearchView) findViewById(R.id.simpleSearchView); // inititate a search view


        // add item click
        addItemFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Clicked on Add Item Fab", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
                Intent intent = new Intent(MainActivity.this, AddItemActivity.class);
                startActivity(intent);
            }
        });

        // order item click
        orderItemFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Clicked on Order Item Fab", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
                Intent intent = new Intent(MainActivity.this, OrderItemActivity.class);
                startActivity(intent);
            }
        });


        // perform set on query text listener event
        simpleSearchView.setOnQueryTextListener(
                new SearchView.OnQueryTextListener() {
                    @Override
                    public boolean onQueryTextSubmit(String query) {
                        // do something on text submit
                        db.getLiveDataItemsByName("default");
                        Log.d("ActivityMain", "onQueryTextSubmit: " + query);

                        return false;
                    }

                    @Override
                    public boolean onQueryTextChange(String newText) {

                        Log.d("ActivityMain", "onQueryTextSubmit:" + newText);
                        return false;
                    }
                });

    }

    // write code here



    // not out code
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

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        return NavigationUI.navigateUp(navController, appBarConfiguration)
                || super.onSupportNavigateUp();
    }
}