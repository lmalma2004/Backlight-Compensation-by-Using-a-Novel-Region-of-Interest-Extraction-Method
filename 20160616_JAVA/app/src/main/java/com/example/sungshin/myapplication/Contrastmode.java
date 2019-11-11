package com.example.sungshin.myapplication;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
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
public class Contrastmode extends AppCompatActivity{
    Bitmap bitmap = null;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.contrastmode);
        Button refresh_button=(Button) findViewById(R.id.refresh);
        Button ok_button=(Button) findViewById(R.id.ok);
        Button textView_button=(Button) findViewById(R.id.textView);

        textView_button.setTypeface(Typeface.createFromAsset(getAssets(), "THEM.ttf"));
        refresh_button.setTypeface(Typeface.createFromAsset(getAssets(), "THEM.ttf"));
        ok_button.setTypeface(Typeface.createFromAsset(getAssets(), "THEM.ttf"));

        Intent intent = getIntent();
        byte[] bytes = intent.getByteArrayExtra("BMP");
        bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
        ImageView img = (ImageView)findViewById(R.id.imageView3);
        img.setImageBitmap(bitmap);
        Toast.makeText(getBaseContext(), "하단 바를 통해 대비를 조절해주세요.", Toast.LENGTH_SHORT).show();

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
                Intent intent = new Intent(Contrastmode.this, SkillActivity.class);
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
