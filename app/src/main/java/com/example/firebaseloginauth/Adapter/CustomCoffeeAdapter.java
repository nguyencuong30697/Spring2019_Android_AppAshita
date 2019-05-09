package com.example.firebaseloginauth.Adapter;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.firebaseloginauth.DTO.CoffeeDTO;
import com.example.firebaseloginauth.DTO.MusicDTO;
import com.example.firebaseloginauth.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class CustomCoffeeAdapter extends ArrayAdapter<CoffeeDTO> implements Filterable {

    Context context;
    int reresource;
    List<CoffeeDTO> obj;

    public CustomCoffeeAdapter(Context context, int resource, List<CoffeeDTO> objects) {
        super(context, resource, objects);
        this.context = context;
        this.reresource = resource;
        this.obj = objects;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
        View view = layoutInflater.inflate(reresource,parent,false);
        TextView textViewSinger = view.findViewById(R.id.textViewNameCF);
        textViewSinger.setText(obj.get(position).getIfCoffee().toString());
        TextView textViewDisplay = view.findViewById(R.id.textViewInfoCoffee);
        textViewDisplay.setText(obj.get(position).getInformation().toString());
        ImageView imageView = view.findViewById(R.id.imageViewMusicCF);
        Uri myUri = Uri.parse(obj.get(position).getPhotoCF());
        Picasso.with(context).load(myUri).into(imageView);
        ImageView imageViewBack = view.findViewById(R.id.imageViewListCF);
        imageViewBack.setImageResource(R.drawable.listitem2);
        return view;
    }
}
