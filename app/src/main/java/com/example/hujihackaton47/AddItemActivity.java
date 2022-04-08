package com.example.hujihackaton47;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
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
import com.squareup.picasso.Picasso;

public class AddItemActivity extends AppCompatActivity {
    private Database db;
    private SharedPreferences sp;
    private MutableLiveData<Integer> paymentLiveData;
    Uri itemPictureUri;
    ImageButton itemImageButton;
    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_item);

        // create user
        user = (User) getIntent().getSerializableExtra("user");


        // set logic components
        db = Database.getInstance();
        sp = PreferenceManager.getDefaultSharedPreferences(this);


        // set ui components
        paymentLiveData = new MutableLiveData<>(10);
        EditText ItemName = findViewById(R.id.AddItemName);
        EditText ItemDescription = findViewById(R.id.AddItemDescription);
//        EditText ItemPrice = findViewById(R.id.AddItemPrice);
//        View ItemImage = findViewById(R.id.AddItemImageButton);
        Button addItemButton = findViewById(R.id.AddItemButton);
        TextView titleTextView = findViewById(R.id.PriceTitle);
        itemImageButton = findViewById(R.id.AddItemImageButton);

        com.google.android.material.slider.Slider paymentSlider = (com.google.android.material.slider.Slider) findViewById(R.id.priceSlider);
        paymentSlider.setValue(10);
        paymentSlider.addOnChangeListener((slider, value, fromUser) -> {
            paymentLiveData.setValue((int) value);
            titleTextView.setText(getString(R.string.price_title) + " - " + (int) value + "$");
        });

        // Buttons etc.
//        ActivityResultLauncher<String> stringActivityResultLauncher = registerForActivityResult(new ActivityResultContracts.GetContent(), new ActivityResultCallback<Uri>() {
//
//            @Override
//            public void onActivityResult(Uri result) {
//                itemPictureUri = Uri.parse(result.toString());
//                Picasso.get().load(itemPictureUri).into(itemImageButton);
//            }
//        });


//        ItemImage.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                stringActivityResultLauncher.launch("image/*");
//            }
//        });

        // profile picture button listener
        itemImageButton.setOnClickListener(v -> {
            Intent intent = new Intent();
            intent.setType("image/*");
            intent.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(Intent.createChooser(intent, "Select Picture"), 100); //activity result method call
        });

        addItemButton.setOnClickListener(v -> {
            db.addItem(new Item(ItemName.getText().toString(), "ItemImage", paymentLiveData.getValue(), null, ItemDescription.getText().toString(), "5"));
            finish();

        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == -1) {
            if (requestCode == 100) {
                itemPictureUri = Uri.parse(data.getData().toString());
                Picasso.get().load(itemPictureUri).into(itemImageButton);
            }
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    public void hideKeyboard(View view) {
        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }


}


