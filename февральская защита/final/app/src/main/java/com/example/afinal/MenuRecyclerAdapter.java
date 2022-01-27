package com.example.afinal;

import android.util.Log;

import android.view.LayoutInflater;

import android.view.View;

import android.view.ViewGroup;

import androidx.annotation.NonNull;

import androidx.recyclerview.widget.RecyclerView;

import com.example.afinal.databinding.MenuListItemBinding;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;

import com.google.android.gms.tasks.OnFailureListener;

import com.google.android.gms.tasks.OnSuccessListener;

import com.google.firebase.firestore.DocumentReference;

import com.google.firebase.firestore.FirebaseFirestore;

import com.google.firebase.firestore.FirebaseFirestoreException;

import java.util.HashMap;

public class MenuRecyclerAdapter extends FirestoreRecyclerAdapter<Menus, MenuRecyclerAdapter.MenuViewHolder> {

    HashMap<String, String> favourite = new HashMap<>();

    FirebaseFirestore db = FirebaseFirestore.getInstance();

    private static String TAG = "menurecycleradapter";

    public interface OnItemClickListener {

        void OnItemClick(int position);

    }

    private final OnItemClickListener listener;

    MenuRecyclerAdapter(FirestoreRecyclerOptions<Menus> options, OnItemClickListener listener) {

        super(options);

        this.listener = listener;

    }

    public MenuRecyclerAdapter(FirestoreRecyclerOptions<Menus> options) {

        super(options);

        this.listener = null;

    }

    @Override

    public void onDataChanged() {

        super.onDataChanged();

        Log.d("op", "data changed");

    }

    @Override

    public void onError(@NonNull FirebaseFirestoreException e) {

        super.onError(e);

        Log.d("op", "err", e);

    }

    class MenuViewHolder extends RecyclerView.ViewHolder {

        private MenuListItemBinding binding;

        MenuViewHolder(MenuListItemBinding binding) {

            super(binding.getRoot());

            this.binding = binding;

        }

        void bind(Menus item) {

            binding.itemCreatedOn.setText(item.getName());

            binding.itemFirstName.setText(item.getDish());

            binding.itemFirstNameLabel.setText(item.getMetro());

            binding.itemLastName.setText(item.getWebsite());

            binding.itemLastNameLabel.setText(item.getPrice());

            binding.itemUserId.setText(item.getNtype());

            binding.btnaddToFav.setOnClickListener(new View.OnClickListener() {

                @Override

                public void onClick(View view) {

                    String Name = (String) binding.itemCreatedOn.getText();

                    String Dish = (String) binding.itemFirstName.getText();

                    String Metro = (String) binding.itemFirstNameLabel.getText();

                    String Website = (String) binding.itemLastName.getText();

                    String Price = (String) binding.itemLastNameLabel.getText();

                    String Ntype = (String) binding.itemUserId.getText();

                    favourite.put("name", Name);

                    favourite.put("dish", Dish);

                    favourite.put("metro", Metro);

                    favourite.put("website", Website);

                    favourite.put("price", Price);

                    favourite.put("Ntype", Ntype);

                    db.collection("favourites").add(favourite)

                            .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {

                                @Override

                                public void onSuccess(DocumentReference documentReference) {

                                    Log.d(TAG, "Menu added to favourites successfuly");

                                }

                            })

                            .addOnFailureListener(new OnFailureListener() {

                                @Override

                                public void onFailure(@NonNull Exception e) {

                                    Log.w(TAG, "Error adding menu to favourites");

                                }

                            });

                }

            });

            if (listener != null) {

                binding.getRoot().setOnClickListener(new View.OnClickListener() {

                    @Override

                    public void onClick(View v) {

                        listener.OnItemClick(getAbsoluteAdapterPosition());

                    }

                });

            }

        }

    }

    @Override

    public void onBindViewHolder(final MenuViewHolder holder, @NonNull int position, @NonNull final Menus menu) {

        holder.bind(menu);

    }

    @Override

    public MenuViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());

        MenuListItemBinding binding = MenuListItemBinding.inflate(layoutInflater, parent, false);

        return new MenuViewHolder(binding);

    }

}