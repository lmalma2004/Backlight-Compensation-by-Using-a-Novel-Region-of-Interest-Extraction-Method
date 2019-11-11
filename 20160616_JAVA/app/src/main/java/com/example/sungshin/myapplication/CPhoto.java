package com.example.sungshin.myapplication;

import android.graphics.Bitmap;
import android.graphics.Color;

import org.opencv.android.Utils;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by JM on 2016-06-13.
 */
public class CPhoto {
    Mat hsvImage;
    Mat srcImage;

    Mat A;
    Mat H;
    Mat S;
    Mat V;
    Mat R;
    Mat G;
    Mat B;
    Mat H2;
    Mat S2;
    Mat V2;
    Mat H_hist;
    Mat S_hist;
    Mat V_hist;
    Bitmap srcImage_bit;
    Bitmap histImage_bit;




    public CPhoto(Bitmap bitmap){
        srcImage=new Mat();
        hsvImage=new Mat();
        A=new Mat();
        H=new Mat();
        S=new Mat();
        V=new Mat();
        H2=new Mat();
        S2=new Mat();
        V2=new Mat();
        H_hist=new Mat();
        S_hist=new Mat();
        V_hist=new Mat();
        Utils.bitmapToMat(bitmap,srcImage);
        Imgproc.cvtColor(srcImage,hsvImage,Imgproc.COLOR_BGR2HSV);
        HSV_photo(hsvImage);

    }
    public void init(Bitmap bitmap){
        srcImage_bit=bitmap;
        Utils.bitmapToMat(bitmap,srcImage);
        Imgproc.cvtColor(srcImage,hsvImage,Imgproc.COLOR_BGR2HSV);
        HSV_photo(hsvImage);
    }
    public void autohist(Bitmap image){
        init(image);
        List<Mat> listMat=new ArrayList<Mat>(3);
        Mat temphsv=new Mat();
        Mat temprgb=new Mat();
        Mat temprgba=new Mat();
        listMat = mat_merge(H,S,V_hist);
        Core.merge(listMat,temphsv);
        Imgproc.cvtColor(temphsv,temprgb, Imgproc.COLOR_HSV2BGR);
        Imgproc.cvtColor(temprgb, temprgba, Imgproc.COLOR_BGR2BGRA, 4);
        Utils.matToBitmap(temprgba,image);
        image=image.copy(Bitmap.Config.ARGB_8888, true);//
        //if(drawView.modify_flag2==3||drawView.modify_flag2==2) {
        for (int i = 0; i < image.getWidth(); i++) {
            for (int j = 0; j < image.getHeight(); j++) {
                int p = image.getPixel(i, j);
                int r = Color.red(p);
                int g = Color.green(p);
                int b = Color.blue(p);
                if (r == 0 && g == 0 && b == 0) {
                    image.setPixel(i, j, Color.TRANSPARENT);
                }
            }
        }
        histImage_bit=image;
        image=null;
    }
    public void HSV_photo(Mat Image){
        List<Mat> listMat=new ArrayList<Mat>(3);
        Core.split(Image,listMat);
        H=listMat.get(0);
        S=listMat.get(1);
        V=listMat.get(2);
        Imgproc.equalizeHist(H,H_hist);
        Imgproc.equalizeHist(S,S_hist);
        Imgproc.equalizeHist(V,V_hist);

    }
    public void RGB_photo(Mat Image){
        List<Mat> listMat=new ArrayList<Mat>(3);
        Core.split(Image,listMat);
        B=listMat.get(0);
        G=listMat.get(1);
        R=listMat.get(2);
        A.create(R.size(),R.type());
    }
    public List<Mat> ch_merge(Bitmap ch1,Bitmap ch2,Bitmap ch3){
        List<Mat> listMat2=new ArrayList<Mat>(3);
        Utils.bitmapToMat(ch1,H2);
        Utils.bitmapToMat(ch2,S2);
        Utils.bitmapToMat(ch3,V2);

        listMat2=mat_merge(H2,S2,V2);
        return listMat2;
    }
    public List<Mat> mat_merge(Mat ch1,Mat ch2,Mat ch3){
        List<Mat> listMat3=new ArrayList<Mat>(3);
        listMat3= Arrays.asList(ch1,ch2,ch3);
        return listMat3;
    }
    public List<Mat> mat_merge_4(Mat ch1,Mat ch2,Mat ch3,Mat ch4){
        List<Mat> listMat4=new ArrayList<Mat>(4);
        listMat4= Arrays.asList(ch1,ch2,ch3,ch4);
        return listMat4;
    }


}
