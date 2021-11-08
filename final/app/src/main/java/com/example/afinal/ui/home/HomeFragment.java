package com.example.afinal.ui.home;

import android.os.Bundle;

import android.text.Editable;

import android.text.TextWatcher;

import android.util.Log;

import android.view.LayoutInflater;

import android.view.View;

import android.view.ViewGroup;

import android.widget.EditText;

import android.widget.TextView;

import androidx.annotation.NonNull;

import androidx.annotation.Nullable;

import androidx.fragment.app.Fragment;

import androidx.lifecycle.Observer;

import androidx.lifecycle.ViewModelProvider;

import androidx.recyclerview.widget.GridLayoutManager;

import androidx.recyclerview.widget.RecyclerView;

import com.example.afinal.MenuRecyclerAdapter;

import com.example.afinal.Menus;

import com.example.afinal.R;

import com.example.afinal.databinding.FragmentHomeBinding;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;

import com.google.firebase.firestore.FirebaseFirestore;

import com.google.firebase.firestore.Query;

public class HomeFragment extends Fragment {

    private HomeViewModel homeViewModel;

    private FragmentHomeBinding binding;

    private MenuRecyclerAdapter mAdapter;

    EditText searchBox;

    private RecyclerView recyclerView;

    private static final String TAG = "FirestoreSearchActivity";

    FirebaseFirestore db = FirebaseFirestore.getInstance();

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        homeViewModel = new ViewModelProvider(this).get(HomeViewModel.class);

        binding = FragmentHomeBinding.inflate(inflater, container, false);

        View root = binding.getRoot();

        View view = inflater.inflate(R.layout.fragment_home, container, false);

        recyclerView = view.findViewById(R.id.recycler_view);

        recyclerView.setHasFixedSize(true);

        recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 2));

        Query query = db.collection("Menu").orderBy("dish",Query.Direction.ASCENDING);

        FirestoreRecyclerOptions<Menus> options = new FirestoreRecyclerOptions.Builder<Menus>().setQuery(query, Menus.class).build();

        mAdapter = new MenuRecyclerAdapter(options);

        recyclerView.setAdapter(mAdapter);

        searchBox = root.findViewById(R.id.searchBox);

        searchBox.addTextChangedListener(new TextWatcher() {

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

                textView.setText(s);

            }

        });

        return root;

    }

    @Override

    public void onDestroyView() {

        super.onDestroyView();

        binding = null;

    }

}