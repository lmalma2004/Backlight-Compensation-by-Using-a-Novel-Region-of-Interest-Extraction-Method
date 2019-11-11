#include "Texture.h"


CTexture::CTexture(void)
{
}


CTexture::~CTexture(void)
{
}

void CTexture:: save_photo(Mat Image,char *name){
	vector<int> params;
	params.push_back(IMWRITE_JPEG_QUALITY);
	params.push_back(100);
	imwrite(name,Image,params);
}

Mat CTexture::Progress(Mat grayImage){

	//Mat srcImg = imread("lena.jpg",0); 
	m_gaborImg = GaborFilter(grayImage);
	m_featImg = FeatureDetect(m_gaborImg);
	m_txtImg = KmeansClustering(m_featImg);
	save_photo(m_gaborImg,"m_gaborImg.jpg");
	save_photo(m_featImg,"m_featImg.jpg");
	save_photo(m_txtImg,"m_txtImg.jpg");
	cvtColor(m_txtImg, m_gray_txtImg,COLOR_RGB2GRAY);
	Divide_texture(m_txtImg);

	th[0]=threshold(ch1,ch1_binary,100,3,THRESH_BINARY+THRESH_OTSU);
	th[1]=threshold(ch2,ch2_binary,100,3,THRESH_BINARY+THRESH_OTSU);
	th[2]=threshold(ch3,ch3_binary,100,3,THRESH_BINARY+THRESH_OTSU);
	return m_txtImg;
	//waitKey();

}
void CTexture::Divide_texture(Mat txtImage){
	split(txtImage,m_txtImg_ch); //hsv채널 분리하여 vector<Mat> HSVplanes_1에 저장
	ch1=(Mat_<uchar>)m_txtImg_ch[0]; //색상
	ch2=(Mat_<uchar>)m_txtImg_ch[1]; //채도
	ch3=(Mat_<uchar>)m_txtImg_ch[2]; //명도
}

Mat CTexture::GaborFilter(Mat srcImg)
{
	Mat dest;
	Mat src_f;
	Mat gaborImg;
	srcImg.convertTo(src_f,CV_32F);

	int kernel_size = 31;
	double sig = 1, th = 0, lm = 1.0, gm = 0.02, ps = 0;
	cv::Mat kernel = cv::getGaborKernel(cv::Size(kernel_size,kernel_size), sig, th, lm, gm, ps);
	cv::filter2D(src_f, dest, CV_32F, kernel);

	cerr << dest(Rect(30,30,10,10)) << endl; // peek into the data
	
	dest.convertTo(gaborImg,CV_8U,1.0/255.0);     // move to proper[0..255] range to show it
	
	//imshow("gabor Filter Image",gaborImg);
	return gaborImg;
}

 Mat CTexture::FeatureDetect(Mat srcImg)
{
	GaussianBlur(srcImg,srcImg, Size(5,5),0.0);

	vector<vector<Point>> msers;
	MSER(10)(srcImg,msers);
	vector<Rect> bboxes;

	Mat featImg(srcImg.size(), CV_8UC3);
	cvtColor(srcImg,featImg,COLOR_GRAY2BGR);
	
	for(int k = 0; k<msers.size(); k++)
	{
		RotatedRect box = fitEllipse(msers[k]);
		ellipse(featImg,box,Scalar(rand()%256,rand()%256,rand()%256),2);
	}

	//imshow("feature Detected Image",featImg);
	return featImg;
}
Mat CTexture::KmeansClustering(Mat srcImage)
{
	int K =4;
	Mat colorTable(K, 1, CV_8UC3);
	Vec3b color;
	for(int k = 0; k < K; k++)
	{
		color[0] = rand()&180+50;
		color[1] = rand()&180+50;
		color[2] = rand()&180+50;
		colorTable.at<Vec3b>(k,0) = color;
	}
	
	Mat txtImage(srcImage.size(), srcImage.type());

	int attempts = 1;
	int flags = KMEANS_RANDOM_CENTERS;
	TermCriteria criteria(TermCriteria::COUNT+TermCriteria::EPS, 10, 1.0);
	
	Mat samples = srcImage.reshape(3, srcImage.rows*srcImage.cols);
	samples.convertTo(samples,CV_32FC3);
	
	Mat labels;
	kmeans(samples, K, labels, criteria, attempts, flags);
	
	for(int y = 0, k = 0; y < srcImage.rows; y++)
		for(int x = 0; x < srcImage.cols; x++, k++)
		{
			int idx = labels.at<int>(k,0);
			color = colorTable.at<Vec3b>(idx,0);
			txtImage.at<Vec3b>(y,x) = color;
		}
	//imshow("Image of texture", txtImage);
	return txtImage;
}