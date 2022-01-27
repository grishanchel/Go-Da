package com.example.afinal;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.util.Log;

import androidx.annotation.NonNull;

public class DataBase {

    private String docId, uid, email, password, avatar, Ntype;

    private FirebaseAuth mAuth;
    private static String TAG = "DataBase";
    private FirebaseFirestore db;
    public Users user;
    public Menus menu;

    private int userId = 0;
    private int menuId = 0;

    public void updateAvatar(String avatarURL){
        getCurrentUserData(user -> {
            mAuth = FirebaseAuth.getInstance();
            DocumentReference documentReference = FirebaseFirestore.getInstance().collection("users").document(user.getDocId());
            documentReference.update("avatar", avatarURL);
        });
    }

    public static String getCurrentUserUid () {
        return FirebaseAuth.getInstance().getCurrentUser().getUid();
    }

    public void getCurrentUserData(UserDataReceiveListener listener){

        if(user != null){
            listener.onUserReceived(user);
        }

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        db.collection("users")
                .whereEqualTo("email", mAuth.getCurrentUser().getEmail())
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()){
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d(TAG, document.getId() + " => " + document.getData());

                                user = new Users(document.getId(), document.get("uid").toString(), document.get("email").toString(), document.get("password").toString(), document.get("avatar").toString());
                                listener.onUserReceived(user);

                            }
                        } else {
                            Log.w(TAG, "Error getting documents.", task.getException());
                        }
                    }
                });

    }

    public void getCurrentMenuData(MenuDataReceiveListener listener){

        if (menu != null){
            listener.onMenuDataReceive(menu);
        }

        db = FirebaseFirestore.getInstance();
        db.collection("menus")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()){
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d(TAG, document.getId() + " => " + document.getData());

                                menu = new Menus(document.getId(), document.get("Name").toString(), document.get("Dish").toString(),document.get("Metro").toString(),document.get("Website").toString(),document.get("Price").toString(), document.get("dish_image").toString(), document.get("Ntype").toString());
                                listener.onMenuDataReceive(menu);

                            }
                        } else {
                            Log.w(TAG, "Error getting documents.", task.getException());
                        }
                    }
                });


    }


}