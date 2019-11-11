#include "MagneticLasso.h"


CMagneticLasso::CMagneticLasso(void)
{
	ksize=3;
	ddepth=CV_32F;
	dtype=CV_8U;
	
}


CMagneticLasso::~CMagneticLasso(void)
{
}

Mat CMagneticLasso::Weight_Image(Mat_<uchar> Image,Mat edge)
{
	Mat Roi;
	Mat_<uchar> Roi_;
	Mat output;
	Roi.create(3,3,CV_8U);
	Roi_=(Mat_<uchar>)Roi;

	output.create(edge.rows,edge.cols,CV_8U);

	for(int y=0;y<Image.rows;y++) //열                
		for(int x=0;x<Image.cols;x++){ //행
			if(x!=0 && y!=0 && x!=Image.cols-1 && y!=Image.rows-1){
			Roi_(1,1)=Image(y,x);
			Roi_(0,1)=Image(y-1,x);
			Roi_(2,1)=Image(y+1,x);
			Roi_(1,2)=Image(y,x+1);
			Roi_(0,2)=Image(y-1,x+1);
			Roi_(2,2)=Image(y+1,x+1);
			Roi_(1,0)=Image(y,x-1);
			Roi_(0,0)=Image(y-1,x-1);
			Roi_(2,0)=Image(y+1,x-1);
			}
			minMaxLoc(Roi_,&minVal,&maxVal,&minLoc,&maxLoc);
			gradient=1-((double)Image(y,x)/maxVal);
			output.at<uchar>(y,x)=saturate_cast<uchar>(0.5*edge.at<uchar>(y,x)+0.5*gradient); //가중치는 0.5를 줌
		}
	return output;
}