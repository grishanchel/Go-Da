package com.example.afinal;

import android.view.LayoutInflater;

import android.view.View;

import android.view.ViewGroup;

import android.widget.ImageView;

import android.widget.TextView;

import androidx.annotation.NonNull;

import androidx.appcompat.view.menu.MenuView;

import androidx.cardview.widget.CardView;

import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;

import com.squareup.okhttp.internal.DiskLruCache;

public class MenuRecyclerAdapter extends FirestoreRecyclerAdapter<Menus, MenuRecyclerAdapter.MenuViewHolder> {

    public interface OnItemClickListener{

        void OnItemClick (int position);

    }

    private final OnItemClickListener listener;

    MenuRecyclerAdapter(FirestoreRecyclerOptions<Menus> options, OnItemClickListener listener){

        super(options);

        this.listener = listener;

    }

    public MenuRecyclerAdapter(FirestoreRecyclerOptions<Menus> options){

        super(options);

        this.listener = null;

    }

    class MenuViewHolder extends RecyclerView.ViewHolder{

        final CardView view;

        final TextView name;

        final TextView dish;

        final TextView metro;

        final TextView website;

        final TextView price;

        final TextView Ntype;

        MenuViewHolder(CardView v){

            super(v);

            view = v;

            name = v.findViewById(R.id.Name);

            dish = v.findViewById(R.id.dish);

            metro = v.findViewById(R.id.metro);

            website = v.findViewById(R.id.website);

            price = v.findViewById(R.id.price);

            Ntype = v.findViewById(R.id.Ntype);

        }

    }

    @Override

    public void onBindViewHolder(final MenuViewHolder holder,@NonNull int position,@NonNull final Menus menu) {

        holder.name.setText(menu.getName());
        holder.dish.setText(menu.getDish());
        holder.metro.setText(menu.getMetro());
        holder.website.setText(menu.getWebsite());
        holder.price.setText(menu.getPrice());
        holder.Ntype.setText(menu.getNtype());

        if (listener != null){

            holder.view.setOnClickListener(new View.OnClickListener() {

                @Override

                public void onClick(View v) {

                    listener.OnItemClick(holder.getAbsoluteAdapterPosition());

                }

            });

        }

    }

    @Override

    public MenuViewHolder onCreateViewHolder(ViewGroup parent, int viewType){

        CardView v = (CardView) LayoutInflater.from(parent.getContext()).inflate(viewType, parent, false);

        return new MenuViewHolder(v);

    }

    @Override

    public int getItemViewType(final int position) {

        return R.layout.menu_list_item;

    }

}
