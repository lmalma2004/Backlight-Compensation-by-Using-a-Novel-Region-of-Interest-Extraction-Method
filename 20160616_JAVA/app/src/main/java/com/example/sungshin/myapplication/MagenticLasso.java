package com.example.sungshin.myapplication;

import android.util.Log;

import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.Point;

/**
 * Created by JM on 2016-06-01.
 */
public class MagenticLasso {

    public double minVal;
    public double maxVal;
    public Point minLoc;
    public Point maxLoc;
    public double gradient;
    public int ksize;
    public int ddepth;
    public int dtype;
    Core.MinMaxLocResult LocRes;
    public Mat WeightPixel;
    public Mat dstGx,dstGy;   //3채널
    public Mat dstGx_abs,dstGy_abs;
    public Mat dstMag; //G=Ix^2+Iy^2의 제곱근 행렬
    public Mat dstImageGx;
    public Mat dstImageGy;
    public Mat dstImageGxy;
    public Mat dstImageGxy_;
    public Mat Segment_image;

    public MagenticLasso(){
        ksize=3;
        ddepth= CvType.CV_32F;
        dtype= CvType.CV_8U;
        /*
        WeightPixel=new Mat();
        dstGx=new Mat();
        dstGy=new Mat();
        dstMag=new Mat();
        dstImageGx=new Mat();
        dstImageGy=new Mat();
        dstImageGxy=new Mat();
        dstImageGxy_=new Mat();
        Segment_image=new Mat();
        */
    }

    public Mat setRoi(Mat Image, Mat Roi, int y, int x){

        double data;

        data=Image.get(y,x)[0];
        Roi.put(1,1,data);
        data=Image.get(y-1,x)[0];
        Roi.put(0,1,data);
        data=Image.get(y+1,x)[0];
        Roi.put(2,1,data);
        data=Image.get(y,x+1)[0];
        Roi.put(1,2,data);
        data=Image.get(y-1,x-1)[0];
        Roi.put(0,2,data);
        data=Image.get(y+1,x+1)[0];
        Roi.put(2,2,data);
        data=Image.get(y,x-1)[0];
        Roi.put(1,0,data);
        data=Image.get(y-1,x-1)[0];
        Roi.put(0,0,data);
        data=Image.get(y+1,x-1)[0];
        Roi.put(2,0,data);

        return Roi;

    }
    public Mat Weight_Image(Mat Image, Mat edge){
        Mat Roi;
        Mat output;
        Roi=new Mat();
        output =new Mat();
        int size=(int) (Image.total()*Image.channels());

        double data;

      //  byte buff[]=new byte[size];
      //  Image.get(0,0,buff);

        Roi.create(3,3, CvType.CV_8U);

        output.create(edge.rows(),edge.cols(), CvType.CV_8U);

        for(int y=0;y<Image.rows();y++) //열
            for(int x=0;x<Image.cols();x++){ //행
                if(x!=0 && y!=0 && x!=Image.cols()-1 && y!=Image.rows()-1){

                    Roi=setRoi(Image,Roi,y,x);
                    /*
                    data=Image.get(y,x)[0];
                    Roi.put(1,1,data);
                    data=Image.get(y-1,x)[0];
                    Roi.put(0,1,data);
                    data=Image.get(y+1,x)[0];
                    Roi.put(2,1,data);
                    data=Image.get(y,x+1)[0];
                    Roi.put(1,2,data);
                    data=Image.get(y-1,x-1)[0];
                    Roi.put(0,2,data);
                    data=Image.get(y+1,x+1)[0];
                    Roi.put(2,2,data);
                    data=Image.get(y,x-1)[0];
                    Roi.put(1,0,data);
                    data=Image.get(y-1,x-1)[0];
                    Roi.put(0,0,data);
                    data=Image.get(y+1,x-1)[0];
                    Roi.put(2,0,data);
                    //Roi(2,0)=Image(y+1,x-1);
                    */

                }
                LocRes= Core.minMaxLoc(Roi);
                maxVal=LocRes.maxVal;
                minVal=LocRes.minVal;

                data=Image.get(y,x)[0];
                gradient=1-(data/maxVal);
                output.put(y,x,0.5*edge.get(y,x)[0]+0.5*gradient);
                Log.d("test","gradient:"+gradient+"output:"+output.get(y,x)[0]);
               // output.at<uchar>(y,x)=saturate_cast<uchar>(0.5*edge.at<uchar>(y,x)+0.5*gradient); //가중치는 0.5를 줌
            }


        return output;

    }
}
