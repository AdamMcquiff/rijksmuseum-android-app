package com.cet325.bg72db;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.cet325.bg72db.SQLite.Models.Painting;

import java.util.ArrayList;

public class PaintingAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<Painting> items;
    private LayoutInflater inflater;

    public PaintingAdapter(Context context, ArrayList<Painting> items) {
        this.context = context;
        this.items = items;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public Object getItem(int position) {
        return items.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = inflater.inflate(R.layout.list_item_painting, parent, false);
        TextView titleTextView = row.findViewById(R.id.painting_list_title);
        TextView artistTextView = row.findViewById(R.id.painting_list_artist);
        TextView yearTextView = row.findViewById(R.id.painting_list_year);
        ImageView thumbnailImageView = row.findViewById(R.id.painting_list_thumbnail);

        Painting painting = (Painting) getItem(position);
        titleTextView.setText(painting.getTitle());
        artistTextView.setText(painting.getArtist());
        yearTextView.setText("Complete " + Integer.toString(painting.getYear()));

//        Picasso.with(context).load(recipe.imageUrl).placeholder(R.mipmap.ic_launcher).into(thumbnailImageView);

        return row;
    }
}
