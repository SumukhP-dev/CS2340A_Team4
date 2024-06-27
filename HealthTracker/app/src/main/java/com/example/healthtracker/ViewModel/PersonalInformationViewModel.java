package com.example.healthtracker.ViewModel;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;

import com.example.healthtracker.model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.AggregateQuery;
import com.google.firebase.firestore.AggregateQuerySnapshot;
import com.google.firebase.firestore.AggregateSource;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PersonalInformationViewModel extends ViewModel {
    private User user;

    public PersonalInformationViewModel() {
        user = User.getInstance();
    }

    // Updates personal information or creates a
    // document and enters said personal information in database
    public void addPersonalData(String name, Double height, Double weight,
                                String gender, String username) {
        CollectionReference citiesRef = user.getDatabase().collection("User");
        Query matchingDocuments = citiesRef.whereIn("User", Collections.singletonList(username));

        getCountOfDocument(matchingDocuments).addOnCompleteListener(new OnCompleteListener<AggregateQuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<AggregateQuerySnapshot> task) {
                if (task.isSuccessful()) {
                    // Count fetched successfully
                    if(task.getResult().getCount() != 0) {
                        // Updates existing user information
                        updateDocument(matchingDocuments, name, height, weight, gender);
                    } else {
                        createUserDocument(name, height, weight, gender, username);
                    }
                }
            }
        });
    }

    // Gets count of documents in query
    public Task<AggregateQuerySnapshot> getCountOfDocument(Query matchingDocuments) {
        AggregateQuery countQuery = matchingDocuments.count();

        return countQuery.get(AggregateSource.SERVER);
    }

    // Creates a new user with name, height, weight, gender, and username
    public void createUserDocument(String name, Double height, Double weight,
                                   String gender, String username) {
        Map<String, Object> user = new HashMap<>();
        user.put("name", name);
        user.put("height", height);
        user.put("weight", weight);
        user.put("gender", gender);
        user.put("username", username);

        // Add a new document with a generated ID
        this.user.getDatabase().collection("User")
                .add(user)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                    }
                });
    }

    // Updates personal information of user
    public void updateDocument(Query document, String name, Double height,
                               Double weight, String gender) {
        document.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    List<DocumentSnapshot> result = task.getResult().getDocuments();
                    DocumentReference documentToUpdate = result.get(0).getReference();
                    documentToUpdate.update("name", name);
                    documentToUpdate.update("height", height);
                    documentToUpdate.update("weight", weight);
                    documentToUpdate.update("gender", gender);
                }
            }
        });
    }
}
