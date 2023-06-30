package com.example.nativefeatures;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.widget.Button;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class ShareActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_share);

        byte[] byteArray = getIntent().getByteArrayExtra("photo");
        Bitmap photo = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);

        ImageView imgView = findViewById(R.id.iv_photo_share);
        imgView.setImageBitmap(photo);

        Button btn_share = findViewById(R.id.btn_share);
        btn_share.setOnClickListener(event -> {
            Intent share = new Intent(Intent.ACTION_SEND);
            share.setType("image/jpeg");
            File f = new File(Environment.getExternalStorageDirectory() + File.separator + "photo.jpg");
            try {
                f.createNewFile();
                FileOutputStream fo = new FileOutputStream(f);
                fo.write(byteArray);
                fo.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            share.putExtra(Intent.EXTRA_STREAM, Uri.parse(Environment.getExternalStorageDirectory().getPath() + "/photo.jpg"));
            startActivity(Intent.createChooser(share, "Share Image"));
        });

        Button btn_backToMain = findViewById(R.id.btn_backToMain);
        btn_backToMain.setOnClickListener(e -> {
            Intent mainIntent = new Intent(this, MainActivity.class);
            mainIntent.putExtra("photo", byteArray);
            startActivity(mainIntent);
        });
    }
}