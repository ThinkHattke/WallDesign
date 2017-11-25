package com.gaurav.walldesign;

import android.animation.Animator;
import android.app.WallpaperManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.io.IOException;

public class Wallpaper extends AppCompatActivity {

    ImageView imageView;

    Button set;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_wallpaper);

        imageView = (ImageView) findViewById(R.id.wallpaper);

        set = (Button) findViewById(R.id.setbutton);

        String id = getIntent().getStringExtra("id");

        String author = getIntent().getStringExtra("name");

        String likes = getIntent().getStringExtra("likes");

        TextView tx = (TextView) findViewById(R.id.textView);

        tx.setText(author);

        Typeface custom_font = Typeface.createFromAsset(getAssets(),  "fonts/fontstyle.ttf");

        tx.setTypeface(custom_font);

        Picasso.with(this)
                .load(id+"?w=500&h=600")
                .centerCrop()
                .resize(800,920)
                .error(R.drawable.error)
                .placeholder(R.drawable.placeholder)
                .into(new Target() {
                    @Override
                    public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {

                        imageView.setImageBitmap(bitmap);

                        supportStartPostponedEnterTransition();

                    }

                    @Override
                    public void onBitmapFailed(Drawable errorDrawable) {

                        imageView.setImageDrawable(errorDrawable);

                    }

                    @Override
                    public void onPrepareLoad(Drawable placeHolderDrawable) {

                        imageView.setImageDrawable(placeHolderDrawable);

                    }
                });

    }

    public void setwallpaper(View view){

        new AlertDialog.Builder(this).
                setMessage("Wallpaper Confirmation")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        WallpaperManager wallpaperManager = WallpaperManager.getInstance(getApplicationContext());
                        try{

                            Bitmap bitmap = ((BitmapDrawable)imageView.getDrawable()).getBitmap();
                            wallpaperManager.setBitmap(bitmap);

                        }catch (IOException e)
                        {
                            e.printStackTrace();
                        }

                        showSuccessDialog();

                    }
                })
                .setNegativeButton("No",null)
                .show();

    }

    private void showSuccessDialog() {
        new AlertDialog.Builder(this)
                .setMessage("Image set successfully")
                .setPositiveButton("OK", null)
                .show();
    }

}
