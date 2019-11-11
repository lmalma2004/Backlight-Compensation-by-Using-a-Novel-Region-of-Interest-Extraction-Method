#pragma once
#include "Define.h"


//class CColor;
//class CBinary;
//class CSkin;
//class CTexture;
class CPhoto;
class CSkill;
class CMain
{
public:
	CMain(void);
	~CMain(void);

private:
	CPhoto*		m_pPhoto;
	CSkill*		m_pSkill;
	//CTexture*	m_pTxt;
	//CSkin*		m_pSkin;
	//CBinary*	m_pBinary;
	//CColor*		m_pColor;
	int x;
	int y;
public:
	void Initialize(Mat image,int cols,int rows);
	void Progress();
	void onMouse(int event, int x, int y, int flags, void* param);
	void Insert_Merge();
	void Edge();
	void Mask();
	
};

