#include "Skin.h"


CSkin::CSkin(void)
{
}


CSkin::~CSkin(void)
{
}

Mat CSkin:: Progress(Mat Image){
	blur(Image,Image,Size(3,3));
	Mat hsv;
	cvtColor(Image,hsv,CV_BGR2HSV);
	inRange(hsv,Scalar(0,10,60),Scalar(20,150,255),m_skinImg);
	th=threshold(m_skinImg,m_skinImg_binary,100,3,THRESH_BINARY+THRESH_OTSU);
	return m_skinImg_binary;
}
/*
void CSkin:: Progress(){
	Mat src=imread("lena.jpg");
	blur(src,src,Size(3,3));
	Mat hsv;
	cvtColor(src,hsv,CV_BGR2HSV);
	
	inRange(hsv,Scalar(0,10,60),Scalar(20,150,255),m_skinImg);
	//imshow("SkinImage",m_skinImg);
	//waitKey();
}
*/