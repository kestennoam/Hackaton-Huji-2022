package com.example.hujihackaton47.db;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.hujihackaton47.models.Item;
import com.example.hujihackaton47.models.User;
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

public class Database implements IDataBase{
    private static Database instance;
    private Map<String, Item> items = new HashMap<>();
    private Map<String, User> users = new HashMap<>();
    private MutableLiveData<List<Item>> itemsMutableLiveData = new MutableLiveData<>();
    private MutableLiveData<List<Item>> myItemsMutableLiveData = new MutableLiveData<>();
    private FirebaseFirestore firestore;


    // items in firestore: items/
    private Database() {
        itemsMutableLiveData.setValue(new ArrayList<>());
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
                itemsMutableLiveData.setValue(new ArrayList<>(items.values()));
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
        itemsMutableLiveData.setValue(new ArrayList<>(items.values()));
        firestore.collection("items").document(item.getId()).delete();
    }

    public void addItem(Item item) {
        String newId = UUID.randomUUID().toString();
        items.put(newId, item);
        itemsMutableLiveData.setValue(new ArrayList<>());
        firestore.collection("items").document(item.getId()).set(item);
    }

    public LiveData<List<Item>> getItemsLiveData() {
        return itemsMutableLiveData;
    }

    public LiveData<List<Item>> getLiveDataItemsByName(String name) {

        Task<QuerySnapshot> querySnapshotTask = firestore.collection("items").whereEqualTo("name", name).get();
        querySnapshotTask.addOnSuccessListener(items -> {
            itemsMutableLiveData.setValue(items.toObjects(Item.class));
        });
        querySnapshotTask.addOnFailureListener(null /* TODO complete[noamkesten]*/);

        return itemsMutableLiveData;
    }

    public LiveData<List<Item>> getLiveDataItemsByUserID(String ownerId) {

        Task<QuerySnapshot> querySnapshotTask = firestore.collection("items").whereEqualTo("ownerId", ownerId).get();
        querySnapshotTask.addOnSuccessListener(items -> {
            myItemsMutableLiveData.setValue(items.toObjects(Item.class));
        });
        querySnapshotTask.addOnFailureListener(null /* TODO complete[noamkesten]*/);

        return myItemsMutableLiveData;
    }
    
    // Users
    public void addUser(User user) {
        String newId = UUID.randomUUID().toString();
        users.put(newId, user);
        itemsMutableLiveData.setValue(new ArrayList<>());
        firestore.collection("users").document(user.getId()).set(user);
    }
}

