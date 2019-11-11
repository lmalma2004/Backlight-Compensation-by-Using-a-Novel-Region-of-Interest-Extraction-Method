#pragma once
#include "Define.h"

class CSkill
{
public:
	CSkill(void);
	~CSkill(void);

public:
	double contrast; //simple contrast control
	int brightress; // simple brightress control
	IplImage *in;//cvFloodFill을 사용하기 위한 변수
	int mode;
	int method;
	vector<vector<Point>> contours;

	Mat texture_pixel; 
	Mat skin_pixel; 
	Mat color_prominency_pixel;
	Mat binary_pixel;
	Mat luminance_pixel;
	Mat saliency_pixel;
	Mat saliency_pixel_gray;
	Mat new_Average_intensity;
	Mat new_luminance_pixel;
	Mat_<uchar> new_luminance_pixel_;
	double average_intensity[3];
	double m_Ym;
	double m_Op;
public:
	Mat_<uchar> Contrast(Mat_<uchar> Image,double contrast);
	Mat_<uchar> Brightress(Mat_<uchar> Image,int brightress);
	void FloodFill_photo(Mat Edges,int cols,int rows);
	void DrawContours_photo(Mat Edges);
	IplImage* MaskImage(IplImage *original,char *name);
	void Saliency_Image(Mat Image,Mat grayImage,Mat_<uchar> H,Mat_<uchar> S,Mat_<uchar> V);
	void Average_intensity(Mat_<uchar> H,Mat_<uchar> S,Mat_<uchar> V);
	void New_Average_intensity();
	void save_photo(Mat Image,char *name);
	Mat_<uchar> Offset();
};

