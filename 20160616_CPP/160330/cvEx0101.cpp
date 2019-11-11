#include "header.h"
#include "Main.h"
#include "MagneticLasso.h"
/*
출력가능 이미지
srcImage
hsvImage
hist_hsvImage_rgb
H,S,V
H_hist
S_hist
V_hist
HSV_change
R,G,B
RGB_change
*/
Mat image;
Mat image2;

Mat Lasso_image;
Mat Lasso_image_color;
vector<vector<Point>> contour(1,vector<Point>());

void Progress();
void onMouse(int event, int x, int y, int flags, void* param);


int main(){

	CMain Main;
	Progress();
	
	int x=0; //cols
	int y=0; //rows;
	int x2;
	int y2;
	double flag; //내부 1,외부 -1,다각형위 0

	for(y=0;y<Lasso_image.rows;y++){
		for(x=0;x<Lasso_image.cols;x++){
			flag=pointPolygonTest(contour[0],Point(y,x),false);
			if(flag==1){
				x2=x;
				y2=y;
				break;
			}
		}
			if(flag==1)
			break;
	}
	cout<<"x:"<<x2;
	cout<<"y:"<<y2;
	Main.Initialize(Lasso_image,x2,y2);
	Main.Progress();
	return 0;
}

void onMouse(int event, int x, int y, int flags, void* param) {
	/*
	Mat *pMat=(Mat*)param;
	Mat Weight_image=Mat(*pMat);
	image2.create(Weight_image.rows,Weight_image.cols,CV_8UC3);
	//Mat_<uchar> image2;
	//image2=(Mat_<uchar>)image;
	switch(event)
	{
	case EVENT_MOUSEMOVE:
		if(flags&EVENT_FLAG_SHIFTKEY)
			rectangle(image,Point(x-5,y-5),Point(x+5,y+5),Scalar(255,0,0));
		else if(flags&EVENT_FLAG_LBUTTON){
			circle(image,Point(x,y),10,Scalar(0,0,255),-1);
			circle(image2,Point(x,y),10,Scalar(0,0,255),-1);
		}
		break;
	case EVENT_RBUTTONDOWN:
		circle(image,Point(x,y),5,Scalar(255,0,0),5);
		break;
	case EVENT_LBUTTONDBLCLK:
		image=Scalar(255,255,255);
		break;
	}
	imshow("mouseImage",image);
	imshow("testImage",image2);
	*/

	Mat *pMat=(Mat*)param;
	Mat Weight_image=Mat(*pMat);
	
	Mat Roi(3,3,CV_8U);
	Mat_<uchar> Roi_=(Mat_<uchar>)Roi;
	Mat_<uchar> Weight_image_=(Mat_<uchar>)Weight_image;
	double minVal;
	double maxVal;
	Point minLoc;
	Point maxLoc;
	Point drawLoc;

	//cout<<"cols"<<Weight_image.cols;
	//cout<<"rows"<<Weight_image.rows;
	switch(event)
	{
	case EVENT_MOUSEMOVE:
		if(flags&EVENT_FLAG_SHIFTKEY)
			rectangle(Weight_image,Point(x-5,y-5),Point(x+5,y+5),Scalar(255,0,0));
		else if(flags&EVENT_FLAG_LBUTTON){
			
			if(x!=0 && y!=0 && x!=Weight_image.cols-1 && y!=Weight_image.rows-1){
			Roi_(1,1)=Weight_image_(y,x);
			Roi_(0,1)=Weight_image_(y-1,x);
			Roi_(2,1)=Weight_image_(y+1,x);
			Roi_(1,2)=Weight_image_(y,x+1);
			Roi_(0,2)=Weight_image_(y-1,x+1);
			Roi_(2,2)=Weight_image_(y+1,x+1);
			Roi_(1,0)=Weight_image_(y,x-1);
			Roi_(0,0)=Weight_image_(y-1,x-1);
			Roi_(2,0)=Weight_image_(y+1,x-1);
			minMaxLoc(Roi_,&minVal,&maxVal,&minLoc,&maxLoc);
			}
			
			if(x!=0 && y!=0 && y!=Weight_image.rows-1 && x!=Weight_image.cols-1){
			if(maxLoc==Point(1,1))
				drawLoc=Point(x,y);
			else if(maxLoc==Point(0,1))
				drawLoc=Point(x-1,y);
			else if(maxLoc==Point(2,1))
				drawLoc=Point(x+1,y);
			else if(maxLoc==Point(1,2))
				drawLoc=Point(x,y+1);
			else if(maxLoc==Point(0,2))
				drawLoc=Point(x-1,y+1);
			else if(maxLoc==Point(2,2))
				drawLoc=Point(x+1,y+1);
			else if(maxLoc==Point(1,0))
				drawLoc=Point(x,y-1);
			else if(maxLoc==Point(0,0))
				drawLoc=Point(x-1,y-1);
			else if(maxLoc==Point(2,0))
				drawLoc=Point(x+1,y-1);
			}
			cout<<"원래 x,y:"<<x<<","<<y<<endl;
			cout<<"drawLoc x,y:"<<drawLoc<<endl;
			cout<<"Weight_image_"<<(int)Weight_image_(drawLoc)<<endl;
			
			if(Weight_image_(drawLoc)>20){
				contour[0].push_back(drawLoc);
				circle(Lasso_image,drawLoc,1,Scalar(255,255,255),-1);
				circle(Lasso_image_color,drawLoc,1,Scalar(255,255,255),-1);
			}
		}
		break;
	case EVENT_RBUTTONDOWN:
		circle(Weight_image,Point(x,y),5,Scalar(255,0,0),5);
		break;
	case EVENT_LBUTTONDBLCLK:
		Weight_image=Scalar(255,255,255);
		break;
	}
	imshow("Lasso_image",Lasso_image);
	imshow("Lasso_image_color",Lasso_image_color);

	const cv::Point *pts1=(const cv::Point*)Mat(contour[0]).data;
	const Point *polygon[1]={pts1};
	int npts[1]={contour[0].size()};
	polylines(Lasso_image,polygon,npts,1,false,Scalar(255,255,255)); //segment_image에 그리기
	polylines(Lasso_image_color,polygon,npts,1,false,Scalar(255,255,255));
	//imshow("Weight_image",Weight_image);
}

void Progress()
{
	Mat dstImage=imread("lena.jpg",IMREAD_GRAYSCALE);
	Mat colordstImage=imread("lena.jpg",1);
	Mat dstImage2=dstImage.clone();
	Mat edge;
	Mat WeightPixel;
	CMagneticLasso lasso;
	
	Canny(dstImage2,edge,50,200);
	lasso.WeightPixel.create(edge.rows,edge.cols,CV_8U);
	
	Sobel(dstImage,lasso.dstGx,lasso.ddepth,1,0,lasso.ksize);
	Sobel(dstImage,lasso.dstGy,lasso.ddepth,0,1,lasso.ksize);

	normalize(abs(lasso.dstGx),lasso.dstImageGx,0,255,NORM_MINMAX,lasso.dtype);
	normalize(abs(lasso.dstGy),lasso.dstImageGy,0,255,NORM_MINMAX,lasso.dtype);

	magnitude(lasso.dstGx,lasso.dstGy,lasso.dstMag);
	normalize(lasso.dstMag,lasso.dstImageGxy,0,255,NORM_MINMAX,lasso.dtype);

	Lasso_image.create(lasso.dstImageGxy.rows,lasso.dstImageGxy.cols,CV_8U);
	Lasso_image_color=colordstImage.clone();

	//lasso.Segment_image.create(lasso.dstImageGxy.rows,lasso.dstImageGxy.cols,CV_8U);
	//lasso.dstImageGxy_=(Mat_<uchar>)lasso.dstImageGxy;
	Mat_<uchar> dstImageGxy_=(Mat_<uchar>)lasso.dstImageGxy;
	WeightPixel=lasso.Weight_Image(dstImageGxy_,edge);
	
	imshow("scissorImage",colordstImage);
	setMouseCallback("scissorImage",onMouse,(void*)&WeightPixel);
	waitKey();
	destroyWindow("scissorImage");
	destroyWindow("Lasso_image");
	destroyWindow("Lasso_image_color");
	imshow("dstImage",colordstImage);
}