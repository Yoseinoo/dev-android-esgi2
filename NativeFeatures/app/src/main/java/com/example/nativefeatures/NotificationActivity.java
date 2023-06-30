package com.example.nativefeatures;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.graphics.drawable.IconCompat;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;

public class NotificationActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);

        byte[] byteArray = getIntent().getByteArrayExtra("photo");
        Bitmap photo = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);

        ImageView imgView = findViewById(R.id.iv_photo_notif);
        imgView.setImageBitmap(photo);

        Button btn_notif = findViewById(R.id.btn_notif);
        btn_notif.setOnClickListener(event -> {
            NotificationCompat.Builder builder = new NotificationCompat.Builder(this, "channel")
                    .setSmallIcon(IconCompat.createWithAdaptiveBitmap(photo))
                    .setContentTitle("Notification")
                    .setContentText("Voici la notification envoyée depuis l'application de Florian Lebon")
                    .setPriority(NotificationCompat.PRIORITY_DEFAULT);

            Notification notification = builder.build();

            NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);

            // notificationId is a unique int for each notification that you must define
            if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
                return;
            }
            notificationManager.notify(1, notification);
        });

        Button btn_backToMain = findViewById(R.id.btn_backToMain);
        btn_backToMain.setOnClickListener(e -> {
            Intent mainIntent = new Intent(this, MainActivity.class);
            mainIntent.putExtra("photo", byteArray);
            startActivity(mainIntent);
        });
    }
}