package com.example.sungshin.myapplication;
import android.app.ActionBar;
import android.app.Application;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.UserHandle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by sungshin on 2016-05-30.
 */
public class Savemode extends AppCompatActivity{
    Bitmap bitmap = null;
    String imgname = null;
    String imgfolder = null;
    String uri2 = null;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.savemode);
        setCustomActionbar();
        ImageButton save_button=(ImageButton) findViewById(R.id.download);
        ImageButton share_button=(ImageButton) findViewById(R.id.upload);

        Intent intent = getIntent();
        byte[] bytes = intent.getByteArrayExtra("BMP");
        bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
        ImageView img = (ImageView)findViewById(R.id.imageView);
        img.setImageBitmap(bitmap);

        share_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                shareBitmaptoPng(bitmap, "DOWNLOAD", "Image1");
            }
        });

        save_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                saveBitmaptoPng(bitmap, "DOWNLOAD", "Image1");


            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_save, menu);
        return super.onCreateOptionsMenu(menu);

    }
    public void shareBitmaptoPng(Bitmap bitmap,String folder, String name){

        String ex_storage = Environment.getExternalStorageDirectory().getAbsolutePath();
        String folder_name = "/" + folder + "/";
        String file_name = folder_name + imgname+".png";
        String string_path = ex_storage+ file_name;

        File imageFileToShare = new File(string_path);
        Uri uri = Uri.fromFile(imageFileToShare);
        //Toast.makeText(getBaseContext(), uri.toString(), Toast.LENGTH_SHORT).show();


        Intent shareIntent = new Intent();
        shareIntent.setAction(Intent.ACTION_SEND);
        shareIntent.putExtra(Intent.EXTRA_STREAM, uri);
        shareIntent.setType("image/jpeg");
        startActivity(Intent.createChooser(shareIntent, "공유하실 앱을 선택하세요"));
    }


    public void saveBitmaptoPng(Bitmap bitmap,String folder, String name){
        SimpleDateFormat dateFormat = new SimpleDateFormat( "yyyyMMdd_HHmmss");
        String todayDate = dateFormat.format(new Date());
        imgname = todayDate;
        String ex_storage = Environment.getExternalStorageDirectory().getAbsolutePath();
        String folder_name = "/" + folder + "/";
        String file_name = todayDate+".png";
        String string_path = ex_storage+ folder_name;
        File file_path;


        try {
            file_path = new File(string_path);
            if(!file_path.isDirectory()){
                file_path.mkdirs();
            }
            FileOutputStream out = new FileOutputStream(string_path+file_name);
            //Toast.makeText(getBaseContext(), string_path+file_name, Toast.LENGTH_SHORT).show();
            //Context context = null;
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, out);
            out.close();

            Savemode.this.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.parse(string_path + file_name)));
            MediaScannerConnection.scanFile(Savemode.this, new String[]{string_path+file_name}, null, null);

            String status = Environment.getExternalStorageState();
            if (status.equals(Environment.MEDIA_MOUNTED))
                Toast.makeText(getBaseContext(), file_name + "이 저장되었습니다.", Toast.LENGTH_SHORT).show();

        }catch(FileNotFoundException exception){
            Log.e("FileNotFoundException", exception.getMessage());
        }catch(IOException exception){
            Log.e("IOException", exception.getMessage());
        }
    }

    private void setCustomActionbar(){
        android.support.v7.app.ActionBar actionBar = getSupportActionBar();

        actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(false);
        actionBar.setDisplayShowTitleEnabled(false);

        View mCustomView = LayoutInflater.from(this).inflate(R.layout.savebar, null);
        actionBar.setCustomView(mCustomView);
        Toolbar parent = (Toolbar) mCustomView.getParent();
        parent.setContentInsetsAbsolute(0, 0);
        actionBar.setBackgroundDrawable(getResources().getDrawable(R.drawable.main));

        android.support.v7.app.ActionBar.LayoutParams params = new android.support.v7.app.ActionBar.LayoutParams(ActionBar.LayoutParams.MATCH_PARENT, ActionBar.LayoutParams.MATCH_PARENT);
        actionBar.setCustomView(mCustomView, params);

    }
}
