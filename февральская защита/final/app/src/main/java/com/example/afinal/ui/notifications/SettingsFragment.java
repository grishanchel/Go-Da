package com.example.afinal.ui.notifications;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.afinal.AddMenuActivity;
import com.example.afinal.DataBase;
import com.example.afinal.LoginActivity;
import com.example.afinal.R;
import com.example.afinal.RegisterActivity;
import com.example.afinal.ScreensActivity;
import com.example.afinal.Users;
import com.example.afinal.databinding.FragmentSettingsBinding;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.example.afinal.uploaderImage;

import java.io.IOException;

import de.hdodenhof.circleimageview.CircleImageView;

public class SettingsFragment extends Fragment {

    private static final int PICK_IMAGE_REQUEST = 234;
    private static ImageView profile_image_btn;

    Users user;

    private SettingsViewModel settingsViewModel;
    private FragmentSettingsBinding binding;

    static final String TAG = "Settings";

    private Button btnReset;
    private CircleImageView circleImageView;
    private ImageView imageView;

    private FirebaseAuth mAuth;
    private FirebaseFirestore db;
    private Button saveAvatarButton;
    private Button btnLogout;

    private static uploaderImage uploaderImage;

    private DataBase DataBase;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        settingsViewModel = new ViewModelProvider(this).get(SettingsViewModel.class);

        binding = FragmentSettingsBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        profile_image_btn = root.findViewById(R.id.profile_image_btn);
        saveAvatarButton = root.findViewById(R.id.saveAvatarBtn);
        btnReset = root.findViewById(R.id.btnReset);
        btnLogout = root.findViewById(R.id.btnLogOut);

        profile_image_btn.setOnClickListener(this::OnClick);
        saveAvatarButton.setOnClickListener(this::OnClick);
        btnLogout.setOnClickListener(this::OnClick);

        btnReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                EditText resetMail = new EditText(v.getContext());
                AlertDialog.Builder passwordResetDialog = new AlertDialog.Builder(v.getContext());
                passwordResetDialog.setTitle("Reset Password ?");
                passwordResetDialog.setMessage("Enter Your Email To Receive Reset Link.");
                passwordResetDialog.setView(resetMail);

                passwordResetDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    // extract the email and send reset link
                        String mail = resetMail.getText().toString();
                        mAuth.sendPasswordResetEmail(mail).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Toast.makeText(getActivity(), "Ссылка для восстановлекния отправлена на почту", Toast.LENGTH_SHORT).show();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(getActivity(), "Ошибка" + e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });

                    }
                });

                passwordResetDialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
// close the dialog
                    }
                });
                passwordResetDialog.create().show();

            }
        });

        uploaderImage = new uploaderImage(getLayoutInflater(), this);

        final TextView textView = binding.textSettings;
        settingsViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });
        return root;
    }


    public static void setImageButton(int requestCode, int resultCode, @Nullable Intent data, Activity activity) {
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK && data != null && data.getData() != null) {
            uploaderImage.setFilePath(data.getData());
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(activity.getContentResolver(), uploaderImage.getFilePath());
                profile_image_btn.setImageBitmap(bitmap);

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void OnClick(View view){
        if(view == profile_image_btn){
            uploaderImage.showFileChooser();
        }
        if(view == saveAvatarButton){
            String avatarURL = uploaderImage.uploadFileAvatar();

            if (uploaderImage.isEmpty()){
                Toast.makeText(requireContext(), "Вы не выбрали фото", Toast.LENGTH_SHORT).show();
            }
            else{
                mAuth = FirebaseAuth.getInstance();
                db = FirebaseFirestore.getInstance();
                DocumentReference documentReference = db.collection("users").document(mAuth.getCurrentUser().getUid());
                documentReference.update("avatar", avatarURL);
                Toast.makeText(requireContext(), "Успешно", Toast.LENGTH_SHORT).show();
            }
        }

        if(view == btnLogout){
            startActivity(new Intent(getActivity(), LoginActivity.class));
        }


    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;

    }

}