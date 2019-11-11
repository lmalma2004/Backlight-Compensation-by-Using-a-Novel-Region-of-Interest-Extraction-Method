package com.example.sungshin.myapplication;

import android.app.ActionBar;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import org.opencv.android.Utils;
import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint;
import org.opencv.core.Point;
import org.opencv.core.Scalar;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by JM on 2016-05-25.
 */
public class Selectmode extends AppCompatActivity {

    static {

        System.loadLibrary("opencv_java3");

    }

    Mat edged_image;
    Mat m_test;
    Bitmap bitmap2 = null; //넘어가는값은 마스크이미지
    Bitmap bitmap3=null; //넘어가는값은 원본이미지
    String uri2 = null;
    private SubView drawView;
    private ImageButton currPaint;
   // public Mat Lasso_image;
    //public Mat Lasso_image_color;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.select_main);
        setCustomActionbar();
        Intent intent = getIntent();
        byte[] bytes = intent.getByteArrayExtra("BMP");
        bitmap2 = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
        bitmap3=bitmap2;
        drawView = (SubView) new SubView(this,bitmap2);


        LinearLayout imagepos=(LinearLayout)findViewById(R.id.imagepos);
        LinearLayout paintLayout = (LinearLayout) findViewById(R.id.paint_color);

        imagepos.addView(drawView);

        currPaint = (ImageButton) paintLayout.getChildAt(0);
        Mat drawImage=new Mat();
        edged_image=new Mat();
        m_test=new Mat();
        Bitmap drawBitmap=null;
      //  try {
            Utils.bitmapToMat(bitmap2,drawImage);
            //drawImage = Utils.loadResource(this, R.drawable.lena, Imgcodecs.CV_LOAD_IMAGE_COLOR);
            drawBitmap = Bitmap.createBitmap(drawImage.cols(), drawImage.rows(), Bitmap.Config.ARGB_8888);
            Utils.matToBitmap(drawImage, drawBitmap);

      //  } catch (IOException e) {
      //      e.printStackTrace();
      //  }

        /*
        Toast.makeText(getBaseContext(), "편집할 부분을 색칠해주세요.", Toast.LENGTH_SHORT).show();
        ImageButton erase = (ImageButton) findViewById(R.id.erase);
        ImageButton done = (ImageButton) findViewById(R.id.done);
        erase.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getBaseContext(), "색칠을 지워줄게요.", Toast.LENGTH_SHORT).show();
            }
        });
        */
        ImageButton done = (ImageButton) findViewById(R.id.done);
        ImageButton refresh=(ImageButton)findViewById(R.id.rotate1);
        refresh.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                drawView.drawCanvas.drawColor(Color.BLACK);
                drawView.drawEdge.drawColor(Color.BLACK);
                drawView.drawCanvas.drawBitmap(drawView.canvasBitmap_refresh,0,0,null);
                drawView.invalidate();
            }


        });
        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               List<MatOfPoint> contours=new ArrayList<MatOfPoint>();
                Mat hierarchy=new Mat();

                bitmap2=drawView.canvasBitmap_lasso;

              //  Utils.bitmapToMat(bitmap2,edged_image);//
             //   Imgproc.Canny(edged_image,m_test,1,10);//
            //    Imgproc.findContours(m_test, contours, hierarchy, Imgproc.RETR_EXTERNAL,Imgproc.CHAIN_APPROX_SIMPLE);//
              //  for (int contourIdx = 0; contourIdx < contours.size(); contourIdx++) {
               //     Imgproc.drawContours(m_test, contours, -1, new Scalar(255,255, 255), 1);//
              //  }
                //
             //   Imgproc.floodFill(m_test,m_test,new Point(50,50),new Scalar(255));
             //   Utils.matToBitmap(m_test,bitmap2);
              //  hierarchy.release();

                Intent intent = new Intent(Selectmode.this, SkillActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                ByteArrayOutputStream stream2= new ByteArrayOutputStream();
                bitmap2.compress(Bitmap.CompressFormat.JPEG, 100, stream);
                bitmap3.compress(Bitmap.CompressFormat.JPEG,100,stream2);
                byte[] bytes2=stream2.toByteArray();
                byte[] bytes = stream.toByteArray();

                intent.putExtra("BMP2",bytes2);
                intent.putExtra("BMP", bytes);
                startActivity(intent);
            }
        });


        //ImageView img = (ImageView)findViewById(R.id.drawing);
        //img.setImageBitmap(bitmap2);
    }



    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_select, menu);
        return super.onCreateOptionsMenu(menu);
    }


    public void clicked(View view) {
        if (view != currPaint) {
            String color = view.getTag().toString();
            drawView.setColor(color);
            currPaint = (ImageButton) view;
        }
    }

    private void setCustomActionbar() {
        android.support.v7.app.ActionBar actionBar = getSupportActionBar();

        actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(false);
        actionBar.setDisplayShowTitleEnabled(false);

        View mCustomView = LayoutInflater.from(this).inflate(R.layout.selectbar_main, null);
        actionBar.setCustomView(mCustomView);
        Toolbar parent = (Toolbar) mCustomView.getParent();
        parent.setContentInsetsAbsolute(0, 0);
        actionBar.setBackgroundDrawable(getResources().getDrawable(R.drawable.main));

        android.support.v7.app.ActionBar.LayoutParams params = new android.support.v7.app.ActionBar.LayoutParams(ActionBar.LayoutParams.MATCH_PARENT, ActionBar.LayoutParams.MATCH_PARENT);
        actionBar.setCustomView(mCustomView, params);
    }
    protected void onPause(){
        super.onPause();
       // drawView=null;
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    public class SubView extends View{

        public Mat Roi;

        public Mat dstImage; //grayscale
        public Mat srcImage; //color
        public Mat edge;

        private Paint paint =new Paint();
        private Paint paint2 =new Paint();
        private Paint paint3=new Paint();
        private Path path=new Path();
        private int paintColor =0x5500FF00;
        private int paintColor2=0xFF000000;
        private int paintColor3=0xFFFFFFFF;
        private Paint canvasPaint;
        private Canvas drawCanvas;
        private Canvas drawEdge;

        private Bitmap canvasBitmap_lasso;
        private Bitmap canvasBitmap_lasso2;
        private Bitmap canvasBitmap_real; //선 그려지는 비트맵
        private Bitmap canvasBitmap_refresh;
        private Bitmap canvasBitmap; //Bitmap.createScaledBitmap을 수행한 원본 이미지 (좌표 조절됨)
        private Bitmap canvasBitmap_calc;//Bitmap.createScaledBitmap을 수행한 엣지 이미지 (이 이미지를 가지고 Magneticlasso 수행)
        private Bitmap canvasBitmap_temp; //원본 이미지
        private Bitmap canvasBitmap_test; // Bitmap.createBitmap(srcImage.width(),srcImage.height(),Bitmap.Config.ARGB_8888) 포맷의 원본 이미지
        private Bitmap canvasBitmap_edge; //Bitmap.createScaledBitmap을 수행전 엣지 이미지
        public  MagenticLasso lasso;

        List<MatOfPoint> contours=new ArrayList<MatOfPoint>();
        Mat hierarchy;

        public SubView(Context context,Bitmap bitmapimage){
            super(context);
            paint.setAntiAlias(true); //곡선을 부드럽게 처리
            paint.setStrokeWidth(5f); //펜 굵기
            paint.setColor(paintColor);
            paint.setStyle(Paint.Style.FILL);
            //paint.setStyle(Paint.Style.STROKE); //채우지는 않고 외곽선만 그린다
            paint.setStrokeJoin(Paint.Join.ROUND); //모서리를 둥근형태로

            paint2.setAntiAlias(true); //곡선을 부드럽게 처리
            paint2.setStrokeJoin(Paint.Join.ROUND); //모서리를 둥근형태로
            paint2.setStyle(Paint.Style.STROKE); //채우지는 않고 외곽선만 그린다
            paint2.setStrokeWidth(10f); //펜 굵기
            paint2.setColor(paintColor2);

            paint3.setAntiAlias(true); //곡선을 부드럽게 처리
            paint3.setStrokeWidth(5f); //펜 굵기
            paint3.setColor(paintColor3);
            paint3.setStyle(Paint.Style.FILL);
            //paint.setStyle(Paint.Style.STROKE); //채우지는 않고 외곽선만 그린다
            paint3.setStrokeJoin(Paint.Join.ROUND); //모서리를 둥근형태로

            lasso=new MagenticLasso();

            lasso.WeightPixel=new Mat();
            lasso.dstGx=new Mat();
            lasso.dstGy=new Mat();
            lasso.dstGx_abs=new Mat();
            lasso.dstGy_abs=new Mat();
            lasso.dstImageGx=new Mat();
            lasso.dstImageGy=new Mat();
            lasso.dstMag=new Mat();
            lasso.dstImageGxy=new Mat();

            hierarchy=new Mat();
            dstImage=new Mat();
            srcImage=new Mat();
            edge=new Mat();
          //  Lasso_image=new Mat();
          //  Lasso_image_color=new Mat();
            Roi=new Mat();
            Roi.create(3,3, CvType.CV_8U);

            canvasBitmap_temp=bitmapimage;


        }
        @Override
        protected void onSizeChanged(int w,int h,int oldw,int oldh){
            super.onSizeChanged(w, h, oldw, oldh);
           // canvasBitmap_temp= BitmapFactory.decodeResource(getResources(), R.drawable.lena);

            srcImage.create(canvasBitmap_temp.getWidth(), canvasBitmap_temp.getHeight(), Imgcodecs.CV_LOAD_IMAGE_COLOR);
          //  Lasso_image.create(canvasBitmap_temp.getWidth(),canvasBitmap_temp.getHeight(),Imgcodecs.CV_LOAD_IMAGE_COLOR);
            //dstImage.create(canvasBitmap_temp.getWidth(), canvasBitmap_temp.getHeight(), Imgcodecs.CV_LOAD_IMAGE_GRAYSCALE);
            Utils.bitmapToMat(canvasBitmap_temp, srcImage);
            Imgproc.cvtColor(srcImage,dstImage,Imgproc.COLOR_BGR2GRAY);
            // Utils.bitmapToMat(canvasBitmap_temp, dstImage);

            Imgproc.Canny(srcImage, edge,20,120);
            Imgproc.findContours(edge, contours, hierarchy, Imgproc.RETR_EXTERNAL,Imgproc.CHAIN_APPROX_SIMPLE);
            /*
            for (int contourIdx = 0; contourIdx < contours.size(); contourIdx++) {
                Imgproc.drawContours(srcImage, contours, contourIdx, new Scalar(0, 0, 255), -1);
            }
            */
            //  canvasBitmap_edge=Bitmap.createBitmap(edge.width(),edge.height(),Bitmap.Config.ARGB_8888);
            //  Utils.matToBitmap(edge,canvasBitmap_edge);

            //lasso.WeightPixel=new Mat();
           // lasso.WeightPixel.create(edge.rows(), edge.cols(), CvType.CV_8U);


            // lasso.dstGx=new Mat();
            //lasso.dstGy=new Mat();

            Imgproc.Sobel(dstImage, lasso.dstGx,dstImage.depth(), 1, 0,lasso.ksize,1.0,0.0,4);
            Imgproc.Sobel(dstImage, lasso.dstGy,dstImage.depth(), 0, 1,lasso.ksize,1.0,0.0,4);

            Core.convertScaleAbs(lasso.dstGx,lasso.dstGx_abs);
            Core.convertScaleAbs(lasso.dstGy,lasso.dstGy_abs);

            // Core.absdiff(lasso.dstGx, Scalar.all(0), lasso.dstGx);
            // Core.absdiff(lasso.dstGy, Scalar.all(0), lasso.dstGy);

            //lasso.dstImageGx=new Mat();
            // lasso.dstImageGy=new Mat();
            Core.normalize(lasso.dstGx_abs, lasso.dstImageGx, 0, 255, Core.NORM_MINMAX, lasso.dtype);
            Core.normalize(lasso.dstGy_abs, lasso.dstImageGy, 0, 255, Core.NORM_MINMAX, lasso.dtype);

            //lasso.dstMag=new Mat();
            //lasso.dstMag.create(lasso.dstGx.size(),lasso.dstGx.type());

            lasso.dstGx.convertTo(lasso.dstGx,CvType.CV_32F);
            lasso.dstGy.convertTo(lasso.dstGy,CvType.CV_32F);
            Core.magnitude(lasso.dstGx, lasso.dstGy, lasso.dstMag);//

            // lasso.dstImageGx.release();
            // lasso.dstImageGy.release();
            // lasso.dstGx.release();
            // lasso.dstGy.release();

            //lasso.dstImageGxy=new Mat();
            Core.normalize(lasso.dstMag, lasso.dstImageGxy, 0, 255, Core.NORM_MINMAX, lasso.dtype);//

            //lasso.dstMag.release();

          //  Lasso_image.create(lasso.dstImageGxy.rows(), lasso.dstImageGxy.cols(), CvType.CV_8U);
          //  Lasso_image_color=srcImage.clone();

            //  WeightPixel=new Mat();
            //  lasso.WeightPixel=lasso.Weight_Image(lasso.dstImageGxy,edge);
            canvasBitmap_edge=Bitmap.createBitmap(edge.width(),edge.height(),Bitmap.Config.ARGB_8888);
            Utils.matToBitmap(edge,canvasBitmap_edge);


            //canvasBitmap_lasso2=Bitmap.createBitmap(srcImage.width(),srcImage.height(),Bitmap.Config.ARGB_8888);
            canvasBitmap_lasso=Bitmap.createBitmap(srcImage.width(),srcImage.height(),Bitmap.Config.ARGB_8888);
            canvasBitmap_test=Bitmap.createBitmap(srcImage.width(),srcImage.height(),Bitmap.Config.ARGB_8888);
            Utils.matToBitmap(srcImage, canvasBitmap_test);

            canvasBitmap=Bitmap.createScaledBitmap(canvasBitmap_test, w, h, false);
            canvasBitmap_calc=Bitmap.createScaledBitmap(canvasBitmap_edge,w,h,false);
            canvasBitmap_lasso=Bitmap.createScaledBitmap(canvasBitmap_lasso,w,h,false);
           // canvasBitmap_lasso2=Bitmap.createScaledBitmap(canvasBitmap_lasso,w,h,false);
            Utils.bitmapToMat(canvasBitmap_calc,edge);

            canvasBitmap_real=canvasBitmap.copy(Bitmap.Config.ARGB_8888, true);
            canvasBitmap_refresh=canvasBitmap.copy(Bitmap.Config.ARGB_8888, true);
            drawEdge=new Canvas(canvasBitmap_lasso);
            drawCanvas=new Canvas( canvasBitmap_real);
            canvasPaint = new Paint(Paint.DITHER_FLAG);
        }
        @Override
        protected void onDraw(Canvas canvas){
            canvas.drawBitmap(canvasBitmap_real, 0, 0, canvasPaint);
            canvas.drawPath(path, paint);
        }
        @Override
        public boolean onTouchEvent(MotionEvent event){
            //detect user touch
            double minVal2;
            double maxVal2;
            Point minLoc2;
            Point maxLoc2;
            Point drawLoc=new Point();

            Core.MinMaxLocResult LocRes;
            float touchX=event.getX();
            float touchY=event.getY();
            if (touchX >0  && touchY >0  && touchY < edge.rows() - 1 && touchX < edge.cols() - 1) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:

                       // if (touchX > 0 && touchY > 0 && touchY < edge.rows() - 1 && touchX < edge.cols() - 1) {
                            path.moveTo(touchX, touchY);
                       // }
                        break;
                    case MotionEvent.ACTION_MOVE:

                        //if (touchX > 0 && touchY > 0 && touchY < edge.rows() - 1 && touchX < edge.cols() - 1) {
                            Roi = lasso.setRoi(edge, Roi, (int) touchY, (int) touchX);
                            LocRes = Core.minMaxLoc(Roi);
                            maxVal2 = LocRes.maxVal;
                            minVal2 = LocRes.minVal;
                            minLoc2 = LocRes.minLoc;
                            maxLoc2 = LocRes.maxLoc;

                            //Log.d("test","maxLoc.x="+maxLoc2.x+"maxLoc.y"+maxLoc2.y+maxLoc2);

                            if (touchX != 0 && touchY != 0 && touchY != edge.rows() - 1 && touchX != edge.cols() - 1) {
                                if (maxLoc2.x == 1 && maxLoc2.y == 1)
                                    drawLoc = new Point(touchX, touchY);
                                else if (maxLoc2.x == 0 && maxLoc2.y == 1)
                                    drawLoc = new Point(touchX - 1, touchY);
                                else if (maxLoc2.x == 2 && maxLoc2.y == 1)
                                    drawLoc = new Point(touchX + 1, touchY);
                                else if (maxLoc2.x == 1 && maxLoc2.y == 2)
                                    drawLoc = new Point(touchX, touchY + 1);
                                else if (maxLoc2.x == 0 && maxLoc2.y == 2)
                                    drawLoc = new Point(touchX - 1, touchY + 1);
                                else if (maxLoc2.x == 2 && maxLoc2.y == 2)
                                    drawLoc = new Point(touchX + 1, touchY + 1);
                                else if (maxLoc2.x == 1 && maxLoc2.y == 0)
                                    drawLoc = new Point(touchX, touchY - 1);
                                else if (maxLoc2.x == 0 && maxLoc2.y == 0)
                                    drawLoc = new Point(touchX - 1, touchY - 1);
                                else if (maxLoc2.x == 2 && maxLoc2.y == 0)
                                    drawLoc = new Point(touchX + 1, touchY - 1);
                            }
                            // Log.d("test","Mat_edge:"+ edge.get((int)drawLoc.x,(int)drawLoc.y)[0]);
                            // Log.d("test","color.red:"+ Color.green(canvasBitmap_edge.getPixel((int)drawLoc.x,(int)drawLoc.y)));
                            // Log.d("test",/*"realx:"+touchX+"realy:"+touchY+*/"bitmap_Weightpixel:"+canvasBitmap_edge.getPixel((int)drawLoc.x,(int)drawLoc.y));
                            // Log.d("test","drawLoc.x="+drawLoc.x+"drawLoc.y="+drawLoc.y+"lasso_weightpixel_value="+lasso.WeightPixel.get((int)touchX,(int)touchY)[0]);

                            if ((Color.red(canvasBitmap_calc.getPixel((int) drawLoc.x, (int) drawLoc.y))) > 20) {
                                path.lineTo((int) drawLoc.x, (int) drawLoc.y);
                                drawCanvas.drawCircle((float) drawLoc.x, (float) drawLoc.y, 2, paint2);
                                //drawEdge.drawCircle((float) drawLoc.x, (float) drawLoc.y, 2, paint2);
                            }
                       // }
                        break;
                    case MotionEvent.ACTION_UP:
                        //if (touchX > 0 && touchY > 0 && touchY < edge.rows() - 1 && touchX < edge.cols() - 1) {
                            drawCanvas.drawPath(path, paint);
                            drawEdge.drawPath(path, paint3);
                            path.reset();
                       // }
                        break;
                    default:
                        return false;
                }
            }
            invalidate();
            return true;
        }
        public void setColor(String newColor){
            invalidate();
            paintColor= Color.parseColor(newColor);
            paint.setColor(paintColor);
        }


/*
        @Override
        protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
            // height 진짜 크기 구하기

            int heightMode = MeasureSpec.getMode(heightMeasureSpec);
            int heightSize = 0;
            switch(heightMode) {
                case MeasureSpec.UNSPECIFIED:    // mode 가 셋팅되지 않은 크기가 넘어올때
                    heightSize = heightMeasureSpec;
                    break;
                case MeasureSpec.AT_MOST:        // wrap_content (뷰 내부의 크기에 따라 크기가 달라짐)
                    heightSize = 600;
                    break;
                case MeasureSpec.EXACTLY:        // fill_parent, match_parent (외부에서 이미 크기가 지정되었음)
                    heightSize = MeasureSpec.getSize(heightMeasureSpec);
                    break;
            }

            // width 진짜 크기 구하기
            int widthMode = MeasureSpec.getMode(widthMeasureSpec);
            int widthSize = 0;
            switch(widthMode) {
                case MeasureSpec.UNSPECIFIED:    // mode 가 셋팅되지 않은 크기가 넘어올때
                    widthSize = widthMeasureSpec;
                    break;
                case MeasureSpec.AT_MOST:        // wrap_content (뷰 내부의 크기에 따라 크기가 달라짐)
                    widthSize = 600;
                    break;
                case MeasureSpec.EXACTLY:        // fill_parent, match_parent (외부에서 이미 크기가 지정되었음)
                    widthSize = MeasureSpec.getSize(widthMeasureSpec);
                    break;
            }
            setMeasuredDimension(widthSize, heightSize);

        }
*/

    }


}

