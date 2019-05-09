package com.example.firebaseloginauth.Adapter;

import android.content.Context;
import android.net.Uri;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.firebaseloginauth.DTO.MusicDTO;
import com.example.firebaseloginauth.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class CustomMusicAdapter extends ArrayAdapter<MusicDTO> implements Filterable {

    Context context;
    int reresource;
    List<MusicDTO> obj;

    public CustomMusicAdapter(Context context, int resource, List<MusicDTO> objects) {
        super(context, resource, objects);
        this.context = context;
        this.reresource = resource;
        this.obj = objects;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater  layoutInflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
        View view = layoutInflater.inflate(reresource,parent,false);
        TextView textViewSinger = view.findViewById(R.id.textViewSinger);
        textViewSinger.setText(obj.get(position).getSinger().toString());
        TextView textViewDisplay = view.findViewById(R.id.textViewName);
        textViewDisplay.setText(obj.get(position).getDisplayname().toString());
        TextView textViewTime = view.findViewById(R.id.textViewTime);
        textViewTime.setText(obj.get(position).getTimeMusic().toString());
        ImageView imageView = view.findViewById(R.id.imageViewMusic);
        Uri myUri = Uri.parse(obj.get(position).getPhotoMusic());
        Picasso.with(context).load(myUri).into(imageView);
        ImageView imageViewBack = view.findViewById(R.id.imageViewList);
        imageViewBack.setImageResource(R.drawable.listitem2);
        return view;
    }
}
