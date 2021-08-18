package com.example.jihyun_vacation.repository;

import android.icu.text.UnicodeSet;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.jihyun_vacation.models.Country;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CountryRepository {
    private final String TAG = this.getClass().getCanonicalName();
    private final FirebaseFirestore db;
    private final String COLLECTION_NAME = "Favorite Countries";
    private final String KEY_NAME = "name";
    public MutableLiveData<List<Country>> allFavorites = new MutableLiveData<>();

    public CountryRepository() {
        this.db = FirebaseFirestore.getInstance();
    }

    public void getAllFavorites() {
        try {
            this.db.collection(COLLECTION_NAME)
                    .orderBy(KEY_NAME, Query.Direction.ASCENDING)
                    .addSnapshotListener(new EventListener<QuerySnapshot>() {
                        @Override
                        public void onEvent(@Nullable QuerySnapshot snapshot, @Nullable FirebaseFirestoreException error) {
                            if (error != null) {
                                Log.e(TAG, "onEvent: Listening to collection for document changes failed" + error);
                                return;
                            }
                            List<Country> tempFavoriteList = new ArrayList<>();

                            if (snapshot != null) {
                                Log.d(TAG, "onEvent: Current Data: " + snapshot.getDocumentChanges());

                           for (DocumentChange docChange : snapshot.getDocumentChanges()) {
                               Country currentCountry = docChange.getDocument().toObject(Country.class);
                               currentCountry.setId(docChange.getDocument().getId());
                               Log.e(TAG, "onEvent: Current Country : " + currentCountry.toString());

                               switch (docChange.getType()) {
                                   case ADDED:
                                       tempFavoriteList.add(currentCountry);
                                       break;
                                   case REMOVED:
                                       tempFavoriteList.remove(currentCountry);
                                       break;
                               }

                           }
                            allFavorites.postValue(tempFavoriteList);
                            }
                        }
                    });
        }catch(Exception ex) {
            Log.e(TAG, ex.getLocalizedMessage());
            Log.e(TAG, ex.toString());
        }
    }

    public void addFavorite(Country country) {
        try {
            Map<String, Object> data = new HashMap<>();
            data.put(KEY_NAME, country.getName());
            data.put("nativeName", country.getNativeName());
            data.put("code", country.getCode());
            data.put("subregion", country.getSubregion());
            data.put("capital", country.getCapital());
            
            this.db.collection(COLLECTION_NAME).add(data)
                    .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                        @Override
                        public void onSuccess(DocumentReference documentReference) {
                            Log.d(TAG, "onSuccess: Document created with ID : " + documentReference.getId());
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.e(TAG, "onFailure: Document can't be created" + e.getLocalizedMessage() );
                        }
                    });
        }catch (Exception ex) {
            Log.e(TAG, ex.toString());
            Log.e(TAG, ex.getLocalizedMessage());
        }
    }

    public void removeFavorite(String docID) {
        try {
            this.db.collection(COLLECTION_NAME).document(docID).delete()
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            Log.e(TAG, "onSuccess: Document successfully deleted");
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.e(TAG, "onFailure: Document couldn't be deleted successfully" + e.getLocalizedMessage());
                        }
                    });
        }catch (Exception ex) {
            Log.e(TAG, ex.toString());
            Log.e(TAG, ex.getLocalizedMessage());
        }
    }
}
