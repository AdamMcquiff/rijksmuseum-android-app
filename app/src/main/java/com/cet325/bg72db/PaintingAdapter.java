package com.cet325.bg72db;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.cet325.bg72db.SQLite.Models.Painting;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.lang.ref.WeakReference;
import java.util.ArrayList;

public class PaintingAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<Painting> items;
    private LayoutInflater inflater;

    PaintingAdapter(Context context, ArrayList<Painting> items) {
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
        // Implemented ViewHolder design pattern for faster ListView rendering.
        ViewHolder holder;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.list_item_painting, parent, false);
            holder = new ViewHolder();
            holder.thumbnailImageView = convertView.findViewById(R.id.painting_list_thumbnail);
            holder.titleTextView = convertView.findViewById(R.id.painting_list_title);
            holder.artistTextView = convertView.findViewById(R.id.painting_list_artist);
            holder.yearTextView = convertView.findViewById(R.id.painting_list_year);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        Painting painting = (Painting) getItem(position);
        holder.titleTextView.setText(painting.getTitle());
        holder.artistTextView.setText(painting.getArtist());
        holder.yearTextView.setText(context.getResources().getString(R.string.detail_painting_year, Integer.toString(painting.getYear())));

        // Loading the bitmaps in the background via an Async task
        BitmapWorkerTask task = new BitmapWorkerTask(holder.thumbnailImageView);
        if (painting.getImage() != null) {
            task.execute(painting.getImage());
        } else {
            Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.image_placeholder);
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
            task.execute(stream.toByteArray());
        }

        return convertView;
    }

    private static class ViewHolder {
        TextView titleTextView;
        TextView artistTextView;
        TextView yearTextView;
        ImageView thumbnailImageView;
    }

    static class BitmapWorkerTask extends AsyncTask<byte[], Void, Bitmap> {
        private final WeakReference<ImageView> imageViewReference;
        private byte[] data = null;

        BitmapWorkerTask(ImageView imageView) {
            imageViewReference = new WeakReference<>(imageView);
        }

        @Override
        protected Bitmap doInBackground(byte[]... params) {
            data = params[0];
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inSampleSize = 16;
            ByteArrayInputStream is = new ByteArrayInputStream(data);
            return BitmapFactory.decodeStream(is, null, options);
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            if (bitmap != null) {
                final ImageView imageView = imageViewReference.get();
                if (imageView != null) {
                    imageView.setImageBitmap(bitmap);
                }
            }
        }
    }
}
