#pragma once
#include "Define.h"

class CMagneticLasso
{
public:
	CMagneticLasso(void);
	~CMagneticLasso(void);

public:
	double minVal;
	double maxVal;
	Point minLoc;
	Point maxLoc;
	double gradient;
	int ksize;
	int ddepth;
	int dtype;

	Mat WeightPixel;
	Mat dstGx,dstGy;   //3채널
	Mat dstMag; //G=Ix^2+Iy^2의 제곱근 행렬
	Mat dstImageGx;   
	Mat dstImageGy;  
	Mat dstImageGxy;
	Mat_<uchar> dstImageGxy_;
	Mat Segment_image;

public:
	Mat Weight_Image(Mat_<uchar> Image,Mat edge);
};

