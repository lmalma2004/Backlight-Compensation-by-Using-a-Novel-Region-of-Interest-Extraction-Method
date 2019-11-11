#include "Binary.h"


CBinary::CBinary(void)
{
}


CBinary::~CBinary(void)
{
}

Mat CBinary::Progress(Mat grayImage){
	//Mat src=imread("lena.jpg",IMREAD_GRAYSCALE);
	th=threshold(grayImage,m_binaryImg,100,3,THRESH_BINARY+THRESH_OTSU);
	return m_binaryImg;
	imshow("binary",m_binaryImg);
	//waitKey(0);
}