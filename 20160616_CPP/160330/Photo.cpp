#include "Photo.h"


CPhoto::CPhoto(void)
{
	sampleImage=cvLoadImage("lena.jpg",1);
	srcImage=cvarrToMat(sampleImage);
	grayImage=imread("lena.jpg",0);
	//srcImage=imread("lena.JPG",1); 
	cvtColor(srcImage,hsvImage,COLOR_BGR2HSV); //rgb->hsv
	HSV_photo(hsvImage);
	RGB_photo(srcImage);
	Edges=EDGES_photo(srcImage);
}

CPhoto::CPhoto(Mat Image)
{
	srcImage=Image;
	cvtColor(srcImage,hsvImage,COLOR_BGR2HSV); //rgb->hsv
	HSV_photo(hsvImage);
	RGB_photo(srcImage);
}

CPhoto::~CPhoto(void)
{
}

void CPhoto:: HSV_photo(Mat Image){
	split(Image,HSVplanes_1); //hsv채널 분리하여 vector<Mat> HSVplanes_1에 저장
	H=(Mat_<uchar>)HSVplanes_1[0]; //색상
	S=(Mat_<uchar>)HSVplanes_1[1]; //채도
	V=(Mat_<uchar>)HSVplanes_1[2]; //명도
	equalizeHist(H,H_hist);
	equalizeHist(S,S_hist);
	equalizeHist(V,V_hist);
}
void CPhoto:: RGB_photo(Mat Image){
	split(Image,RGBplanes_1); //rgb채널 분리하여 vector<Mat> RGBplanes_1에 저장
	B=(Mat_<uchar>)RGBplanes_1[0]; //BLUE
	G=(Mat_<uchar>)RGBplanes_1[1]; //GREEN
	R=(Mat_<uchar>)RGBplanes_1[2]; //RED
	equalizeHist(B,B_hist);
	equalizeHist(G,G_hist);
	equalizeHist(R,R_hist);
}

Mat CPhoto:: EDGES_photo(Mat Image){
	Mat edges;
	Canny(Image,edges,1,10);
	return edges;
}


vector<Mat> CPhoto::ch_merge(Mat_<uchar> ch1,Mat_<uchar> ch2,Mat_<uchar> ch3){
	vector<Mat> CH;
	CH.push_back(ch1);
	CH.push_back(ch2);
	CH.push_back(ch3);
	return CH;
}
void CPhoto:: save_photo(Mat Image,char *name){
	vector<int> params;
	params.push_back(IMWRITE_JPEG_QUALITY);
	params.push_back(100);
	imwrite(name,Image,params);
}