package com.example.hujihackaton47;

import android.app.Activity;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.MutableLiveData;

import com.example.hujihackaton47.db.Database;
import com.example.hujihackaton47.models.Item;
import com.example.hujihackaton47.models.User;

public class AddItemActivity extends AppCompatActivity {
    private Database db;
    private SharedPreferences sp;
    private MutableLiveData<Integer> paymentLiveData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_item);

        // set logic components
        db = Database.getInstance();
        sp = PreferenceManager.getDefaultSharedPreferences(this);
        paymentLiveData = new MutableLiveData<>(10);

        // Buttons etc.
        ActivityResultLauncher<String> stringActivityResultLauncher = registerForActivityResult(new ActivityResultContracts.GetContent(), new ActivityResultCallback<Uri>() {

            @Override
            public void onActivityResult(Uri result) {

            }
        });

        // set ui components
        EditText ItemName = findViewById(R.id.AddItemName);
        EditText ItemDescription = findViewById(R.id.AddItemDescription);
        EditText ItemPrice = findViewById(R.id.AddItemPrice);
        View ItemImage = findViewById(R.id.AddItemImageButton);
        Button addItemButton = findViewById(R.id.AddItemButton);
        TextView titleTextView = findViewById(R.id.PriceTitle);

        com.google.android.material.slider.Slider paymentSlider = (com.google.android.material.slider.Slider) findViewById(R.id.priceSlider);
        paymentSlider.setValue(10);
        paymentSlider.addOnChangeListener((slider, value, fromUser) -> {
            paymentLiveData.setValue((int) value);
            titleTextView.setText(R.string.price_title +" " + (int) value+"$");

        });


        ItemImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                stringActivityResultLauncher.launch("image/*");
            }
        });

        addItemButton.setOnClickListener(v -> {
            db.addItem(new Item(ItemName.getText().toString(), "ItemImage", 5, null, ItemDescription.getText().toString(), "5"));
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    public void hideKeyboard(View view) {
        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }


}


