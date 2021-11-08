package com.example.afinal;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.afinal.ui.notifications.SettingsFragment;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.UUID;

public class uploaderImage {

    private static final int PICK_IMAGE_REQUEST = 234;
    private static final String TAG = "uploaderImage";
    private LayoutInflater inflater;

    private SettingsFragment activity;

    private Uri filePath;

    FirebaseStorage storage = FirebaseStorage.getInstance();
    StorageReference storageReference = storage.getReference();

    public Uri getFilePath() {
        return filePath;
    }

    public void setFilePath(Uri filePath){
        this.filePath = filePath;
    }

    public uploaderImage(@NonNull LayoutInflater inflater, @NonNull SettingsFragment activity){
        this.inflater = inflater;
        this.activity = activity;
    }

    public void showFileChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        activity.getActivity().startActivityForResult(Intent.createChooser(intent,"Выберите изображение"), PICK_IMAGE_REQUEST);
    }

    public boolean isEmpty() {
        if(filePath != null) {
            return false;
        }
        else {
            return true;
        }
    }


    public String uploadFileAvatar() {
        if (filePath != null) {
            final ProgressDialog progressDialog = new ProgressDialog(inflater.getContext());
            progressDialog.setTitle("Uploading");
            progressDialog.show();

            StorageReference ref = storageReference.child("images/avatars/" + UUID.randomUUID().toString() + ".png");
            ref.putFile(filePath)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                            progressDialog.dismiss();

                            Toast.makeText(activity.getContext().getApplicationContext(), "File Uploaded ", Toast.LENGTH_LONG).show();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception exception) {
                            progressDialog.dismiss();

                            Toast.makeText(activity.getContext().getApplicationContext(), exception.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                            double progress = (100.0 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount();
                            progressDialog.setMessage("Uploaded " + ((int) progress) + "%...");
                        }
                    });
            return "https://firebasestorage.googleapis.com/v0/b/edagofinal.appspot.com/o" + "/images%2Favatars%2F" + ref.getPath().substring(15) + "?alt=media";
        }
        else {
            return null;
        }


    }

    public String uploadDishPicture() {
        if (filePath != null) {
            final ProgressDialog progressDialog = new ProgressDialog(inflater.getContext());
            progressDialog.setTitle("Uploading");
            progressDialog.show();

            StorageReference ref = storageReference.child("images/pictures/" + UUID.randomUUID().toString() + ".png");
            ref.putFile(filePath)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                            progressDialog.dismiss();

                            Toast.makeText(activity.getContext().getApplicationContext(), "File Uploaded ", Toast.LENGTH_LONG).show();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception exception) {
                            progressDialog.dismiss();

                            Toast.makeText(activity.getContext().getApplicationContext(), exception.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                            double progress = (100.0 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount();
                            progressDialog.setMessage("Uploaded " + ((int) progress) + "%...");
                        }
                    });
            return "https://firebasestorage.googleapis.com/v0/b/edagofinal.appspot.com/o" + "/images%2Fpictures%2F" + ref.getPath().substring(15) + "?alt=media";
        }
        else {
            return null;
        }


    }

}
