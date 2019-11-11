#include "Main.h"
#include "Photo.h"
#include "SKill.h"
#include "MagneticLasso.h"
//#include "Texture.h"
//#include "Skin.h"
//#include "Binary.h"
//#include "Color.h"

CMain::CMain(void)
	:m_pPhoto(NULL)
	,m_pSkill(NULL)
	//,m_pTxt(NULL)
	//,m_pSkin(NULL)
	//,m_pBinary(NULL)
	//,m_pColor(NULL)
{
}

CMain::~CMain(void)
{
	//if(m_pPhoto != NULL)
	//{
		delete m_pPhoto;
		m_pPhoto = NULL;
	//}
	//if(m_pSkill != NULL)
	//{
		delete m_pSkill;
		m_pSkill = NULL;
	//}
	//if(m_pTxt != NULL)
	//{
	//	delete m_pTxt;
	//	m_pTxt = NULL;
	//}
	//if(m_pSkin !=NULL)
	//{
	//	delete m_pSkin;
	//	m_pSkin =NULL;
	//}
	//if(m_pBinary !=NULL)
	//{
	//	delete m_pBinary;
	//	m_pBinary=NULL;
	//}
	//if(m_pColor	!=NULL)
	//{
	//	delete m_pColor;
	//	m_pColor=NULL;
	//}
}

void CMain::Initialize(Mat image,int cols,int rows)
{
		if(m_pPhoto == NULL)
				m_pPhoto = new CPhoto;
		if(m_pSkill == NULL)
				m_pSkill = new CSkill;
	//	if(m_pSkin == NULL)
	//			m_pSkin = new CSkin;
	//	if(m_pTxt == NULL)
	//			m_pTxt	 = new CTexture;
	//	if(m_pBinary==NULL)
	//			m_pBinary	=new CBinary;
	//	if(m_pColor==NULL)
	//			m_pColor	=new CColor;
		x=cols;
		y=rows;

		m_pPhoto->mouseImage = image.clone();
		//imshow("mouseImage",m_pPhoto->mouseImage);
		
}


void CMain::Progress()
{
	Insert_Merge();
	Edge();
	Mask();	
	Mat_<uchar> new_V;
	m_pSkill->Saliency_Image(m_pPhoto->srcImage,m_pPhoto->grayImage,m_pPhoto->H,m_pPhoto->S,m_pPhoto->V);
	m_pSkill->Average_intensity(m_pPhoto->H,m_pPhoto->S,m_pPhoto->V);
	m_pSkill->New_Average_intensity();
	new_V = m_pSkill->Offset();
	//Mat_<uchar> vv;
	//vv=(Mat_<uchar>)
	m_pPhoto->Test = m_pPhoto->ch_merge(m_pPhoto->H,m_pPhoto->S,new_V);
	merge(m_pPhoto->Test,m_pPhoto->HSV_Backlight);
	cvtColor(m_pPhoto->HSV_Backlight,m_pPhoto->RGB_Backlight,COLOR_HSV2BGR);
	m_pPhoto->save_photo(m_pPhoto->RGB_Backlight,"rgb_backlight.jpg");
	imshow("please",m_pPhoto->RGB_Backlight);
	waitKey();
	//m_pSkin->Progress();
	//m_pBinary->Progress();
	//m_pTxt->Progress();
	
}

void CMain::Insert_Merge()//���� �� ��ħ
{
	m_pPhoto->G =			m_pSkill->Contrast(m_pPhoto->G,1.2);
	m_pPhoto->HSVplanes_2 =	m_pPhoto->ch_merge(m_pPhoto->H,m_pPhoto->S,m_pPhoto->V);			//���Ϳ� ���ϴ� ä�� ����
	
	merge(m_pPhoto->HSVplanes_2,m_pPhoto->HSV_change);										//3ä�η� ��ħ
	//m_pPhoto->RGBplanes_2=m_pPhoto->ch_merge(m_pPhoto->B,m_pPhoto->G,m_pPhoto->R);			//���Ϳ� ���ϴ� ä�� ����
	//merge(m_pPhoto->RGBplanes_2,m_pPhoto->RGB_change);										//3ä�η� ��ħ
	//imshow("hHSV",m_pPhoto->HSV_change);
	//imshow("hhhh",m_pPhoto->hsvImage);
	
	//imshow("srcImage",m_pPhoto->srcImage);													//���
	//cvWaitKey(0);
}

void CMain::Edge()
{
	Mat m_test = m_pPhoto->EDGES_photo(m_pPhoto->mouseImage);    //m_test�� �������ÿ����׸�
	m_pPhoto->mouseImage = m_test.clone();

	m_pSkill->DrawContours_photo(m_pPhoto->mouseImage);										//���� : ������ ����
	m_pSkill->FloodFill_photo(m_pPhoto->mouseImage,x,y);											//���� : ���� ä���		
	m_pPhoto->save_photo(m_pPhoto->mouseImage,"EdgesImage.jpg");								//���� : ����
	//imshow("Edges",m_pPhoto->mouseImage);			//										//���� : ���	
	//cvWaitKey(0);
}

void CMain::Mask()
{
	IplImage* MaskImage_Ipl =	m_pSkill->MaskImage(m_pPhoto->sampleImage,"EdgesImage.jpg");		//����ũ 
	Mat		  MaskImage_Mat=	cvarrToMat(MaskImage_Ipl);										 		
	MaskImage_Mat=m_pSkill->Contrast(MaskImage_Mat,1.9);											//����Ű : ����ŷ�� �̹��� �������
	
	m_pPhoto->save_photo(m_pPhoto->mouseImage,"maskImg.jpg");	
	m_pPhoto->save_photo(MaskImage_Ipl,"maskingDone.jpg");	
	
	//Mat_<uchar> MaskImage_Mat_=(Mat_<uchar>)MaskImage_Mat;
	Mat_<uchar> srcImage_=(Mat_<uchar>)m_pPhoto->srcImage;

	Mat imageROI=srcImage_(Rect(0,0,MaskImage_Mat.cols,MaskImage_Mat.rows));
	addWeighted(imageROI,1.0,MaskImage_Mat,0.3,0.,imageROI);
	
	imshow("Edges",m_pPhoto->mouseImage);
	cvWaitKey(0);
	
	cvNamedWindow("mask",0);																												
	cvShowImage("mask",(CvArr*)MaskImage_Ipl);	//												//����ũ : ���
	cvWaitKey(0);
	cvDestroyWindow("mask");
	m_pPhoto->save_photo(m_pPhoto->srcImage,"masked_img.jpg");
	imshow("srcImage2",m_pPhoto->srcImage);
	cvWaitKey(0);
	cvReleaseImage(&MaskImage_Ipl);
}
