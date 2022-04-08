package com.example.hujihackaton47.db;
import android.net.Uri;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.hujihackaton47.interfaces.IOnSuccessLoadingImage;
import com.example.hujihackaton47.models.Item;
import com.example.hujihackaton47.models.User;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

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

    // images
    public static void uploadImage(Uri filePath, String imageID, IOnSuccessLoadingImage listener) {
        if (filePath != null) {
            //Log.d(TAG, String.format("uploading filepath: <%s> and imageID: <%s>", filePath, imageID));
            // Defining the child of storageReference
            FirebaseStorage storage = FirebaseStorage.getInstance();
            StorageReference storageReference = storage.getReference();
            StorageReference ref = storageReference.child("images/" + imageID);

            // adding listeners on upload or failure of image
            ref.putFile(filePath)
                    .addOnSuccessListener(taskSnapshot -> {
//                        loadImage(imageID, listener);
//                        if (listener != null) {
//                            listener.onSuccess(imageID);
//                        }
                        //Log.d(TAG, String.format("Child image was added successfully: <%s>", imageID));


                    })
                    .addOnFailureListener(e -> Log.e("DB", e.getMessage()))
                    .addOnProgressListener(
                            taskSnapshot -> {
                                double progress
                                        = (100.0
                                        * taskSnapshot.getBytesTransferred()
                                        / taskSnapshot.getTotalByteCount());
                                //Log.d(TAG, "Uploaded " + (int) progress + "%");
                            });
        }
    }


    public static void loadImage(String imageName, IOnSuccessLoadingImage onSuccessLoadingImage) {
        StorageReference mImageStorage = FirebaseStorage.getInstance().getReference();
        StorageReference ref = mImageStorage.child("images").child(imageName);
        ref.getDownloadUrl()
                .addOnSuccessListener(uri -> {
                    //Log.d(TAG, "successful downloaded image: " + uri);
                    if (onSuccessLoadingImage != null){
                        onSuccessLoadingImage.onSuccess(uri);
                    }
                    String imageUrl = uri.toString();
                    //Log.d(TAG, "successful downloaded image after apply listener: " + imageUrl);
                })
                .addOnFailureListener(e -> Log.e("DB", e.getMessage()));

    }
}

