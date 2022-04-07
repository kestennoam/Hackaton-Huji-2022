package com.example.hujihackaton47.db;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.hujihackaton47.models.Item;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

public class Database implements IDataBase{
    private static Database instance;
    private Map<String, Item> items = new HashMap<>();
    private MutableLiveData<List<Item>> mutableLiveData = new MutableLiveData<>();
    private FirebaseFirestore firestore;


    // items in firestore: items/
    private Database() {
        mutableLiveData.setValue(new ArrayList<>());
        firestore = FirebaseFirestore.getInstance();
        Log.d(Database.class.toString(), "fire" + firestore);
        firestore.collection("items").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                if (error != null) {
                    // handle error
                } else if (value == null) {
                    // handle empty
                } else {
                    List<DocumentSnapshot> documents = value.getDocuments();
                    items.clear();
                    for (DocumentSnapshot document : documents) {
                        Item item = document.toObject(Item.class);
                        items.put(item.getId(), item);
                    }
                }
                mutableLiveData.setValue(new ArrayList<>(items.values()));
            }

        });
    }

    // instance
    public static Database getInstance() {
        if (instance == null) {
            instance = new Database();
        }
        return instance;
    }



    // sandwiches
    public Task<DocumentSnapshot> downloadItem(String itemId) {
        return firestore.collection("items").document(itemId).get();
    }

    public void deleteItem(Item item) {
        items.remove(item.getId());
        mutableLiveData.setValue(new ArrayList<>(items.values()));
        firestore.collection("items").document(item.getId()).delete();
    }

    public void addItem(Item item) {
        String newId = UUID.randomUUID().toString();
        items.put(newId, item);
        mutableLiveData.setValue(new ArrayList<>());
        firestore.collection("items").document(item.getId()).set(item);
    }

    public LiveData<List<Item>> getItemsLiveData() {
        return mutableLiveData;
    }

    public LiveData<List<Item>> getLiveDataItemsByName(String name) {

        Task<QuerySnapshot> querySnapshotTask = firestore.collection("items").whereEqualTo("name", name).get();
        querySnapshotTask.addOnSuccessListener(items -> {
            mutableLiveData.setValue(items.toObjects(Item.class));
        });
        querySnapshotTask.addOnFailureListener(null /* TODO complete[noamkesten]*/);

        return mutableLiveData;
    }
}
