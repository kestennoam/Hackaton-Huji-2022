package com.example.hujihackaton47;


import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageButton;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Transformations;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.hujihackaton47.adapters.MyItemAdapter;
import com.example.hujihackaton47.adapters.ResultItemAdapter;
import com.example.hujihackaton47.db.Database;
import com.example.hujihackaton47.models.Item;
import com.example.hujihackaton47.models.User;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ProfileActivity extends AppCompatActivity {
    private Database db;
    private SharedPreferences sp;
    private MyItemAdapter adapter;
    private User user;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        // get user
        user = (User) getIntent().getSerializableExtra("user");


        // set logic components
        db = Database.getInstance();
        sp = PreferenceManager.getDefaultSharedPreferences(this);

        // set ui components
        FloatingActionButton addItemFab = findViewById(R.id.fab_add_item);


        // recycler view
        RecyclerView recyclerView = findViewById(R.id.item_recycler_view);
        adapter = new MyItemAdapter(null);
        getAdapter("7" /* todo [noamkesten] change hardcoded*/);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false));

        // order item click
        addItemFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Snackbar.make(view, "Clicked on Order Item Fab", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
                Intent intent = new Intent(ProfileActivity.this, AddItemActivity.class);
                intent.putExtra("user", user);
                startActivity(intent);
            }
        });

    }

    private RecyclerView.Adapter getAdapter(String ownerId) {

        LiveData<List<Item>> itemsLiveData = db.getLiveDataItemsByUserID(ownerId);

        itemsLiveData.observeForever(items -> {
            if (items == null){
                Log.e("ProfileActivity", "items are null");
            }
            Log.d("ProfileActivity", "tems:" + items);
            adapter.setItems(items);
        });


        return adapter;
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    public void hideKeyboard(View view) {
        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }



}



