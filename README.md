# Backlight-Compensation-by-Using-a-Novel-Region-of-Interest-Extraction-Method
Backlight Compensation and Region of Interest Extraction Project Using OpenCV

▶20160616_CPP<br>

※사용환경<br>
(비쥬얼 스튜디오 2010)<br>
(opencv 2.4.10 기준으로 구현 됨)<br>

※사용법
1. opencv 설치 후 빌드
2. 원하는 영역을 선택 후 차례대로 엔터
3. 정해준 보정값으로 보정되어 사진이 출력됨

※클래스설명<br>
◆Main.cpp : 메인클래스<br>
void CMain::Initialize(Mat image,int cols,int rows);<br>
초기화 함수<br>
void Progress();<br>
실제 진행함수<br>
void onMouse(int event, int x, int y, int flags, void* param);<br>
마우스 클릭 콜벡함수<br>
void Insert_Merge();<br>
채널 삽입 및 합침<br>
void Edge();<br>
선택영역 엣지로 만들기<br>
void Mask();<br>
선택영역 마스크로 만들기<br>
<br>
◆Photo.cpp : 사진클래스<br>
void HSV_photo(Mat Image);<br>
HSV채널로 분리시켜 만들어줌<br>
void RGB_photo(Mat Image);<br>
RGB채널로 분리시켜 만들어줌<br>
Mat EDGES_photo(Mat Image);<br>
엣지로 만들어줌<br>
vector<Mat> ch_merge(Mat_<uchar> ch1,Mat_<uchar> ch2,Mat_<uchar> ch3); <br>
채널 합침<br>
void save_photo(Mat Image,char *name);<br>
사진저장<br>
<br>
◆Skill.cpp : 스킬클래스<br>
Mat_<uchar> Contrast(Mat_<uchar> Image,double contrast);<br>
대비 조정<br>
Mat_<uchar> Brightress(Mat_<uchar> Image,int brightress);<br>
밝기 조정<br>
void FloodFill_photo(Mat Edges,int cols,int rows);<br>
영역 채우기<br>
void DrawContours_photo(Mat Edges);<br>
윤곽선 그리기<br>
IplImage* MaskImage(IplImage *original,char *name);<br>
마스크 이미지 만들기<br>
void Saliency_Image(Mat Image,Mat grayImage,Mat_<uchar> H,Mat_<uchar> S,Mat_<uchar> V);<br>
Saliency_image 픽셀 계산<br>
void Average_intensity(Mat_<uchar> H,Mat_<uchar> S,Mat_<uchar> V);<br>
Average_intensity 픽셀 계산<br>
void New_Average_intensity();<br>
New_Average_intensity 픽셀 계산<br>
void save_photo(Mat Image,char *name);<br>
사진 저장<br>
Mat_<uchar> Offset();<br>
Offset 픽셀 계산<br>
<br>
◆Backlight <br>
-Texture.cpp : 질감추출<br>
-Skin.cpp : 스킨추출 (사람피부)<br>
-Color.cpp : 색감추출<br>
-Binary.cpp : 밝기 2진화<br>
<br>



-------------------------------------------------------------------------------------------------
<br>
▶20160616_JAVA<br>

※사용환경<br>

(안드로이드 : minSdkVersion : 15, compileSdkVersion : 23, buildToolsVersion : 23.0.3)<br>
(android studio 2.1.1로 개발)<br>
(android4OpenCV 3.0 기준으로 구현)<br>
<br>
빌드는 sdk:15로 되지만 실제 사용하려면 sdk:19가 최소 sdk 버전임<br>
opencv 3.0이 최소 19부터 지원함<br>
<br>
※사용법<br>
1. android4OpenCV 3.0 설치 후 빌드<br>


※클래스 & 액티비티 설명 ( ◆:클래스 , ◇:액티비티) <br>
◆CPhoto : C에서의 구현과 기능 동일<br>
◆MagneticLasso : MageticLasso 계산 이미지 클래스<br>
◇MainActivity : 메인 액티비티 ( 사진 아이콘을 클릭시 갤러리로 이동하여 사진선택 후 등록 됨)<br>
◇Savemode : 저장 액티비티<br>
◇Selectmode : 영역선택 부분 액티비티<br>
◇Showapp : 스플래쉬 액티비티 (앱 처음소개)<br>
◇SkillActivity : 사진 수정하는 액티비티 (SurfaceView로 동작 하기때문에 task 관리 중요)<br>
