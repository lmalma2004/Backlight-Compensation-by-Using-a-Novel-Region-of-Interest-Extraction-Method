#include "Color.h"


CColor::CColor(void)
{
}

CColor::~CColor(void)
{
}
Mat CColor::Progress(Mat_<uchar> H,Mat_<uchar> S){
	m_color.create(H.rows,H.cols,CV_8U);

	for(int y=0;y<H.rows;y++)
		for(int x=0;x<H.cols;x++){
			m_color.at<uchar>(y,x)=saturate_cast<uchar>((H.at<uchar>(y,x))+(S.at<uchar>(y,x)*0.2));
		}

	th=threshold(m_color,m_color_binary,100,3,THRESH_BINARY+THRESH_OTSU);
	return m_color_binary;
}