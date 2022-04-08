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

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ProfileActivity extends AppCompatActivity {
    private Database db;
    private SharedPreferences sp;
    private MyItemAdapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        // set logic components
        db = Database.getInstance();
        sp = PreferenceManager.getDefaultSharedPreferences(this);

        // set ui components

        // recycler view
        RecyclerView recyclerView = findViewById(R.id.item_recycler_view);
        adapter = new MyItemAdapter(null);
        getAdapter("7" /* todo [noamkesten] change hardcoded*/);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false));


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



