package com.example.firebaseloginauth.Adapter;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.firebaseloginauth.DTO.CommentDTO;
import com.example.firebaseloginauth.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class CustomCommentAdapter extends ArrayAdapter<CommentDTO> {

    Context context;
    int reresource;
    List<CommentDTO> obj;

    public CustomCommentAdapter(Context context, int resource, List<CommentDTO> objects) {
        super(context, resource, objects);
        this.context = context;
        this.reresource = resource;
        this.obj = objects;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
        View view = layoutInflater.inflate(reresource,parent,false);
        TextView textViewDisplay = view.findViewById(R.id.displayNameComment);
        textViewDisplay.setText(obj.get(position).getDisplayName().toString());
        TextView textViewText = view.findViewById(R.id.stringComment);
        textViewText.setText(obj.get(position).getStringComment().toString());
        return view;
    }
}

