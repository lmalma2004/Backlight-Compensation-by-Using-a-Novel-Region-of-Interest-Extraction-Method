#pragma once
#include "Define.h"
class CSkin
{
public:
	CSkin(void);
	~CSkin(void);
public:

	Mat m_skinImg;
	Mat m_skinImg_binary;
	double th;
public:
	//void Progress();
	Mat Progress(Mat Image); //파라미터 Image는 원본이미지
};

