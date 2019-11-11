package com.example.sungshin.myapplication;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.Image;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import android.app.ActionBar;


public class MainActivity extends AppCompatActivity
{
    final int REQ_CODE_CAMERA_IMAGE = 0;
    int input = 0;
    final int REQ_CODE_CROP_IMAGE = 1;
    Bitmap bitmap = null;
    private LinearLayout mLayout;
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        startActivity(new Intent(this, Showapp.class));
        setCustomActionbar();
        ImageButton camera = (ImageButton) findViewById(R.id.camera);
        ImageButton album = (ImageButton) findViewById(R.id.album);
        ImageButton edit = (ImageButton) findViewById(R.id.edit);

        camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                input = 1;
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                intent.putExtra(MediaStore.EXTRA_OUTPUT,
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI.toString());

                // 이미지 잘라내기 위한 크기
                intent.putExtra("crop", "true");
                intent.putExtra("aspectX", 0);
                intent.putExtra("aspectY", 0);
                intent.putExtra("outputX", 200);
                intent.putExtra("outputY", 150);

                try {
                    intent.putExtra("return-data", true);
                    startActivityForResult(intent, REQ_CODE_CAMERA_IMAGE);
                } catch (ActivityNotFoundException e) {
                    // Do nothing for now
                }
            }


        });

        album.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                input = 1;
                Intent intent2 = new Intent();
                // Gallery 호출
                intent2.setType("image/*");
                intent2.setAction(Intent.ACTION_GET_CONTENT);
                // 잘라내기 셋팅
                intent2.putExtra("crop", "true");
                intent2.putExtra("aspectX", 0);
                intent2.putExtra("aspectY", 0);
                intent2.putExtra("outputX", 200);
                intent2.putExtra("outputY", 150);
                try {
                    intent2.putExtra("return-data", true);
                    startActivityForResult(Intent.createChooser(intent2,
                            "Complete action using"), REQ_CODE_CROP_IMAGE);
                } catch (ActivityNotFoundException e) {
                    // Do nothing for now
                }
            }
        });
        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (input == 1) {
                    Bitmap bitmap2 = null;
                    bitmap2 = bitmap;
                    Toast.makeText(getBaseContext(), "원하시는 부분의 테두리를 이어주세요", Toast.LENGTH_SHORT).show();
                    Intent intent5 = new Intent(MainActivity.this, Selectmode.class);
                    ByteArrayOutputStream stream = new ByteArrayOutputStream();
                    bitmap2.compress(Bitmap.CompressFormat.PNG, 100, stream);
                    byte[] bytes = stream.toByteArray();
                    intent5.putExtra("BMP", bytes);
                    startActivity(intent5);
                } else
                    Toast.makeText(getBaseContext(), "사진을 입력해주세요", Toast.LENGTH_SHORT).show();
            }
        });
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        //super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == REQ_CODE_CAMERA_IMAGE) {
            if (resultCode == RESULT_OK) {
                Bundle extras = data.getExtras();
                bitmap = extras.getParcelable("data");
                Toast.makeText(getBaseContext(), "사진이 등록되었습니다. ", Toast.LENGTH_SHORT).show();
                ImageView _image = (ImageView) findViewById(R.id.imageView4);
                _image.setAdjustViewBounds(true);
                _image.setImageBitmap(bitmap);

            }
        }
        else if(requestCode == REQ_CODE_CROP_IMAGE) {
            if (resultCode == RESULT_OK) {
                Bundle extras = data.getExtras();
                bitmap = extras.getParcelable("data");
                Toast.makeText(getBaseContext(), "사진이 등록되었습니다. ", Toast.LENGTH_SHORT).show();
                ImageView _image = (ImageView) findViewById(R.id.imageView4);
                _image.setAdjustViewBounds(true);
                _image.setImageBitmap(bitmap);
            }
        }

    }

    private void setCustomActionbar(){
        android.support.v7.app.ActionBar actionBar = getSupportActionBar();

        actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(false);
        actionBar.setDisplayShowTitleEnabled(false);

        View mCustomView = LayoutInflater.from(this).inflate(R.layout.actionbar_main, null);
        actionBar.setCustomView(mCustomView);
        Toolbar parent = (Toolbar) mCustomView.getParent();
        parent.setContentInsetsAbsolute(0, 0);
        actionBar.setBackgroundDrawable(getResources().getDrawable(R.drawable.main));


        android.support.v7.app.ActionBar.LayoutParams params = new android.support.v7.app.ActionBar.LayoutParams(ActionBar.LayoutParams.MATCH_PARENT, ActionBar.LayoutParams.MATCH_PARENT);
        actionBar.setCustomView(mCustomView, params);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);

    }

    @Override
    protected void onResume() {
        super.onResume();
    }
}