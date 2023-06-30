package com.example.nativefeatures;

import androidx.appcompat.app.AppCompatActivity;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.widget.Button;
import android.widget.ImageView;

import java.io.ByteArrayOutputStream;

public class MainActivity extends AppCompatActivity {

    private static final int TAKE_PICTURE = 1;

    private Bitmap photo;
    private ImageView iv_photo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /*
            Création du channel de notification
            Safe de le faire ici
            car un nouveau channel ne sera pas créé s'il existe déjà un channel avec le même id
         */
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel("channel", "channel", NotificationManager.IMPORTANCE_DEFAULT);
            channel.setDescription("description");
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }

        Button btn = findViewById(R.id.btn_img);
        btn.setOnClickListener( v -> {
            Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

            try {
                startActivityForResult(cameraIntent, TAKE_PICTURE);
            } catch (Exception e) {
                System.out.println(e);
            }
        });

        iv_photo = findViewById(R.id.iv_img);
        if (getIntent() != null && getIntent().getExtras() != null && getIntent().getExtras().containsKey("photo")) {
            byte[] byteArray = getIntent().getByteArrayExtra("photo");
            photo = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);
            iv_photo.setImageBitmap(photo);
        }

        Button switchToShare = findViewById(R.id.btn_switchToShare);
        switchToShare.setOnClickListener(e -> {
            if (photo != null) {
                Intent switchActivityIntent = new Intent(this, ShareActivity.class);
                //Convert photo to byte array

                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                photo.compress(Bitmap.CompressFormat.PNG, 100, stream);
                byte[] byteArray = stream.toByteArray();
                switchActivityIntent.putExtra("photo", byteArray);

                startActivity(switchActivityIntent);
            }
        });

        Button switchToNotification = findViewById(R.id.btn_switchToNotification);
        switchToNotification.setOnClickListener(e -> {
            if (photo != null) {
                Intent switchActivityIntent = new Intent(this, NotificationActivity.class);
                //Convert photo to byte array

                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                photo.compress(Bitmap.CompressFormat.PNG, 100, stream);
                byte[] byteArray = stream.toByteArray();
                switchActivityIntent.putExtra("photo", byteArray);

                startActivity(switchActivityIntent);
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == TAKE_PICTURE) {
            photo = (Bitmap) data.getExtras().get("data");
            iv_photo.setImageBitmap(photo);
        }
    }
}