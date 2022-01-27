package com.example.afinal;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class RegisterActivity extends AppCompatActivity {

    TextInputEditText etRegEmail;
    TextInputEditText etRegPassword;
    TextView tvLoginHere;
    Button btnRegister;
    Button addMenuBtn;
    FirebaseFirestore db;
    String userId;

    FirebaseAuth mAuth;

    private static String TAG = "DataBase";

    private void createUser() {
        String email = etRegEmail.getText().toString();
        String password = etRegPassword.getText().toString();
        db = FirebaseFirestore.getInstance();
        String name = " ";
        String dish = " ";
        String metro = " ";
        String website = " ";
        String price = " ";
        String Ntype = " ";

        if (TextUtils.isEmpty(email)) {
            etRegEmail.setError("Введите email");
            etRegEmail.requestFocus();
        } else if (TextUtils.isEmpty(password)) {
            etRegPassword.setError("Введите пароль");
            etRegPassword.requestFocus();
        } else {
            mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()){
                        Toast.makeText(RegisterActivity.this, "Пользователь успешно авторизирован", Toast.LENGTH_SHORT).show();
                        userId = mAuth.getCurrentUser().getUid();
                        DocumentReference documentReference = db.collection("users").document(userId);
                        Map<String,Object> user = new HashMap<>();
                        user.put("email", email);
                        user.put("password", password);
                        user.put("avatar", "https://firebasestorage.googleapis.com/v0/b/edagofinal.appspot.com/o/images%2Fedago_settings_photoprofile.png?alt=media");
                        Map<String,Object> favourite = new HashMap<>();
                        favourite.put("name", name);
                        favourite.put("dish", dish);
                        favourite.put("metro", metro);
                        favourite.put("website", website);
                        favourite.put("price", price);
                        favourite.put("dish_image", "https://firebasestorage.googleapis.com/v0/b/edagofinal.appspo..");
                        favourite.put("Ntype", Ntype);
                        db.collection("users").document(userId).collection("favourites").add(favourite);
                        documentReference.set(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                Log.d(TAG, "onSuccess: user Profile is created for"+ userId);
                                FirebaseUser currentUser = mAuth.getCurrentUser();
                                updateUI(currentUser, RegisterActivity.this);
                            }
                        });

                        startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
                    }else{
                        Toast.makeText(RegisterActivity.this, "Ошибка регистрации: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();

                    }

                }
            });
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        etRegEmail = findViewById(R.id.etRegEmail);
        etRegPassword = findViewById(R.id.etRegPass);
        tvLoginHere = findViewById(R.id.tvLoginHere);
        btnRegister = (Button) findViewById(R.id.btnRegister);
        addMenuBtn = findViewById(R.id.addMenuBtn);

        mAuth = FirebaseAuth.getInstance();

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createUser();
            }
        });

        tvLoginHere.setOnClickListener(view ->{
            startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
        });

        addMenuBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(RegisterActivity.this, AddMenuActivity.class));
            }
        });
    }

    public void updateUI (FirebaseUser account, Activity activity){
        if (account!=null){
            mAuth.signOut();
            activity.startActivity(new Intent(activity, ScreensActivity.class));
        }
    }

}