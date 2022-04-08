package com.example.hujihackaton47;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;

import com.example.hujihackaton47.adapters.ResultItemAdapter;
import com.example.hujihackaton47.db.Database;
import com.example.hujihackaton47.models.Item;
import com.example.hujihackaton47.models.User;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.util.Log;
import android.view.View;

import androidx.cardview.widget.CardView;
import androidx.lifecycle.LiveData;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.hujihackaton47.databinding.ActivityMainBinding;

import android.view.Menu;
import android.view.MenuItem;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageButton;
import android.widget.SearchView;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    Database db;
    LiveData<Item> itemLiveData;

    private AppBarConfiguration appBarConfiguration;
    private ActivityMainBinding binding;
    private ResultItemAdapter adapter;
    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // our code
        setContentView(R.layout.activity_main);
        db = Database.getInstance();

        // create user
        user = (User) getIntent().getSerializableExtra("user");

        // set ui components
        FloatingActionButton addItemFab = findViewById(R.id.fab_add_item);
        FloatingActionButton orderItemFab = findViewById(R.id.fab_order_item);
        ImageButton profileIconImageButton = (ImageButton) findViewById(R.id.profile_icon);
        SearchView searchView = (SearchView) findViewById(R.id.simpleSearchView); // inititate a search view
        searchView.setIconified(false);
        searchView.setQueryHint("search product...");


        // recycler view
        RecyclerView recyclerView = findViewById(R.id.item_recycler_view);
        adapter = new ResultItemAdapter(item -> {
            Log.d("Adapter,", item.toString() + "\n\n\n" + user.toString());
            Intent intent = new Intent(MainActivity.this, OrderItemActivity.class);
            intent.setAction(Intent.ACTION_GET_CONTENT);
            intent.putExtra("user", user);
            intent.putExtra("item", item);
            startActivity(intent);
        });
        recyclerView.setAdapter( adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL, false));




        // add item click
        addItemFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Snackbar.make(view, "Clicked on Add Item Fab", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
                Intent intent = new Intent(MainActivity.this, AddItemActivity.class);
                intent.putExtra("user", user);
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
                intent.putExtra("user", user);
                startActivity(intent);
            }
        });

        // profile
        profileIconImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, ProfileActivity.class);
                intent.putExtra("user", user);
                startActivity(intent);
            }
        });


        // perform set on query text listener event
        searchView.setOnQueryTextListener(
                new SearchView.OnQueryTextListener() {
                    @Override
                    public boolean onQueryTextSubmit(String query) {
                        // do something on text submit
                        db.getLiveDataItemsByName("default");
                        Log.d("ActivityMain", "onQueryTextSubmit: " + query);
                        getAdapter(query);
                        recyclerView.setAdapter( adapter);
                        recyclerView.setLayoutManager(new LinearLayoutManager(MainActivity.this, RecyclerView.VERTICAL, false));

                        return false;
                    }

                    @Override
                    public boolean onQueryTextChange(String newText) {

                        Log.d("ActivityMain", "onQueryTextSubmit:" + newText);
                        return false;
                    }
                });

    }

    private RecyclerView.Adapter getAdapter(String name) {

        LiveData<List<Item>> itemsLiveData = db.getLiveDataItemsByName(name);

        itemsLiveData.observeForever(items -> {
        if (items == null){
            Log.e("Main", "vsfdvvdvsdsdsvsddssd");
        }
            adapter.setItems(items);
        });
        Log.d("ActivityMain", "adapter:" + adapter);
        return adapter;
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

    @RequiresApi(api = Build.VERSION_CODES.M)
    public void hideKeyboard(View view) {
        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }
}