#pragma once
#include "Define.h"
#include "opencv.hpp"

class CTexture
{
public:
	CTexture(void);
	~CTexture(void);
public:
	Mat Progress(Mat grayImage); //파라미터 그레이이미지로
	Mat GaborFilter(Mat srcImage);
	Mat FeatureDetect(Mat srcImage);
	Mat KmeansClustering(Mat srcImage);
	void Divide_texture(Mat txtImage);
	void save_photo(Mat Image,char *name);
public:
	double th[3];
	Mat	m_gaborImg;
	Mat m_featImg;
	Mat m_txtImg;
	Mat m_gray_txtImg;

	Mat_<uchar> ch1_binary;
	Mat_<uchar> ch2_binary;
	Mat_<uchar> ch3_binary;

	Mat_<uchar> ch1;
	Mat_<uchar> ch2;
	Mat_<uchar> ch3;
	vector<Mat> m_txtImg_ch;
};

