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

    private ArrayList<Painting> items;
    private LayoutInflater inflater;

    PaintingAdapter(Context context, ArrayList<Painting> items) {
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
        // Implemented ViewHolder design pattern for faster ListView rendering.
        ViewHolder holder;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.list_item_painting, parent, false);
            holder = new ViewHolder();
//            holder.thumbnailImageView = convertView.findViewById(R.id.painting_list_thumbnail);
            holder.titleTextView = convertView.findViewById(R.id.painting_list_title);
            holder.artistTextView = convertView.findViewById(R.id.painting_list_artist);
            holder.yearTextView = convertView.findViewById(R.id.painting_list_year);
            convertView.setTag(holder);
        } else holder = (ViewHolder) convertView.getTag();

        TextView titleTextView = holder.titleTextView;
        TextView artistTextView = holder.artistTextView;
        TextView yearTextView = holder.yearTextView;
//        ImageView thumbnailImageView = holder.thumbnailImageView;

        Painting painting = (Painting) getItem(position);
        titleTextView.setText(painting.getTitle());
        artistTextView.setText(painting.getArtist());
        // TODO: sort this out
        yearTextView.setText("Complete " + Integer.toString(painting.getYear()));

//        Picasso.with(context).load(recipe.imageUrl).placeholder(R.mipmap.ic_launcher).into(thumbnailImageView);

        return convertView;
    }

    private static class ViewHolder {
        TextView titleTextView;
        TextView artistTextView;
        TextView yearTextView;
        ImageView thumbnailImageView;
    }
}
