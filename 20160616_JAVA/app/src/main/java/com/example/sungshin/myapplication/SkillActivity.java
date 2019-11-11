package com.example.sungshin.myapplication;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Toast;

import org.opencv.android.Utils;
import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.Scalar;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by sungshin on 2016-05-30.
 */
public class SkillActivity extends AppCompatActivity {
    Bitmap bitmap2 = null;
    Bitmap bitmap = null;
    Bitmap surface_image;
    String imgname = null;
    String imgfolder = null;
    String uri2 = null;
    Bitmap addimage;
    Skill_SubView drawView;

    static {

        System.loadLibrary("opencv_java3");

    }
    public void clicked(View view) {
        ImageButton bright2 = (ImageButton)findViewById(R.id.bright2);
        ImageButton contrast2 = (ImageButton)findViewById(R.id.contrast2);
        ImageButton color2 = (ImageButton)findViewById(R.id.color2);
       // ImageButton backlight2 = (ImageButton)findViewById(R.id.backlight2);

        bright2.setBackgroundDrawable(getResources().getDrawable(R.drawable.bright6));
        contrast2.setBackgroundDrawable(getResources().getDrawable(R.drawable.contrast6));
        color2.setBackgroundDrawable(getResources().getDrawable(R.drawable.color6));
       // backlight2.setBackgroundDrawable(getResources().getDrawable(R.drawable.backlight));


        String flag = view.getTag().toString();
        if(flag.equals("bright")){
            bright2.setBackgroundDrawable(getResources().getDrawable(R.drawable.bright7));
            drawView.modify_flag=1;
            if(drawView.modify_flag2==3) {
                for (int i = 0; i < drawView.canvasBitmap_mask.getWidth(); i++) {
                    for (int j = 0; j < drawView.canvasBitmap_mask.getHeight(); j++) {
                        int p = drawView.canvasBitmap_mask.getPixel(i, j);
                        int r = Color.red(p);
                        int g = Color.green(p);
                        int b = Color.blue(p);
                        if (r == 0 && g == 0 && b == 0) {
                            drawView.canvasBitmap_mask.setPixel(i, j, Color.TRANSPARENT);
                        }
                    }
                }
            }

        }
        else if(flag.equals("contrast")){
            contrast2.setBackgroundDrawable(getResources().getDrawable(R.drawable.contrast7));
            drawView.modify_flag=2;
            drawView.modify_flag2=2;
        }
        else if(flag.equals("color")){
            color2.setBackgroundDrawable(getResources().getDrawable(R.drawable.color7));
            drawView.modify_flag=3;
                for (int i = 0; i < drawView.canvasBitmap_mask.getWidth(); i++) {
                    for (int j = 0; j < drawView.canvasBitmap_mask.getHeight(); j++) {
                        int p = drawView.canvasBitmap_mask.getPixel(i, j);
                        int r = Color.red(p);
                        int g = Color.green(p);
                        int b = Color.blue(p);
                        if (r == 0 && g == 0 && b == 0) {
                            drawView.canvasBitmap_mask.setPixel(i, j, Color.TRANSPARENT);
                        }
                    }
                }


        }
        else{
            Toast.makeText(getBaseContext(),"서비스 준비중 입니다.", Toast.LENGTH_SHORT).show();
            drawView.modify_flag=4;
            if(drawView.modify_flag2==3) {
                for (int i = 0; i < drawView.canvasBitmap_mask.getWidth(); i++) {
                    for (int j = 0; j < drawView.canvasBitmap_mask.getHeight(); j++) {
                        int p = drawView.canvasBitmap_mask.getPixel(i, j);
                        int r = Color.red(p);
                        int g = Color.green(p);
                        int b = Color.blue(p);
                        if (r == 0 && g == 0 && b == 0) {
                            drawView.canvasBitmap_mask.setPixel(i, j, Color.TRANSPARENT);
                        }
                    }
                }
            }
        }

    }

    //사진합치기
    public Bitmap overlayMark(Bitmap bmp1, Bitmap bmp2, int distanceLeft, int distanceTop) {
        Bitmap bmOverlay = Bitmap.createBitmap(bmp1.getWidth() + distanceLeft,
                bmp1.getHeight() + distanceLeft, bmp1.getConfig());
        Canvas canvas = new Canvas(bmOverlay);
        canvas.drawBitmap(bmp1, distanceLeft, distanceTop, null);
        canvas.drawBitmap(bmp2, 0, 0, null);
        return bmOverlay;
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.skill_main);
        setCustomActionbar();
        //setCustomActionbar2();
        Intent intent = getIntent();
        byte[] bytes = intent.getByteArrayExtra("BMP");
        byte[] bytes2 = intent.getByteArrayExtra("BMP2");
        bitmap2 = BitmapFactory.decodeByteArray(bytes2, 0, bytes2.length); //원본
        bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length); //마스크




        final Bitmap resizedbitmap=Bitmap.createScaledBitmap(bitmap, bitmap2.getWidth(), bitmap2.getHeight(), true);
        Bitmap result = Bitmap.createBitmap(resizedbitmap.getWidth(),resizedbitmap.getHeight(), Bitmap.Config.ARGB_8888);

        Canvas canvas = new Canvas(result);
        canvas.drawBitmap(bitmap2,0,0,null);
        Paint paint=new Paint(Paint.ANTI_ALIAS_FLAG);
       // paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.LIGHTEN)); //반전
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DARKEN));
       // paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_IN));
        canvas.drawBitmap(resizedbitmap,0,0,paint);
        paint.setXfermode(null);

        //bitmap=result;

        final Bitmap result2=result.copy(Bitmap.Config.ARGB_8888,true);


        for(int i=0; i<result2.getWidth();i++){
            for(int j=0;j<result2.getHeight();j++){
                int p=result2.getPixel(i,j);
                int r=Color.red(p);
                int g=Color.green(p);
                int b=Color.blue(p);
                if(r==0&&g==0&&b==0) {
                    result2.setPixel(i, j, Color.TRANSPARENT);
                }
            }
        }

        drawView = new Skill_SubView(this, result2,bitmap2);

        LinearLayout imagepos = (LinearLayout) findViewById(R.id.imagepos2);
        imagepos.addView(drawView);

        // ImageView img = (ImageView) findViewById(R.id.image);
        // img.setImageBitmap(addimage);

        ImageButton download = (ImageButton) findViewById(R.id.download);
        ImageButton done = (ImageButton) findViewById(R.id.done);

        download.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SelectEditImage();
                saveBitmaptoJpeg(surface_image, "DOWNLOAD", "Sticker1");
            }
        });

        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Handler hd = new Handler();

                SelectEditImage();
                Intent intent = new Intent(SkillActivity.this, Savemode.class);

                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                addimage.compress(Bitmap.CompressFormat.JPEG, 100, stream);
                byte[] bytes = stream.toByteArray();

                intent.putExtra("BMP", bytes);
                hd.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        finish();
                    }
                }, 100);
                startActivity(intent);

            }
        });
    }

    public void SelectEditImage()
    {
        if(drawView.modify_flag==3||drawView.modify_flag==2) {
            for (int i = 0; i < drawView.canvasBitmap_mask.getWidth(); i++) {
                for (int j = 0; j < drawView.canvasBitmap_mask.getHeight(); j++) {
                    int p = drawView.canvasBitmap_mask.getPixel(i, j);
                    int r = Color.red(p);
                    int g = Color.green(p);
                    int b = Color.blue(p);
                    if (r == 0 && g == 0 && b == 0) {
                        drawView.canvasBitmap_mask.setPixel(i, j, Color.TRANSPARENT);
                    }
                }
            }
        }
        surface_image=Bitmap.createScaledBitmap(drawView.canvasBitmap_mask, bitmap2.getWidth(), bitmap2.getHeight(), true);
        addimage=overlayMark(bitmap2,surface_image,0,0);
    }

    public class Skill_SubView extends SurfaceView implements SurfaceHolder.Callback {

        private MyThread thread;
        public Bitmap canvasBitmap_mask;
        public Bitmap canvasBitmap_temp;
        public Bitmap canvasBitmap_src_temp;
        public Bitmap canvasBitmap_src;
        private GestureDetector gestureDetector;
        public CPhoto m_photo;
        public Mat hsvimage;
        public Mat rgbimage;
        public Mat rgbimage_4;
        List<Mat> listMat=new ArrayList<Mat>(3);
        List<Mat> listMat2=new ArrayList<Mat>(4);

        int modify_flag=1;
        int modify_flag2=0;


        public Skill_SubView(Context context, Bitmap mask,Bitmap src) {

            super(context);
            SurfaceHolder holder = getHolder();
            holder.addCallback(this);
            thread = new MyThread(holder);
            canvasBitmap_temp = mask;
            canvasBitmap_src_temp=src;
            gestureDetector = new GestureDetector(context, new GestureListener());
            hsvimage=new Mat();
            rgbimage=new Mat();
            rgbimage_4=new Mat();
        }
        private class GestureListener extends GestureDetector.SimpleOnGestureListener{
            @Override
            public boolean onDown(MotionEvent e) {

                if(modify_flag==3) {
                    m_photo.init(canvasBitmap_mask);
                }
                else if(modify_flag==2) {
                    m_photo.autohist(canvasBitmap_mask);
                    canvasBitmap_mask=m_photo.histImage_bit;
                }
                return true;
            }
            @Override
            public boolean onDoubleTap(MotionEvent e) {
                return false;
            }

            @Override
            public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
                                   float velocityY)
            {
                return false;
            }

            @Override
            public boolean onScroll(MotionEvent e1, MotionEvent e2,
                                    float distanceX, float distanceY)
            {
                if(modify_flag==1) {

                    modify_flag2=1;
                    if (e1.getX() > e2.getX())
                        canvasBitmap_mask = brightdown(canvasBitmap_mask);
                    if (e1.getX() < e2.getX())
                        canvasBitmap_mask = brightup(canvasBitmap_mask);
                }
                else if(modify_flag==2) {
                    modify_flag2=2;
                    /*
                    if(e1.getX() > e2.getX()) {
                        if(modify_contrast==1) {

                            canvasBitmap_mask=canvasBitmap_refresh;
                            modify_contrast=0;
                            Log.d("test","down");
                        }
                     }
                     */
                    // else if (e1.getX() < e2.getX()) {
                    //  if(modify_contrast==0) {
                    //       canvasBitmap_mask=m_photo.histImage_bit;
                    //      modify_contrast=1;
                    Log.d("test", "up");
                    //    }
                    //  }
                }
                else if(modify_flag==3){
                    modify_flag2=3;
                    if(e1.getX() > e2.getX()) {
                        m_photo.S=brightdown_mat(m_photo.S);
                        listMat=m_photo.mat_merge(m_photo.H,m_photo.S,m_photo.V);
                        Core.merge(listMat,hsvimage);

                        Log.d("test","hsvimage_channels:"+hsvimage.channels());
                        Imgproc.cvtColor(hsvimage,rgbimage,Imgproc.COLOR_HSV2BGR);
                        Imgproc.cvtColor(rgbimage,rgbimage_4,Imgproc.COLOR_BGR2BGRA,4);
                        Log.d("test","rgbimage_4"+rgbimage_4.channels());

                        //  m_photo.RGB_photo(rgbimage);
                        // listMat.subList(0,3);
                        // listMat2=m_photo.mat_merge_4(m_photo.R,m_photo.G,m_photo.B,m_photo.A);
                        //Core.merge(listMat2,rgbimage_4);

                        Utils.matToBitmap(rgbimage_4,canvasBitmap_mask);
                       // canvasBitmap_mask=canvasBitmap_mask.copy(Bitmap.Config.ARGB_8888,true);
                        rgbimage.release();
                        rgbimage_4.release();
                        hsvimage.release();
                        Log.d("test","HSV_DOWN");
                    }
                    if (e1.getX() < e2.getX()) {
                        m_photo.S=brightup_mat(m_photo.S);
                        listMat=m_photo.mat_merge(m_photo.H,m_photo.S,m_photo.V);
                        Core.merge(listMat,hsvimage);

                        Log.d("test","hsvimage_channels:"+hsvimage.channels());
                        Imgproc.cvtColor(hsvimage,rgbimage,Imgproc.COLOR_HSV2BGR);
                        Imgproc.cvtColor(rgbimage,rgbimage_4,Imgproc.COLOR_BGR2BGRA,4);

                        //m_photo.RGB_photo(rgbimage);
                        // listMat.subList(0,3);
                        // listMat2=m_photo.mat_merge_4(m_photo.R,m_photo.G,m_photo.B,m_photo.A);
                        // Core.merge(listMat2,rgbimage_4);

                        Log.d("test","rgbimage_4"+rgbimage_4.channels());
                        Utils.matToBitmap(rgbimage_4,canvasBitmap_mask);
                      //  canvasBitmap_mask=canvasBitmap_mask.copy(Bitmap.Config.ARGB_8888,true);
                        rgbimage.release();
                        rgbimage_4.release();
                        hsvimage.release();
                        Log.d("test","HSV_UP");
                    }

                }
                //do whatever you want here
                return false;
            }
        }




        protected void onSizeChanged(int w, int h, int oldw, int oldh) {
            super.onSizeChanged(w, h, oldw, oldh);

            canvasBitmap_src=Bitmap.createScaledBitmap(canvasBitmap_src_temp, w, h, false);
            canvasBitmap_src_temp=null;
            canvasBitmap_mask = Bitmap.createScaledBitmap(canvasBitmap_temp, w, h, false);
            canvasBitmap_temp=null;
            //canvasBitmap_refresh = Bitmap.createScaledBitmap(canvasBitmap_refresh,w,h,false);
            m_photo=new CPhoto(canvasBitmap_mask);
            hsvimage.create(canvasBitmap_mask.getWidth(), canvasBitmap_mask.getHeight(), Imgcodecs.CV_LOAD_IMAGE_COLOR);
            rgbimage.create(canvasBitmap_mask.getWidth(), canvasBitmap_mask.getHeight(), Imgcodecs.CV_LOAD_IMAGE_COLOR);
        }

        public MyThread getThread() {
            return thread;
        }

        public void surfaceCreated(SurfaceHolder holder) {
            thread.setRunning(true);
            try{
                thread.start();
            }catch (Exception e){
                thread=null;
                thread=new MyThread(holder);
                thread.start();
            }
        }

        public void surfaceDestroyed(SurfaceHolder holder) {
            boolean retry = true;

            thread.setRunning(false);
            while (retry) {
                try {
                    thread.join();
                    hsvimage.release();
                    rgbimage.release();
                    retry = false;
                }  catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

        }

        public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

        }

        public boolean onTouchEvent(MotionEvent event) {
                return gestureDetector.onTouchEvent(event);
        }




        public class MyThread extends Thread {
            private boolean mRun = false;
            private SurfaceHolder mSurfaceHolder;

            private void doDraw(Canvas cv){

                if(canvasBitmap_mask!=null) {
                    //cv.drawBitmap(canvasBitmap_src,0,0,null);
                    cv.drawBitmap(canvasBitmap_mask, 0, 0, null);
                }
            }

            public MyThread(SurfaceHolder surfaceHolder) {
                mSurfaceHolder = surfaceHolder;
            }

            @Override
            public void run() {
                while (mRun) {
                   Canvas drawCanvas = null;
                    try {
                        synchronized (mSurfaceHolder) {
                            drawCanvas = mSurfaceHolder.lockCanvas(null);
                            doDraw(drawCanvas);
                        }
                    } finally {
                        if (drawCanvas != null) {
                            mSurfaceHolder.unlockCanvasAndPost(drawCanvas);
                        }
                    }

                }
            }

            public void setRunning(boolean b) {
                mRun = b;
            }
        }
    }



    public Bitmap contrastdown(Bitmap image) {
        Mat temp=new Mat();
        Mat temp2=new Mat();
        Mat temp3=new Mat();

        Utils.bitmapToMat(image,temp);
        Log.d("test","channel2:"+temp.channels());

        temp3.create(temp.size(),temp.type());
        Core.scaleAdd(temp,0.5,temp3,temp2);


        /*
        for (int i = 0; i < image.getWidth(); i++) {
            for (int j = 0; j < image.getHeight(); j++) {
                int p = image.getPixel(i, j);
                int r = Color.red(p);
                int g = Color.green(p);
                int b = Color.blue(p);
                r += 20;
                g += 20;
                b += 20;
                if (r > 255)
                    r = 255;
                if (g > 255)
                    g = 255;
                if (b > 255)
                    b = 255;
                image.setPixel(i, j, Color.argb(Color.alpha(p), r, g, b));
            }
        }
        */
        Utils.matToBitmap(temp2,image);
        temp2.release();
        temp.release();
        temp3.release();
        return image;
    }
    public Bitmap contrastup(Bitmap image) {
        Mat temp=new Mat();
        Mat temp2=new Mat();
        Mat temp3=new Mat();
        Utils.bitmapToMat(image,temp);
        Log.d("test","channel2:"+temp.channels());

        temp3.create(temp.size(),temp.type());
        // Core.scaleAdd(temp,1.2,temp3,temp2);
        /*
        for (int i = 0; i < image.getWidth(); i++) {
            for (int j = 0; j < image.getHeight(); j++) {
                int p = image.getPixel(i, j);
                int r = Color.red(p);
                int g = Color.green(p);
                int b = Color.blue(p);
                r += 20;
                g += 20;
                b += 20;
                if (r > 255)
                    r = 255;
                if (g > 255)
                    g = 255;
                if (b > 255)
                    b = 255;
                image.setPixel(i, j, Color.argb(Color.alpha(p), r, g, b));
            }
        }
        */
        Utils.matToBitmap(temp2,image);
        temp2.release();
        temp.release();
        temp3.release();
        return image;
    }
    public Bitmap brightup(Bitmap image) {
        Mat temp=new Mat();
        Mat temp2=new Mat();

        Utils.bitmapToMat(image,temp);
        Log.d("test","channel:"+temp.channels());
        if(temp.channels()==4) {
            Core.add(temp, new Scalar(3, 3, 3, 0), temp2);
        }
        else if(temp.channels()==1){
            Core.add(temp, new Scalar(3), temp2);
        }
        Utils.matToBitmap(temp2,image);
        temp2.release();
        temp.release();
        return image;
    }
    public Bitmap brightdown(Bitmap image) {
        Mat temp=new Mat();
        Mat temp2=new Mat();

        Utils.bitmapToMat(image,temp);
        Log.d("test","channel2:"+temp.channels());
        if(temp.channels()==4) {
            Core.subtract(temp,new Scalar(3,3,3,0),temp2);
        }
        else if(temp.channels()==1){
            Core.subtract(temp,new Scalar(3),temp2);
        }

        Utils.matToBitmap(temp2,image);
        temp2.release();
        temp.release();
        return image;
    }
    public Mat down_mat(Mat image) {
        Mat temp=new Mat();
        Mat temp2=new Mat();
        Mat temp3=new Mat();
        temp=image;
        temp3.create(temp.size(),temp.type());
        Core.scaleAdd(temp,0.5,temp3,temp2);
        temp.release();
        temp3.release();
        return temp2;
    }
    public Mat contrastup_mat(Mat image) {
        Mat temp=new Mat();
        Mat temp2=new Mat();
        Mat temp3=new Mat();
        temp=image;
        temp3.create(temp.size(),temp.type());
        Core.scaleAdd(temp,1.2,temp3,temp2);
        temp.release();
        temp3.release();
        return temp2;
    }
    public Mat brightup_mat(Mat image) {
        Mat temp=new Mat();
        Mat temp2=new Mat();

        temp=image;
        Log.d("test","channel:"+temp.channels());
        if(temp.channels()==4) {
            Core.add(temp, new Scalar(3, 3, 3, 0), temp2);
        }
        else if(temp.channels()==1){
            Core.add(temp, new Scalar(1), temp2);
        }
        temp.release();
        return temp2;
    }
    public Mat brightdown_mat(Mat image) {
        Mat temp=new Mat();
        Mat temp2=new Mat();

        temp=image;
        Log.d("test","channel2:"+temp.channels());
        if(temp.channels()==4) {
            Core.subtract(temp,new Scalar(3,3,3,0),temp2);
        }
        else if(temp.channels()==1){
            Core.subtract(temp,new Scalar(1),temp2);
        }

        temp.release();
        return temp2;
    }
/*
/*
        @Override
        protected void onSizeChanged(int w, int h, int oldw, int oldh) {
            super.onSizeChanged(w, h, oldw, oldh);
            canvasBitmap_mask = Bitmap.createScaledBitmap(canvasBitmap_temp, w, h, false);
            canvasBitmap_src = Bitmap.createScaledBitmap(canvasBitmap_src, w, h, false);

            result = Bitmap.createBitmap(canvasBitmap_mask.getWidth(), canvasBitmap_mask.getHeight(), Bitmap.Config.ARGB_8888);

            drawCanvas = new Canvas(result);
            drawCanvas.drawBitmap(canvasBitmap_src, 0, 0, null);
            Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
            paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DARKEN));
            drawCanvas.drawBitmap(canvasBitmap_mask, 0, 0, paint);
            paint.setXfermode(null);
            result2 = result.copy(Bitmap.Config.ARGB_8888, true);

            for (int i = 0; i < result2.getWidth(); i++) {
                for (int j = 0; j < result2.getHeight(); j++) {
                    int p = result2.getPixel(i, j);
                    int r = Color.red(p);
                    int g = Color.green(p);
                    int b = Color.blue(p);
                    if (r == 0 && g == 0 && b == 0) {
                        result2.setPixel(i, j, Color.TRANSPARENT);
                    }
                 //밝기 100증가 테스트
                else{
                    r+=100;
                    b+=100;
                    g+=100;
                    if(r>255)
                        r=255;
                    if(g>255)
                        g=255;
                    if(b>255)
                        b=255;
                    result2.setPixel(i,j,Color.argb(Color.alpha(p),r,g,b));
                }


                }
            }

            drawCanvas_real=new Canvas(result2);

        }
*/

    // protected void onDraw(Canvas canvas) {
    //      canvas.drawBitmap(result2, 0, 0, null);
    //  }

    /*
    public boolean onTouchEvent(MotionEvent event) {
        float touchX = event.getX();
        float touchY = event.getY();


        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                break;
            case MotionEvent.ACTION_MOVE:
                result2 = brightup(result2);
                drawCanvas_real.drawBitmap(result2, 0, 0, null);
                break;
            case MotionEvent.ACTION_UP:
                addimage = overlayMark(canvasBitmap_src, result2, 0, 0);
                drawCanvas_real.drawBitmap(addimage, 0, 0, null);
                break;
            default:
                return false;
        }

        invalidate();
        return true;
    }

*/





    public void shareBitmaptoPng(){

        String ex_storage = Environment.getExternalStorageDirectory().getAbsolutePath();
        String folder_name = "/DOWNLOAD/";
        String file_name = folder_name + "Image1.png";
        String string_path = ex_storage+ file_name;
        File imageFileToShare = new File(string_path);
        Uri uri = Uri.fromFile(imageFileToShare);
        //Toast.makeText(getBaseContext(), uri.toString(), Toast.LENGTH_SHORT).show();


        Intent shareIntent = new Intent();
        shareIntent.setAction(Intent.ACTION_SEND);
        shareIntent.putExtra(Intent.EXTRA_STREAM, uri);
        shareIntent.setType("image/png");
        startActivity(Intent.createChooser(shareIntent, "공유하실 앱을 선택하세요"));
    }

    public void saveBitmaptoJpeg(Bitmap bitmap,String folder, String name){
        SimpleDateFormat dateFormat = new SimpleDateFormat( "yyyyMMdd_HHmmss");
        String todayDate = dateFormat.format(new Date());
        imgname = todayDate;
        String ex_storage = Environment.getExternalStorageDirectory().getAbsolutePath();
        String folder_name = "/DOWNLOAD/";
        String file_name = todayDate+".png";
        String string_path = ex_storage+ folder_name;
        File file_path;

        try {
            file_path = new File(string_path);
            if(!file_path.isDirectory()){
                file_path.mkdirs();
            }
            FileOutputStream out = new FileOutputStream(string_path+file_name);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, out);
            out.close();

            SkillActivity.this.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.parse(string_path + file_name)));
            MediaScannerConnection.scanFile(SkillActivity.this, new String[]{string_path + file_name}, null, null);

            String status = Environment.getExternalStorageState();
            if (status.equals(Environment.MEDIA_MOUNTED))
                Toast.makeText(getBaseContext(), file_name + "이 저장되었습니다.", Toast.LENGTH_SHORT).show();

        }catch(FileNotFoundException exception){
            Log.e("FileNotFoundException", exception.getMessage());
        }catch(IOException exception){
            Log.e("IOException", exception.getMessage());
        }
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_skill, menu);
        return super.onCreateOptionsMenu(menu);
    }
    protected void onPause(){
        bitmap=null;
        bitmap2=null;
        drawView.canvasBitmap_mask=null;
        drawView.canvasBitmap_src=null;
        drawView.canvasBitmap_src_temp=null;
        drawView.canvasBitmap_temp=null;
        drawView.thread.setRunning(false);
        super.onPause();
    }

    @Override
    protected void onDestroy(){
        drawView=null;
        super.onDestroy();
    }
    protected void onSaveInstanceState(Bundle outState){
        super.onSaveInstanceState(outState);
    }



    private void setCustomActionbar() {
        android.support.v7.app.ActionBar actionBar = getSupportActionBar();

        actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(false);
        actionBar.setDisplayShowTitleEnabled(false);

        View mCustomView = LayoutInflater.from(this).inflate(R.layout.bottombar_main, null);
        actionBar.setCustomView(mCustomView);
        Toolbar parent = (Toolbar) mCustomView.getParent();
        parent.setContentInsetsAbsolute(0, 0);
        actionBar.setBackgroundDrawable(getResources().getDrawable(R.drawable.skill_bar));

        android.support.v7.app.ActionBar.LayoutParams params = new android.support.v7.app.ActionBar.LayoutParams(android.app.ActionBar.LayoutParams.MATCH_PARENT, android.app.ActionBar.LayoutParams.MATCH_PARENT);
        actionBar.setCustomView(mCustomView, params);

    }
}
