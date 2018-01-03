package com.cet325.bg72db.PaintingListView;

import android.content.Context;
import android.content.ContextWrapper;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.cet325.bg72db.R;
import com.cet325.bg72db.SQLite.Models.Painting;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.lang.ref.WeakReference;
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
        // Implemented ViewHolder design pattern for faster ListView rendering.
        ViewHolder holder;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.list_item_painting, parent, false);
            holder = new ViewHolder();
            holder.thumbnailImageView = (ImageView) convertView.findViewById(R.id.painting_list_thumbnail);
            holder.titleTextView = (TextView) convertView.findViewById(R.id.painting_list_title);
            holder.artistTextView = (TextView) convertView.findViewById(R.id.painting_list_artist);
            holder.yearTextView = (TextView) convertView.findViewById(R.id.painting_list_year);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        // Get the painting and populate the activity text fields
        Painting painting = (Painting) getItem(position);
        holder.titleTextView.setText(painting.getTitle());
        holder.artistTextView.setText(painting.getArtist());
        holder.yearTextView.setText(context.getResources().getString(R.string.detail_painting_year, Integer.toString(painting.getYear())));

        // Loading the bitmaps in the background via an Async task
        BitmapWorkerTask task = new BitmapWorkerTask(holder.thumbnailImageView, context.getResources());
        if (painting.getImage() != null) {
            ContextWrapper cw = new ContextWrapper(context);
            File dir = cw.getDir("imageDir", Context.MODE_PRIVATE);
            task.execute(painting.getImage(), dir.toString());
        } else {
            task.execute(null, null);
        }

        return convertView;
    }

    private static class ViewHolder {
        TextView titleTextView;
        TextView artistTextView;
        TextView yearTextView;
        ImageView thumbnailImageView;
    }

    static class BitmapWorkerTask extends AsyncTask<String, Void, Bitmap> {
        private final WeakReference<ImageView> imageViewReference;
        private final Resources res;
        private String fileName = "";
        private String dir = "";

        BitmapWorkerTask(ImageView imageView, Resources res) {
            imageViewReference = new WeakReference<>(imageView);
            this.res = res;
        }

        @Override
        protected Bitmap doInBackground(String... params) {
            fileName = params[0];
            dir = params[1];

            // If painting has no image, provide placeholder
            if (fileName == null || dir == null) {
                return BitmapFactory.decodeResource(res, R.drawable.image_placeholder);
            }

            try {
                // Get the image, setup Bitmap options, set high sample size to reduce memory usage
                File file = new File(dir, fileName);
                BitmapFactory.Options options = new BitmapFactory.Options();
                options.inSampleSize = 16;
                return BitmapFactory.decodeStream(new FileInputStream(file), null, options);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            return null;
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
