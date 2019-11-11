package com.example.sungshin.myapplication;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;

/**
 * Created by sungshin on 2016-05-30.
 */
public class Stickermode extends AppCompatActivity{
    Bitmap bitmap = null;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.stickermode);
        Button refresh_button=(Button) findViewById(R.id.refresh);
        Button ok_button=(Button) findViewById(R.id.ok);

        Intent intent = getIntent();
        byte[] bytes = intent.getByteArrayExtra("BMP");
        bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
        ImageView img = (ImageView)findViewById(R.id.imageView3);
        img.setImageBitmap(bitmap);
        Toast.makeText(getBaseContext(), "도려낼 부분의 윤곽선을 손으로 선택해주세요.", Toast.LENGTH_SHORT).show();
        refresh_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ImageView img = (ImageView)findViewById(R.id.imageView3);
                img.setImageBitmap(bitmap);
            }
        });

        ok_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Stickermode.this, SkillActivity.class);
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
                byte[] bytes = stream.toByteArray();
                intent.putExtra("BMP", bytes);
                // intent.putExtra("bm",bitmap2);
                startActivity(intent);
            }
        });

    }
}
