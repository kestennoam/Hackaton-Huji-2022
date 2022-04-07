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

import java.util.ArrayList;
import java.util.Arrays;

public class OrderItemActivity extends AppCompatActivity {
    private Database db;
    private SharedPreferences sp;
    private Item item;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_item);

        ArrayList<String> tempTags = new ArrayList<String>();
        tempTags.add("Drill");
        tempTags.add("Camera");
        tempTags.add("Vaccume");
        item = new Item("Product", "@drawable/ic_launcher_background", 10, tempTags, "Somethin Great!");
        item.setTags(tempTags);
        TextView name = findViewById(R.id.name);
        TextView tags = findViewById(R.id.tags);
        ImageView image = findViewById(R.id.image);
        TextView price = findViewById(R.id.price);
        TextView description = findViewById(R.id.description);
        String tagsString = "";
        if (item.getTags() != null)
        {
            for (String tag : item.getTags()) {
                tagsString += "#" + tag+" ";
            }
        }
//        image.setImageURI(Uri.parse(item.getImage()));
        name.setText(item.getName());
        tags.setText(tagsString);
        description.setText(item.getDescription());
        price.setText(String.valueOf(item.getPrice()));


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



