#pragma once
#include "Define.h"

class CPhoto
{
public:
	CPhoto(Mat Image);
	CPhoto(void);
	~CPhoto(void);

public:
	IplImage *sampleImage;
	Mat srcImage;  
	Mat hsvImage;
	Mat grayImage;
	Mat hist_hsvImage_rgb; //hsv->rgbcvtColor(srcImage,hsvImage,COLOR_BGR2HSV); //rgb->hsv

	vector<Mat> HSVplanes_1; //hsv split 
	vector<Mat> HSVplanes_2; //hist평활 hsv
	vector<Mat> RGBplanes_1; //rgb split
	vector<Mat> RGBplanes_2; //hist평활 rgb
	vector<Mat> Test;

	Mat_<uchar> H;
	Mat_<uchar> S;
	Mat_<uchar> V;
	
	Mat H_hist;
	Mat S_hist;
	Mat V_hist;
	
	Mat_<uchar> R;
	Mat_<uchar> G;
	Mat_<uchar> B;
	
	Mat R_hist;
	Mat G_hist;
	Mat B_hist;
	
	Mat HSV_change;
	Mat RGB_change;
	Mat HSV_Backlight;
	Mat RGB_Backlight;

	IplImage *sampleEdges;
	Mat Edges;

	Mat mouseImage;
	Mat e_mouseImage;

	

public:
	void HSV_photo(Mat Image);
	void RGB_photo(Mat Image);
	Mat EDGES_photo(Mat Image);
	
	vector<Mat> ch_merge(Mat_<uchar> ch1,Mat_<uchar> ch2,Mat_<uchar> ch3); 

	void save_photo(Mat Image,char *name);
};

