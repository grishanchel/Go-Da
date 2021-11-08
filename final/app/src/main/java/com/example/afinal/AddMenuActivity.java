package com.example.afinal;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class AddMenuActivity extends AppCompatActivity {

    FirebaseFirestore db;

    private static String TAG = "DataBase";

    private ImageView imageDwnldBtn;
    private Button submitBtn;
    LinearLayout layoutList;

    private static final int PICK_IMAGE_REQUEST = 234;

    TextInputEditText etMetro;
    TextInputEditText etName;
    TextInputEditText etWebsite;
    TextInputEditText etDish;
    TextInputEditText etPrice;

    String[] items = {"Бар", "Кафе", "Ресторан", "Кофейня", "Фастфуд", "Столовая"};
    AutoCompleteTextView etNType;
    ArrayAdapter<String> types;

    String menuId;

    private DataBase DataBase;
    private static uploaderImage uploaderImage;

    ArrayList<String> menu = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_menu);

        db = FirebaseFirestore.getInstance();

        layoutList = findViewById(R.id.layout_list);

        imageDwnldBtn = findViewById(R.id.imageDwnldBtn);
        submitBtn = findViewById(R.id.submitBtn);

        etMetro = findViewById(R.id.metro);
        etName = findViewById(R.id.Name);
        etWebsite = findViewById(R.id.website);
        etDish = findViewById(R.id.dish);
        etPrice = findViewById(R.id.price);
        etNType = findViewById(R.id.Ntype);

        ArrayAdapter arrayAdapter = new ArrayAdapter(this, R.layout.dropdown_item, items);
        etNType.setText(arrayAdapter.getItem(0).toString(),false);
        etNType.setAdapter(arrayAdapter);

        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                newMenu();
            }
        });

    }

    public void newMenu(){
        String metro = etMetro.getText().toString();
        String name = etName.getText().toString();
        String website = etWebsite.getText().toString();
        String dish = etDish.getText().toString();
        String price = etPrice.getText().toString();
        String Ntype = etNType.getText().toString();;

        if (TextUtils.isEmpty(metro)) {
            etMetro.setError("Введите станцию метро");
            etMetro.requestFocus();
        } else if (TextUtils.isEmpty(name)) {
            etName.setError("Введите название заведения");
            etName.requestFocus();
        } else if (TextUtils.isEmpty(dish)) {
            etDish.setError("Введите название блюда");
            etDish.requestFocus();
        }else if (TextUtils.isEmpty(Ntype)) {
            etNType.setError("Введите тип блюда");
            etNType.requestFocus();
        } else if (TextUtils.isEmpty(price)) {
            etPrice.setError("Введите цену на блюдо");
            etPrice.requestFocus();
        }else {
            website = "";
            Toast.makeText(AddMenuActivity.this, "<Блюдо успешно добавлено>", Toast.LENGTH_SHORT).show();
            Map<String,Object> menu = new HashMap<>();
            menu.put("name", name);
            menu.put("dish", dish);
            menu.put("metro", metro);
            menu.put("website", website);
            menu.put("price", price);
            menu.put("dish_image", "https://firebasestorage.googleapis.com/v0/b/edagofinal.appspo..");
            menu.put("Ntype", Ntype);
            db.collection("Menu").add(menu).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                @Override
                public void onSuccess(DocumentReference documentReference) {
                    Log.d(TAG, "Menu added successfuly");
                    Toast.makeText(AddMenuActivity.this, "Menu added", Toast.LENGTH_SHORT).show();
                }
            })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.w(TAG, "Error adding menu");
                            Toast.makeText(AddMenuActivity.this, "Error", Toast.LENGTH_SHORT).show();
                        }
                    });
        }

    }

    public void setImageButton (int requestCode, int resultCode, @Nullable Intent data, Activity activity) {
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK && data != null && data.getData() != null) {
            uploaderImage.setFilePath(data.getData());
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(activity.getContentResolver(), uploaderImage.getFilePath());
                imageDwnldBtn.setImageBitmap(bitmap);

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void OnClick(View view){
        if(view == imageDwnldBtn){
            uploaderImage.showFileChooser();
            String ImageURL = uploaderImage.uploadDishPicture();

            if (uploaderImage.isEmpty()){
                Toast.makeText(this, "Вы не выбрали фото", Toast.LENGTH_SHORT).show();
            }
            else{
                DataBase.updateAvatar(ImageURL);
                Toast.makeText(this, "Успешно", Toast.LENGTH_SHORT).show();
                DataBase.getCurrentMenuData(menu -> {
                    startActivity(new Intent(this, RegisterActivity.class));
                });
            }
        }

    }

}