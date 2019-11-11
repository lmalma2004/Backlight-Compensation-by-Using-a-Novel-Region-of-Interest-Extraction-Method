#pragma once
#include "Define.h"
class CBinary
{
public:
	CBinary(void);
	~CBinary(void);
public:
	Mat m_binaryImg;
	double th; //�Ӱ谪
public:
	Mat Progress(Mat grayImage);
};

