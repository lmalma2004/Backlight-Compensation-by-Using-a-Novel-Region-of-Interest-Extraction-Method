#pragma once
#include "Define.h"
class CColor
{
public:
	CColor(void);
	~CColor(void);
public:
	Mat m_color;
	Mat m_color_binary;
	double th;

public:
	Mat Progress(Mat_<uchar> H,Mat_<uchar> S);
};

