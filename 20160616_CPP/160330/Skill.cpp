#include "SKill.h"
#include "Texture.h"
#include "Skin.h"
#include "Color.h"
#include "Binary.h"

CSkill::CSkill(void)
{
	contrast=1.0;
	brightress=0;
	mode=RETR_EXTERNAL;
	method=CHAIN_APPROX_NONE;
}

CSkill::~CSkill(void)
{
}

void CSkill:: Saliency_Image(Mat Image,Mat grayImage,Mat_<uchar> H,Mat_<uchar> S,Mat_<uchar> V){
	CTexture texture;
	CSkin skin;
	CColor color;
	CBinary binary;


	texture_pixel			=	texture.Progress(grayImage);
	skin_pixel				=	skin.Progress(Image);
	binary_pixel			=	binary.Progress(grayImage);
	color_prominency_pixel	=	color.Progress(H,S);
	luminance_pixel			=	V;

	saliency_pixel.create(skin_pixel.rows,skin_pixel.cols,CV_8UC3);
	for(int y=0;y<skin_pixel.rows;y++){
		for(int x=0;x<skin_pixel.cols;x++){
			saliency_pixel.at<Vec3b>(y,x)[0]=saturate_cast<uchar>((skin_pixel.at<uchar>(y,x)+texture.ch1_binary.at<uchar>(y,x)+binary_pixel.at<uchar>(y,x)+color_prominency_pixel.at<uchar>(y,x))*0.25);
			saliency_pixel.at<Vec3b>(y,x)[1]=saturate_cast<uchar>((skin_pixel.at<uchar>(y,x)+texture.ch2_binary.at<uchar>(y,x)+binary_pixel.at<uchar>(y,x)+color_prominency_pixel.at<uchar>(y,x))*0.25);
			saliency_pixel.at<Vec3b>(y,x)[2]=saturate_cast<uchar>((skin_pixel.at<uchar>(y,x)+texture.ch3_binary.at<uchar>(y,x)+binary_pixel.at<uchar>(y,x)+color_prominency_pixel.at<uchar>(y,x))*0.25);
		}
	}
	cvtColor(saliency_pixel,saliency_pixel_gray,COLOR_BGR2GRAY);
	
	save_photo(texture_pixel,"texture_pixel.jpg");
	save_photo(color_prominency_pixel,"color_prominency_pixel.jpg");
	save_photo(saliency_pixel_gray,"saliency_pixel_gray.jpg");
	//imshow("saliencygray",saliency_pixel_gray);
	//imshow("saliency_pixel",saliency_pixel);
}

void CSkill::Average_intensity(Mat_<uchar> H,Mat_<uchar> S,Mat_<uchar> V)
{
	double SpYp = 0.0;
	double Sp = 0.0;
	m_Ym = 0.0;
	for(int i=0; i < 3; i++){
		for(int y=0;y<skin_pixel.rows;y++){
			for(int x=0;x<skin_pixel.cols;x++){
				SpYp+=saliency_pixel.at<Vec3b>(y,x)[i]*V.at<uchar>(y,x);
				Sp+=saliency_pixel.at<Vec3b>(y,x)[i];
				average_intensity[i]= SpYp/Sp;
			}
		}
		SpYp=0.0;
		Sp=0.0;
		m_Ym += average_intensity[i]/3.0;
	}
	
	cout <<"average1:"<< average_intensity[0] << endl;
	cout <<"average2:"<< average_intensity[1] << endl;
	cout <<"average3:"<< average_intensity[2] << endl;
	cout <<"Ym:"<<m_Ym << endl;
	waitKey(0);

}

void CSkill::New_Average_intensity()
{
	new_Average_intensity.create(luminance_pixel.rows,luminance_pixel.cols,CV_8U);
	for(int y=0;y<luminance_pixel.rows;y++){
			for(int x=0;x<luminance_pixel.cols;x++){
				if(luminance_pixel.at<uchar>(y,x) <= 128)
				{
					new_Average_intensity.at<uchar>(y,x) = 
						saturate_cast<uchar>(pow((luminance_pixel.at<uchar>(y,x) + (128 - m_Ym)),2)/128);
				}
				else 
				{
					new_Average_intensity.at<uchar>(y,x) = 
						saturate_cast<uchar>(255 - (pow((luminance_pixel.at<uchar>(y,x) + (128 - m_Ym) - 255), 2)/127));
				}
		}
	}
	
	save_photo(new_Average_intensity,"new_Average_intensity.jpg");
	imshow("new_Average_intensity",new_Average_intensity);
	waitKey();

}

Mat_<uchar> CSkill::Offset()
{
	m_Op = 0.0;
	new_luminance_pixel.create(luminance_pixel.rows,luminance_pixel.cols,CV_8U);
	for(int y=0;y<new_Average_intensity.rows;y++){
			for(int x=0;x<new_Average_intensity.cols;x++){
				m_Op =saturate_cast<uchar>( (saliency_pixel_gray.at<uchar>(y,x)) * (new_Average_intensity.at<uchar>(y,x) - luminance_pixel.at<uchar>(y,x))); 
				new_luminance_pixel.at<uchar>(y,x) = saturate_cast<uchar>(luminance_pixel.at<uchar>(y,x) + m_Op);
		}
	}
	//imshow("new_luminance_pixel",new_luminance_pixel);
	//waitKey();
	new_luminance_pixel_ = (Mat_<uchar>)new_luminance_pixel;
	save_photo(new_luminance_pixel,"new_luminance_pixel.jpg");
	imshow("new_luminance_pixel_",new_luminance_pixel_);
	return new_luminance_pixel_;
}

void CSkill::DrawContours_photo(Mat Edges){  //외곽선 추출 및 그리기
	findContours(Edges,contours,noArray(),mode,method);
	drawContours(Edges,contours,-1,Scalar(255),2);
}

void CSkill:: FloodFill_photo(Mat Edges,int cols,int rows){   //마스킹이미지만들기
	in=new IplImage(Edges);
	int connectivity = 4;
	int new_mask_val = 100;
	int flags = connectivity + (new_mask_val << 8+CV_FLOODFILL_FIXED_RANGE ) ;
		
	cvFloodFill(
		in,
		cvPoint(rows+5,cols+5), //200,400
		cvScalar(255),
		cvScalarAll(20.0),
		cvScalarAll(20.0),
		NULL,
		flags,
		NULL
	);
}

Mat_<uchar> CSkill::Contrast(Mat_<uchar> Image,double contrast){
	if(Image.channels()==1){
	for(int y=0;y<Image.rows;y++) //열  
		for(int x=0;x<Image.cols;x++){ //행
			Image(y,x)=saturate_cast<uchar>(Image(y,x)*contrast); 
		}
	}
	else if(Image.channels()==3){
		for(int y=0;y<Image.rows;y++) //열  
		for(int x=0;x<Image.cols;x++){ //행
			Image.at<Vec3b>(y,x)[0]=saturate_cast<uchar>(Image.at<Vec3b>(y,x)[0]*contrast);
			Image.at<Vec3b>(y,x)[1]=saturate_cast<uchar>(Image.at<Vec3b>(y,x)[1]*contrast);
			Image.at<Vec3b>(y,x)[2]=saturate_cast<uchar>(Image.at<Vec3b>(y,x)[2]*contrast);
		}
	}
		return Image;
}
Mat_<uchar> CSkill::Brightress(Mat_<uchar> Image,int brightress){
	if(Image.channels()==1){
	for(int y=0;y<Image.rows;y++) //열  
		for(int x=0;x<Image.cols;x++){ //행
			Image(y,x)=saturate_cast<uchar>(Image(y,x)+brightress); 
		}
	}
	else if(Image.channels()==3){
		for(int y=0;y<Image.rows;y++) //열  
		for(int x=0;x<Image.cols;x++){ //행
			Image.at<Vec3b>(y,x)[0]=saturate_cast<uchar>(Image.at<Vec3b>(y,x)[0]+brightress); 
			Image.at<Vec3b>(y,x)[1]=saturate_cast<uchar>(Image.at<Vec3b>(y,x)[1]+brightress); 
			Image.at<Vec3b>(y,x)[2]=saturate_cast<uchar>(Image.at<Vec3b>(y,x)[2]+brightress); 
		}
	}
		return Image;
}

IplImage* CSkill::MaskImage(IplImage *original,char *name){  //마스킹 (2번째 매개변수는 마스크이미지 파일이름)
	IplImage *mask=cvLoadImage(name,1);
	IplImage *mask_Image=cvCreateImage(cvGetSize(mask),IPL_DEPTH_8U,3); //마스크 이미지
	cvAnd(original,mask,mask_Image,0);
	//Mat MaskImage=cvarrToMat(soso3,true);
	cvReleaseImage(&mask);
	return mask_Image;
}

void CSkill:: save_photo(Mat Image,char *name){
	vector<int> params;
	params.push_back(IMWRITE_JPEG_QUALITY);
	params.push_back(100);
	imwrite(name,Image,params);
}
