package com.axdev.thequotesgarden;

import android.app.Activity;
import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

public class JokesListAdapter extends ArrayAdapter<Joke>{
    int resource;
    ArrayList<Joke> jokeArrayList;
    Context context;
    private int lastPosition = -1;
    private RoundImage roundedImage;

    public JokesListAdapter(Context context, int resource,ArrayList<Joke> jokeArrayList) {
        super(context, resource,jokeArrayList);
        this.context = context;
        this.resource = resource;
        this.jokeArrayList = jokeArrayList;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;

        ImageHolder holder = null;
        if (row == null) {
            LayoutInflater inflater = ((Activity) context).getLayoutInflater();
            row = inflater.inflate(resource, parent, false);
            holder = new ImageHolder();
            holder.txtTitle = (TextView) row.findViewById(R.id.txtTitle);
            holder.imgIcon = (ImageView) row.findViewById(R.id.imgIcon);
            holder.txtQuote = (TextView) row.findViewById(R.id.txtQuote);
            holder.txtCategory = (TextView) row.findViewById(R.id.txtCategory);

            Typeface font = Typeface.createFromAsset(context.getAssets(),
                    "fonts/Roboto-Light.ttf");
            holder.txtTitle.setTypeface(font);
            holder.txtTitle.setTextSize(16);
            holder.txtQuote.setTypeface(font);
            holder.txtQuote.setTextSize(16);
            holder.txtCategory.setTypeface(font);
            holder.txtCategory.setTextSize(14);
            row.setTag(holder);
        } else {
            holder = (ImageHolder) row.getTag();
        }

        Animation animation = AnimationUtils.loadAnimation(getContext(),
                (position > lastPosition) ? R.anim.up_from_bottom
                        : R.anim.down_from_top);
        row.startAnimation(animation);
        lastPosition = position;

        Joke picture = jokeArrayList.get(position);
        holder.txtTitle.setText(picture.get_name());
        holder.txtQuote.setText(picture.get_quote());
        holder.txtCategory.setText("  " + picture.get_category() + "  ");

        boolean isExist = false;
        AssetManager assetManager = context.getAssets();
        InputStream imageStream = null;
        try {
            imageStream = assetManager.open("jokers/"+picture.get_fileName()+".jpg");

            isExist =true;
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        if (isExist != false){
            Bitmap theImage = BitmapFactory.decodeStream(imageStream);
            roundedImage = new RoundImage(theImage);
            holder.imgIcon.setImageDrawable(roundedImage );
        }
        else {
            Bitmap bm = BitmapFactory.decodeResource(context.getResources(),R.mipmap.author);
            roundedImage = new RoundImage(bm);
            holder.imgIcon.setImageDrawable(roundedImage);
        }
        return row;
    }

    static class ImageHolder {
        ImageView imgIcon;
        TextView txtTitle;
        TextView txtQuote;
        TextView txtCategory;

    }
}
