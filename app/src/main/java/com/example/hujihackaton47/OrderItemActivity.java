package com.example.hujihackaton47;


import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;

import com.example.hujihackaton47.db.Database;
import com.example.hujihackaton47.models.Item;
import com.example.hujihackaton47.models.User;

import java.util.ArrayList;
import java.util.Arrays;

public class OrderItemActivity extends AppCompatActivity {
    private Database db;
    private SharedPreferences sp;
    private Item item;
    private User user;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_item);

        user = new User("Noam", "Kesten", "123", "email@gmail.com",
                "050", "loc", "image");
        TextView userName = findViewById(R.id.user_name);
        String userFullName = user.getFirstName()+ " " +user.getLastName();
        userName.setText(userFullName);

        ArrayList<String> tempTags = new ArrayList<String>();
        tempTags.add("Drill");
        tempTags.add("Camera");
        tempTags.add("Vaccume");
        item = new Item("Makita DHP484Z Drill",
                "@drawable/ic_launcher_background", 10, tempTags, "Somethin Great!", "123");
        item.setTags(tempTags);
        TextView name = findViewById(R.id.name);
        ImageView image = findViewById(R.id.image);
        TextView price = findViewById(R.id.price);
        TextView description = findViewById(R.id.description);
//        image.setImageURI(Uri.parse(item.getImage()));
        name.setText(item.getName());
        description.setText(item.getDescription());
        String dollarPrice = "$" + String.valueOf(item.getPrice());
        price.setText(dollarPrice);


        // set logic components
        db = Database.getInstance();
        sp = PreferenceManager.getDefaultSharedPreferences(this);



    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    public void hideKeyboard(View view) {
        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }


}



