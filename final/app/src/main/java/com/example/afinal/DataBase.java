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

    public void newUser (String email, String password) {
        db = FirebaseFirestore.getInstance();

        Map<String, Object> user = new HashMap<>();
        user.put("email", email);
        user.put("password", password);
        user.put("avatar", "https://firebasestorage.googleapis.com/v0/b/edagofinal.appspot.com/o/images%2Fedago_settings_photoprofile.png?alt=media");


        db.collection("users")
                .add(user)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Log.d(TAG, "DocumentShapshot added with ID:" + documentReference.getId());
                        userId = userId + 1;
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error adding document", e);
                    }
                });

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

    public void newMenu(String Name, String Dish, String Metro, String website, String price, String dish_image, String Ntype) {

        db=FirebaseFirestore.getInstance();

        Map<String, Object> menu = new HashMap<>();
        menu.put("Name", Name);
        menu.put("Dish", Dish);
        menu.put("Metro", Metro);
        menu.put("Website", website);
        menu.put("price", price);
        menu.put("dish_image", "https://firebasestorage.googleapis.com/v0/b/edagofinal.appspot.com/o/images%2Fdish_image.png?alt=media");
        menu.put("Ntype", Ntype);


        db.collection("menus")
                .add(menu)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Log.d(TAG, "DocumentSnapshot added with ID:" + documentReference.getId());
                        menuId = menuId + 1;
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error adding document", e);
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