package com.example.afinal.ui.dashboard;

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

import com.example.afinal.MenuRecyclerAdapter;
import com.example.afinal.Menus;
import com.example.afinal.R;
import com.example.afinal.Users;
import com.example.afinal.databinding.FragmentFavouritesBinding;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

public class FavouritesFragment extends Fragment {

    private FavouritesViewModel FavouritesViewModel;
    private FragmentFavouritesBinding binding;
    private MenuRecyclerAdapter mAdapter;

    FirebaseAuth mAuth;

    EditText searchBoxFav;

    String userId;

    private static final String TAG = "FirestoreSearchActivity";

    FirebaseFirestore db = FirebaseFirestore.getInstance();
    private Users user;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        FavouritesViewModel =
                new ViewModelProvider(this).get(FavouritesViewModel.class);

        binding = FragmentFavouritesBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        Query query = db.collection("favourites").orderBy("dish",Query.Direction.ASCENDING);

        FirestoreRecyclerOptions<Menus> options = new FirestoreRecyclerOptions.Builder<Menus>().setQuery(query, Menus.class).build();

        mAdapter = new MenuRecyclerAdapter(options);

        binding.recyclerViewFav.setLayoutManager(new GridLayoutManager(getActivity(), 2));

        binding.recyclerViewFav.setAdapter(mAdapter);

        searchBoxFav = root.findViewById(R.id.searchBox_fav);

        searchBoxFav.addTextChangedListener(new TextWatcher() {

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

                    query = db.collection("favourites");

                } else{

                    query = db.collection("favourites").whereEqualTo("dish", s.toString());

                }

                FirestoreRecyclerOptions<Menus> options = new FirestoreRecyclerOptions.Builder<Menus>().setQuery(query, Menus.class).build();

                Log.d("op", String.valueOf(options.getSnapshots().size()));

                mAdapter.updateOptions(options);

                mAdapter.notifyDataSetChanged();

            }

        });

        final TextView textView = binding.searchBoxFav;

        FavouritesViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {

            @Override

            public void onChanged(@Nullable String s) {

                //textView.setText(s);

            }

        });

        return root;

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