package com.example.afinal.ui.notifications;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.afinal.DataBase;
import com.example.afinal.R;
import com.example.afinal.ScreensActivity;
import com.example.afinal.databinding.FragmentNotificationsBinding;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.example.afinal.uploaderImage;

import java.io.IOException;

import de.hdodenhof.circleimageview.CircleImageView;

public class SettingsFragment extends Fragment {

    private static final int PICK_IMAGE_REQUEST = 234;


    private SettingsViewModel settingsViewModel;
    private FragmentNotificationsBinding binding;

    static final String TAG = "Settings";

    private Button btnReset;
    private CircleImageView circleImageView;
    private ImageView imageView;

    private FirebaseAuth mAuth;
    private FirebaseFirestore db;
    private ImageView profile_image_btn;
    private Button saveAvatarButton;

    private static uploaderImage uploaderImage;

    private DataBase DataBase;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        settingsViewModel = new ViewModelProvider(this).get(SettingsViewModel.class);

        binding = FragmentNotificationsBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        profile_image_btn = root.findViewById(R.id.profile_image_btn);
        saveAvatarButton = root.findViewById(R.id.saveAvatarBtn);

        profile_image_btn.setOnClickListener(this::OnClick);
        saveAvatarButton.setOnClickListener(this::OnClick);

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

    public void setImageButton (int requestCode, int resultCode, @Nullable Intent data, Activity activity) {
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
                DataBase.updateAvatar(avatarURL);
                Toast.makeText(requireContext(), "Успешно", Toast.LENGTH_SHORT).show();
                DataBase.getCurrentUserData(user -> {
                    Intent intent = new Intent(getActivity(), SettingsFragment.class);
                    startActivity(intent);
                });
            }
        }


    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;

    }

}