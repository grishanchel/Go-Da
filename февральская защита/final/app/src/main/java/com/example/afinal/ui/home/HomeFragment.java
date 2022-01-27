package com.example.afinal.ui.home;

import android.os.Bundle;

import android.text.Editable;

import android.text.TextWatcher;

import android.util.Log;

import android.view.LayoutInflater;

import android.view.View;

import android.view.ViewGroup;

import android.widget.Button;

import android.widget.EditText;

import android.widget.TextView;

import android.widget.Toast;

import androidx.annotation.NonNull;

import androidx.annotation.Nullable;

import androidx.appcompat.view.menu.MenuView;

import androidx.fragment.app.DialogFragment;

import androidx.fragment.app.Fragment;

import androidx.fragment.app.FragmentManager;

import androidx.lifecycle.Observer;

import androidx.lifecycle.ViewModelProvider;

import androidx.recyclerview.widget.GridLayoutManager;

import androidx.recyclerview.widget.RecyclerView;

import com.example.afinal.AddMenuActivity;

import com.example.afinal.CustomDialogFragmentHome;

import com.example.afinal.MenuRecyclerAdapter;

import com.example.afinal.Menus;

import com.example.afinal.R;

import com.example.afinal.databinding.FragmentHomeBinding;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;

import com.google.android.gms.tasks.OnFailureListener;

import com.google.android.gms.tasks.OnSuccessListener;

import com.google.firebase.firestore.DocumentReference;

import com.google.firebase.firestore.FirebaseFirestore;

import com.google.firebase.firestore.Query;

import java.util.HashMap;

public class HomeFragment extends Fragment {

    private HomeViewModel homeViewModel;

    private FragmentHomeBinding binding;

    private MenuRecyclerAdapter mAdapter;

    private static final String TAG = "FirestoreSearchActivity";

    FirebaseFirestore db = FirebaseFirestore.getInstance();

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentHomeBinding.inflate(inflater, container, false);

        homeViewModel = new ViewModelProvider(this).get(HomeViewModel.class);

        binding.filterBtnHome.setOnClickListener(new View.OnClickListener() {

            @Override

            public void onClick(View v) {

                Log.d(TAG, "onClick: opening dialog");

                CustomDialogFragmentHome dialog = new CustomDialogFragmentHome();

                //dialog.setTargetFragment(HomeFragment.this, 1);

                dialog.show(getChildFragmentManager(), "MyCustomDialog");

                Query query = db.collection("Menu");

            }

        });

        Query query = db.collection("Menu").orderBy("dish",Query.Direction.ASCENDING);

        FirestoreRecyclerOptions<Menus> options = new FirestoreRecyclerOptions.Builder<Menus>().setQuery(query, Menus.class).build();

        mAdapter = new MenuRecyclerAdapter(options);

        binding.recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 2));

        binding.recyclerView.setAdapter(mAdapter);

        binding.searchBox.addTextChangedListener(new TextWatcher() {

            @Override

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override

            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override

            public void afterTextChanged(Editable s) {

                Log.d(TAG, "Searchbox has changed to:" + s.toString());

                Query query;

                if (s.toString().isEmpty()){

                    query = db.collection("Menu");

                } else{

                    query = db.collection("Menu").whereEqualTo("dish", s.toString());

                }

                FirestoreRecyclerOptions<Menus> options = new FirestoreRecyclerOptions.Builder<Menus>().setQuery(query, Menus.class).build();

                Log.d("op", String.valueOf(options.getSnapshots().size()));

                mAdapter.updateOptions(options);

                mAdapter.notifyDataSetChanged();

            }

        });

        final TextView textView = binding.searchBox;

        homeViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {

            @Override

            public void onChanged(@Nullable String s) {

//textView.setText(s);

            }

        });

        return binding.getRoot();

    }

    @Override

    public void onDestroyView() {

        super.onDestroyView();

        binding = null;

    }

    @Override

    public void onStart() {

        super.onStart();

        mAdapter.startListening();

    }

    @Override

    public void onPause() {

        super.onPause();

        mAdapter.stopListening();

    }

}